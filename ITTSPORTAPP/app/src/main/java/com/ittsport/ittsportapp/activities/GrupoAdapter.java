package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ittsport.ittsportapp.R;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.NumberViewHolder>{


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.activity_grupo_main;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder{
        TextView listGroupView;
        public NumberViewHolder(View itemView) {
            super(itemView);
            listGroupView = (TextView) itemView.findViewById(R.id.tv_nombre_grupo);
        }
    }
}
