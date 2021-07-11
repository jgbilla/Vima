package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Spinning extends AppCompatActivity {
    View progress;
    Button tryAgain;
    static TextView progressText;
    static Context myContext;

    public void getLikedItems(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        Gson gson = new Gson();
        String LikedItems = prefs.getString("LikedItems", null);
        Type type = new TypeToken<ArrayList<IndividualArticle>>() {}.getType();
        if (LikedItems != null && !LikedItems.equals("")) {
            Recents.myLikedItems = gson.fromJson(LikedItems, type);
        }
    }

    public void updateNumbUsersConnectedToday() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String lastDate = prefs.getString("date", date);
        int numbConnectedToday = prefs.getInt("numbConnectedToday", 1);

        if (lastDate.equals(date)) {
            numbConnectedToday++;
            editor.putBoolean("connectedToday", true);
            editor.putInt("numbConnectedToday", numbConnectedToday);
            editor.apply();
        }
        else {
            mDatabase.child("Stats").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    editor.putBoolean("connectedToday", true);
                    editor.putInt("numbConnectedToday", 1);
                    editor.apply();
                    String counter = String.valueOf(snapshot.child("User Connections on " + lastDate).getValue());
                    mDatabase.child("Stats").child("User Connections on " + lastDate).setValue(
                            Integer.parseInt(counter) + 1
                    );

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateNumbUsersConnectedToday();

        getLikedItems();
        setContentView(R.layout.spinning_activity);
        myContext = getApplicationContext();
        progress = findViewById(R.id.progress);
        tryAgain = findViewById(R.id.retry);
        progressText = findViewById(R.id.progressText);
        getArticles();
    }


    public void setProgressInt(int progress) {
        progressText.setText("Progrès: " + progress + "%");
    }



















    public static ArrayList<ShopModel>
            shopData = new ArrayList<>(80);
    public static boolean isShopsDataFetched = false;
    public static ArrayList<IndividualShop> homeShopsData = new ArrayList<>(4);

    public static ArrayList<IndividualArticle> myData = new ArrayList<>(80);
    public static boolean isDataFetched = false;
    public static boolean isHomeDataFetched = false;
    public static ArrayList<IndividualArticle> homeArticlesData = new ArrayList<>(4);
    public ArrayList<Integer> TotalArticles = new ArrayList<>(1); // Total raw list of articles

    public void cleaningUpDate(DataSnapshot shops, DataSnapshot items){
        shopData.clear();
        ShopModel myCurrentShop = new ShopModel();
        int stop = (int) shops.getChildrenCount()-1;

        if(stop < 80 ){
            stop = 80;
        }
        int realIndex = 1;

        for(int index = 1; index<=stop; index++) {
            realIndex++;
            if(index >= shops.getChildrenCount()-1 & realIndex < 80){
                index = (int) shops.getChildrenCount()-1;
            }
            else if (shops.getChildrenCount()-1<80 & realIndex>=80){
                break;
            }
            Log.println(Log.DEBUG, "Index", String.valueOf(index));

            DataSnapshot thisShop = shops.child(String.valueOf(index));
            myCurrentShop.setName(String.valueOf(thisShop.child("name").getValue()));
            myCurrentShop.call = String.valueOf(thisShop.child("callNumber").getValue());
            myCurrentShop.whatsapp = String.valueOf(thisShop.child("whatsappNumber").getValue());
            myCurrentShop.messages = String.valueOf(thisShop.child("messagesNumber").getValue());
            myCurrentShop.description = String.valueOf(thisShop.child("description").getValue());
            myCurrentShop.typeDeCompte = String.valueOf(thisShop.child("typeDeCompte").getValue());
            myCurrentShop.location = String.valueOf(thisShop.child("location").getValue());
            myCurrentShop.setP_photo(String.valueOf(thisShop.child("p_photo").getValue()));
            Log.println(Log.DEBUG, "BegContrat", String.valueOf(thisShop.child("begContrat").getValue()));

            myCurrentShop.beginningContrat = Integer.parseInt(String.valueOf(thisShop.child("begContrat").getValue()));
            myCurrentShop.endContrat = Integer.parseInt(String.valueOf(thisShop.child("endContrat").getValue()));
            myCurrentShop.partnershipCount = Integer.parseInt(String.valueOf(thisShop.child("partnershipCount").getValue()));
            myCurrentShop.numbOfShop = Integer.parseInt(String.valueOf(index));

            DataSnapshot articlesSnapshot = thisShop.child("Articles");
            ArrayList<String> titlesNames = new ArrayList<>(3);
            ArrayList<ArrayList<Integer>> Articles = new ArrayList<>(1); //Gives separate arrays for each title
            for (DataSnapshot snapshotTemp : articlesSnapshot.getChildren()) {
                titlesNames.add(snapshotTemp.getKey());

                ArrayList<Integer> thisTitlesArticles = new ArrayList();
                for (int a = 1; a <= snapshotTemp.getChildrenCount(); a++) {
                    Log.println(Log.DEBUG, snapshotTemp.getKey(), String.valueOf(snapshotTemp.child(String.valueOf(a)).getValue()));

                    thisTitlesArticles.add(Integer.parseInt(String.valueOf(snapshotTemp.child(String.valueOf(a)).getValue())));

                    TotalArticles.add(Integer.parseInt(String.valueOf(snapshotTemp.child(String.valueOf(a)).getValue())));
                }
                Articles.add(thisTitlesArticles);
            }
            ShopModel.Articles hello = new ShopModel.Articles();
            hello.titlesNames = titlesNames;
            hello.articlesList = Articles;
            myCurrentShop.myArticles = hello;

            shopData.add(myCurrentShop);
        }

        setProgressInt(70);

        myData.clear();
        String deliveryNumber = String.valueOf(items.child("deliveryNumber").getValue());
        Log.println(Log.DEBUG, "NUMBER", deliveryNumber);

        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("deliveryNumber", deliveryNumber);
        editor.apply();
        IndividualArticle currentArticleN;
        String sCounterN = String.valueOf(items.getChildrenCount()-1);
        int counterN = Integer.parseInt(sCounterN);
        int counterS = 1;
            counterS = counterN;

        for(int i = 1; i<(counterS); i++){
                currentArticleN = items.child(Integer.toString(i)).getValue(IndividualArticle.class);

            Objects.requireNonNull(currentArticleN).positionInDataBase = i;

            if(i < 5){
                if (i == 5){
                    isHomeDataFetched = true;
                }
                homeArticlesData.add(currentArticleN);
            }
            myData.add(currentArticleN);
        }
        isDataFetched = true;

    }

    public ArrayList<IndividualArticleTemplate> myArticles; //Raw list of ArticlesModels
    public void getArticles(){
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setProgressInt(40);
                    mDatabase.child("Articles").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot Nsnapshot) {

                            cleaningUpDate(snapshot, Nsnapshot);
                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            setProgressInt(90);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }























}
class IndividualArticleTemplate {
    public int positionInArray = -1;
    private String name;
    private String p_photo;
    public int colour = 0;
    /*
    0 = white mono
    1 = black mono
    2 = every color
     */
    public int size = 3;
    private String shop_name;
    public int shopPositionInDatabase = 1;
    public boolean getNumberTimesLiked() {
        return isLiked;
    }

    public void setNumberTimesLiked(boolean numberTimesLiked) {
        this.isLiked = numberTimesLiked;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public void setPhone(String phone) {
        this.phoneNumber = phone;
    }

    private String phoneNumber = "+22676603608";
    public int positionInDataBase = 0;
    public boolean isLiked = false;
    private Long rank, seller_id, popularity_index, price;

    public IndividualArticleTemplate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getP_photo() {
        return p_photo;
    }

    public void setP_photo(String p_photo) {
        this.p_photo = p_photo;
    }

    public void setSeller_id(Long seller_id) {
        this.seller_id = seller_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public Long getPopularity_index() {
        return popularity_index;
    }

    public void setPopularity_index(Long popularity_index) {
        this.popularity_index = popularity_index;
    }

}
