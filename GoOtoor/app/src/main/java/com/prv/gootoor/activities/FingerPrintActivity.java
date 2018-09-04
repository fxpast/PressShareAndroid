//
//  FingerPrintActivity
//  PressShare
//
//  Description : Sign in with finger print
//
//  Created by MacbookPRV on 25/05/2018.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


package com.prv.gootoor.activities;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.prv.gootoor.R;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.FingerprintHandler;
import com.prv.gootoor.utils.MyTools;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerPrintActivity extends AppCompatActivity {


    private Config mConfig = Config.sharedInstance();

    private ProgressBar mIBActivity;


    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private static final String KEY_NAME = "yourKey";
    private Handler timerFinger;
    private Runnable runnableFinger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);


        mIBActivity = (ProgressBar) findViewById(R.id.IBProgressBar);


        // If you’ve set your app’s minSdkVersion to anything lower than 23, then you’ll
        // need to verify that the device is running Marshmallow
        // or higher before executing any fingerprint-related code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get an instance of KeyguardManager and FingerprintManager//
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            //Check whether the device has a fingerprint sensor//

            if (!fingerprintManager.isHardwareDetected()) {
                MyTools.sharedInstance().displayAlert(this,getString(R.string.authFingerPrint));
            }

            //Check whether the user has granted your app the USE_FINGERPRINT permission//
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                MyTools.sharedInstance().displayAlert(this,getString(R.string.authFingerPrint));

            }

            //Check that the user has registered at least one fingerprint//
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                MyTools.sharedInstance().displayAlert(this,getString(R.string.setupFingerPrint));

            }

            //Check that the lockscreen is secured//
            if (!keyguardManager.isKeyguardSecure()) {
                MyTools.sharedInstance().displayAlert(this,getString(R.string.lockscreenFingerP));

            } else {
                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    //If the cipher is initialized successfully, then create a CryptoObject instance//
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // Here, I’m referencing the FingerprintHandler class that we’ll
                    // create in the next section. This class will be responsible
                    // for starting the authentication process (via the startAuth method) and processing
                    // the authentication process events//
                    FingerprintHandler helper = new FingerprintHandler(this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }


            }



        }


    }



    @Override
    protected void onStart() {
        super.onStart();

        timerFinger = new Handler();
        timerFinger.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mConfig.getIsTimer()) {

                    if (mConfig.getIsFingerPrint()) {
                        finish();
                    }
                    runnableFinger = this;
                    timerFinger.postDelayed(runnableFinger, 3000);
                }
            }
        }, 3000);

        initColor();

        mIBActivity.setVisibility(View.VISIBLE);



    }


    @Override
    protected void onPause() {

        mConfig.setIsTimer(false);
        timerFinger.removeCallbacks(runnableFinger);
        runnableFinger = null;


        super.onPause();
    }


    @Override
    protected void onDestroy() {
        mConfig.setIsTimer(false);
        super.onDestroy();
    }



    public void actionCloseWidows(View view) {

        mIBActivity.setVisibility(View.GONE);

        finish();
    }




    private void initColor() {

        ConstraintLayout mIBFingerBackgr;
        ImageView mIBImageFinger;

        mIBFingerBackgr = (ConstraintLayout) findViewById(R.id.IBFingerBackgr);

        mIBImageFinger = (ImageView) findViewById(R.id.IBImageFinger);

        String hexColor = "#" + mConfig.getColorApp();
        mIBFingerBackgr.setBackgroundColor(Color.parseColor(hexColor));
        mIBImageFinger.setBackgroundColor(Color.parseColor(hexColor));


    }



    //Create the generateKey method that we’ll use to gain access to the
    // Android keystore and generate the encryption key//

    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore
            // container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }


    //Create a new method that we’ll use to initialize our cipher//
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }


    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }




}
