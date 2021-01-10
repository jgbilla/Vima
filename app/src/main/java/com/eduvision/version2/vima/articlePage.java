package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Popular;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.eduvision.version2.vima.Tabs.Fetching.myData;
import static com.eduvision.version2.vima.Tabs.Recents.likedItemsPosition;
import static com.facebook.FacebookSdk.getApplicationContext;

public class articlePage extends AppCompatActivity {

    long article_id;
    TextView price, title, description, shop_name, shop_description, shop_location;
    ImageView  big_pic, sm_pic1, sm_pic2, sm_pic3, shop_pic;
    CircleImageView profile_picture;
    String phoneNumber;
    CardView like;

    public void whatclick(IndividualArticle article){
        String number = article.getPhone();
        String message = createMessage(article);
        String url = null;
        try {
            url = "https://api.whatsapp.com/send?phone="+number + "&text=" + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void callclick(IndividualArticle article){

        String number = article.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    public String createMessage(IndividualArticle article){
        String result = "J'ai connu votre boutique a travers l'application Vima. Je suis intéressé par l'Article \"" + article.getName() +
                "\" qui est marqué au prix de " + article.getPrice() + " francs CFA. J'aimerais recevoir plus d'information sur cet Article s'il vous plait.";
        return result;
    }
    public void smsclick(IndividualArticle article){
        String number = article.getPhone();
        String message = createMessage(article);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+number));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_indiv_article_page);

        LinearLayout myll = findViewById(R.id.ll);
        myll.setBackgroundResource(R.drawable.border_home);
        Bundle i = getIntent().getExtras();
        IndividualArticle article = new IndividualArticle();
        if(i != null){
            article = Spinning.myData.get(i.getInt("LockerKey"));
        }

        article_id = article.getRank();
        profile_picture = findViewById(R.id.profile_image);
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
        shop_pic = findViewById(R.id.shop_picture);
        ImageButton call, sms, what;
        call = findViewById(R.id.callclick);
        IndividualArticle finalArticle1 = article;
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callclick(finalArticle1);
            }
        });
        sms = findViewById(R.id.smsclick);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsclick(finalArticle1);
            }
        });
        what = findViewById(R.id.whatclick);
        what.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatclick(finalArticle1);
            }
        });

        ImageView profile = findViewById(R.id.profile_image);
        SharedPreferences sharedPreferences = sharedPreferences = getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        String profilePicture = sharedPreferences.getString("profile", "https://www.google.com/search?q=placeholder+profile+pictures+free+to+use&tbm=isch&ved=2ahUKEwjA6ZvV2tDrAhUElBoKHd_bDRIQ2-cCegQIABAA&oq=placeholder+profile+pictures+free+to+use&gs_lcp=CgNpbWcQAzoECAAQHlC7YVixcGCvcWgAcAB4AIAB5QWIAbsVkgEHNC0zLjEuMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=ANVSX8DpHoSoat-3t5AB&bih=792&biw=1536#imgrc=_JeJ3jskVgcZaM");
        Glide.with(getApplicationContext())
                .load(profilePicture)
                .placeholder(R.drawable.categorie_enfant)
                .into(profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
            }
        });

        like = findViewById(R.id.like);
        ImageButton goBack = findViewById(R.id.go_back);
        TextView textLike = findViewById(R.id.textLike);
        IndividualArticle finalArticle = article;
        if(finalArticle.isLiked){
            like.getBackground().setTint(getResources().getColor(R.color.Red));
            textLike.setTextColor(getResources().getColor(R.color.White));
            textLike.setText("Vous aimez cet article!");
        }
        else{
            like.getBackground().setTint(getResources().getColor(R.color.Grey));
            textLike.setTextColor(getResources().getColor(R.color.Black));
            textLike.setText("Liker");
        }
        like.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                finalArticle.isLiked = !finalArticle.isLiked;
                boolean temp = finalArticle.isLiked;

                if (!(temp)) {
                    for(IndividualArticle likedArticle : myData){
                        if (likedArticle.positionInDataBase == finalArticle.positionInDataBase) {
                            likedArticle.isLiked = false;
                        }
                    }
                    like.getBackground().setTint(getResources().getColor(R.color.Grey));
                    textLike.setTextColor(getResources().getColor(R.color.Black));
                    textLike.setText("Liker");
                    if(likedItemsPosition.contains(finalArticle.positionInArray)) {
                        likedItemsPosition.remove(finalArticle.positionInArray);
                    }
                } else if (temp) {
                    like.getBackground().setTint(getResources().getColor(R.color.Red));
                    textLike.setTextColor(getResources().getColor(R.color.White));
                    textLike.setText("Vous aimez cet article!");
                    if(!Recents.myLikedItems.contains(finalArticle)){
                        Recents.myLikedItems.add(finalArticle);
                        finalArticle.positionInArray = likedItemsPosition.size();
                        likedItemsPosition.add(finalArticle.positionInDataBase);
                        if(Recents.myLikedItems != null){
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            Gson gson = new Gson();
                            String jsonText = gson.toJson(Recents.myLikedItems);
                            editor.putString("LikedItems", jsonText);
                            editor.apply();
                        }
                        for(IndividualArticle likedArticle : myData){
                            if (likedArticle.positionInDataBase == finalArticle.positionInDataBase) {
                                likedArticle.isLiked = true;
                            }
                        }
                    }
                }
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });

        big_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        sm_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        sm_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        sm_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Context mContext = getApplicationContext();

        ArticleAdapter.glideIt(big_pic, article.getP_photo(), mContext);
        ArticleAdapter.glideIt(sm_pic1, article.getP_photo(), mContext);
        ArticleAdapter.glideIt(sm_pic2, article.getP_photo(), mContext);
        ArticleAdapter.glideIt(sm_pic3, article.getP_photo(), mContext);

        price.setText(article.getPrice() + " CFA");
        title.setText(article.getName());
        description.setText(article.getName());
        shop_name.setText(article.getShop_name());
        shop_description.setText(article.getShop_name());
        shop_location.setText(article.getShop_name());

        phoneNumber = article.getPhone();

    }
}
