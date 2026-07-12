package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.model.ArtifactItemAdapter;
import com.example.b07demosummer2024.model.DummyData;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewStaticFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArtifactItemAdapter itemAdapter;
    private List<ArtifactItem> itemList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();
        loadStaticItems();

        itemAdapter = new ArtifactItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        return view;
    }

    private void loadStaticItems() {
        // Load static items from strings.xml or hardcoded values
        itemList.add(DummyData.createArtifact1());
        itemList.add(DummyData.createArtifact2());
        itemList.add(DummyData.createArtifact3());
        itemList.add(DummyData.createArtifact4());
        itemList.add(DummyData.createArtifact5());
        itemList.add(DummyData.createArtifact6());
        itemList.add(DummyData.createArtifact7());
        itemList.add(DummyData.createArtifact8());
    }
}
