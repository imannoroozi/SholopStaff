package com.sholop.sholopstaff.objects;


public class DrawerItem {

    String itemTitle, itemIcon;

    public DrawerItem(String itemTitle, String itemIcon) {
        super();
        this.itemTitle = itemTitle;
        this.itemIcon = itemIcon;
    }

    public String getItemTitle() {
        return itemTitle;
    }
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    public String getItemIcon() {
        return itemIcon;
    }
    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }
}