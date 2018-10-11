package com.ittsport.ittsportapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ittsport.ittsportapp.R;

public class LoginActivity extends AppCompatActivity {

    EditText emailLogin;
    EditText passwordLogin;
    Button buttonLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        buttonLogin = findViewById(R.id.button_login);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getBaseContext(), "Debe insertar un email correctamente", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getBaseContext(), "Debe insertar su contraseña", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Se ha registrado usted no mas", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "KAPPISIMA NO TE REGISTRASTE AJAJASADKOAS", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
