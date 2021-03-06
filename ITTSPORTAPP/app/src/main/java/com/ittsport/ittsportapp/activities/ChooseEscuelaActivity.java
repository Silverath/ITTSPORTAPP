package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.ChooseEscuelaAdapter;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.LoadingDialog;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ChooseEscuelaActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView mRecyclerview;
    List<Escuela> escuelas;
    ChooseEscuelaAdapter chooseEscuelaAdapter;
    private int LAUNCH_INSCRIPCION_ALUMNO = 2;
    Context context;
    FirebaseAuth firebaseAuth;
    private MaterialToolbar appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_escuela);
        escuelas = new ArrayList<>();
        context = this;
        appBarLayout = (MaterialToolbar) findViewById(R.id.topAppBar_choose_escuela);
        setSupportActionBar(appBarLayout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.firebaseAuth = FirebaseAuth.getInstance();
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

        appBarLayout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setUpRecyclerView();
        setUpFireBase();
        loadDataFromFirebase();
        loadingDialog.dismissDialog();
    }

    private void loadDataFromFirebase() {
        List<Escuela> todasLasEscuelas = new ArrayList<>();
        List<Escuela> escuelasInscrito = new ArrayList<>();
        List<Escuela> escuelasNoInscrito = new ArrayList<>();
        Task<QuerySnapshot> allEscuelas = db.collection("escuelas").whereEqualTo("estado", Estado.ACEPTADO).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Escuela escuela = documentSnapshot.toObject(Escuela.class);
                    escuela.setId(documentSnapshot.getId());
                    escuelas.add(escuela);
                }
                chooseEscuelaAdapter = new ChooseEscuelaAdapter(ChooseEscuelaActivity.this, escuelas, context);
                mRecyclerview.setAdapter(chooseEscuelaAdapter);
            }
        });
    }
    private void setUpFireBase() {

        db = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView() {
        mRecyclerview = (RecyclerView) findViewById(R.id.rv_escuelas_choose_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerview.getContext(),
                llm.getOrientation());
        mRecyclerview.addItemDecoration(dividerItemDecoration);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(llm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_INSCRIPCION_ALUMNO) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    public void goToNextActivity(Escuela escuela){
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        sharedPreferences.setEscuelaParaInscribirse(escuela.getId());
        int LAUNCH_THIRD_ACTIVITY = 2;
        Context context = ChooseEscuelaActivity.this;
        Intent startNewSocialProfileActivityClass = new Intent(context, InscripcionAlumnoActivity.class);
        startNewSocialProfileActivityClass.putExtra("escuela", escuela);
        startActivityForResult(startNewSocialProfileActivityClass, LAUNCH_THIRD_ACTIVITY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choose_escuela, menu);

        MenuItem searchItem = menu.findItem(R.id.choose_escuela_search_icon);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                chooseEscuelaAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
