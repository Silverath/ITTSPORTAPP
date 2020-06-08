package com.ittsport.ittsportapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.fragments.CardFragmentEscuela;
import com.ittsport.ittsportapp.utils.CardAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CardFragmentPagerAdapterEscuela extends FragmentStatePagerAdapter implements CardAdapter {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private List<CardFragmentEscuela> fragments;
    private float baseElevation;

    public CardFragmentPagerAdapterEscuela(FragmentManager fm, float baseElevation) {
        super(fm);
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        //TODO
        //Hay que buscar en la lista de alumnos de la escuela y filtrar por el id del logueado
        if (firebaseAuth.getCurrentUser() != null) {
            db.collection("escuelas").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            addCardFragment(new CardFragmentEscuela());
                        }
                    }
                }
            });
        }

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
    public int getItemPosition(@Nonnull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragmentEscuela) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragmentEscuela fragment) {
        fragments.add(fragment);
    }
}