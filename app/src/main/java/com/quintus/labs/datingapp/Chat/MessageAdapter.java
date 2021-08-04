package com.quintus.labs.datingapp.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessages;

    public void setData(List<Message> list)
    {
        mMessages = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        if(message == null) {
            return;
        }
        holder.mTextView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        if(mMessages != null)
            return mMessages.size();
        return 0;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}
