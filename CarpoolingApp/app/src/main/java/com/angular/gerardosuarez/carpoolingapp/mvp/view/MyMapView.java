package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyMapFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerQuota;
import com.google.android.gms.common.GoogleApiAvailability;
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
import timber.log.Timber;

public class MyMapView extends FragmentView<MyMapFragment, Void> {

    public static final int DEFAULT_ZOOM = 16;
    private GoogleMap map;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private static final double LATITUDE_CALI_FIRST = 3.5408;
    private static final double LONGITUDE_CALI_FIRST = -76.5367;
    private static final double LATITUDE_CALI_SECOND = 3.2872;
    private static final double LONGITUDE_CALI_SECOND = -76.4872;

    @BindView(R.id.switch_from_to) Switch switchFromTo;
    @BindView(R.id.edit_location) TextView textLocation;
    @BindView(R.id.btn_hour) Button btnHour;
    @BindView(R.id.btn_date) Button btnDate;

    private PlaceAutocompleteFragment autocompleteFragment;

    public MyMapView(MyMapFragment fragment) {
        super(fragment);
        if (fragment.getView() != null) {
            ButterKnife.bind(this, fragment.getView());
        }
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setAutocompleteFragment() {
        autocompleteFragment = (PlaceAutocompleteFragment) getFragment().
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(LATITUDE_CALI_SECOND, LONGITUDE_CALI_SECOND),
                new LatLng(LATITUDE_CALI_FIRST, LONGITUDE_CALI_FIRST)
        ));

        autocompleteFragment.setOnPlaceSelectedListener(getFragment());
    }

    private void setTextAutocompleteFragmentWithText(String text) {
        autocompleteFragment.setText(text);
    }

    public void setTextAutocompleteFragmentWithCurrentCoord(String currentLocation) {
        autocompleteFragment.setText(currentLocation);
    }

    public void initMap() {
        MapFragment mapFragment = (MapFragment) getFragment().getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(getFragment());
    }

    public void initViews() {
        textLocation.setText("ORIGEN: ICESI");
        switchFromTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textLocation.setText("DESTINO: ICESI");
                } else {
                    textLocation.setText("ORIGEN: ICESI");
                }
            }
        });
    }

    public void showErrorDialog(GoogleApiAvailability api, int availableId) {
        Dialog dialog = api.getErrorDialog(getActivity(), availableId, 0);
        dialog.show();
    }

    public void showToast(int res) {
        Toast.makeText(getActivity(), getActivity().getResources().getString(res), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void setMarker(LatLng latLng, String title, int id) {
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

    public void animateCamera(LatLng position) {
        map.animateCamera(CameraUpdateFactory.newLatLng(position));
    }

    public void setLocationManager() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, getFragment());
        } catch (SecurityException e) {
            Timber.e(e);
        }
    }

    public void goToCurrentLocation(LatLng latLng, String currentAddress) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        map.animateCamera(cameraUpdate);
        locationManager.removeUpdates(getFragment());
        setTextAutocompleteFragmentWithText(currentAddress);
    }

    public void requestPermissionsActivity() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MyMapFragment.PERMISSION_REQUEST_FINE_LOCATION);
    }

    public LatLng getCurrentCoordinatesFromCamera() {
        return map.getCameraPosition().target;
    }

    public void addPassengerQuotaMarker(PassengerQuota passengerQuota) {
        setMarker(new LatLng(passengerQuota.latitude, passengerQuota.longitude), passengerQuota.description);
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
        locationManager.removeUpdates(fragment);
        map.setOnCameraMoveListener(null);
        map.setOnCameraMoveStartedListener(null);
        map.setOnCameraIdleListener(null);
        map.setOnMarkerClickListener(null);
    }

    public void setButtonHour() {
        btnHour.setText("Listo");
        //btnHour.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public void setButtonDate() {
        btnDate.setText("Listo");
        //btnDate.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }
}
