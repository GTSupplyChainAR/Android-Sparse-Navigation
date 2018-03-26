package com.thad.sparse_nav_lib;

import android.util.Log;

import com.thad.sparse_nav_lib.Utils.Vec;

/**
 * Warehouse Location keeps track of two things:
 * The Grid Cell (row,col) that contains the location.
 * Displacement vector for position inside that grid cell
 * ... displacement vector must always be normalized.
 */

public class WarehouseLocation {
    private static final String TAG = "|WarehouseLocation|";

    private int row, col;
    private Vec displacement;

    public WarehouseLocation(WarehouseLocation loc){
        this.row = loc.getCell()[0];
        this.col = loc.getCell()[1];
        this.displacement = new Vec(loc.getDisplacement());
    }
    public WarehouseLocation(){
        row = 0; col = 0;
        displacement = new Vec();
    }

    public void setCell(int row, int col){
        //Log.d(TAG, "Row/Col : "+row+"/"+col);
        this.row = row;
        this.col = col;
    }
    public void setDisplacement(Vec displacement){
        //Log.d(TAG, "Displacement : "+displacement.toString());
        this.displacement = displacement;
    }

    public int[] getCell(){return new int[]{row,col};}
    public Vec getDisplacement(){return displacement;}

    public String toString(){return row+","+col+" : ("+
            Math.floor(displacement.x*100)/100+", "+Math.floor(displacement.y*100)/100+")";}
}
