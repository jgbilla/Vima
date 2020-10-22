package com.eduvision.version2.vima;

import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.appevents.codeless.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
    ArrayList<String> myLinks;
    ArrayList<Uri> myUrls;
    private Button btnSelect, btnUpload;
    private ImageView imageView;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private DatabaseReference mDatabase;
    private String selectedImagePath;
    private String filemanagerstring;
    public void buttonClicked(View v) {

    }

    public void chooseButtonClicked(View v) {
        chooseImage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        myLinks = new ArrayList<>(3);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_upload);
        Button up1, up2, up3, up4, bp, fp, sp, tp, btnupload;
        EditText title, rank, description, price, shop;

        up1 = findViewById(R.id.uploadbphoto);

        btnupload = findViewById(R.id.upload);
        title = findViewById(R.id.title);
        rank = findViewById(R.id.rank);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        shop = findViewById(R.id.shop);
        bp = findViewById(R.id.bphoto);

        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        uploadMultipleImages();
                        Article myArticle = new Article(
                                description.getText().toString(),
                                title.getText().toString(),
                                price.getText().toString(),
                                shop.getText().toString(),
                                myLinks.get(0),
                                myLinks.get(1),
                                myLinks.get(2),
                                myLinks.get(3)
                        );
                        myLinks.clear();
                        int count = Integer.parseInt(
                                String.valueOf(snapshot.child("myArticles").getChildrenCount()));
                        mDatabase.child("myArticles").setValue(myArticle);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference riversRef = storageReference.child("images/"+selectedImageUri.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(selectedImageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");}
                            });


                filemanagerstring = selectedImageUri.getPath();
                selectedImagePath = getPath(selectedImageUri);
                Toast.makeText(this.getApplicationContext(), selectedImagePath, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
    private void uploadMultipleImages() {

        for (int a = 0; a<myUrls.size(); a++){
            final String[] myLink = new String[1];
            Uri current = myUrls.get(a);
            if(current != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                ref.putFile(current)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                myLink[0] = String.valueOf(ref.getDownloadUrl());
                                progressDialog.dismiss();
                                Toast.makeText(UploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(UploadActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }
            myLinks.add(String.valueOf(myLink));
        }
    }
}
