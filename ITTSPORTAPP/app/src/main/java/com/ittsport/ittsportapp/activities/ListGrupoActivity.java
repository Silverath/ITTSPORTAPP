package com.ittsport.ittsportapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.ListGrupoAdapter;
import com.ittsport.ittsportapp.models.Grupo;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class ListGrupoActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView mRecyclerview;
    ArrayList<Grupo> grupos;
    ListGrupoAdapter listGrupoAdapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_grupo_activity);
        grupos = new ArrayList<>();

        setUpRecyclerView();
        setUpFireBase();
        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        db.collection("grupos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot querySnapshot:task.getResult()){
                        Grupo group = new Grupo(querySnapshot.getString("nombre"), querySnapshot.getString("horario"), querySnapshot.getId());
                        grupos.add(group);
                    }
                    listGrupoAdapter = new ListGrupoAdapter(ListGrupoActivity.this, grupos);
                    mRecyclerview.setAdapter(listGrupoAdapter);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
                Toast.makeText(ListGrupoActivity.this, "Could not show the entire list", Toast.LENGTH_LONG).show();
                Log.w("Fallos:", e.getMessage());
            }
        });

    }

    private void setUpFireBase() {

        db = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView() {
        mRecyclerview = (RecyclerView) findViewById(R.id.rv_grupo_names);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }
}
