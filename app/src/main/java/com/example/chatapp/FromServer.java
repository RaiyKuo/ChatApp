package com.example.chatapp;

import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FromServer {       // Update the dialogue by JSON parser for the response of server
    public static void updateDialogue(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            MainActivity.update_counter = 0;       // Count how many messages were updated in this run
            reader.beginArray();                   // JSON parser (the format of Json is an Array of messages)
            while (reader.hasNext()) {
                MainActivity.dialogue.add(readMessage(reader)); // Update dat from server
                MainActivity.update_counter += 1;
            }
            reader.endArray();
        } finally {reader.close();}
    }

    public static MainActivity.Message readMessage(JsonReader reader) throws IOException {
        String user = null, text = null;
        long time = (long) 0.0;

        reader.beginObject();
        while (reader.hasNext()) {             // Parse each message in the array
            String name = reader.nextName();
            if (name.equals("user")) {
                user = reader.nextString();
            } else if (name.equals("text")) {
                text = reader.nextString();
            } else if (name.equals("time")) {
                time = reader.nextLong();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new MainActivity.Message(time, user, text);
    }

    // Obtain message JSON object via HTTP Get method
    public static void updateMessage(){
        Thread thread = new Thread(new Runnable() {           // Execute by a independent thread
            @Override
            public void run() {
                try {
                    long latest_message_time = (long) 0.0;    // Tell server to update message begin from
                    if (!MainActivity.dialogue.isEmpty()) {
                        latest_message_time = MainActivity.dialogue.get(MainActivity.dialogue.size() - 1).timestamp;
                    }

                    String host = "45.19.61.246:5000";  // IP address of my server
                    Uri uri = new Uri.Builder()         // Construct "GET" url with query parameters
                            .scheme("http")
                            .encodedAuthority(host)
                            .path("chatUpdate")
                            .appendQueryParameter("begin", Long.toString(latest_message_time))  // Tell server to update from when
                            .build();
                    URL url = new URL(uri.toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Setup a connection
                    InputStream in = new BufferedInputStream(conn.getInputStream());    // Capture the response

                    updateDialogue(in);  // Read the response from server then update data

                    in.close();
                    conn.disconnect();
                }catch(Exception e){Log.e("chatUpdate", e.toString());}
            }
        });
        thread.start();
    }
}
