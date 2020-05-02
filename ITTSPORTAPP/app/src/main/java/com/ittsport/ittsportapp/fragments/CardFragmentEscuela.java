package com.ittsport.ittsportapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.utils.CardAdapter;

public class CardFragmentEscuela extends Fragment {

    private CardView cardView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_escuelas_list_item, container, false);

        cardView = (CardView) view.findViewById(R.id.cv_escuelas_item);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}