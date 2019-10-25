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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thad.sparse_nav_lib.Book;
import com.thad.sparse_nav_lib.Static.Prefs;

import org.w3c.dom.Text;

/**
 * Created by tinggu on 4/15/18.
 */

public class CompletionView extends LinearLayout {
    private static final String TAG = "|Completion View|";

    private Context context;
    private TextView instructions_view;
    private int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    // CONSTRUCTOR
    public CompletionView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    private void init(){
        this.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        this.setOrientation(LinearLayout.HORIZONTAL);


        instructions_view = new TextView(context);
        instructions_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(instructions_view);
        instructions_view.setText("Press Trigger for Next Book, Bumper for Change View");
        instructions_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        this.addView(instructions_view);
    }

    private void applyTextStyle(TextView textView){
        textView.setTextColor(Color.rgb(255,255,255));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
    }

}
