package com.bigandroiddev.vibify.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.bigandroiddev.vibify.Dialogs.RemoteDialog;
import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Vibify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

/**
 * Created by spiros on 11/30/14.
 */
public class DialogUtils{


    private static final String TAG = DialogUtils.class.getSimpleName();
    private static final String REMOTE_CONTENT_URL = "https://www.dropbox.com/s/zgn39q73fuzh78h/credits_thirdparty?dl=1";

    public static void showRemoteDialog(final Activity currentActivity){

        new AsyncTask<Void, Void, String>() {


            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL oracle = new URL(REMOTE_CONTENT_URL);
                    BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

                    String inputLine, remoteData = "";
                    inputLine = in.readLine();
                    if(inputLine.contains("show") && inputLine.contains("true")){
                        while ((inputLine = in.readLine()) != null){
                            remoteData = remoteData.concat(inputLine);
                        }
                        in.close();
                        return remoteData;
                    }else{
                        in.close();
                        return "";
                    }
//                    validVersionCode = Integer.parseInt(inputLine);
//					Log.d(TAG, "response: " + inputLine);
//					Log.d(TAG, "validVersionCode: " + validVersionCode);

                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String remoteData) {
                super.onPostExecute(remoteData);

                if(!remoteData.isEmpty()) {
                    Intent intent = new Intent(currentActivity, RemoteDialog.class);
                    intent.putExtra("remote_content", remoteData);
                    currentActivity.startActivity(intent);
                }
            }

        }.execute();



    }

    public static void showRateDialog(final Activity currentActivity){
        if (Vibify.isFirstRun() || Vibify.isPleaseRateClicked()) return;
        if ((new Random()).nextInt(10) < 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            // Add the buttons
            builder.setTitle(currentActivity.getResources().getString(R.string.please_rate));
            builder.setMessage(R.string.please_rate_text);
            builder.setIcon(android.R.drawable.btn_star);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.yeaaah, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + currentActivity.getApplication().getPackageName()));
                    currentActivity.startActivity(intent);
                    Vibify.setPleaseRateClicked(true);
                }
            });
            builder.setNegativeButton(R.string.naaaah, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Vibify.setPleaseRateClicked(false);
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }

}
