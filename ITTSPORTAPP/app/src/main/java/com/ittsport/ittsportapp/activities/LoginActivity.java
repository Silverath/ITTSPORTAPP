package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.utils.LoadingDialog;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    EditText correo_electronico;
    EditText passwordLogin;
    Button buttonLogin;
    TextView go_to_register;
    FirebaseAuth firebaseAuth;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingDialog = new LoadingDialog(this);
        correo_electronico = (EditText) findViewById(R.id.correo_electronico);
        passwordLogin = (EditText)findViewById(R.id.contraseña);
        buttonLogin = (Button) findViewById(R.id.boton_iniciar_sesion);
        go_to_register = (TextView) findViewById(R.id.go_to_register);



        firebaseAuth = FirebaseAuth.getInstance();

        go_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            loadingDialog.startLoadingDialog();
            String email = correo_electronico.getText().toString();
            String password = passwordLogin.getText().toString();
            if(validate(email, password)){
                login(email, password);
            }
            else{
                loadingDialog.dismissDialog();
            }
            }
        });
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        if(firebaseAuth.getCurrentUser() != null && sharedPreferences.getPerfilLogueadoId() != null && sharedPreferences.getEscuelaSeleccionada() != null){
            loadingDialog.dismissDialog();
            goTo(3);
        }
        else if(firebaseAuth.getCurrentUser() != null && sharedPreferences.getPerfilLogueadoId() == null && sharedPreferences.getEscuelaSeleccionada() != null){
            loadingDialog.dismissDialog();
            goTo(2);
        }
        else if(firebaseAuth.getCurrentUser() != null && sharedPreferences.getPerfilLogueadoId() == null && sharedPreferences.getEscuelaSeleccionada() == null){
            loadingDialog.dismissDialog();
            goTo(1);
        }
        else{
            loadingDialog.dismissDialog();
        }
    }

    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.dismissDialog();
                            goTo(1);

                        } else {
                            try {
                                loadingDialog.dismissDialog();
                                throw task.getException();
                            }
                            catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(LoginActivity.this, "Email o contraseña no válidos", Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthInvalidUserException e) {
                                Toast.makeText(LoginActivity.this, "No existe cuenta con este Email", Toast.LENGTH_SHORT).show();
                            }catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    public boolean validate(String email, String password){
        boolean res = true;
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            loadingDialog.dismissDialog();
            Toast.makeText(getBaseContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
            res = false;
        }

        return res;
    }

    public void goTo(Integer nextStep){
        if(nextStep == 3){
            Context context = LoginActivity.this;
            Intent startHomeActivityClass = new Intent(context, HomeActivity.class);
            startActivity(startHomeActivityClass);
        }
        else if(nextStep == 2){
            Context context = LoginActivity.this;
            Intent startListSocialProfileActivityClass = new Intent(context, ListSocialProfileActivity.class);
            startActivity(startListSocialProfileActivityClass);
        }
        else if(nextStep == 1){
            Context context = LoginActivity.this;
            Intent startListEscuelaActivityClass = new Intent(context, ListEscuelaActivity.class);
            startActivity(startListEscuelaActivityClass);
        }
    }

    public void goToRegisterActivity(){
        Context context = LoginActivity.this;
        Intent startRegisterActivityClass = new Intent(context, RegisterActivity.class);
        startActivity(startRegisterActivityClass);
    }

    @Override
    public void onBackPressed() {

    }
}
