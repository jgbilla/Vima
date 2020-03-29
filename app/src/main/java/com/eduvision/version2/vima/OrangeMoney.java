package com.eduvision.version2.vima;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.ArrayList;
public class OrangeMoney extends Activity
{
	String[] articles = new String[100];
	TextView text; Button button; String montant; Bundle i;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orange);
		i = getIntent().getExtras();
		if(i!=null){montant = i.getString(("message"));
		articles = i.getStringArray("Array");
		}
		text = (TextView) findViewById(R.id.text);
		button = (Button) findViewById(R.id.next);
		text.setText("Le montant total s'élève à " + montant +"F + la livraison de 1200F");
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(OrangeMoney.this, Verify.class);
				intent.putExtra("message", montant);
				intent.putExtra("Array", articles);
				startActivity(intent);
			}
		});
		
	}
}
