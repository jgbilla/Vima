package com.eduvision.version2.vima.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Splashscreen;

public class TermsConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        Button acceptButton = findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(TermsConditions.this, com.eduvision.version2.vima.Login.login_activity.class);
                startActivity(a);
                finish();
            }
        });
    }
}