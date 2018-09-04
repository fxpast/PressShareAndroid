package com.prv.gootoor.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBFeedback;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.models.Feedback;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;

/**
 * Created by roger on 31/10/2017.
 */

public class FeedbackActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private EditText mIBFeedbackText;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mIBFeedbackText = (EditText) findViewById(R.id.IBFeedbackText);


    }

    public void actionCloseWidows(View view) {

        finish();
    }



    public void actionSend(View view) {

        if (mIBFeedbackText.getText().toString().length() == 0) {
            MyTools.sharedInstance().displayAlert(this,getText(R.string.emptyMessage));
            return;
        }

        Feedback feedback = new Feedback(this, null);
        MDBFeedback.sharedInstance().setAddFeedback(this, feedback, new MDBInterface() {
            @Override
            public void completionHandler(final Boolean success, final String errorString) {

                MyTools.sharedInstance().performUIUpdatesOnMain(FeedbackActivity.this, new MainThreadInterface() {
                    @Override
                    public void completionUpdateMain() {

                        if (success) {

                            MyTools.sharedInstance().displayAlert(FeedbackActivity.this,getText(R.string.feedbackMess));
                            mIBFeedbackText.setText("");

                        } else {

                            MyTools.sharedInstance().displayAlert(FeedbackActivity.this,errorString);
                        }


                    }
                });



            }
        });



    }


}
