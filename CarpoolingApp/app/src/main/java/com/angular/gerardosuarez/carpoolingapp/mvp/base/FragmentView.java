package com.angular.gerardosuarez.carpoolingapp.mvp.base;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.activity.BaseActivity;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;

public class FragmentView<T extends Fragment, AO> {
    private WeakReference<T> fragmentRef;
    protected Observer<AO> adapterObserver;

    protected FragmentView(T fragment) {
        fragmentRef = new WeakReference<>(fragment);
    }

    public Observer<AO> getAdapterObserver() {
        return adapterObserver;
    }

    public void setAdapterObserver(Observer<AO> observer) {
        this.adapterObserver = observer;
    }

    @Nullable
    public T getFragment() {
        return fragmentRef.get();
    }

    @Nullable
    public Context getContext() {
        return getActivity();
    }

    @Nullable
    public BaseActivity getActivity() {
        return (BaseActivity) fragmentRef.get().getActivity();
    }

    public void showToast(int res) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(res), Toast.LENGTH_SHORT).show();
        }
    }
}
