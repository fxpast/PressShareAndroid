package com.prv.pressshare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.MapView;
import com.prv.pressshare.R;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;


public class ProductActivity extends AppCompatActivity  {

    private Config mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mConfig = Config.sharedInstance();

        ImageButton mIBProdClose = (ImageButton) findViewById(R.id.IBProdClose);
        mIBProdClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton mIBProdHelp = (ImageButton) findViewById(R.id.IBProdHelp);
        mIBProdHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo Tuto_Presentation
                MyTools.sharedInstance().showHelp("Tuto_Presentation", ProductActivity.this);
            }
        });

        ImageButton mIBProdMess = (ImageButton) findViewById(R.id.IBProdMess);
        mIBProdMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo email management
                MyTools.sharedInstance().displayAlert(ProductActivity.this,"email management");
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

}
