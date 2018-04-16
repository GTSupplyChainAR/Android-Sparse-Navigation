package com.thad.sparse_nav_lib.Static;

import android.content.Context;

import com.thad.sparse_nav_lib.Utils.Vec;
import com.thad.sparse_nav_lib.Utils.Vec3D;
import com.thad.sparse_nav_lib.WarehouseLocation;

/**
 * Created by theo on 3/25/18.
 */

public class UtilFunctions {

    public static int dp_to_pixels(Context context, float dp){
        return (int) ((dp)*context.getResources().getDisplayMetrics().density +0.5f);
    }

    public static Vec3D get3DLocation(WarehouseLocation loc){
        Vec3D pos = new Vec3D(0, 0, 0);

        int r = loc.getCell()[0], c = loc.getCell()[1];
        pos.x = c - Prefs.MAP_WIDTH/2 + 0.5f;
        pos.y = -(r - Prefs.MAP_HEIGHT/2 + 0.5f);

        return pos.add(loc.getDisplacement().toVec3D().mult(0.5f));
    }
    public static Vec3D get3DLocation(Vec cell2D){
        Vec3D pos = new Vec3D(0, 0, 0);

        int r = (int)cell2D.x, c = (int)cell2D.y;
        pos.x = c - Prefs.MAP_WIDTH/2 + 0.5f;
        pos.y = -(r - Prefs.MAP_HEIGHT/2 + 0.5f);


        return pos;
    }
}
