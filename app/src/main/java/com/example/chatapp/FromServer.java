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
import java.util.List;

public class FromServer {       // Update the dialogue by JSON parser for the response of server
    public static List<MainActivity.Message> updateDialogue(InputStream in, List<MainActivity.Message> dialogue) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginArray();                   // JSON parser (the format of Json is an Array of messages)
            while (reader.hasNext()) {
                dialogue.add(readMessage(reader)); // Update "dialogue" stored in client end from new messages from server
            }
            reader.endArray();
            return dialogue;
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
    public static void updateMessage(final List<MainActivity.Message> dialogue, final long begin_time){
        Thread thread = new Thread(new Runnable() {                                    // Execute by a independent thread
            @Override
            public void run() {
                try {
                    String host = "45.19.61.246:5000";  //IP address of my server
                    Uri uri = new Uri.Builder()
                            .scheme("http")
                            .encodedAuthority(host)
                            .path("chatUpdate")
                            .appendQueryParameter("begin", Long.toString(begin_time))  // Tell server to update from when
                            .build();

                    URL url = new URL(uri.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Setup a connection
                    InputStream in = new BufferedInputStream(conn.getInputStream());    // Capture the response

                    // Read the response and update data
                    List<MainActivity.Message> message = updateDialogue(in, dialogue);

                    in.close();
                    conn.disconnect();
                }catch(Exception e){Log.e("chatUpdate", e.toString());}
            }
        });
        thread.start();
    }
}
