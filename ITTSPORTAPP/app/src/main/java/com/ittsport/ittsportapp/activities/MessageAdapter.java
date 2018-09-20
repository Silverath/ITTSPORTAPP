package com.ittsport.ittsportapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>{

    MessageActivityShow messageActivityShow;
    ArrayList<Mensaje> mensajes;

    class MessageAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView mMessageTextView;
        public TextView mMessageSender;

        public MessageAdapterViewHolder (View view){
            super(view);
            mMessageTextView = (TextView) view.findViewById(R.id.tv_asunto_mensaje);
            mMessageSender = (TextView) view.findViewById(R.id.tv_sender_id_mensaje);
        }
    }

    public MessageAdapter(MessageActivityShow messageActivityShow, ArrayList<Mensaje> mensajes){
        this.messageActivityShow = messageActivityShow;
        this.mensajes = mensajes;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(messageActivityShow.getBaseContext());
        View view = layoutInflater.inflate(R.layout.number_list_message, parent, false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        holder.mMessageTextView.setText(mensajes.get(position).getAsunto());
        holder.mMessageSender.setText(mensajes.get(position).getSenderId());
    }

    @Override
    public int getItemCount() {
        if(mensajes==null){
            return 0;
        } else {
            return mensajes.size();
        }
    }
}
