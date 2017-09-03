package com.prv.pressshare.activities.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prv.pressshare.R;
import com.prv.pressshare.lists.Products;
import com.prv.pressshare.models.Product;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.views.CustomItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ListProductFragment extends Fragment {


    Button mIBListProd;
    Button mIBMesListProd;
    Button mIBHistProd;
    private Config mConfig;
    private ListView mIBListListView;
;

    public ListProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        mIBListProd = (Button) view.findViewById(R.id.IBListProd);
        mIBMesListProd = (Button) view.findViewById(R.id.IBMesListProd);
        mIBHistProd = (Button) view.findViewById(R.id.IBHistProd);
        mConfig = Config.sharedInstance();

        TextView mIBListTitle = (TextView) view.findViewById(R.id.IBListTitle);
        mIBListTitle.setText(mConfig.getUser_pseudo()+"("+mConfig.getUser_id()+")");


        mIBListProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSelectButton(v);
            }
        });

        mIBMesListProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionSelectButton(v);
            }
        });

        mIBHistProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionSelectButton(v);
            }
        });



        List<Object> aList = new ArrayList<>();


        try {

            for (int i = 0; i < Products.sharedInstance().getProductsArray().length() ; i++) {

                JSONObject dictionary = Products.sharedInstance().getProductsArray().getJSONObject(i);
                Product product = new Product(getContext(), dictionary);
                aList.add(i, product);
            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }


        ListAdapter mAdapter = new CustomItem(getContext(),R.layout.list_item, aList);

        mIBListListView = (ListView) view.findViewById(R.id.IBListListView);

        mIBListListView.setAdapter(mAdapter);

        mIBListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Todo List product rows
                MyTools.sharedInstance().showHelp("click index:"+position, getContext());
            }
        });


        ImageButton mIBListLogout = (ImageButton) view.findViewById(R.id.IBListLogout);
        mIBListLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //action logout
                actionLogout();

            }
        });


        ImageButton mIBListHelp = (ImageButton) view.findViewById(R.id.IBListHelp);
        mIBListHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo Tuto_Presentation
                MyTools.sharedInstance().showHelp("Tuto_Presentation", getContext());

            }
        });


        ImageButton mIBListLoop = (ImageButton) view.findViewById(R.id.IBListLoop);
        mIBListLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo show product search field
                MyTools.sharedInstance().showHelp("show product search field", getContext());

            }
        });

        FloatingActionButton mIBListAddProduct = (FloatingActionButton)  view.findViewById(R.id.IBListAddProduct);
        mIBListAddProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Todo  floating action buttion function
                MyTools.sharedInstance().showHelp("floating action buttion function", getContext());
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


    private void actionSelectButton(View view) {

        defaultColorButton();
        Button button = (Button) view;
        button.setBackgroundColor(Color.BLUE);
        button.setTextColor(Color.WHITE);

    }


    private void defaultColorButton() {

        mIBListProd.setBackgroundColor(Color.LTGRAY);
        mIBListProd.setTextColor(Color.BLUE);

        mIBMesListProd.setBackgroundColor(Color.LTGRAY);
        mIBMesListProd.setTextColor(Color.BLUE);

        mIBHistProd.setBackgroundColor(Color.LTGRAY);
        mIBHistProd.setTextColor(Color.BLUE);

    }



}
