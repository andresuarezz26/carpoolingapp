package com.angular.gerardosuarez.carpoolingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MainPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 1;


    @BindView(R.id.bottom_navigation) BottomNavigationView bottomMenu;
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(new MainView(this));
        presenter.init();

        bottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_my_profile:
                                presenter.goToMyProfileFragment();
                                break;
                            case R.id.action_my_quota:
                                presenter.goToMyQuotaFragment();
                                break;
                            case R.id.action_map:
                                presenter.goToDriverMapFragment();
                                break;
                        }
                        return true;
                    }
                });
    }
}
