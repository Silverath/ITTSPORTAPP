package com.ittsport.ittsportapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.view.LayoutInflater;

import com.ittsport.ittsportapp.R;

public class ErrorDialog {

    Activity activity;
    AlertDialog alertDialog;

    public ErrorDialog (Activity activity){
        this.activity = activity;
    }

    public void startErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.error_dialog, null));

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
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "¡Entendido!", listener);
        alertDialog.show();

        try{
            Looper.loop();
        } catch (RuntimeException e){

        }
    }

}
