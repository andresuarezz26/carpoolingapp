package com.angular.gerardosuarez.carpoolingapp.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public final class StringUtils {

    public static final String ENCODING = "UTF-8";
    public static final String SPLIT_SEPARATOR = " ";
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

    public static String getPriceFormatted(float price) {
        DecimalFormat decimalFormat;
        if (price - (int) price > 0) {
            decimalFormat = new DecimalFormat(DECIMAL_FORMAT_WITH_CENTS);
            return decimalFormat.format((double) price);
        }
        decimalFormat = new DecimalFormat(DECIMAL_FORMAT_WITHOUT_CENTS);
        return decimalFormat.format((int) price);
    }

    public static boolean isValidEmail(String email) {
        Pattern validEmailAddressRegex = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = validEmailAddressRegex.matcher(email);
        return matcher.find();
    }

    public static String removeBarreled(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        return value.replace("-", " ");

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

    public static String getCharPerLineString(String text, int charSizePerLine) {
        String tenCharPerLineString = EMPTY_STRING;
        while (text.length() > charSizePerLine) {
            String buffer = text.substring(0, charSizePerLine);
            tenCharPerLineString = tenCharPerLineString + buffer + LINE_SEPARATOR;
            text = text.substring(charSizePerLine);
        }
        tenCharPerLineString = tenCharPerLineString + text.substring(0);
        return tenCharPerLineString;
    }

    public static String addEllipsisEndToText(String text, int charLimit) {
        if (text.length() < charLimit) {
            return text;
        } else {
            return String.format(HORIZONTAL_ELLIPSIS, text.substring(0, charLimit));
        }
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty() || EMPTY_STRING.equals(string);
    }
}
