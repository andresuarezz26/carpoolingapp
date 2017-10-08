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
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.RequestInfo;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyMapView;
import com.angular.gerardosuarez.carpoolingapp.service.DriverMapService;
import com.angular.gerardosuarez.carpoolingapp.service.MyBookingDriverService;
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
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class MyMapFragmentPresenter extends BaseFragmentPresenter {

    private final static String ROLE_DRIVER = "driver";
    private final static String ROLE_PASSEGNER = "passenger";
    private static final int FIRST_POSITION = 0;
    private static final int MININUM_NUMBER_OF_PASSENGERS_LESS_ONE = 3;

    private MyMapView view;
    private DriverMapService driverMapService;
    private PassengerMapService passengerMapService;
    private RolePreference rolePreference;
    private UserService userService;
    private MyBookingDriverService myBookingDriverService;

    private ValueEventListener quotaPassengerListener;

    private boolean mapWasTouched = false;
    private String currentAddress;
    private LatLng currentCoordinates;

    private Map<String, PassengerBooking> passengerBookingMap = new LinkedHashMap<>();

    public MyMapFragmentPresenter(MyMapView view,
                                  DriverMapService driverMapService,
                                  PassengerMapService passengerMapService,
                                  RolePreference rolePreference,
                                  MapPreference mapPreference,
                                  UserService userService,
                                  MyBookingDriverService myBookingDriverService) {
        super(mapPreference, view, userService);
        this.view = view;
        this.driverMapService = driverMapService;
        this.passengerMapService = passengerMapService;
        this.rolePreference = rolePreference;
        this.userService = userService;
        this.myBookingDriverService = myBookingDriverService;
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

    //region DriverServices
    public void getBookingsAndAddMarkers() {
        if (areAllMapPreferenceNonnull()) {
            getBookingsAndAddMarkers(community, fromOrTo, date, hour);
        }
    }

    private void getBookingsAndAddMarkers(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour) {
        quotaPassengerListener = driverMapService.getQuotasPerCommunityOriginDateAndHour(comunity, origin, date, hour).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String role = rolePreference.getCurrentRole();
                        if (role != null && role.equalsIgnoreCase(ROLE_DRIVER)) {
                            view.clearMap();
                            DriverMapService.passengersSelectedByDriver.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getKey();
                                try {
                                    PassengerBooking passengerBooking = snapshot.getValue(PassengerBooking.class);
                                    if (!PassengerInfoRequest.STATUS_CANCELED.equalsIgnoreCase(passengerBooking.status)
                                            ) {
                                        setPassengerBookingAditionalInfo(snapshot.getKey(), passengerBooking);
                                    }
                                } catch (DatabaseException e) {
                                    Timber.e(e.getMessage(), e);
                                }
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
                        passengerBooking.setUserAttributes(user);
                        passengerBooking.setKey(uid);
                        passengerBookingMap.put(passengerBooking.getKey(), passengerBooking);
                        int position = new ArrayList<>(passengerBookingMap.keySet()).indexOf(passengerBooking.getKey());
                        //view.addMarkerForNoChoosenPassenger(passengerBooking, position);
                        addMarker(passengerBooking, position);
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

    //FIXME: find the way of reuse the passengerSelectedByDriver
    private void addMarker(@NonNull PassengerBooking passengerBooking, int position) {
        /*if (passengersSelectedByDriver.size() != 0) {
            chooseNoChooseOrAlreadyChooseMarker(passengerBooking, position);
        } else {*/
        queryCurrentChoosenPassengersByDriver(passengerBooking, position);
        //}
    }

    private void queryCurrentChoosenPassengersByDriver(@NonNull final PassengerBooking passengerBooking, final int position) {
        if (getMyUid() == null) return;
        myBookingDriverService.getRequestOfTheDriver(community, fromOrTo, date, hour, getMyUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int numberOfPasengers = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                numberOfPasengers++;
                                PassengerInfoRequest passengerInfoRequest = snapshot.getValue(PassengerInfoRequest.class);
                                if (passengerInfoRequest != null) {
                                    DriverMapService.passengersSelectedByDriver.put(snapshot.getKey(), snapshot.getKey());
                                    chooseNoChooseOrAlreadyChooseMarker(passengerBooking, position);
                                }
                            } catch (DatabaseException e) {
                                Timber.e(e.getMessage(), e);
                            }
                        }
                        if (numberOfPasengers == 0) {
                            addMarkerNoChoosenPassenger(passengerBooking, position);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.toString(), databaseError);
                    }
                });
    }

    private void chooseNoChooseOrAlreadyChooseMarker(@NonNull PassengerBooking passengerBooking, int position) {
        if (DriverMapService.passengersSelectedByDriver.get(passengerBooking.getKey()) == null) {
            addMarkerNoChoosenPassenger(passengerBooking, position);
        } else {
            view.addMarkerForAlreadyChoosenByDriverPassenger(passengerBooking, position);
        }
    }

    private void addMarkerNoChoosenPassenger(@NonNull PassengerBooking passengerBooking, int position) {
        if (!PassengerInfoRequest.STATUS_ACCEPTED.equalsIgnoreCase(passengerBooking.status)) {
            view.addMarkerForNoChoosenPassenger(passengerBooking, position);
        }
    }

    //endregion

    public void onClickMarkerDialogResponse(@Nullable PassengerBooking passengerBooking) {
        if (passengerBooking != null) assignBookingToDriverAndPassenger(passengerBooking);
    }

    private void onPassengerRequestingBookingDialogResponse(Pair<PassengerBooking, RequestInfo> pair) {
        if (pair != null) {
            putBooking();
            mapPreference.putAddress(currentAddress);
            putAlreadyDataChoosen(true);
        }
    }

    private void assignBookingToDriverAndPassenger(@NonNull PassengerBooking passengerBooking) {
        PassengerInfoRequest passengerInfoRequest = new PassengerInfoRequest();
        String myUid = getMyUid();
        if (myUid != null)
            passengerInfoRequest.driverUid = myUid;
        if (!TextUtils.isEmpty(currentAddress)) {
            passengerInfoRequest.address = currentAddress;
        }
        passengerInfoRequest.status = PassengerInfoRequest.STATUS_ACCEPTED;
        passengerInfoRequest.setKey(passengerBooking.getKey());
        DriverInfoRequest driverInfoRequest = new DriverInfoRequest();
        driverInfoRequest.status = PassengerInfoRequest.STATUS_ACCEPTED;
        driverInfoRequest.address = passengerBooking.address;
        driverInfoRequest.passengerUid = passengerBooking.getKey();
        driverInfoRequest.setKey(myUid);
        if (areAllMapPreferenceNonnull()) {
            driverMapService.assignBookingToDriverAndPassenger(
                    passengerInfoRequest,
                    driverInfoRequest,
                    StringUtils.buildRoute(community, fromOrTo, date, hour));
        }
    }

    //Passenger Services
    private void putBooking() {
        PassengerBooking passengerBooking = new PassengerBooking();
        if (!TextUtils.isEmpty(getMyUid())) {
            passengerBooking.setKey(getMyUid());
        }

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
        if (areAllMapPreferenceNonnull()) {
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
        view.animateCamera(placeLocation);
    }

    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        view.goToCurrentLocation(latLng, getCurrentAddressFromLatLng(latLng));
    }

    public void setAutocompleteFragmentText() {
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
        if (view.isNetworkAvailable()) {
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
        view.showToast(R.string.network_not_available);
        return StringUtils.EMPTY_STRING;
    }

    public void onTimeSelected(String time) {
        currentAddress = getCurrentAddressFromCamera();
        if (time != null) {
            mapPreference.putTimeSelected(true);
            view.setButtonHour(StringUtils.formatHour(time));
            mapPreference.putTime(time);
            if (mapPreference.isDateSelected()) {
                String role = rolePreference.getCurrentRole();
                if (role != null && role.equalsIgnoreCase(ROLE_DRIVER)) {
                    onDriverRouteAccepted();
                } else {
                    startDialogToPutPassengerBooking();
                }
                mapPreference.putDateSelected(false);
            }
        } else {
            view.showToast(R.string.error_time_range);
        }
    }

    private void onDriverRouteAccepted() {
        getBookingsAndAddMarkers();
        putAlreadyDataChoosen(true);
        view.setSwitchIsClickable(false);
        mapPreference.putAddress(currentAddress);
    }

    public void onDateSelected(String date) {
        currentAddress = getCurrentAddressFromCamera();
        if (!StringUtils.isEmpty(date)) {
            mapPreference.putDateSelected(true);
            view.setButtonDate(StringUtils.formatDateWithTodayLogic(date));
            mapPreference.putDate(date);
            if (mapPreference.isTimeSelected()) {
                String role = rolePreference.getCurrentRole();
                if (role != null && role.equalsIgnoreCase(ROLE_DRIVER)) {
                    onDriverRouteAccepted();
                } else {
                    startDialogToPutPassengerBooking();
                }
                mapPreference.putTimeSelected(false);
            }
        } else {
            view.showToast(R.string.error_empty_date);
        }
    }

    private void startDialogToPutPassengerBooking() {
        PassengerBooking passengerBooking = new PassengerBooking();
        RequestInfo requestInfo = getRequestInfo();
        if (requestInfo != null) {
            requestInfo.setAddress(currentAddress);
            view.showDialogRequestBooking(new OnPassengerRequestingBookingObserver(), passengerBooking, requestInfo);
        }
    }

    public void changeViewElements() {
        view.setTextLocationText(StringUtils.getFromOrToFormattedText(
                mapPreference.getFromOrTo() == null ? MapPreference.FROM : mapPreference.getFromOrTo(),
                mapPreference.getCommunity()));
        if (mapPreference.getDate() == null) {
            if (MapPreference.FROM.equalsIgnoreCase(mapPreference.getFromOrTo())) {
                view.setButtonDate(R.string.departure_date_message);
            } else {
                view.setButtonDate(R.string.arrival_date_message);
            }
        } else {
            view.setButtonDate(StringUtils.formatDateWithTodayLogic(mapPreference.getDate()));
        }

        setButtonHourText();

        if (mapPreference.getFromOrTo() != null) {
            view.setSwitchState(MapPreference.TO.equals(mapPreference.getFromOrTo()));
        }

        if (mapPreference.isAlreadyDataChoosen()) {
            view.setSwitchIsClickable(false);
        } else {
            view.setSwitchIsClickable(true);
        }
    }

    private void setButtonHourText() {
        if (mapPreference.getHour() == null) {
            if (MapPreference.FROM.equalsIgnoreCase(mapPreference.getFromOrTo())) {
                view.setButtonHour(R.string.departure_hour_message);
            } else {
                view.setButtonHour(R.string.arrival_hour_message);
            }
        } else {
            view.setButtonHour(StringUtils.formatHour(mapPreference.getHour()));
        }
    }

    public void cleanMapIfNecessary() {
        if (mapPreference.getDate() == null || mapPreference.getHour() == null) {
            view.clearMap();
        }
    }

    private class OnPassengerRequestingBookingObserver extends DisposableObserver<Pair<PassengerBooking, RequestInfo>> {

        @Override
        public void onNext(Pair<PassengerBooking, RequestInfo> pair) {

            onPassengerRequestingBookingDialogResponse(pair);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
        }
    }

    public void onRoleChanged(boolean newRole) {
        if (newRole) view.showToast(R.string.deleted_hour_and_date);
        String role = rolePreference.getCurrentRole();
        if (role.equalsIgnoreCase(ROLE_PASSEGNER)) {
            clearMapTimeAndDate();
        }
    }

    private void clearMapTimeAndDate() {
        if (passengerBookingMap.size() > 0) {
            mapPreference.putTimeSelected(false);
            mapPreference.putDateSelected(false);
            view.clearMap();
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
        if (mapPreference.getFromOrTo() == null) {
            mapPreference.putFromOrTo(MapPreference.FROM);
        }
        community = mapPreference.getCommunity();
        view.initViews();
    }

    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            mapPreference.putFromOrTo(MapPreference.TO);
            view.setTextLocationText(StringUtils.getFromOrToFormattedText(
                    MapPreference.TO, mapPreference.getCommunity()));
        } else {
            mapPreference.putFromOrTo(MapPreference.FROM);
            view.setTextLocationText(StringUtils.getFromOrToFormattedText(
                    MapPreference.FROM, mapPreference.getCommunity()));
        }
        setButtonHourText();
    }

    public void setLocationRequest() {
        view.setLocationRequest();
    }

    public void showClickMarkerDialog(DisposableObserver<PassengerBooking> observer, Marker marker) {
        if (marker != null && marker.getTitle() != null) {
            if (DriverMapService.passengersSelectedByDriver.get(marker.getTitle()) == null &&
                    DriverMapService.passengersSelectedByDriver.size() <= MININUM_NUMBER_OF_PASSENGERS_LESS_ONE) {
                view.showDialogQuota(observer, passengerBookingMap.get(marker.getTitle()));
            }
        }
    }

    public void showTimePickerFragment(DisposableObserver<String> observer) {
        if (!alreadyDataChoosen()) {
            view.showTimePickerFragment(observer);
        } else {
            alreadyDataChoosenErrorMsg();
        }
    }

    public void unsubscribeObservers() {
        view.unsubscribeObservers();
    }

    public void showDatePickerFragment(DisposableObserver<String> observer) {
        if (!alreadyDataChoosen()) {
            view.showDatePickerFragment(observer);
        } else {
            alreadyDataChoosenErrorMsg();
        }
    }

    private void alreadyDataChoosenErrorMsg() {
        String role = rolePreference.getCurrentRole();
        if (!TextUtils.isEmpty(role)) {
            int res = ROLE_DRIVER.equalsIgnoreCase(rolePreference.getCurrentRole()) ?
                    R.string.already_data_choosen_driver : R.string.already_data_choosen_passenger;
            view.showToast(res);
        }
    }
}

