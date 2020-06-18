package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.VariablesGlobales;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeDirectorActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    MaterialCardView chats;
    MaterialCardView cambiarPerfil;
    MaterialCardView cambiarEscuela;
    MaterialCardView cerrarSesion;
    CircleImageView fotoEscuela;
    CircleImageView fotoDirector;
    MaterialTextView nombreEscuela;
    MaterialTextView nombreDirector;
    private FirebaseFirestore db;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_director);
        context = this;
        firebaseAuth = FirebaseAuth.getInstance();
        chats = (MaterialCardView) findViewById(R.id.cv_home_alumno_chats);
        cambiarPerfil = (MaterialCardView) findViewById(R.id.cv_home_alumno_cambiar_perfil);
        cambiarEscuela = (MaterialCardView) findViewById(R.id.cv_home_alumno_cambiar_escuela);
        cerrarSesion = (MaterialCardView) findViewById(R.id.cv_home_alumno_cerrar_sesion);
        fotoEscuela = (CircleImageView) findViewById(R.id.iv_home_alumno_escuela_photo);
        fotoDirector = (CircleImageView) findViewById(R.id.iv_home_alumno_perfil_photo);
        nombreDirector = (MaterialTextView) findViewById(R.id.tv_home_alumno_nombre);
        nombreEscuela = (MaterialTextView) findViewById(R.id.tv_home_director_nombre_escuela);
        db = FirebaseFirestore.getInstance();
        setImagesAndNames();
        setOnClickListenners();
    }

    private void setImagesAndNames() {
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        db.collection("escuelas").document(sharedPreferences.getEscuelaSeleccionada()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Escuela escuela = documentSnapshot.toObject(Escuela.class);
                Picasso.with(context).load(escuela.getUrlLogo())
                        .fit().centerCrop().into(fotoEscuela);
                nombreEscuela.setText(escuela.getNombre());
            }
        });
        db.collection("perfilesSociales").document(sharedPreferences.getPerfilLogueadoId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PerfilSocial perfil = documentSnapshot.toObject(PerfilSocial.class);
                nombreDirector.setText(perfil.getNombre() + " " + perfil.getPrimerApellido() + " " + perfil.getSegundoApellido());
                if(perfil.getUrlImagen() != null){
                    Picasso.with(context).load(perfil.getUrlImagen())
                            .fit().centerCrop().into(fotoDirector);
                }
                else{
                    fotoDirector.setImageDrawable(getDrawable(R.drawable.no_profile_icon));
                }
            }
        });
    }

    private void setOnClickListenners() {
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaMensajeria();
            }
        });
        cambiarEscuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirEscuela();
            }
        });
        cambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirPerfil();
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //doNothing
    }

    private void cerrarSesion() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(HomeDirectorActivity.this, "no ai nadie xd", Toast.LENGTH_LONG).show();
        } else {
            VariablesGlobales shared = new VariablesGlobales(this);
            shared.setEscuelaSeleccionada(null);
            shared.setPerfilLogueadoId(null);
            FirebaseAuth.getInstance().signOut();
            Context context = HomeDirectorActivity.this;
            Intent goToLogin = new Intent(context, LoginActivity.class);
            startActivity(goToLogin);
        }
    }

    private void elegirPerfil() {
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        sharedPreferences.setPerfilLogueadoId(null);
        Context context = HomeDirectorActivity.this;
        Intent goToElegirPerfil = new Intent(context, ListSocialProfileActivity.class);
        startActivity(goToElegirPerfil);
    }

    private void elegirEscuela() {
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        sharedPreferences.setPerfilLogueadoId(null);
        sharedPreferences.setEscuelaSeleccionada(null);
        Context context = HomeDirectorActivity.this;
        Intent goToElegirEscuela = new Intent(context, ListEscuelaActivity.class);
        startActivity(goToElegirEscuela);
    }

    private void vistaMensajeria() {
        Context context = HomeDirectorActivity.this;
        Class destinationActivity = MessageActivityShow.class;
        Intent startMessageActivityIntent = new Intent(context, destinationActivity);
        startActivity(startMessageActivityIntent);
    }

    private void solicitarEscuela() {
        Context context = HomeDirectorActivity.this;
        Class destinationActivity = NewEscuelaActivity.class;
        Intent startNewEscuelaActivityIntent = new Intent(context, destinationActivity);
        startActivity(startNewEscuelaActivityIntent);
    }


    private void escuelasAAceptar() {
        Context context = HomeDirectorActivity.this;
        Class destinationActivity = AppRejectEscuelaActivity.class;
        Intent startAppRejectEscuelaActivityIntent = new Intent(context, destinationActivity);
        startActivity(startAppRejectEscuelaActivityIntent);
    }
}
