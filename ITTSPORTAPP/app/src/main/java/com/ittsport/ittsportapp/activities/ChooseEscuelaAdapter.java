package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Escuela;

import java.util.ArrayList;

public class ChooseEscuelaAdapter extends RecyclerView.Adapter<ChooseEscuelaHolder>{

    ChooseEscuelaActivity chooseEscuelaActivity;
    ArrayList<Escuela> escuelas;
    private Context mContext;

    public ChooseEscuelaAdapter(ChooseEscuelaActivity chooseEscuelaActivity, ArrayList<Escuela> escuelas, Context context) {
        this.chooseEscuelaActivity = chooseEscuelaActivity;
        this.escuelas = escuelas;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ChooseEscuelaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(chooseEscuelaActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.activity_choose_escuela_item, parent, false);

        return new ChooseEscuelaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseEscuelaHolder holder, int position) {
        String nombre = escuelas.get(position).getNombre();
        holder.nombre.setText(nombre);
        String lugar = escuelas.get(position).getProvincia() + ", " + escuelas.get(position).getMunicipio();
        holder.lugar.setText(lugar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof ChooseEscuelaActivity) {
                    ((ChooseEscuelaActivity)mContext).goToNextActivity(escuelas.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return escuelas.size();
    }
}
