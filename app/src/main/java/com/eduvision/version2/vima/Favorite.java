package com.eduvision.version2.vima;

import android.content.DialogInterface;
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
import android.widget.TextView;

import com.eduvision.version2.vima.Tabs.Adapters.RecentsRecyclerAdapter;
import com.eduvision.version2.vima.Tabs.Adapters.RecyclerAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.gson.Gson;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        super.onCreate(savedInstanceState);
        RecyclerView articlegv = view.findViewById(R.id.gridview);
        TextView viewNone = view.findViewById(R.id.textNone);
        if(Recents.myLikedItems.size() == 0){
            viewNone.setVisibility(View.VISIBLE);
            view.findViewById(R.id.clearButton).setVisibility(View.INVISIBLE);
            articlegv.setVisibility(View.INVISIBLE);
        }
        else{
            RecyclerAdapter mAdapter = new RecyclerAdapter(Recents.myLikedItems, getContext());
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
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                            .setNegativeButton("No", null)
                            .show();
                }
            });
        }
        return view;
    }
}
