package com.ittsport.ittsportapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Grupo;

import java.util.ArrayList;

public class ChooseEscuelaActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView mRecyclerview;
    ArrayList<Escuela> escuelas;
    ChooseEscuelaAdapter chooseEscuelaAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_escuela);
        escuelas = new ArrayList<>();


        setUpRecyclerView();
        setUpFireBase();
        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        db.collection("escuelas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Escuela escuela = documentSnapshot.toObject(Escuela.class);
                    escuela.setId(documentSnapshot.getId());
                    escuelas.add(escuela);
                }
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
}
