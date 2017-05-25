package com.angular.gerardosuarez.carpoolingapp.service.rx;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class DefaultServicePublisher<T> {
    protected PublishSubject<T> subject = PublishSubject.create();

    protected void notifyAuthResult(T t) {
        subject.onNext(t);
    }

    public void addOnAuthResultConsumer(Consumer<T> consumer) {
        subject.subscribe(consumer);
    }
}
