package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.view.View;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.BaseActivity;
import com.angular.gerardosuarez.carpoolingapp.fragment.ConfigurationFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;

import butterknife.ButterKnife;

public class ConfigurationView extends FragmentView<ConfigurationFragment, Void> {

    private static final String REPORT_ISSUE_URL = "https://www.miscupos.com/reportarusuarioapp";
    private BottomNavigationView bottomMenu;

    public ConfigurationView(ConfigurationFragment fragment) {
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

    public void finishActivity() {
        final BaseActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public void init() {

    }

    public void goToReportIssueWebPage() {
        final BaseActivity activity = getActivity();
        if (activity != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(REPORT_ISSUE_URL));
            activity.startActivity(i);
        }

    }
}
