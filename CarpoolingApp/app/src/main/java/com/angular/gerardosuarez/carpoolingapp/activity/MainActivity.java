package com.angular.gerardosuarez.carpoolingapp.activity;

import android.os.Bundle;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MainPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MainView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(new MainView(this));
        presenter.init();
    }

    @OnClick(R.id.btn_main_map)
    void onMapClick() {
        presenter.goToDriverMapFragment();
    }
}
