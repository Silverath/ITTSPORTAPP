package com.ittsport.ittsportapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.activities.MessageActivityShow;
import com.ittsport.ittsportapp.adapters.UserMessagingAdapter;
import com.ittsport.ittsportapp.models.CuentaUsuario;
import com.ittsport.ittsportapp.models.PerfilSocial;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserMessagingAdapter userMessagingAdapter;
    private List<PerfilSocial> mUsers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        mUsers = new ArrayList<>();

        readUsers();

        recyclerView = view.findViewById(R.id.rv_usuarios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //userMessagingAdapter = new UserMessagingAdapter(this.getContext(), mUsers);
        //recyclerView.setAdapter(userMessagingAdapter);

        return view;
    }

    //Tiene que devolver una lista de users para pasarla en el metodo de arriba.
    private void readUsers(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("perfilesSociales").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(DocumentSnapshot i: queryDocumentSnapshots) {
                    PerfilSocial perfilSocial = new PerfilSocial(i.getString("nombre"), i.getString("primerApellido"), i.getString("segundoApellido"), i.getString("cuentaUsuarioId"));
                    String id = i.getId();
                    mUsers.add(perfilSocial);
                }
                userMessagingAdapter = new UserMessagingAdapter(UsersFragment.this.getContext(), mUsers);
                recyclerView.setAdapter(userMessagingAdapter);

            }
        });

    }

}
