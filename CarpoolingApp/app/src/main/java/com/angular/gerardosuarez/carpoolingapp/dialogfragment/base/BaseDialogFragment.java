package com.angular.gerardosuarez.carpoolingapp.dialogfragment.base;

import android.app.DialogFragment;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;

public class BaseDialogFragment<O> extends DialogFragment {

    protected WeakReference<Observer<O>> observerRef;

    protected BaseDialogFragment(Observer<O> observer) {
        this.observerRef = new WeakReference<>(observer);
    }

    protected Observer<O> getObserver() {
        return observerRef.get();
    }
}
