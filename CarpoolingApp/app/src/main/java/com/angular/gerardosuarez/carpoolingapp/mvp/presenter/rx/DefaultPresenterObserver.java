package com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx;


import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DefaultPresenterObserver<T, P> implements Observer<T> {
    private WeakReference<P> weakReference;

    public DefaultPresenterObserver(P presenter) {
        this.weakReference = new WeakReference<>(presenter);
    }

    protected P getPresenter() {
        return weakReference.get();
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T value) {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
    }
}
