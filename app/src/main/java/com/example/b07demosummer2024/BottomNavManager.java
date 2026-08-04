package com.example.b07demosummer2024;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.example.b07demosummer2024.user.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavManager {
    private final BottomNavigationView bottomNavView;
    private final NavController navController;
    private final SessionManager sessionManager;

    private boolean keyboardIsVisible = false;
    private boolean isTopLevelDestination = false;

    public BottomNavManager (@NonNull BottomNavigationView bottomNavView,
                             @NonNull NavController navController) {
        this.bottomNavView = bottomNavView;
        this.navController = navController;
        sessionManager = SessionManager.getInstance();

        setupNavigation();
        setupDestinationListener();
    }

    public void updateKeyboardStatus(boolean isVisible) {
        keyboardIsVisible = isVisible;
        updateVisibility();
    }

    private void setupNavigation() {
        // Set up navigation on menu item click
        bottomNavView.setOnItemSelectedListener(item -> {
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
    }

    private void setupDestinationListener() {
        navController.addOnDestinationChangedListener(
                (controller,destination, arguments) -> {
                    // Check if nav menu should be shown in current fragment
                    isTopLevelDestination = arguments == null
                            || !arguments.getBoolean("hideBottomNav", false);
                    updateVisibility();

                    // Show manage artifacts button if user is admin
                    boolean isAdmin = sessionManager.isAdminSession();
                    MenuItem manageArtifactsButton = bottomNavView.getMenu()
                            .findItem(R.id.manageItemsFragment);
                    if (manageArtifactsButton != null) {
                        manageArtifactsButton.setVisible(isAdmin);
                    }

                    // Keep button state synced with current screen
                    MenuItem menuItem = bottomNavView.getMenu().findItem(destination.getId());
                    if (menuItem != null) {
                        menuItem.setChecked(true);
                    }
                });
    }

    private void updateVisibility() {
        if (isTopLevelDestination && !keyboardIsVisible) {
            bottomNavView.setVisibility(View.VISIBLE);
        } else {
            bottomNavView.setVisibility(View.GONE);
        }
    }
}
