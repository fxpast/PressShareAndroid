package com.prv.pressshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity {
    private ListView listView;
    private String[] adapterData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        listView = (ListView)findViewById(R.id.list_field);
        adapterData = new String[] { "Photo", "Etat du produit", "Nom","Prix","Temps dispo","Commentaire",
                "Echange possible","Lieu","Map"};

        listView.setAdapter(new ArrayAdapter<>(ProductActivity.this,android.R.layout.simple_list_item_1, adapterData));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProductActivity.this,"click index:"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
