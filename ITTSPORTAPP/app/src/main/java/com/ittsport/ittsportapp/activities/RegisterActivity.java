package com.ittsport.ittsportapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ittsport.ittsportapp.R;

public class RegisterActivity extends AppCompatActivity {

    EditText correo_electronico;
    EditText passwordLogin;
    Button buttonLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        correo_electronico = findViewById(R.id.correo_electronico);
        passwordLogin = findViewById(R.id.contraseña);
        buttonLogin = findViewById(R.id.boton_iniciar_sesion);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = correo_electronico.getText().toString();
                String password = passwordLogin.getText().toString();

                if(TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getBaseContext(), "Debe insertar un email correctamente", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password) || password.length()<=4){
                    Toast.makeText(getBaseContext(), "Debe insertar su contraseña de 5 caracteres como mínimo", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Se ha registrado usted no mas", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "KAPPISIMA NO TE REGISTRASTE AJAJASADKOAS", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
    public void validate(){

    };

    public void login(){

    };
}
