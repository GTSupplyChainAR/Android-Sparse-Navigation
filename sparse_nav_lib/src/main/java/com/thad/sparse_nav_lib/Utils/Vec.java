package com.thad.sparse_nav_lib.Utils;

/**
 * Created by theo on 2/20/18.
 */

public class Vec {
    public double x,y;

    public Vec(Vec v){ x = v.x; y = v.y;}
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

    public float distance(Vec v){return (float)Math.sqrt(Math.pow(x-v.x, 2)+ Math.pow(y-v.y, 2));}

    public Vec add(Vec v){
        return new Vec(x+v.x, y + v.y);
    }
    public Vec sub(Vec v){
        return new Vec(x-v.x, y - v.y);
    }
    public Vec mult(float m){return new Vec(x*m, y*m);}
    public Vec reverse(){return new Vec(-x, -y);}
    public Vec perpendicular(){return new Vec(-y, x);}

    public Vec3D toVec3D(){return new Vec3D(x, y, 0);}

    public String toString(){
        return x+", "+y;
    }
}
