package com.example.b07demosummer2024;
import androidx.annotation.NonNull;

import com.example.b07demosummer2024.model.ArtifactItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArtifactFetcher {
    public interface OnItemFetchedListener {
        void onSuccess(ArtifactItem item);
        void onError(String errorMessage);
    }
    // Fetch
    public void fetchItemData(String ArtifactNo, OnItemFetchedListener listener) {
        DatabaseReference itemRef = FirebaseDatabase.getInstance()
                .getReference("artifacts")
                .child(ArtifactNo);
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Automatically builds Item object and fills the fields with strings!!!
                    ArtifactItem fetchedItem = snapshot.getValue(ArtifactItem.class);

                    if (fetchedItem != null) {
                        listener.onSuccess(fetchedItem); // Hand the clean object to the Fragment
                    } else {
                        listener.onError("Failed to convert database data into Item object.");
                    }
                } else {
                    listener.onError("Item not found in database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Firebase needs this overridden
                listener.onError(error.getMessage());
            }
        });
    }
}