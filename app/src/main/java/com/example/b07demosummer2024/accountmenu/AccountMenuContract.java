package com.example.b07demosummer2024.accountmenu;

import com.example.b07demosummer2024.base.BaseContract;

import java.util.List;

/**
 * Basically the main rulebook for the account menu screen.
 * It holds the View and Presenter interfaces so both sides know what methods they need to implement for our MVP setup.
 */
public interface AccountMenuContract {

    /**
     * The View part of our MVP. This handles all the actual UI stuff like showing the list or moving to new screens.
     */
    interface View extends BaseContract.View {

        /**
         * Throws the list of menu items onto the screen.
         * * @param menuItems The list of items we want to display
         */
        void showMenuItems(List<MenuItem> menuItems);

        /**
         * Kicks the user back to the login screen (usually called after they successfully log out).
         */
        void navigateToLogin();

        /**
         * Pops up that "Are you sure you want to log out?" warning box so they don't do it by accident.
         */
        void showLogoutConfirmationDialog();

        /**
         * Takes the user to the Learn About Us page when they tap on it in the list.
         */
        void navigateToLearnAboutUs();
    }

    /**
     * The brain of the menu. It catches clicks from the View and decides what actually happens behind the scenes.
     */
    interface Presenter extends BaseContract.Presenter<View> {

        /**
         * Runs when the screen is first set up. It basically tells the View to grab and load up the menu items.
         */
        void onViewCreated();

        /**
         * Figures out what to do when a user taps on a specific menu item.
         * * @param item The exact menu item the user just poked
         */
        void onMenuItemClicked(MenuItem item);

        /**
         * Does the actual logging out stuff (like clearing the session) after the user hits 'yes' on the warning dialog.
         */
        void onLogoutConfirmed();
    }
}
