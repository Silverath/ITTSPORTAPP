package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.utils.LoadingDialog;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import javax.annotation.Nonnull;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout correo_electronico;
    TextInputLayout passwordLogin;
    Button buttonLogin;
    TextView go_to_register;
    FirebaseAuth firebaseAuth;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingDialog = new LoadingDialog(this);
        correo_electronico = (TextInputLayout) findViewById(R.id.til_login_correo_electronico);
        passwordLogin = (TextInputLayout)findViewById(R.id.til_login_contraseña);
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
            String email = correo_electronico.getEditText().getText().toString();
            String password = passwordLogin.getEditText().getText().toString();
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
            goTo(3);
        }
        else if(firebaseAuth.getCurrentUser() != null && sharedPreferences.getPerfilLogueadoId() == null && sharedPreferences.getEscuelaSeleccionada() != null){
            goTo(2);
        }
        else if(firebaseAuth.getCurrentUser() != null && sharedPreferences.getPerfilLogueadoId() == null && sharedPreferences.getEscuelaSeleccionada() == null){
            goTo(1);
        }
    }

    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@Nonnull Task<AuthResult> task) {
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
                                correo_electronico.setError("No existe cuenta con este Email");
                            }catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    public boolean validate(String email, String password){
        boolean res = true;
        if(TextUtils.isEmpty(email)){
            loadingDialog.dismissDialog();
            correo_electronico.setError("Falta el correo electrónico");
            res = false;
        }
        else if(TextUtils.isEmpty(password)){
            loadingDialog.dismissDialog();
            passwordLogin.setError("Falta la contraseña");
            res = false;
        }

        return res;
    }

    public void goTo(Integer nextStep){
        if(nextStep == 3){
            Context context = LoginActivity.this;
            Intent startHomeActivityClass = new Intent(context, HomeAlumnoActivity.class);
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
