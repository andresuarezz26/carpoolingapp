package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.fragment.DriverMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base.ActivityView;

import butterknife.ButterKnife;

public class MainView extends ActivityView<MainActivity> {


    private FragmentManager fragmentManager;

    public MainView(MainActivity activity) {
        super(activity);
        ButterKnife.bind(this, activity);
    }

    public void init() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, new MyProfileFragment());
        transaction.commit();
    }

    public void goToDriverMapFragment() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, new DriverMapFragment());
        transaction.commit();
    }
}
