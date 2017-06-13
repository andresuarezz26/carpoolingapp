package com.angular.gerardosuarez.carpoolingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MainPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MainView;
import com.angular.gerardosuarez.carpoolingapp.navigation.NavigationManager;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

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
                                break;
                            case R.id.action_my_quota:
                                navigationManager.goToMyQuotaFragment();
                                break;
                            case R.id.action_map:
                                navigationManager.goToDriverMapFragment();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationManager.destroyNavigation();
    }
}
