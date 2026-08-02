package com.example.b07demosummer2024.model;


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

/**
 * RecyclerView adapter for displaying a list of ArtifactItem objects.
 * Binds artifact data to the item layout, loads images via Glide with a
 * placeholder fallback, and handles "Learn More" button clicks through
 * the OnArtifactClickListener interface.
 */
public class    ArtifactItemAdapter extends RecyclerView.Adapter<ArtifactItemAdapter.ItemViewHolder> {
    private List<ArtifactItem> itemList;
    private OnArtifactClickListener clickListener;

    /**
     * Constructs the adapter with an initial data set and click listener.
     *
     * @param itemList     The list of artifacts to display
     * @param clickListener Callback for "Learn More" button clicks
     */
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

    /**
     * ViewHolder class that holds references to all views in each item row.
     */
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

    /**
     * Interface for handling "Learn More" button clicks on artifact items.
     */
    public static interface OnArtifactClickListener {
        /**
         * Called when the "Learn More" button is clicked on an item.
         *
         * @param artifactIdentifier The lot number of the clicked artifact
         */
        void onLearnMoreClick(String artifactIdentifier);
    }

    /**
     * Updates the adapter's data set and refreshes the RecyclerView.
     *
     * @param newList The new list of artifacts to display
     */
    public void updateList(List<ArtifactItem> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }
}

