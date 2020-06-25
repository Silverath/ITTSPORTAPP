package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.ChooseEntrenadorAdapter;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.models.Rol;
import com.ittsport.ittsportapp.utils.LoadingDialog;
import com.ittsport.ittsportapp.utils.VariablesGlobales;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CrearGrupoActivity extends AppCompatActivity {

    TextInputLayout nombreGrupo;
    TextInputLayout horarioGrupo;
    RecyclerView rvEntrenadores;
    FloatingActionButton crearGrupo;
    LoadingDialog loadingDialog;
    List<PerfilSocial> entrenadores;
    FirebaseFirestore db;
    ChooseEntrenadorAdapter chooseEntrenadorAdapter;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        loadingDialog = new LoadingDialog(this);
        context = this;
        db = FirebaseFirestore.getInstance();
        loadingDialog.startLoadingDialog();
        nombreGrupo = findViewById(R.id.til_crear_grupo_nombre);
        horarioGrupo = findViewById(R.id.til_crear_grupo_horario);
        crearGrupo = findViewById(R.id.btn_crear_grupo);
        rvEntrenadores = findViewById(R.id.rv_crear_grupo_entrenadores);
        entrenadores = new ArrayList<>();
        setRVListEntrenadores();
        setCrearGrupo();

    }

    private void setCrearGrupo(){
        crearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombreGrupo.getEditText().getText().toString().isEmpty()){
                    nombreGrupo.setError("Debe de poner el nombre del grupo");
                }
                else if(horarioGrupo.getEditText().getText().toString().isEmpty()){
                    horarioGrupo.setError("Debe de poner el horario del grupo");
                }
                else if(chooseEntrenadorAdapter.getLastSelected() == null){
                    Toast.makeText(context, "Debe de seleccionar el entrenador", Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
        });
    }

    private void setRVListEntrenadores(){
        rvEntrenadores.setHasFixedSize(true);
        rvEntrenadores.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvEntrenadores.getContext(),
                llm.getOrientation());
        rvEntrenadores.addItemDecoration(dividerItemDecoration);
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        db.collection("perfilesSociales").document(sharedPreferences.getPerfilLogueadoId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    PerfilSocial director = documentSnapshot.toObject(PerfilSocial.class);
                    entrenadores.add(director);
                    db.collection("perfilesSociales").whereEqualTo("escuelaId", sharedPreferences.getEscuelaSeleccionada()).whereEqualTo("rol", Rol.ENTRENADOR).whereEqualTo("estado", Estado.ACEPTADO).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                if(documentSnapshot.exists()){
                                    PerfilSocial perfilSocial = documentSnapshot.toObject(PerfilSocial.class);
                                    entrenadores.add(perfilSocial);
                                }
                            }
                            chooseEntrenadorAdapter = new ChooseEntrenadorAdapter(entrenadores, context);
                            rvEntrenadores.setAdapter(chooseEntrenadorAdapter);
                            loadingDialog.dismissDialog();
                        }
                    });
                }
            }
        });
    }
}
