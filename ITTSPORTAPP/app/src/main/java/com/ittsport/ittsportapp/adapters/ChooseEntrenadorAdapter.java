package com.ittsport.ittsportapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseEntrenadorAdapter extends RecyclerView.Adapter<ChooseEntrenadorAdapter.ChooseEntrenadorHolder> {

    List<PerfilSocial> entrenadores;
    Context context;
    private PerfilSocial lastEntrenadorSelected = null;

    public ChooseEntrenadorAdapter(List<PerfilSocial> entrenadores, Context context) {
        this.entrenadores = entrenadores;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseEntrenadorHolder holder, int position) {
        PerfilSocial entrenador = entrenadores.get(position);
        holder.nombre.setText(entrenador.getNombre() + " " + entrenador.getPrimerApellido() + " " + entrenador.getSegundoApellido());
        if(lastEntrenadorSelected != null){
            holder.radioButton.setChecked(lastEntrenadorSelected.equals(entrenador));
        }
        Picasso.with(context).load(entrenador.getUrlImagen())
                .fit().centerCrop().into(holder.photo);
    }

    @NonNull
    @Override
    public ChooseEntrenadorAdapter.ChooseEntrenadorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_choose_entrenador_para_grupo_item, parent, false);

        return new ChooseEntrenadorAdapter.ChooseEntrenadorHolder(view);
    }

    @Override
    public int getItemCount() {
        return entrenadores.size();
    }

    public class ChooseEntrenadorHolder extends RecyclerView.ViewHolder {

        public CircleImageView photo;
        public MaterialTextView nombre;
        public MaterialRadioButton radioButton;
        public PerfilSocial selected;

        public ChooseEntrenadorHolder(View itemView) {
            super(itemView);
            photo = (CircleImageView) itemView.findViewById(R.id.iv_choose_entrenador_dialog_item_foto);
            nombre = (MaterialTextView) itemView.findViewById(R.id.iv_choose_entrenador_dialog_item_nombre);
            radioButton = (MaterialRadioButton) itemView.findViewById(R.id.rb_choose_entrenador_dialog_item);


            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastEntrenadorSelected = entrenadores.get(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastEntrenadorSelected = entrenadores.get(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }

    public PerfilSocial getLastSelected(){
        return lastEntrenadorSelected;
    }
}
