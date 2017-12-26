package com.example.shubham_gupta.cameratest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Main4Activity extends AppCompatActivity {

    private ImageView imageView;
    private Button uploadButton;
    private static final int SELECT_PICTURE = 100;
    private FirebaseAuth auth;

    // creating an instance of Firebase Storage
    //  FirebaseStorage storage = FirebaseStorage.getInstance().getInstance()
    //creating a storage reeference. Replace the below URL with your Firebase storage URL.
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference. Replace the below URL with your Firebase storage URL.
    StorageReference storageRef = storage.getReferenceFromUrl("gs://cameratest-459ca.appspot.com");

    //public StorageReference StorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        getSupportActionBar().hide();
        // StorageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        // Firebase.setAndroidContext(this);
        //getting the reference of the views
        imageView = (ImageView) findViewById(R.id.imageview);
        uploadButton = (Button) findViewById(R.id.uploadbutton);
        onImageViewClick(); // for selecting an Image from gallery.
        onUploadButtonClick(); // for uploading the image to Firebase Storage.
    }

    protected  void onImageViewClick(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"),SELECT_PICTURE );
            }
        });

    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i("IMAGE PATH TAG", "Image Path : " + path);
                    // Set the image in ImageView
                    imageView.setImageURI(selectedImageUri);

                }
            }
        }
    }
    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    protected void onUploadButtonClick(){

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int x=0;
                x++;
                final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                StorageReference myfileRef = storageRef.child("upload1"+x+".jpg");

                imageView.setDrawingCacheEnabled(true);

                imageView.buildDrawingCache();

                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                byte[] data = baos.toByteArray();

                UploadTask uploadTask = myfileRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(Main4Activity.this, "TASK FAILED", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(Main4Activity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        @SuppressWarnings("VisibleForTests")   Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String DOWNLOAD_URL = downloadUrl.getPath();
                        Log.v("DOWNLOAD URL", DOWNLOAD_URL);
                        Toast.makeText(Main4Activity.this, DOWNLOAD_URL, Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")double progress = (100.0 * (float)taskSnapshot.getBytesTransferred()) / (float)taskSnapshot.getTotalByteCount();
                        Toast.makeText(Main4Activity.this, "File uploading "+ ((int) progress) + "%...", Toast.LENGTH_SHORT).show();
                        System.out.println("File uploading :"+(int)progress+"%...");
                        progressDialog.setTitle("Uploading");
                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");


                    }
                });
            }
        });

    }
}