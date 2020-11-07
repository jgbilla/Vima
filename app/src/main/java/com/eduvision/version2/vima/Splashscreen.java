package com.eduvision.version2.vima;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

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
    public void getLikedItems(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String LikedItems = prefs.getString("LikedItems", null);
        Type type = new TypeToken<ArrayList<IndividualArticle>>() {}.getType();
            if (LikedItems != null && !LikedItems.equals("")) {
                Recents.myLikedItems = gson.fromJson(LikedItems, type);
            }
    }
    protected Boolean waitSecond(){
        final Boolean[] result = {false};
        if(!DownloadFilesTask.dataFetched || !Fetching.waitInternetAvailable(getApplicationContext())){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    if(DownloadFilesTask.dataFetched) {
                        result[0] = true;
                    }
                }
            }, 1000);
        }
        else{
            if(DownloadFilesTask.dataFetched) {
                result[0] = true;
            }
        }
        return result[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadFilesTask().execute();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        setContentView(R.layout.splashcreen);
        getLikedItems();
        mAuth = FirebaseAuth.getInstance();
        boolean firstStart = prefs.getBoolean("firstStart",true);


        image = findViewById(R.id.logo);
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.splashscreen_transition);
        image.startAnimation(myAnim);

        if (firstStart) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i<10; i++){
                        if(waitSecond()){
                            Intent a = new Intent(Splashscreen.this, com.eduvision.version2.vima.login_activity.class);
                            startActivity(a);
                            finish();
                        }
                        if(i>= 9 && !DownloadFilesTask.dataFetched) {
                            Fetching.makeCustomToast(getApplicationContext(), "Connectez-vous à Internet", Toast.LENGTH_LONG);
                            finish();
                        }
                    }
            }
            },SPLASH_TIME_OUT);

            prefs = getSharedPreferences("prefs",MODE_PRIVATE);
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
                                for(int i = 0; i<10; i++){
                                    if(waitSecond()){
                                        Intent a = new Intent(Splashscreen.this, com.eduvision.version2.vima.login_activity.class);
                                        startActivity(a);
                                        finish();
                                    }
                                    if(i>= 9 && !DownloadFilesTask.dataFetched) {
                                        Fetching.makeCustomToast(getApplicationContext(), "Connectez-vous à Internet", Toast.LENGTH_LONG);
                                        finish();
                                    }
                                }
                            }
                        },SPLASH_TIME_OUT);
                    }

                    if (firebaseAuth.getCurrentUser() == null){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i<10; i++){
                                    if(waitSecond()){
                                        Intent a = new Intent(Splashscreen.this, com.eduvision.version2.vima.login_activity.class);
                                        startActivity(a);
                                        finish();
                                    }
                                    if(i>= 9 && !DownloadFilesTask.dataFetched) {
                                        Fetching.makeCustomToast(getApplicationContext(), "Connectez-vous à Internet", Toast.LENGTH_LONG);
                                        finish();
                                    }
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
