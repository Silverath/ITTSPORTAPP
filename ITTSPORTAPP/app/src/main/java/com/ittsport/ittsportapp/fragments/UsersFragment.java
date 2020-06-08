package com.ittsport.ittsportapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.UserMessagingAdapter;
import com.ittsport.ittsportapp.models.PerfilSocial;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserMessagingAdapter userMessagingAdapter;
    private List<PerfilSocial> mUsers = new ArrayList<>();
    private List<PerfilSocial> afterSearch = new ArrayList<>();

    EditText search_Users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        mUsers = new ArrayList<>();

        readUsers();

        search_Users = view.findViewById(R.id.search_users);
        search_Users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                    PerfilSocial perfilSocial = i.toObject(PerfilSocial.class);
                    String id = i.getId();
                    mUsers.add(perfilSocial);
                }
                userMessagingAdapter = new UserMessagingAdapter(UsersFragment.this.getContext(), mUsers);
                recyclerView.setAdapter(userMessagingAdapter);

            }
        });

    }

    private void searchUsers(String text){
        if(text.isEmpty()){
            userMessagingAdapter = new UserMessagingAdapter(UsersFragment.this.getContext(), mUsers);
            recyclerView.setAdapter(userMessagingAdapter);
        } else {
            afterSearch.clear();
            for (PerfilSocial perfilSocial : mUsers) {
                String nombreCompleto = perfilSocial.getNombre() + " " + perfilSocial.getPrimerApellido() + " " +
                        perfilSocial.getSegundoApellido();
                if (nombreCompleto.toLowerCase().contains(text.toLowerCase())) {
                    afterSearch.add(perfilSocial);
                }
                userMessagingAdapter = new UserMessagingAdapter(UsersFragment.this.getContext(), afterSearch);
                recyclerView.setAdapter(userMessagingAdapter);
            }
        }
    }

}
