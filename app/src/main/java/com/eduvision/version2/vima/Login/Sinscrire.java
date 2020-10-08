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
import android.text.TextUtils;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.eduvision.version2.vima.Home;
import com.eduvision.version2.vima.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 */
public class Sinscrire extends Fragment {
    private final static int RC_SIGN_IN = 2;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("Users/Acheteurs");
    SignInButton signup;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText Email, Telephone, Password, Username;
    String email, telephone, password, username;
    Button sinscrire;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private CheckBox showPassword;

    //Localisation
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user;
    // TODO: Rename parameter arguments, choose names that match

    public Sinscrire() {

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
        return inflater.inflate(R.layout.fragment_sinscrire, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Localisation
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());



        radioSexGroup = getView().findViewById(R.id.radio_gender);

        sharedPreferences = getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Email = getView().findViewById(R.id.sinscrire_email);
        Telephone = getView().findViewById(R.id.sinscrire_telephone);
        Password = getView().findViewById(R.id.sinscrire_password);
        showPassword = getView().findViewById(R.id.checkbox);
        Username = getView().findViewById(R.id.sinscrire_username);
        sinscrire = getView().findViewById(R.id.sinscrire_button);
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

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

        sinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = Email.getText().toString();
                telephone = Telephone.getText().toString();
                password = Password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Entrez votre e-mail", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(telephone)) {
                    Toast.makeText(getApplicationContext(), "Entrez votre numero de telephone", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password) || password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Votre mot de passe doit exceder 8 caracteres ", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();

                    radioSexButton = getView().findViewById(selectedId);
                    String selection = radioSexButton.getText().toString();
                    editor.putString("gender", selection);
                    editor.apply();

                    username = Username.getText().toString();
                    editor.putString("username", username);
                    editor.apply();


                    editor.putString("telephone", telephone);
                    editor.apply();

                    editor.putString("email", email);
                    editor.apply();


                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                }

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

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Authentication success",
                            Toast.LENGTH_SHORT).show();
                    telephone = Telephone.getText().toString();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String Email = user.getEmail();
                    String ID = user.getUid();
                    String gender = sharedPreferences.getString("gender", "nothing");
                    String username = sharedPreferences.getString("username", "nothing");
                    usersRef.child(ID).setValue(new User(Email, ID, telephone, gender, username));

                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        OnGPS();
                    } else {
                        getLocation();
                    }
Intent t = new Intent(getContext(), Home.class);
                    startActivity(t);

                }
                else {
                    Toast.makeText(getContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static class User {

        public String email;
        public String id;
        public String telephone;
        public String gender;
        public String username;


        public User(String email, String id, String telephone, String gender, String username){
            this.email =email;
            this.id = id;
            this.telephone = telephone;
            this.gender = gender;
            this.username = username;

        }
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
                getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
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

