package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.RegisterFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gerardosuarez on 7/08/17.
 */

public class RegisterView extends FragmentView<RegisterFragment, Void> {

    BottomNavigationView bottomMenu;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.edit_car_plate)
    EditText editCarPlate;
    @BindView(R.id.edit_car_color)
    EditText editCarColor;
    @BindView(R.id.edit_car_model)
    EditText editCarModel;


    public RegisterView(RegisterFragment fragment) {
        super(fragment);
        if (fragment.getView() != null) {
            ButterKnife.bind(this, fragment.getView());
            if (getActivity() != null)
                bottomMenu = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        }
    }

    public void hideMenu() {
        bottomMenu.setVisibility(View.GONE);
    }

    public void showMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }

    public void setInitialTexts(@Nullable String name,
                                @Nullable String email,
                                @Nullable String phoneNumber,
                                @Nullable String carPlate,
                                @Nullable String carColor,
                                @Nullable String carModel) {
        if (!TextUtils.isEmpty(name)) {
            editName.setText(name);
            editName.setEnabled(false);
        } else {
            editName.setEnabled(true);
        }

        if (!TextUtils.isEmpty(email)) {
            editEmail.setText(email);
            editEmail.setEnabled(false);
        } else {
            editEmail.setEnabled(true);
        }

        if (!TextUtils.isEmpty(phoneNumber)) {
            editPhone.setText(phoneNumber);
        }
        if (!TextUtils.isEmpty(carPlate)) {
            editCarPlate.setText(carPlate);
        }
        if (!TextUtils.isEmpty(carColor)) {
            editCarColor.setText(carColor);
        }
        if (!TextUtils.isEmpty(carModel)) {
            editCarModel.setText(carModel);
        }
    }


    @NonNull
    public User createUserFromForm() {
        User user = new User();
        user.email = editEmail.getText().toString();
        user.name = editName.getText().toString();
        user.phone = editPhone.getText().toString();
        user.car_color = editCarColor.getText().toString();
        user.car_plate = editCarPlate.getText().toString();
        user.car_model = editCarModel.getText().toString();
        return user;
    }
}
