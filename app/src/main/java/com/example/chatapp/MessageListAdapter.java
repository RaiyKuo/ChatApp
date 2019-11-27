package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter{
    private List<MainActivity.Message> mdata;

    public MessageListAdapter(List<MainActivity.Message> data) {
        this.mdata = data;
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    @Override
    public int getItemViewType(int position){
        String user = mdata.get(position).user;
        if (user.equals(MainActivity.my_identity)){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(v);
        }
        else if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0){
            ((SentMessageHolder) holder).bind(position);
        }
        else{
            ((ReceivedMessageHolder) holder).bind(position);
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            nameText = itemView.findViewById(R.id.text_message_name);
        }
        void bind(int position){
            messageText.setText(mdata.get(position).text);
            nameText.setText(mdata.get(position).user);
        }
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        public SentMessageHolder(View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
        }

        void bind(int position){
            messageText.setText(mdata.get(position).text);
        }
    }

}

