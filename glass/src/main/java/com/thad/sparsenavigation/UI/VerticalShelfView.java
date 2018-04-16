package com.thad.sparsenavigation.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by tinggu on 4/15/18.
 */

public class VerticalShelfView extends LinearLayout {
    private static final String TAG = "|VerticalShelfView|";

    private Context context;
    private Button customBtn;
    private TextView textview;

    // CONSTRUCTOR
    public VerticalShelfView(Context context) {
        super(context);
        init(context);
        Log.d(TAG, "Oh my gad");
    }

    private void init(Context context){
        this.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));


        textview = new TextView(context);
        textview.setText("Pavane\nKeith roberts\nF");
        LayoutParams textParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.BOTTOM;
        textParams.gravity = Gravity.LEFT;
        //buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textview.setLayoutParams(textParams);
        textview.setTextColor(Color.rgb(255,255,255));
        textview.setTypeface(null, Typeface.BOLD);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f);

        this.addView(textview);
    }

}
