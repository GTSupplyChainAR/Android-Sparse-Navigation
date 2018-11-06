package com.thad.sparsenavigation.Communications;

import android.content.Context;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Book targetBook = new Book();
    FirebaseDatabase database;
    JSONObject obj;
    JSONArray picklists;
    //private ClientBluetooth bluetooth;
    private DatabaseReference mPostReference;

    public CommunicationHandler(GlassClient client) {
        mClient = client;
        currentLocation = new WarehouseLocation();
        currentPickPath = new PickPath();
        //bluetooth = new ClientBluetooth(this);
        //bluetooth.setAddress(Prefs.PHONE_ADDRESS, Prefs.GLASS_UUID);
        //bluetooth.connect();

        // Add value event listener to the post
        // [START post_value_event_listener]


//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        mPostReference = FirebaseDatabase.getInstance().getReference();

//                .child("warehouseLayout").child("currentLocation");


//            public int getBook(String pick_path_index, int book_index){
//
//                if dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("cellByCellPathToTargetBookLocation").child(cell_index+"").child("0").getValue() != null)
//            }


//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Long cellRow = (Long) dataSnapshot.child("warehouseLayout").child("currentLocation").child("row").getValue();
//                Long cellCol = (Long) dataSnapshot.child("warehouseLayout").child("currentLocation").child("col").getValue();
//                Log.d(TAG, "row: " + dataSnapshot.child("warehouseLayout").child("currentLocation").child("row"));
//                Log.d(TAG, "VectorX: " + dataSnapshot.child("warehouseLayout").child("currentLocation").child("vecx"));
//                String vecX = (String) dataSnapshot.child("warehouseLayout").child("currentLocation").child("vecx").getValue();
//                String vecY = (String) dataSnapshot.child("warehouseLayout").child("currentLocation").child("vecy").getValue();
//                Float vecx = Float.parseFloat(vecX);
//                Float vecy = Float.parseFloat(vecY);
//                int pick_path_index = 0;
//                int book_index = 0;
//
//                for (book_index = 0; book_index < 11; book_index++) {
//                    Book targetBook = new Book();
//                    ArrayList<Vec> ordered_cells = new ArrayList<Vec>();
//                    int cell_index = 0;
//                    String author = (String) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("targetBookAndTargetBookLocation").child("book").child("author").getValue();
//                    String tag = (String) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("targetBookAndTargetBookLocation").child("book").child("tag").getValue();
//                    String title = (String) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("targetBookAndTargetBookLocation").child("book").child("title").getValue();
//                    Long bookRow = (Long) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("targetBookAndTargetBookLocation").child("location").child("0").getValue();
//                    Long bookCol = (Long) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("targetBookAndTargetBookLocation").child("location").child("1").getValue();
//                    if (book_index != 10) {
//                        targetBook.setAuthor(author);
//                        targetBook.setLocationTag(tag);
//                        targetBook.setTitle(title);
//                        targetBook.setCell(bookRow.intValue(), bookCol.intValue());
//                    }
//
//                    while ((Long) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("cellByCellPathToTargetBookLocation").child(cell_index + "").child("0").getValue() != null) {
//                        Long x = (Long) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("cellByCellPathToTargetBookLocation").child(cell_index + "").child("0").getValue();
//                        Long y = (Long) dataSnapshot.child("pickPaths").child(pick_path_index + "").child("pickPathInformation").child("orderedPickPath").child(book_index + "").child("cellByCellPathToTargetBookLocation").child(cell_index + "").child("1").getValue();
//
//                        ordered_cells.add(new Vec((float) x.intValue(), (float) y.intValue()));
//                        cell_index++;
//
//                    }
//
//                    PickRoute route = new PickRoute();
//
//                    route.setTargetBook(targetBook);
//                    route.setOrderedCells(ordered_cells);
////                    Log.d(TAG, "bookrow: " + bookRow.intValue());
////
////                    Log.d(TAG, "author: " + route.getTargetBook().getAuthor());
////
////                    Log.d(TAG, "cells: " + route.getOrderedCells());
//                    currentPickPath.addRoute(route);
//                }
//
//                currentLocation.setCell(cellRow.intValue(), cellCol.intValue());
//                currentLocation.setDisplacement(new Vec(vecx.floatValue(), vecy.floatValue()));
//
//                //mClient.onLocationUpdate(currentLocation);
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                // [START_EXCLUDE]
//                // [END_EXCLUDE]
//            }
//
//
//        };
//        mPostReference.addValueEventListener(postListener);
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

    public void readData(Context context) {
        String JSON_text = loadJSONFromAsset(context);
        //Log.d(TAG, JSON_text);
        try {
            JSONObject obj = new JSONObject(JSON_text);
            JSONArray picklists = obj.getJSONArray("pickPaths");
            Log.d(TAG, "ohno");
        } catch (JSONException e) {
            Log.d(TAG, "ohno");
        }

    }

    public String loadJSONFromAsset(Context context) {

        String json = null;
        try {

            Log.d("entry", "loadJSONFromAsset: " + json);

            InputStream is = context.getAssets().open("picklists.json");

            StringBuilder buf = new StringBuilder();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            json = buf.toString();

            // Log.d("json", "loadJSONFromAsset: "+json);

        } catch (IOException ex) {
            Log.d("error", "loadJSONFromAsset: " + ex.toString());
            ex.printStackTrace();
            return null;
        }
        return json;
    }


//    public String loadJSONFromAsset(Context context) {
//        String json = null;
//        try {
//            InputStream is = context.getAssets().open("picklists.json");
//
//            int size = is.available();
//
//            byte[] buffer = new byte[size];
//
//            is.read(buffer);
//
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        Log.d(TAG, json);
//        return json;
//
//    }


    public int getBook(int pick_path_index, int book_index, Context context) {
        Log.d(TAG, "GetBookCalled");

            //Log.d(TAG, JSON_text);
            try {
                String JSON_text = loadJSONFromAsset(context);
                JSONObject obj = new JSONObject(JSON_text);
                JSONArray picklists = obj.getJSONArray("pickPaths");
        JSONObject current_book = picklists.getJSONObject(pick_path_index - 1).
                getJSONObject("pickPathInformation").getJSONArray("orderedBooksAndLocations").
               getJSONObject(book_index);
        String author = current_book.getJSONObject("book").getString("author");
            if (author== null) return 0;
        String tag = current_book.getJSONObject("book").getString("tag");
            Log.d(TAG, tag);
        String title = current_book.getJSONObject("book").getString("title");
        Long bookRow = new Long(current_book.getJSONArray("location").getInt(0));
        Long bookCol = new Long(current_book.getJSONArray("location").getInt(1));
        targetBook.setAuthor(author);
        targetBook.setLocationTag(tag);
        targetBook.setTitle(title);
        targetBook.setCell(bookRow.intValue(), bookCol.intValue());

                PickRoute route = new PickRoute();

                route.setTargetBook(targetBook);

                currentPickPath.addRoute(route);
        }
        catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        };


//        String author = (String) database.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("book").child("author").getValue();
//        String tag = (String) base.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("book").child("tag").getValue();
//        String title = (String) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("book").child("title").getValue();
//        Long bookRow = (Long) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("location").child("0").getValue();
//        Long bookCol = (Long) dataSnapshot.child("pickPaths").child(pick_path_index+"").child("pickPathInformation").child("orderedPickPath").child(book_index+"").child("targetBookAndTargetBookLocation").child("location").child("1").getValue();
//
//        targetBook.setAuthor(author);
//        targetBook.setLocationTag(tag);
//        targetBook.setTitle(title);
//        targetBook.setCell(bookRow.intValue(), bookCol.intValue());
//
//        PickRoute route = new PickRoute();
//
//        route.setTargetBook(targetBook);
//        currentPickPath.addRoute(route);

      return 1;


    }

    public String getBookTag(){
        return targetBook.getLocationTag();
    }

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
        //mPostReference.updateChildren(childUpdates); //not posting to firebase
    }
    public void onConnected(){mClient.onConnected();}
    public void onConnectionLost(){
        mClient.onConnectionLost();
    }

    public PickRoute getNextPickRoute(){return currentPickPath.getNextRoute();}

    public void postToApi(final String userId,final String phase, final String time, final String pathId, final String bookTag) {



        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    // Your implementation

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("https://eyegaze4605api.herokuapp.com/api/userData");

                    try {
                        // Add your data
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("userId",userId));
                        nameValuePairs.add(new BasicNameValuePair("phase", phase));
                        nameValuePairs.add(new BasicNameValuePair("time", time));
                        nameValuePairs.add(new BasicNameValuePair("pathId", pathId));
                        nameValuePairs.add(new BasicNameValuePair("bookTag", bookTag));
                        nameValuePairs.add(new BasicNameValuePair("device", "19"));
                        nameValuePairs.add(new BasicNameValuePair("viewPosition", "0"));

                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        // Execute HTTP Post Request
                        HttpResponse response = httpclient.execute(httppost);
                        int responseCode = response.getStatusLine().getStatusCode();
                        Log.d(TAG, Integer.toString(responseCode));
                        switch(responseCode) {

                            case 422:
                                HttpEntity entity = response.getEntity();
                                if(entity != null) {
                                    String responseBody = EntityUtils.toString(entity);
                                    Log.d(TAG, responseBody);
                                }
                                break;
                        }



                    } catch (ClientProtocolException e) {
                        // TODO Auto-generated catch block
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                    }

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();




    }

}


