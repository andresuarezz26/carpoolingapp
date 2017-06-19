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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.dialogfragment.DatePickerFragment;
import com.angular.gerardosuarez.carpoolingapp.dialogfragment.TimePickerFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.DriverMapPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.DriverMapView;
import com.angular.gerardosuarez.carpoolingapp.service.DriverMapService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class DriverMapFragment extends Fragment
        implements
        OnMapReadyCallback,
        LocationListener,
        PlaceSelectionListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener {

    public static final String TAG = "driver_map";
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private boolean mapWasTouched = false;

    private DriverMapPresenter presenter;

    @BindView(R.id.switch_from_to) Switch switchFromTo;
    @BindView(R.id.edit_location) TextView textLocation;


    @BindView(R.id.btn_hour) Button btnHour;
    @BindView(R.id.btn_date) Button btnDate;

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
        presenter = new DriverMapPresenter(
                new DriverMapView(this),
                new DriverMapService());
        if (presenter.googleServicesAvailable()) {
            presenter.initMap();
        }
        presenter.setAutocompleteFragment();

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

    @OnClick(R.id.btn_hour)
    void onTimeClick() {
        DialogFragment newFragment = new TimePickerFragment(new OnTimeSelectedObserver());
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private class OnTimeSelectedObserver implements Observer<Integer> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            btnHour.setText("" + integer);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
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
            btnDate.setText("" + integer);
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
        presenter.subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_FINE_LOCATION) {
            presenter.addLocationButton(permissions, grantResults);
        }
    }

    //OnMapReadyCallback
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
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mapWasTouched = true;
        }
    }

    //OnCameraMoveStartedListener callbacks
    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraIdle() {
        if (mapWasTouched) {
            presenter.setAutocompleteFragmentText();
        }
    }
}
