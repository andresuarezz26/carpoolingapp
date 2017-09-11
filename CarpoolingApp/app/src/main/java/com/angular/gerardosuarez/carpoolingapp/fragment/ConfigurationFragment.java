package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.ConfigurationPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.ConfigurationView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigurationFragment extends Fragment {

    public static final String TAG = "configuration_fragment";
    private ConfigurationPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapPreference mapPreference = new MapPreferenceImpl(getActivity(), MapPreferenceImpl.NAME);
        presenter = new ConfigurationPresenter(
                mapPreference,
                new ConfigurationView(this),
                new UserService()
        );
        presenter.init();
    }

    @OnClick(R.id.btn_close_session)
    void onCloseSessionClick() {
        presenter.onCloseSession();
    }

    @OnClick(R.id.btn_report_issue)
    void onReportIssue() {
        presenter.goToReportIssueWebPage();
    }
}
