package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.models.Rol;
import com.ittsport.ittsportapp.utils.AcceptDialog;
import com.ittsport.ittsportapp.utils.ErrorDialog;
import com.ittsport.ittsportapp.utils.LoadingDialog;
import com.ittsport.ittsportapp.utils.VariablesGlobales;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;

public class InscripcionAlumnoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private RadioButton rb_rol_alumno;
    private RadioButton rb_rol_entrenador;
    private EditText nombre;
    private EditText primerApellido;
    private EditText segundoApellido;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Button createProfile;
    private Button btnNuevaImagenPerfil;
    private ImageView ivNuevaImagenPerfil;
    private CircleImageView logo;
    private Uri uriImagenPerfil;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Context context;
    private LoadingDialog loadingDialog;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion_alumno);
        context = this;
        loadingDialog = new LoadingDialog(this);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile images");
        back = (ImageView) findViewById(R.id.arrow_back_inscripcion_alumno);
        rb_rol_alumno = (RadioButton) findViewById(R.id.rb_rol_alumno);
        rb_rol_entrenador = (RadioButton) findViewById(R.id.rb_rol_entrenador);
        nombre = findViewById(R.id.et_nueva_inscripcion_perfil_social_nombre);
        primerApellido = findViewById(R.id.et_nueva_inscripcion_perfil_social_primer_apellido);
        segundoApellido = findViewById(R.id.et_nueva_inscripcion_perfil_social_segundo_apellido);
        createProfile = findViewById(R.id.btn_nueva_inscripcion_crear_perfil_social);
        btnNuevaImagenPerfil = findViewById(R.id.btn_nueva_inscripcion_nueva_foto_perfil);
        ivNuevaImagenPerfil = findViewById(R.id.iv_nueva_inscripcion_foto_perfil);
        AcceptDialog acceptDialog = new AcceptDialog(this);
        ErrorDialog errorDialog = new ErrorDialog(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rol = "";
                if(rb_rol_alumno.isChecked()){
                    rol = Rol.ALUMNO.toString();
                } else if(rb_rol_entrenador.isChecked()){
                    rol = Rol.ENTRENADOR.toString();
                }
                if (nombre.getText().toString().equals("") || primerApellido.getText().toString().equals("") || segundoApellido.getText().toString().equals("") || rol.equals("")) {
                    Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
                } else if (rol.equals(Rol.ENTRENADOR.toString()) && uriImagenPerfil == null){
                    Toast.makeText(getBaseContext(), "El entrenador tiene que tener foto de perfil", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.startLoadingDialog();
                    if (uriImagenPerfil != null) {
                        VariablesGlobales sharedPreferences = new VariablesGlobales(context);
                        String cuentaUsuarioId = firebaseAuth.getCurrentUser().getUid();
                        String nombreCompleto = nombre.getText().toString() + primerApellido.getText().toString() + segundoApellido.getText().toString();
                        String escuelaId = sharedPreferences.getEscuelaParaInscribirse();
                        final PerfilSocial nuevo = new PerfilSocial(nombre.getText().toString(), primerApellido.getText().toString(), segundoApellido.getText().toString(), nombreCompleto, cuentaUsuarioId, Estado.PENDIENTE, Rol.valueOf(rol), escuelaId);
                        Map<String, Object> nuevoPerfil = new HashMap<>();
                        nuevoPerfil.put("nombre", nuevo.getNombre());
                        nuevoPerfil.put("primerApellido", nuevo.getPrimerApellido());
                        nuevoPerfil.put("segundoApellido", nuevo.getSegundoApellido());
                        nuevoPerfil.put("cuentaUsuarioId", nuevo.getCuentaUsuarioId());
                        nuevoPerfil.put("nombreImagen", nuevo.getNombreImagen());
                        nuevoPerfil.put("estado", nuevo.getEstado());
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
                                                            loadingDialog.dismissDialog();
                                                            acceptDialog.startAcceptDialog("Alumno inscrito, espere a ser aceptado por su director.");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@Nonnull Exception e) {
                                                            loadingDialog.dismissDialog();
                                                            errorDialog.startErrorDialog("Ha habido un problema al inscribir al alumno.");
                                                        }
                                                    });
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@Nonnull Exception e) {
                                        loadingDialog.dismissDialog();
                                        errorDialog.startErrorDialog("Ha habido un problema al guardar la foto.");
                                    }
                                });
                    } else {
                        String cuentaUsuarioId = firebaseAuth.getCurrentUser().getUid();
                        VariablesGlobales sharedPreferences = new VariablesGlobales(context);
                        String escuelaId = sharedPreferences.getEscuelaParaInscribirse();
                        final PerfilSocial nuevo = new PerfilSocial(nombre.getText().toString(), primerApellido.getText().toString(), segundoApellido.getText().toString(), cuentaUsuarioId, Estado.PENDIENTE, Rol.ALUMNO, escuelaId);
                        Map<String, Object> nuevoPerfil = new HashMap<>();
                        nuevoPerfil.put("nombre", nuevo.getNombre());
                        nuevoPerfil.put("primerApellido", nuevo.getPrimerApellido());
                        nuevoPerfil.put("segundoApellido", nuevo.getSegundoApellido());
                        nuevoPerfil.put("cuentaUsuarioId", nuevo.getCuentaUsuarioId());
                        nuevoPerfil.put("estado", nuevo.getEstado());
                        nuevoPerfil.put("rol", nuevo.getRol());
                        nuevoPerfil.put("escuelaId", nuevo.getEscuelaId());
                        db.collection("perfilesSociales").document()
                                .set(nuevoPerfil)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadingDialog.dismissDialog();
                                        acceptDialog.startAcceptDialog("Alumno inscrito, espere a ser aceptado por su director.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@Nonnull Exception e) {
                                        loadingDialog.dismissDialog();
                                        errorDialog.startErrorDialog("Ha habido un problema al inscribir al alumno.");
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
