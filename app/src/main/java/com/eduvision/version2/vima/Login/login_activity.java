package com.eduvision.version2.vima.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.eduvision.version2.vima.MainPage;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.TabAdapter;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.UploadActivity;
import com.google.android.material.tabs.TabLayout;

public class login_activity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        viewPager = findViewById(R.id.sign_up_view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Sinscrire(), "S'inscrire");
        adapter.addFragment(new SeConnecter(), "Se connecter");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        ImageView myLogo = (ImageView) findViewById(R.id.logo);
        final int[] i = {0};
        myLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Fetching.isInternetAvailable(getApplicationContext())){
                    Log.println(Log.INFO, "Handler Tag", "Data is not fetched");
                }
                else  {
                    if(Fetching.isDataFetched.equals("No")|| FetchShops.isShopsDataBeingFetched.equals("No")){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                Log.println(Log.INFO, "Handler Tag", "Data is not fetched");
                                if(Fetching.isDataFetched.equals("No")|| FetchShops.isShopsDataBeingFetched.equals("No")){
                                    Log.println(Log.INFO, "Handler Tag", "Data is still not fetched. Logging out.");
                                }
                                else{
                                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        }, 1000);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), MainPage.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        Button upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(login_activity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        /*

        //   signup = findViewById(R.id.google_sign_in);
       mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



        FacebookSdk.sdkInitialize(getApplicationContext());


        // 0 - for private mode

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebook_login);
        loginButton.setReadPermissions("email", "public_profile", "id", "name");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonclickFb(v);
            }
        });


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                            Intent aftersigningupIntent = new Intent(login_activity.this, MainActivity.class);
                            startActivity(aftersigningupIntent);

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                            Intent afterFacebooksigningupIntent = new Intent(login_activity.this, MainActivity.class);
                            startActivity(afterFacebooksigningupIntent);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            // ;
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void buttonclickFb(View v) {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(login_activity.this, "User Cancelled", Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(login_activity.this, "ERROR", Toast.LENGTH_SHORT).show();
                // ...
            }
        });

    }


    public void UpdateUI(FirebaseUser user) {
        String name = user.getDisplayName();

        editor.putString("nameKey", name);
        editor.apply();
    }

*/
    }

}
