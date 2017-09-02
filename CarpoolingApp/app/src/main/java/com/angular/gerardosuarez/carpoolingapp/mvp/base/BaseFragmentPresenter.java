package com.angular.gerardosuarez.carpoolingapp.mvp.base;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.RequestInfo;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseFragmentPresenter {
    protected String community;
    protected String fromOrTo;
    protected String date;
    protected String hour;
    protected DatabaseReference databaseRef;
    protected MapPreference mapPreference;
    protected FragmentView view;
    private UserService userService;
    private FirebaseAuth firebaseAuth;

    protected BaseFragmentPresenter(MapPreference mapPreference, FragmentView view, UserService userService) {
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
        this.mapPreference = mapPreference;
        this.view = view;
        this.userService = userService;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    protected boolean getMapPreferences() {
        community = mapPreference.getCommunity();
        if (TextUtils.isEmpty(community)) {
            view.showToast(R.string.error_empty_community);
            return false;
        }
        fromOrTo = mapPreference.getFromOrTo();
        if (TextUtils.isEmpty(fromOrTo)) {
            view.showToast(R.string.error_empty_from_or_to);
            return false;
        }
        date = mapPreference.getDate();
        if (TextUtils.isEmpty(date)) {
            view.showToast(R.string.error_empty_date);
            return false;
        }
        hour = mapPreference.getHour();
        if (TextUtils.isEmpty(hour)) {
            view.showToast(R.string.error_empty_hour);
            return false;
        }
        return true;
    }

    protected boolean getMapPreferencesWithoutErrorMsg() {
        community = mapPreference.getCommunity();
        if (TextUtils.isEmpty(community)) {
            return false;
        }
        fromOrTo = mapPreference.getFromOrTo();
        if (TextUtils.isEmpty(fromOrTo)) {
            return false;
        }
        date = mapPreference.getDate();
        if (TextUtils.isEmpty(date)) {
            return false;
        }
        hour = mapPreference.getHour();
        return !TextUtils.isEmpty(hour);
    }

    protected boolean alreadyDataChoosen() {
        return mapPreference.isAlreadyDataChoosen();
    }

    protected void putAlreadyDataChoosen(boolean isChoosen) {
        mapPreference.putAlreadyDataChoosen(isChoosen);
    }

    @Nullable
    protected RequestInfo getRequestInfo() {
        if (getMapPreferencesWithoutErrorMsg()) {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setDate(mapPreference.getDate());
            requestInfo.setHour(mapPreference.getHour());
            requestInfo.setCommunity(mapPreference.getCommunity());
            requestInfo.setFromOrTo(mapPreference.getFromOrTo());
            return requestInfo;
        }
        return null;
    }

    protected String getRoute() {
        return StringUtils.buildRoute(community, fromOrTo, date, hour);
    }

    @Nullable
    protected String getMyUid() {
        if (firebaseAuth.getCurrentUser() == null) return null;
        return firebaseAuth.getCurrentUser().getUid();

    }

    @Nullable
    protected User getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) return null;
        return userService.mapFirebaseUserToUser(firebaseUser);
    }

    protected void resetMapPreferences() {
        mapPreference.putDate(null);
        mapPreference.putTime(null);
        mapPreference.putFromOrTo(MapPreference.FROM);
    }
}
