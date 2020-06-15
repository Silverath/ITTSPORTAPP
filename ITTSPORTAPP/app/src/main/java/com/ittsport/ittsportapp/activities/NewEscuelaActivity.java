package com.ittsport.ittsportapp.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.Rol;
import com.ittsport.ittsportapp.utils.AcceptDialog;
import com.ittsport.ittsportapp.utils.ErrorDialog;
import com.ittsport.ittsportapp.utils.LoadingDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class NewEscuelaActivity extends AppCompatActivity {

    EditText nombre;
    EditText direccion;
    EditText municipio;
    EditText provincia;
    FirebaseFirestore db;
    Button solicitarEscuela;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int imagenPerfil = 0;
    int logoPerfil = 0;
    private Button btnLogoEscuela;
    private ImageView ivLogoEscuela;
    private Uri uriLogoEscuela;
    EditText nombrePerfil;
    EditText primerApellido;
    EditText segundoApellido;
    Button btnPerfilDirector;
    ImageView ivPerfilDirector;
    Uri uriPerfilDirector;
    private StorageReference storageReference;
    LoadingDialog loadingDialog = new LoadingDialog(this);
    AcceptDialog acceptDialog = new AcceptDialog(this);
    ErrorDialog errorDialog = new ErrorDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_escuela);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("logo images");
        nombre = (EditText) findViewById(R.id.et_escuela_nombre);
        direccion = (EditText) findViewById(R.id.et_escuela_direccion);
        municipio = (EditText) findViewById(R.id.et_escuela_municipio);
        provincia = (EditText) findViewById(R.id.et_escuela_provincia);
        solicitarEscuela = (Button) findViewById(R.id.btn_solicitar_escuela);
        btnLogoEscuela = (Button) findViewById(R.id.btn_escuela_logo);
        ivLogoEscuela = (ImageView) findViewById(R.id.iv_escuela_logo);
        nombrePerfil = (EditText) findViewById(R.id.et_perfil_social_nombre);
        primerApellido = (EditText) findViewById(R.id.et_perfil_social_primer_apellido);
        segundoApellido = (EditText) findViewById(R.id.et_perfil_social_segundo_apellido);
        btnPerfilDirector = (Button) findViewById(R.id.btn_perfil_imagen);
        ivPerfilDirector = (ImageView) findViewById(R.id.iv_perfil_imagen);


        solicitarEscuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nombre.getText().toString().equals("") || direccion.getText().toString().equals("") ||
                        municipio.getText().toString().equals("") || provincia.getText().toString().equals("") || uriLogoEscuela == null
                || uriPerfilDirector == null || nombrePerfil.toString().equals("") || primerApellido.toString().equals("")
                 || segundoApellido.toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.startLoadingDialog();
                    final Escuela nueva;
                    if(uriLogoEscuela != null){
                        nueva = new Escuela(nombre.getText().toString(), nombre.getText().toString()
                                , uriLogoEscuela.toString(), direccion.getText().toString(), provincia.getText().toString()
                                , municipio.getText().toString(), Estado.PENDIENTE.toString());
                    } else {
                        nueva = new Escuela(nombre.getText().toString(), null
                                , null, direccion.getText().toString(), provincia.getText().toString()
                                , municipio.getText().toString(), Estado.PENDIENTE.toString());
                    }
                    final Map<String, Object> nuevaEscuela = new HashMap<>();
                    nuevaEscuela.put("nombre", nueva.getNombre());
                    nuevaEscuela.put("nombreLogo", nueva.getNombreLogo());
                    nuevaEscuela.put("urlLogo", nueva.getUrlLogo());
                    nuevaEscuela.put("direccion", nueva.getDireccion());
                    nuevaEscuela.put("provincia", nueva.getProvincia());
                    nuevaEscuela.put("municipio", nueva.getMunicipio());
                    nuevaEscuela.put("estado", nueva.getEstado());

                    StorageReference fileReference = storageReference.child((nombre + "." + getFileExtension(uriLogoEscuela)));
                    fileReference.putFile(uriLogoEscuela)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            uriLogoEscuela = uri;
                                            nuevaEscuela.put("urlLogo", uriLogoEscuela.toString());
                                            db.collection("escuelas").document().set(nuevaEscuela)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            String nombreCompleto = nombrePerfil.getText().toString() + primerApellido.getText().toString() + segundoApellido.getText().toString();
                                                            Map<String, Object> perfilDirector = new HashMap<>();
                                                            perfilDirector.put("nombre", nombrePerfil.getText().toString());
                                                            perfilDirector.put("primerApellido", primerApellido.getText().toString());
                                                            perfilDirector.put("segundoApellido", segundoApellido.getText().toString());
                                                            perfilDirector.put("cuentaUsuarioId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            perfilDirector.put("nombreImagen", nombreCompleto);
                                                            perfilDirector.put("estado", Estado.PENDIENTE.toString());
                                                            perfilDirector.put("rol", Rol.DIRECTOR.toString());


                                                            StorageReference perfilReference = storageReference.child(nombreCompleto + "." + getFileExtension(uriPerfilDirector));
                                                            perfilReference.putFile(uriPerfilDirector)
                                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                            perfilReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                @Override
                                                                                public void onSuccess(Uri uri2) {
                                                                                    perfilDirector.put("urlImagen", uri2.toString());
                                                                                    Boolean wtf = uri.equals(uri2);
                                                                                    db.collection("escuelas").whereEqualTo("nombre",nueva.getNombre())
                                                                                            .whereEqualTo("provincia", nueva.getProvincia())
                                                                                            .whereEqualTo("municipio", nueva.getMunicipio())
                                                                                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                            String escuelaId = "";
                                                                                            for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                                                                                                escuelaId = q.getId();
                                                                                            }
                                                                                            perfilDirector.put("escuelaId", escuelaId);
                                                                                            db.collection("perfilesSociales").document()
                                                                                                    .set(perfilDirector).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    loadingDialog.dismissDialog();
                                                                                                    acceptDialog.startAcceptDialog("Escuela creada, le notificaremos con la respuesta");
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@Nonnull Exception e) {
                                                    loadingDialog.dismissDialog();
                                                    Toast.makeText(getBaseContext(), "Ha habido un problema guardando el logo.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            ;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@Nonnull Exception e) {
                                            loadingDialog.dismissDialog();
                                            Toast.makeText(getBaseContext(), "Ha habido un problema al crear la escuela", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                }
            }
        });

        btnLogoEscuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenPerfil = 0;
                openFileChooser();
            }
        });

        btnPerfilDirector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenPerfil = 1;
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null && imagenPerfil == 0) {
            uriLogoEscuela = data.getData();
            Picasso.with(this).load(uriLogoEscuela).into(ivLogoEscuela);
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null && imagenPerfil == 1) {
            uriPerfilDirector = data.getData();
            Picasso.with(this).load(uriPerfilDirector).into(ivPerfilDirector);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
