package com.prv.gootoor.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.prv.gootoor.R;
import com.prv.gootoor.activities.AbonnerActivity;
import com.prv.gootoor.activities.ChangePwdActivity;
import com.prv.gootoor.activities.ListCardActivity;
import com.prv.gootoor.activities.ListTransactActivity;
import com.prv.gootoor.activities.MyProfilActivity;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.views.CustomItemSettings;
import java.io.File;
import java.io.FileOutputStream;


public class SettingsFragment extends Fragment {


    private Config mConfig = Config.sharedInstance();
    private ListView mIBSetListView;
    private ImageView mIBSetImageUser;
    private static final int CAMERA_REQUEST = 1234;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mConfig.setPreviousView("SettingsTableViewContr");

        mIBSetImageUser = (ImageView) view.findViewById(R.id.IBSetImageUser);

        TextView mIBSetTitle = (TextView) view.findViewById(R.id.IBSetTitle);
        mIBSetTitle.setText(mConfig.getUser_pseudo()+"("+mConfig.getUser_id()+")");

         String[] mActionsArray = new String[] {getString(R.string.editProfil),
                 getString(R.string.runTransac),getString(R.string.mySubscrit),
                getString(R.string.connectInfo),getString(R.string.myCB),getString(R.string.termsOfUse)};

        CardView mIBSetCardView = (CardView) view.findViewById(R.id.IBSetCardView);
        String hexColor = "#" + mConfig.getColorApp();
        mIBSetCardView.setCardBackgroundColor(Color.parseColor(hexColor));

        mIBSetListView = (ListView) view.findViewById(R.id.IBSetListView);

        ListAdapter mAdapter = new CustomItemSettings(getActivity(), R.layout.settings_item, mActionsArray);
        mIBSetListView.setAdapter(mAdapter);

        mIBSetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuSettings(position);

            }
        });


        TextView mIBSetProfil = (TextView) view.findViewById(R.id.IBSetProfil);
        mIBSetProfil.setText(mConfig.getUser_nom()+" "+mConfig.getUser_nom()
                +" ("+mConfig.getUser_note()+" "+getString(R.string.star)+") "+mConfig.getUser_email());
        hexColor = "#" + mConfig.getColorAppLabel();
        mIBSetProfil.setTextColor(Color.parseColor(hexColor));

        chargePhoto();

        mIBSetImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mConfig.getLevel() > 0) {

                    callCamera();

                }

            }
        });


        ImageView mIBSetLogout = (ImageView) view.findViewById(R.id.IBSetLogout);
        mIBSetLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionLogout();
            }
        });


        ImageView mIBSetHelp = (ImageView) view.findViewById(R.id.IBSetHelp);
        mIBSetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyTools.sharedInstance().showHelp("SettingsTableViewContr", getContext());
            }
        });


        return  view;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data.getExtras() != null) {

            Bitmap bitmap =  data.getExtras().getParcelable("data");
            mIBSetImageUser.setImageBitmap(bitmap);

            final File file;

            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {

                file = new File(getContext().getExternalFilesDir(null), mConfig.getDomaineApp()+"photoProfil.jpg");

            } else {

                file = new File(getContext().getFilesDir(), mConfig.getDomaineApp()+"photoProfil.jpg");
            }

            try {

                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);

                mIBSetImageUser.setDrawingCacheEnabled(false); //clean cache

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    private void MenuSettings(int position) {

        if (position == 0) {

            if (mConfig.getLevel()>-1) {

                Intent intent = new Intent(getContext(), MyProfilActivity.class);
                startActivity(intent);

            }


        } else if (position == 1) {

            if (mConfig.getLevel()>-1) {

                Intent intent = new Intent(getContext(), ListTransactActivity.class);
                startActivity(intent);
            }


        } else if (position == 2) {

            if (mConfig.getLevel()>-1) {

                Intent intent = new Intent(getContext(), AbonnerActivity.class);
                startActivity(intent);
            }


        } else if (position == 3) {

            if (mConfig.getLevel()>-1) {

                Intent intent = new Intent(getContext(), ChangePwdActivity.class);
                startActivity(intent);
            }


        } else if (position == 4) {

            if (mConfig.getLevel()>-1) {

                Intent intent = new Intent(getContext(), ListCardActivity.class);
                startActivity(intent);
            }

        } else if (position == 5) {

            MyTools.sharedInstance().showHelp("Conditions_PressShare", getContext());

        }


    }

    private void chargePhoto() {


        File file;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            file = new File(getContext().getExternalFilesDir(null), mConfig.getDomaineApp()+"photoProfil.jpg");

        } else {

            file = new File(getContext().getFilesDir(), mConfig.getDomaineApp()+"photoProfil.jpg");
        }


        if (file.exists()) {

            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            mIBSetImageUser.setImageDrawable(drawable);
        }
        else {

            if (Environment.MEDIA_MOUNTED.equals(state)) {

                file = new File(getContext().getExternalFilesDir(null), mConfig.getDomaineApp()+"photoLoaded.jpg");

            } else {

                file = new File(getContext().getFilesDir(), mConfig.getDomaineApp()+"photoLoaded.jpg");
            }

            if (file.exists()) {

                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                mIBSetImageUser.setImageDrawable(drawable);

                if (Environment.MEDIA_MOUNTED.equals(state)) {

                    file = new File(getContext().getExternalFilesDir(null), mConfig.getDomaineApp()+"photoProfil.jpg");

                } else {

                    file = new File(getContext().getFilesDir(), mConfig.getDomaineApp()+"photoProfil.jpg");
                }

                try {

                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

    private void callCamera() {


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);


    }


    private void  actionLogout() {


        /*
        final SharedPreferences sharedPref = getContext().getSharedPreferences(mConfig.getFileParameters(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("user_pseudo");
        editor.remove("user_email");
        editor.apply();
        */

        getActivity().finish();

    }


}
