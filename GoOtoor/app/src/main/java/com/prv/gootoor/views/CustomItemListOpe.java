
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

        import com.prv.gootoor.models.PressOperation;

        import com.prv.gootoor.utils.Config;

        import java.util.Calendar;
        import java.util.List;

/**
 * Created by roger on 24/10/2017.
 */

public class CustomItemListOpe extends ArrayAdapter {

    private Config mConfig = Config.sharedInstance();
    private  int resource;
    private Context context;


    public CustomItemListOpe(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);

        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(resource, parent, false);

        PressOperation operation =(PressOperation) getItem(position);

        CardView mIBListOperBackgr = (CardView) customView.findViewById(R.id.IBListOperBackgr);
        String hexColor = "#" + mConfig.getColorApp();
        mIBListOperBackgr.setCardBackgroundColor(Color.parseColor(hexColor));


        TextView mIBListOpeDate =(TextView) customView.findViewById(R.id.IBListOpeDate);
        TextView mIBListOpeType =(TextView) customView.findViewById(R.id.IBListOpeType);
        TextView mIBListOpeAmount =(TextView) customView.findViewById(R.id.IBListOpeAmount);
        TextView mIBListOpeAwording =(TextView) customView.findViewById(R.id.IBListOpeAwording);

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBListOpeDate.setTextColor(Color.parseColor(hexColor));
        mIBListOpeType.setTextColor(Color.parseColor(hexColor));
        mIBListOpeAmount.setTextColor(Color.parseColor(hexColor));
        mIBListOpeAwording.setTextColor(Color.parseColor(hexColor));


        Calendar c = Calendar.getInstance();
        c.setTime(operation.getOp_date());
        int day =  c.get(c.DAY_OF_MONTH);
        int month = c.get(c.MONTH);
        int year = c.get(c.YEAR);
        String dateString = year + "/" + month + "/" + day;

        mIBListOpeDate.setText(dateString);
        if (operation.getOp_type() == 1) {

            mIBListOpeType.setText(context.getString(R.string.deposit));

        } else  if (operation.getOp_type() == 2) {

            mIBListOpeType.setText(context.getString(R.string.withdrawal));
        } else if (operation.getOp_type() == 3) {

            mIBListOpeType.setText(context.getString(R.string.buy));
        } else if (operation.getOp_type() == 4) {

            mIBListOpeType.setText(context.getString(R.string.sell));
        } else if (operation.getOp_type() == 5) {

            mIBListOpeType.setText(context.getString(R.string.commission));
        } else if (operation.getOp_type() == 6) {

            mIBListOpeType.setText(context.getString(R.string.refund));
        }

        mIBListOpeAmount.setText(operation.getOp_amount().toString());
        mIBListOpeAwording.setText(operation.getOp_wording());


        return customView;
    }




}
