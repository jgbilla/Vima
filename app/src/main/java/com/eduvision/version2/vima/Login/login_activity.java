package com.eduvision.version2.vima.Login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.eduvision.version2.vima.Login.SeConnecter;
import com.eduvision.version2.vima.Login.Sinscrire;
import com.eduvision.version2.vima.MainPage;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.TabAdapter;
import com.eduvision.version2.vima.Tabs.DownloadFilesTask;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class login_activity extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static final int REQUEST_LOCATION = 1;
    SharedPreferences sharedPreferences;
    View phoneSignLayoutSecond;
    View phoneSignLayoutFirst;

    SharedPreferences.Editor editor;
    String id;
    Button seConnecter;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    LocationManager locationManager;
    String latitude, longitude;
    FusedLocationProviderClient fusedLocationClient;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    GoogleSignInClient mGoogleSignInClient;
    private final String PREFERENCE_FILE_KEY = "myAppPreference";
    private static final String KEY_USERNAME = "prefUserNameKey";
    Context context = this;
    View fragment;
    private TabAdapter adapter;


    TextView back, loginText, phoneSignIn, emailSignIn;
    Button connectWithPhone;
    AppCompatEditText numberTail, number, codeEditText, username;
    LinearLayout choose, actualSignIn;
    CallbackManager mCallbackManager;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    FrameLayout frameLayout;
    FragmentManager fm;
    public static Context mContext;


    public static boolean tryFetching(){
        final boolean[] result = {false};
        for (int i = 0; i<=100; i++){
            if (DownloadFilesTask.downloadProgress != 100){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(DownloadFilesTask.downloadProgress != 100){
                            result[0] = false;
                        }
                        else {
                            result[0] = true;
                        }
                    }
                },100000);
            }
            else{
                result[0] = true;
                break;
            }
        }
        return result[0];

    }
    private void hideKeyboard(View view){
        //Hide the soft input keyboard (I mean, what else?) <3
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        //When Credentials are verified and you actually want to sign in and go on to next Activity
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().getUser();
                            assert user != null;
                            id = user.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Users").child("Acheteurs").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        String Userid = snapshot.child("id").getValue(String.class);

                                        if (id.equals(Userid)) {
                                            String username = snapshot.child("username").getValue(String.class);
                                            editor.putString("username", username);
                                            editor.apply();

                                            String Picture = snapshot.child("ImageURL").getValue(String.class);
                                            editor.putString("profile", Picture);
                                            editor.apply();

                                            String Phone = snapshot.child("telephone").getValue(String.class);
                                            editor.putString("telephone", Phone);
                                            editor.apply();

                                            String Email = snapshot.child("email").getValue(String.class);
                                            editor.putString("email", Email);
                                            editor.apply();

                                            String Adress = snapshot.child("Adresse").getValue(String.class);

                                            if(Adress != null){
                                                editor.putString("location", Adress);
                                                editor.apply();

                                            } else{
                                                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                                    OnGPS();
                                                } else {
                                                    getLocation();
                                                }

                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Intent intent = new Intent(getApplicationContext(), Spinning.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("La Localisation nous permettra de vous offrir les meilleurs services. Autoriser la localisation?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void getLocation(){

        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {

                                double lat = location.getLatitude();
                                double longi = location.getLongitude();

                                latitude = String.valueOf(lat);
                                longitude = String.valueOf(longi);

                                Geocoder geocoder;
                                List<Address> addresses = new ArrayList<>();
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                Log.d(TAG, "We made it:" + address);
                                user = mAuth.getCurrentUser();
                                mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Acheteurs").child(user.getUid());
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Adresse", address);
                                mDatabase.updateChildren(map);
                                editor.putString("location", address);
                                editor.apply();
                            }
                        }
                    });
        }
    }
    public void afterSpinning(){
        progress.setVisibility(View.VISIBLE);
        if (!tryFetching()) {
            progress.setVisibility(View.GONE);
            tryAgain.setVisibility(View.VISIBLE);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    afterRetryButtonClick();
                }
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
            finish();
        }
    }
    public void afterRetryButtonClick(){
        afterSpinning();
        tryAgain.setVisibility(View.GONE);
    }
    void toggleView(int choice){
        back.setVisibility(View.VISIBLE);
        choose.setVisibility(View.GONE);
        loginText.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleViewBack();
            }
        });
        if(choice == 0){
            phoneSignLayoutFirst = findViewById(R.id.phone_sign_in_layout);
            phoneSignLayoutFirst.setVisibility(View.VISIBLE);
            final RadioGroup[] radioSexGroup = {findViewById(R.id.radio_gender)};
            username =findViewById(R.id.username);

            connectWithPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(username.getText() != null && number.getText()!=null && numberTail.getText()!=null) {
                        if (TextUtils.isEmpty(username.getText().toString())) {
                            Fetching.makeCustomToast(getApplicationContext(), "Entrez votre e-mail", Toast.LENGTH_LONG);
                            return;
                        }

                        if (TextUtils.isEmpty(numberTail.getText().toString()) || TextUtils.isEmpty(number.getText().toString())) {
                            Fetching.makeCustomToast(getApplicationContext(), "Entrez votre numero de telephone", Toast.LENGTH_LONG);
                            return;
                        }
                        else {
                            int selectedId = radioSexGroup[0].getCheckedRadioButtonId();

                            RadioButton radioSexButton = findViewById(selectedId);
                            String selection = radioSexButton.getText().toString();
                            editor.putString("gender", selection);
                            editor.apply();

                            String usernameText = username.getText().toString();
                            editor.putString("username", usernameText);
                            editor.apply();


                            editor.putString("telephone", numberTail.getText().toString() + number.getText().toString());
                            editor.apply();
                            hideKeyboard(Objects.requireNonNull(getCurrentFocus()));
                            phoneSignLayoutSecond = findViewById(R.id.get_code_phone_layout);
                            phoneSignLayoutFirst.setVisibility(View.GONE);
                            phoneSignLayoutSecond.setVisibility(View.VISIBLE);

                            final String[] mVerificationId = new String[1];
                            final PhoneAuthProvider.ForceResendingToken[] mResendToken = new PhoneAuthProvider.ForceResendingToken[1];
                            Button finish = findViewById(R.id.finish);
                            String phoneNumber = numberTail.getText().toString() + number.getText().toString();

                            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential credential) {
                                    signInWithPhoneAuthCredential(credential);
                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {

                                }
                                View progressView = findViewById(R.id.progress);
                                @Override
                                public void onCodeSent(@NonNull String verificationId,
                                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                    mVerificationId[0] = verificationId;
                                    codeEditText = findViewById(R.id.code);
                                    mResendToken[0] = token;
                                    finish.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            progressView.setVisibility(View.VISIBLE);
                                            finish.setVisibility(View.GONE);
                                            String code = codeEditText.getText().toString();
                                            if (code != null) {
                                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                                                signInWithPhoneAuthCredential(credential);
                                            }
                                        }
                                    });
                                }
                            };

                            Activity myActivity = (Activity) context;
                            PhoneAuthOptions options =
                                    PhoneAuthOptions.newBuilder(mAuth)
                                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                                            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                                            .setActivity(myActivity)
                                            .setCallbacks(mCallbacks)
                                            .build();

                            PhoneAuthProvider.verifyPhoneNumber(options);

                            }
                        }
                    }
            });
        }



        else{
            actualSignIn.setVisibility(View.VISIBLE);
            adapter = new TabAdapter(getSupportFragmentManager());
            adapter.addFragment(new Sinscrire(), "S'inscrire");
            adapter.addFragment(new SeConnecter(), "Se connecter");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }
    void toggleViewBack(){
        back.setVisibility(View.GONE);
        actualSignIn.setVisibility(View.GONE);
        choose.setVisibility(View.VISIBLE);
        loginText.setVisibility(View.VISIBLE);
        phoneSignLayoutSecond = findViewById(R.id.get_code_phone_layout);
        phoneSignLayoutFirst = findViewById(R.id.phone_sign_in_layout);
        if(phoneSignLayoutFirst.getVisibility()==View.VISIBLE || phoneSignLayoutSecond.getVisibility() == View.VISIBLE){
            phoneSignLayoutFirst.setVisibility(View.GONE);
            phoneSignLayoutSecond.setVisibility(View.GONE);
        }
        phoneSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView(0);
            }
        });
        emailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView(1);
            }
        });
    }
    View progress;
    Button tryAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        loginText = findViewById(R.id.login_text);
        mContext = getApplicationContext();
        choose = findViewById(R.id.choose);
        actualSignIn = findViewById(R.id.actual_sign_in);
        back = findViewById(R.id.back);
        phoneSignIn = findViewById(R.id.phone);
        emailSignIn = findViewById(R.id.email);

        sharedPreferences =getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        viewPager = findViewById(R.id.sign_up_view_pager);
        tabLayout = findViewById(R.id.tabLayout);

        tryAgain = findViewById(R.id.retry);
        progress = findViewById(R.id.progress);
        numberTail = findViewById(R.id.number_tail);
        number = findViewById(R.id.phone_number);
        connectWithPhone = findViewById(R.id.se_connecter_button);
        ImageView myLogo = findViewById(R.id.logo);
        mAuth = FirebaseAuth.getInstance();

        phoneSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView(0);
            }
        });
        emailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView(1);
            }
        });
        myLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Fetching.waitInternetAvailable(getApplicationContext())){
                    Fetching.makeCustomToast(getApplicationContext(), "Connectez-vous à Internet", Toast.LENGTH_SHORT);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Spinning.class);
                    startActivity(intent);
                    finish();
                }
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
