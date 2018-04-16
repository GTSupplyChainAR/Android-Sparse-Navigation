package com.thad.sparsenavigation.Communications;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thad.sparse_nav_lib.Decoder;
import com.thad.sparse_nav_lib.PickPath;
import com.thad.sparse_nav_lib.PickRoute;
import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.Utils.Vec;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.Book;
import com.thad.sparsenavigation.Communications.ClientBluetooth;
import com.thad.sparsenavigation.GlassClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This Class handles the communications between Mobile App and glass.
 * It decodes the messages received on Bluetooth and populates the data structures.
 */

//TO DO
// Check for errors in Picking Data format

public class CommunicationHandler {
    private static final String TAG = "|CommunicationHandler|";

    private GlassClient mClient;

    //-- TING --
    //These 2 variables should be updated with the latest data from Firebase/ Websocket.
    private WarehouseLocation currentLocation;
    private PickPath currentPickPath;


    //private ClientBluetooth bluetooth;
    private DatabaseReference mPostReference;




    public CommunicationHandler(GlassClient client){
        mClient = client;
        currentLocation = new WarehouseLocation();
        currentPickPath = new PickPath();

        //bluetooth = new ClientBluetooth(this);
        //bluetooth.setAddress(Prefs.PHONE_ADDRESS, Prefs.GLASS_UUID);
        //bluetooth.connect();

        // Add value event listener to the post
        // [START post_value_event_listener]
        mPostReference = FirebaseDatabase.getInstance().getReference();
//                .child("warehouseLayout").child("currentLocation");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Long cellRow = (Long) dataSnapshot.child("warehouseLayout").child("currentLocation").child("row").getValue();
                Long cellCol = (Long) dataSnapshot.child("warehouseLayout").child("currentLocation").child("col").getValue();
                Log.d(TAG, "row: " +dataSnapshot.child("warehouseLayout").child("currentLocation").child("row"));
                Log.d(TAG, "VectorX: " +dataSnapshot.child("warehouseLayout").child("currentLocation").child("vecx"));
                String vecX = (String) dataSnapshot.child("warehouseLayout").child("currentLocation").child("vecx").getValue();
                String vecY = (String)dataSnapshot.child("warehouseLayout").child("currentLocation").child("vecx").getValue();
                Float vecx = Float.parseFloat(vecX);
                Float vecy = Float.parseFloat(vecY);
                int pick_path_index = 0;
                int book_index = 0;

                for (book_index = 0; book_index < 11; book_index++) {
                    Book targetBook = new Book();
                    ArrayList<Vec> ordered_cells = new ArrayList<Vec>();
                    int cell_index = 0;
                    String author = (String) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("book").child("author").getValue();
                    String tag = (String) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("book").child("tag").getValue();
                    String title = (String) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("book").child("title").getValue();
                    Long bookRow = (Long) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("location").child("0").getValue();
                    Long bookCol = (Long) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("location").child("1").getValue();
                    if(book_index!=10) {
                        targetBook.setAuthor(author);
                        targetBook.setLocationTag(tag);
                        targetBook.setTitle(title);
                        targetBook.setCell(bookRow.intValue(), bookCol.intValue());
                    }

                    while((Long) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("cellByCellPathToTargetBookLocation").child(cell_index+"").child("0").getValue() != null) {
                        Long x = (Long) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("cellByCellPathToTargetBookLocation").child(cell_index+"").child("0").getValue();
                        Long y = (Long) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("cellByCellPathToTargetBookLocation").child(cell_index+"").child("1").getValue();

                        ordered_cells.add(new Vec((float)x.intValue(), (float)y.intValue()));
                        cell_index++;

                    }

                    PickRoute route = new PickRoute();

                    route.setTargetBook(targetBook);
                    route.setOrderedCells(ordered_cells);
//                    Log.d(TAG, "bookrow: " + bookRow.intValue());
//
//                    Log.d(TAG, "author: " + route.getTargetBook().getAuthor());
//
//                    Log.d(TAG, "cells: " + route.getOrderedCells());
                    currentPickPath.addRoute(route);
                }

                currentLocation.setCell( cellRow.intValue(), cellCol.intValue());
                currentLocation.setDisplacement(new Vec(vecx.floatValue(), vecy.floatValue()));

                mClient.onLocationUpdate(currentLocation);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        //POOJA -> populate PickPath currentPickPath
        // ..add code
        //Book book new Book(...);
        //PickRoute route = new PickRoute();
        //route.setTargetBook(book);
        //currentPickPath.add(route);

    }




    //BLUETOOTH

    //public void shutdown(){
    //    bluetooth.disconnect();
    //}


    public void onMessageReceived(Decoder.MSG_TAG msgTag, String msgString) {
        switch (msgTag){
            case MAP:
                Log.d(TAG, "Received the Warehouse Map.");
                //mClient.startExperiment(false);
                break;
            case LOC:
                Log.d(TAG, "Received a new location. "+msgString);
                currentLocation = Decoder.decodeWarehouseLocation(msgString);
                //mClient.onLocationUpdate(Decoder.decodeWarehouseLocation(msgString));
                break;
        }
    }
    // update some value in Firebase after user tap. TODO:change the parameters
    public void confirmPickedFirebase(){
        //Log.d(TAG,"update firebase meow");
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/row", 42);
        childUpdates.put("/col", 42);
        mPostReference.updateChildren(childUpdates);
    }
    public void onConnected(){mClient.onConnected();}
    public void onConnectionLost(){
        mClient.onConnectionLost();
    }

    public PickRoute getNextPickRoute(){return currentPickPath.getNextRoute();}
}
