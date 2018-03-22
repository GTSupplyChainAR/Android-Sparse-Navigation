package com.thad.locationtracker.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by tinggu on 3/16/18.
 */

public class PickerView extends View {

    int xTouch = -100;
    int yTouch = -100;
    int radius = 50;

    // CONSTRUCTOR
    public PickerView(Context context, int x, int y) {
        super(context);
        setFocusable(true);
        xTouch = x;
        yTouch = y;

    }

    public void setLocation(int x, int y)
    {
        xTouch = x;
        yTouch = y;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.YELLOW);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(xTouch, yTouch, radius, p);

    }



}
