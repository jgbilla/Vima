package com.eduvision.version2.vima;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.pusher.pushnotifications.PushNotifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eduvision.version2.vima.Tabs.DownloadFilesTask;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Splashscreen extends AppCompatActivity {

    ImageView image;
    FirebaseAuth mAuth;
    private static int SPLASH_TIME_OUT = 1000;
    FirebaseAuth.AuthStateListener mAuthListener;

    protected Boolean waitSecond(){
        final Boolean[] result = {false};
        Log.println(Log.INFO, "Debug", "Wait Second");
        Log.println(Log.INFO, "Debug", String.valueOf(Fetching.isHomeDataFetched));
        if(!(Fetching.myData.size() >= 3) || !Fetching.waitInternetAvailable(getApplicationContext())){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    if(Fetching.myData.size() >= 3) {
                        result[0] = true;
                        Log.println(Log.INFO, "Debug", "Inside Run");
                    }
                }
            }, 10000);
        }
        else{
            if(Fetching.myData.size() >= 3) {
                result[0] = true;
            }
        }
        return result[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        PushNotifications.start(getApplicationContext(), "eec5d6e9-94f3-4b3c-ad59-712e60e138c6");
        PushNotifications.addDeviceInterest("hello");

        setContentView(R.layout.splashcreen);
        image = findViewById(R.id.logo);
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.splashscreen_transition);
        image.startAnimation(myAnim);
        mAuth = FirebaseAuth.getInstance();
        boolean firstStart = prefs.getBoolean("firstStart",true);



        if (firstStart) {
            prefs = getSharedPreferences("prefs",MODE_PRIVATE);
            editor.putBoolean("firstStart", false);
            editor.apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*
                    for(int i = 0; i<50; i++){
                        Log.println(Log.INFO, "Debug", "Inside If RUN " + i + " times");
                        if(waitSecond()){
                            Intent a = new Intent(Splashscreen.this, com.eduvision.version2.vima.Login.login_activity.class);
                            startActivity(a);
                            finish();
                        }
                        if(i== 49 ) {
                            Fetching.makeCustomToast(getApplicationContext(), "Connectez-vous à Internet", Toast.LENGTH_LONG);
                            finish();
                        }
                    }

             */
                    Intent a = new Intent(Splashscreen.this, com.eduvision.version2.vima.Login.login_activity.class);
                    startActivity(a);
                    finish();

            }
            },SPLASH_TIME_OUT);



        }
        else {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() != null){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent a = new Intent(Splashscreen.this, Spinning.class);
                                startActivity(a);
                                finish();
                            }
                        },SPLASH_TIME_OUT);
                    }

                    if (firebaseAuth.getCurrentUser() == null){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent a = new Intent(Splashscreen.this, com.eduvision.version2.vima.Login.login_activity.class);
                                startActivity(a);
                                finish();
                            }
                        },SPLASH_TIME_OUT);


                    }

                }
            };
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
}
