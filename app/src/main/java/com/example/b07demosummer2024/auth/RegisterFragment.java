package com.example.b07demosummer2024.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
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
        EditText emailField = view.findViewById(R.id.editTextTextEmailAddress);
        EditText passwordField = view.findViewById(R.id.editTextTextPassword);
        Button registerButton = view.findViewById(R.id.registerButton);
        TextView loginText = view.findViewById(R.id.textViewLogin);

        // Handle register button click
        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            // Validate fields arent empty

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Create acc with Firebase Auth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
        // Navigate back to login screen when "Already have an account?" is clicked
        loginText.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new LoginFragment());
            transaction.commit();
        });
        return view;
    }
}