package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.support.design.widget.BottomNavigationView;
import android.view.View;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base.FragmentView;

import butterknife.ButterKnife;

public class MyProfileView extends FragmentView<MyProfileFragment, Void> {

    BottomNavigationView bottomMenu;

    public MyProfileView(MyProfileFragment fragment) {
        super(fragment);
        if (fragment.getView() != null) {
            ButterKnife.bind(this, fragment.getView());
            bottomMenu = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        }
    }

    public void hideMenu() {
        bottomMenu.setVisibility(View.GONE);
    }

    public void showMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }
}
