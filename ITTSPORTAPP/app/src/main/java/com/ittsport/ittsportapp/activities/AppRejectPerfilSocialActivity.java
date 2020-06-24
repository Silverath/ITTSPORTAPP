package com.ittsport.ittsportapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.AppRejectEscuelaAdapter;
import com.ittsport.ittsportapp.adapters.AppRejectPerfilSocialAdapter;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.models.Rol;
import com.ittsport.ittsportapp.utils.LoadingDialog;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.util.ArrayList;

public class AppRejectPerfilSocialActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ArrayList<PerfilSocial> perfilesSocialesToAppReject;

    public static RecyclerView mRecyclerView;

    public static AppRejectPerfilSocialAdapter appRejectPerfilSocialAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_reject_perfil_social);

        perfilesSocialesToAppReject = new ArrayList<PerfilSocial>();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_perfiles_sociales_to_manage);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        perfilesPendientes();
    }

    private void perfilesPendientes(){
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        CollectionReference collRef = db.collection("perfilesSociales");
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);

        collRef.whereEqualTo("estado", Estado.PENDIENTE)
                .whereEqualTo("escuelaId", sharedPreferences.getEscuelaSeleccionada())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                    PerfilSocial perfilSocial = new PerfilSocial();
                    perfilSocial.setNombre(q.get("nombre").toString());
                    perfilSocial.setPrimerApellido(q.get("primerApellido").toString());
                    perfilSocial.setSegundoApellido(q.get("segundoApellido").toString());
                    perfilSocial.setUrlImagen(q.get("urlImagen").toString());
                    perfilSocial.setCuentaUsuarioId(q.get("cuentaUsuarioId").toString());
                    perfilSocial.setEscuelaId(q.get("escuelaId").toString());
                    perfilSocial.setEstado(Estado.valueOf(q.get("estado").toString()));
                    perfilSocial.setRol(Rol.valueOf(q.get("rol").toString()));
                    perfilesSocialesToAppReject.add(perfilSocial);
                }
                appRejectPerfilSocialAdapter = new AppRejectPerfilSocialAdapter(AppRejectPerfilSocialActivity.this, perfilesSocialesToAppReject);
                mRecyclerView.setAdapter(appRejectPerfilSocialAdapter);
                loadingDialog.dismissDialog();
            }
        });
    }
}
