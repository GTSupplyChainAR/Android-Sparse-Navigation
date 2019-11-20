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

/**
 * Created by tinggu on 4/15/18.
 */

public class AisleView extends LinearLayout {
    private static final String TAG = "|AisleView|";

    private Context context;
    private TextView columnTag, rowTag, author_view, title_view, aisle_letter_view, instructions_view;
    private TextView[] rackNumTag;
    //private LinearLayout[] racks;
    private ImageView[] shelves;
    private ViewGroup shelving;

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
        this.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        this.setOrientation(HORIZONTAL);

        instructions_view = new TextView(context);
        instructions_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(instructions_view);
//        instructions_view.setText("P");
        instructions_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        this.addView(instructions_view);


//        shelving = new ViewGroup() {
//            @Override
//            protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//            }
//        }

        aisle_letter_view = new TextView(context);
//        LayoutParams lp = new LayoutParams(WC, WC);
//        lp.gravity = Gravity.CENTER;
//        columnTag.setLayoutParams(lp);
        applyTextStyle(aisle_letter_view );
        aisle_letter_view.setText("");
        this.addView(aisle_letter_view);


//        LinearLayout book_info_layout = new LinearLayout(context);
//        book_info_layout.setLayoutParams(new LayoutParams(0, MP, 0.7f));
//        book_info_layout.setOrientation(VERTICAL);
//        book_info_layout.setGravity(Gravity.CENTER);
        int padding_px = 10;
//        book_info_layout.setPadding(padding_px, padding_px, padding_px, padding_px);

//        author_view = new TextView(context);
//        author_view.setLayoutParams(new LayoutParams(WC, WC));
//        applyTextStyle(author_view);
//        author_view.setText("The other view");
//        book_info_layout.addView(author_view);
//
//        title_view = new TextView(context);
//        title_view.setLayoutParams(new LayoutParams(WC, WC));
//        applyTextStyle(title_view);
//        title_view.setText("Book Title");
//        book_info_layout.addView(title_view);
        //racks = new LinearLayout[6];
        rackNumTag = new TextView[6];

        int n = Prefs.VERTICAL_HEIGHT;
        shelves = new ImageView[n*6];

        for(int j = 0; j < Prefs.SHELVES_PER_AISLE; j++) {
            LinearLayout vertical_rack_layout = new LinearLayout(context);
            vertical_rack_layout.setLayoutParams(new LayoutParams(0, MP, 0.3f));
            vertical_rack_layout.setOrientation(VERTICAL);
            vertical_rack_layout.setPadding(0, padding_px, 0, padding_px);

            columnTag = new TextView(context);
            LayoutParams lp = new LayoutParams(WC, WC);
            lp.gravity = Gravity.CENTER;
            columnTag.setLayoutParams(lp);
            applyTextStyle(columnTag);
            if (j > 0) {
                columnTag.setText("" + j);
            }
            columnTag.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
            vertical_rack_layout.addView(columnTag);
            rackNumTag[j] = columnTag;



            for (int i = 0; i < Prefs.VERTICAL_HEIGHT; i++) {
                ImageView shelf = new ImageView(context);
                LayoutParams lpi = new LayoutParams(MP, 0, 1f);
                int margins = 10;
                lpi.setMargins(margins, margins, margins, margins);
                shelf.setLayoutParams(lpi);

                if(j == 0) {
                    rowTag = new TextView(context);
                    rowTag.setLayoutParams(lp);
                    applyTextStyle(rowTag);
                    rowTag.setText("Aisle " + (char)(i + 1 + 64));
                    rowTag.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
                    vertical_rack_layout.addView(rowTag);
                }

                //draw the border on the imageView
                if (j != 0) {
                    Bitmap bitmap = Bitmap.createBitmap(15, 5, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(colors[i]);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(0, 0, 15, 5, paint);
                    shelf.setImageBitmap(bitmap);
                }
                shelves[j*6+i] = shelf;
                vertical_rack_layout.addView(shelf);
            }
            //racks[j] = vertical_rack_layout;
            this.addView(vertical_rack_layout);
        }

        //this.addView(book_info_layout);
        //this.setTargetBook();

    }

    private void applyTextStyle(TextView textView){
        textView.setTextColor(Color.rgb(255,255,255));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
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

        for (int i = 0; i < 6; i++) {
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
        shelves[leftIndex*6+vertIndex].setBackgroundColor(colors[vertIndex]);


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
