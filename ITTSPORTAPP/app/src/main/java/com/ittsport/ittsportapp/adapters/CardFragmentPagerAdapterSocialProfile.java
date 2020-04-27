package com.ittsport.ittsportapp.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.fragments.CardFragmentSocialProfile;
import com.ittsport.ittsportapp.utils.CardAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapterSocialProfile extends FragmentStatePagerAdapter implements CardAdapter {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private List<CardFragmentSocialProfile> fragments;
    private float baseElevation;

    public CardFragmentPagerAdapterSocialProfile(FragmentManager fm, float baseElevation) {
        super(fm);
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        if (firebaseAuth.getCurrentUser() != null) {
            db.collection("perfilesSociales").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            addCardFragment(new CardFragmentSocialProfile());
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
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragmentSocialProfile) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragmentSocialProfile fragment) {
        fragments.add(fragment);
    }
}