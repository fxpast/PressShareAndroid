
/**
 * Created by roger on 27/10/2017.
 */


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
        import com.prv.gootoor.models.TypeCard;
        import com.prv.gootoor.utils.Config;
        import com.prv.gootoor.utils.MyTools;
        import com.prv.gootoor.utils.SaveImageInterface;
        import java.util.List;


/**
 * Created by roger on 25/10/2017.
 */


public class CustomItemListTypeCB extends ArrayAdapter {

    private Config mConfig = Config.sharedInstance();
    private  int resource;
    private Context context;


    public CustomItemListTypeCB(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(resource, parent, false);

        TypeCard typeCard = (TypeCard) getItem(position);

        CardView mIBTypeCBItemBackgr = (CardView) customView.findViewById(R.id.IBTypeCBItemBackgr);
        String hexColor = "#" + mConfig.getColorApp();
        mIBTypeCBItemBackgr.setCardBackgroundColor(Color.parseColor(hexColor));


        TextView mIBListTypeCBText =(TextView) customView.findViewById(R.id.IBListTypeCBText);

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBListTypeCBText.setTextColor(Color.parseColor(hexColor));


        mIBListTypeCBText.setText(typeCard.getTypeCard_Wording());


        final ImageView mIBListTypeCBImage = (ImageView) customView.findViewById(R.id.IBListTypeCBImage);
        final ProgressBar mIBListTypeCBProgBar = (ProgressBar) customView.findViewById(R.id.IBListTypeCBProgBar);


        MyTools.sharedInstance().saveImageArchive(context, typeCard.getTypeCard_ImageUrl(),  "images_cb/" , new SaveImageInterface() {
            @Override
            public void completionHandlerSaveImage(Boolean success, Drawable drawable) {

                if (!success) {

                    mIBListTypeCBImage.setTag("noimage");
                    Log.v(mConfig.getDomaineApp()+"CustomItemListTypeCB", "error image");

                } else {
                    mIBListTypeCBImage.setTag("");
                }

                mIBListTypeCBImage.setImageDrawable(drawable);


            }
        });



        return customView;
    }



}