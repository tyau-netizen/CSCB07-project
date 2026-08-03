package com.example.b07demosummer2024.accountmenu;

import com.example.b07demosummer2024.R;

import java.util.ArrayList;
import java.util.List;

public class MenuRepository {
    public List<MenuItem> getAccountMenuItems() {
        List<MenuItem> menuList = new ArrayList<>();

        menuList.add(new MenuItem("Log Out", R.drawable.outline_logout_24));

        return menuList;
    }
}
