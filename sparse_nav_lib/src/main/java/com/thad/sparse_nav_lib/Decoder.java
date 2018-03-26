package com.thad.sparse_nav_lib;

import android.util.Log;

import com.thad.sparse_nav_lib.Utils.Vec;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * The Decoder class contains all static functions that decode messages.
 * Called both by Glass classes and Mobile.
 *
 * Every message transmitted between Mobile and Glass has the following structure:
 * MSG = SIZE_INFO + TAG + DATA
 * SIZE_INFO -> How many bytes the message includes.
 * TAG -> What type of information it includes.
 */

public class Decoder {
    private static final String TAG = "|Decoder|";

    public static final int HEADER_MSG_LENGTH_SIZE = 10;
    public static final int HEADER_MSG_TAG_SIZE = 10;
    public static final String STRING_ENCODE = "UTF-8";

    //These TAGs identify the type of data held by the message
    public static enum MSG_TAG{
        MAP, LOC
    }

    public static String encodeWarehouseLocation(WarehouseLocation loc){
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("row", loc.getCell()[0]);
            jObj.put("col", loc.getCell()[1]);
            jObj.put("displacementX", loc.getDisplacement().x);
            jObj.put("displacementY", loc.getDisplacement().y);
        }catch (JSONException e){
            Log.e(TAG, "Failed to encode Warehouse Location.");
        }
        return jObj.toString();
    }

    public static WarehouseLocation decodeWarehouseLocation(String loc_encoded){
        WarehouseLocation loc = new WarehouseLocation();
        try{
            JSONObject jObj = new JSONObject(loc_encoded);
            loc.setCell(jObj.getInt("row"), jObj.getInt("col"));
            loc.setDisplacement(new Vec(jObj.getDouble("displacementX"),
                                        jObj.getDouble("displacementY")));
        }catch (JSONException e){
            Log.e(TAG, "Failed to decode Warehouse Location.");
        }
        return loc;
    }

    public static int decodeMSGlength(byte[] buffer){
        byte[] header = new byte[Decoder.HEADER_MSG_LENGTH_SIZE];
        System.arraycopy(buffer, 0, header, 0, Decoder.HEADER_MSG_LENGTH_SIZE);
        ByteBuffer wrapped = ByteBuffer.wrap(header);
        return wrapped.getInt();
    }

    public static MSG_TAG decodeMSGtag(byte[] msgBytes){
        byte[] tagBytes = new byte[HEADER_MSG_TAG_SIZE];
        System.arraycopy(msgBytes, 0, tagBytes, 0, HEADER_MSG_TAG_SIZE);

        String strTag = new String(tagBytes);

        int count = 0;
        for(int i = 0 ; i < strTag.length() ; i++){
            if(Character.getNumericValue(strTag.charAt(i)) == -1)
                break;
            count ++;
        }
        strTag = strTag.substring(0, count);

        return MSG_TAG.valueOf(strTag);
    }

    public static String decodeMSGtoString(byte[] msg){
        int n = msg.length - Decoder.HEADER_MSG_TAG_SIZE;
        byte[] dataWithoutTag = new byte[n];
        System.arraycopy(msg, Decoder.HEADER_MSG_TAG_SIZE, dataWithoutTag, 0, n);
        return new String(dataWithoutTag);
    }

    public static byte[] encodeMSG(MSG_TAG tag, String data){
        byte[] data_bytes;
        try{
            data_bytes = data.getBytes(STRING_ENCODE);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Log.e(TAG, "Unsupported Encoding Exception. Tag = "+tag.toString()+", Msg = "+data);
            return null;
        }
        byte[] tag_bytes = getHeaderBytes(tag);
        int n = data_bytes.length + tag_bytes.length;
        byte[] msg_size_bytes = getHeaderBytes(n);

        byte[] msg = new byte[n+msg_size_bytes.length];
        System.arraycopy(msg_size_bytes, 0, msg, 0, msg_size_bytes.length);
        System.arraycopy(tag_bytes, 0, msg, msg_size_bytes.length, tag_bytes.length);
        System.arraycopy(data_bytes, 0, msg, msg_size_bytes.length+tag_bytes.length, data_bytes.length);

        return msg;
    }

    //Encode the headers into byte arrays
    private static byte[] getHeaderBytes(int n){
        ByteBuffer dbuf = ByteBuffer.allocate(HEADER_MSG_LENGTH_SIZE);
        dbuf.putInt(n);
        return dbuf.array();
    }
    private static byte[] getHeaderBytes(MSG_TAG tag){
        String str = tag.toString();
        byte[] bytes = new byte[HEADER_MSG_TAG_SIZE];

        try {
            byte[] raw_bytes = str.getBytes(STRING_ENCODE);

            if(raw_bytes.length <= bytes.length)
                System.arraycopy(raw_bytes, 0, bytes, 0, raw_bytes.length);
            else
                Log.e(TAG, "Tag larger than allowed.");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "Unsupported Encoding Exception. Tag = "+str);
        }

        return bytes;
    }
}
