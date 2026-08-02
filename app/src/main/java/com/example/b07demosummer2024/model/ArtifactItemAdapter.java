package com.example.b07demosummer2024.model;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;
//import static androidx.core.content.ContentProviderCompat.requireContext;

//import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.R;

import java.util.List;

public class    ArtifactItemAdapter extends RecyclerView.Adapter<ArtifactItemAdapter.ItemViewHolder> {
    private List<ArtifactItem> itemList;
    private OnArtifactClickListener clickListener;

    public ArtifactItemAdapter(List<ArtifactItem> itemList, OnArtifactClickListener clickListener) {
        this.itemList = itemList;
        this.clickListener = clickListener;
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

        String imageUrl = item.getImageUri();

        // Check if the URL is valid
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            // Load the image from the URL, but fallback to placeholder if it fails
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            // URL is EMPTY - explicitly load the placeholder
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.placeholder_image)
                    .centerCrop()
                    .into(holder.imageView);
        }


        holder.expandedBtn.setOnClickListener(v -> {
            if (clickListener != null) {
                String artifactId = item.getLotNumber();
                clickListener.onLearnMoreClick(artifactId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName, tvCategory, tvDynastyPeriod;
        Button expandedBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvCategory = itemView.findViewById(R.id.category);
            tvDynastyPeriod = itemView.findViewById(R.id.dynastyPeriod);
            imageView = itemView.findViewById(R.id.imageView);
            expandedBtn = itemView.findViewById(R.id.expandedBtn);
        }
    }


    public static interface OnArtifactClickListener {
        void onLearnMoreClick(String artifactIdentifier);
    }

    public void updateList(List<ArtifactItem> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }
}

