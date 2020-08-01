package com.eduvision.version2.vima;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class Recents extends Fragment {
    int lastId;
    private String title;
    private int page;
    private static boolean transfer = false;
    GridView articlegv;
    FirebaseDatabase rootref;
    MaterialSearchView materialSearchView;
    String[] list;
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    public static ArrayList<individual_info_class> myList;

    public static ArrayList<individual_info_class> getMyList() {
        return myList;
    }

    protected static void getItems(final int n){
        //This static method will fetch data chronologically from Firebase database and update the static ArrayList myList
        //Remember that we're also using it to fetch the data for the Popular class
        if(myList!=null){
            //If myList ArrayList is not empty, it will be cleared
            myList.clear();
        }
        final int[] lastId = new int[1];
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
         mDatabase.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastId[0] = (int) dataSnapshot.getChildrenCount();
                String name;
                String price;
                String photo;
                final String[] shop = new String[1];
                int shop_id;
                for(int i=0; i<(n-1); i++){
                    final individual_info_class infos = new individual_info_class();
                    name = dataSnapshot.child(Integer.toString(lastId[0] -i)).child("infos").child("name").getValue().toString();
                    infos.setName(name);
                    price = dataSnapshot.child(Integer.toString(lastId[0] -i)).child("infos").child("price").getValue().toString();
                    infos.setPrice(price);
                    photo = dataSnapshot.child(Integer.toString(lastId[0] -i)).child("pictures").child("p_photo").getValue().toString();
                    infos.setP_photo(photo);
                    shop_id = (int) dataSnapshot.child(Integer.toString(lastId[0] -i)).child("infos").child("seller_id").getValue();
                    infos.setSeller_id(shop_id);
                    final int finalShop_id = shop_id;
                    mDatabase.child("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            shop[0] = dataSnapshot2.child(Integer.toString(finalShop_id)).child("infos").child("name").toString();
                            infos.setShop_name(shop[0]);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    infos.setRank(lastId[0] -i);
                    myList.add(infos);
                }
                transfer = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static Recents newInstance(int page, String title) {
        //Create newInstance: See Home class
        Recents recents = new Recents();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        recents.setArguments(args);
        return recents;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.recents_tab1, container, false);

        /*myList = new ArrayList<>(0);
        super.onCreate(savedInstanceState);
        articlegv = view.findViewById(R.id.gridview);
        if(myList == null){
            //Fetch data again only if myList is already empty
            getItems(100);
        }
        synchronized (myList) {
          try {
              //Just waiting 5 seconds that the loading of the getItems function is done
              myList.wait(5000);
              Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
          } catch (InterruptedException e){
                e.printStackTrace();
            }
            }

        articlegv.setAdapter(new ArticleAdapter(getActivity(), myList, 1));
        articlegv.setOnItemClickListener((parent, v, position, id) -> {
            //Get to specific chosen Article page,It is not complete yet tho
            Intent myIntent = new Intent(getActivity(), articlePage.class);
            myIntent.putExtra("id", (Parcelable) myList.get(position));
            startActivity(myIntent);
        });
*/

        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootref = FirebaseDatabase.getInstance();


        DatabaseReference articleRef = rootref.getReference("Articles");
       /* list = new String[]{"Clipcodes", "Android Tutorials", "Youtube Clipcodes Tutorials", "SearchView Clicodes", "Android Clipcodes", "Tutorials Clipcodes"};

       materialSearchView = (MaterialSearchView)getView().findViewById(R.id.mysearch);
        materialSearchView.clearFocus();
        materialSearchView.setSuggestions(list);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Here Create your filtering
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //You can make change realtime if you typing here
                //See my tutorials for filtering with ListView
                return false;


            }

        });
        */


        //Follow this video for fix and other happend, Comment and Like this video . THANKS
    }

    }





class individual_info_class{
    //Custom class used to get the specific info needed for displaying Recents and Popular classes
    private String name, price, p_photo, shop_name;
    private int rank, seller_id, popularity_index;

    public individual_info_class() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getP_photo() {
        return p_photo;
    }

    public void setP_photo(String p_photo) {
        this.p_photo = p_photo;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPopularity_index() {
        return popularity_index;
    }

    public void setPopularity_index(int popularity_index) {
        this.popularity_index = popularity_index;
    }






}
