package com.example.chatapp;

import android.util.Log;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ToServer {      // Create JSON and sending via HTTP POST method
    public static void sendPost(final String message_out, final long now){
        Thread thread = new Thread(new Runnable() {                                    // Execute by a independent thread
            @Override
            public void run() {
                try {
                    URL url = new URL("http://45.19.61.246:5000/chatSend");      // URL for the target server
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Setup a connection
                    conn.setRequestMethod("POST");                                     // By HTTP POST method
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");              // Designated data type is JSON
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject json = new JSONObject();                 // Create a JSON object
                    json.put("time", now);                       // Input data to JSON
                    json.put("user", MainActivity.my_identity);  // Input data to JSON
                    json.put("text", message_out);

                    DataOutputStream outstream = new DataOutputStream(conn.getOutputStream());
                    outstream.writeBytes(json.toString());              // Send to server
                    outstream.flush();
                    outstream.close();

                    conn.getResponseMessage();
                    conn.disconnect();
                }catch(Exception e){
                    Log.e("sendPost", e.toString());
                }
            }
        });
        thread.start();
    }
}
