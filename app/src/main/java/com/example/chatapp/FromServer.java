package com.example.chatapp;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FromServer {



    // Obtain message JSON object via HTTP Get method

    public static void updateMessage(){
        Thread thread = new Thread(new Runnable() {                                    // Execute by a independent thread
            @Override
            public void run() {
                try {
                    URL url = new URL("http://45.19.61.246:5000/chatUpdate");  // URL for the target server
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Setup a connection
                    InputStream in = new BufferedInputStream(conn.getInputStream());

                    List<MainActivity.Message> message = MainActivity.updateDialogue(in);

                    Log.v("chatUpdate", message.get(0).user + message.get(0).text);  // Show first mesage on the log

                    in.close();
                    conn.disconnect();                            // Disconnect
                }catch(Exception e){Log.e("chatUpdate", e.toString());}
            }
        });
        thread.start();
    }



}
