package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditPerfilSocialActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputEditText nombre;
    private TextInputEditText primerApellido;
    private TextInputEditText segundoApellido;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Button editProfile;
    private Button btnImagenPerfil;
    private ImageView ivImagenPerfil;
    private CircleImageView logo;
    private Uri uriImagenPerfil;
    private StorageReference storageReference;
    private Context context;
    private LoadingDialog loadingDialog;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_social_profile);
        context = this;
        loadingDialog = new LoadingDialog(this);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile images");
        back = (ImageView) findViewById(R.id.arrow_back_editar_perfil);
        nombre = findViewById(R.id.et_editar_perfil_social_nombre);
        primerApellido = findViewById(R.id.et_editar_perfil_social_primer_apellido);
        segundoApellido = findViewById(R.id.et_editar_perfil_social_segundo_apellido);
        editProfile = findViewById(R.id.btn_editar_perfil_social);
        btnImagenPerfil = findViewById(R.id.btn_editar_foto_perfil);
        ivImagenPerfil = findViewById(R.id.iv_editar_perfil_foto);
        setFields();
        AcceptDialog acceptDialog = new AcceptDialog(this);
        ErrorDialog errorDialog = new ErrorDialog(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombre.getText().toString().isEmpty() || nombre.getText().toString() == null) {
                    nombre.setError("No puede estar vacío");
                }
                else if(primerApellido.getText().toString().isEmpty() || primerApellido.getText().toString() == null){
                    primerApellido.setError("No puede estar vacío");
                }
                else if(segundoApellido.getText().toString().isEmpty() || segundoApellido.getText().toString() == null){
                    segundoApellido.setError("No puede estar vacío");
                }else {
                    loadingDialog.startLoadingDialog();
                    if (uriImagenPerfil != null) {
                        VariablesGlobales sharedPreferences = new VariablesGlobales(context);
                        String cuentaUsuarioId = firebaseAuth.getCurrentUser().getUid();
                        String nombreCompleto = nombre.getText().toString() + primerApellido.getText().toString() + segundoApellido.getText().toString();
                        String escuelaId = sharedPreferences.getEscuelaParaInscribirse();
                        final PerfilSocial nuevo = new PerfilSocial(nombre.getText().toString(), primerApellido.getText().toString(), segundoApellido.getText().toString(), nombreCompleto, cuentaUsuarioId, Estado.ACEPTADO, Rol.ALUMNO, escuelaId);
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
                                                db.collection("perfilesSociales").document(sharedPreferences.getPerfilLogueadoId())
                                                        .set(nuevoPerfil)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                loadingDialog.dismissDialog();
                                                                acceptDialog.startAcceptDialog("Perfil social cambiado con éxito.");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@Nonnull Exception e) {
                                                                loadingDialog.dismissDialog();
                                                                errorDialog.startErrorDialog("Ha habido un problema al crear el perfil.");
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
                        final PerfilSocial nuevo = new PerfilSocial(nombre.getText().toString(), primerApellido.getText().toString(), segundoApellido.getText().toString(), cuentaUsuarioId, Estado.ACEPTADO, Rol.ALUMNO, escuelaId);
                        Map<String, Object> nuevoPerfil = new HashMap<>();
                        nuevoPerfil.put("nombre", nuevo.getNombre());
                        nuevoPerfil.put("primerApellido", nuevo.getPrimerApellido());
                        nuevoPerfil.put("segundoApellido", nuevo.getSegundoApellido());
                        nuevoPerfil.put("cuentaUsuarioId", nuevo.getCuentaUsuarioId());
                        nuevoPerfil.put("estado", nuevo.getEstado());
                        nuevoPerfil.put("rol", nuevo.getRol());
                        nuevoPerfil.put("escuelaId", nuevo.getEscuelaId());
                        db.collection("perfilesSociales").document(sharedPreferences.getPerfilLogueadoId())
                                .set(nuevoPerfil, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String nombreCompleto = nuevo.getNombre() + nuevo.getPrimerApellido() + nuevo.getSegundoApellido();
                                        loadingDialog.dismissDialog();
                                        acceptDialog.startAcceptDialog("Perfil social cambiado con éxito.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@Nonnull Exception e) {
                                        loadingDialog.dismissDialog();
                                        errorDialog.startErrorDialog("Ha habido un problema al crear el perfil.");
                                    }
                                });
                    }

                }
            }
        });

        btnImagenPerfil.setOnClickListener(new View.OnClickListener() {
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
            Picasso.with(this).load(uriImagenPerfil).into(ivImagenPerfil);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void setFields(){
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        db.collection("perfilesSociales").document(sharedPreferences.getPerfilLogueadoId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PerfilSocial perfil = documentSnapshot.toObject(PerfilSocial.class);
                nombre.setText(perfil.getNombre());
                primerApellido.setText(perfil.getPrimerApellido());
                segundoApellido.setText(perfil.getSegundoApellido());
                if(perfil.getUrlImagen() != null){
                    Picasso.with(context).load(perfil.getUrlImagen()).fit().centerCrop().into(ivImagenPerfil);
                }
            }
        });
    }
}
