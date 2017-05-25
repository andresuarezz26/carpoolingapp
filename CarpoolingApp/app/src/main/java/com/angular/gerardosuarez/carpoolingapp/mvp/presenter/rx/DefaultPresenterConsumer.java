package com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

public abstract class DefaultPresenterConsumer<T, P> implements Consumer<T> {

    private final WeakReference<P> presenterWeakReference;

    public DefaultPresenterConsumer(@NonNull P presenter) {
        this.presenterWeakReference = new WeakReference<>(presenter);
    }

    public P getPresenter() {
        return presenterWeakReference.get();
    }
}
