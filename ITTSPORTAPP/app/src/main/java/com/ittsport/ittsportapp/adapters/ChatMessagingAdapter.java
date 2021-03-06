package com.ittsport.ittsportapp.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Chat;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.util.List;

import javax.annotation.Nonnull;

public class ChatMessagingAdapter extends RecyclerView.Adapter<ChatMessagingAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    VariablesGlobales sharedPreferences;
    String loggedId;

    public ChatMessagingAdapter(Context mContext, List<Chat> mChat) {
        this.mChat = mChat;
        this.mContext = mContext;
        sharedPreferences = new VariablesGlobales(this.mContext);
        loggedId = sharedPreferences.getPerfilLogueadoId();
    }

    @Nonnull
    @Override
    public ChatMessagingAdapter.ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ChatMessagingAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ChatMessagingAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@Nonnull ChatMessagingAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image_user);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(loggedId)){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}