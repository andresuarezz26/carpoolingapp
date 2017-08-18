package com.angular.gerardosuarez.carpoolingapp.fragment.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;

/**
 * Created by gerardosuarez on 17/08/17.
 */

public class BaseFragment extends Fragment implements View.OnKeyListener {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(this);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.getNavigationManager().goToMyProfileFragment();
            }
            return true;
        }
        return false;
    }
}
