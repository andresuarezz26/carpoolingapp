package com.angular.gerardosuarez.carpoolingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.adapter.base.BaseAdapter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.Passenger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyQuotaAdapter extends BaseAdapter<Passenger, Passenger, MyQuotaAdapter.MyQuotaViewHolder> {

    public MyQuotaAdapter() {
        super();
    }

    @Override
    public void onBindViewHolder(MyQuotaViewHolder holder, int position) {
        holder.passenger = items.get(position);
        String name = holder.passenger.getName();
        if (!TextUtils.isEmpty(name)) {
            holder.textName.setText(name);
        }
        String address = holder.passenger.getAddress();
        if (!TextUtils.isEmpty(address)) {
            holder.textAddress.setText(name);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.item_my_quota;
    }

    @NonNull
    @Override
    protected MyQuotaViewHolder getViewHolder(View view) {
        return new MyQuotaViewHolder(view);
    }

    class MyQuotaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_my_quota_name) TextView textName;
        @BindView(R.id.text_my_quota_address) TextView textAddress;
        private Passenger passenger;

        MyQuotaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
