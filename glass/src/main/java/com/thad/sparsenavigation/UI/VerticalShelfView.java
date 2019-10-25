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
        this.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout book_info_layout = new LinearLayout(context);
        book_info_layout.setLayoutParams(new LayoutParams(0, MP, 0.7f));
        book_info_layout.setOrientation(VERTICAL);
        book_info_layout.setGravity(Gravity.CENTER);
        int padding_px = 30;
        book_info_layout.setPadding(padding_px, padding_px, padding_px, padding_px);
        
        instructions_view = new TextView(context);
        instructions_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(instructions_view);
        instructions_view.setText("Press Trigger for Next Book, Bumper for Change View");
        instructions_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        book_info_layout.addView(instructions_view);

        author_view = new TextView(context);
        author_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(author_view);
        author_view.setText("Book Title");
        author_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        book_info_layout.addView(author_view);

        title_view = new TextView(context);
        title_view.setLayoutParams(new LayoutParams(WC, WC));
        applyTextStyle(title_view);
        title_view.setText("Book Author");
        title_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        book_info_layout.addView(title_view);
        this.addView(book_info_layout);


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
                int margins = 15;
                lpi.setMargins(margins, margins, margins, margins);
                shelf.setLayoutParams(lpi);
                shelf.setBackgroundColor(Color.RED);
                shelves[j] = shelf;
                vertical_layout.addView(shelf);
            }
            this.addView(vertical_layout);
        }
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
}
