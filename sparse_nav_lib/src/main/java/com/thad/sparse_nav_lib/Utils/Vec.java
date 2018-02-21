package com.thad.sparse_nav_lib.Utils;

/**
 * Created by theo on 2/20/18.
 */

public class Vec {
    public double x,y;

    public Vec(){x = 0; y = 0;}
    public Vec(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void normalize(){
        double length = Math.sqrt(x*x + y*y);
        x /= length;
        y /= length;
    }
}
