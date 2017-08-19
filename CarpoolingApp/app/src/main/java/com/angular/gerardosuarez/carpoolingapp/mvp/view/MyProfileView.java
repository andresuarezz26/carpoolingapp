package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileView extends FragmentView<MyProfileFragment, Void> {

    public static final String COMMUNITY_TEXT = "Comunidad ";
    BottomNavigationView bottomMenu;
    @BindView(R.id.txt_community_my_profile)
    TextView textCommunity;

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

    public void setCommunityText(String communityText) {
        textCommunity.setText(COMMUNITY_TEXT + communityText);
    }
}
