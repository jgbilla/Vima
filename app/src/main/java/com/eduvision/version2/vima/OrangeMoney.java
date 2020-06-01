package com.eduvision.version2.vima;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrangeMoney extends AppCompatActivity {
    int lastId;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        final GridView articlegv = findViewById(R.id.gridview);
        mDatabase.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastId = (int) dataSnapshot.getChildrenCount();
                articlegv.setAdapter(new ArticleAdapter(OrangeMoney.this, lastId));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        setContentView(R.layout.orangemoney);
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
