package com.ittsport.ittsportapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.PerfilSocial;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class NewSocialProfileActivity extends AppCompatActivity {

    EditText nombre;
    EditText primerApellido;
    EditText segundoApellido;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    Button createProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_social_profile);
        db  = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        nombre = (EditText) findViewById(R.id.et_perfil_social_nombre);
        primerApellido = (EditText) findViewById(R.id.et_perfil_social_primer_apellido);
        segundoApellido = (EditText) findViewById(R.id.et_perfil_social_segundo_apellido);
        createProfile = (Button) findViewById(R.id.btn_crear_perfil_social);

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre.getText().toString().equals("") || primerApellido.getText().toString().equals("") || segundoApellido.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
                }
                else{
                    String cuentaUsuarioId = firebaseAuth.getCurrentUser().getUid();
                    PerfilSocial nuevo = new PerfilSocial(nombre.getText().toString(), primerApellido.getText().toString(), segundoApellido.getText().toString(), cuentaUsuarioId);
                    Map<String, Object> nuevoPerfil = new HashMap<>();
                    nuevoPerfil.put("nombre", nuevo.getNombre());
                    nuevoPerfil.put("primerApellido", nuevo.getPrimerApellido());
                    nuevoPerfil.put("segundoApellido", nuevo.getSegundoApellido());
                    nuevoPerfil.put("cuentaUsuarioId", nuevo.getCuentaUsuarioId());
                    db.collection("perfilesSociales").document()
                            .set(nuevoPerfil)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getBaseContext(), "Perfil social creado", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(), "Ha habido un problema", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
