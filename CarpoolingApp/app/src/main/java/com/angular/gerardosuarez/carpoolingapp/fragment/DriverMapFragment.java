package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.DriverMapPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.DriverMapView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.ButterKnife;
import timber.log.Timber;

public class DriverMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    public static final String TAG = "driver_map";
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    public static final double LATITUDE_CALI_FIRST = 3.5408;
    public static final double LONGITUDE_CALI_FIRST = -76.5367;
    public static final double LATITUDE_CALI_SECOND = 3.2872;
    public static final double LONGITUDE_CALI_SECOND = -76.4872;

    private DriverMapPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new DriverMapPresenter(new DriverMapView(this));
        if (presenter.googleServicesAvailable()) {
            presenter.initMap();
        }
        setAutocompleteFragment();
    }

    private void setAutocompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(LATITUDE_CALI_SECOND, LONGITUDE_CALI_SECOND),
                new LatLng(LATITUDE_CALI_FIRST, LONGITUDE_CALI_FIRST)
        ));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                presenter.searchPlace(place);
            }

            @Override
            public void onError(Status status) {
                Timber.e(status.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_FINE_LOCATION) {
            presenter.addLocationButton(permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        presenter.setMap(googleMap);
        presenter.init();
        presenter.addMockMarkers();
    }

    @Override
    public void onLocationChanged(Location location) {
        presenter.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
