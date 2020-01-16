package com.ittsport.ittsportapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.ChatMessagingActivity;
import com.ittsport.ittsportapp.models.CuentaUsuario;
import com.ittsport.ittsportapp.models.PerfilSocial;

import java.util.List;

public class UserMessagingAdapter extends RecyclerView.Adapter<UserMessagingAdapter.ViewHolder> {

    private Context mContext;
    private List<PerfilSocial> mUsers;

    public UserMessagingAdapter(Context mContext, List<PerfilSocial> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.usuario_item, parent, false);

        return new UserMessagingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PerfilSocial usuario = mUsers.get(position);
        holder.perfil.setText(usuario.getNombre() + " " + usuario.getPrimerApellido() + " " + usuario.getSegundoApellido());
        holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatMessagingActivity.class);
                intent.putExtra("profileSelected", usuario.getPerfilID());
                intent.putExtra("nameSelected", usuario.getNombre());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView perfil;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            perfil = itemView.findViewById(R.id.chat_username);
            profile_image = itemView.findViewById(R.id.profile_image_user);
        }
    }
}
