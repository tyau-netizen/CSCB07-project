package com.example.b07demosummer2024.savedartifacts;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.b07demosummer2024.ArtifactFetcher;
import com.example.b07demosummer2024.base.BasePresenter;
import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.user.SavedArtifactsManager;
import com.example.b07demosummer2024.user.SessionManager;
import com.example.b07demosummer2024.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SavedArtifactsPresenter extends BasePresenter<SavedArtifactsContract.View>
        implements SavedArtifactsContract.Presenter {
    private static final String TAG = "SavedArtifactsPresenter";
    private final SessionManager sessionManager;
    private final ArtifactFetcher artifactFetcher;

    public SavedArtifactsPresenter() {
        sessionManager = SessionManager.getInstance();
        artifactFetcher = new ArtifactFetcher();
    }

    @Override
    public void loadSavedArtifacts() {
        User user = sessionManager.getCurrentUser();
        SavedArtifactsManager artifactsManager = user.getSavedArtifactsManager();
        Map<String, String> savedArtifacts = artifactsManager.getArtifacts();
        Log.d(TAG, "Loading artifacts");

        int totalItems = savedArtifacts.size();
        AtomicInteger completedCount = new AtomicInteger(0);

        List<ArtifactItem> artifactList = Collections.synchronizedList(new ArrayList<>());

        for (String artifactId : savedArtifacts.keySet()) {
            artifactFetcher.fetchItemData(artifactId, new ArtifactFetcher.OnItemFetchedListener() {
                @Override
                public void onSuccess(ArtifactItem item) {
                    artifactList.add(item);
                    Log.d(TAG, "Artifact added");
                    checkIfFinished();
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e(TAG, "Failed to load artifact " + artifactId + ": " + errorMessage);
                    checkIfFinished();
                }

                private void checkIfFinished() {
                    if (completedCount.incrementAndGet() == totalItems) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            view.displayArtifacts(new ArrayList<>(artifactList));
                        });
                    }
                }
            });
        }
    }
}
