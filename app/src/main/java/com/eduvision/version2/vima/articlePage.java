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

    Article info;
    Shop shop;
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
        sm_pic3 = findViewById(R.id.smaller_images3);
        sm_pic4 = findViewById(R.id.smaller_images4);
        shop_pic = findViewById(R.id.shop_picture);
        spin1 = findViewById(R.id.color_spinner);
        spin2 = findViewById(R.id.sex_spinner);

        mDatabase.child("Articles").child(Long.toString(article_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info = dataSnapshot.getValue(Article.class);
                mDatabase.child("Shops").child(info.getShop()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shop = dataSnapshot.getValue(Shop.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        price.setText(info.getPrice());
        title.setText(info.getName());
        description.setText(info.getDescription());
        shop_name.setText(info.getShop());
        shop_description.setText(shop.getDescription());
        shop_location.setText(shop.getLocation());

        //Setting the ImageResource of ImageViews from Firebase data
        Glide.with(mContext)
                .load(info.getPhoto())
                .into(big_pic);
        Glide.with(mContext)
                .load(info.getSmall_pic1())
                .into(sm_pic1);
        Glide.with(mContext)
                .load(info.getSmall_pic2())
                .into(sm_pic2);
        Glide.with(mContext)
                .load(info.getSmall_pic3())
                .into(sm_pic3);
        Glide.with(mContext)
                .load(info.getSmall_pic4())
                .into(sm_pic4);
        Glide.with(mContext)
                .load(shop.getPicture_logo())
                .into(shop_pic);

        setContentView(R.layout.indiv_article_page);

    }
}
