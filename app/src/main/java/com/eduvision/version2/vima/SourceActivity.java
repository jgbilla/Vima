package com.eduvision.version2.vima;
import android.os.*;
import android.app.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;

public class SourceActivity extends Activity
{ 
	boolean clicked = false;
    public void mon(){
		clicked = true;
	}
	String[] articles = new String [100];
	public void arr(){
		for(int i =0; i<100; i++){
			articles[i] = " ";
		}
	}
	
	String[] items;
	int totalValue;
	Button total, next; Bundle i; 
	TextView textFruits, textLegumes, textAssaisonnements, text;
	@Override
	public void onCreate(Bundle savedInstanceState){
		arr();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sourcemain);
	    i = getIntent().getExtras();
	    next = (Button) findViewById(R.id.next);
		if(i!=null){totalValue += Integer.parseInt(i.getString("message"));
		articles = i.getStringArray("Array");}
		textFruits=(TextView) findViewById(R.id.textFruits);
		textLegumes =(TextView)findViewById(R.id.textLegumes);
		textAssaisonnements=(TextView)findViewById(R.id.textAssaisonnements);
		
		//****    ****    **** 
		textFruits.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				items = new String[]{
					"Oranges", "Pommes", "Raisins", "Bananes", "Mandarines", "Prunes",
					"Ananas", "Fraises", "Melons", "Goyaves"
				};
				
				Intent intent = new Intent(SourceActivity.this, ListActivity.class);
				intent.putExtra("message", items);
				intent.putExtra("Array",articles);
				intent.putExtra("total", totalValue);
				startActivity(intent);
			}
		});
		//****    ****    ****
		textLegumes.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					items = new String[]{
						"Tomates mures", "Tomates vertes", "Oignons", "Toega", "Feuille d'oignons", "Poivrons",
						"Patte d'arachide", "Patte de tomate", "Courgettes", "Aubergines"
					};
					Intent intent = new Intent(SourceActivity.this, ListActivity.class);
					intent.putExtra("message", items);
					intent.putExtra("Array", articles);
					intent.putExtra("total", totalValue);
					startActivity(intent);
				}
			});
		//****    ****    ****
		textAssaisonnements.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					items = new String[]{
					"Sel en grumaux", "Sel en poudre", "Sucre en poudre", "Sucre en carreaux", "Maggi", "Lait concentré",
					"Piment en poudre", "Café", "Thé", "Poisson sec"
					};
					Intent intent = new Intent(SourceActivity.this, ListActivity.class);
					intent.putExtra("message", items);
					intent.putExtra("Array", articles);
					intent.putExtra("total", totalValue);
					startActivity(intent);
				}
			});
		//Invisible textview to show the total amount	
		text=(TextView) findViewById(R.id.text);
			
		//Button "Voir montant total"
		total = (Button) findViewById(R.id.total);
		total.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mon();
				text.setText("Montant total: "+totalValue);
					
			}
		});
			
        //Button "suivant" to get to the next Activity OrangeMoney
		next.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					if(clicked==true&&totalValue!=0){
					    Intent intent = new Intent(SourceActivity.this, ListActivity.class);
					    intent.putExtra("message", totalValue+"");
						intent.putExtra("Array",articles);
					    startActivity(intent);
					}
				}
			});
	}
}
