

package com.prv.gootoor.views;

import android.content.Context;

import android.os.AsyncTask;
import android.os.Environment;

import com.prv.gootoor.utils.Config;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;


/**
 * Created by roger on 31/10/2017.
 */




public class FileDownloader extends AsyncTask<String, Integer, File> {

    private Config mConfig = Config.sharedInstance();

    private Context context;
    private FileInterface completion;


    public FileDownloader(Context context, FileInterface completion) {
        this.context = context;
        this.completion = completion;

    }

    @Override
    protected File doInBackground(String... params) {
        // This is done in a background thread
        return downloadImage(params[0]);
    }

    @Override
    protected void onPostExecute(File file) {

        if (file == null) {

            completion.completionHandlerFile(false, null);

        } else {
            completion.completionHandlerFile(true, file);
        }


    }




    private File downloadImage(String _url) {


        try {

            URL url = new URL(_url);

            InputStream inputStream = url.openStream();

            File destination;

            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {

                 destination  = new File(context.getExternalFilesDir(null), mConfig.getDomaineApp()+"filePDF.pdf");

            } else {

                 destination  = new File(context.getFilesDir(), "filePDF.pdf");

            }

            destination.setWritable(true, false);
            OutputStream outputStream = new FileOutputStream(destination);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }


            outputStream.close();
            inputStream.close();

            return destination;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
