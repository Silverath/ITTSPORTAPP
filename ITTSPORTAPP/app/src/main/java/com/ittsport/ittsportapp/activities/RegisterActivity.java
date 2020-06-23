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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.utils.LoadingDialog;

import javax.annotation.Nonnull;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout correoElectronico;
    TextInputLayout password;
    TextInputLayout confirmPassword;
    Button buttonRegister;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ImageView arrowBack;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadingDialog = new LoadingDialog(this);
        arrowBack = (ImageView) findViewById(R.id.arrow_back_register);
        correoElectronico = (TextInputLayout) findViewById(R.id.til_register_correo_electronico);
        password = (TextInputLayout) findViewById(R.id.til_register_contraseña);
        confirmPassword = (TextInputLayout) findViewById(R.id.til_register_contraseña_confirm);
        buttonRegister = (Button) findViewById(R.id.btn_registro);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                String email = correoElectronico.getEditText().getText().toString();
                String passwordString = password.getEditText().getText().toString();
                String confirmPass = confirmPassword.getEditText().getText().toString();

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
        if(TextUtils.isEmpty(email)){
            loadingDialog.dismissDialog();
            correoElectronico.setError("Falta el correo electrónico");
            res = false;
        }
        else if(TextUtils.isEmpty(passwordString)){
            loadingDialog.dismissDialog();
            password.setError("Falta la contraseña");
            res = false;
        }

        else if(TextUtils.isEmpty(confirmPass)){
            loadingDialog.dismissDialog();
            confirmPassword.setError("Debe de confirmar la contraseña");
            res = false;
        }

        if(!passwordString.equals(confirmPass)) {
            loadingDialog.dismissDialog();
            this.confirmPassword.setError("La contraseña debe coincidir");
            res = false;
        }

        return res;
    }

    public void register(final String email, final String passwordString){
        firebaseAuth.createUserWithEmailAndPassword(email, passwordString).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
                onBackPressed();
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
                try {
                    throw e;
                } catch(FirebaseAuthWeakPasswordException ex) {
                    loadingDialog.dismissDialog();
                    password.setError("Debe insertar su contraseña de 5 caracteres como mínimo");
                } catch(FirebaseAuthInvalidCredentialsException ex) {
                    loadingDialog.dismissDialog();
                    correoElectronico.setError("Email no válido");
                } catch(FirebaseAuthUserCollisionException ex) {
                    loadingDialog.dismissDialog();
                    correoElectronico.setError("Ya hay una cuenta registrada con este Email");
                } catch(Exception ex) {
                    loadingDialog.dismissDialog();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    };
}

