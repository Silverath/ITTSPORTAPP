package com.ittsport.ittsportapp.utils;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.PerfilSocial;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<PerfilSocial> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(PerfilSocial item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.list_social_profile_adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        final CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerfilSocial perfilSocial = mData.get(position);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference colRef = db.collection("perfilesSociales");
                colRef.whereEqualTo("nombre", perfilSocial.getNombre())
                        .whereEqualTo("primerApellido", perfilSocial.getPrimerApellido())
                        .whereEqualTo("segundoApellido", perfilSocial.getSegundoApellido())
                        .whereEqualTo("cuentaUsuarioId", perfilSocial.getCuentaUsuarioId())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                VariablesGlobales.perfilLogueado = document.getId();
                            }
                        }
                    }
                });
            }
        });

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(PerfilSocial item, View view) {
        TextView name = (TextView) view.findViewById(R.id.tv_list_perfil_nombre_adapter);
        TextView first = (TextView) view.findViewById(R.id.tv_list_perfil_primerApellido_adapter);
        TextView second = (TextView) view.findViewById(R.id.tv_list_perfil_segundoApellido_adapter);
        name.setText(item.getNombre());
        first.setText(item.getPrimerApellido());
        second.setText(item.getSegundoApellido());
    }
}