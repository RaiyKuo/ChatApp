package com.example.chatapp;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FromServer {

    // Obtain message JSON object via HTTP Get method
    public static void updateMessage(final List<MainActivity.Message> dialogue){
        Thread thread = new Thread(new Runnable() {                                    // Execute by a independent thread
            @Override
            public void run() {
                try {
                    String host = "45.19.61.246:5000";  //IP address of target server
                    Uri uri = new Uri.Builder()
                            .scheme("http")
                            .encodedAuthority(host)
                            .path("chatUpdate")
                            .appendQueryParameter("begin", Long.toString(MainActivity.latest_message_time))
                            .build();

                    URL url = new URL(uri.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Setup a connection
                    InputStream in = new BufferedInputStream(conn.getInputStream());    // Capture the response

                    List<MainActivity.Message> message = MainActivity.updateDialogue(in, dialogue); // Update the dialogue

                    //Log.v("chatUpdate", message.get(0).user + message.get(0).text);  // Show first message on the log
                    in.close();
                    conn.disconnect();                            // Disconnect
                }catch(Exception e){Log.e("chatUpdate", e.toString());}
            }
        });
        thread.start();
    }
}
