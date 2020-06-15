package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.CardFragmentPagerAdapterEscuela;
import com.ittsport.ittsportapp.adapters.CardPagerAdapterEscuela;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.ShadowTransformer;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class ListEscuelaActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private boolean mShowingFragments = false;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private CardPagerAdapterEscuela mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapterEscuela mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private List<Escuela> escuelas;
    private int LAUNCH_SECOND_ACTIVITY = 1;
    private MaterialToolbar appBarLayout;
    private Map<String, Integer> perfilesVerificados;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escuelas_list);
        final Context context = this;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        escuelas = new ArrayList<>();
        appBarLayout = (MaterialToolbar) findViewById(R.id.topAppBar_escuelas_list);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mViewPager = (ViewPager) findViewById(R.id.vp_escuelas_list);
        tabLayout = (TabLayout) findViewById(R.id.tl_escuelas_list);
        perfilesVerificados = new HashMap<>();

        appBarLayout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        appBarLayout.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.escuelas_list_cerrar_sesion:
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            Toast.makeText(ListEscuelaActivity.this, "no ai nadie xd", Toast.LENGTH_SHORT).show();
                        } else {
                            VariablesGlobales shared = new VariablesGlobales(context);
                            shared.setEscuelaSeleccionada(null);
                            shared.setPerfilLogueadoId(null);
                            FirebaseAuth.getInstance().signOut();
                            Context context = ListEscuelaActivity.this;
                            Intent goToLogin = new Intent(context, LoginActivity.class);
                            startActivity(goToLogin);
                        }
                        return true;
                    case R.id.escuelas_list_inscribirse:
                        goToNextActivity();
                        return true;
                    case R.id.escuelas_list_crear_escuela:
                        Context context = ListEscuelaActivity.this;
                        Intent goToNewEscuelaActivity = new Intent(context, NewEscuelaActivity.class);
                        startActivity(goToNewEscuelaActivity);
                        return true;
                    default:
                        return true;
                }
            }
        });

        Task<QuerySnapshot> perfiles = db.collection("perfilesSociales").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("estado", Estado.ACEPTADO).get();
        perfiles.continueWithTask(new Continuation<QuerySnapshot, Task<List<DocumentSnapshot>>>() {
            @Override
            public Task<List<DocumentSnapshot>> then(@Nonnull Task<QuerySnapshot> task) throws Exception {
                List<Task<DocumentSnapshot>> allTasks = new ArrayList<>();
                List<String> idEscuelas = new ArrayList<>();
                Integer perfiles = 0;
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    PerfilSocial perfil = documentSnapshot.toObject(PerfilSocial.class);
                    String id = documentSnapshot.getId();
                    perfil.setId(documentSnapshot.getId());
                    if (!idEscuelas.contains(perfil.getEscuelaId())) {
                        perfiles++;
                        perfilesVerificados.put(perfil.getEscuelaId(), perfiles);
                        idEscuelas.add(perfil.getEscuelaId());
                        Task<DocumentSnapshot> query = db.collection("escuelas").document(perfil.getEscuelaId()).get();
                        allTasks.add(query);
                    }
                    else{
                        perfiles++;
                        perfilesVerificados.put(perfil.getEscuelaId(), perfiles);
                    }
                }
                return Tasks.<DocumentSnapshot>whenAllSuccess(allTasks);
            }
        }).addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                mCardAdapter = new CardPagerAdapterEscuela(context, perfilesVerificados);
                for (DocumentSnapshot doc : documentSnapshots) {
                    if(doc.exists()){
                        Escuela escuela = doc.toObject(Escuela.class);
                        escuela.setId(doc.getId());
                        if (!escuelas.contains(escuela)) {
                            escuelas.add(escuela);
                            mCardAdapter.addCardItem(escuela);
                            mCardAdapter.notifyDataSetChanged();
                        }
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
                Log.d("", e.toString());
            }
        });
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCardAdapter.notifyDataSetChanged();
    }
}