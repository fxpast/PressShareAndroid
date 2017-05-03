package com.prv.pressshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button attemptSuggestion = (Button) findViewById(R.id.suggestion_button);
        attemptSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSuggestion();
            }
        });


    }


    private void attemptSuggestion() {

        startActivity(new Intent(this, SuggestionActivity.class));

    }


}
