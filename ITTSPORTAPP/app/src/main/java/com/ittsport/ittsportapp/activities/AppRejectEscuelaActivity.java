package com.ittsport.ittsportapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.AppRejectEscuelaAdapter;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AppRejectEscuelaActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ArrayList<Escuela> escuelasToAppReject;

    public static RecyclerView mRecyclerView;

    public static AppRejectEscuelaAdapter appRejectEscuelaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_reject_escuela);

        escuelasToAppReject = new ArrayList<Escuela>();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_escuelas_to_manage);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        escuelasPendientes();
    }

    private void escuelasPendientes(){
        CollectionReference collRef = db.collection("escuelas");

        collRef.whereEqualTo("status", Estado.PENDIENTE).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                    Escuela escuela = new Escuela();
                    escuela.setNombre(q.get("nombre").toString());
                    escuela.setDireccion(q.get("direccion").toString());
                    escuela.setMunicipio(q.get("municipio").toString());
                    escuela.setProvincia(q.get("provincia").toString());
                    escuela.setStatus(Estado.valueOf(q.get("status").toString()));
                    escuelasToAppReject.add(escuela);
                }
                appRejectEscuelaAdapter = new AppRejectEscuelaAdapter(AppRejectEscuelaActivity.this, escuelasToAppReject);
                mRecyclerView.setAdapter(appRejectEscuelaAdapter);
            }
        });
    }
}
