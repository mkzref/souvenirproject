package com.example.souvenir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class addNewProduct extends AppCompatActivity {
    String categoryName,pDescribtion,pName,pPrice,saveCurrentDate,saveCurrentTime,pQuantity;
    Button addProduct;
    EditText name,describtion,price,quantity;
    ImageView selectImage,back;
    Uri imageUri;
    String productRandomKey,downloadImageUrl;
    static final int  galleryPick=1;
    StorageReference productImagesref;
    DatabaseReference databaseref,databaseref2;
    ProgressDialog loadingBar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        categoryName = getIntent().getExtras().get("Category").toString();
        //String bar = getIntent().getExtras().get("actionBarTitle").toString();
        productImagesref = FirebaseStorage.getInstance().getReference().child("product images");
        databaseref = FirebaseDatabase.getInstance().getReference().child("Products");
        databaseref2 = FirebaseDatabase.getInstance().getReference().child("View Of Products");


        addProduct = (Button) findViewById(R.id.add_new_product);
        name = (EditText) findViewById(R.id.product_name);
        describtion = (EditText) findViewById(R.id.product_Describtion);
        price = (EditText) findViewById(R.id.product_price);
        if(categoryName.equals("Donations"))
            price.setVisibility(View.GONE);
        quantity = (EditText) findViewById(R.id.product_quantity);
        selectImage = (ImageView) findViewById(R.id.Select_Product_Image);
        try {
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
            loadingBar = new ProgressDialog(this);
            // Toast.makeText(this,categoryName,Toast.LENGTH_LONG).show();
            //getting the toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            //setting the title
            toolbar.setTitle("add new Product");
            back= (ImageView) findViewById(R.id.back2);
            selectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenGallery();
                }
            });

            addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ValidateProductData();
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in=new Intent( addNewProduct.this,Main.class);
                    startActivity(in);

                }
            });
        }
        catch (Exception ex)
        {



        }
    }

    private void ValidateProductData() {


        pName=name.getText().toString();
        pDescribtion=describtion.getText().toString();
        if(categoryName.equals("Donations"))
            pPrice= "For Free";
        else
            pPrice =price.getText().toString();
        pQuantity= String.valueOf(1);
        if(imageUri==null)
            Toast.makeText(this,"Product image is required..",Toast.LENGTH_LONG).show();
        else if(TextUtils.isEmpty(pDescribtion))

        {
            Toast.makeText(this,"Please write the product describtion",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pName))

        {
            Toast.makeText(this,"Please Enter the product subject",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pPrice))

        {
            Toast.makeText(this,"Please Enter the product price",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pQuantity))

        {
            Toast.makeText(this,"Please Enter the product quantity",Toast.LENGTH_LONG).show();
        }
        else
        {
            storePrductInformation();
        }
    }

    private void storePrductInformation() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait, while we adding the product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        productRandomKey=saveCurrentDate+saveCurrentTime;
        final StorageReference filePath=productImagesref.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.toString();
                Toast.makeText(addNewProduct.this,"Error: "+msg,Toast.LENGTH_LONG).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Toast.makeText(addNewProduct.this,"Image uploaded successfully",Toast.LENGTH_LONG).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            // loadingBar.dismiss();
                            throw  task.getException();

                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            // Toast.makeText(addNewProduct.this,"got the product Image url successfully",Toast.LENGTH_LONG).show();
                            saveProductInfoToTheDataBase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToTheDataBase() {
        try {


            String userid = firebaseUser.getUid();
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productRandomKey);
            productMap.put("ownerID", userid);
            productMap.put("date", saveCurrentDate);
            productMap.put("time", saveCurrentTime);
            productMap.put("category", categoryName);
            productMap.put("pName", pName.toLowerCase());
            productMap.put("pDescription", pDescribtion);
            if((categoryName.equals("Donations")))
            {
                productMap.put("pPrice", "For Free");}

            else if ((categoryName.equals("Art Hobbies"))) {
                productMap.put("pPrice", " ");
            }
            else
                productMap.put("pPrice", pPrice);
            productMap.put("image", downloadImageUrl);
            productMap.put("pQuantity", pQuantity);
            databaseref.child("Ownership Structure").child(userid).child("View Of Products").child(productRandomKey).updateChildren(productMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                loadingBar.dismiss();
                                Toast.makeText(addNewProduct.this, "Product is added successfully", Toast.LENGTH_LONG).show();
                                Intent in = new Intent(addNewProduct.this, Main.class);
                                startActivity(in);
                            } else {
                                loadingBar.dismiss();
                                String msg = task.getException().toString();
                                Toast.makeText(addNewProduct.this, "Error: " + msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });


            databaseref.child("View Of Products").child(productRandomKey).updateChildren(productMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                loadingBar.dismiss();
                                Toast.makeText(addNewProduct.this, "Product is added successfully", Toast.LENGTH_LONG).show();
                                Intent in = new Intent(addNewProduct.this, Main.class);
                                startActivity(in);
                            } else {
                                loadingBar.dismiss();
                                String msg = task.getException().toString();
                                Toast.makeText(addNewProduct.this, "Error: " + msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        catch (Exception ex)
        {


        }
    }

    private void OpenGallery() {
        Intent in=new Intent();
        in.setAction(Intent.ACTION_GET_CONTENT);
        in.setType("image/*");
        startActivityForResult(in,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            selectImage.setImageURI(imageUri);

        }
    }



}
