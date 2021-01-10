package com.eduvision.version2.vima;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eduvision.version2.vima.Tabs.Fetching;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileSettings extends AppCompatActivity {
    TextView name, email, number, label;
    EditText editName, editEmail, editNumber;
    Button saving;
    private DatabaseReference mDatabase;

    public void swapVisibility(TextView tv, EditText n){
        //Swapping the visibility from TextView to EditText and vice-versa
        if(tv.getVisibility()==View.VISIBLE){
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    label.setText("Paramètres: Edit Mode");
                    tv.setVisibility(View.GONE);
                    n.setVisibility(View.VISIBLE);
                    n.setText(tv.getText());
                    return false;
                }
            });
        }
        else{
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    label.setText("Parametres: View Mode");
                    n.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(tv.getText());
                    return false;
                }
            });

        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        name = findViewById(R.id.name);
        email = findViewById(R.id.mail);
        number = findViewById(R.id.number);
        editEmail = findViewById(R.id.editMail);
        editName = findViewById(R.id.editName);
        editNumber = findViewById(R.id.editNumber);
        label = findViewById(R.id.label);
        label.setText("Paramètres: View Mode");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Fetching.makeCustomToast(getApplicationContext(), "Appuyer longuement sur une info pour la modifier.",
                Toast.LENGTH_SHORT);
        mDatabase.child("Custumers").child("000001").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameA = Objects.requireNonNull(dataSnapshot.child("first_name").getValue()).toString() + " " +Objects.requireNonNull(dataSnapshot.child("last_name").getValue()).toString();
                name.setText(nameA);
                String emailA = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                email.setText(emailA);
                String tel = Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.child("tel").getValue()).toString());
                number.setText(tel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        swapVisibility(name, editName);
        swapVisibility(email, editEmail);
        swapVisibility(number, editNumber);

        saving.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDatabase.child("Custumers").child("000001").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Rewrite the database with the data provided
                        if(name.getVisibility()==View.VISIBLE){
                            mDatabase.child("Custumers").child("000001").child("first_name").setValue(name.getText());
                        }
                        else{
                            mDatabase.child("Custumers").child("000001").child("first_name").setValue(editName.getText());
                        }
                        if(email.getVisibility()==View.VISIBLE){
                            mDatabase.child("Custumers").child("000001").child("email").setValue(name.getText());
                        }
                        else{
                            mDatabase.child("Custumers").child("000001").child("email").setValue(editName.getText());
                        }
                        if(number.getVisibility()==View.VISIBLE){
                            mDatabase.child("Custumers").child("000001").child("tel").setValue(name.getText());
                        }
                        else{
                            mDatabase.child("Custumers").child("000001").child("tel").setValue(editName.getText());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
