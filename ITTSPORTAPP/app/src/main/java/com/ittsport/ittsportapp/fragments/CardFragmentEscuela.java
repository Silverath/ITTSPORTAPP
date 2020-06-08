package com.ittsport.ittsportapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.annotations.Nullable;
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