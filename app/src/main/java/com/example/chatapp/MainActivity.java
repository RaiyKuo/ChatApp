package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //change start from here

        recyclerView = findViewById(R.id.reyclerview_message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<String> data = new ArrayList<>();

        Button mButton = (Button)findViewById(R.id.button_send);
        final EditText mEdit  = (EditText)findViewById(R.id.edittext_chatbox);

        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String message_out = mEdit.getText().toString();
                        data.add(message_out);
                        mEdit.getText().clear();
                        Log.v("EditText", message_out);
                        adapter = new MessageListAdapter(this, data);
                        recyclerView.setAdapter(adapter);
                    }
                });

    }
}
