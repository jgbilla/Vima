package com.eduvision.version2.vima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.eduvision.version2.vima.Tabs.DownloadFilesTask;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.google.firebase.auth.FirebaseAuth;

public class Splashscreen extends AppCompatActivity {

    ImageView image;
    FirebaseAuth mAuth;
    private static int SPLASH_TIME_OUT = 1000;
    FirebaseAuth.AuthStateListener mAuthListener;
    protected Boolean waitSecond(){
        final Boolean[] result = {false};
        if(!DownloadFilesTask.dataFetched || !Fetching.isInternetAvailable(getApplicationContext())){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    if(DownloadFilesTask.dataFetched) {
                        result[0] = true;
                    }
                }
            }, 400);
        }
        else{
            result[0] = true;
        }
        return result[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadFilesTask().execute();
        setContentView(R.layout.activity_splashscreen);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);


        image = findViewById(R.id.logo);
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.splashscreen_transition);
        image.startAnimation(myAnim);

        if (firstStart) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i<5; i++){
                        if(waitSecond()){
                            Intent a = new Intent(Splashscreen.this, login_activity.class);
                            startActivity(a);
                            finish();
                        }
                    }
                    if(!DownloadFilesTask.dataFetched) {
                        Fetching.makeCustomToast(getApplicationContext(), "Action Impossible", Toast.LENGTH_LONG);
                    }
            }
            },SPLASH_TIME_OUT);

            prefs = getSharedPreferences("prefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }
        else {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() != null){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i<5; i++){
                                    if(waitSecond()){
                                        Intent a = new Intent(Splashscreen.this, MainPage.class);
                                        startActivity(a);
                                        finish();
                                    }
                                }
                                if(!DownloadFilesTask.dataFetched) {
                                    Fetching.makeCustomToast(getApplicationContext(), "Action Impossible", Toast.LENGTH_LONG);
                                }
                            }
                        },SPLASH_TIME_OUT);
                    }

                    if (firebaseAuth.getCurrentUser() == null){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i<5; i++){
                                    if(waitSecond()){
                                        Intent a = new Intent(Splashscreen.this, login_activity.class);
                                        startActivity(a);
                                        finish();
                                    }
                                }
                                if(!DownloadFilesTask.dataFetched) {
                                    Fetching.makeCustomToast(getApplicationContext(), "Action Impossible", Toast.LENGTH_LONG);
                                }
                            }
                        },SPLASH_TIME_OUT);


                    }

                }
            };
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

}
