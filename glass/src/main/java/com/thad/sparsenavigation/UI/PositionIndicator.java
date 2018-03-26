package com.thad.sparsenavigation.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.Static.UtilFunctions;
import com.thad.sparsenavigation.R;

/**
 * Created by theo on 3/26/18.
 */

public class PositionIndicator extends ImageView {
    private static final String TAG = "|PickerView|";

    private Context context;
    private int xTouch;
    private int yTouch;
    private int size = 30;

    private float currentHeading = 0f;

    // CONSTRUCTOR
    public PositionIndicator(Context context) {
        super(context);
        this.context = context;
        setFocusable(true);
        xTouch = 0; yTouch = 0;
        size = UtilFunctions.dp_to_pixels(context, size);

        this.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
        this.setBackground(context.getResources().getDrawable(R.drawable.cursor));
    }

    public void setLocation(int x, int y) {
        //Uncomment is you use drawable for cursor
        this.setLeft(x-size/2);//2*(x- Prefs.SCREEN_WIDTH/2));
        this.setTop(y-size/2);//2*(y-Prefs.SCREEN_HEIGHT/2));
        this.setRight(x+size/2);
        this.setBottom(y+size/2);
        //xTouch = x;
        //yTouch = y;
    }

    public void setHeading(float heading){
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentHeading, heading,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        this.startAnimation(ra);
        currentHeading = heading;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.argb(100, 255,0,0));
        p.setStyle(Paint.Style.FILL);
        //canvas.drawCircle(xTouch, yTouch, size*2, p);

        p.setColor(Color.argb(255, 255,0,0));
        //canvas.drawCircle(xTouch, yTouch, size, p);

    }

}
