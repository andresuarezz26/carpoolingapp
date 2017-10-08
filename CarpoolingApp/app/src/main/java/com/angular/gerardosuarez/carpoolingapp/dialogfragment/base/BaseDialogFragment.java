package com.angular.gerardosuarez.carpoolingapp.dialogfragment.base;

import android.app.DialogFragment;
import android.support.annotation.NonNull;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class BaseDialogFragment<O> extends DialogFragment {

    protected PublishSubject<String> publishSubject = PublishSubject.create();

    public void subscribeToDialogFragment(@NonNull DisposableObserver<String> observer) {
        publishSubject.subscribe(observer);
    }

    public void unsubscribeToDialogFragment() {
        publishSubject.onComplete();
    }
}
