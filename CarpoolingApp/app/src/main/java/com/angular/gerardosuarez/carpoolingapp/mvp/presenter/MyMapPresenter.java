package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BasePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyMapView;
import com.angular.gerardosuarez.carpoolingapp.service.DriverMapService;
import com.angular.gerardosuarez.carpoolingapp.service.PassengerMapService;
import com.angular.gerardosuarez.carpoolingapp.utils.NetworkUtils;
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
import java.util.Random;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class MyMapPresenter extends BasePresenter {

    private final static String ROLE_DRIVER = "driver";
    private final static String ROLE_PASSEGNER = "passenger";

    private MyMapView view;
    private DriverMapService driverMapService;
    private PassengerMapService passengerMapService;
    private RolePreference rolePreference;
    private MapPreference mapPreference;

    private ValueEventListener quotaPassengerListener;
    private String currentRole;

    private boolean mapWasTouched = false;
    private boolean wasDateSelected = false;
    private boolean wasTimeSelected = false;

    private LinkedHashMap<String, PassengerBooking> passengerQuotasMap = new LinkedHashMap<>();

    public MyMapPresenter(MyMapView view,
                          DriverMapService driverMapService,
                          PassengerMapService passengerMapService,
                          RolePreference rolePreference,
                          MapPreference mapPreference) {
        super();
        this.view = view;
        this.driverMapService = driverMapService;
        this.passengerMapService = passengerMapService;
        this.rolePreference = rolePreference;
        this.mapPreference = mapPreference;
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
        getQuotas("icesi", "from", "18062017", "1600");
    }

    private void getQuotas(String comunity, String origin, String date, String hour) {
        quotaPassengerListener = driverMapService.getQuotasPerCommunityOriginDateAndHour(comunity, origin, date, hour).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getKey();
                            try {
                                PassengerBooking passengerBooking = snapshot.getValue(PassengerBooking.class);
                                passengerQuotasMap.put(passengerBooking.userId, passengerBooking);
                                int position = new ArrayList<>(passengerQuotasMap.keySet()).indexOf(passengerBooking.userId);
                                view.addPassengerQuotaMarker(passengerBooking, position);
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

    public void onDialogResponse(boolean wantSendRequest) {
        if (wantSendRequest) putRequestToPassenger();
    }

    private void putRequestToPassenger() {
        PassengerInfoRequest passengerInfoRequest = new PassengerInfoRequest();
        //FIXME: remove this
        Random number = new Random();
        passengerInfoRequest.driverUid = "yo";
        passengerInfoRequest.address = "Gerardo Suarez";
        passengerInfoRequest.status = "waiting";
        passengerInfoRequest.setKey("passengerMock");
        DriverInfoRequest driverInfoRequest = new DriverInfoRequest();
        driverInfoRequest.status = "waiting";
        driverInfoRequest.address = "calle 4a # 3-84";
        driverInfoRequest.passengerUid = "passengerMock";
        driverInfoRequest.setKey("yo");
        driverMapService.putInfoRequestToPassengerAndDriver(passengerInfoRequest, driverInfoRequest);
    }

    //Passenger Services
    private void putQuota() {
        PassengerBooking passengerBooking = new PassengerBooking();
        //FIXME: remove this
        Random number = new Random();
        passengerBooking.userId = "yo";
        passengerBooking.description = "Calle 3a 34-3";
        passengerBooking.latitude = 3.4380741597868383;
        passengerBooking.longitude = -76.54428374022400;
        passengerMapService.putQuotaPerCommunityOriginDate(passengerBooking);
    }

    public void setAutocompleteFragment() {
        view.setAutocompleteFragment();
    }


    private void requestPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            view.buildGoogleApiClient();
            view.setMyLocationEnabled();
        } else {
            view.requestPermissionsActivity();
        }
    }

    public boolean googleServicesAvailable() {
        boolean isAvailable = false;
        if (view.getContext() == null) {
            return isAvailable;
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
            }
        } else {
            view.showToast(R.string.permission_denied);
        }
    }

    public void addMockMarkers() {
    }

    public boolean onMarkerClick(Marker marker) {
        if (marker == null) {
            return false;
        }
        PassengerBooking passengerBooking = passengerQuotasMap.get(marker.getTitle());
        view.showToast(passengerBooking.description + passengerBooking.userId);
        return true;
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

    public void setAutocompleteFragmentText() {
        view.setTextAutocompleteFragmentWithCurrentCoord(getCurrentAddressFromCamera());
    }

    private String getCurrentAddressFromLatLng(LatLng coordinates) {
        return calculateAddress(coordinates);
    }

    private String getCurrentAddressFromCamera() {
        LatLng currentCoordinates = view.getCurrentCoordinatesFromCamera();
        return calculateAddress(currentCoordinates);
    }

    private String calculateAddress(LatLng currentCoordinates) {
        String address = "";
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(view.getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(currentCoordinates.latitude, currentCoordinates.longitude, 1);
            if (!addresses.isEmpty()) {
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void onTimeSelected(String time) {
        wasTimeSelected = true;
        view.setButtonHour();
        mapPreference.putTime("1600");
        if (wasDateSelected) {
            if (rolePreference.getCurrentRole().equalsIgnoreCase(ROLE_DRIVER)) {
                getQuotas();
            } else {
                putQuota();
            }
            wasDateSelected = false;
        }
    }

    public void onDateSelected(String date) {
        wasDateSelected = true;
        view.setButtonDate();
        mapPreference.putDate("18062017");
        if (wasTimeSelected) {
            if (rolePreference.getCurrentRole().equalsIgnoreCase(ROLE_DRIVER)) {
                getQuotas();
            } else {
                view.clearMap();
                putQuota();
            }
            wasTimeSelected = false;
        }
    }

    public void onRoleChanged() {
        String role = rolePreference.getCurrentRole();
        if (role.equalsIgnoreCase(ROLE_PASSEGNER)) {
            if (passengerQuotasMap.size() > 0) {
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
        view.initViews();
    }

    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            view.setTextLocationText("DESTINO: ICESI");
        } else {
            view.setTextLocationText("ORIGEN: ICESI");
        }
    }

    public void setLocationRequest() {
        view.setLocationRequest();
    }

    public void showDialog(DisposableObserver<Boolean> observer, Marker marker) {
        view.showDialogQuota(observer, marker.getTitle(), marker.getTitle());
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

