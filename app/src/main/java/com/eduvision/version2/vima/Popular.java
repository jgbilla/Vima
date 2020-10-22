package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Popular extends Fragment {
    GridView grid;
    Button previous, suivant;
    TextView counter;

    public static Popular newInstance(int page, String title) {
        Popular populars = new Popular();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        populars.setArguments(args);
        return populars;
    }

//TODO: Add footer to list
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.popular_tab2, container, false);
        super.onCreate(savedInstanceState);
        grid = view.findViewById(R.id.grid);
        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(), 1, "Popular");
        grid.setAdapter(articleAdapter);
        View footerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_for_recents, null, false);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(getContext(), articlePage.class);
                startActivity(myIntent);
            }
        });
        previous = view.findViewById(R.id.previous);
        suivant = view.findViewById(R.id.suivant);
        counter = view.findViewById(R.id.counter);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Fetching.PopularPageNumber != 1){
                    Fetching.PopularPageNumber--;
                    counter.setText(Integer.toString(Fetching.PopularPageNumber));
                    articleAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Action Impossible", Toast.LENGTH_SHORT);
                }
            }
        });
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fetching.PopularPageNumber = Fetching.PopularPageNumber + 1;
                counter.setText(Integer.toString(Fetching.PopularPageNumber));
                articleAdapter.notifyDataSetChanged();
            }
        });
        /*
        super.onCreate(savedInstanceState);
        GridView articlegv = view.findViewById(R.id.gridview_popular);
        Recents.getItems(100);
        ArrayList<individual_info_class> pop_list = Recents.getMyList();

        //We are ranking the articles by order of popularity
        pop_list.sort(Comparator.comparingInt(individual_info_class::getPopularity_index).reversed());

        articlegv.setAdapter(new ArticleAdapter(getActivity(), pop_list, 2));
        articlegv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get to chosen Article page
                Intent myIntent = new Intent(getActivity(), articlePage.class);
                myIntent.putExtra("id", (Parcelable) pop_list.get(position));
                startActivity(myIntent);
            }
        });
        */
        return view;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
