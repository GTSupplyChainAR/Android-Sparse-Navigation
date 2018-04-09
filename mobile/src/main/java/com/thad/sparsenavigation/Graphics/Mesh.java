package com.thad.sparsenavigation.Graphics;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by theo on 4/4/18.
 */

public class Mesh {
    // Our vertex buffer.
    private FloatBuffer verticesBuffer = null;

    // Our index buffer.
    private ShortBuffer indicesBuffer = null;

    // The number of indices.
    private int numOfIndices = -1;

    // Flat Color
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    // Smooth Colors
    private FloatBuffer colorBuffer = null;

    public void draw(GLES20 gl) {
        /*
        // Counter-clockwise winding.
        gl.glFrontFace(GLES20.GL_CCW);
        // Enable face culling.
        gl.glEnable(GLES20.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GLES20.GL_BACK);
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GLES20.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GLES20.GL_FLOAT, 0, verticesBuffer);
        // Set flat color
        gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
        // Smooth color
        if ( colorBuffer != null ) {
            // Enable the color array buffer to be used during rendering.
            gl.glEnableClientState(GLES20.GL_COLOR_ARRAY);
            // Point out the where the color buffer is.
            gl.glColorPointer(4, GLES20.GL_FLOAT, 0, colorBuffer);
        }
        gl.glDrawElements(GLES20.GL_TRIANGLES, numOfIndices,
                GLES20.GL_UNSIGNED_SHORT, indicesBuffer);
        // Disable the vertices buffer.
        gl.glDisableClientState(GLES20.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GLES20.GL_CULL_FACE);*/
    }
}
