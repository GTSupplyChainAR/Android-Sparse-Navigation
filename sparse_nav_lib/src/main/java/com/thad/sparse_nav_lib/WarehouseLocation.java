package com.thad.sparse_nav_lib;

import com.thad.sparse_nav_lib.Utils.Vec;

/**
 * Warehouse Location keeps track of two things:
 * The Grid Cell (row,col) that contains the location.
 * Displacement vector for position inside that grid cell
 * ... displacement vector must always be normalized.
 */

public class WarehouseLocation {
    private int row, col;
    private Vec displacement;

    public WarehouseLocation(){
        row = 0; col = 0;
        displacement = new Vec();
    }
}
