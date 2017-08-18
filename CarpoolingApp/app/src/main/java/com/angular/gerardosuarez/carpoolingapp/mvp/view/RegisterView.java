package com.angular.gerardosuarez.carpoolingapp.mvp.view;

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

    public void setInitialTexts(String name, String email, String phone) {
        if (!TextUtils.isEmpty(name)) {
            editName.setText(name);
            editName.setEnabled(false);
        }
        if (!TextUtils.isEmpty(email)) {
            editEmail.setText(email);
            editEmail.setEnabled(false);
        }
        if (!TextUtils.isEmpty(phone)) {
            editPhone.setText(phone);
            editPhone.setEnabled(false);
        }
    }

    public User createUserFromForm() {
        User user = new User();
        user.email = editEmail.getText().toString();
        user.name = editName.getText().toString();
        user.phone = editPhone.getText().toString();
        return user;
    }
}
