package com.example.b07demosummer2024.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.base.BaseFragment;
import com.example.b07demosummer2024.databinding.FragmentHomeBinding;
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

/**
 * Fragment that displays a paginated, filterable list of artifacts.
 * Supports category filtering via spinner, keyword search via SearchView,
 * and configurable page size (12, 24, or All items per page).
 * User preferences for page size are persisted using SharedPreferences.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding,
        HomeContract.View, HomeContract.Presenter> implements HomeContract.View {

    // UI components
    private RecyclerView recyclerView;
    private ArtifactItemAdapter itemAdapter;
    private List<ArtifactItem> itemList = new ArrayList<>();
    private List<ArtifactItem> masterList = new ArrayList<>();

    // Filter state
    private String currentCategoryFilter = "All Artifacts";
    private String currentSearchQuery = "";

    // UI views
    private SearchView searchView;
    private Spinner spinnerCategory;

    // Firebase
    private FirebaseDatabase db;

    // SharedPreferences
    private static final String PREFS_NAME = "ArtifactAppPrefs";
    private static final String KEY_PAGE_SIZE = "selected_page_size";

    // Pagination state
    private int currentPageSize = 12;
    private int currentPage = 1;

    // Pagination UI components
    private Spinner spinnerPageSize;
    private Button btnPrevious;
    private Button btnNext;
    private TextView textPageIndicator;
    private SharedPreferences sharedPreferences;
    private static final String ALL_ARTIFACTS_LABEL = "All Artifacts";

    // Bundle flags
    public static final String KEY_WELCOME_USER = "welcomeUser";
    public static final String KEY_IS_GUEST = "isGuest";

    public static Bundle packWelcomeBundle(boolean isGuest) {
        Bundle isGuestBundle = new Bundle();
        isGuestBundle.putBoolean(KEY_IS_GUEST, isGuest);

        Bundle mainBundle = new Bundle();
        mainBundle.putBundle(KEY_WELCOME_USER, isGuestBundle);
        return mainBundle;
    }

    @NonNull
    @Override
    protected FragmentHomeBinding inflateBinding(@NonNull LayoutInflater inflater,
                                                 @Nullable ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    /**
     * Inflates the fragment layout, initializes all views, sets up the
     * RecyclerView adapter, and configures filters and pagination controls.
     * Restores saved page size preference from SharedPreferences.
     *
     * @param inflater           Used to inflate the fragment's XML layout
     * @param container          The parent view this fragment is attached to
     * @param savedInstanceState Any saved state from a previous instance
     * @return The fully initialized View for this fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Load saved pagination choice
        currentPageSize = sharedPreferences.getInt(KEY_PAGE_SIZE, 12);

        spinnerPageSize = view.findViewById(R.id.spinnerPageSize);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);
        textPageIndicator = view.findViewById(R.id.textPageIndicator);
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
                currentPage = 1;
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
        // Page Size Spinner Setup
        List<String> pageSizeOptions = new ArrayList<>();
        pageSizeOptions.add("12 per page");
        pageSizeOptions.add("24 per page");
        pageSizeOptions.add("All per page");

        ArrayAdapter<String> pageSizeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, pageSizeOptions);
        pageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPageSize.setAdapter(pageSizeAdapter);

        // Set default dropdown position
        if (currentPageSize == 12) {
            spinnerPageSize.setSelection(0);
        } else if (currentPageSize == 24) {
            spinnerPageSize.setSelection(1);
        } else {
            spinnerPageSize.setSelection(2);
        }

        spinnerPageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    currentPageSize = 12;
                } else if (position == 1) {
                    currentPageSize = 24;
                } else {
                    currentPageSize = 0; // 0 = All
                }

                // Save selected option to SharedPreferences
                sharedPreferences.edit().putInt(KEY_PAGE_SIZE, currentPageSize).apply();
                currentPage = 1;
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                applyFilters();
                if (binding != null && binding.recyclerView != null) {
                    binding.recyclerView.scrollToPosition(0);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            currentPage++;
            applyFilters();
            if (binding != null && binding.recyclerView != null) {
                binding.recyclerView.scrollToPosition(0);
            }
        });

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
                currentPage = 1;
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Setup RecyclerView Adapter
        itemAdapter = new ArtifactItemAdapter(itemList,
                new ArtifactItemAdapter.OnArtifactClickListener() {
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.handleInitialArguments(getArguments());
    }

    /**
     * Fetches all artifacts from Firebase Realtime Database in a single
     * read operation. Stores the full dataset in masterList, then calls
     * applyFilters() to display the initial paginated page.
     */
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

    /**
     * Applies category and search filters to the full dataset, then paginates
     * the results based on the selected page size. Updates the RecyclerView
     * adapter with the current page and refreshes pagination controls.
     */
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

        // Calculate pagination
        int totalItems = filteredList.size();
        int totalPages;

        if (currentPageSize == 0 || totalItems == 0) {
            totalPages = 1;
            currentPage = 1;
        } else {
            totalPages = (int) Math.ceil((double) totalItems / currentPageSize);
            if (currentPage > totalPages) currentPage = totalPages;
            if (currentPage < 1) currentPage = 1;
        }

        List<ArtifactItem> pageList;
        if (currentPageSize == 0 || totalItems == 0) {
            pageList = filteredList;
        } else {
            int startIndex = (currentPage - 1) * currentPageSize;
            int endIndex = Math.min(startIndex + currentPageSize, totalItems);
            pageList = filteredList.subList(startIndex, endIndex);
        }

        // Update adapter with current page list
        itemAdapter.updateList(pageList);

        // Update button states & page indicator text
        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        textPageIndicator.setText("Page " + currentPage + " of " + totalPages);
    }

    /**
     * Performs a case-insensitive keyword search across all fields of an
     * ArtifactItem, including enum display names. Returns true if the keyword
     * appears in any field.
     *
     * @param item  The artifact to search within
     * @param query The search keyword
     * @return true if the keyword matches any field, false otherwise
     */
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

    /**
     * Navigates to the expanded detail view for a specific artifact.
     * Passes the artifact's lot number as a bundle argument.
     *
     * @param artifactId The lot number of the artifact to display
     */
    private void navigateToDetailFragment(String artifactId) {
        Bundle args = new Bundle();
        args.putString("ARTIFACT_NO", artifactId);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_homeFragment_to_expandedArtifactFragment, args);
    }

    @Override
    public void showWelcomeMessage(String username) {
        displayToastMessage("Welcome, " + username + "!");
    }
}