package com.example.b07demosummer2024.savedartifacts;

import com.example.b07demosummer2024.base.BaseContract;
import com.example.b07demosummer2024.model.ArtifactItem;

import java.util.List;

public interface SavedArtifactsContract {
    interface View extends BaseContract.View {
        void displayArtifacts(List<ArtifactItem> artifactList);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void loadSavedArtifacts();
    }
}
