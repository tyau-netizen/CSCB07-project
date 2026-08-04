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
    private BottomNavManager bottomNavManager;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Enable edge-to-edge drawing to account for device elements
        EdgeToEdge.enable(this);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(binding.navHostFragment.getId());

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            bottomNavManager = new BottomNavManager(binding.bottomNavigation, navController);
            setupWindowInsets();
        }

        db = FirebaseDatabase.getInstance("https://b07-demo-summer-2024-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = db.getReference("testDemo");

        myRef.child("movies").setValue("B07 Demo!");
    }

    private void setupWindowInsets() {
        // Apply window insets to account for device elements
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(),
                (v, windowInsets) -> {
                    // System bars e.g. top status bar, navigation buttons
                    Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                    // On-screen keyboard
                    Insets ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime());

                    // Hide bottom nav menu if keyboard is on screen
                    boolean keyboardIsVisible =
                            ime.bottom > 0
                            && windowInsets.isVisible(WindowInsetsCompat.Type.ime());
                    bottomNavManager.updateKeyboardStatus(keyboardIsVisible);

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            ime.bottom
                    );

                    return windowInsets;
                });
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }
}