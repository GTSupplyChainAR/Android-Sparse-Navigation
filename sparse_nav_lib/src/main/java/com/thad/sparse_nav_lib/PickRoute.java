package com.thad.sparse_nav_lib;

import com.thad.sparse_nav_lib.Utils.Vec;

import java.util.ArrayList;

/**
 * Created by theo on 4/9/18.
 */

public class PickRoute {
    private static final String TAG = "|PickRoute|";

    private ArrayList<Vec> orderedCells;
    private Book targetBook;

    public PickRoute(){


    }

    public void setTargetBook(Book book) {
        this.targetBook = book;
    }

    public void setOrderedCells(ArrayList<Vec> pick_cells) {
        this.orderedCells = pick_cells;
    }

    public Book getTargetBook() {
        return targetBook;
    }

    public ArrayList<Vec> getOrderedCells() {
        return orderedCells;
    }
}
