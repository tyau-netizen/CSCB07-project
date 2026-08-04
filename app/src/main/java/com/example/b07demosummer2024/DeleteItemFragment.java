package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.model.ArtifactItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteItemFragment extends Fragment {
    private EditText editTextTitle;
    private Spinner spinnerCategory;
    private Button buttonDelete;

    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        db = FirebaseDatabase.getInstance("https://taam-100-default-rtdb.firebaseio.com/");

        // Set up the spinner with real categories
        com.example.b07demosummer2024.model.Category[] categories = com.example.b07demosummer2024.model.Category.values();
        java.util.List<String> displayNames = new java.util.ArrayList<>();
        for (com.example.b07demosummer2024.model.Category c : categories) {
            displayNames.add(c.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, displayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        com.google.android.material.appbar.MaterialToolbar toolbar = view.findViewById(R.id.toolbar_delete_artifact);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> {
                androidx.navigation.Navigation.findNavController(requireView()).navigateUp();
            });
        }

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        if (buttonCancel != null) {
            buttonCancel.setOnClickListener(v -> {
                androidx.navigation.Navigation.findNavController(requireView()).navigateUp();
            });
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemByTitle();
            }
        });

        return view;
    }

    private void deleteItemByTitle() {
        String title = editTextTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter item title", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = db.getReference("artifacts");
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ArtifactItem item = snapshot.getValue(ArtifactItem.class);
                    if (item != null && item.getName() != null && item.getName().equalsIgnoreCase(title)) {
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Artifact deleted successfully", Toast.LENGTH_SHORT).show();
                                androidx.navigation.Navigation.findNavController(requireView()).navigateUp();
                            } else {
                                Toast.makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Toast.makeText(getContext(), "Artifact with title '" + title + "' not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}