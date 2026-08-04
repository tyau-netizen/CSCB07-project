package com.example.b07demosummer2024;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.R;

/**
 * Fragment showing TAAM's vision, mission, history, education/research
 * info, and contact details. Phone number, email, and address are tappable
 * to launch the device's dialer, email app, and maps app respectively, and
 * there's a button that opens TAAM's website for viewing current upcoming
 * events.
 */
public class LearnAboutUsFragment extends Fragment {

    private static final String PHONE_NUMBER = "+16479467698";
    private static final String EMAIL_ADDRESS = "info@taam.ca";
    private static final String EVENTS_URL = "https://taam.ca/index.php/en/what-s-on";
    private static final String ADDRESS = "255 Consumers Road, Toronto, Ontario, Canada M2J 4R3";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_about_us, container, false);

        // Tap phone number to open dialer pre-filled with TAAM's number
        TextView phoneText = view.findViewById(R.id.textContactPhone);
        phoneText.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PHONE_NUMBER));
            startActivity(dialIntent);
        });

        // Tap email to open default email app with TAAM's address pre-filled
        TextView emailText = view.findViewById(R.id.textContactEmail);
        emailText.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + EMAIL_ADDRESS));
            if (emailIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(getContext(), "No email app found.", Toast.LENGTH_SHORT).show();
            }
        });

        // Tap address to open default maps app with TAAM's location searched
        TextView addressText = view.findViewById(R.id.textContactAddress);
        addressText.setOnClickListener(v -> {
            Uri mapsUri = Uri.parse("geo:0,0?q=" + Uri.encode(ADDRESS));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(getContext(), "No maps app found.", Toast.LENGTH_SHORT).show();
            }
        });

        // Button to open TAAM's website events page in the browser
        Button viewEventsButton = view.findViewById(R.id.buttonViewEvents);
        viewEventsButton.setOnClickListener(v -> {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EVENTS_URL));
            startActivity(webIntent);
        });

        return view;
    }
}