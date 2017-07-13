package com.prv.pressshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Adil Soomro
 *
 */
public class ParamActivity extends AppCompatActivity {

    private ListView listView;
    private String[] adapterData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.list_view_product);

        adapterData = new String[] { "Modifier mon profil","Liste des transactions","Mon abonnement",
                "Information de connexion","Mes cartes bancaires","Conditions d'utilisation"};

        listView.setAdapter(new ArrayAdapter<>(ParamActivity.this,R.layout.param_item, adapterData));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ParamActivity.this,"click index:"+position,Toast.LENGTH_SHORT).show();

            }
        });
    }

}
