package com.ittsport.ittsportapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.ChatMessagingActivity;
import com.ittsport.ittsportapp.models.Chat;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.VariablesGlobales;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UserMessagingAdapter extends RecyclerView.Adapter<UserMessagingAdapter.ViewHolder> {

    private Context mContext;
    private List<PerfilSocial> mUsers;

    private String theLastMessage;

    public UserMessagingAdapter(Context mContext, List<PerfilSocial> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.usuario_item, parent, false);

        return new UserMessagingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @Nonnull ViewHolder holder, int position) {
        final PerfilSocial usuario = mUsers.get(position);
        holder.perfil.setText(usuario.getNombre() + " " + usuario.getPrimerApellido() + " " + usuario.getSegundoApellido());
        if (usuario.getUrlImagen() == null){
            holder.profile_image.setImageDrawable(mContext.getDrawable(R.drawable.no_profile_icon));
        } else {
            Picasso.with(mContext).load(usuario.getUrlImagen()).fit().centerCrop().into(holder.profile_image);
        }

        CollectionReference collRef = FirebaseFirestore.getInstance().collection("perfilesSociales");
        collRef.whereEqualTo("nombre", usuario.getNombre()).whereEqualTo("primerApellido", usuario.getPrimerApellido())
                .whereEqualTo("segundoApellido", usuario.getSegundoApellido()).whereEqualTo("cuentaUsuarioId", usuario.getCuentaUsuarioId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                lastMessage(queryDocumentSnapshots.getDocuments().get(0).getId(), holder.last_message);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(mContext, ChatMessagingActivity.class);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference colRef = db.collection("perfilesSociales");
                colRef.whereEqualTo("nombre", usuario.getNombre())
                        .whereEqualTo("primerApellido", usuario.getPrimerApellido())
                        .whereEqualTo("segundoApellido", usuario.getSegundoApellido())
                        .whereEqualTo("cuentaUsuarioId", usuario.getCuentaUsuarioId())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                VariablesGlobales.perfilParaChatear = document.getId();
                            }
                            intent.putExtra("profileSelected", VariablesGlobales.perfilParaChatear);
                            intent.putExtra("nameSelected", usuario.getNombre());
                            mContext.startActivity(intent);
                        }
                    }
                });
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
        public TextView last_message;

        public ViewHolder(View itemView){
            super(itemView);

            perfil = itemView.findViewById(R.id.chat_username);
            profile_image = itemView.findViewById(R.id.profile_image_user);
            last_message = itemView.findViewById(R.id.last_message);
        }
    }

    private void lastMessage(final String userId, final TextView last_message){
        theLastMessage = "";
        CollectionReference collRef = FirebaseFirestore.getInstance().collection("chat");

        collRef.orderBy("sentDate").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                    Chat chat = new Chat();
                    chat.setSender(q.get("sender").toString());
                    chat.setReceiver(q.get("receiver").toString());
                    chat.setMessage(q.get("message").toString());
                    if((chat.getReceiver().equals(VariablesGlobales.perfilLogueado) && chat.getSender().equals(userId)) ||
                            (chat.getSender().equals(VariablesGlobales.perfilLogueado) && chat.getReceiver().equals(userId))){
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage) {
                    case "":
                        last_message.setText("");
                        break;
                    default:
                        last_message.setText(theLastMessage);
                        break;
                }

                theLastMessage = "";
            }
        });
    }


}
