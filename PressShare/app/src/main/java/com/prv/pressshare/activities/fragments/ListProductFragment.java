package com.prv.pressshare.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.prv.pressshare.R;
import com.prv.pressshare.activities.ProductActivity;
import com.prv.pressshare.daos.MDBInterfaceArray;
import com.prv.pressshare.daos.MDBProduct;
import com.prv.pressshare.lists.Products;
import com.prv.pressshare.models.Product;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.views.CustomItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class ListProductFragment extends Fragment {


    private Button mIBListProd;
    private Button mIBMesListProd;
    private Button mIBHistProd;
    private Config mConfig;
    private ListView mIBListListView;
    private EditText mIBListSearchPro;
    private int mTypeListe = 0; //List :0, MyList :1, Historical:2
    private List<Object> mProducts;

    public ListProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        mProducts = new ArrayList<>();

        mIBListProd = (Button) view.findViewById(R.id.IBListProd);
        mIBMesListProd = (Button) view.findViewById(R.id.IBMesListProd);
        mIBHistProd = (Button) view.findViewById(R.id.IBHistProd);
        mConfig = Config.sharedInstance();

        mIBListProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTypeListe = 0;
                actionSelectButton(v);
                ActionSearchProduct(mIBListSearchPro.getText().toString(), mTypeListe);
            }
        });

        mIBMesListProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTypeListe = 1;
                actionSelectButton(v);
                actionMenuMyList();
            }
        });

        mIBHistProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTypeListe = 2;
                actionSelectButton(v);
                actionMenuHistory();
            }
        });

        mIBListListView = (ListView) view.findViewById(R.id.IBListListView);

        mIBListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), ProductActivity.class);
                Product product =(Product) mProducts.get(position);

                intent.putExtra(mConfig.getDomaineApp()+"prod_id", product.getProd_id());
                intent.putExtra(mConfig.getDomaineApp()+"typeListe", mTypeListe);
                startActivity(intent);

            }
        });

        mIBListSearchPro = (EditText) view.findViewById(R.id.IBListSearchPro);
        ActionSearchProduct(mIBListSearchPro.getText().toString(), mTypeListe);
        mIBListSearchPro.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        mIBListSearchPro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ActionSearchProduct(s.toString(), mTypeListe);

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        ImageView mIBListLogout = (ImageView) view.findViewById(R.id.IBListLogout);
        mIBListLogout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //action logout
                actionLogout();
                return false;
            }
        });



        ImageView mIBListHelp = (ImageView) view.findViewById(R.id.IBListHelp);
        mIBListHelp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Todo Tuto_Presentation
                MyTools.sharedInstance().showHelp("Tuto_Presentation", getContext());
                return false;
            }
        });



        FloatingActionButton mIBListAddProduct = (FloatingActionButton)  view.findViewById(R.id.IBListAddProduct);
        mIBListAddProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ProductActivity.class));

            }
        });


        return  view;
    }


    private void actionMenuMyList() {

        MDBProduct.sharedInstance().getProductsByUser(getContext(), mConfig.getUser_id(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                if (success) {

                    try {

                        Products.sharedInstance().setProductsUserArray(anArray);
                        MyTools.sharedInstance().performUIUpdatesOnMain(getContext(), new MainThreadInterface() {

                            @Override
                            public void completionUpdateMain() {
                                ActionSearchProduct(mIBListSearchPro.getText().toString(), mTypeListe);

                            }
                        });


                    }  catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(getContext(), new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            MyTools.sharedInstance().displayAlert(getContext(),errorString);

                        }
                    });
                }
            }
        });
    }


    private void actionMenuHistory() {

        MDBProduct.sharedInstance().getProductsByTrader(getContext(), mConfig.getUser_id(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                if (success) {

                    try {

                        Products.sharedInstance().setProductsTraderArray(anArray);
                        MyTools.sharedInstance().performUIUpdatesOnMain(getContext(), new MainThreadInterface() {

                            @Override
                            public void completionUpdateMain() {
                                ActionSearchProduct(mIBListSearchPro.getText().toString(), mTypeListe);

                            }
                        });


                    }  catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(getContext(), new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            MyTools.sharedInstance().displayAlert(getContext(),errorString);

                        }
                    });
                }

            }
        });

    }



    private void ActionSearchProduct(String value, int  typeListe) {

        mProducts.clear();
        //List
        JSONArray productsJSON = Products.sharedInstance().getProductsArray();

         if (typeListe == 1) {

            //MyList
            productsJSON = Products.sharedInstance().getProductsUserArray();

        } else  if (typeListe == 2) {

            //History
            productsJSON = Products.sharedInstance().getProductsTraderArray();
        }


        try {

            for (int i = 0; i < productsJSON.length() ; i++) {

                JSONObject dictionary = productsJSON.getJSONObject(i);
                Product product = new Product(getContext(), dictionary);
                String nom = product.getProd_nom().toLowerCase();

                if (value.equals("")) {

                    mProducts.add(i, product);

                } else {

                    if (nom.contains(value.toLowerCase())) {
                        mProducts.add(product);
                    }

                }

            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter mAdapter = new CustomItem(getContext(),R.layout.list_item, mProducts);
        mIBListListView.setAdapter(mAdapter);


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
