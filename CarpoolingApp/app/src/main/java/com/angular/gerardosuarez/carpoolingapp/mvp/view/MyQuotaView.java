package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.adapter.MyQuotaAdapter;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyQuotaFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.Passenger;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base.FragmentView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyQuotaView extends FragmentView<MyQuotaFragment, Integer> {
    @BindView(R.id.recycler_view_my_quota) RecyclerView recyclerView;
    private MyQuotaAdapter adapter;

    public MyQuotaView(MyQuotaFragment fragment) {
        super(fragment);
        ButterKnife.bind(this, fragment.getView());
    }

    public void init(List<Passenger> list) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        adapter = new MyQuotaAdapter(adapterObserver);
        adapter.addAll(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }


    public void addAll(List<Passenger> list) {
        adapter.addAll(list);
    }

    public void remove(int position) {
        adapter.remove(position);
    }
}
