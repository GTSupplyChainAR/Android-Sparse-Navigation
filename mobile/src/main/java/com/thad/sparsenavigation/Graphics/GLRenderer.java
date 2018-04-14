package com.thad.sparsenavigation.Graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.thad.sparse_nav_lib.Utils.Vec;
import com.thad.sparse_nav_lib.Utils.Vec3D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by theo on 4/3/18.
 */

public class GLRenderer implements Renderer {
    private static final String TAG = "|GLRenderer|";

    private Context context;

    private WarehouseMap3D warehouseMap3D;
    private Vec3D dir, pos;
    private float behindDistance = 15, upDistance = 15;
    private float th = 0;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public GLRenderer(Context context){
        this.context = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        warehouseMap3D = new WarehouseMap3D(context);

        dir = new Vec3D(0, -1, 0);
        pos = new Vec3D(0, 0, 0);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 60);
    }

    public void onDrawFrame(GL10 unused) {
        th += 0.001;
        dir.x = Math.cos(th);
        dir.y = Math.sin(th);

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Vec3D cameraXY = dir.reverse().normalize().mult(behindDistance);
        Vec3D cameraZ = new Vec3D(0,0,1).mult(upDistance);
        Vec3D cameraPos = pos.add(cameraXY.add(cameraZ));
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                (float)cameraPos.x, (float)cameraPos.y, (float)cameraPos.z,
                (float)pos.x, (float)pos.y, 0f,
                0f, 0.0f, 1.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        warehouseMap3D.draw(mMVPMatrix);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
