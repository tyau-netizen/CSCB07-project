package com.example.b07demosummer2024.accountmenu;

public class MenuItem {
    private final String title;
    private final int iconResId;

    public MenuItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
