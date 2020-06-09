package com.ittsport.ittsportapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;

import com.ittsport.ittsportapp.R;

public class AcceptDialog {

    Activity activity;
    AlertDialog alertDialog;

    public AcceptDialog (Activity activity){
        this.activity = activity;
    }

    public void startAcceptDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.accept_dialog, null));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                Intent returnIntent = new Intent();
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();
            }
        };

        alertDialog = builder.create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "¡Entendido!", listener);
        alertDialog.show();

        try{
            Looper.loop();
        } catch (RuntimeException e){

        }
    }
}
