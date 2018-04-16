package com.thad.sparsenavigation.Graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.thad.sparsenavigation.UI.UserInterfaceHandler;

/**
 * Created by theo on 4/3/18.
 */

public class GraphicsGLView extends GLSurfaceView {

    private UserInterfaceHandler mUI;
    private final GLRenderer mRenderer;

    public GraphicsGLView(UserInterfaceHandler mUI) {
        super(mUI.getContext());
        this.mUI = mUI;

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setEGLConfigChooser(new GlassGLConfiguration());

        mRenderer = mUI.getRenderer();//new GLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setHeading(float heading){
        mRenderer.setHeading(heading);
    }
    public void addHeading(float heading) {mRenderer.addHeading(heading);}
}
