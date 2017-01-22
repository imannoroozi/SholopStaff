package com.sholop.sholopstaff.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.objects.DrawerItem;
import com.sholop.sholopstaff.utilities.Util;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    Typeface fontRegular, fontAwesome;

    public CustomDrawerAdapter(Context context, int layoutResourceID, List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

        fontAwesome = Util.getInstance().getFontAwesome(context);
        fontRegular = Util.getInstance().getFontRegular(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);

            drawerHolder.itemIcon = (TextView) view.findViewById(R.id.item_icon);
            drawerHolder.itemTitle = (TextView) view.findViewById(R.id.item_title);

            view.setTag(drawerHolder);

            drawerHolder.itemIcon.setTypeface(fontAwesome);
            drawerHolder.itemTitle.setTypeface(fontRegular);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        DrawerItem dItem = this.drawerItemList.get(position);

        drawerHolder.itemTitle.setText(dItem.getItemTitle());
        drawerHolder.itemIcon.setText(dItem.getItemIcon());

        return view;
    }

    private static class DrawerItemHolder {
        TextView itemTitle, itemIcon;
    }
}