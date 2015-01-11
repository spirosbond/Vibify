package com.bigandroiddev.vibify.Utils;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by spiros on 11/30/14.
 */
public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();


    public StorageUtils(Context applicationCtx) {

    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable(Context applicationCtx) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean writeToExternalStorage(Context applicationCtx, String fName, Object data, boolean override) {

        File[] dir = ContextCompat.getExternalFilesDirs(applicationCtx, null);
        File mFile = new File(dir[0].getAbsolutePath(), fName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;

        if (mFile.exists() && !override) {
            Log.e(TAG, "file exists");
            return false;
        }

        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();

        try {
            FileOutputStream outputStream = new FileOutputStream(mFile);
            outputStream.write(buf);
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static Object readFromExternalStorage(Context applicationCtx, String fName) {

//		StringBuilder data= new StringBuilder("");
        byte[] data;
        int i = 0;
        File[] dir = ContextCompat.getExternalFilesDirs(applicationCtx, null);
        File mFile = new File(dir[0].getAbsolutePath(), fName);
        if (!mFile.exists()) {
            Log.e(TAG, "file not found");
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(mFile);
            int content;
            data = new byte[inputStream.available()];
            while ((content = inputStream.read()) != -1) {
                // convert to char and display it
                data[i] = (byte) content;
                i++;
            }
            ObjectInputStream ois;
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object obj = ois.readObject();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * reads the changelog file and returns its content as a String.
     *
     * @param context
     *            the context.
     * @param ressourceName
     *            Name of the ressource to be loaded
     * @return content of the ressource as a String.
     * @throws IOException
     *             if errors occur while reading the changelog file
     */
    public static String loadRawData(final Context context, final String ressourceName)
            throws IOException {
        int resourceIdentifier = context
                .getApplicationContext()
                .getResources()
                .getIdentifier(ressourceName, "raw",
                        context.getApplicationContext().getPackageName());
        if (resourceIdentifier != 0) {
            InputStream inputStream = context.getApplicationContext().getResources()
                    .openRawResource(resourceIdentifier);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            StringBuffer data = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            reader.close();
            return data.toString();
        }
        return null;
    }
}
