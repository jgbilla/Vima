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

import java.util.ArrayList;

public class OrangeMoney extends AppCompatActivity {
    int lastId;
    private DatabaseReference mDatabase;
    public static ArrayList<ArrayList<String>> myList;

    public static ArrayList<ArrayList<String>> getMyList() {
        return myList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myList = new ArrayList<>(0);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        final GridView articlegv = findViewById(R.id.gridview);
        mDatabase.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastId = (int) dataSnapshot.getChildrenCount();
                String a1, a2, a3, a4, a5;
                for(int i=0; i<30; i++){
                    ArrayList<String> info = new ArrayList<String>(5);
                    a1 = dataSnapshot.child(Long.toString(lastId-i)).child("name").getValue().toString();
                    info.add(a1);
                    a2 = dataSnapshot.child(Long.toString(lastId-i)).child("price").getValue().toString();
                    info.add(a2);
                    a3 = dataSnapshot.child(Long.toString(lastId-i)).child("photo").getValue().toString();
                    info.add(a3);
                    a4 = dataSnapshot.child(Long.toString(lastId-i)).child("shop").getValue().toString();
                    info.add(a4);
                    a5 = dataSnapshot.child(Long.toString(lastId-i)).child("type").getValue().toString();
                    info.add(a5);
                    myList.add(info);
                }
                articlegv.setAdapter(new ArticleAdapter(OrangeMoney.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        setContentView(R.layout.orangemoney);
        articlegv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /*Get to specific chosen Article page,It is not complete yet tho*/
                Intent myIntent = new Intent(OrangeMoney.this, articlePage.class);
                myIntent.putExtra("id", id);
                startActivity(myIntent);
            }
        });
    }
}
