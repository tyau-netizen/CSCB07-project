package com.example.b07demosummer2024.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.databinding.FragmentHomeBinding;
import com.example.b07demosummer2024.user.SessionManager;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate fragment layout using ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Click listeners
        binding.buttonRecyclerView.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_homeFragment_to_recyclerViewFragment);
        });

        binding.buttonScroller.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_homeFragment_to_scrollerFragment);
        });

        binding.buttonSpinner.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_homeFragment_to_spinnerFragment);
        });

        binding.buttonManageItems.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_homeFragment_to_manageItemsFragment);
        });
    }
}
