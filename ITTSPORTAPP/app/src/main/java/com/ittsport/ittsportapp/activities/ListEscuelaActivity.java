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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.CardFragmentPagerAdapterEscuela;
import com.ittsport.ittsportapp.adapters.CardFragmentPagerAdapterSocialProfile;
import com.ittsport.ittsportapp.adapters.CardPagerAdapterEscuela;
import com.ittsport.ittsportapp.adapters.CardPagerAdapterSocialProfile;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.ShadowTransformer;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private List<Escuela> escuelas;
    private int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escuelas_list);
        final Context context = this;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        escuelas = new ArrayList<>();
        cerrarSesion = (ImageView) findViewById(R.id.iv_cerrar_sesion);
        solicitarEscuela = (Button) findViewById(R.id.btn_crear_escuela);
        inscribirseEscuela = (Button) findViewById(R.id.btn_inscribirse_en_escuela);
        progressBar = (ProgressBar) findViewById(R.id.pb_list_escuelas);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mViewPager = (ViewPager) findViewById(R.id.vp_escuelas_list);
        tabLayout = (TabLayout) findViewById(R.id.tl_escuelas_list);

        mCardAdapter = new CardPagerAdapterEscuela(this);
        //TODO
        Task<QuerySnapshot> perfiles = db.collection("perfilesSociales").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("status", Estado.ACEPTADO).get();
        perfiles.continueWithTask(new Continuation<QuerySnapshot, Task<List<DocumentSnapshot>>>() {
            @Override
            public Task<List<DocumentSnapshot>> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                List<Task<DocumentSnapshot>> allTasks = new ArrayList<>();
                List<String> idEscuelas = new ArrayList<>();
                if (task.getResult().isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    PerfilSocial perfil = documentSnapshot.toObject(PerfilSocial.class);
                    String id = documentSnapshot.getId();
                    perfil.setId(documentSnapshot.getId());
                    if (!idEscuelas.contains(perfil.getEscuelaId())) {
                        idEscuelas.add(perfil.getEscuelaId());
                        Task<DocumentSnapshot> query = db.collection("escuelas").document(perfil.getEscuelaId()).get();
                        allTasks.add(query);
                    }
                }
                return Tasks.<DocumentSnapshot>whenAllSuccess(allTasks);
            }
        }).addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                for (DocumentSnapshot doc : documentSnapshots) {
                    Escuela escuela = doc.toObject(Escuela.class);
                    if (!escuelas.contains(escuela)) {
                        escuelas.add(escuela);
                        mCardAdapter.addCardItem(escuela);
                        mCardAdapter.notifyDataSetChanged();
                    }
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
                if (documentSnapshots.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.toString());
            }
        });

        //TODO
        if (cerrarSesion != null)
            cerrarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user == null){
                        Toast.makeText(ListEscuelaActivity.this, "no ai nadie xd", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseAuth.getInstance().signOut();
                        Context context = ListEscuelaActivity.this;
                        Intent goToLogin = new Intent(context, LoginActivity.class);
                        startActivity(goToLogin);
                    }
                }
            });
        //TODO
        if (solicitarEscuela != null)
            solicitarEscuela.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        //TODO
        if (inscribirseEscuela != null)
            inscribirseEscuela.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToNextActivity();
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

    public void goToNextActivity() {
        int LAUNCH_SECOND_ACTIVITY = 1;
        Context context = ListEscuelaActivity.this;
        Intent startNewSocialProfileActivityClass = new Intent(context, ChooseEscuelaActivity.class);
        startActivityForResult(startNewSocialProfileActivityClass, LAUNCH_SECOND_ACTIVITY);
    }
}