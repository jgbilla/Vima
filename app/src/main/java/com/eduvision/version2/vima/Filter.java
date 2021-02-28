package com.eduvision.version2.vima;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;
import com.eduvision.version2.vima.Tabs.Verify;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Filter extends AppCompatActivity {
    View filter;
    Button valider;
    TextView textNone;
    Spinner spinner1, spinner2, spinner3;
    public static ArrayList<IndividualArticle> myNewData;
    final int[] chosen1 = new int[1];
    final int[] chosen2 = new int[1];
    final int[] chosen3 = new int[1];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        //Declaration of views
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        valider = findViewById(R.id.valider);

        //ArrayAdapters for Lists for spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.size_array, R.layout.custom_item_spinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.prize_array, R.layout.custom_item_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.color_array, R.layout.custom_item_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        //onItemSelectedListeners
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosen1[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen1[0] = 0;

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosen2[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen2[0] = 0;

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosen3[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen3[0] = 0;

            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myNewData = Spinning.myData;
                for(int i = 0; i<5; i++){
                    if(chosen1[0] == i){
                        myNewData = sortDataSize(i, Spinning.myData);
                    }
                }
                for(int i = 0; i<3; i++){
                    if(chosen2[0] == i){
                        myNewData = sortDataPrice(i, myNewData);
                    }
                }
                for(int i = 0; i<2; i++){
                    if(chosen3[0] == i){
                        myNewData = sortDataColor(i, myNewData);
                    }
                }
                Intent myIntent = new Intent(getApplicationContext(), Verify.class);
                myIntent.putExtra("key", "3");
                if(myNewData.size()==0){
                    textNone.setVisibility(View.VISIBLE);
                    myIntent.putExtra("resultCode", 0);
                    Log.println(Log.DEBUG, "Tag", "fuck 1");

                    startActivity(myIntent);
                }
                else{
                    myIntent.putExtra("resultCode", 1);
                    Log.println(Log.DEBUG, "Tag", "fuck 2");
                }
                startActivity(myIntent);
                finish();
            }
        });


    }

    public ArrayList<IndividualArticle> sortDataPrice(int i, ArrayList<IndividualArticle> oldData){
        ArrayList<IndividualArticle> result = oldData;
        ArrayList<IndividualArticle> mockResultTemp = result;
        if(oldData.size() != 0) {
            switch (i) {
                case 1:
                    result = oldData;
                case 2:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.getPrice() > 5000) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
                case 3:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.getPrice() > 10000 || article.getPrice() < 5000) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
                case 4:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.getPrice() > 15000 || article.getPrice() < 10000) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
                case 5:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.getPrice() > 20000 || article.getPrice() < 15000) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
                case 6:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.getPrice() < 20000) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
            }
        }
        return result;
    }

    public ArrayList<IndividualArticle> sortDataColor(int i, ArrayList<IndividualArticle> oldData){
        ArrayList<IndividualArticle> result = oldData;
        ArrayList<IndividualArticle> mockResultTemp = result;
        if(oldData.size() != 0) {
            switch (i) {
                case 1:
                    result = oldData;
                case 2:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.colour != 0) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
                case 3:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.colour != 1) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
                case 4:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.colour != 2) {
                            mockResultTemp.remove(article);
                        }
                    }
                    result = mockResultTemp;
            }
        }
        return result;
    }


    public ArrayList<IndividualArticle> sortDataSize(int i, ArrayList<IndividualArticle> oldData){
        ArrayList<IndividualArticle> result = oldData;
        if(oldData.size()!=0) {
            switch (i) {
                case 1:
                    result = oldData;
                case 2:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.size != 0) {
                            result.remove(article);
                        }
                    }
                case 3:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.size != 1) {
                            result.remove(article);
                        }
                    }
                case 4:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.size != 2) {
                            result.remove(article);
                        }
                    }
                case 5:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.size != 3) {
                            result.remove(article);
                        }
                    }
                case 6:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.size != 4) {
                            result.remove(article);
                        }
                    }
                case 7:
                    for (int a = 0; i<result.size(); i++) {
                        IndividualArticle article = result.get(i);
                        if (article.size != 5) {
                            result.remove(article);
                        }
                    }
            }
            Log.println(Log.DEBUG, "Tag", String.valueOf(result.size()));
        }
        return result;
    }
}
