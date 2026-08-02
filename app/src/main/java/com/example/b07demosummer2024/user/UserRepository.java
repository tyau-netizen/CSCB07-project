package com.example.b07demosummer2024.user;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    private static final String USER_KEY = "users";
    private final DatabaseReference databaseReference;

    public UserRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    public void fetchUserProfile(String uid, UserFetchCallback callback) {
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onSuccess(new User(snapshot));
                } else {
                    callback.onFailure(new UserNotFoundException());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    public void saveNewUserProfile(User user, UserSaveCallback callback) {
        databaseReference.child(user.getUid()).setValue(user.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void updateSavedArtifact(String uid, String artifactId, String orderKey,
                                    UserSaveCallback callback) {
        databaseReference.child(uid)
                .child(User.SAVED_ARTIFACTS_KEY)
                .child(artifactId)
                .setValue(orderKey)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (callback != null) callback.onSuccess();
                    } else {
                        if (callback != null) callback.onFailure(task.getException());
                    }
                });
    }

    public void addSavedArtifact(String uid, String artifactId, String orderKey,
                                 UserSaveCallback callback) {
        updateSavedArtifact(uid, artifactId, orderKey, callback);
    }

    public void removeSavedArtifact(String uid, String artifactId, UserSaveCallback callback) {
        updateSavedArtifact(uid, artifactId, null, callback);
    }

    public interface UserFetchCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }

    public interface UserSaveCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
