package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Splashscreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.eduvision.version2.vima.Tabs.Recents.likedItemsPosition;

public class Fetching {
    public static int PopularPageNumber = 1;
    public static int RecentsPageNumber = 1;
    public static ArrayList<IndividualArticle> myData = new ArrayList<>(80);
    public static boolean isDataFetched = false;
    public static String isDataBeingFetched = "No";
    public static boolean isHomeDataFetched = false;
    public static ArrayList<IndividualArticle> homeArticlesData = new ArrayList<>(4);

    public static void handleLike(ImageButton myBtn, IndividualArticle myArticle, Context mContext){
        myArticle.isLiked = !myArticle.isLiked;
        boolean temp = myArticle.isLiked;

        if (!(temp)) {
            myBtn.setImageResource(R.drawable.icon_a);
            likedItemsPosition.remove(myArticle.positionInArray);
        } else if (temp) {
            myBtn.setImageResource(R.drawable.icon_b);
            if(!Recents.myLikedItems.contains(myArticle)){
                Recents.myLikedItems.add(myArticle);
                myArticle.positionInArray = likedItemsPosition.size();
                if(Recents.myLikedItems != null){
                    SharedPreferences prefs = mContext.getSharedPreferences("prefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    Gson gson = new Gson();
                    String jsonText = gson.toJson(Recents.myLikedItems);
                    editor.putString("LikedItems", jsonText);
                    editor.apply();
                }
                likedItemsPosition.add(myArticle.positionInDataBase);
            }
        }
    }



    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }
    public static boolean waitInternetAvailable(Context context) {
        boolean[] result = {false};
        if(isInternetAvailable(context)){
            result[0] = true;
        }
        else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    if(isInternetAvailable(context)) {
                        result[0] = true;
                    }
                }
            }, 200);
        }
        return result[0];
    }

    public static void changeText(View convertView, IndividualArticle myArticle, Context mContext){
        TextView myShop = convertView.findViewById(R.id.shop);
        TextView myDescription = convertView.findViewById(R.id.article_name);
        TextView myPrice = convertView.findViewById(R.id.article_price);
        myShop.setText(myArticle.getShop_name());
        myDescription.setText(myArticle.getName());
        myPrice.setText(String.valueOf(myArticle.getPrice()));
        ArticleAdapter.glideIt(convertView.findViewById(R.id.article_picture), myArticle.getP_photo(), mContext);
    }

    public static void makeCustomToast(Context mContext, String mText, int length){
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View layout = mInflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.text);
        text.setText(mText);

        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.setDuration(length);
        toast.setView(layout);
        toast.show();
    }

    public static IndividualArticle getArticle(int positionInDatabase){
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        final IndividualArticle[] currentArticle = new IndividualArticle[1];
        mDatabase.child("Articles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sCounter = snapshot.child("counter").getValue().toString();
                int counter = Integer.parseInt(sCounter);
                if(positionInDatabase < counter){
                    currentArticle[0] = snapshot.child(Integer.toString(positionInDatabase)).getValue(IndividualArticle.class);
                }
                else{
                    currentArticle[0] = snapshot.child(sCounter).getValue(IndividualArticle.class);
                }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return currentArticle[0];
    }

    public static void getItems(){
        isDataBeingFetched = "Yes";
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Articles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myData.clear();
                IndividualArticle currentArticle;
                String sCounter = snapshot.child("counter").getValue().toString();
                int counter = Integer.parseInt(sCounter);
                for(int i = 1; i<=(counter); i++){
                        currentArticle = snapshot.child(Integer.toString(i)).getValue(IndividualArticle.class);
                        Objects.requireNonNull(currentArticle).positionInDataBase = i;

                    if(i <= 5){
                        if (i == 5){
                            isHomeDataFetched = true;
                        }
                        homeArticlesData.add(currentArticle);
                    }
                    myData.add(currentArticle);
            }
                isDataFetched = true;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
