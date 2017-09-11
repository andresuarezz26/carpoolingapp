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
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.InformationAppPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.InformationAppView;

import butterknife.ButterKnife;

public class InformationAppFragment extends Fragment {

    public static final String TAG = "information_app_fragment";
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
        MapPreference mapPreference = new MapPreferenceImpl(getActivity(), MapPreferenceImpl.NAME);
        presenter = new InformationAppPresenter(
                mapPreference,
                new InformationAppView(this)
        );
        presenter.init();
    }
}
