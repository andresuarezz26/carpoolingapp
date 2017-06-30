package com.angular.gerardosuarez.carpoolingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyMapFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MainPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MainView;
import com.angular.gerardosuarez.carpoolingapp.navigation.NavigationManager;

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
        final RolePreference preference = new RolePreferenceImpl(this, RolePreferenceImpl.NAME);
        navigationManager = NavigationManager.getInstance(getFragmentManager(), preference);
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
                                navigationManager.goToMapFragment();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyMapFragment fragment = navigationManager.getDriverMapFragment();
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }
}
