package com.ittsport.ittsportapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.ListGrupoActivity;
import com.ittsport.ittsportapp.models.Grupo;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListGrupoAdapter extends RecyclerView.Adapter<ListGrupoHolder>{


    ListGrupoActivity listGrupoActivity;
    ArrayList<Grupo> grupos;

    public ListGrupoAdapter(ListGrupoActivity listGrupoActivity, ArrayList<Grupo> grupos) {
        this.listGrupoActivity = listGrupoActivity;
        this.grupos = grupos;
    }

    @NonNull
    @Override
    public ListGrupoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(listGrupoActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.list_grupo_activity_row, parent, false);

        return new ListGrupoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListGrupoHolder holder, int position) {
        holder.mGrupoName.setText(grupos.get(position).getNombre());
        Log.w(TAG, holder.mGrupoName.getText().toString());
        if(holder.mGrupoName.getText().length()==0){
            Log.w(TAG, "vacio");
        }
    }

    @Override
    public int getItemCount() {
        return grupos.size();
    }

}
