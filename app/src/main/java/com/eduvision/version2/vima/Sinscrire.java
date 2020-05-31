package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText Email, Telephone, Password;
    String email, telephone, password;
    Button sinscrire;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
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
        return  inflater.inflate(R.layout.fragment_sinscrire, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioSexGroup = getView().findViewById(R.id.radio_gender);

        sharedPreferences =getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Email = getView().findViewById(R.id.sinscrire_email);
        Telephone = getView().findViewById(R.id.sinscrire_telephone);
        Password = getView().findViewById(R.id.sinscrire_password);
        sinscrire = getView().findViewById(R.id.sinscrire_button);
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

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
                }
                else {
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();

                    radioSexButton = getView().findViewById(selectedId);
                    String selection = radioSexButton.getText().toString();
                    editor.putString("gender", selection);
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
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Authentication success",
                            Toast.LENGTH_SHORT).show();
                    telephone = Telephone.getText().toString();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String Email = user.getEmail();
                    String ID = user.getUid();
                    String gender = sharedPreferences.getString("gender", "nothing");
                    usersRef.child(ID).setValue(new User(Email,ID,telephone, gender));
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


        public User(String email, String id, String telephone, String gender){
            this.email =email;
            this.id = id;
            this.telephone = telephone;
            this.gender = gender;

        }
    }



    }

