package com.ittsport.ittsportapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.ittsport.ittsportapp.R;

import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MessageActivity extends AppCompatActivity {

    Button populateButton;
    EditText mAsunto;
    EditText mCuerpo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        populateButton = findViewById(R.id.message_populate);
        mAsunto = findViewById(R.id.asunto_text);
        mCuerpo = findViewById(R.id.cuerpo_text);

        populateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> mensaje = new HashMap<>();
                mensaje.put("asunto", mAsunto.getText().toString());
                mensaje.put("cuerpo", mCuerpo.getText().toString());
                mensaje.put("fecha", new Date());
                mensaje.put("senderId", 69);
                mensaje.put("receiversIds", Arrays.asList(1,2,3));

                db.collection("mensajes").document("mensaje1")
                        .set(mensaje)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }
}
