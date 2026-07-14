package com.example.b07demosummer2024;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
public class ArtifactRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void getAllArtifacts(OnArtifactsLoadedListener listener) {
        db.collection("artifacts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<ArtifactItem> list = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    list.add(document.toObject(ArtifactItem.class));
                }
                listener.onSuccess(list);
            } else {
                listener.onError(task.getException());
            }
        });
    }

    public interface OnArtifactsLoadedListener {
        void onSuccess(List<ArtifactItem> artifacts);
        void onError(Exception e);
    }
}
