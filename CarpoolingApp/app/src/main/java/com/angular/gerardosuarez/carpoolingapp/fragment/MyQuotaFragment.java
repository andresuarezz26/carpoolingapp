package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyQuotaPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyQuotaView;

import butterknife.ButterKnife;

public class MyQuotaFragment extends Fragment {

    public static final String TAG = "my_quota";
    private MyQuotaPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_quota, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MyQuotaPresenter(new MyQuotaView(this));
        presenter.init();
    }


}
