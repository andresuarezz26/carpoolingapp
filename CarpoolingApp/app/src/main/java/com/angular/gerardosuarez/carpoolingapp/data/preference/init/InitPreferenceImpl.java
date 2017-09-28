package com.angular.gerardosuarez.carpoolingapp.data.preference.init;

import android.content.Context;

import com.angular.gerardosuarez.carpoolingapp.data.preference.BasePreferenceImpl;


public class InitPreferenceImpl extends BasePreferenceImpl implements InitPreference {

    public static final String NAME = "init_preferences";
    private static final String KEY_WAS_VIDEO_SHOWN = "KEY_WAS_VIDEO_SHOWN";
    private static final String KEY_WAS_TERMS_AND_COND_ACCEP = "KEY_WAS_TERMS_AND_COND_ACCEP";
    private static final String KEY_IS_ALREADY_REGISTRED = "KEY_IS_ALREADY_REGISTRED";
    private static final String KEY_WAS_COMMUNITY_CHOOSED = "KEY_WAS_COMMUNITY_CHOOSED";


    public InitPreferenceImpl(Context context, String name) {
        super(context, name);
    }

    @Override
    public boolean wasVideoShown() {
        return getBoolean(KEY_WAS_VIDEO_SHOWN);
    }

    @Override
    public void putWasVideoShown(boolean value) {
        put(KEY_WAS_VIDEO_SHOWN, value);
    }

    @Override
    public boolean wasTermsAndConditionsAccepted() {
        return getBoolean(KEY_WAS_TERMS_AND_COND_ACCEP);
    }

    @Override
    public void putWasTermsAndCondictionsAccepted(boolean value) {
        put(KEY_WAS_TERMS_AND_COND_ACCEP, value);
    }

    @Override
    public boolean isAlreadyRegistered() {
        return getBoolean(KEY_IS_ALREADY_REGISTRED);
    }

    @Override
    public void putAlreadyRegistered(boolean value) {
        put(KEY_IS_ALREADY_REGISTRED, value);
    }

    @Override
    public boolean wasComunityChoosed() {
        return getBoolean(KEY_WAS_COMMUNITY_CHOOSED);
    }

    @Override
    public void putCommunityChoosed(boolean value) {
        put(KEY_WAS_COMMUNITY_CHOOSED, value);
    }
}
