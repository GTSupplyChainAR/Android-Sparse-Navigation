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
import com.thad.sparsenavigation.Communications.ClientBluetooth;
import com.thad.sparsenavigation.GlassClient;

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
    //private PickPath currentPickPath;

    //private ClientBluetooth bluetooth;
    private DatabaseReference mPostReference;


    public CommunicationHandler(GlassClient client){
        mClient = client;
        currentLocation = new WarehouseLocation();
        //bluetooth = new ClientBluetooth(this);
        //bluetooth.setAddress(Prefs.PHONE_ADDRESS, Prefs.GLASS_UUID);

        //bluetooth.connect();

        // Add value event listener to the post
        // [START post_value_event_listener]
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("warehouseLayout").child("currentLocation");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Long cellRow = (Long) dataSnapshot.child("row").getValue();
                Long cellCol = (Long) dataSnapshot.child("col").getValue();
                Log.d(TAG, "row: " +dataSnapshot.child("row"));
                Log.d(TAG, "VectorX: " +dataSnapshot.child("vecx"));
                String vecX = (String) dataSnapshot.child("vecx").getValue();
                String vecY = (String) dataSnapshot.child("vecy").getValue();
                Float vecx = Float.parseFloat(vecX);
                Float vecy = Float.parseFloat(vecY);
                currentLocation.setCell( cellRow.intValue(), cellCol.intValue());
                currentLocation.setDisplacement(new Vec(vecx.floatValue(), vecy.floatValue()
                ));

                Log.d(TAG, "Location: " +currentLocation.toString());
                // [END_EXCLUDE]
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

    }

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

    public void onConnected(){mClient.onConnected();}
    public void onConnectionLost(){
        mClient.onConnectionLost();
    }

    //public PickPath getNextPickPath(){ return new PickPath();}
    public PickRoute getNextPickRoute(){return new PickRoute();}
}
