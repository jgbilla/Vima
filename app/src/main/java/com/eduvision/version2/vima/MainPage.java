package com.eduvision.version2.vima;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URL;

public class MainPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        new DownloadFilesTask().execute();

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.main_page_bottom_navigation);

        replaceFragment(new Home());

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                replaceFragment(new Home());
                                return true;
                            case R.id.menu:
                                replaceFragment(new Menu());
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
}
