package com.prv.pressshare;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Adil Soomro
 *
 */
public class SettingsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.list_param, null, false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.params);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                        getBaseContext(), LinearLayoutManager.VERTICAL, false
                )
        );
        recyclerView.setAdapter(new RecycleAdapter());

        setContentView(view);

    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_param, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {



            if (position == 0) {

                holder.txt.setText("Modifier mon profil");

            }
            else if (position == 1) {


                holder.txt.setText("Liste des transactions");

            }
            else if (position == 2) {

                holder.txt.setText("Mon abonnement");

            }
            else if (position == 3) {

                holder.txt.setText("Information de connexion");

            }
            else if (position == 4) {

                holder.txt.setText("Mes cartes bancaires");

            }
            else if (position == 5) {

                holder.txt.setText("Conditions d'utilisation");

            }


        }

        @Override
        public int getItemCount() {
            return 6;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(final View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_item_param);
            }
        }
    }


}
