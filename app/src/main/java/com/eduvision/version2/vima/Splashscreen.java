package com.eduvision.version2.vima;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splashscreen extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);


        image = findViewById(R.id.logo);
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.splashscreen_transition);
        image.startAnimation(myAnim);

        if (firstStart) {
            final Intent i = new Intent(this, MainActivity.class);

            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        startActivity(i);
                        finish();

                    }
                }
            };
            timer.start();

            prefs = getSharedPreferences("prefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }
        else {
            final Intent i = new Intent(this, MainActivity.class);

            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        startActivity(i);
                        finish();

                    }
                }
            };
            timer.start();

        }



    }
}
