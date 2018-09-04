package com.prv.gootoor.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prv.gootoor.R;
import com.prv.gootoor.activities.ProductActivity;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBProduct;
import com.prv.gootoor.lists.Products;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.views.CustomItemProduct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class ListProductFragment extends Fragment {


    private Button mIBListProd;
    private Button mIBMesListProd;
    private Button mIBHistProd;
    private Config mConfig = Config.sharedInstance();
    private ListView mIBListListView;
    private EditText mIBListSearchPro;
    public int mTypeSListe = 0; //List :0, MyList :1, Historical:2
    private List<Object> mProducts;
    private FloatingActionButton mIBListAddProduct;

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

        mIBListProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             actionListProd(v);
            }
        });

        mIBMesListProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             actionMesListProd(v);
            }
        });

        mIBHistProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             actionHistProd(v);
            }
        });

        mIBListListView = (ListView) view.findViewById(R.id.IBListListView);

        mIBListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Product product =(Product) mProducts.get(position);

            Intent intent = new Intent(getContext(), ProductActivity.class);
            intent.putExtra(mConfig.getDomaineApp()+"prod_id", product.getProd_id());
            intent.putExtra(mConfig.getDomaineApp()+"typeListe", 1);
            intent.putExtra(mConfig.getDomaineApp()+"typeSListe", mTypeSListe);
            startActivity(intent);

            }
        });

        mIBListSearchPro = (EditText) view.findViewById(R.id.IBListSearchPro);

        if (mTypeSListe == 0) {

            actionListProd(mIBListProd);

        } else if (mTypeSListe == 1) {

            actionMesListProd(mIBMesListProd);

        } else if (mTypeSListe == 2) {

            actionHistProd(mIBHistProd);

        }

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

                ActionSearchProduct(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        ImageView mIBListLogout = (ImageView) view.findViewById(R.id.IBListLogout);
        mIBListLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionLogout();
            }
        });


        ImageView mIBListHelp = (ImageView) view.findViewById(R.id.IBListHelp);
        mIBListHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTools.sharedInstance().showHelp("carte_liste", getContext());
            }
        });


        mIBListAddProduct = (FloatingActionButton)  view.findViewById(R.id.IBListAddProduct);
        mIBListAddProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mConfig.getLevel() > 0) {

                    Intent intent = new Intent(getContext(), ProductActivity.class);
                    intent.putExtra(mConfig.getDomaineApp()+"prod_id", 0);
                    intent.putExtra(mConfig.getDomaineApp()+"typeListe", 1);
                    intent.putExtra(mConfig.getDomaineApp()+"typeSListe", mTypeSListe);
                    startActivity(intent);

                }

            }
        });


        return  view;
    }



    @Override
    public void onStart() {
        super.onStart();

        initColor();


        if (mConfig.getLevel() <= 0) {
            mIBListAddProduct = (FloatingActionButton) MyTools.sharedInstance().activerObjet(mIBListAddProduct, false);

        } else {
            mIBListAddProduct = (FloatingActionButton) MyTools.sharedInstance().activerObjet(mIBListAddProduct, true);

        }


    }


    private void initColor() {

        String hexColor = "#" + mConfig.getColorAppPlHd();
        mIBListSearchPro.setHintTextColor(Color.parseColor(hexColor));
        hexColor = "#" + mConfig.getColorAppText();
        mIBListSearchPro.setTextColor(Color.parseColor(hexColor));

    }



    private void actionListProd(View v) {

        mTypeSListe = 0;
        actionSelectButton(v);
        ActionSearchProduct(mIBListSearchPro.getText().toString());

    }


    private void actionMesListProd(View v) {

        mTypeSListe = 1;
        actionSelectButton(v);
        actionMenuMyList();

    }


    private void actionHistProd(View v) {

        mTypeSListe = 2;
        actionSelectButton(v);
        actionMenuHistory();

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
                                ActionSearchProduct(mIBListSearchPro.getText().toString());

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
                                ActionSearchProduct(mIBListSearchPro.getText().toString());

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



    private void ActionSearchProduct(String value) {

        mProducts.clear();
        //List
        JSONArray productsJSON = Products.sharedInstance().getProductsArray();

         if (mTypeSListe == 1) {

            //MyList
            productsJSON = Products.sharedInstance().getProductsUserArray();

        } else  if (mTypeSListe == 2) {

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

        ListAdapter mAdapter = new CustomItemProduct(getContext(),R.layout.list_item, mProducts);
        mIBListListView.setAdapter(mAdapter);


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
