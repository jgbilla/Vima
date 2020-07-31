package com.eduvision.version2.vima;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchHandling extends AppCompatActivity {

    RecyclerView mResultList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_handling);
        handleIntent(getIntent());

    }
       private void firebaseSearch (String text) {

           Toast.makeText(SearchHandling.this, "Started Search", Toast.LENGTH_LONG).show();

           Query firebaseSearchQuery = mDatabase.orderByChild("name").startAt(text).endAt(text + "\uf8ff");
           FirebaseRecyclerAdapter<Article, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Article, ViewHolder>(

                   Article.class,
                   R.layout.search_result_item,
                   ViewHolder.class,
                   firebaseSearchQuery
           ) {


               @Override
               protected void populateViewHolder(ViewHolder viewHolder, Article article, int i) {

                   viewHolder.setDetails(getApplicationContext(), article.getName(), article.getDescription());


               }
           };

        }


        // View Holder Class

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("MyPersonalQuery", query);

            mDatabase = FirebaseDatabase.getInstance().getReference("Articles");
            mResultList = (RecyclerView) findViewById(R.id.lv_results);
            mResultList.setHasFixedSize(true);
            mResultList.setLayoutManager(new LinearLayoutManager(this));

            firebaseSearch(query);
            //use the query to search your data somehow
        }
    }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            View mView;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;

            }

            public void setDetails(Context ctx, String name, String description){

                TextView Name = (TextView) mView.findViewById(R.id.name_text);
                ImageView Image = (ImageView) mView.findViewById(R.id.image);


                Name.setText(name);


               // Glide.with(ctx).load(image).into(Image);


            }




        }




    }

