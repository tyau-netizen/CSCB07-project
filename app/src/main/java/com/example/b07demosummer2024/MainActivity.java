package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.example.b07demosummer2024.databinding.ActivityMainBinding;
import com.example.b07demosummer2024.user.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private final SessionManager sessionManager = SessionManager.getInstance();
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(binding.navHostFragment.getId());

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            setupBottomNavMenu();
        }

        db = FirebaseDatabase.getInstance("https://b07-demo-summer-2024-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = db.getReference("testDemo");

        myRef.child("movies").setValue("B07 Demo!");
    }

    private void setupBottomNavMenu() {
        // Set up navigation on menu item click
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int destinationId = item.getItemId();

            // Do nothing if attempting to navigate to current fragment
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() == destinationId) {
                return true;
            }

            NavOptions navOptions = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.homeFragment, false)
                    .build();

            navController.navigate(destinationId, null, navOptions);
            return true;
        });

        navController.addOnDestinationChangedListener(
                (controller,destination, arguments) -> {
            // Check if nav menu should be shown in current fragment
            boolean shouldHide = arguments != null
                    && arguments.getBoolean("hideBottomNav", false);
            binding.bottomNavigation.setVisibility(shouldHide ? View.GONE : View.VISIBLE);

            // Show manage artifacts button if user is admin
            boolean isAdmin = sessionManager.isAdminSession();
            MenuItem manageArtifactsButton = binding.bottomNavigation.getMenu()
                    .findItem(R.id.manageItemsFragment);
            if (manageArtifactsButton != null) {
                manageArtifactsButton.setVisible(isAdmin);
            }

            // Keep button state synced with current screen
            MenuItem menuItem = binding.bottomNavigation.getMenu().findItem(destination.getId());
            if (menuItem != null) {
                menuItem.setChecked(true);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }
}