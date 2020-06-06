package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.ChooseEscuelaAdapter;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.util.ArrayList;
import java.util.List;

public class ChooseEscuelaActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView mRecyclerview;
    List<Escuela> escuelas;
    ChooseEscuelaAdapter chooseEscuelaAdapter;
    private int LAUNCH_INSCRIPCION_ALUMNO = 2;
    Context context;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_escuela);
        escuelas = new ArrayList<>();
        context = this;
        this.firebaseAuth = FirebaseAuth.getInstance();

        setUpRecyclerView();
        setUpFireBase();
        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        List<Escuela> todasLasEscuelas = new ArrayList<>();
        List<Escuela> escuelasNoInscrito = new ArrayList<>();
        Task<QuerySnapshot> allEscuelas = db.collection("escuelas").get();
        allEscuelas.continueWithTask(new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
            @Override
            public Task<QuerySnapshot> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                for(DocumentSnapshot documentSnapshot : task.getResult()){
                    Escuela escuela = documentSnapshot.toObject(Escuela.class);
                    escuela.setId(documentSnapshot.getId());
                    todasLasEscuelas.add(escuela);
                }
                return db.collection("perfilesSociales").whereEqualTo("cuentaUsuarioId", firebaseAuth.getCurrentUser().getUid()).get();
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    PerfilSocial perfil = documentSnapshot.toObject(PerfilSocial.class);
                    perfil.setId(documentSnapshot.getId());
                    for(Escuela e : todasLasEscuelas){
                        if(!e.getId().equals(perfil.getEscuelaId()) && !escuelasNoInscrito.contains(e)){
                            escuelasNoInscrito.add(e);
                        }
                    }
                }
                if(escuelasNoInscrito.isEmpty()){
                    escuelas = todasLasEscuelas;
                }
                else{
                    escuelas = escuelasNoInscrito;
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
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
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
