package com.thad.sparse_nav_lib;

import com.thad.sparse_nav_lib.Utils.Vec;

import java.util.ArrayList;

/**
 * Created by theo on 4/9/18.
 */

public class PickPath {
    private ArrayList<PickRoute> orderedRoutes;

    private int index = -1;

    public PickPath(){
        orderedRoutes = new ArrayList<PickRoute>();
    }

    public void addRoute(PickRoute route){orderedRoutes.add(route);}
    public PickRoute getNextRoute(){
        if(orderedRoutes.size() == 0)
            return null;
        if(orderedRoutes == null || index >= orderedRoutes.size())
            return new PickRoute();
        return orderedRoutes.get(++index);
    }
}
