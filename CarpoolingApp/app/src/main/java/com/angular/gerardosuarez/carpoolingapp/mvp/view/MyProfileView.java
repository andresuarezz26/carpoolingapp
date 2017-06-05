package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.DriverMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base.FragmentView;

public class MyProfileView extends FragmentView<MyProfileFragment> {

    FragmentManager fragmentManager;

    public MyProfileView(MyProfileFragment fragment) {
        super(fragment);
    }

    public void goToDriverMap() {
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
