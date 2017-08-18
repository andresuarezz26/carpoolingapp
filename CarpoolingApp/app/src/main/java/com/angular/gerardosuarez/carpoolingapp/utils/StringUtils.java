package com.angular.gerardosuarez.carpoolingapp.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public final class StringUtils {

    public static final String ENCODING = "UTF-8";
    private static final String SPLIT_SEPARATOR = " ";
    public static final String REGEX_FULL_NAME = "^[a-zA-Z0-9 - ! # $ % & ' * + - / = ? ^ _ ` { | } ~]*$";
    public static final String REGEX_ONLY_SPACES = "[ ]*";
    private static final String EMAIL_PATTERN = "^[A-Z0-9_.%+-]+@[A-Z0-9]+\\.[A-Z]{2,6}$";
    private static final String OUTPUT_CHARSET = "UTF-8";
    private static final String ERROR_ENCODING_STRING = "error encoding String";
    private static final String ERROR_DECODING_STRING = "error decoding String";
    private static final String DECIMAL_FORMAT_WITH_CENTS = "###,###,##0.00";
    private static final String DECIMAL_FORMAT_WITHOUT_CENTS = "###,###,##0.##";
    public static final String LINE_SEPARATOR = "\n";
    public static final String EMPTY_STRING = "\u200B";
    public static final String HORIZONTAL_ELLIPSIS = "%s\u2026";
    public static final String DOUBLE_BREAK = "\n\n";
    public static final String COLON = ",";
    public static final String DOUBLE_POINT = ":";
    private static final String ZERO_STRING = "0";
    private static final int INT_TEN = 10;
    private static final String DATE_PATTERN = "dd/mm/yyyy";

    private StringUtils() {

    }

    public static boolean applyRegex(@NonNull final String regex, @NonNull final String value) {
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(value);
        return m.matches();
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String htmlFormattedString) {
        if (TextUtils.isEmpty(htmlFormattedString)) {
            return null;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlFormattedString, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(htmlFormattedString);
        }
    }

    public static String buildRoute(String community, String fromOrTo, String date, String hour) {
        return fromOrTo + "-" + community + "/" + date + "/" + hour + "/";
    }

    public static String addZeroToStart(int number) {
        String formatNumber;
        if (number < INT_TEN) {
            formatNumber = ZERO_STRING + number;
        } else {
            formatNumber = number + EMPTY_STRING;
        }
        return formatNumber;
    }

    public static boolean isValidEmail(String email) {
        Pattern validEmailAddressRegex = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = validEmailAddressRegex.matcher(email);
        return matcher.find();
    }

    public static String capitalizeAllWords(final String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();
        final String[] values = value.split(SPLIT_SEPARATOR);
        for (int i = 0; i < values.length; i++) {
            String v = values[i];
            if (TextUtils.isEmpty(v)) {
                continue;
            }
            if (i > 0) {
                builder.append(SPLIT_SEPARATOR);
            }
            builder.append(v.substring(0, 1).toUpperCase());
            builder.append(v.substring(1).toLowerCase());
        }
        if (value.endsWith(SPLIT_SEPARATOR)) {
            builder.append(SPLIT_SEPARATOR);
        }
        return builder.toString();
    }

    public static String encodeString(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }

        try {
            byte[] data = string.getBytes(OUTPUT_CHARSET);
            return Base64.encodeToString(data, Base64.DEFAULT);

        } catch (Exception e) {
            Timber.e(ERROR_ENCODING_STRING, e.getMessage());
        }
        return string;
    }

    public static String decodeString(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }
        String stringExtra = string;
        try {
            byte[] data = Base64.decode(string, Base64.DEFAULT);
            stringExtra = new String(data, OUTPUT_CHARSET);
        } catch (Exception e) {
            Timber.e(ERROR_DECODING_STRING, e.getMessage());
        }
        return stringExtra;
    }


    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty() || EMPTY_STRING.equals(string);
    }

    public static String formatDate(@NonNull String date) {
        return date.substring(0, 2) +
                "/" +
                date.substring(3, 5) +
                "/" +
                date.substring(5, date.length());
    }

    public static String formatHour(@NonNull String hour) {
        return hour.substring(0, 2) +
                ":" +
                hour.substring(2, hour.length());
    }
}
