package com.eduvision.version2.vima;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class articlePage extends AppCompatActivity {

    Article_info info;
    Article_description bDescription;
    Article_pictures bPictures;

    IndividualArticleConstructor individualArticleConstructor;
    private long article_id;
    Context mContext;
    FirebaseStorage myFireBaseStorage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    TextView price, title, description, shop_name, shop_description, shop_location;
    ImageView big_pic, sm_pic1, sm_pic2, sm_pic3, sm_pic4, shop_pic;
    Spinner spin1, spin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indiv_article_page);
        Bundle i = getIntent().getExtras();
        if(i!=null) {
            article_id = i.getLong("id");
        }

        // Refer to indiv_article_page.xml to see what those are all about
        price = findViewById(R.id.article_price);
        title = findViewById(R.id.article_title);
        description = findViewById(R.id.article_description);
        shop_name = findViewById(R.id.shop_name);
        shop_description = findViewById(R.id.shop_description);
        shop_location = findViewById(R.id.shop_location);
        big_pic = findViewById(R.id.big_picture);
        sm_pic1 = findViewById(R.id.smaller_images1);
        sm_pic2 = findViewById(R.id.smaller_images2);
        shop_pic = findViewById(R.id.shop_picture);
        spin1 = findViewById(R.id.color_spinner);
        spin2 = findViewById(R.id.sex_spinner);

        mDatabase.child("Articles").child(Long.toString(article_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info = dataSnapshot.child("infos").child("name").getValue(Article_info.class);
                bDescription = dataSnapshot.child("infos").child("name").getValue(Article_description.class);
                bPictures = dataSnapshot.child("infos").child("name").getValue(Article_pictures.class);

                mDatabase.child("Shops").child(Integer.toString(info.getSeller_id())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        individualArticleConstructor = dataSnapshot.getValue(IndividualArticleConstructor.class);
                        //I am adding all of this inside of the onDataChange because of the asynchronous loading
                        shop_name.setText(individualArticleConstructor.getName());
                        shop_description.setText(individualArticleConstructor.getDescription());
                        shop_location.setText(individualArticleConstructor.getLocation());
                        Glide.with(mContext)
                                .load(individualArticleConstructor.getPicture_logo())
                                .into(shop_pic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                //I am adding all of this inside of the onDataChange function because of the asynchronous loading
                price.setText(info.getPrice());
                title.setText(info.getName());
                description.setText(bDescription.getDescription());

                //Setting the ImageResource of ImageViews from Firebase data
                Glide.with(mContext)
                        .load(bPictures.getPhoto())
                        .into(big_pic);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic1())
                        .into(sm_pic1);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic2())
                        .into(sm_pic2);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic3())
                        .into(sm_pic3);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic4())
                        .into(sm_pic4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
}
