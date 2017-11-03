package com.prv.pressshare.views;

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

import com.prv.pressshare.R;

import com.prv.pressshare.models.Transaction;
import com.prv.pressshare.utils.Config;


import java.util.Calendar;
import java.util.List;

/**
 * Created by roger on 23/10/2017.
 */

public class CustomItemListTrans extends ArrayAdapter {

    private Config mConfig = Config.sharedInstance();
    private  int resource;
    private Context context;


    public CustomItemListTrans(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(resource, parent, false);

        Transaction transaction =(Transaction) getItem(position);


        TextView mIBListTransDate =(TextView) customView.findViewById(R.id.IBListTransDate);
        TextView mIBListTransType =(TextView) customView.findViewById(R.id.IBListTransType);
        TextView mIBListTransAmount =(TextView) customView.findViewById(R.id.IBListTransAmount);
        TextView mIBListTransAwording =(TextView) customView.findViewById(R.id.IBListTransAwording);

        Calendar c = Calendar.getInstance();
        c.setTime(transaction.getTrans_date());
        int day =  c.get(c.DAY_OF_MONTH);
        int month = c.get(c.MONTH);
        int year = c.get(c.YEAR);
        String dateString = year + "/" + month + "/" + day;

        mIBListTransDate.setText(dateString);
        if (transaction.getTrans_type() == 1) {

            mIBListTransType.setText(context.getString(R.string.buy));

        } else  if (transaction.getTrans_type() == 2) {

            mIBListTransType.setText(context.getString(R.string.exchange));
        } else {

            mIBListTransType.setText("");
        }

        mIBListTransAmount.setText(transaction.getTrans_amount().toString());
        mIBListTransAwording.setText(transaction.getTrans_wording());

        if (transaction.getTrans_valid() == 1 || transaction.getTrans_valid() == 2) {

            mIBListTransDate.setTextColor(Color.BLACK);
            mIBListTransType.setTextColor(Color.BLACK);
            mIBListTransAmount.setTextColor(Color.BLACK);
            mIBListTransAwording.setTextColor(Color.BLACK);

        } else {

            mIBListTransDate.setTextColor(Color.BLUE);
            mIBListTransType.setTextColor(Color.BLUE);
            mIBListTransAmount.setTextColor(Color.BLUE);
            mIBListTransAwording.setTextColor(Color.BLUE);

        }



        return customView;
    }




}
