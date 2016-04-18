package com.example.lordofthering.mapapp.Adapter;

import android.graphics.drawable.Drawable;

/**
 * Created by lordo on 08.03.2016.
 */
public class PojoForList {
    private int id;
    private String title;
    private String subitem;
    private Drawable drawable;

    public PojoForList(int id, String title, String subitem, Drawable drawable) {
        this.id = id;
        this.title = title;
        this.subitem = subitem;
        this.drawable = drawable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubitem() {
        return subitem;
    }

    public void setSubitem(String subitem) {
        this.subitem = subitem;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
