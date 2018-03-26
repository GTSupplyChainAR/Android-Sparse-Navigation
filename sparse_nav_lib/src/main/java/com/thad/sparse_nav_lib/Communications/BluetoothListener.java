package com.thad.sparse_nav_lib.Communications;

/**
 * Created by theo on 3/13/18.
 */

public interface BluetoothListener {
    enum CONN_STATES {CONNECTED, DISCONNECTED, CONNECTING}

    void onThreadConnected();
    void onThreadDisconnect();
    void onBytesReceived(byte[] bytes);
}
