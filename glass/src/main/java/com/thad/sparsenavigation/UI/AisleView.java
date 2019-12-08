package com.thad.sparsenavigation.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinggu on 4/15/18.
 */

public class AisleView extends LinearLayout {
    private static final String TAG = "|AisleView|";

    private Context context;
    private TextView columnTag, rowTag, author_view, title_view, aisle_letter_view, instructions_view, instructions_view2;
    private int padding_px;
    private TextView[] rackNumTag;
    //private LinearLayout[] racks;
    private ImageView[] shelves;
    private ViewGroup shelving;
    private List<Canvas> canvases;

    private int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    int[] colors = {Color.RED, Color.rgb(255,165,0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.RED, Color.RED};
    // CONSTRUCTOR
    public AisleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){
        canvases = new ArrayList<>();
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));
        this.setOrientation(LinearLayout.VERTICAL);

        instructions_view2 = new TextView(context);
        instructions_view2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 35));
        applyTextStyle(instructions_view2);
        instructions_view2.setText("Press G for Next Book, Press C for Change View");
        instructions_view2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
        this.addView(instructions_view2);


        LinearLayout layout2 = new LinearLayout(context);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        padding_px = 10;



        rackNumTag = new TextView[Prefs.SHELVES_PER_AISLE];


//        int n = Prefs.VERTICAL_HEIGHT;
        shelves = new ImageView[Prefs.SHELVES_PER_AISLE*Prefs.VERTICAL_HEIGHT];
        Log.d("TAG", "" + shelves.length);

        for(int j = 0; j < Prefs.SHELVES_PER_AISLE; j++) {
            LinearLayout vertical_rack_layout = new LinearLayout(context);
            vertical_rack_layout.setLayoutParams(new LayoutParams(0, MP, 0.3f));
            vertical_rack_layout.setOrientation(VERTICAL);
            vertical_rack_layout.setPadding(0, padding_px, 0, padding_px);
            if (j % 2 == 0) {
                vertical_rack_layout.setPadding(0, padding_px, 40, padding_px);
            }

            columnTag = new TextView(context);
            LayoutParams lp = new LayoutParams(WC, WC);
            lp.gravity = Gravity.CENTER;
            columnTag.setLayoutParams(lp);
            applyTextStyle(columnTag);
            columnTag.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
            vertical_rack_layout.addView(columnTag);
            rackNumTag[j] = columnTag;
            for (int i = 0; i < Prefs.VERTICAL_HEIGHT; i++) {
                ImageView shelf = new ImageView(context);
                LayoutParams lpi = new LayoutParams(MP, 0, 1f);
                int margins = 10;
                if (i == 1) {
                    margins = 0;
                }
                lpi.setMargins(0, margins, 0, margins);
                shelf.setLayoutParams(lpi);


                if(i == 1) {
                    rowTag = new TextView(context);
                    rowTag.setLayoutParams(lp);
                    applyTextStyle(rowTag);
                    rowTag.setText("" + (char)(j + 1 + 64));
                    rowTag.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                    vertical_rack_layout.addView(rowTag);
                }

                //draw the border on the imageView
                if (i != 0 && i != 1) {
                    Bitmap bitmap = Bitmap.createBitmap(10, 15, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(colors[j]);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(0, 0, 10, 15, paint);
                    canvases.add(canvas);
                    shelf.setImageBitmap(bitmap);
                    shelves[j * Prefs.VERTICAL_HEIGHT + i] = shelf;
                    vertical_rack_layout.addView(shelf);
                }
            }
            layout2.addView(vertical_rack_layout);
        }
        this.addView(layout2);

        //this.setTargetBook();

    }

    private void applyTextStyle(TextView textView){
        textView.setTextColor(Color.rgb(255,255,255));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
    }

    public void setTargetShelf(int column, int row) {
        Canvas canvas = canvases.get(column * (Prefs.VERTICAL_HEIGHT-2) + row);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colors[column]);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, 10, 15, paint);
    }

    public void removeTargetShelf(int column, int row) {
        Canvas canvas = canvases.get(column * (Prefs.VERTICAL_HEIGHT-2) + row);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, 10, 15, paint);
        paint.setColor(colors[column]);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, 10, 15, paint);
    }

    public void setTargetBook(Book newBook){

//        author_view.setText("Author: " + newBook.getAuthor().toUpperCase());
//        title_view.setText("Title: "+newBook.getTitle().toUpperCase());
        //String tag = newBook.getLocationTag();
        // looks like "D-C-110-F"

        String tag = newBook.getLocationTag();
        Log.d(TAG, tag);
        char aisleTag = tag.charAt(2);
        aisle_letter_view.setText(Character.toString(aisleTag));


        int rackTag = Integer.parseInt(tag.substring(4,7));
        char vertTag = tag.charAt(tag.length()-1);


        int leftIndex = 0;
        switch (rackTag){
            case 100: leftIndex = 0; break;
            case 111: leftIndex = 0; break;
            case 102: leftIndex = 1; break;
            case 109: leftIndex = 1; break;
            case 104: leftIndex = 2; break;
            case 107: leftIndex = 2; break;
            case 108: leftIndex = 3; break;
            case 105: leftIndex = 3; break;
            case 110: leftIndex = 4; break;
            case 103: leftIndex = 4; break;
            case 112: leftIndex = 5; break;
            case 101: leftIndex = 5; break;

        }

        for (int i = 0; i < Prefs.VERTICAL_HEIGHT; i++) {
           rackNumTag[i].setText("");
        }

        rackNumTag[leftIndex].setText(Integer.toString(rackTag));
//
        int vertIndex = 0;
        switch (vertTag){
            case 'A': vertIndex = 0; break;
            case 'B': vertIndex = 1; break;
            case 'C': vertIndex = 2; break;
            case 'D': vertIndex = 3; break;
            case 'E': vertIndex = 4; break;
            case 'F': vertIndex = 5; break;
        }
        Log.d(TAG, Integer.toString(vertIndex));


       //fill color
        for (int i = 0; i < 36; i++) {
            shelves[i].setBackgroundColor(Color.BLACK);
        }
        shelves[leftIndex*Prefs.VERTICAL_HEIGHT+vertIndex].setBackgroundColor(colors[vertIndex]);


//        for(ImageView shelf : shelves)
//            shelf.setBackgroundColor(Color.RED);
//        shelves[vertIndex].setBackgroundColor(Color.GREEN);
//
//        int rackIndex = Integer.parseInt(tag.charAt(tag.length() - 4)+""
//                +tag.charAt(tag.length()-3));
//        if(rackIndex%2==1) {
//            rackIndex--;
//            rackIndex = 10-rackIndex;
//        }
//        rackIndex /= 2;
//        rackIndex ++;
//        columnTag.setText(rackIndex+"");
        //title_view.setText(tag);
    }
}
