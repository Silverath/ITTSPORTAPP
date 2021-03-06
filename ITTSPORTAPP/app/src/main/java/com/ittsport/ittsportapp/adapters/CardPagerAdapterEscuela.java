package com.ittsport.ittsportapp.adapters;

import android.app.Activity;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.ChooseEscuelaActivity;
import com.ittsport.ittsportapp.activities.InscripcionAlumnoActivity;
import com.ittsport.ittsportapp.activities.ListSocialProfileActivity;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.utils.CardAdapter;
import com.ittsport.ittsportapp.utils.VariablesGlobales;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardPagerAdapterEscuela extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<Escuela> mData;
    private float mBaseElevation;
    private Map<String, AtomicInteger> perfilesVerificados;
    private Context context;

    public CardPagerAdapterEscuela(Context contextActivity, Map<String, AtomicInteger> perfilesVerificados) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        context = contextActivity;
        this.perfilesVerificados = perfilesVerificados;
    }

    public void addCardItem(Escuela item) {
        mViews.add(null);
        mData.add(item);
    }

    public void addAllCardItems(List<Escuela> list){
        mViews.add(null);
        for(Escuela escuela : list){
            mData.add(escuela);
        }
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
                .inflate(R.layout.activity_escuelas_list_item, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        final CardView cardView = (CardView) view.findViewById(R.id.cv_escuelas_item);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Escuela escuela = mData.get(position);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference colRef = db.collection("escuelas");
                colRef.document(escuela.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@Nonnull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            VariablesGlobales shared = new VariablesGlobales(context);
                            VariablesGlobales.perfilLogueado = task.getResult().getId();
                            shared.setEscuelaSeleccionada(task.getResult().getId());
                            Intent startListSocialProfileActivityClass = new Intent(context, ListSocialProfileActivity.class);
                            context.startActivity(startListSocialProfileActivityClass);
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

    private void bind(Escuela item, View view) {
        CircleImageView logo = (CircleImageView) view.findViewById(R.id.iv_escuelas_list_item_logo);
        TextView name = (TextView) view.findViewById(R.id.tv_list_escuelas__item_nombre);
        TextView verificados = (TextView) view.findViewById(R.id.tv_perfiles_sociales_numero_verificados);
        MaterialButton addPerfil = (MaterialButton) view.findViewById(R.id.btn_list_escuelas_añadir_perfil);
        addPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariablesGlobales sharedPreferences = new VariablesGlobales(context);
                sharedPreferences.setEscuelaParaInscribirse(item.getId());
                int LAUNCH_THIRD_ACTIVITY = 2;
                Intent startNewSocialProfileActivityClass = new Intent(context, InscripcionAlumnoActivity.class);
                ((Activity) context).startActivityForResult(startNewSocialProfileActivityClass, LAUNCH_THIRD_ACTIVITY);
            }
        });
        name.setText(item.getNombre().toString());
        Picasso.with(context).load(item.getUrlLogo())
                .fit().centerCrop().into(logo);
        verificados.setText(perfilesVerificados.get(item.getId()).toString());
    }
}