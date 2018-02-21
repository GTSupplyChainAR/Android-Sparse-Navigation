package com.thad.sparsenavigation.UI;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.AndroidClient;
import com.thad.sparsenavigation.R;

/**
 * Responsible for all-things-UI.
 */

public class UserInterfaceHandler {
    private static final String TAG = "|UserInterfaceHandler|";

    private Activity mMain;
    private AndroidClient mClient;

    public UserInterfaceHandler(Context context, AndroidClient androidClient){
        mMain = (Activity) context;
        mClient = androidClient;
    }

    public void setMap(WarehouseMap map){

    }
}
