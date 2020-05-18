package com.ittsport.ittsportapp.activities;

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

    public ChooseEscuelaAdapter(ChooseEscuelaActivity chooseEscuelaActivity, ArrayList<Escuela> escuelas) {
        this.chooseEscuelaActivity = chooseEscuelaActivity;
        this.escuelas = escuelas;
    }

    @NonNull
    @Override
    public ChooseEscuelaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(chooseEscuelaActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.list_grupo_activity_row, parent, false);

        return new ChooseEscuelaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseEscuelaHolder holder, int position) {
        holder.nombre.setText(escuelas.get(position).getNombre());
        String lugar = escuelas.get(position).getProvincia() + ", " + escuelas.get(position).getMunicipio();
        holder.lugar.setText(lugar);
    }

    @Override
    public int getItemCount() {
        return escuelas.size();
    }
}
