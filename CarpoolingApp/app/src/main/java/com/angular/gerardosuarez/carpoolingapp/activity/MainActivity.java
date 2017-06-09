package com.angular.gerardosuarez.carpoolingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MainPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MainView;
import com.angular.gerardosuarez.carpoolingapp.navigation.NavigationManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    public static final String DRIVER_MAP_FRAGMENT = "DRIVER_MAP_FRAGMENT";
    public static final String MY_PROFILE_FRAGMENT = "MY_PROFILE_FRAGMENT";
    public static final String MY_QUOTA_FRAGMENT = "MY_QUOTA_FRAGMENT";

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomMenu;
    MainPresenter presenter;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(new MainView(this));
        presenter.init();
        navigationManager = NavigationManager.getInstance(getFragmentManager());
        navigationManager.goToMyProfileFragment();

        bottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_my_profile:
                                navigationManager.goToMyProfileFragment();
                                //presenter.goToMyProfileFragment();
                                break;
                            case R.id.action_my_quota:
                                navigationManager.goToMyQuotaFragment();
                                //presenter.goToMyQuotaFragment();
                                break;
                            case R.id.action_map:
                                navigationManager.goToDriverMapFragment();
                                //presenter.goToDriverMapFragment();
                                break;
                        }
                        return true;
                    }
                });
    }
}
