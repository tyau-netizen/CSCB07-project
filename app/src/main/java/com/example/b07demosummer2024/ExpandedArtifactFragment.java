package com.example.b07demosummer2024;
import com.example.b07demosummer2024.R;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.b07demosummer2024.model.ArtifactItem;
import com.example.b07demosummer2024.user.SavedArtifactsManager;
import com.example.b07demosummer2024.user.SessionManager;
import com.example.b07demosummer2024.user.User;
import com.example.b07demosummer2024.user.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ExpandedArtifactFragment extends Fragment {

    // --- CLASS VARIABLES ---
    private TextView titleText;
    private TextView descText;
    private ImageButton saveButton;
    private ImageView artifactImage;
    private LinearLayout relatedContainer;
    private ArtifactItem currentArtifactItem;
    private List<ArtifactItem> allItemsList;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expanded_artifact, container, false);
        sessionManager = SessionManager.getInstance();

        titleText = view.findViewById(R.id.text_artifact_name);
        descText = view.findViewById(R.id.text_artifact_description);
        saveButton = view.findViewById(R.id.button_save_artifact);
        artifactImage = view.findViewById(R.id.image_artifact_large);
        relatedContainer = view.findViewById(R.id.container_related_artifacts);

        // Save/unsave artifact
        /* TODO: Make the save button change appearance based on whether artifact is saved or not.
        *   Note - the following code will tell you if the artifact is saved or not:
        *   ----------------------------------------------------------------------------------------
        *       User currentUser = sessionManager.getCurrentUser();
        *       SavedArtifactsManager artifactsManager = currentUser.getSavedArtifactsManager();
        *       String lotNumber = currentArtifactItem.getLotNumber();
        *       boolean artifactIsSaved = artifactsManager.containsArtifact(lotNumber);
        *   ----------------------------------------------------------------------------------------
        *   Also check out handleSaveClick() at the bottom of the file it's a cool method
        *  */
        saveButton.setOnClickListener(v -> handleSaveClick());


        Spinner spinner = view.findViewById(R.id.spinner_sort_related);
        if (spinner != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    requireContext(), R.array.sort_options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedCriteria = parent.getItemAtPosition(position).toString();
                    if (currentArtifactItem != null && allItemsList != null) {
                        RecommendationEngine engine = new RecommendationEngine();
                        List<String> sortedIds = engine.SortRelatedIds(currentArtifactItem, allItemsList, selectedCriteria);
                        displayRelatedArtifacts(sortedIds);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Should not run...
                }
            });
        }

        String currentArtifactNo = "LOT001"; // change this!!!!!
        if (getArguments() != null && getArguments().getString("ARTIFACT_NO") != null) { // need to bundle
            currentArtifactNo = getArguments().getString("ARTIFACT_NO");
        }
        fetchAllArtifactsForEngine();
        fetchArtifactData(currentArtifactNo);

        return view;
    }
    private void fetchAllArtifactsForEngine() {
        DatabaseReference allRef = FirebaseDatabase.getInstance().getReference("artifacts");
        allRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allItemsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ArtifactItem item = dataSnapshot.getValue(ArtifactItem.class);
                    if (item != null) {
                        allItemsList.add(item);
                    }
                }
                if (currentArtifactItem != null) {
                    runEngine();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading list: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void runEngine() {
        if (allItemsList != null && currentArtifactItem != null) {
            RecommendationEngine engine = new RecommendationEngine();
            // Assuming you have a way to get selected criteria
            String criteria = "Category";
            List<String> sortedIds = engine.SortRelatedIds(currentArtifactItem, allItemsList, criteria);
            displayRelatedArtifacts(sortedIds);
        }
    }
    private void fetchArtifactData(String artifactNo) {
        ArtifactFetcher fetcher = new ArtifactFetcher();

        fetcher.fetchItemData(artifactNo, new ArtifactFetcher.OnItemFetchedListener() {
            @Override
            public void onSuccess(ArtifactItem item) {
                if (getContext() == null || !isAdded()) return;

                if (item.getName() != null) {
                    titleText.setText(item.getName());
                }

                if (item.getImageUri() != null && !item.getImageUri().trim().isEmpty()) {
                    Glide.with(requireContext()).load(item.getImageUri()).centerCrop().into(artifactImage);
                }

                // HTML Formattin'
                StringBuilder fullDescription = new StringBuilder();
                if (item.getLotNumber() != null && !item.getLotNumber().trim().isEmpty()) {
                    fullDescription.append("<b>LOT NUMBER:</b> <font color='#7A7A7A'>").append(item.getLotNumber()).append("</font><br><br>");
                }
                if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
                    fullDescription.append("<b>DESCRIPTION:</b> <font color='#7A7A7A'>").append(item.getDescription()).append("</font><br><br>");
                }
                if (item.getCategory() != null) {
                    String categoryText = item.getCategory().getDisplayName();

                    if (categoryText != null && !categoryText.trim().isEmpty()) {
                        fullDescription.append("<b>CATEGORY:</b> <font color='#7A7A7A'>")
                                .append(categoryText)
                                .append("</font><br><br>");
                    }
                }
                if (item.getMaterial() != null) {
                    String materialText = item.getMaterial().getDisplayName();

                    if (materialText != null && !materialText.trim().isEmpty()) {
                        fullDescription.append("<b>MATERIAL:</b> <font color='#7A7A7A'>")
                                .append(materialText)
                                .append("</font><br><br>");
                    }
                }
                if (item.getDynastyPeriod() != null) {
                    String dynastyText = item.getDynastyPeriod().getDisplayName();

                    if (dynastyText != null && !dynastyText.trim().isEmpty()) {
                        fullDescription.append("<b>DYNASTY PERIOD:</b> <font color='#7A7A7A'>")
                                .append(dynastyText)
                                .append("</font><br><br>");
                    }
                }
                if (item.getCulturalOrigin() != null && !item.getCulturalOrigin().trim().isEmpty()) {
                    fullDescription.append("<b>CULTURAL ORIGIN:</b> <font color='#7A7A7A'>").append(item.getCulturalOrigin()).append("</font><br><br>");
                }
                if (item.getDimensions() != null && !item.getDimensions().trim().isEmpty()) {
                    fullDescription.append("<b>DIMENSIONS:</b> <font color='#7A7A7A'>").append(item.getDimensions()).append("</font><br><br>");
                }
                if (item.getConditionReport() != null && !item.getConditionReport().trim().isEmpty()) {
                    fullDescription.append("<b>CONDITION REPORT:</b> <font color='#7A7A7A'>").append(item.getConditionReport()).append("</font><br><br>");
                }
                if (item.getCurrentLocation() != null && !item.getCurrentLocation().trim().isEmpty()) {
                    fullDescription.append("<b>CURRENT LOCATION:</b> <font color='#7A7A7A'>").append(item.getCurrentLocation()).append("</font><br><br>");
                }
                if (item.getAcquisitionMethod() != null && !item.getAcquisitionMethod().trim().isEmpty()) {
                    fullDescription.append("<b>ACQUISITION METHOD:</b> <font color='#7A7A7A'>").append(item.getAcquisitionMethod()).append("</font><br><br>");
                }
                if (item.getProvenance() != null && !item.getProvenance().trim().isEmpty()) {
                    fullDescription.append("<b>PROVENANCE:</b> <font color='#7A7A7A'>").append(item.getProvenance()).append("</font><br><br>");
                }
                if (item.getAccessionNumber() != null && !item.getAccessionNumber().trim().isEmpty()) {
                    fullDescription.append("<b>ACCESSION NUMBER:</b> <font color='#7A7A7A'>").append(item.getAccessionNumber()).append("</font><br><br>");
                }
                if (item.getNotes() != null && !item.getNotes().trim().isEmpty()) {
                    fullDescription.append("<b>NOTES:</b> <font color='#7A7A7A'>").append(item.getNotes()).append("</font><br><br>");
                }

                descText.setText(Html.fromHtml(fullDescription.toString(), Html.FROM_HTML_MODE_COMPACT));

                currentArtifactItem = item;
                runEngine();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayRelatedArtifacts(List<String> relatedIds) {
        if (relatedIds == null || relatedIds.isEmpty()) {
            relatedContainer.setVisibility(View.GONE);
            return;
        }

        relatedContainer.setVisibility(View.VISIBLE);
        relatedContainer.removeAllViews(); // No need

        // max 5
        int itemsToShow = Math.min(relatedIds.size(), 5);
        List<String> displayList = relatedIds.subList(0, itemsToShow);

        for (String artifactId : displayList) {
            ArtifactFetcher fetcher = new ArtifactFetcher();
            fetcher.fetchItemData(artifactId, new ArtifactFetcher.OnItemFetchedListener() {
                @Override
                public void onSuccess(ArtifactItem item) {
                    if (getContext() == null || !isAdded()) return;

                    View artifactCard = LayoutInflater.from(getContext()).inflate(R.layout.item_related_artifact, relatedContainer, false);

                    ImageView img = artifactCard.findViewById(R.id.imageView);
                    TextView nameText = artifactCard.findViewById(R.id.name);
                    TextView categoryText = artifactCard.findViewById(R.id.category);
                    TextView dynastyText = artifactCard.findViewById(R.id.dynastyPeriod);

                    // Load Image
                    if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
                        Glide.with(requireContext()).load(item.getImageUri()).centerCrop().into(img);
                    }
                    if (item.getName() != null && !item.getName().trim().isEmpty()) {
                        nameText.setText(item.getName());
                    } else {
                        nameText.setText("Unknown Artifact");
                    }
                    if (item.getCategory() != null && item.getCategory().getDisplayName() != null) {
                        categoryText.setText("Category: " + item.getCategory().getDisplayName());
                    } else if (item.getMaterial() != null && item.getMaterial().getDisplayName() != null) {
                        categoryText.setText("Material: " + item.getMaterial().getDisplayName());
                    } else {
                        categoryText.setText("Category Unknown");
                    }
                    if (item.getDynastyPeriod() != null && item.getDynastyPeriod().getDisplayName() != null) {
                        dynastyText.setText("Dynasty: " + item.getDynastyPeriod().getDisplayName());
                    } else {
                        dynastyText.setText("Dynasty Unknown");
                    }

                    relatedContainer.addView(artifactCard);
                }

                @Override
                public void onError(String error) {
                    // Skip item...
                }
            });
        }
    }

    private void handleSaveClick() {
        User currentUser = sessionManager.getCurrentUser();
        SavedArtifactsManager artifactsManager = currentUser.getSavedArtifactsManager();
        UserRepository userRepository = sessionManager.getUserRepository();
        String lotNumber = currentArtifactItem.getLotNumber();

        if (!artifactsManager.containsArtifact(lotNumber)) {
            // Update local data
            String orderKey = artifactsManager.add(lotNumber);
            // Update database
            userRepository.addSavedArtifact(currentUser.getUid(), lotNumber, orderKey,
                    new UserRepository.UserSaveCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(),
                                    "Artifact Saved!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            artifactsManager.remove(lotNumber);
                            Toast.makeText(getContext(),
                                    "Failed to save artifact: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Update database
            userRepository.removeSavedArtifact(currentUser.getUid(), lotNumber,
                    new UserRepository.UserSaveCallback() {
                        @Override
                        public void onSuccess() {
                            // Update local data
                            artifactsManager.remove(lotNumber);
                            Toast.makeText(getContext(),
                                    "Artifact Unsaved.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getContext(),
                                    "Failed to unsave artifact: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}