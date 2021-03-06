package mau.restaurantapp.utils.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import mau.restaurantapp.data.AppData;

/**
 * Created by Yoouughurt on 19-12-2016.
 */

public class FirebaseTokenHandlerService extends FirebaseInstanceIdService {
    public static final String instanceID = "instanceID";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString(instanceID, token).apply();
        Log.d("TOKEN", token);
        if (AppData.currentUser != null && AppData.currentUser.isAdmin()) {
            sendTokenToServer(token);
        }
    }

    /**
     * Sends the token to our server with an simple UDP Packet
     * @param token The device-token
     */
    private static void sendTokenToServer(String token) {
        byte[] data = token.getBytes();

        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName("185.15.73.229");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 9000);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Packet send", packet.toString());
    }

    public static void sendCurrentToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(instanceID, "None");
        if (!(token.equals("None"))) {
            AsyncTokenSender sender = new AsyncTokenSender();
            sender.token = token;
            sender.execute();
        }
    }

    private static class AsyncTokenSender extends AsyncTask<Void, Void, Void>
    {
        private String token;

        @Override
        protected Void doInBackground(Void... params) {
            sendTokenToServer(token);
            return null;
        }
    }
}


