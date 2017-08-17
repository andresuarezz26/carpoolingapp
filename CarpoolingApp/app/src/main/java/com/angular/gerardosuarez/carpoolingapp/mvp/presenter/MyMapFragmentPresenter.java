package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyMapView;
import com.angular.gerardosuarez.carpoolingapp.service.DriverMapService;
import com.angular.gerardosuarez.carpoolingapp.service.PassengerMapService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.NetworkUtils;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class MyMapFragmentPresenter extends BaseFragmentPresenter {

    private final static String ROLE_DRIVER = "driver";
    private final static String ROLE_PASSEGNER = "passenger";
    private static final int FIRST_POSITION = 0;

    private MyMapView view;
    private DriverMapService driverMapService;
    private PassengerMapService passengerMapService;
    private RolePreference rolePreference;
    private UserService userService;

    private ValueEventListener quotaPassengerListener;
    private String currentRole;

    private boolean mapWasTouched = false;
    private boolean wasDateSelected = false;
    private boolean wasTimeSelected = false;
    private String currentAddress;
    private LatLng currentCoordinates;

    private LinkedHashMap<String, PassengerBooking> passengerBookingMap = new LinkedHashMap<>();

    public MyMapFragmentPresenter(MyMapView view,
                                  DriverMapService driverMapService,
                                  PassengerMapService passengerMapService,
                                  RolePreference rolePreference,
                                  MapPreference mapPreference,
                                  UserService userService) {
        super(mapPreference, view);
        this.view = view;
        this.driverMapService = driverMapService;
        this.passengerMapService = passengerMapService;
        this.rolePreference = rolePreference;
        this.userService = userService;
    }

    public void init() {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        if (activity.getFragmentManager() == null) {
            return;
        }
        requestPermissions(activity);
        setListeners();

    }

    public void setListeners() {
        view.setListeners();
    }

    public void removeListeners() {
        view.removeListeners();
    }


    public void unsubscribeFirebaseListener() {
        if (quotaPassengerListener != null) {
            databaseRef.removeEventListener(quotaPassengerListener);
        }
    }

    //Driver Services
    private void getQuotas() {
        if (getMapPreferences()) {
            getQuotas(community, fromOrTo, date, hour);
        }
    }

    private void getQuotas(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour) {
        quotaPassengerListener = driverMapService.getQuotasPerCommunityOriginDateAndHour(comunity, origin, date, hour).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getKey();
                            try {
                                view.clearMap();
                                PassengerBooking passengerBooking = snapshot.getValue(PassengerBooking.class);
                                if (!PassengerInfoRequest.STATUS_ACCEPTED.equalsIgnoreCase(passengerBooking.status)) {
                                    setPassengerBookingAditionalInfo(snapshot.getKey(), passengerBooking);
                                }

                            } catch (DatabaseException e) {
                                Timber.e(e.getMessage(), e);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.toString(), databaseError);
                    }
                });
    }

    private void setPassengerBookingAditionalInfo(@NonNull final String uid, @NonNull final PassengerBooking passengerBooking) {
        userService.getUserByUid(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        passengerBooking.setPhone(user.phone);
                        passengerBooking.setEmail(user.email);
                        passengerBooking.setName(user.name);
                        passengerBooking.setPhotoUri(user.photo_uri);
                        passengerBooking.setKey(uid);

                        passengerBookingMap.put(passengerBooking.getKey(), passengerBooking);
                        int position = new ArrayList<>(passengerBookingMap.keySet()).indexOf(passengerBooking.getKey());
                        view.addPassengerQuotaMarker(passengerBooking, position);
                    }
                } catch (DatabaseException e) {
                    Timber.e(e.getMessage(), e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(databaseError.getMessage(), databaseError);
            }
        });
    }

    public void onDialogResponse(@Nullable PassengerBooking passengerBooking) {
        if (passengerBooking != null) assignBookingToDriverAndPassenger(passengerBooking);
    }

    private void assignBookingToDriverAndPassenger(@NonNull PassengerBooking passengerBooking) {
        PassengerInfoRequest passengerInfoRequest = new PassengerInfoRequest();
        passengerInfoRequest.driverUid = "user1";
        if (!TextUtils.isEmpty(currentAddress)) {
            passengerInfoRequest.address = currentAddress;
        }
        passengerInfoRequest.status = PassengerInfoRequest.STATUS_ACCEPTED;
        passengerInfoRequest.setKey(passengerBooking.getKey());
        DriverInfoRequest driverInfoRequest = new DriverInfoRequest();
        driverInfoRequest.status = PassengerInfoRequest.STATUS_ACCEPTED;
        driverInfoRequest.address = passengerBooking.address;
        driverInfoRequest.passengerUid = passengerBooking.getKey();
        driverInfoRequest.setKey("user1");
        if (getMapPreferences()) {

            driverMapService.assignBookingToDriverAndPassenger(
                    passengerInfoRequest,
                    driverInfoRequest,
                    StringUtils.buildRoute(community, fromOrTo, date, hour));
        }
    }

    //Passenger Services
    private void putBooking() {
        PassengerBooking passengerBooking = new PassengerBooking();
        //FIXME: change by curent user
        passengerBooking.setKey("user1");

        if (currentCoordinates == null) {
            view.showToast(R.string.error_empty_coordinates);
            return;
        }
        if (TextUtils.isEmpty(currentAddress)) {
            currentAddress = getCurrentAddressFromCamera();
        }
        passengerBooking.address = currentAddress;
        passengerBooking.latitude = currentCoordinates.latitude;
        passengerBooking.longitude = currentCoordinates.longitude;
        passengerBooking.status = PassengerInfoRequest.STATUS_WAITING;
        if (getMapPreferences()) {
            passengerMapService.putPassengerBookingPerCommunityOriginDate(passengerBooking, fromOrTo + "-" + community, date, hour);
        }
    }

    public void setAutocompleteFragment() {
        view.setAutocompleteFragment();
    }


    private void requestPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            view.buildGoogleApiClient();
            view.setMyLocationEnabled();
            currentCoordinates = view.getCurrentCoordinatesFromCamera();
        } else {
            view.requestPermissionsActivity();
        }
    }

    public boolean googleServicesAvailable() {
        boolean isAvailable = false;
        if (view.getContext() == null) {
            return false;
        }
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int availableId = api.isGooglePlayServicesAvailable(view.getContext());
        if (availableId == ConnectionResult.SUCCESS) {
            isAvailable = true;
        } else if (api.isUserResolvableError(availableId)) {
            view.showErrorDialog(api, availableId);
        } else {
            view.showToast(R.string.error_google_services_conection);
        }
        return isAvailable;
    }

    public void initMap() {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        Fragment fragment = view.getFragment();
        if (fragment == null) {
            return;
        }
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        view.initMap();
    }

    public void setMap(GoogleMap map) {
        view.setMap(map);
    }

    public void addLocationButton(String[] permissions, int[] grantResults) {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        if (permissions.length == 1 &&
                permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                view.buildGoogleApiClient();
                view.setMyLocationEnabled();
                currentCoordinates = view.getCurrentCoordinatesFromCamera();
            }
        } else {
            view.showToast(R.string.permission_denied);
        }
    }

    public void addMockMarkers() {
    }

    public void searchPlace(Place place) {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        LatLng placeLocation = place.getLatLng();
        String placeName = place.getName().toString();
        view.animateCamera(placeLocation);
        //view.setMarker(placeLocation, placeName);
    }

    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        view.goToCurrentLocation(latLng, getCurrentAddressFromLatLng(latLng));
    }

    private void setAutocompleteFragmentText() {
        currentAddress = getCurrentAddressFromCamera();
        view.setTextAutocompleteFragmentWithCurrentCoord(currentAddress);
    }

    private String getCurrentAddressFromLatLng(LatLng coordinates) {
        return calculateAddress(coordinates);
    }

    private String getCurrentAddressFromCamera() {
        currentCoordinates = view.getCurrentCoordinatesFromCamera();
        if (currentCoordinates != null) {
            return calculateAddress(currentCoordinates);
        }
        return StringUtils.EMPTY_STRING;

    }

    private String calculateAddress(LatLng currentCoordinates) {
        String address = StringUtils.EMPTY_STRING;
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(view.getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(currentCoordinates.latitude, currentCoordinates.longitude, 1);
            if (!addresses.isEmpty()) {
                address = addresses.get(FIRST_POSITION).getAddressLine(FIRST_POSITION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void onTimeSelected(String time) {
        wasTimeSelected = true;
        view.setButtonHour();
        mapPreference.putTime(time);
        if (wasDateSelected) {
            if (rolePreference.getCurrentRole().equalsIgnoreCase(ROLE_DRIVER)) {
                getQuotas();
            } else {
                putBooking();
            }
            wasDateSelected = false;
        }
    }

    public void onDateSelected(String date) {
        if (!StringUtils.isEmpty(date)) {
            wasDateSelected = true;
            view.setButtonDate(date);
            mapPreference.putDate(date);
            if (wasTimeSelected) {
                if (rolePreference.getCurrentRole().equalsIgnoreCase(ROLE_DRIVER)) {
                    getQuotas();
                } else {
                    view.clearMap();
                    putBooking();
                }
                wasTimeSelected = false;
            }
        } else {
            view.showToast(R.string.error_empty_date);
        }
    }

    public void onRoleChanged() {
        String role = rolePreference.getCurrentRole();
        if (role.equalsIgnoreCase(ROLE_PASSEGNER)) {
            if (passengerBookingMap.size() > 0) {
                wasDateSelected = false;
                view.clearMap();
            }
        }
    }

    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mapWasTouched = true;
        }
    }

    public void onCameraIdle() {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        if (NetworkUtils.isNetworkAvailable(activity)) {
            if (mapWasTouched) {
                setAutocompleteFragmentText();
            }
        }
    }

    public void initView() {
        mapPreference.putFromOrTo(MapPreference.FROM);
        String community = mapPreference.getCommunity();
        view.initViews(MapPreference.FROM, community != null ? community : StringUtils.EMPTY_STRING);
    }

    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            view.setTextLocationText("DESTINO: ICESI");
            mapPreference.putFromOrTo(MapPreference.TO);
        } else {
            view.setTextLocationText("ORIGEN: ICESI");
            mapPreference.putFromOrTo(MapPreference.FROM);
        }
    }

    public void setLocationRequest() {
        view.setLocationRequest();
    }

    public void showDialog(DisposableObserver<PassengerBooking> observer, Marker marker) {
        view.showDialogQuota(observer, passengerBookingMap.get(marker.getTitle()));
    }

    public void showTimePickerFragment(DisposableObserver<String> observer) {
        view.showTimePickerFragment(observer);
    }

    public void unsubscribeObservers() {
        view.unsubscribeObservers();
    }

    public void showDatePickerFragment(DisposableObserver<String> observer) {
        view.showDatePickerFragment(observer);
    }
}

