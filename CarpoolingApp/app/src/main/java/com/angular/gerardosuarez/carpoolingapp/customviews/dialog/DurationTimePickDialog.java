package com.angular.gerardosuarez.carpoolingapp.customviews.dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DurationTimePickDialog extends TimePickerDialog {
    final OnTimeSetListener mCallback;
    TimePicker mTimePicker;
    final int increment;

    public DurationTimePickDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView, int increment) {
        super(context, callBack, hourOfDay, minute / increment, is24HourView);
        this.mCallback = callBack;
        this.increment = increment;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null && mTimePicker != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                    mTimePicker.getCurrentMinute() * increment);
        }
    }

    @Override
    protected void onStop() {
        // override and do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class<?> rClass = Class.forName("com.android.internal.R$id");
            Field timePicker = rClass.getField("timePicker");
            this.mTimePicker = (TimePicker) findViewById(timePicker.getInt(null));
            Field m = rClass.getField("minute");

            NumberPicker mMinuteSpinner = (NumberPicker) mTimePicker.findViewById(m.getInt(null));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / increment) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += increment) {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
