package com.prv.gootoor.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by roger on 03/09/2017.
 */

public class ImageDownloader extends AsyncTask<String, Integer, Drawable> {


    private Context context;
    private ImageInterface completion;


    public ImageDownloader(Context context, ImageInterface completion) {
        this.context = context;
        this.completion = completion;

    }

    @Override
    protected Drawable doInBackground(String... params) {
        // This is done in a background thread
        return downloadImage(params[0]);
    }

    @Override
    protected void onPostExecute(Drawable drawable) {

        if (drawable == null) {
            completion.completionHandlerImage(false, null);

        } else {
            completion.completionHandlerImage(true, drawable);
        }


    }




    private Drawable downloadImage(String _url) {

        //Prepare to download image
        URL url;
        InputStream in;
        BufferedInputStream buf;

        try {

            url = new URL(_url);
            in = url.openStream();
            buf = new BufferedInputStream(in);

            Bitmap bMap = BitmapFactory.decodeStream(buf);

            if (in != null) {
                in.close();
            }
            if (buf != null) {
                buf.close();
            }

            return new BitmapDrawable(context.getResources(), bMap);


        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }

        return null;
    }


}
