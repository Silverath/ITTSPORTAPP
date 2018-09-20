package com.ittsport.ittsportapp.activities;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class MessageActivityShow extends AppCompatActivity {

    private static final String TAG = "MessageActivityShow";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView mRecyclerView;

    private MessageAdapter mMessageAdapter;

    ArrayList<Mensaje> mensajes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);
        mensajes = new ArrayList<Mensaje>();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_message_asuntos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        loadData();
    }

    private void loadData(){
        db.collection("mensajes").orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot querySnapshot:task.getResult()){
                    Mensaje mensaje = new Mensaje(querySnapshot.getString("asunto"), querySnapshot.getString("cuerpo"), querySnapshot.getDate("fecha"), querySnapshot.getString("senderId"));
                    mensajes.add(mensaje);
                }
                mMessageAdapter = new MessageAdapter(MessageActivityShow.this, mensajes);
                mRecyclerView.setAdapter(mMessageAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessageActivityShow.this, "Could not show the entire list", Toast.LENGTH_LONG).show();
                Log.w("Fallos:", e.getMessage());
            }
        });
    }
}
