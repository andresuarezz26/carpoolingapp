package com.angular.gerardosuarez.carpoolingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.AuthModel;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.AuthPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.utils.BusProvider;
import com.angular.gerardosuarez.carpoolingapp.utils.ServiceUtils;

import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

    AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if( presenter == null){
            presenter = new AuthPresenter(new AuthModel(ServiceUtils.getItemService()), new AuthView(this,
                    BusProvider.getInstance()));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.unregister(presenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.register(presenter);
    }
}
