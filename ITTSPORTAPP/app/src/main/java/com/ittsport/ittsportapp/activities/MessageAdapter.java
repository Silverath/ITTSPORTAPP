package com.ittsport.ittsportapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Mensaje;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>{

    MessageActivityShow messageActivityShow;
    ArrayList<Mensaje> mensajes;

    class MessageAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView mMessageTextView;
        public TextView mMessageSender;
        public TextView mMessageCuerpo;
        public TextView mMessageReceiver;
        public TextView mFecha;

        public MessageAdapterViewHolder (View view){
            super(view);
            mMessageTextView = (TextView) view.findViewById(R.id.tv_asunto_mensaje);
            mMessageSender = (TextView) view.findViewById(R.id.tv_sender_id_mensaje);
            mMessageCuerpo = (TextView) view.findViewById(R.id.tv_cuerpo_mensaje);
            mMessageReceiver = (TextView) view.findViewById(R.id.tv_receiver_id_mensaje);
            mFecha = (TextView) view.findViewById(R.id.tv_fecha_mensaje);
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
    public void onBindViewHolder(@NonNull final MessageAdapterViewHolder holder, final int position) {
        holder.mMessageTextView.setText(mensajes.get(position).getAsunto());
        holder.mMessageSender.setText(mensajes.get(position).getSenderId());

        holder.mMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mMessageCuerpo.setText(mensajes.get(position).getCuerpo());
                holder.mMessageReceiver.setText(mensajes.get(position).getReceiversIds().toString());
                holder.mFecha.setText(mensajes.get(position).getFecha().toString());
                if (holder.mMessageCuerpo.getVisibility() == View.GONE){
                    holder.mMessageCuerpo.setVisibility(View.VISIBLE);
                    holder.mMessageReceiver.setVisibility(View.VISIBLE);
                    holder.mFecha.setVisibility(View.VISIBLE);
                } else {
                    holder.mMessageCuerpo.setVisibility(View.GONE);
                    holder.mMessageReceiver.setVisibility(View.GONE);
                    holder.mFecha.setVisibility(View.GONE);
                }
            }
        });

        holder.mMessageTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialogo();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mensajes==null){
            return 0;
        } else {
            return mensajes.size();
        }
    }

    public void dialogo(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(messageActivityShow.getBaseContext());
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
