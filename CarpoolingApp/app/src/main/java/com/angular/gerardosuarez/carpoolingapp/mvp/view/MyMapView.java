package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.customviews.dialog.DialogMarker;
import com.angular.gerardosuarez.carpoolingapp.customviews.dialog.DialogPassengerMap;
import com.angular.gerardosuarez.carpoolingapp.dialogfragment.DatePickerFragment;
import com.angular.gerardosuarez.carpoolingapp.dialogfragment.TimePickerFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyMapFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.RequestInfo;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class MyMapView extends FragmentView<MyMapFragment, Void> {

    private static final int DEFAULT_ZOOM = 16;
    private static final int INITIAL_ZOOM = 11;
    private static final String DATE_PICKER = "datePicker";
    private static final String TIME_PICKER = "timePicker";
    private GoogleMap map;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private static final double LATITUDE_CALI_FIRST = 3.5408;
    private static final double LONGITUDE_CALI_FIRST = -76.5367;
    private static final double LATITUDE_CALI_SECOND = 3.2872;
    private static final double LONGITUDE_CALI_SECOND = -76.4872;
    private static final double LONGITUDE_INITIAL = -76.54428374022244;
    private static final double LATITUDE_INITIAL = 3.4380741597868383;

    private DialogMarker dialogMarkerClick;
    private DialogPassengerMap dialogPassengerMap;
    private TimePickerFragment timePickerFragment;
    private DatePickerFragment datePickerFragment;

    @BindView(R.id.switch_from_to)
    Switch switchFromTo;
    @BindView(R.id.edit_location)
    TextView textLocation;
    @BindView(R.id.btn_hour)
    Button btnHour;
    @BindView(R.id.btn_date)
    Button btnDate;

    private PlaceAutocompleteFragment autocompleteFragment;

    public MyMapView(MyMapFragment fragment) {
        super(fragment);
        if (fragment.getView() != null) {
            ButterKnife.bind(this, fragment.getView());
        }
    }

    public void setAutocompleteFragment() {
        if (getFragment() == null) return;
        if (getFragment().getChildFragmentManager() == null) return;
        autocompleteFragment = (PlaceAutocompleteFragment) getFragment().
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(LATITUDE_CALI_SECOND, LONGITUDE_CALI_SECOND),
                new LatLng(LATITUDE_CALI_FIRST, LONGITUDE_CALI_FIRST)
        ));

        autocompleteFragment.setOnPlaceSelectedListener(getFragment());
    }

    private void setTextAutocompleteFragmentWithText(String text) {
        try {
            if (autocompleteFragment != null && text != null) {
                autocompleteFragment.setText(text);
            }
        } catch (NullPointerException e) {
            Timber.e(e, e.getMessage());
        }

    }

    public void setTextAutocompleteFragmentWithCurrentCoord(String currentLocation) {
        try {
            if (autocompleteFragment != null && currentLocation != null) {
                autocompleteFragment.setText(currentLocation);
            }
        } catch (NullPointerException e) {
            Timber.e(e, e.getMessage());
        }
    }

    public void initMap() {
        if (getFragment() == null) return;
        if (getFragment().getChildFragmentManager() == null) return;
        MapFragment mapFragment = (MapFragment) getFragment().getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(getFragment());
    }

    public synchronized void buildGoogleApiClient() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        MyMapFragment fragment = getFragment();
        if (fragment == null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(fragment)
                .addOnConnectionFailedListener(fragment)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void initViews() {
        switchFromTo.setOnCheckedChangeListener(getFragment());
    }

    public void setTextLocationText(String text) {
        textLocation.setText(text);
    }

    public void showErrorDialog(GoogleApiAvailability api, int availableId) {
        Dialog dialog = api.getErrorDialog(getActivity(), availableId, 0);
        dialog.show();
    }

    public void setMap(GoogleMap map) {
        this.map = map;
        moveCamera(LATITUDE_INITIAL, LONGITUDE_INITIAL);
    }

    private void moveCamera(double latitude, double longitude) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), INITIAL_ZOOM));
    }

    private void setMarker(LatLng latLng, String title, int id) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title))
                .setTag(id);
    }

    private void setMarker(LatLng latLng, String title) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title));
    }

    private void setMarkerWithTitleAndSnippet(LatLng latLng, String title) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title));
    }

    public void animateCamera(LatLng position) {
        map.animateCamera(CameraUpdateFactory.newLatLng(position));
    }

    public void setLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, getFragment());
        }
    }

    public void goToCurrentLocation(LatLng latLng, String currentAddress) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        map.animateCamera(cameraUpdate);
        removeLocationUpdates();
        setTextAutocompleteFragmentWithText(currentAddress);
    }

    public void requestPermissionsActivity() {
        if (getActivity() != null) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MyMapFragment.PERMISSION_REQUEST_FINE_LOCATION);
        }
    }

    public LatLng getCurrentCoordinatesFromCamera() {
        return map.getCameraPosition().target;
    }

    public void addPassengerQuotaMarker(PassengerBooking passengerBooking, int id) {
        setMarker(new LatLng(passengerBooking.latitude, passengerBooking.longitude), passengerBooking.getKey(), id);
    }

    public void showDialogQuota(DisposableObserver<PassengerBooking> observer, PassengerBooking passengerBooking) {
        if (getActivity() == null) return;
        dialogMarkerClick = new DialogMarker(getActivity(), passengerBooking);
        dialogMarkerClick.subscribeToDialogEvent(observer);
        dialogMarkerClick.show();
    }

    public void showDialogRequestBooking(DisposableObserver<Pair<PassengerBooking, RequestInfo>> observer, PassengerBooking passengerBooking, RequestInfo requestInfo) {
        if (getActivity() == null) return;
        dialogPassengerMap = new DialogPassengerMap(getActivity(), passengerBooking, requestInfo);
        dialogPassengerMap.subscribeToDialogEvent(observer);
        dialogPassengerMap.show();
    }

    public void setListeners() {
        MyMapFragment fragment = getFragment();
        if (fragment == null) {
            return;
        }
        if (map == null) {
            return;
        }
        map.setOnCameraMoveListener(getFragment());
        map.setOnCameraMoveStartedListener(getFragment());
        map.setOnCameraIdleListener(getFragment());
        map.setOnMarkerClickListener(getFragment());
    }

    public void removeListeners() {
        MyMapFragment fragment = getFragment();
        if (fragment == null) {
            return;
        }
        if (map == null) {
            return;
        }
        removeLocationUpdates();
        map.setOnCameraMoveListener(null);
        map.setOnCameraMoveStartedListener(null);
        map.setOnCameraIdleListener(null);
        map.setOnMarkerClickListener(null);
    }

    private void removeLocationUpdates() {
        try {
            if (mGoogleApiClient != null && getFragment() != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, getFragment());
            }
        } catch (IllegalStateException e) {
            Timber.e(e.toString(), e);
        }
    }

    public void setMyLocationEnabled() {
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Timber.e(e.getMessage(), e);
        }
    }

    public void setButtonHour(String hour) {
        btnHour.setText(StringUtils.formatHour(hour));
        //btnHour.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public void setButtonDate(String date) {
        btnDate.setText(date);
        //btnDate.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public void clearMap() {
        if (map == null) return;
        map.clear();
    }

    public void showTimePickerFragment(DisposableObserver<String> observer) {
        if (getFragment() == null) return;
        if (getFragment().getFragmentManager() == null) return;
        timePickerFragment = new TimePickerFragment();
        timePickerFragment.subscribeToDialogFragment(observer);
        timePickerFragment.show(getFragment().getFragmentManager(), TIME_PICKER);
    }

    public void showDatePickerFragment(DisposableObserver<String> observer) {
        if (getFragment() == null) return;
        if (getFragment().getFragmentManager() == null) return;
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.subscribeToDialogFragment(observer);
        datePickerFragment.show(getFragment().getFragmentManager(), DATE_PICKER);
    }

    public void unsubscribeObservers() {
        if (timePickerFragment != null) {
            timePickerFragment.unsubscribeToDialogFragment();
            timePickerFragment.dismiss();
        }
        if (dialogMarkerClick != null) {
            dialogMarkerClick.unsubscribeToDialogEvent();
            dialogMarkerClick.dismiss();
        }
        if (datePickerFragment != null) {
            datePickerFragment.unsubscribeToDialogFragment();
            datePickerFragment.dismiss();
        }
        if (dialogPassengerMap != null) {
            dialogPassengerMap.unsubscribeToDialogEvent();
            dialogPassengerMap.dismiss();
        }
    }
}
