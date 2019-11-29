package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static class Message {                                       // Message storage structure
        long timestamp;
        String user, text;
        public Message(long time ,String user, String text){            // Constructor for new message
            this.timestamp = time;
            this.user = user;
            this.text = text;
        }
    }

    public static ArrayList<Message> dialogue = new ArrayList<>();      // The dialogue data list
    final static String my_identity = "Computer";                       // Your ID
    static int update_counter = 0;                                    //  Counter for many updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.message_list);     // RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Set layout for RecyclerView

        Button mButton = findViewById(R.id.button_send);                       // Link to Send button
        final EditText mEdit  = findViewById(R.id.edittext_chatbox);           // input box for user to type dialogue

        mButton.setOnClickListener(                                            // Once click send button
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        ToServer.sendPost(mEdit.getText().toString()); // Send the text of input box to server
                        mEdit.getText().clear();                       // Clear input box after pressing "send" button
                    }
                });
        AutoRefresh(recyclerView, 2500);  // AutoRefresh the incoming new message and update display
    }

    // AutoRefresh function
    private final Handler handler = new Handler();
    private Parcelable recyclerViewState;
    private void AutoRefresh(final RecyclerView recyclerView, final int cycletime) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FromServer.updateMessage();        // Update data from server

                MessageListAdapter adapter = new MessageListAdapter(dialogue);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState(); // Save current display position
                recyclerView.setAdapter(adapter);  // Update the scree display

                if (update_counter == 0) {         // Keep the same focus position
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                } else {                           // Scroll to bottom after every update
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }

                AutoRefresh(recyclerView, cycletime);
            }
        }, cycletime);                                                     // Refresh every [cycletime] millisecond
    }
}
