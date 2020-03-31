package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        setNavigationViewListener();

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.vista_mensajes:
                vistaMensajeria();
                return true;
            case R.id.cerrar_sesion:
                cerrarSesion();
                return true;
            case R.id.cambiar_perfil:
                elegirPerfil();
                return true;
            default:
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //doNothing
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    private void cerrarSesion(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(HomeActivity.this, "no ai nadie xd", Toast.LENGTH_LONG).show();
        }
        else{
            FirebaseAuth.getInstance().signOut();
            Context context = HomeActivity.this;
            Intent goToLogin = new Intent(context, LoginActivity.class);
            startActivity(goToLogin);
        }
    }

    private void elegirPerfil(){
        VariablesGlobales sharedPreferences = new VariablesGlobales(this);
        sharedPreferences.setPerfilLogueadoId(null);
        Context context = HomeActivity.this;
        Intent goToElegirPerfil = new Intent(context, ListSocialProfileActivity.class);
        startActivity(goToElegirPerfil);
    }

    private void vistaMensajeria(){
        Context context = HomeActivity.this;
        Class destinationActivity = MessageActivityShow.class;
        Intent startMessageActivityIntent = new Intent(context, destinationActivity);
        startActivity(startMessageActivityIntent);
    }
}
