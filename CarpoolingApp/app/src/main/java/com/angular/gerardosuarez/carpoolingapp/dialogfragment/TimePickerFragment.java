package com.angular.gerardosuarez.carpoolingapp.dialogfragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.angular.gerardosuarez.carpoolingapp.customviews.dialog.CustomTimePickerDialog;
import com.angular.gerardosuarez.carpoolingapp.dialogfragment.base.BaseDialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends BaseDialogFragment<Integer>
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new CustomTimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        publishSubject.onNext(hourOfDay + "" + minute);
        unsubscribeToDialogFragment();
    }
}
