package com.thad.locationtracker.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.thad.locationtracker.Prefs;
import com.thad.locationtracker.R;
import com.thad.sparse_nav_lib.Utils.UtilFunctions;

/**
 * Created by tinggu on 3/16/18.
 */

public class PickerView extends View {
    private static final String TAG = "|PickerView|";

    private Context context;
    private int xTouch = -100;
    private int yTouch = -100;
    private int radius = 50;

    // CONSTRUCTOR
    public PickerView(Context context, int x, int y) {
        super(context);
        this.context = context;
        setFocusable(true);
        xTouch = x;
        yTouch = y;
    }

    public void setLocation(int x, int y) {
        //this.setLeft(2*(x- Prefs.SCREEN_WIDTH/2));
        //this.setTop(2*(y-Prefs.SCREEN_HEIGHT/2) + UtilFunctions.dp_to_pixels(context, 25));
        xTouch = x;
        yTouch = y;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint pblack = new Paint();
        pblack.setAntiAlias(true);
        pblack.setColor(Color.BLACK);
        pblack.setStyle(Paint.Style.FILL);
        canvas.drawCircle(xTouch, yTouch, (int)(radius*1.1), pblack);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.YELLOW);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(xTouch, yTouch, radius, p);
    }



}
