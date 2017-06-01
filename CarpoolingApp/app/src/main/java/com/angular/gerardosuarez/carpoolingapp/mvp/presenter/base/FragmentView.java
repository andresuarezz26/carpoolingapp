package com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.angular.gerardosuarez.carpoolingapp.activity.BaseActivity;

import java.lang.ref.WeakReference;

public class FragmentView<T extends Fragment> {
    private WeakReference<T> fragmentRef;

    protected FragmentView(T fragment) {
        fragmentRef = new WeakReference<>(fragment);
    }

    @Nullable
    public T getFragment() {
        return fragmentRef.get();
    }

    @Nullable
    public BaseActivity getActivity() {
        return (BaseActivity) fragmentRef.get().getActivity();
    }
}
