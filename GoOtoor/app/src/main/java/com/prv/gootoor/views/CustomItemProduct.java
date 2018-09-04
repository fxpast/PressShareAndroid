package com.prv.gootoor.views;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.prv.gootoor.R;
import com.prv.gootoor.activities.TabMainActivity;
import com.prv.gootoor.lists.Messages;
import com.prv.gootoor.models.Message;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.utils.Config;

import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.utils.SaveImageInterface;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;


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

        final Product product =(Product) getItem(position);

        CardView mIBListProdItemBackgr = (CardView) customView.findViewById(R.id.IBListProdItemBackgr);
        String hexColor = "#" + mConfig.getColorApp();
        mIBListProdItemBackgr.setCardBackgroundColor(Color.parseColor(hexColor));

        TextView mIBListItemText =(TextView) customView.findViewById(R.id.IBListItemText);
        mIBListItemText.setText(product.getProd_nom()+" (user:"+product.getProd_by_user()+")");
        hexColor = "#" + mConfig.getColorAppLabel();
        mIBListItemText.setTextColor(Color.parseColor(hexColor));

        final ImageView mIBListItemImage = (ImageView) customView.findViewById(R.id.IBListItemImage);


        MyTools.sharedInstance().saveImageArchive(context, product.getProd_imageUrl(),  "images/", new SaveImageInterface() {
            @Override
            public void completionHandlerSaveImage(Boolean success, Drawable drawable) {


                if (!success) {

                    mIBListItemImage.setTag("noimage");
                    Log.v(mConfig.getDomaineApp()+"CustomItemProduct", "error image");

                } else {
                    mIBListItemImage.setTag("");
                }

                mIBListItemImage.setImageDrawable(drawable);


                int i = 0;
                try {

                    JSONArray messagesJSON = Messages.sharedInstance().getMessagesArray();

                    for (int j = 0; j < messagesJSON.length(); j++) {

                        JSONObject dictionary = messagesJSON.getJSONObject(j);
                        Message message = new Message(context, dictionary);
                        if (message.getProduct_id() == product.getProd_id() && message.getDestinataire() == mConfig.getUser_id() && !message.getDeja_lu()){
                            i+=1;
                        }
                    }

                    if (i>0) {
                        BadgeView badge = new BadgeView(context, mIBListItemImage);
                        badge.setText(""+i);
                        badge.show();
                    }


                }  catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });



        return customView;
    }




}