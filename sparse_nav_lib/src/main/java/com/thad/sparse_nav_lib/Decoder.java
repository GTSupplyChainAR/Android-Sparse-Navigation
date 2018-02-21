package com.thad.sparse_nav_lib;

import android.util.Log;

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

    public static final int MSG_SIZE_INFO_LENGTH = 10;
    public static final int MSG_TAG_LENGTH = 10;
    public static final String STRING_ENCODE = "UTF-8";

    //These TAGs identify the type of data held by the message
    public static enum MSG_TAGS{
        MAP, LOC
    }

    //Encode the Message.
    //Adds the headers and creates a byte array
    public static byte[] getMsgBytes(MSG_TAGS tag, String data){
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
        ByteBuffer dbuf = ByteBuffer.allocate(MSG_SIZE_INFO_LENGTH);
        dbuf.putInt(n);
        return dbuf.array();
    }
    private static byte[] getHeaderBytes(MSG_TAGS tag){
        String str = tag.toString();
        byte[] bytes = new byte[MSG_TAG_LENGTH];

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


    //Decode JSON String that carries all the information about the Map
    public static WarehouseMap decodeMap(String mapJSON){
        //PLACEHOLDER
        return new WarehouseMap(10,16);
    }
}
