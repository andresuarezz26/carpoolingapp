package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.data.preference.init.InitPreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.fragment.base.BaseMapPreferenceFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.TermsAndConditionPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.TermsAndConditionView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsAndConditionFragment extends BaseMapPreferenceFragment {

    public static final String TAG = "terms_and_condition_fragment";
    private TermsAndConditionPresenter presenter;

    @BindView(R.id.radio_button_terms_and_condition)
    RadioButton radioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_and_condition, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new TermsAndConditionPresenter(
                new InitPreferenceImpl(getActivity(), InitPreferenceImpl.NAME),
                new TermsAndConditionView(this)
        );
        presenter.init();

    }

    @OnClick(R.id.btn_continue)
    void onContinuecLICK() {
        if (radioButton.isChecked()) {
            presenter.setTermsAndConditionAccepted();
            MainActivity activity = (MainActivity) getActivity();
            if (activity == null) return;
            if (activity.getNavigationManager() == null) return;
            activity.getNavigationManager().chooseInitialScreen();
        }

    }
}
