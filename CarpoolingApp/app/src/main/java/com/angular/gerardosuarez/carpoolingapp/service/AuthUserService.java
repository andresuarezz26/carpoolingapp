package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.service.rx.DefaultServicePublisher;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthUserService extends DefaultServicePublisher<Boolean> {

    private AuthActivity activity;

    private FirebaseAuth firebaseAuth;

    public AuthUserService(AuthActivity activity) {
        this.activity = activity;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> authUser(String username, String password) {
        return firebaseAuth.signInWithEmailAndPassword(username, password);
    }

    public Task<AuthResult> createUserWithEmail(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

/*    public void authUser(String username, String password) {
        //Do query to firebase here
        activity.getmAuth().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Timber.e("signInWithEmail:failed", task.getException());
                            notifyAuthResult(false);
                        } else {
                            notifyAuthResult(true);
                        }
                    }
                });
    }*/
}
