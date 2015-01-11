package com.bigandroiddev.vibify.Lists;

import android.graphics.drawable.Drawable;

/**
 * Created by spiros on 11/30/14.
 */
public class AppListItem {
    private Drawable logo;
    private String name, packageName;


    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}