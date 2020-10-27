package com.eduvision.version2.vima.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eduvision.version2.vima.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_password extends AppCompatActivity {
Button sendEmail;
EditText email;
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        sendEmail = findViewById(R.id.envoyer_email);
        email = findViewById(R.id.new_email);


        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();

                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    Toast.makeText(Reset_password.this, "Inserez une adresse valide", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(Reset_password.this, "Verifiez votre boite mail s'il vous plait", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Reset_password.this, SeConnecter.class));
                            }

                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Reset_password.this, "Une erreur s'est produite, veuillez reessayer: "+message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}