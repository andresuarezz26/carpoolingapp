package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by gerardosuarez on 2/03/17.
 */
public class ActivityView <T> {

    private WeakReference <T> activityRef;
    private ProgressDialog progressDialog;

    public ActivityView(T activity) {
        activityRef = new WeakReference<T>(activity);
    }

    @Nullable
    public T getActivity() {
        return activityRef.get();
    }

    @Nullable
    public Context getContext() {
        return (Context) getActivity();
    }

    public Resources getResources() {
        return getContext().getResources();
    }
}
