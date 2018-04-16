package com.thad.sparsenavigation.Graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import com.thad.sparse_nav_lib.Utils.Vec;
import com.thad.sparse_nav_lib.Utils.Vec3D;
import com.thad.sparsenavigation.R;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.thad.sparsenavigation.Graphics.GraphicsUtils.getByteBuffer;

/**
 * Created by theo on 4/5/18.
 */

public class NavArrow3D {
    private static final String TAG = "|NavArrow3D|";
    private Context context;
    private Vec3D pos;

    private int mProgram;
    private int mPositionHandle, mMVPMatrixHandle,
            mTextureHandle, mTextureCoordsHandle, mTextureUniformHandle;

    private FloatBuffer vertexBuffer, textureBuffer;
    private ShortBuffer indexBuffer;

    private boolean meshGenerated;
    private final float width = 1, elevation = 0.03f;

    private float[] vertices;//[] = {
            //-size/2,  size/2, elevation,   // top left
            //-size/2, -size/2, elevation,   // bottom left
            //size/2, -size/2, elevation,   // bottom right
            //size/2,  size/2, elevation }; // top right
    private float[] tex_coords;//[] = {
    //        0.0f, 1.0f,
    //        1.0f, 1.0f,
    //        1.0f, 0.0f,
    //        0.0f, 0.0f
    //};
    private final short indices[] = { 0, 1, 2, 1, 3, 2 };

    public NavArrow3D(Context context) {
        this.context = context;
        meshGenerated = false;
    }

    public void createArrow(Vec3D start, Vec3D end){
        Vec3D dir = end.sub(start).normalize();
        float length = start.distance(end);
        Vec3D perp = new Vec3D(-dir.y, dir.x, dir.z);

        Vec3D[] p = new Vec3D[4];
        p[0] = start.add(perp.mult(width/2));
        p[1] = start.add(perp.mult(-width/2));
        p[2] = end.add(perp.mult(width/2));
        p[3] = end.add(perp.mult(-width/2));
        vertices = new float[]{
            (float)p[0].x, (float)p[0].y, elevation,
            (float)p[1].x, (float)p[1].y, elevation,
            (float)p[2].x, (float)p[2].y, elevation,
            (float)p[3].x, (float)p[3].y, elevation
        };

        float texture_coord = length;
        tex_coords = new float[]{
            0.0f, texture_coord,
            1.0f, texture_coord,
            0.0f, 0.0f,
            1.0f, 0.0f
        };

        generateMesh();
    }

    private void generateMesh(){
        meshGenerated = true;

        vertexBuffer = getByteBuffer(vertices);
        indexBuffer = getByteBuffer(indices);
        textureBuffer = getByteBuffer(tex_coords);
        bindShaders();

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.nav_arrow_body);
        mTextureHandle = GraphicsUtils.loadTexture(bmp);
        bmp.recycle();

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mTextureCoordsHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
    }

    private void bindShaders(){
        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                Shaders.TEXTURE_VERTEX_SHADER);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                Shaders.TEXTURE_FRAGMENT_SHADER);

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        if(!meshGenerated) {
            Log.e(TAG, "Attempted to draw arrow but mesh not generated.");
            return;
        }

        GLES20.glUseProgram(mProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        // Prepare the triangle coordinate data
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(mTextureCoordsHandle, 2, GLES20.GL_FLOAT,
                false, 0, textureBuffer);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordsHandle);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordsHandle);
    }

    public void setPosition(Vec3D pos){this.pos = pos;}
    public Vec3D getPosition(){return pos;}
}
