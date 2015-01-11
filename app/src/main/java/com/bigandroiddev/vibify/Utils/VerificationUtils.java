package com.bigandroiddev.vibify.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Vibify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * Created by spiros on 11/30/14.
 */
public class VerificationUtils {

    private static final String TAG = VerificationUtils.class.getSimpleName();
    private static final String TRIAL_START = "trialStart", TRIAL_END = "trialEnd", VALID_VERSION_CODE = "validCode";
    private static final boolean OVERRIDE = true, DONT_OVERRIDE = false;

    public VerificationUtils(Context ctx) {

    }

    public static void createTrialStart(Context applicationCtx, boolean override) {

        StorageUtils.writeToExternalStorage(applicationCtx, TRIAL_START, new Date(), override);
    }

    public static void createTrialEnd(Context applicationCtx, int duration, boolean override) {
        Date trialStartDate = (Date) StorageUtils.readFromExternalStorage(applicationCtx, TRIAL_START);
        Date trialEndDate = new Date();
        trialEndDate.setDate(trialStartDate.getDate() + duration);
        StorageUtils.writeToExternalStorage(applicationCtx, TRIAL_END, trialEndDate, override);
    }

    public static void checkIfTrialEnded(Activity currentActivity) {

        Date trialEndDate = (Date) StorageUtils.readFromExternalStorage(currentActivity.getApplicationContext(), TRIAL_END);

        Date now = new Date();
        Log.i(TAG, now.toString());
        Log.i(TAG, trialEndDate.toString());
        if (now.after(trialEndDate)) {
            showErrorDialog(currentActivity);
        }
    }

    public static void verifyThroughVersionCode(final Activity currentActivity) {

        AsyncTask<Void, Void, Integer> verify = new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    URL oracle = new URL("https://www.dropbox.com/s/imafxqo9j2r4w3x/vibify_licence?dl=1");
                    BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
                    int validVersionCode;
                    String inputLine;
                    inputLine = in.readLine();
                    validVersionCode = Integer.parseInt(inputLine);
//					Log.d(TAG, "response: " + inputLine);
//					Log.d(TAG, "validVersionCode: " + validVersionCode);
                    in.close();
                    StorageUtils.writeToExternalStorage(currentActivity.getApplicationContext(), VALID_VERSION_CODE, validVersionCode, OVERRIDE);
                    return validVersionCode;
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            @Override
            protected void onPostExecute(Integer validVersionCode) {
                super.onPostExecute(validVersionCode);
                int currentVersionCode;

                try {
                    currentVersionCode = currentActivity.getPackageManager().getPackageInfo(currentActivity.getPackageName(), 0).versionCode;

                    Log.d(TAG, "validVersionCode: " + validVersionCode);
                    Log.d(TAG, "currentVersionCode: " + currentVersionCode);
                    if (validVersionCode == 0) {
                        validVersionCode = (Integer) StorageUtils.readFromExternalStorage(currentActivity.getApplicationContext(), VALID_VERSION_CODE);
                    }

                    if (currentVersionCode < validVersionCode) {
                        showErrorDialog(currentActivity);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };
        verify.execute();

    }

    public static void showErrorDialog(final Activity currentActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        // Add the buttons
        builder.setTitle("Ooops");
        builder.setMessage(R.string.error_licencing_app);
        builder.setIcon(R.drawable.ic_warning);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:Big Android Dev"));
                currentActivity.startActivity(intent);
                currentActivity.finish();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public static void showBuyPremiumVersionDialog(final Activity currentActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        // Add the buttons
        builder.setTitle(currentActivity.getResources().getString(R.string.buy_premium));
        builder.setMessage(R.string.buy_premium_text);
        builder.setIcon(R.drawable.ic_you_shall_not_pass);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yeaaah, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.spirosbond.vibify"));
                currentActivity.startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.naaaah, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public static void showPleaseRateDialog(final Activity currentActivity) {
        if (Vibify.isFirstRun() || Vibify.isPleaseRateClicked()) return;
        if ((new Random()).nextInt(10) < 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            // Add the buttons
            builder.setTitle(currentActivity.getResources().getString(R.string.please_rate));
            builder.setMessage(R.string.please_rate_text);
            builder.setIcon(R.drawable.ic_logo_small);
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
