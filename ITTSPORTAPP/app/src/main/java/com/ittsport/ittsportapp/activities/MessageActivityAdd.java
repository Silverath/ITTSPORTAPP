package com.ittsport.ittsportapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittsport.ittsportapp.R;

import org.w3c.dom.Document;

import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MessageActivityAdd extends AppCompatActivity {

    Button populateButton;
    EditText mAsunto;
    EditText mCuerpo;
    Button showMessagesButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static Boolean bla = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        populateButton = findViewById(R.id.message_populate);
        mAsunto = findViewById(R.id.asunto_text);
        mCuerpo = findViewById(R.id.cuerpo_text);
        showMessagesButton = findViewById(R.id.show_messages);

        //TODO (1): Hacer que se muestre una lista de mensajes con todos los mensajes mandados y tal
        //TODO (2): Abrir un mensaje y que te muestre toda la información relacionada con el mensaje
        //TODO (3): Poder eliminar un mensaje de la lista

        populateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> mensaje = new HashMap<>();
                mensaje.put("asunto", mAsunto.getText().toString());
                mensaje.put("cuerpo", mCuerpo.getText().toString());
                mensaje.put("fecha", new Date());
                mensaje.put("senderId", 69);
                mensaje.put("receiversIds", Arrays.asList(1,2,3));

                final String nMensaje = "mensaje";

                DocumentReference document = db.collection("mensajes").document();
                document.set(mensaje).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });

        showMessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MessageActivityAdd.this;
                Class destinationActivity = MessageActivityShow.class;
                Intent startMessageActivityIntent = new Intent(context, destinationActivity);
                startActivity(startMessageActivityIntent);
            }
        });
    }
}
