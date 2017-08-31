package com.angular.gerardosuarez.carpoolingapp.data.preference.init;

import android.content.Context;

import com.angular.gerardosuarez.carpoolingapp.data.preference.BasePreferenceImpl;


public class InitPreferenceImpl extends BasePreferenceImpl implements InitPreference {

    public static final String NAME = "init_preferences";
    private static final String KEY_WAS_VIDEO_SHOWN = "KEY_WAS_VIDEO_SHOWN";
    private static final String KEY_WAS_TERMS_AND_COND_ACCEP = "KEY_WAS_TERMS_AND_COND_ACCEP";


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
}
