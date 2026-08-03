package com.example.b07demosummer2024.accountmenu;

import com.example.b07demosummer2024.base.BaseContract;

import java.util.List;

public interface AccountMenuContract {
    interface View extends BaseContract.View {
        void showMenuItems(List<MenuItem> menuItems);
        void navigateToLogin();
        void showLogoutConfirmationDialog();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onViewCreated();
        void onMenuItemClicked(MenuItem item);
        void onLogoutConfirmed();
    }
}
