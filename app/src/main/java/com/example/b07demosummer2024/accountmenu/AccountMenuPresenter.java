package com.example.b07demosummer2024.accountmenu;

import com.example.b07demosummer2024.base.BasePresenter;
import com.example.b07demosummer2024.user.SessionManager;

import java.util.List;

public class AccountMenuPresenter extends BasePresenter<AccountMenuContract.View>
        implements AccountMenuContract.Presenter {
    private final SessionManager sessionManager;
    private final MenuRepository menuRepository;

    public AccountMenuPresenter() {
        this.sessionManager = SessionManager.getInstance();
        this.menuRepository = new MenuRepository();
    }

    @Override
    public void onViewCreated() {
        if (view != null) {
            List<MenuItem> items = menuRepository.getAccountMenuItems();
            view.showMenuItems(items);
        }
    }

    @Override
    public void onMenuItemClicked(MenuItem item) {
        if (view == null || item == null) return;

        switch (item.getTitle()) {
            case "Log Out":
                view.showLogoutConfirmationDialog();
                break;
        }
    }

    @Override
    public void onLogoutConfirmed() {
        sessionManager.endSession();

        if (view != null) {
            view.navigateToLogin();
        }
    }
}
