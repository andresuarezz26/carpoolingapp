package com.angular.gerardosuarez.carpoolingapp.mvp.base;

import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
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

    protected BaseFragmentPresenter(MapPreference mapPreference, FragmentView view) {
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
        this.mapPreference = mapPreference;
        this.view = view;
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
        hour = mapPreference.getTime();
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
        hour = mapPreference.getTime();
        if (TextUtils.isEmpty(hour)) {
            return false;
        }
        return true;
    }


    protected String getRoute() {
        return StringUtils.buildRoute(community, fromOrTo, date, hour);
    }

    protected String getMyUid() {
        return "user1";
    }

    protected void resetMapPreferences() {
        mapPreference.putDate(null);
        mapPreference.putFromOrTo(null);
        mapPreference.putTime(null);
    }
}
