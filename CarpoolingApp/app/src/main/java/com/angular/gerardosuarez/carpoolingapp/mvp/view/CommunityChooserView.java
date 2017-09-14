package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.support.design.widget.BottomNavigationView;
import android.view.View;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.CommunityChooserFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;

import butterknife.ButterKnife;

public class CommunityChooserView extends FragmentView<CommunityChooserFragment, Void> {

    private BottomNavigationView bottomMenu;

    public CommunityChooserView(CommunityChooserFragment fragment) {
        super(fragment);
        if (fragment.getView() != null) {
            ButterKnife.bind(this, fragment.getView());
            if (getActivity() != null)
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
