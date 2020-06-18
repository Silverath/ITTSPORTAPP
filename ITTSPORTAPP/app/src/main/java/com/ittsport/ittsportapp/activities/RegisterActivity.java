package com.ittsport.ittsportapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.utils.AcceptDialog;
import com.ittsport.ittsportapp.utils.ErrorDialog;
import com.ittsport.ittsportapp.utils.LoadingDialog;

import javax.annotation.Nonnull;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    EditText correoElectronico;
    EditText password;
    EditText confirmPassword;
    Button buttonRegister;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ImageView arrowBack;
    LoadingDialog loadingDialog;
    AcceptDialog acceptDialog = new AcceptDialog(this);
    ErrorDialog errorDialog = new ErrorDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadingDialog = new LoadingDialog(this);
        arrowBack = (ImageView) findViewById(R.id.arrow_back_register);
        correoElectronico = (EditText) findViewById(R.id.et_correo_electronico);
        password = (EditText) findViewById(R.id.et_contraseña);
        confirmPassword = (EditText) findViewById(R.id.et_repetir_contraseña);
        buttonRegister = (Button) findViewById(R.id.btn_registro);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                String email = correoElectronico.getText().toString();
                String passwordString = password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                if(validate(email, passwordString, confirmPass)){
                    register(email, passwordString);
                }

            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public boolean validate(String email, String passwordString, String confirmPass){
        boolean res = true;
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(passwordString) || TextUtils.isEmpty(confirmPass)){
            loadingDialog.dismissDialog();
            Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
            res = false;
        }

        if(!passwordString.equals(confirmPass)) {
            loadingDialog.dismissDialog();
            Toast.makeText(getBaseContext(), "La contraseña debe coincidir", Toast.LENGTH_SHORT).show();
            res = false;
        }

        return res;
    };

    public void register(final String email, final String passwordString){
        firebaseAuth.createUserWithEmailAndPassword(email, passwordString).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                loadingDialog.dismissDialog();
                acceptDialog.startAcceptDialog("Se ha registrado correctamente.");
                onBackPressed();
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
                try {
                    throw e;
                } catch(FirebaseAuthWeakPasswordException ex) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(RegisterActivity.this, "Debe insertar su contraseña de 5 caracteres como mínimo", Toast.LENGTH_SHORT).show();
                } catch(FirebaseAuthInvalidCredentialsException ex) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(RegisterActivity.this, "Email no válido", Toast.LENGTH_SHORT).show();
                } catch(FirebaseAuthUserCollisionException ex) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(RegisterActivity.this, "Ya hay una cuenta registrada con este Email", Toast.LENGTH_SHORT).show();
                } catch(Exception ex) {
                    loadingDialog.dismissDialog();
                    errorDialog.startErrorDialog("Ha habido un error inesperado.");
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    };
}

