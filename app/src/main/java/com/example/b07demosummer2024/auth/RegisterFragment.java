package com.example.b07demosummer2024.auth;

import android.os.Bundle;
import com.example.b07demosummer2024.user.User;
import com.example.b07demosummer2024.user.UserRepository;
import com.google.firebase.auth.FirebaseUser;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.b07demosummer2024.R;

/**
 * Fragment that handles new user registration.
 * Allows users to create an account using email and password via Firebase Auth.
 */
public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // Get references to UI elements
        EditText usernameField = view.findViewById(R.id.register_username_input);
        EditText emailField = view.findViewById(R.id.login_email_input);
        EditText passwordField = view.findViewById(R.id.login_password_input);
        Button registerButton = view.findViewById(R.id.registerButton);
        TextView loginText = view.findViewById(R.id.textViewLogin);

        // Handle register button click
        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            // Validate fields arent empty

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Create acc with Firebase Auth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = firebaseUser.getUid();
                            User newUser = new User(uid, username);
                            UserRepository userRepository = new UserRepository();
                            userRepository.saveNewUserProfile(newUser, new UserRepository.UserSaveCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                                    // Navigate back to LoginFragment
                                    Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(getContext(), "Failed to save user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
        // Navigate back to login screen when "Already have an account?" is clicked
        loginText.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_registerFragment_to_loginFragment);
        });
        return view;
    }
}