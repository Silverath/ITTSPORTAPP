package com.ittsport.ittsportapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.HomeActivity;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.CardAdapter;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CardPagerAdapterSocialProfile extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<PerfilSocial> mData;
    private float mBaseElevation;
    private Context context;

    public CardPagerAdapterSocialProfile(Context contextActivity) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        context = contextActivity;
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
                colRef.document(perfilSocial.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@Nonnull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            VariablesGlobales shared = new VariablesGlobales(context);
                            VariablesGlobales.perfilLogueado = task.getResult().getId();
                            shared.setPerfilLogueadoId(task.getResult().getId());
                            Intent startHomeActivityClass = new Intent(context, HomeActivity.class);
                            context.startActivity(startHomeActivityClass);
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