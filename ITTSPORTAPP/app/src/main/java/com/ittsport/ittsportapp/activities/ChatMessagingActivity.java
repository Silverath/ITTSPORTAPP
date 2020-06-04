package com.ittsport.ittsportapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;
import com.ittsport.ittsportapp.adapters.ChatMessagingAdapter;
import com.ittsport.ittsportapp.fragments.ChatsFragment;
import com.ittsport.ittsportapp.models.Chat;
import com.ittsport.ittsportapp.models.Estado;
import com.ittsport.ittsportapp.models.PerfilSocial;
import com.ittsport.ittsportapp.models.Rol;
import com.ittsport.ittsportapp.utils.VariablesGlobales;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagingActivity extends Activity {

    CircleImageView profile_image;
    TextView tv_nombre;
    EditText et_message;
    ImageButton send_message_button;

    ChatMessagingAdapter chatMessagingAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messaging);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //getSupportActionBar().hide();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.chat_list_messages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image_user);
        tv_nombre = findViewById(R.id.chat_username);
        et_message = findViewById(R.id.edit_message_text);
        send_message_button = findViewById(R.id.send_message_button);

        intent = getIntent();
        final String perfilId = intent.getStringExtra("profileSelected");
        VariablesGlobales sharedPreferences = new VariablesGlobales(ChatMessagingActivity.this);
        final String loggedId = sharedPreferences.getPerfilLogueadoId();

        // Esto se crea para poder acceder desde dentro de los eventos.
        final List<PerfilSocial> perfilSocial = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("perfilesSociales").document(perfilId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot i = task.getResult();
                PerfilSocial perfil = i.toObject(PerfilSocial.class);
                perfilSocial.add(perfil);
            }
        });

        db.collection("chat").whereEqualTo("targetId", perfilId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                tv_nombre.setText(intent.getStringExtra("nameSelected"));
                profile_image.setImageResource(R.mipmap.ic_launcher_round);
            }
        });

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_message.getText().toString();
                if(!msg.equals("")){
                    sendMessage(loggedId, perfilId, msg);
                } else {
                    Toast.makeText(ChatMessagingActivity.this, "No puedes enviar un mensaje vacío cruck", Toast.LENGTH_SHORT).show();
                }
                et_message.setText("");
            }
        });

        db.collection("chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                readMessages(loggedId, perfilId);
            }
        });

    }

    private void sendMessage(String sender, String receiver, String message){
        ChatsFragment.checkChats(sender, receiver, message);
    }



    private void readMessages(final String myId, final String userId){
        mChat = new ArrayList<>();

        CollectionReference colRef = FirebaseFirestore.getInstance().collection("chat");
        colRef.orderBy("sentDate").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mChat.clear();
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    Chat chat = new Chat();
                    chat.setMessage(doc.get("message").toString());
                    chat.setSender(doc.get("sender").toString());
                    chat.setReceiver(doc.get("receiver").toString());
                    if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                    chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        mChat.add(chat);
                    }
                    chatMessagingAdapter = new ChatMessagingAdapter(ChatMessagingActivity.this, mChat);
                    recyclerView.setAdapter(chatMessagingAdapter);
                }
            }
        });
    }
}
