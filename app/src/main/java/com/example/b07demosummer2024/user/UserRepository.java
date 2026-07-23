package com.example.b07demosummer2024.user;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserRepository {
    private final DatabaseReference databaseReference;

    public UserRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public void fetchUserProfile(String uid, UserFetchCallback callback) {
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onSuccess(new User(snapshot));
                } else {
                    callback.onFailure(new Exception("User does not exist in database"));
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

    public interface UserFetchCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }

    public interface UserSaveCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
