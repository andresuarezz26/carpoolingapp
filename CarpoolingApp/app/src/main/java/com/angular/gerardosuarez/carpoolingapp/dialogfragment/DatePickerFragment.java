package com.angular.gerardosuarez.carpoolingapp.dialogfragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.angular.gerardosuarez.carpoolingapp.dialogfragment.base.BaseDialogFragment;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

import java.util.Calendar;

public class DatePickerFragment extends BaseDialogFragment<Integer>
        implements DatePickerDialog.OnDateSetListener {

    private static final int CORRECTION_ZERO_INDEX_ARRAY_FOR_MONTHS = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        publishSubject.onNext(StringUtils.addZeroToStart(dayOfMonth) + "" + StringUtils.addZeroToStart(month + CORRECTION_ZERO_INDEX_ARRAY_FOR_MONTHS) + "" + year);
        unsubscribeToDialogFragment();
    }
}
