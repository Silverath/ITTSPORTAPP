package com.ittsport.ittsportapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.AppRejectPerfilSocialActivity;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.AcceptDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppRejectPerfilSocialAdapter extends RecyclerView.Adapter<AppRejectPerfilSocialAdapter.ViewHolder>{

    private Context mContext;
    private List<PerfilSocial> perfilesSociales;

    public AppRejectPerfilSocialAdapter (Context mContext, List<PerfilSocial> perfilesSociales){
        this.mContext = mContext;
        this.perfilesSociales = perfilesSociales;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView perfil_imagen;
        public TextView perfil_nombre;
        public TextView perfil_primer_apellido;
        public TextView perfil_segundo_apellido;
        public ImageView btn_accept;
        public ImageView btn_reject;

        public ViewHolder(View itemView){
            super(itemView);
            perfil_imagen = itemView.findViewById(R.id.iv_perfil_imagen);
            perfil_nombre = itemView.findViewById(R.id.tv_nombre_perfil);
            perfil_primer_apellido = itemView.findViewById(R.id.tv_primer_apellido_perfil);
            perfil_segundo_apellido = itemView.findViewById(R.id.tv_segundo_apellido_perfil);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_reject = itemView.findViewById(R.id.btn_reject);
        }
    }

    @Nonnull
    @Override
    public AppRejectPerfilSocialAdapter.ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.perfil_social_to_accept, parent, false);
        return new AppRejectPerfilSocialAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@Nonnull AppRejectPerfilSocialAdapter.ViewHolder holder, int position){
        final PerfilSocial perfilSocial = perfilesSociales.get(position);
        if(perfilSocial.getUrlImagen() == null){
            holder.perfil_imagen.setImageDrawable(mContext.getDrawable(R.drawable.no_profile_icon));
        } else {
            Picasso.with(mContext).load(perfilSocial.getUrlImagen()).fit().centerCrop().into(holder.perfil_imagen);
        }
        holder.perfil_nombre.setText(perfilSocial.getNombre());
        holder.perfil_primer_apellido.setText(perfilSocial.getPrimerApellido());
        holder.perfil_segundo_apellido.setText(perfilSocial.getSegundoApellido());
        AcceptDialog acceptDialog = new AcceptDialog((AppRejectPerfilSocialActivity) mContext);

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setMessage("¿Estás seguro de que quieres aceptar este perfil social?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore.getInstance().collection("perfilesSociales")
                                        .whereEqualTo("nombre", perfilSocial.getNombre())
                                        .whereEqualTo("primerApellido", perfilSocial.getPrimerApellido())
                                        .whereEqualTo("segundoApellido", perfilSocial.getSegundoApellido())
                                        .whereEqualTo("escuelaId", perfilSocial.getEscuelaId())
                                        .whereEqualTo("cuentaUsuarioId", perfilSocial.getCuentaUsuarioId())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(QueryDocumentSnapshot q: task.getResult()){
                                            String id = q.getId();
                                            FirebaseFirestore.getInstance().collection("perfilesSociales")
                                                    .document(id).update("estado", Estado.ACEPTADO);
                                            for(PerfilSocial p: AppRejectPerfilSocialActivity.perfilesSocialesToAppReject){
                                                if(p.getNombre().equals(q.get("nombre").toString()) &&
                                                p.getPrimerApellido().equals(q.get("primerApellido").toString())
                                                && p.getSegundoApellido().equals(q.get("segundoApellido").toString())
                                                && p.getEscuelaId().equals(q.get("escuelaId").toString())
                                                && p.getCuentaUsuarioId().equals(q.get("cuentaUsuarioId").toString())){
                                                    perfilesSociales.remove(p);
                                                    notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                        }
                                        acceptDialog.startAcceptDialog("Perfil social aceptado con éxito.");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                 .show();
            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setMessage("¿Estás seguro de que quieres rechazar este perfil social?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore.getInstance().collection("perfilesSociales")
                                        .whereEqualTo("nombre", perfilSocial.getNombre())
                                        .whereEqualTo("primerApellido", perfilSocial.getPrimerApellido())
                                        .whereEqualTo("segundoApellido", perfilSocial.getSegundoApellido())
                                        .whereEqualTo("escuelaId", perfilSocial.getEscuelaId())
                                        .whereEqualTo("cuentaUsuarioId", perfilSocial.getCuentaUsuarioId())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(QueryDocumentSnapshot q: task.getResult()){
                                            String id = q.getId();
                                            FirebaseFirestore.getInstance().collection("perfilesSociales")
                                                    .document(id).delete();
                                            for(PerfilSocial p: AppRejectPerfilSocialActivity.perfilesSocialesToAppReject){
                                                if(p.getNombre().equals(q.get("nombre").toString()) &&
                                                        p.getPrimerApellido().equals(q.get("primerApellido").toString())
                                                        && p.getSegundoApellido().equals(q.get("segundoApellido").toString())
                                                        && p.getEscuelaId().equals(q.get("escuelaId").toString())
                                                        && p.getCuentaUsuarioId().equals(q.get("cuentaUsuarioId").toString())){
                                                    perfilesSociales.remove(p);
                                                    notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                        }
                                        acceptDialog.startAcceptDialog("Perfil social rechazado con éxito.");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount(){
        return perfilesSociales.size();
    }

}
