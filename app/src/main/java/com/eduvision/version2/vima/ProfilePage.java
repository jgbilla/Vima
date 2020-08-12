package com.eduvision.version2.vima;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static java.lang.String.format;

public class ProfilePage extends AppCompatActivity {
    public static ArrayList <String[]>  myArray;
    FirebaseStorage myFireBaseStorage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    int id;
    GridView gridView;
    TextView name, numb_likes, numb_fav;
    ImageView photo;
    ImageButton settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        gridView = findViewById(R.id.grid);
        name = findViewById(R.id.name);
        numb_likes = findViewById(R.id.number_of_likes);
        numb_fav = findViewById(R.id.number_of_favorites);
        photo = findViewById(R.id.profile_picture);
        final String[] eventDate = new String[1];
        final String[] eventTime = new String[1];
        settings = findViewById(R.id.settings);
        myArray = new ArrayList<>(3);

        mDatabase.child("Customers").child(Integer.toString(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameA = Objects.requireNonNull(dataSnapshot.child("first_name").getValue()).toString();
                nameA = nameA + Objects.requireNonNull(dataSnapshot.child("last_name").getValue()).toString();
                name.setText(nameA);
                String numb_likeA = Integer.toString((int) dataSnapshot.child("likedItems").getChildrenCount());
                numb_likes.setText(numb_likeA);
                String numb_favA = Integer.toString((int) dataSnapshot.child("favItems").getChildrenCount());
                numb_likes.setText(numb_likeA);
                numb_fav.setText(numb_favA);
                String photoA = dataSnapshot.child("photo").getValue().toString();
                Glide.with(getApplicationContext())
                        .load(photoA)
                        .into(photo);
                for(int i=1; i<=dataSnapshot.child("likedItems").getChildrenCount(); i++){
                    String[] temp = new String[5];
                    temp[2] = "Ajouté aux Favoris";
                    mDatabase.child("Articles");
                    mDatabase.child(Integer.toString(i));
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            temp[0] = Objects.requireNonNull(dataSnapshot.child("infos").child("name").getValue()).toString();
                            temp[3] = Objects.requireNonNull(dataSnapshot.child("pictures").child("p_photo").getValue()).toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // **********************************************************
                    // We're finding the date and time when the article was liked

                    eventDate[0] = Objects.requireNonNull(dataSnapshot.child("likedItems").child(Integer.toString(i)).child("eventDate").getValue()).toString();
                    eventTime[0] = Objects.requireNonNull(dataSnapshot.child("likedItems").child(Integer.toString(i)).child("eventTime").getValue()).toString();
                    // Convert these to a suitable DateTime object
                    DateTime targetDateTime = DateTime.parse(format("%s %s", eventDate[0], eventTime[0]), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
                    // Grab a timestamp for the current DateTime
                    DateTime now = DateTime.now();
                    // Find the period between the two DateTimes
                    Period period = new Period(now, targetDateTime);
                    temp[4] = Integer.toString(((int) now.getMillis()));
                    // Convert the period to suitable String
                    String prettyPeriod = PeriodFormat.getDefault().print(period);
                    temp[1] = "Il y a " + prettyPeriod;

                    //Waiting for the Article ValueListener to do its job as the loading is asynchronous
                    while(temp[0] == null || temp[3]==null){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myArray.add(temp);
                }

                //Sorting the array by ascending order of the millis that is in order of recentness
                Collections.sort(myArray, new Comparator<String[]>() {
                    public int compare(String[] time, String[] others) {
                        return time[4].compareTo(others[4]);
                    }
                });
                gridView.setAdapter(new Profile_Article_Adapter(myArray));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        /*Get to chosen Article page*/
                        Intent myIntent = new Intent(ProfilePage.this, articlePage.class);
                        myIntent.putExtra("id", myArray.get(position));
                        startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfilePage.this, ProfileSettings.class);
            }
        });

    }
}
