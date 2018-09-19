package com.ittsport.ittsportapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Mensaje;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>{

    private List<Mensaje> mMensajesData;

    class MessageAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView mMessageTextView;

        public MessageAdapterViewHolder (View view){
            super(view);
            mMessageTextView = (TextView) view.findViewById(R.id.tv_asunto_mensaje);
        }
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_list_message, parent, false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        holder.mMessageTextView.setText(mMensajesData.get(position).getAsunto().toString());
    }

    @Override
    public int getItemCount() {
        if(mMensajesData==null){
            return 0;
        } else {
            return mMensajesData.size();
        }
    }

    public void setMensajesData(List<Mensaje> mensajesData){
        mMensajesData = mensajesData;
        notifyDataSetChanged();
    }
}
