package com.bigandroiddev.vibify.Lists;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by spiros on 11/30/14.
 */
public class SortByString implements Comparator {
    private static String ITEM_KEY = "key", IMAGE_KEY = "image", APP_NAME_KEY = "appname";

    public int compare(Object o1, Object o2) {
        AppListItem p1 = ( (HashMap<String,AppListItem>) o1).get(ITEM_KEY);
        AppListItem p2 = ( (HashMap<String,AppListItem>) o2).get(ITEM_KEY);
        int i = p1.getName().compareToIgnoreCase(p2.getName());
        if (i < 0) return -1;
        else if (i == 0) return 0;
        else return 1;
    }
}
