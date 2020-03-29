package com.eduvision.version2.vima;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.ArrayList;
public class Verify extends Activity
{
	//What you need is the global boolean result to 
	//incorporate the articles to firebase
	protected int decodeIt(String interest){
		int result = 0;
		if(interest!=null){
		String mResult = "";
		for(int i = 4; i<interest.length(); i++){
			mResult += interest.charAt(i);
		}
		result = Integer.parseInt(mResult);
		result -= 500;
		result = result/2;
		return result;}
		else{return result;}
	}
	ArrayList<Article> articles = new ArrayList<>();
	int numb, crypt;
	EditText numero, code;
	boolean result;
	Button next;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle i = getIntent().getExtras();
		if(i!=null){crypt=Integer.parseInt( i.getString(("message")) +1200);}
		setContentView(R.layout.verify);
		numero = (EditText) findViewById(R.id.numero);
		code = (EditText) findViewById(R.id.code);
		next = (Button) findViewById(R.id.suivant);
		next.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(numero.getText().toString().length()==8&&code.getText().toString().length()!=0){
				    numb = Integer.parseInt(numero.getText().toString());
					result = (decodeIt(code.getText().toString())-crypt <= 25
						|| crypt-decodeIt(code.getText().toString()) <= 25);
				}
			}
		});
		
	}
}
