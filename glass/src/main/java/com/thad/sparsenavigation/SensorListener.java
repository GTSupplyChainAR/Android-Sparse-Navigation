package com.thad.sparsenavigation;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.glass.widget.CardScrollView;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by theo on 3/25/18.
 */

public class SensorListener implements SensorEventListener {
    private static final String TAG = "|SensorListener|";

    private GlassClient client;

    private SensorManager mSensorManager;

    private float heading = 0f;


    public SensorListener(GlassClient client){
        // initialize your android device sensor capabilities
        this.client = client;
        mSensorManager = (SensorManager) this.client.getContext().getSystemService(SENSOR_SERVICE);
        resume();
    }

    public void resume() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                                     SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        //Log.d(TAG, "Heading: " + Float.toString(degree) + " degrees");
        heading = degree;
        client.onSensorUpdate(degree);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public float getHeading(){return heading;}
}
