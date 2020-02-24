package com.ittsport.ittsportapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.UserMessagingAdapter;
import com.ittsport.ittsportapp.models.Chat;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.annotation.Nullable;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserMessagingAdapter userMessagingAdapter;
    private ArrayList<String> IDList;
    private ArrayList<PerfilSocial> perfilSocialList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        perfilSocialList = new ArrayList<>();
        IDList = new ArrayList<>();


        getOpenChats();

        recyclerView = view.findViewById(R.id.rv_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }



    private void getOpenChats(){
        final String loggedId = VariablesGlobales.perfilLogueado;

        db.collection("chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot q: queryDocumentSnapshots){
                    if(q.get("sender").equals(loggedId) && !IDList.contains(q.get("receiver"))){
                        IDList.add(q.get("receiver").toString());
                    } else if(q.get("receiver").equals(loggedId) && !IDList.contains(q.get("sender"))){
                        IDList.add(q.get("sender").toString());
                    }
                }
                for(String id: IDList)
                    db.collection("perfilesSociales").document(id).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    PerfilSocial perfil = new PerfilSocial(documentSnapshot.get("nombre").toString(),
                                            documentSnapshot.get("primerApellido").toString(),
                                            documentSnapshot.get("segundoApellido").toString(),
                                            documentSnapshot.get("cuentaUsuarioId").toString());
                                    perfilSocialList.add(perfil);
                                    userMessagingAdapter = new UserMessagingAdapter(ChatsFragment.this.getContext(), perfilSocialList);
                                    recyclerView.setAdapter(userMessagingAdapter);
                                    ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar_chat_activity);
                                    progressBar.setVisibility(View.GONE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }

                            });
            }
        });


    }
}
