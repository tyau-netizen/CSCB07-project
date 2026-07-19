package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.model.ArtifactItemAdapter;
import com.example.b07demosummer2024.model.Category;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArtifactItemAdapter itemAdapter;
    private List<ArtifactItem> itemList = new ArrayList<>();
    private List<ArtifactItem> masterList = new ArrayList<>();

    private String currentCategoryFilter = "All Artifacts";
    private String currentSearchQuery = "";

    private SearchView searchView;
    private Spinner spinnerCategory;

    private FirebaseDatabase db;

    private static final String ALL_ARTIFACTS_LABEL = "All Artifacts";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Not needed, we filter as user types
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                applyFilters();
                return true;
            }
        });

        // Setup Spinner (dynamic categories)
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        Category[] categories = Category.values();
        List<String> displayNames = new ArrayList<>();
        displayNames.add(ALL_ARTIFACTS_LABEL);
        for (Category c : categories) {
            displayNames.add(c.getDisplayName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, displayNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    currentCategoryFilter = ALL_ARTIFACTS_LABEL;
                } else {
                    currentCategoryFilter = categories[position - 1].getDisplayName();
                }
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Setup RecyclerView Adapter
        itemAdapter = new ArtifactItemAdapter(itemList, new ArtifactItemAdapter.OnArtifactClickListener() {
            @Override
            public void onLearnMoreClick(String artifactIdentifier) {
                navigateToDetailFragment(artifactIdentifier);
            }
        });
        recyclerView.setAdapter(itemAdapter);

        // Initialize Firebase
        db = FirebaseDatabase.getInstance("https://taam-100-default-rtdb.firebaseio.com/");

        // Fetch all artifacts once
        fetchAllArtifactsOnce();

        return view;
    }

    private void fetchAllArtifactsOnce() {
        DatabaseReference artifactsRef = db.getReference("artifacts");
        artifactsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                masterList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ArtifactItem item = snapshot.getValue(ArtifactItem.class);
                    if (item != null) {
                        masterList.add(item);
                    }
                }
                applyFilters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("error", "Failed to fetch artifacts", databaseError.toException());
            }
        });
    }

    private void applyFilters() {
        List<ArtifactItem> filteredList = new ArrayList<>();

        for (ArtifactItem item : masterList) {
            // 1. Category filter
            boolean matchesCategory = currentCategoryFilter.equals(ALL_ARTIFACTS_LABEL) ||
                    (item.getCategory() != null &&
                            item.getCategory().getDisplayName().equalsIgnoreCase(currentCategoryFilter));

            if (!matchesCategory) {
                continue;
            }

            // 2. Search query filter
            if (!currentSearchQuery.isEmpty()) {
                boolean matchesSearch = matchesSearchQuery(item, currentSearchQuery);
                if (!matchesSearch) {
                    continue;
                }
            }

            filteredList.add(item);
        }

        itemAdapter.updateList(filteredList);
    }
    private boolean matchesSearchQuery(ArtifactItem item, String query) {
        String lowerQuery = query.toLowerCase().trim();
        if (lowerQuery.isEmpty()) {
            return true;
        }

        // Check all string fields
        if (item.getName() != null && item.getName().toLowerCase().contains(lowerQuery)) return true;
        if (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerQuery)) return true;
        if (item.getLotNumber() != null && item.getLotNumber().toLowerCase().contains(lowerQuery)) return true;
        if (item.getCulturalOrigin() != null && item.getCulturalOrigin().toLowerCase().contains(lowerQuery)) return true;
        if (item.getDimensions() != null && item.getDimensions().toLowerCase().contains(lowerQuery)) return true;
        if (item.getConditionReport() != null && item.getConditionReport().toLowerCase().contains(lowerQuery)) return true;
        if (item.getCurrentLocation() != null && item.getCurrentLocation().toLowerCase().contains(lowerQuery)) return true;
        if (item.getAcquisitionMethod() != null && item.getAcquisitionMethod().toLowerCase().contains(lowerQuery)) return true;
        if (item.getProvenance() != null && item.getProvenance().toLowerCase().contains(lowerQuery)) return true;
        if (item.getAccessionNumber() != null && item.getAccessionNumber().toLowerCase().contains(lowerQuery)) return true;
        if (item.getNotes() != null && item.getNotes().toLowerCase().contains(lowerQuery)) return true;

        // Check enum display names
        if (item.getCategory() != null && item.getCategory().getDisplayName().toLowerCase().contains(lowerQuery)) return true;
        if (item.getMaterial() != null && item.getMaterial().getDisplayName().toLowerCase().contains(lowerQuery)) return true;
        if (item.getDynastyPeriod() != null && item.getDynastyPeriod().getDisplayName().toLowerCase().contains(lowerQuery)) return true;

        return false;
    }

    private void navigateToDetailFragment(String artifactId) {
        Bundle args = new Bundle();
        args.putString("ARTIFACT_NO", artifactId);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_recyclerViewFragment_to_expandedArtifactFragment, args);
    }
}