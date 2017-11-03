package com.prv.pressshare.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.prv.pressshare.R;
import com.prv.pressshare.models.Product;
import com.prv.pressshare.utils.Config;

import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.utils.SaveImageInterface;

import java.util.List;


/**
 * Created by roger on 01/09/2017.
 */

public class CustomItemProduct extends ArrayAdapter {

    private Config  mConfig = Config.sharedInstance();

    private  int resource;
    private Context context;


    public CustomItemProduct(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable  View convertView, @NonNull ViewGroup parent) {


        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(resource, parent, false);

        Product product =(Product) getItem(position);


        TextView mIBListItemText =(TextView) customView.findViewById(R.id.IBListItemText);
         mIBListItemText.setText(product.getProd_nom()+" (user:"+product.getProd_by_user()+")");

       final ImageView mIBListItemImage = (ImageView) customView.findViewById(R.id.IBListItemImage);
       final ProgressBar  mIBListActivity = (ProgressBar) customView.findViewById(R.id.IBListProgressBar);



        MyTools.sharedInstance().saveImageArchive(context, product.getProd_imageUrl(), "images/", new SaveImageInterface() {
            @Override
            public void completionHandlerSaveImage(Boolean success, Drawable drawable) {


                if (!success) {

                    mIBListItemImage.setTag("noimage");
                    Log.v(mConfig.getDomaineApp()+"CustomItemProduct", "error image");

                } else {
                    mIBListItemImage.setTag("");
                }

                mIBListItemImage.setImageDrawable(drawable);


            }
        });



        return customView;
    }




}