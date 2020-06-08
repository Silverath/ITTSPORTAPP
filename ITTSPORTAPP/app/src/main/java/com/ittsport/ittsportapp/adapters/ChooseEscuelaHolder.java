package com.ittsport.ittsportapp.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseEscuelaHolder extends RecyclerView.ViewHolder {

    public CircleImageView photo;
    public TextView nombre;
    public TextView lugar;
    public CircleImageView logo;

    public ChooseEscuelaHolder(View itemView) {
        super(itemView);
        photo = (CircleImageView) itemView.findViewById(R.id.iv_escuela_photo);
        nombre = (TextView) itemView.findViewById(R.id.tv_escuela_nombre);
        lugar = (TextView) itemView.findViewById(R.id.tv_escuela_provincia_municipio);
        logo = (CircleImageView) itemView.findViewById(R.id.iv_escuela_photo);

    }
}
