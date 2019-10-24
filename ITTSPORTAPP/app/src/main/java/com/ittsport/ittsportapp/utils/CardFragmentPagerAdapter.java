package com.ittsport.ittsportapp.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.models.PerfilSocial;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private List<CardFragment> fragments;
    private float baseElevation;
    List<PerfilSocial> perfiles;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        if(firebaseAuth.getCurrentUser() != null){
            db.collection("perfiles").whereEqualTo("email", firebaseAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().size() > 0){
                            for(DocumentSnapshot document : task.getResult()){
                                PerfilSocial perfil = document.toObject(PerfilSocial.class);
                                addCardFragment(perfil);
                            }
                        }
                    }
                }
            });
        }

    }

    @Override
    public float getPageWidth (int position) {
        return 0.93f;
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CardFragment.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(PerfilSocial perfil) {
        Bundle bundle = new Bundle();
        bundle.putString("nombre", perfil.getNombre());
        bundle.putString("primerApellido", perfil.getPrimerApellido());
        bundle.putString("segundoApellido", perfil.getSegundoApellido());
        CardFragment fragment = new CardFragment();
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

}