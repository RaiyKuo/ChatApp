package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public class Message {                                                    // Message storage structure
        String text, user;
        public Message(String user, String text){                             // Constructor for new message
            this.text = text;
            this.user = user;
        }
    }

    private ArrayList<Message> message = new ArrayList<>();                    // The message data list
    public String message_out;                                                 // The message sent from cellphone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.message_list);      // RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   // Set layout for RecyclerView

        Button mButton = (Button)findViewById(R.id.button_send);                // Send button
        final EditText mEdit  = (EditText)findViewById(R.id.edittext_chatbox);  // input box for user to type message

        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        message_out = mEdit.getText().toString();               // Read user input
                        mEdit.getText().clear();                                // Clear input box after pressing "send" button

                        ToServer.sendPost(message_out);                         // Send message to server
                        message.add(new Message("me", message_out));       // Record sent message

                        recyclerView.setAdapter(new MessageListAdapter(message)); // Display messages on the screen
                    }
                });
    }

}
