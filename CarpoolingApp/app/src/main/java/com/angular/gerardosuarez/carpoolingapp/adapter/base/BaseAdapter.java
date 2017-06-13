package com.angular.gerardosuarez.carpoolingapp.adapter.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;

public abstract class BaseAdapter<T, OT, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> items;
    private WeakReference<Observer<OT>> observerRef;

    public BaseAdapter(final Observer<OT> observer) {
        this.observerRef = new WeakReference<>(observer);
        this.items = new ArrayList<>();
    }

    protected void onNext(OT value) {
        final Observer<OT> observer = observerRef.get();
        if (observer == null) {
            return;
        }
        observer.onNext(value);
    }

    protected boolean hasObserver() {
        return observerRef.get() != null;
    }

    protected Observer<OT> getObserver() {
        return observerRef.get();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        return getViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @LayoutRes
    protected abstract int getLayout();

    @NonNull
    protected abstract VH getViewHolder(View view);

    public void add(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void add(List<T> newItems) {
        if (newItems.isEmpty()) {
            return;
        }
        int position = items.size() - 1;
        items.addAll(newItems);
        notifyItemRangeInserted(position, newItems.size());
    }

    public void add(int position, T item) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void add(int position, List<T> newItems) {
        items.addAll(position, newItems);
        notifyItemRangeInserted(position, newItems.size());
    }

    public void add(List<Integer> positions, List<T> newItems) {
        for (int i = 0; i < newItems.size(); i++) {
            int positionToInsert = positions.get(i);

            items.add(positionToInsert, newItems.get(i));
            notifyItemInserted(positionToInsert);
        }
    }

    public void addAll(List<T> list) {
        if (list.isEmpty()) {
            return;
        }
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(int position, List<T> newItems) {
        items.removeAll(newItems);
        notifyItemRangeRemoved(position, newItems.size());
    }

    public void remove(List<Integer> positions, List<T> newItems) {
        for (int i = newItems.size() - 1; i >= 0; i--) {
            items.remove(newItems.get(i));
            notifyItemRemoved(positions.get(i));
        }
    }

    public void removeAll() {
        items.clear();
        notifyDataSetChanged();
    }
}