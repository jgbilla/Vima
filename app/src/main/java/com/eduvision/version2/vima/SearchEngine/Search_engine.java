package com.eduvision.version2.vima.SearchEngine;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Search_engine extends AppCompatActivity {


    DatabaseReference databaseReference;
    ArrayList<String> nameList;
    ArrayList<StorageReference> photoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_engine_layout);
    }

    public void setAdapter(String searchedString, RecyclerView searchResults, Context context,ArrayList<String> nameList,ArrayList<StorageReference> photoList ) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nameList.clear();
                photoList.clear();
                searchResults.removeAllViews();
                int counter = 0;

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String id= snapshot.getKey();
                    String name = snapshot.child("infos").child("name").getValue(String.class);
                    String photo = snapshot.child("pictures").child("s_pic1").getValue(String.class);

                    counter++;

                    if (photo!= null && name.toLowerCase().contains(searchedString.toLowerCase())) {

                        nameList.add(name);

                        String [] finalphoto = photo.split("/");
                        String test = finalphoto[finalphoto.length-1];
                        StorageReference reference = FirebaseStorage.getInstance().getReference().child(finalphoto[finalphoto.length-2]).child(test);

                        //ToDo: All images should have the same name as the item so that we can retrieve them easily. Add this feature here.


                        photoList.add(reference);
                        Log.d(TAG, "Value is: " + photoList);


                    }
                    if (counter==15){
                        break;
                    }

                    SearchAdapter searchAdapter = new SearchAdapter(context, nameList, photoList);
                    searchResults.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}