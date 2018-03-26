package com.thad.locationtracker.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.RelativeLayout;

import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.Static.UtilFunctions;

/**
 * Created by tinggu on 3/16/18.
 */

public class PickerView extends View {
    private static final String TAG = "|PickerView|";

    private Context context;
    private int xTouch = 0;
    private int yTouch = 0;
    private int radius = 30;

    // CONSTRUCTOR
    public PickerView(Context context, int x, int y) {
        super(context);
        this.context = context;
        setFocusable(true);
        xTouch = x;
        yTouch = y;
    }

    public void setLocation(int x, int y) {
        //Uncomment is you use drawable for cursor
        //this.setLeft(x);//2*(x- Prefs.SCREEN_WIDTH/2));
        //this.setTop(y);//2*(y- Prefs.SCREEN_HEIGHT/2) + UtilFunctions.dp_to_pixels(context, 25));
        //this.setRight(x+UtilFunctions.dp_to_pixels(context, radius));
        //this.setBottom(y+UtilFunctions.dp_to_pixels(context, radius));
        xTouch = x;
        yTouch = y;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.argb(50,255,255,0));
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(xTouch, yTouch, radius*2, p);

        p.setColor(Color.argb(100,255,255,0));
        canvas.drawCircle(xTouch, yTouch, (int)(radius*1.5), p);

        p.setColor(Color.argb(150,255,255,0));
        canvas.drawCircle(xTouch, yTouch, (int)(radius*1), p);

        p.setColor(Color.argb(200,255,255,0));
        canvas.drawCircle(xTouch, yTouch, (int)(radius*0.5), p);

    }

}
