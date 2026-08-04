package com.example.b07demosummer2024.accountmenu;

import com.example.b07demosummer2024.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the list of items shown in the account menu screen. Each item
 * pairs a display title with an icon, and is matched against its title in
 * AccountMenuPresenter to decide what happens when it's tapped.
 */
public class MenuRepository {

    /**
     * Builds the list of menu items shown on the account menu screen.
     *
     * @return the list of MenuItems to display, in display order
     */
    public List<MenuItem> getAccountMenuItems() {
        List<MenuItem> menuList = new ArrayList<>();

        menuList.add(new MenuItem("Learn About Us", android.R.drawable.ic_menu_info_details));
        menuList.add(new MenuItem("Log Out", R.drawable.outline_logout_24));

        return menuList;
    }
}