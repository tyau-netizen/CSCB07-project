package com.example.b07demosummer2024;
import androidx.annotation.NonNull;

import com.example.b07demosummer2024.model.ArtifactItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Little helper class for grabbing a single artifact's data out of Firebase
 * by its lot number. Just wraps the Firebase listener stuff so the
 * fragments don't have to deal with it directly.
 */
public class ArtifactFetcher {
    /**
     * Callback for getting the result of a fetch back, since Firebase reads
     * are async and don't just return a value directly.
     */
    public interface OnItemFetchedListener {
        /**
         * Called when the artifact was found and successfully converted.
         *
         * @param item the fetched artifact
         */
        void onSuccess(ArtifactItem item);

        /**
         * Called when the fetch failed for whatever reason (not found,
         * couldn't parse, Firebase error, etc).
         *
         * @param errorMessage a human readable reason for why it failed
         */
        void onError(String errorMessage);
    }
    // Fetch
    /**
     * Looks up an artifact in Firebase by its lot number and hands it back
     * through the listener. Doesn't return anything directly since Firebase
     * reads happen async in the background.
     *
     * @param ArtifactNo the lot number of the artifact to fetch
     * @param listener called with the artifact on success, or an error message on failure
     */
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