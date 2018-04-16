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

/**
 * Created by tinggu on 4/15/18.
 */

public class VerticalShelfView extends LinearLayout {
    private static final String TAG = "|VerticalShelfView|";

    private Context context;
    private TextView columnTag, author_view, title_view;
    private ImageView[] shelves;

    private int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    // CONSTRUCTOR
    public VerticalShelfView(Context context) {
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

        LinearLayout book_info_layout = new LinearLayout(context);
        book_info_layout.setLayoutParams(new LayoutParams(0, MP, 0.7f));
        book_info_layout.setOrientation(VERTICAL);
        book_info_layout.setGravity(Gravity.CENTER);
        int padding_px = 30;
        book_info_layout.setPadding(padding_px, padding_px, padding_px, padding_px);

        author_view = new TextView(context);
        author_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(author_view);
        author_view.setText("Author");
        book_info_layout.addView(author_view);

        title_view = new TextView(context);
        title_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(title_view);
        title_view.setText("Book Title");
        book_info_layout.addView(title_view);

        LinearLayout vertical_rack_layout = new LinearLayout(context);
        vertical_rack_layout.setLayoutParams(new LayoutParams(0, MP, 0.3f));
        vertical_rack_layout.setOrientation(VERTICAL);
        vertical_rack_layout.setPadding(padding_px, padding_px, padding_px, padding_px);

        columnTag = new TextView(context);
        LayoutParams lp = new LayoutParams(WC, WC);
        lp.gravity = Gravity.CENTER;
        columnTag.setLayoutParams(lp);
        applyTextStyle(columnTag);
        columnTag.setText("1");
        vertical_rack_layout.addView(columnTag);


        int n = Prefs.VERTICAL_HEIGHT;
        shelves = new ImageView[n];
        for(int i = 0; i < Prefs.VERTICAL_HEIGHT ; i++){
            ImageView shelf = new ImageView(context);
            LayoutParams lpi = new LayoutParams(MP,0, 1f);
            int margins = 15;
            lpi.setMargins(margins, margins, margins, margins);
            shelf.setLayoutParams(lpi);

            shelf.setBackgroundColor(Color.RED);
            shelves[i] = shelf;
            vertical_rack_layout.addView(shelf);
        }


        this.addView(book_info_layout);
        this.addView(vertical_rack_layout);
    }

    private void applyTextStyle(TextView textView){
        textView.setTextColor(Color.rgb(255,255,255));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
    }

    public void setTargetBook(Book newBook){

        author_view.setText("Author: " + newBook.getAuthor().toUpperCase());
        title_view.setText("Title: "+newBook.getTitle().toUpperCase());
        String tag = newBook.getLocationTag();
        Log.d(TAG, tag);
        char vertTag = tag.charAt(tag.length()-1);
        int vertIndex = 0;
        switch (vertTag){
            case 'A': vertIndex = 0; break;
            case 'B': vertIndex = 1; break;
            case 'C': vertIndex = 2; break;
            case 'D': vertIndex = 3; break;
            case 'E': vertIndex = 4; break;
            case 'F': vertIndex = 5; break;
        }
        for(ImageView shelf : shelves)
            shelf.setBackgroundColor(Color.RED);
        shelves[vertIndex].setBackgroundColor(Color.GREEN);

        int rackIndex = Integer.parseInt(tag.charAt(tag.length() - 4)+""
                                                +tag.charAt(tag.length()-3));
        if(rackIndex%2==1) {
            rackIndex--;
            rackIndex = 10-rackIndex;
        }
        rackIndex /= 2;
        rackIndex ++;
        columnTag.setText(rackIndex+"");
        //title_view.setText(tag);
    }
}
