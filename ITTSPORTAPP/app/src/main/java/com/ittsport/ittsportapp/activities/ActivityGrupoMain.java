package com.ittsport.ittsportapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;

public class ActivityGrupoMain extends AppCompatActivity {


    RecyclerView rGrupos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_main);


        rGrupos = (RecyclerView) findViewById(R.id.rv_grupo_names);
    }
}
