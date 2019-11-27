package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.max;

public class MainActivity extends AppCompatActivity {

    public static class Message {                                       // Message storage structure
        String timestamp, user, text;
        public Message(long time ,String user, String text){            // Constructor for new message
            this.timestamp = Long.toString(time);
            this.user = user;
            this.text = text;
        }
    }

    private ArrayList<Message> dialogue = new ArrayList<>();            // The dialogue data list
    public String message_out;                                          // The message sent from cellphone
    final static String my_identity = "Computer";                       // Your ID
    static long latest_message_time = (long) 0.0;                       // For update message begin position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.message_list);      // RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // Set layout for RecyclerView

        Button mButton = (Button)findViewById(R.id.button_send);                // Send button
        final EditText mEdit  = (EditText)findViewById(R.id.edittext_chatbox);  // input box for user to type dialogue


        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        message_out = mEdit.getText().toString();                 // Read user input
                        mEdit.getText().clear();                                  // Clear input box after pressing "send" button

                        long now = System.currentTimeMillis()/1000;
                        ToServer.sendPost(message_out, now);                           // Send dialogue to server
                        //dialogue.add(new Message(now, my_identity, message_out)); // Record sent dialogue
                        //latest_message_time = max(now, latest_message_time);
                    }
                });
        FromServer.updateMessage(dialogue);                       // Update whole dialogue from server
        recyclerView.setAdapter(new MessageListAdapter(dialogue)); // Display messages on the screen
    }

    // Update the dialogue by JSON parser for the response of server
    public static List<Message> updateDialogue(InputStream in, List<Message> dialogue) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                dialogue.add(readMessage(reader));

            }
            reader.endArray();
            return dialogue;
        } finally {
            reader.close();
        }
    }

    public static Message readMessage(JsonReader reader) throws IOException {
        String user = null, text = null;
        long time = (long) 0.0;

        reader.beginObject();
        while (reader.hasNext()) {
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

        latest_message_time = max(time, latest_message_time); // Update time of latest message
        return new Message(time, user, text);
    }
}
