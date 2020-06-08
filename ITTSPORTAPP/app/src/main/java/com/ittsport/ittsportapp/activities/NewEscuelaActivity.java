package com.ittsport.ittsportapp.activities;

import android.app.Activity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.Escuela;
import com.ittsport.ittsportapp.models.Estado;
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
    private Button btnLogoEscuela;
    private ImageView ivLogoEscuela;
    private Uri uriLogoEscuela;
    private StorageReference storageReference;
    LoadingDialog loadingDialog = new LoadingDialog(this);

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

        solicitarEscuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nombre.getText().toString().equals("") || direccion.getText().toString().equals("") ||
                        municipio.getText().toString().equals("") || provincia.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.startLoadingDialog();
                    final Escuela nueva;
                    if(uriLogoEscuela != null){
                        nueva = new Escuela(nombre.getText().toString(), nombre.getText().toString()
                                , uriLogoEscuela.toString(), direccion.getText().toString(), provincia.getText().toString()
                                , municipio.getText().toString(), Estado.PENDIENTE);
                    } else {
                        nueva = new Escuela(nombre.getText().toString(), null
                                , null, direccion.getText().toString(), provincia.getText().toString()
                                , municipio.getText().toString(), Estado.PENDIENTE);
                    }
                    final Map<String, Object> nuevaEscuela = new HashMap<>();
                    nuevaEscuela.put("nombre", nueva.getNombre());
                    nuevaEscuela.put("nombreLogo", nueva.getNombreLogo());
                    nuevaEscuela.put("urlLogo", nueva.getUrlLogo());
                    nuevaEscuela.put("direccion", nueva.getDireccion());
                    nuevaEscuela.put("provincia", nueva.getProvincia());
                    nuevaEscuela.put("municipio", nueva.getMunicipio());
                    nuevaEscuela.put("status", nueva.getStatus());

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
                                                            loadingDialog.dismissDialog();
                                                            Toast.makeText(getBaseContext(), "Escuela creada, le notificaremos con la respuesta"
                                                                    , Toast.LENGTH_SHORT).show();
                                                            Intent returnIntent = new Intent();
                                                            returnIntent.putExtra("nuevaEscuela", nueva);
                                                            setResult(Activity.RESULT_OK, returnIntent);
                                                            finish();
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
                && data != null && data.getData() != null) {
            uriLogoEscuela = data.getData();
            Picasso.with(this).load(uriLogoEscuela).into(ivLogoEscuela);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
