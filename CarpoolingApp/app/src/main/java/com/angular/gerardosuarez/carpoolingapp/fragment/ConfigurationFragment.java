package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.init.InitPreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.fragment.base.BaseMapPreferenceFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.ConfigurationPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.ConfigurationView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigurationFragment extends BaseMapPreferenceFragment {

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
        presenter = new ConfigurationPresenter(
                new ConfigurationView(this),
                new UserService(),
                new InitPreferenceImpl(getActivity(), InitPreferenceImpl.NAME)
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

    @OnClick(R.id.btn_report_user)
    void onReportUser() {
        presenter.goToReportUserWebPage();
    }

    @OnClick(R.id.btn_active_user)
    void onActiveUserClick() {
        presenter.goToActivateUser();
    }
}
