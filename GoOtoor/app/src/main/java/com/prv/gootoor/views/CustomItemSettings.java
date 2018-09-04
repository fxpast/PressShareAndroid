package com.prv.gootoor.views;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.prv.gootoor.R;
import com.prv.gootoor.activities.TabMainActivity;
import com.prv.gootoor.utils.Config;
import com.readystatesoftware.viewbadger.BadgeView;


/**
 * Created by roger on 01/09/2017.
 */

public class CustomItemSettings extends ArrayAdapter {

    private Config  mConfig = Config.sharedInstance();

    private  int resource;
    private Context context;


    public CustomItemSettings(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable  View convertView, @NonNull ViewGroup parent) {


        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(resource, parent, false);

        String  textSettings  =(String) getItem(position);

        TextView mIBItemTextView =(TextView) customView.findViewById(R.id.IBItemTextView);
        mIBItemTextView.setText(textSettings);
        String hexColor = "#" + mConfig.getColorAppLabel();
        mIBItemTextView.setTextColor(Color.parseColor(hexColor));

        return customView;
    }




}