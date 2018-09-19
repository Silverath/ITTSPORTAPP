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

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_message_asuntos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMessageAdapter = new MessageAdapter();

        mRecyclerView.setAdapter(mMessageAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadData();
    }

    private void loadData(){
        //mRecyclerView.setVisibility(View.VISIBLE);
        final List<Mensaje> mensajes = new ArrayList<Mensaje>();
        db.collection("mensajes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot task) {
                        if(!task.isEmpty()){
                            List<Mensaje> ci = task.toObjects(Mensaje.class);
                            mensajes.addAll(ci);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
        mMessageAdapter.setMensajesData(mensajes);
    }
}
