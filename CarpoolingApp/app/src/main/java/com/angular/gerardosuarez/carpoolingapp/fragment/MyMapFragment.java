package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.dialogfragment.DatePickerFragment;
import com.angular.gerardosuarez.carpoolingapp.dialogfragment.TimePickerFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyMapPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyMapView;
import com.angular.gerardosuarez.carpoolingapp.service.DriverMapService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MyMapFragment extends Fragment
        implements
        OnMapReadyCallback,
        LocationListener,
        PlaceSelectionListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener,
        CompoundButton.OnCheckedChangeListener{

    public static final String TAG = "driver_map";
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    private MyMapPresenter presenter;

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
        MapPreference mapPreference = new MapPreferenceImpl(getActivity(), MapPreferenceImpl.NAME);
        RolePreference rolePreference = new RolePreferenceImpl(getActivity(), RolePreferenceImpl.NAME);
        presenter = new MyMapPresenter(
                new MyMapView(this),
                new DriverMapService(),
                rolePreference,
                mapPreference);
        if (presenter.googleServicesAvailable()) {
            presenter.initMap();
        }
        presenter.setAutocompleteFragment();
        presenter.initView();
    }

    @OnClick(R.id.btn_hour)
    void onTimeClick() {
        DialogFragment timePickerFragment = new TimePickerFragment(new OnTimeSelectedObserver());
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    //On switch changed
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.onSwitchChanged(isChecked);
    }

    private class OnTimeSelectedObserver implements Observer<Integer> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            presenter.onTimeSelected(integer);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribeFirebaseListener();
    }

    @OnClick(R.id.btn_date)
    void onDateClick() {
        DialogFragment newFragment = new DatePickerFragment(new OnDateSelectedObserver());
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private class OnDateSelectedObserver implements Observer<Integer> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            presenter.onDateSelected(integer);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setListeners();
        presenter.getRole();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.removeListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_FINE_LOCATION) {
            presenter.addLocationButton(permissions, grantResults);
        }
    }

    //OnMapReady Callback
    @Override
    public void onMapReady(GoogleMap googleMap) {
        presenter.setMap(googleMap);
        presenter.init();
        presenter.addMockMarkers();
    }

    //LocationListener callbacks
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

    //Autocomplete Fragment callbacks
    @Override
    public void onPlaceSelected(Place place) {
        presenter.searchPlace(place);
    }

    @Override
    public void onError(Status status) {
        Timber.e(status.toString());
    }

    //OnCameraMoveStartedListener callbacks
    @Override
    public void onCameraMoveStarted(int reason) {
        presenter.onCameraMoveStarted(reason);
    }

    //OnCameraMoveStartedListener callbacks
    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraIdle() {
        presenter.onCameraIdle();
    }

    //GoogleMap.OnMarkerClickListener callback
    @Override
    public boolean onMarkerClick(Marker marker) {
        return presenter.onMarkerClick(marker);
    }
}
