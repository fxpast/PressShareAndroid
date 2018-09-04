
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
import com.prv.gootoor.models.Card;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.utils.SaveImageInterface;

import java.util.List;



/**
 * Created by roger on 25/10/2017.
 */


public class CustomItemListCard extends ArrayAdapter {

    private Config  mConfig = Config.sharedInstance();
    private  int resource;
    private Context context;


    public CustomItemListCard(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);

        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(resource, parent, false);

        Card card = (Card) getItem(position);

        CardView mIBListCardItemBackgr = (CardView) customView.findViewById(R.id.IBListCardItemBackgr);
        String hexColor = "#" + mConfig.getColorApp();
        mIBListCardItemBackgr.setCardBackgroundColor(Color.parseColor(hexColor));


        TextView mIBListCardText =(TextView) customView.findViewById(R.id.IBListCardText);
        TextView mIBCheckmark =(TextView) customView.findViewById(R.id.IBCheckmark);

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBListCardText.setTextColor(Color.parseColor(hexColor));

        if (card.getTypeCard_id() == 6) {
            mIBListCardText.setText("Paypal");
        } else {
            mIBListCardText.setText(card.getCard_lastNumber());
        }

        if (card.getMain_card()) {

            mIBCheckmark.setVisibility(View.VISIBLE);

        } else {
            mIBCheckmark.setVisibility(View.GONE);

        }



        final ImageView mIBListCardImage = (ImageView) customView.findViewById(R.id.IBListCardImage);
        final ProgressBar mIBListCardProgBar = (ProgressBar) customView.findViewById(R.id.IBListCardProgBar);


        MyTools.sharedInstance().saveImageArchive(context, card.getTypeCard_ImageUrl(), "images_cb/" , new SaveImageInterface() {
            @Override
            public void completionHandlerSaveImage(Boolean success, Drawable drawable) {

                if (!success) {

                    mIBListCardImage.setTag("noimage");
                    Log.v(mConfig.getDomaineApp()+"CustomItemListCard", "error image");

                } else {
                    mIBListCardImage.setTag("");
                }

                mIBListCardImage.setImageDrawable(drawable);


            }
        });



        return customView;
    }



}