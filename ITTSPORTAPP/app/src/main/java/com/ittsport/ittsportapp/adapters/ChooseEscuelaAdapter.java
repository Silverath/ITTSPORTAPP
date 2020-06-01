package com.ittsport.ittsportapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.ChooseEscuelaActivity;
import com.ittsport.ittsportapp.models.Escuela;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChooseEscuelaAdapter extends RecyclerView.Adapter<ChooseEscuelaHolder> implements Filterable {

    ChooseEscuelaActivity chooseEscuelaActivity;
    ArrayList<Escuela> escuelas;
    ArrayList<Escuela> escuelasFull;
    private Context mContext;

    public ChooseEscuelaAdapter(ChooseEscuelaActivity chooseEscuelaActivity, ArrayList<Escuela> escuelas, Context context) {
        this.chooseEscuelaActivity = chooseEscuelaActivity;
        this.escuelas = escuelas;
        this.mContext = context;
        this.escuelasFull = new ArrayList<>(escuelas);
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
        Escuela current = escuelas.get(position);
        String nombre = current.getNombre();
        holder.nombre.setText(nombre);
        String lugar = current.getProvincia() + ", " + current.getMunicipio();
        holder.lugar.setText(lugar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof ChooseEscuelaActivity) {
                    ((ChooseEscuelaActivity)mContext).goToNextActivity(current);
                }
            }
        });
        Picasso.with(mContext).load(current.getUrlLogo())
                .fit().centerCrop().into(holder.logo);

    }

    @Override
    public int getItemCount() {
        return escuelas.size();
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Escuela> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(escuelasFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase();
                for(Escuela item : escuelasFull){
                    if(item.getNombre().toLowerCase().contains(filterPattern) || item.getMunicipio().toLowerCase().contains(filterPattern) || item.getProvincia().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            escuelas.clear();
            escuelas.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
