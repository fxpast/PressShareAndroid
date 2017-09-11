package com.prv.pressshare.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import com.prv.pressshare.R;
import com.prv.pressshare.lists.Products;
import com.prv.pressshare.models.Product;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.views.ImageDownloader;
import com.prv.pressshare.views.ImageInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ProductActivity extends AppCompatActivity  {

    private Config mConfig;
    private Product mProduct = null;
    private ImageView mIBProdAddImage;
    private final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mConfig = Config.sharedInstance();

        mIBProdAddImage = (ImageView) findViewById(R.id.IBProdAddImage);
        mIBProdAddImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                }
                return false;
            }
        });

        int prodId  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"prod_id",0);
        int typeListe  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"typeListe",0);

        try {

            JSONArray productsJSON = Products.sharedInstance().getProductsArray();

            if (typeListe == 1) {

                //MyList
                productsJSON = Products.sharedInstance().getProductsUserArray();

            } else  if (typeListe == 2) {

                //History
                productsJSON = Products.sharedInstance().getProductsTraderArray();
            }
            for (int i = 0; i < productsJSON.length(); i++) {

                JSONObject dictionary = productsJSON.getJSONObject(i);
                Product product = new Product(this, dictionary);
                if (product.getProd_id() == prodId) {
                    mProduct = product;
                    fillTheForm();
                }

            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView mIBProdClose = (ImageView) findViewById(R.id.IBProdClose);
        mIBProdClose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                finish();
                return false;
            }
        });


        ImageView mIBProdHelp = (ImageView) findViewById(R.id.IBProdHelp);
        mIBProdHelp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Todo Tuto_Presentation
                MyTools.sharedInstance().showHelp("Tuto_Presentation", ProductActivity.this);
                return false;
            }
        });


        ImageView mIBProdMess = (ImageView) findViewById(R.id.IBProdMess);
        mIBProdMess.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Todo email management
                MyTools.sharedInstance().displayAlert(ProductActivity.this,"email management");
                return false;
            }
        });

        Button mIBProdValid = (Button) findViewById(R.id.IBProdValid);
        mIBProdValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo the product is saving
                MyTools.sharedInstance().displayAlert(ProductActivity.this,"the product is saving");
            }
        });

        final EditText mIBInfoLocation = (EditText) findViewById(R.id.IBInfoLocation);
        final EditText mIBNom = (EditText) findViewById(R.id.IBNom);
        final EditText mIBComment = (EditText) findViewById(R.id.IBComment);

        Button mIBFind = (Button) findViewById(R.id.IBFind);
        mIBFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo trouver sur la carte

                Intent intent = new Intent(getBaseContext(), MapProdActivity.class);
                intent.putExtra(mConfig.getDomaineApp()+"IBInfoLocation", mIBInfoLocation.getText().toString());
                intent.putExtra(mConfig.getDomaineApp()+"IBNom", mIBNom.getText().toString());
                intent.putExtra(mConfig.getDomaineApp()+"IBComment", mIBComment.getText().toString());
                startActivity(intent);
            }
        });


        Button mIBMyPositon = (Button) findViewById(R.id.IBMyPositon);
        mIBMyPositon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), MapProdActivity.class);
                intent.putExtra(mConfig.getDomaineApp()+"IBInfoLocation", "");
                intent.putExtra(mConfig.getDomaineApp()+"IBNom", mIBNom.getText().toString());
                intent.putExtra(mConfig.getDomaineApp()+"IBComment", mIBComment.getText().toString());
                startActivity(intent);

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mIBProdAddImage.setImageBitmap(imageBitmap);

        }
    }


    private void fillTheForm() {

        Switch mIBEchangeChoice = (Switch) findViewById(R.id.IBEchangeChoice);
        mIBEchangeChoice.setChecked(mProduct.getProd_echange());
        EditText mIBNom = (EditText) findViewById(R.id.IBNom);
        mIBNom.setText(mProduct.getProd_nom());
        EditText mIBPrix = (EditText) findViewById(R.id.IBPrix);
        mIBPrix.setText(mProduct.getProd_prix().toString());
        EditText mIBComment = (EditText) findViewById(R.id.IBComment);
        mIBComment.setText(mProduct.getProd_comment());
        EditText mIBTemps = (EditText) findViewById(R.id.IBTemps);
        mIBTemps.setText(mProduct.getProd_tempsDispo());
        EditText mIBInfoLocation = (EditText) findViewById(R.id.IBInfoLocation);
        mIBInfoLocation.setText(mProduct.getProd_mapString());


        String  mUrl = mConfig.getUrlServer()+"/images/"+mProduct.getProd_imageUrl()+".jpg";

        ImageDownloader downloadImage =  new ImageDownloader(this, new ImageInterface() {
            @Override
            public void completionHandlerImage(final Boolean success, final Drawable drawable) {

                MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                    @Override
                    public void completionUpdateMain() {
                        if (success) {
                            mIBProdAddImage.setImageDrawable(drawable);

                        } else {
                            mIBProdAddImage.setImageResource(R.drawable.noimage);
                        }
                    }
                });

            }
        });

        downloadImage.execute(mUrl);



    }


}
