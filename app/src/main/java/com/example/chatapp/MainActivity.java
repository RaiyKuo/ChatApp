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

public class MainActivity extends AppCompatActivity {

    public static class Message {                                                    // Message storage structure
        String text, user;
        public Message(String user, String text){                             // Constructor for new dialogue
            this.text = text;
            this.user = user;
        }
    }

    private ArrayList<Message> dialogue = new ArrayList<>();                    // The dialogue data list
    public String message_out;                                                 // The dialogue sent from cellphone

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
                        message_out = mEdit.getText().toString();               // Read user input
                        mEdit.getText().clear();                                // Clear input box after pressing "send" button

                        ToServer.sendPost(message_out);                         // Send dialogue to server
                        dialogue.add(new Message("me", message_out));       // Record sent dialogue

                        FromServer.updateMessage();                             // Update whole dialogue from server

                        recyclerView.setAdapter(new MessageListAdapter(dialogue)); // Display messages on the screen
                    }
                });
    }

    // JSON parser for the response of server
    public static List<Message> updateDialogue(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public static List<Message> readMessagesArray(JsonReader reader) throws IOException {
        List<Message> messages = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public static Message readMessage(JsonReader reader) throws IOException {
        String user = null, text = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("user")) {
                user = reader.nextString();
            } else if (name.equals("text")) {
                text = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Message(user, text);
    }
}
