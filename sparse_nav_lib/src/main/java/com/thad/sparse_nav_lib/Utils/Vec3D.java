package com.thad.sparse_nav_lib.Utils;

/**
 * Created by theo on 4/4/18.
 */

public class Vec3D {
    public double x,y,z;

    public Vec3D(Vec3D v){ x = v.x; y = v.y; z = v.z;}
    public Vec3D(){x = 0; y = 0; z = 0;}
    public Vec3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D normalize(){
        double length = Math.sqrt(x*x + y*y + z*z);
        return new Vec3D(x/length, y/length, z/length);
    }

    public float distance(Vec3D v){return (float)Math.sqrt(
            Math.pow(x-v.x, 2) + Math.pow(y-v.y, 2) + Math.pow(z-v.z, 2));
    }

    public Vec3D mult(float m){return new Vec3D(x*m, y*m, z*m);}
    public Vec3D add(Vec3D v){
        return new Vec3D(x+v.x, y + v.y, z + v.z);
    }
    public Vec3D sub(Vec3D v){
        return new Vec3D(x-v.x, y - v.y, z - v.z);
    }
    public Vec3D reverse(){return new Vec3D(-x, -y, -z);}

    public String toString(){
        return x + ", " + y + ", " + z;
    }
}
