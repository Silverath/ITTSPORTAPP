package com.ittsport.ittsportapp.adapters;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;

public class ListGrupoHolder extends ViewHolder{

    public TextView mGrupoName;
    public ListGrupoHolder(View itemView) {
        super(itemView);
        mGrupoName = (TextView) itemView.findViewById(R.id.tv_grupo_name);

    }
}
