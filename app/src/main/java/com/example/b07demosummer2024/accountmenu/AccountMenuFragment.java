package com.example.b07demosummer2024.accountmenu;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.base.BaseFragment;
import com.example.b07demosummer2024.databinding.FragmentAccountMenuBinding;

import java.util.List;

public class AccountMenuFragment extends BaseFragment<FragmentAccountMenuBinding,
        AccountMenuContract.View, AccountMenuContract.Presenter>
        implements AccountMenuContract.View {

    @NonNull
    @Override
    protected FragmentAccountMenuBinding inflateBinding(@NonNull LayoutInflater inflater,
                                                        @Nullable ViewGroup container) {
        return FragmentAccountMenuBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    protected AccountMenuContract.Presenter createPresenter() {
        return new AccountMenuPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.accountMenuRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.accountMenuRecycler.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        presenter.onViewCreated();
    }

    @Override
    public void showMenuItems(List<MenuItem> menuItems) {
        AccountMenuAdapter adapter = new AccountMenuAdapter(menuItems, item ->
                presenter.onMenuItemClicked(item));
        binding.accountMenuRecycler.setAdapter(adapter);
    }

    @Override
    public void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", (dialog, which) ->
                        presenter.onLogoutConfirmed())
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(
                R.id.action_accountMenuFragment_to_loginFragment);
    }

    @Override
    public void navigateToLearnAboutUs() {
        Navigation.findNavController(requireView()).navigate(
                R.id.action_accountMenuFragment_to_learnAboutUsFragment);
    }
}