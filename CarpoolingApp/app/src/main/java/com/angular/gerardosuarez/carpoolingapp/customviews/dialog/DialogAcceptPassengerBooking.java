package com.angular.gerardosuarez.carpoolingapp.customviews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.squareup.picasso.Picasso;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;


public class DialogAcceptPassengerBooking extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    private PassengerBooking passengerBooking;
    private PublishSubject<PassengerBooking> publishSubject = PublishSubject.create();


    public DialogAcceptPassengerBooking(Context context, PassengerBooking passengerBooking) {
        super(context);
        this.context = context;
        this.passengerBooking = passengerBooking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quota_dialog);
        ImageView imageFacebook = (ImageView) findViewById(R.id.image_facebook);
        Button buttonAccept = (Button) findViewById(R.id.btn_accept_quota);
        Button buttonCancel = (Button) findViewById(R.id.btn_cancel_quota);
        TextView textName = (TextView) findViewById(R.id.txt_name);
        if (passengerBooking != null) {
            if (passengerBooking.getName() != null) {
                textName.setText(passengerBooking.getName());
            }
            TextView textDescription = (TextView) findViewById(R.id.txt_description);
            if (passengerBooking.address != null) {
                textDescription.setText(passengerBooking.address);
            }
            if (!TextUtils.isEmpty(passengerBooking.getPhotoUri())) {
                Picasso.with(imageFacebook.getContext()).load(passengerBooking.getPhotoUri()).into(imageFacebook);
            }
        }
        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept_quota:
                publishSubject.onNext(passengerBooking);
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

    public void subscribeToDialogEvent(DisposableObserver<PassengerBooking> observer) {
        publishSubject.subscribe(observer);
    }

    public void unsubscribeToDialogEvent() {
        publishSubject.onComplete();
    }
}