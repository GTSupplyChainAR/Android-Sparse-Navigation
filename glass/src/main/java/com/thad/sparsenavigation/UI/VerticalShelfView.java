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

public class VerticalShelfView extends LinearLayout {
    private static final String TAG = "|VerticalShelfView|";

    private Context context;
    private TextView columnTag, author_view, title_view, instructions_view;
    private ImageView[] shelves;
    private String book;
    private String author;

    private int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    // CONSTRUCTOR
    public VerticalShelfView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){
        this.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        this.setOrientation(LinearLayout.VERTICAL);


        int padding_px = 10;

        instructions_view = new TextView(context);
        instructions_view.setLayoutParams(new LayoutParams(MP, 60));
        applyTextStyle(instructions_view);
        instructions_view.setText("Press G for Next Book, Press C for Change View");
        instructions_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
        this.addView(instructions_view);

        LinearLayout layout2 = new LinearLayout(context);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);

        author_view = new TextView(context);
        author_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(author_view);
        author_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
        author_view.setPadding(0,80,80,0);
        layout2.addView(author_view);


        int rows = Prefs.ROWS_SHELF_VIEW;
        int cols = Prefs.COLUMNS_SHELF_VIEW;
        for(int i = 0; i  < cols; i++) {
            LinearLayout vertical_layout  = new LinearLayout(context);
            vertical_layout.setLayoutParams(new LayoutParams(0, MP, 0.3f));
            vertical_layout.setOrientation(VERTICAL);
            vertical_layout.setPadding(padding_px, padding_px, padding_px, padding_px);
            columnTag = new TextView(context);
            LayoutParams lp = new LayoutParams(WC, WC);
            lp.gravity = Gravity.CENTER;
            columnTag.setLayoutParams(lp);
            applyTextStyle(columnTag);
            columnTag.setText(Integer.toString(i +  1));
            vertical_layout.addView(columnTag);
            shelves = new ImageView[rows];
            for (int j = 0; j < rows; j++) {
                ImageView shelf = new ImageView(context);
                LayoutParams lpi = new LayoutParams(MP, 0, 1f);
                int margins = 10;
                lpi.setMargins( 0, margins, 0, margins);
                shelf.setLayoutParams(lpi);
                shelf.setBackgroundColor(Color.RED);
                shelves[j] = shelf;
                vertical_layout.addView(shelf);
            }
            layout2.addView(vertical_layout);
        }
        this.addView(layout2);
    }

    private void applyTextStyle(TextView textView){
        textView.setTextColor(Color.rgb(255,255,255));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
    }

    public void setTargetBook(Book newBook){

        author_view.setText("Author: " + newBook.getAuthor().toUpperCase());
        title_view.setText("Title: "+ newBook.getTitle().toUpperCase());
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

    public void setBook(String book) {
        this.book = book;
        author_view.setText("Book Title \n" + this.book +"\nBook Author \n" + this.author);
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
