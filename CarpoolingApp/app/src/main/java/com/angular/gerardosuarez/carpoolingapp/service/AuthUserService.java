package com.angular.gerardosuarez.carpoolingapp.service;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class AuthUserService {

    private PublishSubject<Boolean> subject = PublishSubject.create();

    public void authUser(String username, String password) {
        //Do query to firebase here
        Boolean resultLogin = true;

        notifyAuthResult(resultLogin);
    }

    private void notifyAuthResult(Boolean isLoged) {
        subject.onNext(isLoged);
    }

    public void addOnAuthResultConsumer(Consumer<Boolean> consumer) {
        subject.subscribe(consumer);
    }
}
