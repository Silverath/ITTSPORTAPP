package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.CardFragmentPagerAdapterEscuela;
import com.ittsport.ittsportapp.adapters.CardFragmentPagerAdapterSocialProfile;
import com.ittsport.ittsportapp.adapters.CardPagerAdapterEscuela;
import com.ittsport.ittsportapp.adapters.CardPagerAdapterSocialProfile;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.ShadowTransformer;

public class ListEscuelaActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private boolean mShowingFragments = false;
    private ImageView cerrarSesion;
    private Button solicitarEscuela;
    private Button inscribirseEscuela;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private CardPagerAdapterEscuela mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapterEscuela mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        cerrarSesion = (ImageView) findViewById(R.id.iv_cerrar_sesion);
        solicitarEscuela = (Button) findViewById(R.id.btn_crear_escuela);
        inscribirseEscuela = (Button) findViewById(R.id.btn_inscribirse_en_escuela);
        setContentView(R.layout.activity_escuelas_list);
        progressBar = (ProgressBar) findViewById(R.id.pb_list_escuelas);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mViewPager = (ViewPager) findViewById(R.id.vp_escuelas_list);
        tabLayout = (TabLayout) findViewById(R.id.tl_escuelas_list);
        FloatingActionButton newSocialProfile = (FloatingActionButton) findViewById(R.id.btn_nuevo_social_profile);

        mCardAdapter = new CardPagerAdapterEscuela(this);
        //TODO
        db.collection("escuelas").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Escuela escuela = document.toObject(Escuela.class);
                        escuela.setId(document.getId());
                        mCardAdapter.addCardItem(escuela);
                    }
                    mFragmentCardAdapter = new CardFragmentPagerAdapterEscuela(getSupportFragmentManager(),
                            dpToPixels(2, context));

                    mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                    mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
                    tabLayout.setupWithViewPager(mViewPager, true);

                    mViewPager.setAdapter(mCardAdapter);
                    mViewPager.setPageTransformer(false, mCardShadowTransformer);
                    mViewPager.setOffscreenPageLimit(3);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });
        //TODO
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //TODO
        solicitarEscuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //TODO
        inscribirseEscuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Change value in dp to pixels
     *
     * @param dp
     * @param context
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {

    }
}