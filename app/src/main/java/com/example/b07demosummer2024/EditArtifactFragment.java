package com.example.b07demosummer2024;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.model.Category;
import com.example.b07demosummer2024.model.Material;
import com.example.b07demosummer2024.model.DynastyPeriod;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditArtifactFragment extends Fragment {

    private ArtifactItem currentArtifact;

    private ImageView ivImage;
    private TextView tvLotNumber;
    private TextInputEditText etName, etDescription, etCulturalOrigin, etDimensions;
    private TextInputEditText etCondition, etLocation, etAcquisition, etProvenance;
    private TextInputEditText etAccession, etNotes;
    private Spinner spinnerCategory, spinnerMaterial, spinnerDynasty;
    private Button btnSaveChanges, btnChangeImage;

    private SupabaseImageUploader imageUploader;
    private ActivityResultLauncher<String> pickImageLauncher;

    public EditArtifactFragment(ArtifactItem artifact) {
        this.currentArtifact = artifact;
    }

    public EditArtifactFragment() {
        // Required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageUploader = new SupabaseImageUploader(requireContext());

        // Register the gallery picker launcher before the fragment is 'started'
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        handleImageSelected(uri);
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_artifact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initiate UI
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar_edit_artifact);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        ivImage = view.findViewById(R.id.iv_edit_image);
        tvLotNumber = view.findViewById(R.id.tv_edit_lot_number);
        etName = view.findViewById(R.id.et_edit_name);
        etDescription = view.findViewById(R.id.et_edit_description);
        etCulturalOrigin = view.findViewById(R.id.et_edit_cultural_origin);
        etDimensions = view.findViewById(R.id.et_edit_dimensions);
        etCondition = view.findViewById(R.id.et_edit_condition);
        etLocation = view.findViewById(R.id.et_edit_location);
        etAcquisition = view.findViewById(R.id.et_edit_acquisition);
        etProvenance = view.findViewById(R.id.et_edit_provenance);
        etAccession = view.findViewById(R.id.et_edit_accession);
        etNotes = view.findViewById(R.id.et_edit_notes);

        spinnerCategory = view.findViewById(R.id.spinner_edit_category);
        spinnerMaterial = view.findViewById(R.id.spinner_edit_material);
        spinnerDynasty = view.findViewById(R.id.spinner_edit_dynasty);

        btnSaveChanges = view.findViewById(R.id.btn_save_artifact_changes);
        btnChangeImage = view.findViewById(R.id.btn_change_image);

        // Spinners
        String[] categories = {"Box", "Figure/Sculpture", "Tea Bowl", "Washer", "Vase", "Flask"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        String[] materials = {"Porcelain/Ceramics", "Sancai", "Bronze", "Jade", "Silk"};
        ArrayAdapter<String> matAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, materials);
        matAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterial.setAdapter(matAdapter);

        String[] dynasties = {"Three Kingdoms", "Tang", "Liao", "Song", "Jin", "Yuan", "Ming", "Qing"};
        ArrayAdapter<String> dynAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dynasties);
        dynAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDynasty.setAdapter(dynAdapter);

        // Pre-fill fields, not sure if in requirements or not
        if (currentArtifact != null) {
            if (currentArtifact.getImageUri() != null && !currentArtifact.getImageUri().isEmpty()) {
                Glide.with(this).load(currentArtifact.getImageUri()).into(ivImage);
            }

            tvLotNumber.setText(currentArtifact.getLotNumber());
            etName.setText(currentArtifact.getName() != null ? currentArtifact.getName() : "");
            etDescription.setText(currentArtifact.getDescription() != null ? currentArtifact.getDescription() : "");
            etCulturalOrigin.setText(currentArtifact.getCulturalOrigin() != null ? currentArtifact.getCulturalOrigin() : "");
            etDimensions.setText(currentArtifact.getDimensions() != null ? currentArtifact.getDimensions() : "");
            etCondition.setText(currentArtifact.getConditionReport() != null ? currentArtifact.getConditionReport() : "");
            etLocation.setText(currentArtifact.getCurrentLocation() != null ? currentArtifact.getCurrentLocation() : "");
            etAcquisition.setText(currentArtifact.getAcquisitionMethod() != null ? currentArtifact.getAcquisitionMethod() : "");
            etProvenance.setText(currentArtifact.getProvenance() != null ? currentArtifact.getProvenance() : "");
            etAccession.setText(currentArtifact.getAccessionNumber() != null ? currentArtifact.getAccessionNumber() : "");
            etNotes.setText(currentArtifact.getNotes() != null ? currentArtifact.getNotes() : "");

            if (currentArtifact.getCategory() != null) {
                int pos = catAdapter.getPosition(currentArtifact.getCategory().getDisplayName());
                if (pos >= 0) spinnerCategory.setSelection(pos);
            }
            if (currentArtifact.getMaterial() != null) {
                int pos = matAdapter.getPosition(currentArtifact.getMaterial().getDisplayName());
                if (pos >= 0) spinnerMaterial.setSelection(pos);
            }
            if (currentArtifact.getDynastyPeriod() != null) {
                int pos = dynAdapter.getPosition(currentArtifact.getDynastyPeriod().getDisplayName());
                if (pos >= 0) spinnerDynasty.setSelection(pos);
            }
        }

        // Change Image button to launch gallery picker
        btnChangeImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // Save Button to save changes
        btnSaveChanges.setOnClickListener(v -> saveModificationsToFirebase());
    }

    private void handleImageSelected(Uri imageUri) {
        if (currentArtifact == null || currentArtifact.getLotNumber() == null) {
            Toast.makeText(getContext(), "Cannot upload image: artifact lot number missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        Glide.with(this).load(imageUri).into(ivImage);
        Toast.makeText(getContext(), "Uploading image...", Toast.LENGTH_SHORT).show();
        btnSaveChanges.setEnabled(false); // disable Save Button until upload resolves

        imageUploader.uploadImage(imageUri, currentArtifact.getLotNumber(), new SupabaseImageUploader.UploadCallback() {
            @Override
            public void onSuccess(String publicUrl) {
                if (getContext() == null || !isAdded()) return;
                currentArtifact.setImageUri(publicUrl);
                btnSaveChanges.setEnabled(true);
                Toast.makeText(getContext(), "Image uploaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                if (getContext() == null || !isAdded()) return;
                btnSaveChanges.setEnabled(true);
                Toast.makeText(getContext(), "Upload failed: " + message, Toast.LENGTH_LONG).show();
                if (currentArtifact.getImageUri() != null && !currentArtifact.getImageUri().isEmpty()) {
                    Glide.with(EditArtifactFragment.this).load(currentArtifact.getImageUri()).into(ivImage);
                }
            }
        });
    }

    private void saveModificationsToFirebase() {
        if (currentArtifact == null) return;

        // Update ArtifactItem Class
        currentArtifact.setName(etName.getText().toString().trim());
        currentArtifact.setDescription(etDescription.getText().toString().trim());
        currentArtifact.setCulturalOrigin(etCulturalOrigin.getText().toString().trim());
        currentArtifact.setDimensions(etDimensions.getText().toString().trim());
        currentArtifact.setConditionReport(etCondition.getText().toString().trim());
        currentArtifact.setCurrentLocation(etLocation.getText().toString().trim());
        currentArtifact.setAcquisitionMethod(etAcquisition.getText().toString().trim());
        currentArtifact.setProvenance(etProvenance.getText().toString().trim());
        currentArtifact.setAccessionNumber(etAccession.getText().toString().trim());
        currentArtifact.setNotes(etNotes.getText().toString().trim());

        // Update custom objects using the Spinners
        // We use helper methods to find the correct Enum based on the text selected
        currentArtifact.setCategory(findCategory(spinnerCategory.getSelectedItem().toString()));
        currentArtifact.setMaterial(findMaterial(spinnerMaterial.getSelectedItem().toString()));
        currentArtifact.setDynastyPeriod(findDynasty(spinnerDynasty.getSelectedItem().toString()));

        // currentArtifact.getImageUri() already holds the new Supabase public URL if the
        // user picked a new image. and handleImageSelected() sets it on upload success

        // Find the folder and overwrite it in Firebase
        DatabaseReference artifactRef = FirebaseDatabase.getInstance()
                .getReference("artifacts")
                .child(currentArtifact.getLotNumber());

        artifactRef.setValue(currentArtifact)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Artifact Updated Successfully!", Toast.LENGTH_SHORT).show();
                    // Close the edit page and go back
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to update: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // Enum helpers
    private Category findCategory(String selectedText) {
        for (Category c : Category.values()) {
            if (c.getDisplayName() != null && c.getDisplayName().equals(selectedText)) {
                return c;
            }
        }
        return null;
    }

    private Material findMaterial(String selectedText) {
        for (Material m : Material.values()) {
            if (m.getDisplayName() != null && m.getDisplayName().equals(selectedText)) {
                return m;
            }
        }
        return null;
    }

    private DynastyPeriod findDynasty(String selectedText) {
        for (DynastyPeriod d : DynastyPeriod.values()) {
            if (d.getDisplayName() != null && d.getDisplayName().equals(selectedText)) {
                return d;
            }
        }
        return null;
    }
}