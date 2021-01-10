package com.eduvision.version2.vima;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eduvision.version2.vima.Tabs.Adapters.RecentsRecyclerAdapter;
import com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter;
import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.eduvision.version2.vima.Tabs.Fetching.myData;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favorite#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favorite extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Favorite() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favorite.
     */
    // TODO: Rename and change types and number of parameters
    public static Favorite newInstance(String param1, String param2) {
        Favorite fragment = new Favorite();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView viewNone;
    RecyclerView articlegv;
    RecyclerAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        super.onCreate(savedInstanceState);
        articlegv = view.findViewById(R.id.gridview);
        viewNone = view.findViewById(R.id.textNone);
        if(Recents.myLikedItems.size() == 0){
            viewNone.setVisibility(View.VISIBLE);
            view.findViewById(R.id.clearButton).setVisibility(View.INVISIBLE);
            articlegv.setVisibility(View.INVISIBLE);
        }
        else{
            mAdapter = new RecyclerAdapter(Recents.myLikedItems, getContext());
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            articlegv.setHasFixedSize(true);
            articlegv.setLayoutManager(manager);

            articlegv.setAdapter(mAdapter);



            Button clear = view.findViewById(R.id.clearButton);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Effacer les données")
                            .setMessage("Voulez-vous vraiment effacer les données sur les articles likés? Cette action est irréversible.")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for(IndividualArticle likedArticle : Recents.myLikedItems){
                                        likedArticle.isLiked = false;
                                    }
                                    Recents.myLikedItems.clear();
                                    articlegv.setVisibility(View.GONE);
                                    SharedPreferences prefs = getContext().getSharedPreferences("prefs",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("LikedItems", "");
                                    editor.apply();
                                    viewNone.setVisibility(View.VISIBLE);
                                }
                            })
                            .setNegativeButton("Non", null)
                            .show();
                }
            });
        }
        return view;
    }







    class RecyclerAdapter extends RecyclerView.Adapter<com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter.MyViewHolder> {
        private ArrayList<IndividualArticle> myData;
        private Context mContext;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public View myLayout;
            public ImageView myImage;
            public MyViewHolder(View v) {
                super(v);
                myLayout = v;
                //myImage.findViewById(R.id.article_picture);
            }
            public View getView(){
                return myLayout;
            }
        }

        public RecyclerAdapter(ArrayList<IndividualArticle> mData, Context mContext) {
            this.myData = mData;
            this.mContext = mContext;
        }

        @Override
        public com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.liked_article_model, parent, false);
            com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter.MyViewHolder vh = new com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter.MyViewHolder holder, int position) {
            if(Recents.myLikedItems.size() != 0){
                View convertView =  holder.myLayout;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(mContext, articlePage.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        myIntent.putExtra("LockerKey", Recents.myLikedItems.get(position).positionInArray);
                        mContext.startActivity(myIntent);
                    }
                });
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(mContext)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Effacer cet Article")
                                .setMessage("Voulez-vous effacer ce article de votre historique? Cette action est irréversible.")
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(IndividualArticle Article : Spinning.myData){
                                            if(Recents.myLikedItems.get(position).positionInDataBase == Article.positionInDataBase){
                                                Article.isLiked = false;
                                            }
                                        }
                                        Recents.myLikedItems.get(position).isLiked = false;
                                        Recents.myLikedItems.remove(position);

                                        SharedPreferences prefs = mContext.getSharedPreferences("prefs",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();

                                        if (Recents.myLikedItems.size()==0){
                                            articlegv.setVisibility(View.GONE);
                                            viewNone.setVisibility(View.VISIBLE);
                                            viewNone.setVisibility(View.VISIBLE);
                                            editor.putString("LikedItems", "");
                                            editor.apply();
                                        }
                                        else{

                                            Gson gson = new Gson();
                                            String jsonText = gson.toJson(Recents.myLikedItems);
                                            editor.putString("LikedItems", jsonText);
                                            editor.apply();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                })
                                .setNegativeButton("Non", null)
                                .show();
                        return false;
                    }
                });
                TextView myShop = convertView.findViewById(R.id.shop);
                TextView myDescription = convertView.findViewById(R.id.article_name);
                TextView myPrice = convertView.findViewById(R.id.article_price);
                myShop.setText(Recents.myLikedItems.get(position).getShop_name());
                myDescription.setText(Recents.myLikedItems.get(position).getName());
                myPrice.setText(String.valueOf(Recents.myLikedItems.get(position).getPrice()));
                ArticleAdapter.glideIt(convertView.findViewById(R.id.article_picture), Recents.myLikedItems.get(position).getP_photo(), mContext);
            }
        }
        @Override
        public int getItemCount() {
            return myData.size();
        }
    }

}
