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
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.net.URL;

public class MainPage extends AppCompatActivity {
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

        new DownloadFilesTask().execute();

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.main_page_bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                replaceFragment(new Home());
                                return true;
                            case R.id.favorite:
                                replaceFragment(new Favorite());
                                return true;
                            case R.id.bag:
                                replaceFragment(new Bag());
                                return true;
                        }

                        return false;
                    }
                });
        replaceFragment(new Home());


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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Vima")
                .setMessage("Voulez-vous vraiment quitter Vima?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainPage.this.finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
