package com.thad.sparsenavigation.UI;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.AndroidClient;
import com.thad.sparsenavigation.Graphics.GLRenderer;
import com.thad.sparsenavigation.Graphics.GraphicsGLView;
import com.thad.sparsenavigation.R;

/**
 * Responsible for all-things-UI.
 */

public class UserInterfaceHandler {
    private static final String TAG = "|UserInterfaceHandler|";

    private Activity mMain;
    private AndroidClient mClient;
    private GraphicsGLView glView;

    public UserInterfaceHandler(Context context, AndroidClient androidClient){
        mMain = (Activity) context;
        mClient = androidClient;
        init();
    }

    private void init(){
        LinearLayout container = mMain.findViewById(R.id.graphics_container);

        glView = new GraphicsGLView(mMain);
        container.addView(glView);
    }

    public void setMap(WarehouseMap map){

    }
}
