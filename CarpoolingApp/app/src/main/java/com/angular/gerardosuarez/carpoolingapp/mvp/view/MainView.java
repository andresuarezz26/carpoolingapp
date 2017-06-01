package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Dialog;
import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base.ActivityView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;

public class MainView extends ActivityView<MainActivity> {

    private GoogleMap map;

    public MainView(MainActivity activity) {
        super(activity);
        ButterKnife.bind(this, activity);
    }

    public GoogleMap getMap() {
        return map;
    }

    public void initMap() {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(getActivity());
    }

    public void showErrorDialog(GoogleApiAvailability api, int availableId) {
        Dialog dialog = api.getErrorDialog(getActivity(), availableId, 0);
        dialog.show();
    }

    public void showToast(int res) {
        Toast.makeText(getContext(), getResources().getString(res), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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


}
