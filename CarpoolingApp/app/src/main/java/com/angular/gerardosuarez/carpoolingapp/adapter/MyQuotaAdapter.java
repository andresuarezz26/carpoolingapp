package com.angular.gerardosuarez.carpoolingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.adapter.base.BaseAdapter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.Passenger;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class MyQuotaAdapter extends BaseAdapter<Passenger, Integer, MyQuotaAdapter.MyQuotaViewHolder> {

    public MyQuotaAdapter(Observer<Integer> observer) {
        super(observer);
    }

    @Override
    public void onBindViewHolder(MyQuotaViewHolder holder, int position) {
        holder.passenger = items.get(position);
        holder.position = position;
        String name = holder.passenger.getName();
        if (!TextUtils.isEmpty(name)) {
            holder.textName.setText(name);
        }
        String address = holder.passenger.getAddress();
        if (!TextUtils.isEmpty(address)) {
            holder.textAddress.setText(name);
        }
        if (hasObserver()) {
            holder.observerRef = new WeakReference<>(getObserver());
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
        @BindView(R.id.btn_my_quota_remove) ImageButton removeButton;
        private Passenger passenger;
        private WeakReference<Observer<Integer>> observerRef;
        private int position;


        MyQuotaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btn_my_quota_remove)
        void onRemoveClick() {
            if (observerRef == null) {
                return;
            }
            Observable.just(Integer.valueOf(position)).subscribe(observerRef.get());
        }
    }
}
