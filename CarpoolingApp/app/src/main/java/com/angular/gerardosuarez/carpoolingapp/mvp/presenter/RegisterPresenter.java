package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.init.InitPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.RegisterView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;


public class RegisterPresenter extends BaseFragmentPresenter {

    private RegisterView view;
    private UserService userService;
    private InitPreference initPreference;

    public RegisterPresenter(RegisterView view, UserService userService, InitPreference initPreference) {
        super(view, userService);
        this.view = view;
        this.userService = userService;
        this.initPreference = initPreference;
    }

    public void init() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            listenForCurrentUser(currentUser.getKey());
        }
        hideMenu();
    }

    private void listenForCurrentUser(String uid) {
        userService.getUserByUid(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            view.setInitialTexts(
                                    user.name,
                                    user.email,
                                    user.phone,
                                    user.car_plate,
                                    user.car_color,
                                    user.car_model);
                        }
                    }
                } catch (DatabaseException e) {
                    Timber.e(e.getMessage(), e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(databaseError.getMessage(), databaseError);
            }
        });
    }

    public boolean saveUserData() {
        User user = view.createUserFromForm();
        if (validatePhoneNumber(user)) {
            User currentUserFromFirebaseDatabase = getCurrentUser();
            FirebaseUser firebaseUser = getFirebaseUser();
            if (firebaseUser != null) {
                user.photo_uri = getPhotoUrl(firebaseUser);
            } else {
                if (currentUserFromFirebaseDatabase != null) {
                    user.photo_uri = currentUserFromFirebaseDatabase.photo_uri;
                }
            }
            if (getMyUid() != null) {
                user.setKey(getMyUid());
                userService.createOrUpdateUser(user);
                initPreference.putAlreadyRegistered(true);
                return true;
            } else {
                initPreference.putAlreadyRegistered(false);
                return false;
            }
        } else {
            return false;
        }
    }

    @Nullable
    private String getPhotoUrl(final FirebaseUser user) {
        String facebookUserId = StringUtils.EMPTY_STRING;
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }
            return "https://graph.facebook.com/" + facebookUserId + "/picture?height=200";
        } else {
            return facebookUserId;
        }
    }

    private boolean validatePhoneNumber(User user) {
        if (TextUtils.isEmpty(user.phone)) {
            view.showToast(R.string.error_empty_fields);
            return false;
        } else {
            if (user.phone.length() != 10) {
                view.showToast(R.string.error_phone_number_characters);
                return false;
            }
        }
        return true;
    }

    public void hideMenu() {
        view.hideMenu();
    }
}
