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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.model.ArtifactItemAdapter;
import com.example.b07demosummer2024.model.Category;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArtifactItemAdapter itemAdapter;
    private List<ArtifactItem> itemList;
    private Spinner spinnerCategory;

    private FirebaseDatabase db;

    private static final String ALL_ARTIFACTS_LABEL = "All Artifacts";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        Category[] categories = Category.values();

        // Build display list: "All Artifacts" first, then every category
        List<String> displayNames = new ArrayList<>();
        displayNames.add(ALL_ARTIFACTS_LABEL);
        for (Category c : categories) {
            displayNames.add(c.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, displayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        itemList = new ArrayList<>();
        itemAdapter = new ArtifactItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        db = FirebaseDatabase.getInstance("https://taam-100-default-rtdb.firebaseio.com/");

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // "All Artifacts" selected
                    fetchAllItemsFromDatabase();
                } else {
                    // position 1 maps to categories[0], position 2 to categories[1], etc.
                    Category selectedCategory = categories[position - 1];
                    fetchItemsFromDatabase(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return view;
    }

    private void fetchAllItemsFromDatabase() {
        DatabaseReference artifactsRef = db.getReference("artifacts");
        artifactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ArtifactItem item = snapshot.getValue(ArtifactItem.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RecyclerViewFragment", "Failed to read all items.", databaseError.toException());
            }
        });
    }

    private void fetchItemsFromDatabase(Category category) {
        DatabaseReference artifactsRef = db.getReference("artifacts");
        Query query = artifactsRef.orderByChild("category").equalTo(category.name());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ArtifactItem item = snapshot.getValue(ArtifactItem.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RecyclerViewFragment", "Failed to read filtered items.", databaseError.toException());
            }
        });
    }
}