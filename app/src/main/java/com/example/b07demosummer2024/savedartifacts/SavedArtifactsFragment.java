package com.example.b07demosummer2024.savedartifacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.base.BaseFragment;
import com.example.b07demosummer2024.databinding.FragmentSavedArtifactsBinding;
import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.model.ArtifactItemAdapter;
import com.example.b07demosummer2024.user.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SavedArtifactsFragment extends BaseFragment<FragmentSavedArtifactsBinding,
        SavedArtifactsContract.View, SavedArtifactsContract.Presenter>
        implements SavedArtifactsContract.View {
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private ArtifactItemAdapter adapter;

    public SavedArtifactsFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeRecyclerView();
    }

    @NonNull
    @Override
    protected FragmentSavedArtifactsBinding inflateBinding(@NonNull LayoutInflater inflater,
                                                           @Nullable ViewGroup container) {
        return FragmentSavedArtifactsBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    protected SavedArtifactsContract.Presenter createPresenter() {
        return new SavedArtifactsPresenter();
    }

    @Override
    public void displayArtifacts(List<ArtifactItem> artifacts) {
        adapter.updateList(artifacts);
    }

    private void initializeRecyclerView() {
        recyclerView = binding.savedArtifactRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ArtifactItemAdapter(new ArrayList<>(),
                new ArtifactItemAdapter.OnArtifactClickListener() {
            @Override
            public void onLearnMoreClick(String artifactIdentifier) {
                navigateToExpandedArtifact(artifactIdentifier);
            }
        });

        recyclerView.setAdapter(adapter);
        presenter.loadSavedArtifacts();
    }

    private void navigateToExpandedArtifact(String artifactId) {
        Bundle args = new Bundle();
        args.putString("ARTIFACT_NO", artifactId);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_savedArtifactsFragment_to_expandedArtifactFragment);
    }


}