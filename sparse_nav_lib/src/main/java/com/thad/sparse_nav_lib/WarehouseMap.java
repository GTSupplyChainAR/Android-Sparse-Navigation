package com.thad.sparse_nav_lib;

/**
 * Created by theo on 2/20/18.
 */

public class WarehouseMap {

    private int grid_rows, grid_cols;

    public WarehouseMap(int w, int h){
        grid_rows = h;
        grid_cols = w;
    }

    public int[] getGridDims(){return new int[]{grid_rows, grid_cols};}
}
