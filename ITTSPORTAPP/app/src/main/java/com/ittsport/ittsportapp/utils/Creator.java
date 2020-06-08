package com.ittsport.ittsportapp.utils;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import static android.content.ContentValues.TAG;

public class Creator {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void populateGrupos(){
        // Crea un grupo nuevo con nombre y horario
        Map<String, Object> grupo1 = new HashMap<>();
        grupo1.put("nombre", "putosamos");
        grupo1.put("horario", "cuando me da la gana");


// Add a new document with a generated ID
        db.collection("grupos")
                .add(grupo1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@Nonnull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
