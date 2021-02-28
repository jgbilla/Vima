package com.eduvision.version2.vima;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

public class MainPage extends AppCompatActivity {
    int currentFragment;
    public void putLikedItems(){
        if(Recents.myLikedItems != null){
            SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String jsonText = gson.toJson(Recents.myLikedItems);
            editor.putString("LikedItems", jsonText);
            editor.apply();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        putLikedItems();


        currentFragment = 0;
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.main_page_bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                replaceFragment(new Home());
                                currentFragment = 0;
                                return true;
                            case R.id.favorite:
                                replaceFragment(new Favorite());
                                currentFragment = 1;
                                return true;
                            case R.id.bag:
                                replaceFragment(new Bag());
                                currentFragment = 2;
                                return true;
                        }

                        return false;
                    }
                });
        replaceFragment(new Home());
        currentFragment = 0;


    }

    public void replaceFragment(Fragment Fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_page, Fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

        ProgressDialog progressDialog;

        @Override
        protected Long doInBackground(URL... urls) {
            Fetching.getItems();
            FetchShops.getShops();
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

        }
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainPage.this,
                    "ProgressDialog",
                    "Wait for the items to load");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    /*
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        if (!hasFocus){
            String jsonText = gson.toJson(Spinning.myData);
            String jsonTextShops = gson.toJson(Spinning.shopData);
            editor.putString("Articles", jsonText);
            editor.putString("Shops", jsonTextShops);
            editor.apply();
        }
        else{
            String myData = prefs.getString("Articles", null);
            Type type = new TypeToken<ArrayList<IndividualArticle>>() {}.getType();
            Spinning.myData = gson.fromJson(myData, type);

            String shopData = prefs.getString("Articles", null);
            type = new TypeToken<ArrayList<IndividualShop>>() {}.getType();
            Spinning.shopData = gson.fromJson(shopData, type);
        }
    }

     */
    @Override
    public void onBackPressed() {
        if(currentFragment == 0){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Vima")
                    .setMessage("Voulez-vous vraiment quitter Vima?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainPage.this.finish();
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("Non", null)
                    .show();
        }
        else{
            BottomNavigationView bNView;
            bNView = findViewById(R.id.main_page_bottom_navigation);
            bNView.setSelectedItemId(R.id.home);
        }
    }

}
