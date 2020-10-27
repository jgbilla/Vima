package com.eduvision.version2.vima.Login;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.eduvision.version2.vima.MainPage;
import com.eduvision.version2.vima.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;



public class SeConnecter extends Fragment {
    private final static int RC_SIGN_IN = 2;
    //Localisation
    private static final int REQUEST_LOCATION = 1;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText Email, Password;
    String email, password, id;
    Button seConnecter;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    TextView ForgottenPassword;
    DatabaseReference databaseReference;
    LocationManager locationManager;
    String latitude, longitude;
    FusedLocationProviderClient fusedLocationClient;
    FirebaseUser user;
    private CheckBox showPassword;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public SeConnecter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in_login_activity_tab_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Localisation
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        Email = getView().findViewById(R.id.se_connecter_email);
        Password = getView().findViewById(R.id.se_connecter_password);
        showPassword = getView().findViewById(R.id.checkbox);
        seConnecter = getView().findViewById(R.id.se_connecter_button);
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        sharedPreferences =getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Show and Hide password
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        seConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = Email.getText().toString();
                password = Password.getText().toString();

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });
ForgottenPassword = getView().findViewById(R.id.mot_de_passe_oublie);

ForgottenPassword.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent change = new Intent(getContext(), Reset_password.class);
        startActivity(change);
    }
});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Bienvenue",
                            Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
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
                                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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

                    Intent r = new Intent(getContext(), MainPage.class);
                    startActivity(r);

                } else {
                    Toast.makeText(getContext(), "Votre email ou mot de passe est incorrect",
                            Toast.LENGTH_SHORT).show();
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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

}
