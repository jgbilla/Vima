package com.eduvision.version2.vima;
import android.view.*;
import android.os.*;
import android.app.*;
import android.content.*;
import android.text.InputType;
import java.util.*;
import android.widget.*;

class Article{
	private String mName;
	private int mPrice;
	public Article(String name, int price){
		this.mName = name;
		this.mPrice = price;
	}
	public String getName(){return mName;};
	public int getPrice(){return mPrice;};
	public void setName(String name){mName = name;};
	public void setPrice(int price){mPrice = price;};
}
public class MainActivity extends Activity
{
	//Global varables
	int total, n;
	//final Article[] articles = new Article[10];
	
	//GetBlank for next function
	public String getBlank(String interest){
		int numb = 40 - interest.length();
		String result = "";
		for(int i = 0; i<numb; i++){
			result += "-";
		}
		return result;
	}
	String[] articles = new String[100];
	//GetItDone to get the thing that made you almost die with work
	public void getItDone(final ArrayList<String> arrayList, final ArrayAdapter arrayAdapter, final int position, final String givenString){
		final EditText edit = new EditText(context);
		edit.setInputType(InputType.TYPE_CLASS_NUMBER);
		AlertDialog.Builder  price = new AlertDialog.Builder(context);
		price.setTitle("Entrez le montant de la marchandise:");
		price.setView(edit);
		price.setNegativeButton("Retour", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		price.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					arrayList.set(position, arrayList.get(position)+getBlank(arrayList.get(position))+"Prix: " + edit.getText());
					arrayAdapter.notifyDataSetChanged();
					int number = Integer.parseInt(edit.getText().toString());
					articles[n] = givenString + "&" + number;
					total+=number;
					n++;
				}
			});
		AlertDialog priceDiaolog = price.create();
		priceDiaolog.show();
	}

    //Random global variables
	Context context = this;
    String[] items; int totalValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bundle i = getIntent().getExtras();
		if(i!=null){items= i.getStringArray(("message"));
		totalValue = i.getInt("total");
		articles=i.getStringArray("Array");}
		final ListView list = findViewById(R.id.list);
		final ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add("Article 1");
		arrayList.add("Article 2");
		arrayList.add("Article 3");
		arrayList.add("Article 4");
		arrayList.add("Article 5");
		arrayList.add("Article 6");
		arrayList.add("Article 7");
		arrayList.add("Article 8");
		arrayList.add("Article 9");
		arrayList.add("Article 10");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,                   
																		   android.R.layout.simple_list_item_1, arrayList);
		list.setAdapter(arrayAdapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
					String clickedItem=(String) list.getItemAtPosition(position);
					

					AlertDialog.Builder builder = new AlertDialog.Builder(context);

					//set the title for alert dialog
					builder.setTitle("Choisissez l'article: ");

					//set items to alert dialog. i.e. our array , which will be shown as list view in alert dialog
					builder.setItems(items, new DialogInterface.OnClickListener() {
							@Override public void onClick(DialogInterface dialog, int item) {
								//TODO add a shit here
								arrayList.set(position, items[item]);
								arrayAdapter.notifyDataSetChanged();
								list.getChildAt(position).setBackgroundColor(0xffaaaaaa);
								dialog.dismiss();
								getItDone(arrayList, arrayAdapter, position, items[item]);
							}
						});
					//Creating alert dialog
				    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
					//Showing alert dialog
					AlertDialog alert = builder.create();
                    alert.show();

				}


			});
			//Button "Enregistrer les changements"
			Button button = (Button) findViewById(R.id.button);
			button.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					Intent intent = new Intent(MainActivity.this, SourceActivity.class);
					intent.putExtra("message", (total+totalValue)+"");
					intent.putExtra("Array", articles);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), 
					               "Enregistré",
								   Toast.LENGTH_SHORT).show();
				}
			});

	}
}

