package com.angular.gerardosuarez.carpoolingapp.customviews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;


public class DialogPassengerQuota extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    private String name;
    private String description;
    private PublishSubject<Boolean> publishSubject = PublishSubject.create();


    public DialogPassengerQuota(Context context, String name, String description) {
        super(context);
        this.context = context;
        this.name = name;
        this.description = description;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quota_dialog);
        Button buttonAccept = (Button) findViewById(R.id.btn_yes);
        Button buttonCancel = (Button) findViewById(R.id.btn_no);
        TextView textName = (TextView) findViewById(R.id.txt_name);
        textName.setText(name);
        TextView textDescription = (TextView) findViewById(R.id.txt_description);
        textDescription.setText(description);

        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                publishSubject.onNext(true);
                dismiss();
                break;
            case R.id.btn_no:
                publishSubject.onNext(false);
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public void subscribeToDialogEvent(DisposableObserver<Boolean> observer) {
        publishSubject.subscribe(observer);
    }

    public void unsubscribeToDialogEvent() {
        publishSubject.onComplete();
    }
}