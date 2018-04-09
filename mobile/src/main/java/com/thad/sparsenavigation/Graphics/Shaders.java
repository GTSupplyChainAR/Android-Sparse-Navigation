package com.thad.sparsenavigation.Graphics;

/**
 * Created by theo on 4/4/18.
 */

public class Shaders {

    public static final String COLOR_VERTEX_SHADER =
            "attribute vec4 a_Position;" +
                    "attribute vec4 a_Color;" +

                    "varying vec4 v_Color;"+

                    "void main() {" +
                    "  v_Color = a_Color;" +
                    "  gl_Position = a_Position;" +
                    "}";

    public static final String COLOR_FRAGMENT_SHADER =
            "precision mediump float;"
            + "varying vec4 v_Color;"
            + "void main() {"
            + "  gl_FragColor = v_Color;"
            + "}";

    public static final String TEXTURE_VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;"
            + "attribute vec4 a_Position;"
            + "attribute vec2 a_TexCoordinate;"

            + "varying vec2 v_TexCoordinate;"

            + "void main(){"
            + "   v_TexCoordinate = a_TexCoordinate;"
            + "   gl_Position = uMVPMatrix * a_Position;"
            + "}";

    public static final String TEXTURE_FRAGMENT_SHADER =
            "precision mediump float;"
                    + "uniform sampler2D u_Texture;"

                    + "varying vec2 v_TexCoordinate;"

                    + "void main(){"
                    + "   gl_FragColor = texture2D(u_Texture, v_TexCoordinate);"
                    + "}";

}
