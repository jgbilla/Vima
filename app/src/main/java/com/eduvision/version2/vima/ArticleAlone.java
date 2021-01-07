package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleAlone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_alone);

        Bundle i = getIntent().getExtras();
        IndividualArticle article = new IndividualArticle();
        if(i != null){
            article = Spinning.myData.get(i.getInt("LockerKey"));
        }
        ImageView myView = findViewById(R.id.picture);
        ArticleAdapter.glideIt(myView, article.getP_photo(), getApplicationContext());
        ImageButton goBack = findViewById(R.id.go_back);
        View myWhiteView = findViewById(R.id.whitespace);
        myWhiteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    myWhiteView.setVisibility(View.INVISIBLE);
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}
