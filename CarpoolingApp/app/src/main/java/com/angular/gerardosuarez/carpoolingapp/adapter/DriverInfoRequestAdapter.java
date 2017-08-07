package com.angular.gerardosuarez.carpoolingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.adapter.base.BaseAdapter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class DriverInfoRequestAdapter extends BaseAdapter<DriverInfoRequest, Integer, DriverInfoRequestAdapter.DriverInfoRequestViewHolder> {

    public DriverInfoRequestAdapter(Observer<Integer> observer) {
        super(observer);
    }

    @Override
    public void onBindViewHolder(DriverInfoRequestViewHolder holder, int position) {
        holder.driverInfoRequest = items.get(holder.getAdapterPosition());
        String name = holder.driverInfoRequest.getDriverName();
        if (!TextUtils.isEmpty(name)) {
            holder.textName.setText(name);
        }
        String address = holder.driverInfoRequest.address;
        if (!TextUtils.isEmpty(address)) {
            holder.textAddress.setText(address);
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
    protected DriverInfoRequestViewHolder getViewHolder(View view) {
        return new DriverInfoRequestViewHolder(view);
    }

    class DriverInfoRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_my_quota_name)
        TextView textName;
        @BindView(R.id.text_my_quota_address)
        TextView textAddress;
        @BindView(R.id.btn_my_quota_remove)
        ImageButton removeButton;
        private DriverInfoRequest driverInfoRequest;
        private WeakReference<Observer<Integer>> observerRef;


        DriverInfoRequestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btn_my_quota_remove)
        void onRemoveClick() {
            if (observerRef == null) {
                return;
            }
            Observable.just(getAdapterPosition()).subscribe(observerRef.get());
        }
    }
}
