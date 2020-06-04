package com.ittsport.ittsportapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.AppRejectEscuelaActivity;
import com.ittsport.ittsportapp.activities.HomeActivity;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRejectEscuelaAdapter extends RecyclerView.Adapter<AppRejectEscuelaAdapter.ViewHolder> {

    private Context mContext;
    private List<Escuela> escuelas;

    public AppRejectEscuelaAdapter(Context mContext, List<Escuela> escuelas){
        this.mContext = mContext;
        this.escuelas = escuelas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView escuela_logo;
        public TextView nombre_escuela;
        public TextView direccion_escuela;
        public TextView provincia_escuela;
        public TextView municipio_escuela;
        public ImageView btn_accept;
        public ImageView btn_reject;

        public ViewHolder(View itemView){
            super(itemView);
            escuela_logo = itemView.findViewById(R.id.iv_escuela_logo);
            nombre_escuela = itemView.findViewById(R.id.tv_nombre_escuela);
            direccion_escuela = itemView.findViewById(R.id.tv_direccion_escuela);
            provincia_escuela = itemView.findViewById(R.id.tv_provincia_escuela);
            municipio_escuela = itemView.findViewById(R.id.tv_municipio_escuela);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_reject = itemView.findViewById(R.id.btn_reject);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.escuela_to_accept, parent, false);
        return new AppRejectEscuelaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppRejectEscuelaAdapter.ViewHolder holder, int position) {
        final Escuela escuela = escuelas.get(position);
        //holder.escuela_logo = blablabla TODO
        holder.nombre_escuela.setText(escuela.getNombre());
        holder.direccion_escuela.setText(escuela.getDireccion());
        holder.provincia_escuela.setText(escuela.getProvincia());
        holder.municipio_escuela.setText(escuela.getMunicipio());

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setMessage("¿Estás seguro de que quieres aceptar esta escuela?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore.getInstance().collection("escuelas")
                                        .whereEqualTo("nombre", escuela.getNombre())
                                        .whereEqualTo("direccion", escuela.getDireccion())
                                        .whereEqualTo("municipio", escuela.getMunicipio())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(QueryDocumentSnapshot q: task.getResult()){
                                            String id = q.getId();
                                            FirebaseFirestore.getInstance().collection("escuelas")
                                                    .document(id)
                                                    .update("status", Estado.ACEPTADO);
                                            for(Escuela e: AppRejectEscuelaActivity.escuelasToAppReject){
                                                if(e.getDireccion().equals(q.get("direccion").toString()) &&
                                                        e.getMunicipio().equals(q.get("municipio").toString()) &&
                                                        e.getNombre().equals(q.get("nombre").toString())){
                                                    escuelas.remove(e);
                                                    notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                            Toast.makeText(mContext, "Escuela aceptada con éxito.", Toast.LENGTH_SHORT).show();
                                        }

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
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setMessage("¿Estás seguro de que quieres rechazar esta escuela?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore.getInstance().collection("escuelas")
                                        .whereEqualTo("nombre", escuela.getNombre())
                                        .whereEqualTo("direccion", escuela.getDireccion())
                                        .whereEqualTo("municipio", escuela.getMunicipio())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(QueryDocumentSnapshot q: task.getResult()){
                                            String id = q.getId();
                                            FirebaseFirestore.getInstance().collection("escuelas")
                                                    .document(id)
                                                    .update("status", Estado.RECHAZADO);
                                            for(Escuela e: AppRejectEscuelaActivity.escuelasToAppReject){
                                                if(e.getDireccion().equals(q.get("direccion").toString()) &&
                                                        e.getMunicipio().equals(q.get("municipio").toString()) &&
                                                        e.getNombre().equals(q.get("nombre").toString())){
                                                    escuelas.remove(e);
                                                    notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                        }
                                        Toast.makeText(mContext, "Escuela rechazada con éxito.", Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {
        return escuelas.size();
    }


}
