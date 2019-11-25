package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //change start from here
        Button mButton = (Button)findViewById(R.id.button_send);
        final EditText mEdit  = (EditText)findViewById(R.id.edittext_chatbox);
        final TextView text_out = (TextView) findViewById(R.id.text_out);

        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String message_out = mEdit.getText().toString();
                        Log.v("EditText", message_out);
                        text_out.setText(message_out);

                    }
                });


    }
}
