package com.eduvision.version2.vima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OrangeMoney extends AppCompatActivity {

    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences =getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orangemoney);
        mDatabase.child("Articles").child("lastId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long test = (long) dataSnapshot.getValue();
                int lastId = (int) test;
                editor.putInt("lastId", lastId);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        GridView articlegv = findViewById(R.id.gridview);
        int FinalId = sharedPreferences.getInt("lastId", 0);
        articlegv.setAdapter(new ArticleAdapter(this, FinalId));
        articlegv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /*Get to specific chosen Article page*/
                Intent myIntent = new Intent(OrangeMoney.this, articlePage.class);
                myIntent.putExtra("id", id);
                startActivity(myIntent);
            }
        });
    }
}
