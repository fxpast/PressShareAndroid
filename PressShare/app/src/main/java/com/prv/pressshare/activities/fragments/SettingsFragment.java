package com.prv.pressshare.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.prv.pressshare.R;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;


public class SettingsFragment extends Fragment {

    private Config mConfig;
    private ListView mIBSetListView;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mConfig = Config.sharedInstance();

        TextView mIBSetTitle = (TextView) view.findViewById(R.id.IBSetTitle);
        mIBSetTitle.setText(mConfig.getUser_pseudo()+"("+mConfig.getUser_id()+")");

         String[] mActionsArray = new String[] {getString(R.string.editProfil), getString(R.string.runTransac),getString(R.string.mySubscrit),
                getString(R.string.connectInfo),getString(R.string.myCB),getString(R.string.termsOfUse)};

        ListAdapter mAdapter = new ArrayAdapter<>(getActivity(), R.layout.settings_item, mActionsArray);

        mIBSetListView = (ListView) view.findViewById(R.id.IBSetListView);

        mIBSetListView.setAdapter(mAdapter);

        mIBSetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Todo Settings rows
                MyTools.sharedInstance().showHelp("click index:"+position, getContext());
            }
        });


        ImageButton mIBSetImageUser = (ImageButton) view.findViewById(R.id.IBSetImageUser);


        TextView mIBSetProfil = (TextView) view.findViewById(R.id.IBSetProfil);
        mIBSetProfil.setText(mConfig.getUser_nom()+" "+mConfig.getUser_nom()+" ("+mConfig.getUser_note()+" "+getString(R.string.star)+") "+mConfig.getUser_email());

        mIBSetImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo Open Camera
                MyTools.sharedInstance().showHelp("Camera is openning", getContext());
            }
        });

        ImageButton mIBSetLogout = (ImageButton) view.findViewById(R.id.IBSetLogout);
        mIBSetLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action logout
                actionLogout();
            }
        });


        ImageButton mIBSetHelp = (ImageButton) view.findViewById(R.id.IBSetHelp);
        mIBSetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo Tuto_Presentation
                MyTools.sharedInstance().showHelp("Tuto_Presentation", getContext());
            }
        });


        return  view;
    }


    private void  actionLogout() {

        final SharedPreferences sharedPref = getContext().getSharedPreferences(mConfig.getFileParameters(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("user_pseudo");
        editor.remove("user_email");
        editor.apply();
        getActivity().finish();

    }



}
