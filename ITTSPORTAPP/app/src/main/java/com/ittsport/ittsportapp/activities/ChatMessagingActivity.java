package com.ittsport.ittsportapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.PerfilSocial;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagingActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView tv_nombre;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messaging);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profile_image = findViewById(R.id.profile_image_user);
        tv_nombre = findViewById(R.id.chat_username);

        intent = getIntent();
        final String perfilId = intent.getStringExtra("profileSelected");
        String loggedId = "NuOlyGV3tPzpJmT2XDgK"; //Es Airin

        // Esto se crea para poder acceder desde dentro de los eventos.
        final List<PerfilSocial> perfilSocial = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("perfilesSociales").document(perfilId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot i = task.getResult();
                PerfilSocial perfil = new PerfilSocial((String) i.get("nombre"), (String) i.get("primerApellido"), (String) i.get("segundoApellido"), (String) i.get("usuarioId"), i.getId());
                perfilSocial.add(perfil);
            }
        });

        db.collection("chat" + loggedId).whereEqualTo("targetId", perfilId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                tv_nombre.setText(perfilSocial.get(0).getNombre());
                profile_image.setImageResource(R.mipmap.ic_launcher_round);
            }
        });

    }
}
