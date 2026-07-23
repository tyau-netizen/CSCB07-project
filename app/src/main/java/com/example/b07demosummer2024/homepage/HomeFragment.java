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
import com.example.b07demosummer2024.base.BaseFragment;
import com.example.b07demosummer2024.databinding.FragmentHomeBinding;
import com.example.b07demosummer2024.user.SessionManager;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeContract.View,
        HomeContract.Presenter> implements HomeContract.View {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.handleInitialArguments(getArguments());

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

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(
                R.id.action_homeFragment_to_loginFragment);
    }

    @Override
    public void showWelcomeMessage(String username) {
        displayToastMessage("Welcome, " + username + "!");
    }
}
