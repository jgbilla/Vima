package com.eduvision.version2.vima;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottom_navigation);

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
        transaction.replace(R.id.activity_box, Fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

}
