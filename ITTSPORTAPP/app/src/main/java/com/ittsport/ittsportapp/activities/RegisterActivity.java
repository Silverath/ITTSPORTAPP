package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.models.CuentaUsuario;
import com.ittsport.ittsportapp.models.Rol;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    EditText correoElectronico;
    EditText password;
    EditText confirmPassword;
    Button buttonRegister;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ImageView arrowBack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        arrowBack = (ImageView) findViewById(R.id.arrow_back);
        correoElectronico = (EditText) findViewById(R.id.et_correo_electronico);
        password = (EditText) findViewById(R.id.et_contraseña);
        confirmPassword = (EditText) findViewById(R.id.et_repetir_contraseña);
        buttonRegister = (Button) findViewById(R.id.btn_registro);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_register);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
            Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
            res = false;
        }

        if(!passwordString.equals(confirmPass)) {
            Toast.makeText(getBaseContext(), "La contraseña debe coincidir", Toast.LENGTH_SHORT).show();
            res = false;
        }

        return res;
    };

    public void register(final String email, final String passwordString){
        firebaseAuth.createUserWithEmailAndPassword(email, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final Context context = RegisterActivity.this;


                    CuentaUsuario account = new CuentaUsuario(email, passwordString, Rol.ALUMNO);
                    Map<String, Object> newAccount = new HashMap<>();
                    newAccount.put("email", account.getEmail());
                    newAccount.put("contraseña", account.getPassword());
                    newAccount.put("rol", account.getRol());

                    db.collection("grupos").document(account.getEmail()).set(newAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                            Intent startLoginActivityClass = new Intent(context, MainActivity.class);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            startActivity(startLoginActivityClass);
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(RegisterActivity.this, "Ha habido un error", Toast.LENGTH_SHORT).show();
                                }
                            });


                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(RegisterActivity.this, "Debe insertar su contraseña de 5 caracteres como mínimo", Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(RegisterActivity.this, "Email no válido", Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(RegisterActivity.this, "Ya hay una cuenta registrada con este Email", Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });

    };

    public void crearCuentaUsuario(String email, String password){

    }
}

