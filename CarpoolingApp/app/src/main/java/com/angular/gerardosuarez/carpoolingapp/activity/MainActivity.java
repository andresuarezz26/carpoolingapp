package com.angular.gerardosuarez.carpoolingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MainPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MainView;
import com.angular.gerardosuarez.carpoolingapp.utils.BusProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (presenter == null) {
            presenter = new MainPresenter(new MainView(this, BusProvider.getInstance()));
        }
        presenter.init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        presenter.setMap(googleMap);
    }
}
