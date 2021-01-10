package com.eduvision.version2.vima;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

public class ProfilePage extends AppCompatActivity {
    private static final int IMAGE_REQUEST = 1;
    private static final int REQUEST_LOCATION = 1;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth mAuth;
    TextView name, telephone, email, adresse;
    ImageView profile;
    String username, Phone, Email;
    ImageButton settings, back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LocationManager locationManager;
    String latitude, longitude;
    FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Uri ImageUri;
    private StorageTask uploadTask;
    Geocoder geocoder;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page_layout);

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()); //For location
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Location
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //SharedPreferences
        sharedPreferences = getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Firebase
        user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures").child(user.getUid());

        settings = findViewById(R.id.settings);

        AlertDialog.Builder modifysettings = new AlertDialog.Builder(this);
        modifysettings.setTitle("Modifications");
        final View customLayout = getLayoutInflater().inflate(R.layout.model_custom_dialog_profile_modification, null);
        modifysettings.setView(customLayout);
        AlertDialog dialog = modifysettings.create();
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            }
        });
        //Changing the username
        Button ChangeUsername = customLayout.findViewById(R.id.change_username);
        AlertDialog.Builder newUsername = new AlertDialog.Builder(this);
        newUsername.setTitle("Insérer");

        final View customUsername = getLayoutInflater().inflate(R.layout.model_custom_dialog_modify_username, null);
        newUsername.setView(customUsername);
        AlertDialog userdialog = newUsername.create();
        ChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdialog.show();

                EditText text = customUsername.findViewById(R.id.new_username);

                Button save = customUsername.findViewById(R.id.save_new_username);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialog progressDialog = new ProgressDialog(ProfilePage.this);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        String getUsername = text.getText().toString();
                        Log.d(TAG, "Username is: " + getUsername);

                        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Acheteurs").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("username", getUsername);
                        mDatabase.updateChildren(map);
                        username = sharedPreferences.getString("username", "nothing");
                        editor.putString("username", getUsername);
                        editor.apply();
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Sauvegarde reussie", Toast.LENGTH_SHORT).show();

                        name = findViewById(R.id.name);
                        username = sharedPreferences.getString("username", "nothing");
                        name.setText(username);
                        userdialog.hide();
                        dialog.hide();

                    }
                });

            }
        });



        //Change number
        Button ChangeNumber = customLayout.findViewById(R.id.change_number);

        ChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdialog.show();

                EditText number = customUsername.findViewById(R.id.new_username);
                Button saveNumber = customUsername.findViewById(R.id.save_new_username);
                saveNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialog progressDialog = new ProgressDialog(ProfilePage.this);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        String getNumber = number.getText().toString();

                        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Acheteurs").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("telephone", getNumber);
                        mDatabase.updateChildren(map);
                        Phone = sharedPreferences.getString("telephone", "Entrez votre numéro de téléphone");
                        editor.putString("telephone", getNumber);
                        editor.apply();
                        progressDialog.dismiss();

                        Fetching.makeCustomToast(getApplicationContext(), "Sauvegarde réussie", Toast.LENGTH_SHORT);

                        telephone = findViewById(R.id.numero_de_telephone);
                        Phone = sharedPreferences.getString("telephone", "nothing");
                        telephone.setText(Phone);
                        userdialog.hide();
                        dialog.hide();

                        number.setText("");


                    }
                });
            }
        });



        //Change email
        Button ChangeEmail = customLayout.findViewById(R.id.change_email);

        ChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdialog.show();

                EditText Mail = customUsername.findViewById(R.id.new_username);
                Button saveEmail = customUsername.findViewById(R.id.save_new_username);
                saveEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialog progressDialog = new ProgressDialog(ProfilePage.this);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        String getEmail = Mail.getText().toString();

                        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Acheteurs").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("email", getEmail);
                        mDatabase.updateChildren(map);
                        Email = sharedPreferences.getString("email", "Entrez votre email");
                        editor.putString("email", getEmail);
                        editor.apply();
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Sauvegarde réussie", Toast.LENGTH_SHORT).show();

                        email = findViewById(R.id.adresse_mail);
                        Email = sharedPreferences.getString("email", "Entrez votre email");
                        email.setText(Email);
                        userdialog.hide();
                        dialog.hide();

                    }
                });
            }
        });



        //Change location
        Button Changelocation = customLayout.findViewById(R.id.change_location);
        Changelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }

            }
        });

        //Sign out
        Button signOut = customLayout.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent a = new Intent(getApplicationContext(), com.eduvision.version2.vima.Login.login_activity.class);
                startActivity(a);
            }
        });

        //Modify picture
        Button ChangePicture = customLayout.findViewById(R.id.change_picture);
        ChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        name = findViewById(R.id.name);
        username = sharedPreferences.getString("username", "nothing");
        name.setText(username);

        profile = findViewById(R.id.profile_picture);
        String profilePicture = sharedPreferences.getString("profile", "https://www.google.com/search?q=placeholder+profile+pictures+free+to+use&tbm=isch&ved=2ahUKEwjA6ZvV2tDrAhUElBoKHd_bDRIQ2-cCegQIABAA&oq=placeholder+profile+pictures+free+to+use&gs_lcp=CgNpbWcQAzoECAAQHlC7YVixcGCvcWgAcAB4AIAB5QWIAbsVkgEHNC0zLjEuMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=ANVSX8DpHoSoat-3t5AB&bih=792&biw=1536#imgrc=_JeJ3jskVgcZaM");
        Glide.with(getApplicationContext())
                .load(profilePicture)
                .placeholder(R.drawable.categorie_enfant)
                .into(profile);

        telephone = findViewById(R.id.numero_de_telephone);
        Phone = sharedPreferences.getString("telephone", "Entrez votre numéro de téléphone");
        telephone.setText(Phone);

        email = findViewById(R.id.adresse_mail);
        String Email = sharedPreferences.getString("email", "Entrez votre email");
        email.setText(Email);

        adresse = findViewById(R.id.adresse);
        String Adresse = sharedPreferences.getString("location", "Ajoutez votre adresse");
       adresse.setText(Adresse);



        /*
        numb_fav = findViewById(R.id.number_of_favorites);
        photo = findViewById(R.id.profile_picture);
        final String[] eventDate = new String[1];
        final String[] eventTime = new String[1];
        settings = findViewById(R.id.settings);
        myArray = new ArrayList<>(3);

        mDatabase.child("Customers").child(Integer.toString(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameA = Objects.requireNonNull(dataSnapshot.child("first_name").getValue()).toString();
                nameA = nameA + Objects.requireNonNull(dataSnapshot.child("last_name").getValue()).toString();
                name.setText(nameA);
                String numb_likeA = Integer.toString((int) dataSnapshot.child("likedItems").getChildrenCount());
                numb_likes.setText(numb_likeA);
                String numb_favA = Integer.toString((int) dataSnapshot.child("favItems").getChildrenCount());
                numb_likes.setText(numb_likeA);
                numb_fav.setText(numb_favA);
                String photoA = dataSnapshot.child("photo").getValue().toString();
                Glide.with(getApplicationContext())
                        .load(photoA)
                        .into(photo);
                for(int i=1; i<=dataSnapshot.child("likedItems").getChildrenCount(); i++){
                    String[] temp = new String[5];
                    temp[2] = "Ajouté aux Favoris";
                    mDatabase.child("Articles");
                    mDatabase.child(Integer.toString(i));
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            temp[0] = Objects.requireNonNull(dataSnapshot.child("infos").child("name").getValue()).toString();
                            temp[3] = Objects.requireNonNull(dataSnapshot.child("pictures").child("p_photo").getValue()).toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // **********************************************************
                    // We're finding the date and time when the article was liked

                    eventDate[0] = Objects.requireNonNull(dataSnapshot.child("likedItems").child(Integer.toString(i)).child("eventDate").getValue()).toString();
                    eventTime[0] = Objects.requireNonNull(dataSnapshot.child("likedItems").child(Integer.toString(i)).child("eventTime").getValue()).toString();
                    // Convert these to a suitable DateTime object
                    DateTime targetDateTime = DateTime.parse(format("%s %s", eventDate[0], eventTime[0]), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
                    // Grab a timestamp for the current DateTime
                    DateTime now = DateTime.now();
                    // Find the period between the two DateTimes
                    Period period = new Period(now, targetDateTime);
                    temp[4] = Integer.toString(((int) now.getMillis()));
                    // Convert the period to suitable String
                    String prettyPeriod = PeriodFormat.getDefault().print(period);
                    temp[1] = "Il y a " + prettyPeriod;

                    //Waiting for the Article ValueListener to do its job as the loading is asynchronous
                    while(temp[0] == null || temp[3]==null){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myArray.add(temp);
                }

                //Sorting the array by ascending order of the millis that is in order of recentness
                Collections.sort(myArray, new Comparator<String[]>() {
                    public int compare(String[] time, String[] others) {
                        return time[4].compareTo(others[4]);
                    }
                });
                gridView.setAdapter(new Profile_Article_Adapter(myArray));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        //Get to chosen Article page
                        Intent myIntent = new Intent(ProfilePage.this, articlePage.class);
                        myIntent.putExtra("id", myArray.get(position));
                        startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfilePage.this, ProfileSettings.class);
            }
        });

    }
    */
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                ProfilePage.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ProfilePage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(ProfilePage.this);
            progressDialog.setMessage("Sauvegarde en cours");
            progressDialog.show();
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            Log.d(TAG, "We made it: " + location);
                            if (location != null) {

                                double lat = location.getLatitude();
                                double longi = location.getLongitude();

                                latitude = String.valueOf(lat);
                                longitude = String.valueOf(longi);


                                List<Address> addresses = new ArrayList<>();

                                try {
                                    addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                progressDialog.dismiss();
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


    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sauvegarde en cours");
        progressDialog.show();

        if (ImageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() +"."+getFileExtension(ImageUri));

            uploadTask = fileReference.putFile(ImageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if (!task.isSuccessful()){
                       throw  task.getException();
                   }
                   return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        editor.putString("profile", mUri);
                        editor.apply();


                        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Acheteurs").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("ImageURL", mUri);
                        mDatabase.updateChildren(map);


                        Glide.with(getApplicationContext())
                                .load(mUri)
                                .placeholder(R.drawable.categorie_enfant)
                                .into(profile);

                        progressDialog.dismiss();
                    }else {
                        Fetching.makeCustomToast(getApplicationContext(), "Veuillez reessayer", Toast.LENGTH_SHORT);
                        progressDialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });


            }
        else{
            Toast.makeText(getApplicationContext(), "Veuillez resélectionner une image", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() !=null){
            ImageUri = data.getData();

        }

        if (uploadTask !=null && uploadTask.isInProgress()){
            Toast.makeText(getApplicationContext(), "Sauvegarde en cours", Toast.LENGTH_SHORT).show();
        }else {
            uploadImage();

        }
    }




}
