package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.InformationAppPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.InformationAppView;

import butterknife.ButterKnife;

public class InformationAppFragment extends Fragment {

    private InformationAppPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_app, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new InformationAppPresenter(
                new InformationAppView(this)
        );
        presenter.init();
    }
}
