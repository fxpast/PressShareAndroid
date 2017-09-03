package com.prv.pressshare.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;

import java.util.List;


/**
 * Created by roger on 01/09/2017.
 */

public class CustomItem extends ArrayAdapter {

    private Config mConfig;
    private  int resource;
    private Context context;


    public CustomItem(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        mConfig = Config.sharedInstance();
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(resource, parent, false);

        Product product =(Product) getItem(position);

        TextView mIBListItemText =(TextView) customView.findViewById(R.id.IBListItemText);
         mIBListItemText.setText(product.getProd_nom()+" (user:"+product.getProd_by_user()+")");

       final ImageView mIBListItemImage = (ImageView) customView.findViewById(R.id.IBListItemImage);
       final ProgressBar  mIBListActivity = (ProgressBar) customView.findViewById(R.id.IBListActivity);


        String  mUrl = mConfig.getUrlServer()+"/images/"+product.getProd_imageUrl()+".jpg";


        mIBListActivity.setVisibility(View.VISIBLE);
        mIBListItemImage.setVisibility(View.GONE);


        ImageDownloader downloadImage =  new ImageDownloader(context, new ImageInterface() {
            @Override
            public void completionHandlerImage(final Boolean success, final Drawable drawable) {

                MyTools.sharedInstance().performUIUpdatesOnMain(context, new MainThreadInterface() {

                    @Override
                    public void completionUpdateMain() {
                        if (success) {
                            mIBListItemImage.setImageDrawable(drawable);
                        } else {
                            mIBListItemImage.setImageResource(R.drawable.noimage);
                        }

                        mIBListActivity.setVisibility(View.GONE);
                        mIBListItemImage.setVisibility(View.VISIBLE);

                    }
                });

            }
        });

        downloadImage.execute(mUrl);


        return customView;
    }




}