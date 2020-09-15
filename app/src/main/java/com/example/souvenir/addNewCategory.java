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
import com.example.souvenir.R;

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

public class addNewCategory extends AppCompatActivity {
    String name,userid;
    Button addCategory;
    EditText Cname;
    ImageView selectImage,bc;
    Uri imageUri;
    String CategoryRandomKey,downloadImageUrl,saveCurrentDate,saveCurrentTime;
    static final int  galleryPick=1;
    StorageReference CategoryImagesref;
    DatabaseReference databaseref;
    ProgressDialog loadingBar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);
        try {

            //getting the toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            //setting the title
            toolbar.setTitle("add new Category");
            bc= (ImageView) findViewById(R.id.bc);
            CategoryImagesref = FirebaseStorage.getInstance().getReference().child("Category Images");
            databaseref = FirebaseDatabase.getInstance().getReference().child("Categories");
            addCategory = (Button) findViewById(R.id.add_new_category);
            Cname = (EditText) findViewById(R.id.category_name);
            selectImage = (ImageView) findViewById(R.id.Select_Category_Image);

            bc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in2=new Intent( addNewCategory.this,Main.class);
                    startActivity(in2);

                }
            });
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();

            if (firebaseUser.getUid().equals("8CxyfESWn8XgpkqBlpBXLhbBVPl2")) {
                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                loadingBar = new ProgressDialog(this);

                // Toast.makeText(this,categoryName,Toast.LENGTH_LONG).show();
                selectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenGallery();
                    }
                });

                addCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ValidateData();
                    }
                });


            }
            else{
                throw new Exception();
            }
        }

        catch (Exception ex)
        {



        }
    }

    private void ValidateData() {


        name=Cname.getText().toString();

        if(imageUri==null)
            Toast.makeText(this,"Category image is required..",Toast.LENGTH_LONG).show();

        else if(TextUtils.isEmpty(name))

        {
            Toast.makeText(this,"Please Enter the category subject",Toast.LENGTH_LONG).show();
        }

        else
        {
            storePrductInformation();
        }
    }

    private void storePrductInformation() {
        loadingBar.setTitle("Add New Category");
        loadingBar.setMessage("Please wait, while we adding the category");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        CategoryRandomKey=saveCurrentDate+saveCurrentTime;
        final StorageReference filePath=CategoryImagesref.child(imageUri.getLastPathSegment()+CategoryRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.toString();
                Toast.makeText(addNewCategory.this,"Error: "+msg,Toast.LENGTH_LONG).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Toast.makeText(addNewCategory.this,"Image uploaded successfully",Toast.LENGTH_LONG).show();
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
                            //Toast.makeText(addNewCategory.this,"got the category Image url successfully",Toast.LENGTH_LONG).show();
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
            HashMap<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("cid", CategoryRandomKey);
            categoryMap.put("date", saveCurrentDate);
            categoryMap.put("time", saveCurrentTime);
            categoryMap.put("name", name);
            categoryMap.put("image", downloadImageUrl);

            databaseref.child(userid).child(CategoryRandomKey).updateChildren(categoryMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                loadingBar.dismiss();
                                Toast.makeText(addNewCategory.this, "Category is added successfully", Toast.LENGTH_LONG).show();
                                Intent in = new Intent(addNewCategory.this, Main.class);
                                startActivity(in);
                            } else {
                                loadingBar.dismiss();
                                String msg = task.getException().toString();
                                Toast.makeText(addNewCategory.this, "Error: " + msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });



        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();



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
