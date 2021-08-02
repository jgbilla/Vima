package com.eduvision.version2.vima.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Splashscreen;
import com.google.firebase.auth.FirebaseAuth;

public class TermsConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        LinearLayout wrapper = findViewById(R.id.wrapper);
        TextView continueReading = findViewById(R.id.continue_reading);
        wrapper.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        ScrollView message = findViewById(R.id.message);
        ViewGroup.LayoutParams params = message.getLayoutParams();
        params.height = 40;
        message.setLayoutParams(params);
        continueReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView message = findViewById(R.id.message);
                ViewGroup.LayoutParams params = message.getLayoutParams();
                params.height = params.height == 40 ? ViewGroup.LayoutParams.WRAP_CONTENT : 40;
                message.setLayoutParams(params);
                continueReading.setText(continueReading.getText().equals("Continuer la lecture")? "Finir la lecture" : "Continuer la lecture");
            }
        });

        Button acceptButton = findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("terms", true);
                editor.apply();
                        Intent a = new Intent(TermsConditions.this, login_activity.class);
                        startActivity(a);
                        finish();
            }
        });


    }
}