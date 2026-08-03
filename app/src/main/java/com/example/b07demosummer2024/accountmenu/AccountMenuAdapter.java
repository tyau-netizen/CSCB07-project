package com.example.b07demosummer2024.accountmenu;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.databinding.ItemAccountMenuBinding;

import java.util.List;

public class AccountMenuAdapter extends RecyclerView.Adapter<AccountMenuAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    private final List<MenuItem> menuItems;
    private final OnItemClickListener listener;

    public AccountMenuAdapter(List<MenuItem> menuItems, OnItemClickListener listener) {
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAccountMenuBinding binding = ItemAccountMenuBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        holder.binding.menuTitle.setText(item.getTitle());
        holder.binding.menuIcon.setImageResource(item.getIconResId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemAccountMenuBinding binding;

        ViewHolder(ItemAccountMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
