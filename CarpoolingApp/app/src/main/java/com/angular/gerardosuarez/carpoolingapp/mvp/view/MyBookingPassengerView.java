package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingPassengerFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBookingPassengerView extends FragmentView<MyBookingPassengerFragment, Integer> {

    @BindView(R.id.txt_driver_name)
    TextView textDriverName;
    @BindView(R.id.txt_driver_phone)
    TextView textDriverPhone;
    @BindView(R.id.txt_driver_address)
    TextView textDriverAddress;
    @BindView(R.id.txt_driver_date)
    TextView textDriverDate;
    @BindView(R.id.txt_driver_hour)
    TextView textDriverHour;
    @BindView(R.id.image_photo)
    ImageView imagePhoto;

    public MyBookingPassengerView(MyBookingPassengerFragment fragment) {
        super(fragment);
        if (fragment.getView() != null)
            ButterKnife.bind(this, fragment.getView());
    }

    public void init() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
    }

    public void setDriverInfo(@NonNull DriverInfoRequest driverInfo, @NonNull String date, @NonNull String hour) {
        if (TextUtils.isEmpty(driverInfo.getDriverName())) {
            textDriverName.setText(StringUtils.EMPTY_STRING);
        } else {
            textDriverName.setText(driverInfo.getDriverName());
        }
        if (TextUtils.isEmpty(driverInfo.getDriverPhone())) {
            textDriverPhone.setText(StringUtils.EMPTY_STRING);
        } else {
            textDriverPhone.setText(driverInfo.getDriverPhone());
        }
        if (TextUtils.isEmpty(driverInfo.address)) {
            textDriverAddress.setText(StringUtils.EMPTY_STRING);
        } else {
            textDriverAddress.setText(driverInfo.address);
        }
        if (!TextUtils.isEmpty(driverInfo.getDriverPhotoUri())) {
            Picasso.with(getActivity()).load(driverInfo.getDriverPhotoUri()).into(imagePhoto);
        }
        textDriverDate.setText(date);
        textDriverHour.setText(hour);
    }

    public void cleanTexts() {
        textDriverPhone.setText(StringUtils.EMPTY_STRING);
        textDriverAddress.setText(StringUtils.EMPTY_STRING);
        textDriverDate.setText(StringUtils.EMPTY_STRING);
        textDriverHour.setText(StringUtils.EMPTY_STRING);
    }

    public void setInitialSearchingDriverInfo() {
        if (getActivity() == null) return;
        textDriverPhone.setText(getActivity().getResources().getText(R.string.my_booking_passenger_searching));
        textDriverAddress.setText(getActivity().getResources().getText(R.string.my_booking_passenger_description));
        textDriverDate.setText(StringUtils.EMPTY_STRING);
        textDriverHour.setText(StringUtils.EMPTY_STRING);
    }


}
