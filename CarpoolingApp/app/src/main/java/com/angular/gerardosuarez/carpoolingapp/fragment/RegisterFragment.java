package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.RegisterPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.RegisterView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment {

    public static final String TAG = "register_fragment";
    private RegisterPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_form, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapPreference mapPreference = new MapPreferenceImpl(getActivity(), MapPreferenceImpl.NAME);
        presenter = new RegisterPresenter(
                mapPreference,
                new RegisterView(this),
                new UserService()

        );
        presenter.init();

    }

    @OnClick(R.id.btn_continue)
    void onContinueClick() {
        if (presenter.saveUserData()) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity == null) return;
            if (activity.getNavigationManager() == null) return;
            activity.getNavigationManager().chooseInitialScreen();
        }
    }
}
