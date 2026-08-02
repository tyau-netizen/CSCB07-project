package com.example.b07demosummer2024;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.model.Category;
import com.example.b07demosummer2024.model.DynastyPeriod;
import com.example.b07demosummer2024.model.Material;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that provides a form for administrators to add new artifacts to the collection.
 * Displays all fields from the ArtifactItem data model, with mandatory fields marked
 * with an asterisk (*). Handles image upload to Supabase Storage, validates input,
 * checks for duplicate lot numbers, and saves the new artifact to Firebase Realtime Database.
 * This is part of Sprint 3 and is accessible only to admin users via the ManageItemsFragment.
 */
public class AddItemFragment extends Fragment {

    private static final String TAG = "AddItemFragment";

    // EditTexts
    private EditText editTextLotNumber;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextCulturalOrigin;
    private EditText editTextDimensions;
    private EditText editTextConditionReport;
    private EditText editTextCurrentLocation;
    private EditText editTextAcquisitionMethod;
    private EditText editTextProvenance;
    private EditText editTextAccessionNumber;
    private EditText editTextNotes;

    // Spinners
    private Spinner spinnerCategory;
    private Spinner spinnerMaterial;
    private Spinner spinnerDynasty;

    // Image Picker
    private ImageView artifactImageView;
    private Button buttonPickImage;
    private String uploadedImageUrl = null; // Stores the URL after upload

    // Firebase
    private Button buttonAdd;
    private FirebaseDatabase db;
    private DatabaseReference artifactsRef;

    // Supabase Uploader
    private SupabaseImageUploader imageUploader;

    /**
     * Launcher for picking an image from the device gallery.
     * When an image is selected, it displays a preview and automatically uploads
     * to Supabase Storage using the artifact's lot number as the folder name.
     */
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    // Show preview
                    Glide.with(requireContext())
                            .load(uri)
                            .centerCrop()
                            .into(artifactImageView);
                    // Upload to Supabase
                    uploadImageToSupabase(uri);
                }
            });

    /**
     * Inflates the fragment layout, initializes all views, sets up the spinners,
     * wires up the image picker, and prepares the form for user input.
     * The user must enter a lot number before picking an image, as the lot number
     * is used as the folder name in Supabase Storage.
     *
     * @param inflater           Used to inflate the fragment's XML layout
     * @param container          The parent view this fragment is attached to
     * @param savedInstanceState Any saved state from a previous instance
     * @return The fully initialized View for this fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        // Initialize all fields
        editTextLotNumber = view.findViewById(R.id.editTextLotNumber);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextCulturalOrigin = view.findViewById(R.id.editTextCulturalOrigin);
        editTextDimensions = view.findViewById(R.id.editTextDimensions);
        editTextConditionReport = view.findViewById(R.id.editTextConditionReport);
        editTextCurrentLocation = view.findViewById(R.id.editTextCurrentLocation);
        editTextAcquisitionMethod = view.findViewById(R.id.editTextAcquisitionMethod);
        editTextProvenance = view.findViewById(R.id.editTextProvenance);
        editTextAccessionNumber = view.findViewById(R.id.editTextAccessionNumber);
        editTextNotes = view.findViewById(R.id.editTextNotes);

        artifactImageView = view.findViewById(R.id.artifactImageView);
        buttonPickImage = view.findViewById(R.id.buttonPickImage);

        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerMaterial = view.findViewById(R.id.spinnerMaterial);
        spinnerDynasty = view.findViewById(R.id.spinnerDynasty);

        buttonAdd = view.findViewById(R.id.buttonAdd);

        // Initialize Firebase
        db = FirebaseDatabase.getInstance("https://taam-100-default-rtdb.firebaseio.com/");
        artifactsRef = db.getReference("artifacts");

        // Initialize Supabase Uploader
        imageUploader = new SupabaseImageUploader(requireContext());

        // Setup Spinners
        setupCategorySpinner();
        setupMaterialSpinner();
        setupDynastySpinner();

        // Image Picker Button
        buttonPickImage.setOnClickListener(v -> {
            // Get the lot number first for the file path
            String lotNumber = editTextLotNumber.getText().toString().trim();
            if (lotNumber.isEmpty()) {
                Toast.makeText(getContext(), "Please enter Lot Number first", Toast.LENGTH_SHORT).show();
                editTextLotNumber.requestFocus();
                return;
            }
            // Launch image picker
            pickImageLauncher.launch("image/*");
        });

        // Add Button
        buttonAdd.setOnClickListener(v -> addArtifact());

        return view;
    }

     /**
     * Uploads the selected image to Supabase Storage using the artifact's lot number
     * as the folder name. Displays a progress toast during upload and saves the
     * resulting public URL to the uploadedImageUrl field on success.
     * The upload happens asynchronously; the user can continue filling out other
     * fields while the image uploads in the background.
     *
     * @param imageUri The URI of the image selected from the device gallery
     */
    private void uploadImageToSupabase(Uri imageUri) {
        String lotNumber = editTextLotNumber.getText().toString().trim();

        Toast.makeText(getContext(), "Uploading image...", Toast.LENGTH_SHORT).show();

        imageUploader.uploadImage(imageUri, lotNumber, new SupabaseImageUploader.UploadCallback() {
            @Override
            public void onSuccess(String publicUrl) {
                uploadedImageUrl = publicUrl;
                Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Uploaded image URL: " + publicUrl);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Image upload failed: " + message, Toast.LENGTH_LONG).show();
//                Log.e(TAG, "Image upload error: " + message);
            }
        });
    }

    /**
     * Populates the Category spinner with values from the Category enum
     */
    private void setupCategorySpinner() {
        List<String> displayNames = new ArrayList<>();
        for (Category c : Category.values()) {
            displayNames.add(c.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, displayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    /**
     * Populates the Material spinner with values from the Material enum
     */
    private void setupMaterialSpinner() {
        List<String> displayNames = new ArrayList<>();
        for (Material m : Material.values()) {
            displayNames.add(m.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, displayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterial.setAdapter(adapter);
    }

    /**
     * Populates the Dynasty spinner with values from the DynastyPeriod enum
     */
    private void setupDynastySpinner() {
        List<String> displayNames = new ArrayList<>();
        for (DynastyPeriod d : DynastyPeriod.values()) {
            displayNames.add(d.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, displayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDynasty.setAdapter(adapter);
    }

    /**
     * Validates all mandatory fields, converts spinner selections to enum
     * constants, and checks that the lot number is unique in the database.
     * If any mandatory field is empty, the corresponding EditText is highlighted
     * with an error message. If the lot number already exists, the user is prompted
     * to choose a different one. Only after all validation passes does it proceed
     * to save the artifact.
     */
    private void addArtifact() {
        //Get all field values
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String culturalOrigin = editTextCulturalOrigin.getText().toString().trim();
        String dimensions = editTextDimensions.getText().toString().trim();
        String conditionReport = editTextConditionReport.getText().toString().trim();
        String currentLocation = editTextCurrentLocation.getText().toString().trim();
        String acquisitionMethod = editTextAcquisitionMethod.getText().toString().trim();
        String provenance = editTextProvenance.getText().toString().trim();
        String accessionNumber = editTextAccessionNumber.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();

        // Get image URL from the upload result
        String imageUrl = uploadedImageUrl != null ? uploadedImageUrl : "";

        // Get spinner selections
        String categoryDisplayName = spinnerCategory.getSelectedItem().toString();
        String materialDisplayName = spinnerMaterial.getSelectedItem().toString();
        String dynastyDisplayName = spinnerDynasty.getSelectedItem().toString();

        // Map display names back to enum constants
        Category category = Category.fromDisplayName(categoryDisplayName);
        Material material = Material.fromDisplayName(materialDisplayName);
        DynastyPeriod dynasty = DynastyPeriod.fromDisplayName(dynastyDisplayName);

        // Validate mandatory fields
        if (lotNumber.isEmpty()) {
            editTextLotNumber.setError("Lot number is required");
            editTextLotNumber.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editTextDescription.setError("Description is required");
            editTextDescription.requestFocus();
            return;
        }

        if (category == null) {
            Toast.makeText(getContext(), "Please select a valid category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (material == null) {
            Toast.makeText(getContext(), "Please select a valid material", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dynasty == null) {
            Toast.makeText(getContext(), "Please select a valid dynasty period", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if Lot Number already exists
        checkLotNumberUniqueness(lotNumber, name, description, culturalOrigin, dimensions,
                conditionReport, currentLocation, acquisitionMethod, provenance,
                accessionNumber, notes, imageUrl, category, material, dynasty);
    }

    /**
     * Queries Firebase Realtime Database to check if the given lot number
     * already exists in the "artifacts" node. If it does, the user is shown
     * an error and asked to choose a different lot number. If not, it proceeds
     * to save the artifact.
     *
     * @param lotNumber          The unique identifier to check
     * @param name               Artifact name
     * @param description        Detailed description
     * @param culturalOrigin     Cultural/geographic origin
     * @param dimensions         Physical dimensions
     * @param conditionReport    Condition description
     * @param currentLocation    Current storage location
     * @param acquisitionMethod  How the artifact was acquired
     * @param provenance         Ownership history
     * @param accessionNumber    Museum accession number
     * @param notes              Additional notes
     * @param imageUrl           Public URL of the uploaded image
     * @param category           Artifact category enum
     * @param material           Primary material enum
     * @param dynasty            Historical dynasty/period enum
     */
    private void checkLotNumberUniqueness(String lotNumber, String name, String description,
                                          String culturalOrigin, String dimensions,
                                          String conditionReport, String currentLocation,
                                          String acquisitionMethod, String provenance,
                                          String accessionNumber, String notes,
                                          String imageUrl, Category category,
                                          Material material, DynastyPeriod dynasty) {

        Query query = artifactsRef.orderByChild("lotNumber").equalTo(lotNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    editTextLotNumber.setError("Lot number already exists. Please use a unique lot number.");
                    editTextLotNumber.requestFocus();
                    Toast.makeText(getContext(), "Lot number must be unique", Toast.LENGTH_LONG).show();
                } else {
                    saveArtifactToFirebase(lotNumber, name, description, culturalOrigin,
                            dimensions, conditionReport, currentLocation,
                            acquisitionMethod, provenance, accessionNumber,
                            notes, imageUrl, category, material, dynasty);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "Error checking lot number uniqueness", databaseError.toException());
                Toast.makeText(getContext(), "Error checking lot number: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Creates a new ArtifactItem object with all provided field values and saves it
     * to Firebase Realtime Database under the path "artifacts/{lotNumber}".
     * Optional fields are only set if they are non-empty. Default values are
     * assigned to imageInt (0) and saved (false). On success, the user is shown
     * a confirmation toast and navigated back to the artifact list. On failure,
     * an error toast is displayed.
     *
     * @param lotNumber          The unique identifier (used as the Firebase key)
     * @param name               Artifact name
     * @param description        Detailed description
     * @param culturalOrigin     Cultural/geographic origin
     * @param dimensions         Physical dimensions
     * @param conditionReport    Condition description
     * @param currentLocation    Current storage location
     * @param acquisitionMethod  How the artifact was acquired
     * @param provenance         Ownership history
     * @param accessionNumber    Museum accession number
     * @param notes              Additional notes
     * @param imageUrl           Public URL of the uploaded image
     * @param category           Artifact category enum
     * @param material           Primary material enum
     * @param dynasty            Historical dynasty/period enum
     */
    private void saveArtifactToFirebase(String lotNumber, String name, String description,
                                        String culturalOrigin, String dimensions,
                                        String conditionReport, String currentLocation,
                                        String acquisitionMethod, String provenance,
                                        String accessionNumber, String notes,
                                        String imageUrl, Category category,
                                        Material material, DynastyPeriod dynasty) {

        ArtifactItem artifact = new ArtifactItem();
        artifact.setLotNumber(lotNumber);
        artifact.setName(name);
        artifact.setDescription(description);

        // Optional fields
        if (!culturalOrigin.isEmpty()) artifact.setCulturalOrigin(culturalOrigin);
        if (!dimensions.isEmpty()) artifact.setDimensions(dimensions);
        if (!conditionReport.isEmpty()) artifact.setConditionReport(conditionReport);
        if (!currentLocation.isEmpty()) artifact.setCurrentLocation(currentLocation);
        if (!acquisitionMethod.isEmpty()) artifact.setAcquisitionMethod(acquisitionMethod);
        if (!provenance.isEmpty()) artifact.setProvenance(provenance);
        if (!accessionNumber.isEmpty()) artifact.setAccessionNumber(accessionNumber);
        if (!notes.isEmpty()) artifact.setNotes(notes);
        if (!imageUrl.isEmpty()) artifact.setImageUri(imageUrl);

        // Enums
        artifact.setCategory(category);
        artifact.setMaterial(material);
        artifact.setDynastyPeriod(dynasty);

        // Default values
        artifact.setImageInt(0);
        artifact.setSaved(false);

        artifactsRef.child(lotNumber).setValue(artifact)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Artifact added successfully!", Toast.LENGTH_SHORT).show();
                    navigateBackToList();
                })
                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Error saving artifact", e);
                    Toast.makeText(getContext(), "Failed to add artifact: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Navigates back to the RecyclerViewFragment
     */
    private void navigateBackToList() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }
}