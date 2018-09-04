package com.prv.gootoor.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prv.gootoor.R;
import com.prv.gootoor.models.Creneau;
import com.prv.gootoor.models.Transaction;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MyTools;

import java.util.Calendar;
import java.util.List;

/**
 * Created by roger on 23/10/2017.
 */

public class CustomItemListCren extends ArrayAdapter {

    private Config mConfig = Config.sharedInstance();
    private  int resource;
    private Context context;


    public CustomItemListCren(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(resource, parent, false);

        Creneau creneau =(Creneau) getItem(position);

        CardView mIBListCrenItemBackgr = (CardView) customView.findViewById(R.id.IBListCrenItemBackgr);
        String hexColor = "#" + mConfig.getColorApp();
        mIBListCrenItemBackgr.setCardBackgroundColor(Color.parseColor(hexColor));


        TextView mIBListDateDebut =(TextView) customView.findViewById(R.id.IBListDateDebut);
        TextView mIBListDateFin =(TextView) customView.findViewById(R.id.IBListDateFin);
        TextView mIBListTapAloc =(TextView) customView.findViewById(R.id.IBListTapAloc);
        TextView mIBListRepeat =(TextView) customView.findViewById(R.id.IBListRepeat);

        mIBListDateDebut.setText(MyTools.sharedInstance().stringFromDate(creneau.getCre_dateDebut()));
        mIBListDateFin.setText(MyTools.sharedInstance().stringFromDate(creneau.getCre_dateFin()));

        mIBListTapAloc.setText(creneau.getCre_mapString());
        if (creneau.getCre_repeat() == 0) {
            mIBListRepeat.setText("");
        } else if (creneau.getCre_repeat() == 1) {
            mIBListRepeat.setText(context.getString(R.string.daily));
        } else if (creneau.getCre_repeat() == 2) {
            mIBListRepeat.setText(context.getString(R.string.weekly));
        }


        return customView;
    }




}
