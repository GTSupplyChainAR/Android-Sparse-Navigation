package com.thad.sparsenavigation.Graphics;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by theo on 4/4/18.
 */

public class GraphicsUtils {
    private static final String TAG = "|GraphicsUtils|";
    private static int textureCount = 1;

    public static int loadTexture(Bitmap bmp) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);
        textureHandle[0] = textureCount++;

        if (textureHandle[0] != 0) {
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        }else{
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    public static FloatBuffer getByteBuffer(float[] data){
        ByteBuffer bb = ByteBuffer.allocateDirect(
                data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = bb.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static ShortBuffer getByteBuffer(short[] data){
        ByteBuffer bb = ByteBuffer.allocateDirect(
                data.length * 2);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = bb.asShortBuffer();
        shortBuffer.put(data);
        shortBuffer.position(0);
        return shortBuffer;
    }
}
