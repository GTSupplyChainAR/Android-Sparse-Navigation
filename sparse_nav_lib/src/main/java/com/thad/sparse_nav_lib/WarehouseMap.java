package com.thad.sparse_nav_lib;

import android.util.Log;

/**
 * Created by theo on 2/20/18.
 */

public class WarehouseMap {
    private static final String TAG = "|WarehouseMap|";

    private int grid_rows, grid_cols;
    private double ratio;

    public WarehouseMap(int w, int h){
        grid_rows = h;
        grid_cols = w;
        ratio = ((double)w)/h;
    }

    public int[] getGridDims(){return new int[]{grid_rows, grid_cols};}
    public double getRatio(){return ratio;}
}
