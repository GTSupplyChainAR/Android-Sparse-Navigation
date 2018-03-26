package com.thad.sparse_nav_lib.Static;

import android.content.Context;

/**
 * Created by theo on 3/25/18.
 */

public class UtilFunctions {

    public static int dp_to_pixels(Context context, float dp){
        return (int) ((dp)*context.getResources().getDisplayMetrics().density +0.5f);
    }
}
