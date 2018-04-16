package com.thad.sparsenavigation.Graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

import com.thad.sparse_nav_lib.PickPath;
import com.thad.sparse_nav_lib.PickRoute;
import com.thad.sparse_nav_lib.Static.UtilFunctions;
import com.thad.sparse_nav_lib.Utils.Vec;
import com.thad.sparse_nav_lib.Utils.Vec3D;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparsenavigation.GlassClient;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_SRC_ALPHA;

/**
 * Created by theo on 4/3/18.
 */

public class GLRenderer implements Renderer {
    private static final String TAG = "|GLRenderer|";

    private GlassClient client;
    private Context context;

    private WarehouseMap3D warehouseMap3D;
    private Cursor3D cursor3D;
    private ArrayList<NavArrow3D> navArrows3D;
    private Vec3D dir, pos;
    private float behindDistance = 12, upDistance = 15;
    private float th = 0, lastHeading = 0;
    private PickRoute activeRoute;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mMVMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private final float[] mCurrentRotation = new float[16];
    private final float[] mTranslationMatrix = new float[16];


    public GLRenderer(GlassClient client){
        this.context = client.getContext();
        this.client = client;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.d(TAG, "Version: "+GLES20.glGetString(GLES20.GL_VERSION));

        Matrix.setIdentityM(mCurrentRotation, 0);

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        warehouseMap3D = new WarehouseMap3D(context);
        cursor3D = new Cursor3D(context);
        navArrows3D = new ArrayList<NavArrow3D>();

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

    private void populateNavArrows3D(PickRoute route){
        navArrows3D.clear();
        if(route != null && route.getOrderedCells() != null){
            ArrayList<Vec> pathCells = route.getOrderedCells();
            for(int i = 1; i < pathCells.size() ; i++){
                NavArrow3D navArrow3D = new NavArrow3D(context);
                Vec3D start = UtilFunctions.get3DLocation(pathCells.get(i-1));
                Vec3D end = UtilFunctions.get3DLocation(pathCells.get(i));
                navArrow3D.createArrow(start, end);
                navArrows3D.add(navArrow3D);
            }
        }
    }

    public void onDrawFrame(GL10 unused) {
        if(activeRoute == null){
            activeRoute = client.getNextPickRoute();
            if(activeRoute != null)
                populateNavArrows3D(activeRoute);
        }
        //th += 0.0001;
        dir.x = Math.cos(Math.toRadians(lastHeading));
        dir.y = Math.sin(Math.toRadians(lastHeading));

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        Vec3D cameraXY = dir.reverse().normalize().mult(behindDistance);
        Vec3D cameraZ = new Vec3D(0,0,1).mult(upDistance);
        Vec3D cameraPos = pos.add(cameraXY.add(cameraZ));
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                (float)cameraPos.x, (float)cameraPos.y, (float)cameraPos.z,
                (float)pos.x, (float)pos.y, 0f,
                0f, 0.0f, 1.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        warehouseMap3D.draw(mMVPMatrix);

        for(NavArrow3D arrow3D : navArrows3D){
            arrow3D.draw(mMVPMatrix);
        }


        Matrix.setIdentityM(mTranslationMatrix, 0);
        Matrix.translateM(mTranslationMatrix, 0, (float)pos.x, (float)pos.y, 0f);

        // Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);
        Matrix.rotateM(mCurrentRotation, 0, lastHeading, 0.0f, 0.0f, 1.0f);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.multiplyMM(mModelMatrix, 0, mCurrentRotation, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mModelMatrix, 0, mTranslationMatrix, 0, mModelMatrix, 0);
        //System.arraycopy(mCurrentRotation, 0, mModelMatrix, 0, 16);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);

        cursor3D.draw(mMVPMatrix);
    }

    public void setHeading(float heading){
        lastHeading = -heading;
    }
    public void setLocation(WarehouseLocation loc){
        Log.d(TAG, "New Location - "+loc.toString());
        pos = UtilFunctions.get3DLocation(loc);
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
