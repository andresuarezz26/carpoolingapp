package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Dialog;
import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.squareup.otto.Bus;

import butterknife.ButterKnife;

public class MainView extends ActivityView<MainActivity> {

    private Bus bus;
    GoogleMap map;

    public MainView(MainActivity activity, Bus bus) {
        super(activity);
        ButterKnife.bind(this, activity);
        this.bus = bus;
    }

    public void initMap() {
        MainActivity activity = getActivity();
        if (getActivity() == null) {
            return;
        }
        if (activity.getFragmentManager() == null) {
            return;
        }
        MapFragment mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(activity);
    }

    public void showErrorDialog(GoogleApiAvailability api, int availableId) {
        Dialog dialog = api.getErrorDialog(getActivity(), availableId, 0);
        dialog.show();
    }

    public void showToast(int res) {
        Toast.makeText(getContext(), getResources().getString(res), Toast.LENGTH_LONG).show();
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
