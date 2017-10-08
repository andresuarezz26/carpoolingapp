package com.angular.gerardosuarez.carpoolingapp.customviews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.RequestInfo;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;


public class DialogPassengerMap extends Dialog implements
        View.OnClickListener {

    public Context context;
    private PassengerBooking passengerBooking;
    private RequestInfo requestInfo;
    private PublishSubject<Pair<PassengerBooking, RequestInfo>> publishSubject = PublishSubject.create();


    public DialogPassengerMap(Context context, PassengerBooking passengerBooking, RequestInfo requestInfo) {
        super(context);
        this.context = context;
        this.passengerBooking = passengerBooking;
        this.requestInfo = requestInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.passenger_accept_booking_dialog);
        Button buttonAccept = (Button) findViewById(R.id.btn_accept_quota);
        Button buttonCancel = (Button) findViewById(R.id.btn_cancel_quota);
        TextView startingPointText = (TextView) findViewById(R.id.txt_starting_point);
        TextView finalPointText = (TextView) findViewById(R.id.txt_final_point);
        TextView dateAndHourText = (TextView) findViewById(R.id.txt_date_and_hour);
        if (requestInfo != null) {
            if (requestInfo.getFromOrTo() != null) {
                if (MapPreference.FROM.equalsIgnoreCase(requestInfo.getFromOrTo())) {
                    startingPointText.setText(requestInfo.getCommunity());
                    finalPointText.setText(requestInfo.getAddress());
                } else {
                    startingPointText.setText(requestInfo.getAddress());
                    finalPointText.setText(requestInfo.getCommunity());
                }
                String date = StringUtils.formatDateWithTodayLogic(requestInfo.getDate());
                String hour = StringUtils.formatHour(requestInfo.getHour());
                dateAndHourText.setText(date + " " + hour);
            }
        }
        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept_quota:
                Pair<PassengerBooking, RequestInfo> pair = new Pair<>(passengerBooking, requestInfo);
                publishSubject.onNext(pair);
                dismiss();
                break;
            case R.id.btn_cancel_quota:
                publishSubject.onNext(null);
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public void subscribeToDialogEvent(DisposableObserver<Pair<PassengerBooking, RequestInfo>> observer) {
        publishSubject.subscribe(observer);
    }

    public void unsubscribeToDialogEvent() {
        publishSubject.onComplete();
    }
}