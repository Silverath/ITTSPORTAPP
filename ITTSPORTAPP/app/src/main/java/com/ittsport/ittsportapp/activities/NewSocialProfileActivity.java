package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.models.Rol;
import com.ittsport.ittsportapp.utils.VariablesGlobales;
import com.squareup.picasso.Picasso;

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
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnNuevaImagenPerfil;
    private ImageView ivNuevaImagenPerfil;
    private Uri uriImagenPerfil;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_social_profile);
        context = this;
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile images");
        databaseReference = FirebaseDatabase.getInstance().getReference("profile images");
        nombre = (EditText) findViewById(R.id.et_perfil_social_nombre);
        primerApellido = (EditText) findViewById(R.id.et_perfil_social_primer_apellido);
        segundoApellido = (EditText) findViewById(R.id.et_perfil_social_segundo_apellido);
        createProfile = (Button) findViewById(R.id.btn_crear_perfil_social);
        btnNuevaImagenPerfil = (Button) findViewById(R.id.btn_nueva_foto_perfil);
        ivNuevaImagenPerfil = (ImageView) findViewById(R.id.iv_nueva_foto_perfil);

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombre.getText().toString().equals("") || primerApellido.getText().toString().equals("") || segundoApellido.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
                } else {
                    if (uriImagenPerfil != null) {
                        VariablesGlobales sharedPreferences = new VariablesGlobales(context);
                        String cuentaUsuarioId = firebaseAuth.getCurrentUser().getUid();
                        String nombreCompleto = nombre.getText().toString() + primerApellido.getText().toString() + segundoApellido.getText().toString();
                        final PerfilSocial nuevo = new PerfilSocial(nombre.getText().toString(), primerApellido.getText().toString(), segundoApellido.getText().toString(), nombreCompleto, cuentaUsuarioId, Estado.PENDIENTE, Rol.ALUMNO, sharedPreferences.getEscuelaParaInscribirse());
                        Map<String, Object> nuevoPerfil = new HashMap<>();
                        nuevoPerfil.put("nombre", nuevo.getNombre());
                        nuevoPerfil.put("primerApellido", nuevo.getPrimerApellido());
                        nuevoPerfil.put("segundoApellido", nuevo.getSegundoApellido());
                        nuevoPerfil.put("cuentaUsuarioId", nuevo.getCuentaUsuarioId());
                        nuevoPerfil.put("nombreImagen", nuevo.getNombreImagen());
                        nuevoPerfil.put("estado", nuevo.getStatus());
                        nuevoPerfil.put("rol", nuevo.getRol());
                        nuevoPerfil.put("escuelaId", nuevo.getEscuelaId());

                        StorageReference fileReference = storageReference.child((nombreCompleto + "." + getFileExtension(uriImagenPerfil)));
                        fileReference.putFile(uriImagenPerfil)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                nuevoPerfil.put("urlImagen", uri.toString());
                                                db.collection("perfilesSociales").document()
                                                    .set(nuevoPerfil)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getBaseContext(), "Perfil social creado", Toast.LENGTH_SHORT).show();
                                                            Intent returnIntent = new Intent();
                                                            returnIntent.putExtra("perfilSocial", nuevo);
                                                            setResult(Activity.RESULT_OK, returnIntent);
                                                            finish();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getBaseContext(), "Ha habido un problema al crear el perfil", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), "Ha habido un problema al guardar la foto", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        VariablesGlobales sharedPreferences = new VariablesGlobales(context);
                        String cuentaUsuarioId = firebaseAuth.getCurrentUser().getUid();
                        final PerfilSocial nuevo = new PerfilSocial(nombre.getText().toString(), primerApellido.getText().toString(), segundoApellido.getText().toString(), cuentaUsuarioId, Estado.PENDIENTE, Rol.ALUMNO, sharedPreferences.getEscuelaParaInscribirse());
                        Map<String, Object> nuevoPerfil = new HashMap<>();
                        nuevoPerfil.put("nombre", nuevo.getNombre());
                        nuevoPerfil.put("primerApellido", nuevo.getPrimerApellido());
                        nuevoPerfil.put("segundoApellido", nuevo.getSegundoApellido());
                        nuevoPerfil.put("cuentaUsuarioId", nuevo.getCuentaUsuarioId());
                        nuevoPerfil.put("estado", nuevo.getStatus());
                        nuevoPerfil.put("rol", nuevo.getRol());
                        nuevoPerfil.put("escuelaId", nuevo.getEscuelaId());
                        db.collection("perfilesSociales").document()
                                .set(nuevoPerfil)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String nombreCompleto = nuevo.getNombre() + nuevo.getPrimerApellido() + nuevo.getSegundoApellido();
                                        Toast.makeText(getBaseContext(), "Perfil social creado", Toast.LENGTH_SHORT).show();
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("perfilSocial", nuevo);
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), "Ha habido un problema al crear el perfil", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                }
            }
        });

        btnNuevaImagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriImagenPerfil = data.getData();
            Picasso.with(this).load(uriImagenPerfil).into(ivNuevaImagenPerfil);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
