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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ChatsFragment extends Fragment {

    private static RecyclerView recyclerView;

    private static UserMessagingAdapter userMessagingAdapter;
    private static ArrayList<String> IDList;
    private static ArrayList<PerfilSocial> perfilSocialList;

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
        VariablesGlobales sharedPreferences = new VariablesGlobales(getContext());
        final String loggedId = sharedPreferences.getPerfilLogueadoId();

        db.collection("chat").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
                                }

                            });
                ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar_chat_activity);
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public static void checkChats(final String sender, final String receiver, final String message){
        final FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        dataBase.collection("chat").whereEqualTo("receiver", receiver).
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                    userMessagingAdapter.notifyDataSetChanged();
                } else{
                    dataBase.collection("perfilesSociales").document(receiver).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    PerfilSocial perfil = new PerfilSocial(documentSnapshot.get("nombre").toString(),
                                            documentSnapshot.get("primerApellido").toString(),
                                            documentSnapshot.get("segundoApellido").toString(),
                                            documentSnapshot.get("cuentaUsuarioId").toString());
                                    perfilSocialList.add(perfil);
                                    userMessagingAdapter.notifyDataSetChanged();
                                }

                            });
                }
                final Map<String, Object> map = new HashMap<>();
                map.put("sender", sender);
                map.put("receiver", receiver);
                map.put("message", message);
                map.put("sentDate", Calendar.getInstance().getTime());
                final CollectionReference colRef = dataBase.collection("chat");


                DocumentReference doc = dataBase.collection("chat").document();
                doc.set(map);
            }
        });
    }
}
