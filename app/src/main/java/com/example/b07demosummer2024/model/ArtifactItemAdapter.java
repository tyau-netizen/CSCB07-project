package com.example.b07demosummer2024.model;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;
//import static androidx.core.content.ContentProviderCompat.requireContext;

//import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.R;

import java.util.List;

public class ArtifactItemAdapter extends RecyclerView.Adapter<ArtifactItemAdapter.ItemViewHolder> {
    private List<ArtifactItem> itemList;

    public ArtifactItemAdapter(List<ArtifactItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recycler_view_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ArtifactItem item = itemList.get(position);
        holder.tvName.setText(item.getName());
        if (item.getCategory() != null) {
            holder.tvCategory.setText(item.getCategory().toString());
        } else {
            holder.tvCategory.setText("Unknown Category");
        }

        if (item.getDynastyPeriod() != null) {
            holder.tvDynastyPeriod.setText(item.getDynastyPeriod().toString());
        } else {
            holder.tvDynastyPeriod.setText("Unknown Dynasty Period");
        }

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUri())
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.imageView);
        } else {
            // no image

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName, tvCategory, tvDynastyPeriod;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvCategory = itemView.findViewById(R.id.category);
            tvDynastyPeriod = itemView.findViewById(R.id.dynastyPeriod);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
