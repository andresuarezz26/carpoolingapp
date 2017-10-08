package com.angular.gerardosuarez.carpoolingapp.data.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.utils.Preconditions;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class BasePreferenceImpl {

    private static final float DEFAULT_FLOAT = -1f;
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final int DEFAULT_INT = -1;
    private static final long DEFAULT_LONG = -1;
    private SharedPreferences preferences;

    public BasePreferenceImpl(final Context context, final String name) {
        Preconditions.notNull(context);
        this.preferences = context.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
    }

    protected void put(@NonNull String key, String value) {
        preferences.edit().putString(key, StringUtils.encodeString(value)).apply();
    }

    protected void put(@NonNull String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    protected void put(@NonNull String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    protected void put(@NonNull String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    protected void put(@NonNull String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    protected void put(@NonNull String key, Set<String> values) {
        preferences.edit().putStringSet(key, values).apply();
    }

    protected String getString(@NonNull String key) {
        return StringUtils.decodeString(preferences.getString(key, null));
    }

    protected boolean getBoolean(@NonNull String key) {
        return preferences.getBoolean(key, DEFAULT_BOOLEAN);
    }

    protected float getFloat(@NonNull String key) {
        return preferences.getFloat(key, DEFAULT_FLOAT);
    }

    protected int getInt(@NonNull String key) {
        return preferences.getInt(key, DEFAULT_INT);
    }

    protected long getLong(@NonNull String key) {
        return preferences.getLong(key, DEFAULT_LONG);
    }

    protected Set<String> getStringSet(@NonNull String key) {
        return preferences.getStringSet(key, null);
    }

    protected void remove(@NonNull String key) {
        preferences.edit().remove(key).apply();
    }
}
