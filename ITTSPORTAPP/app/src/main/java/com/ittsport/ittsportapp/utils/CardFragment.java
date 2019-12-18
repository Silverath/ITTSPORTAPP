package com.ittsport.ittsportapp.utils;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ittsport.ittsportapp.R;


public class CardFragment extends Fragment {

    private CardView cardView;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;

    public CardFragment getInstance(String n, String primer, String segundo) {
        this.nombre = n;
        this.primerApellido = primer;
        this.segundoApellido = segundo;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_choose_social_profile_item, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.title);



        title.setText(nombre + " " + primerApellido + " " + segundoApellido);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            nombre = getArguments().getString("nombre");
            primerApellido = getArguments().getString("primerApellido");
            segundoApellido = getArguments().getString("segundoApellido");
        }
    }

    public CardView getCardView() {
        return cardView;
    }
}