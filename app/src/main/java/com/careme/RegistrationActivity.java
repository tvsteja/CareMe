package com.careme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.careme.Model.UserItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class RegistrationActivity extends AppCompatActivity {

    EditText email_et, pass_et, fn_et, ln_et, phone_et, add_et, cpass_et, age_et;
    String email, pass, fn, ln, age, cpass, add, gender, phone;
    ProgressBar pb;
    RadioButton r1, r2, r3;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern = "^[0-9]*$";
    DatabaseReference rootRef;
    String uid;
    String check;
    ImageView iv;
    String filePath = "";
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    String Database_Path = "user";
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email_et = findViewById(R.id.email_et);
        pass_et = findViewById(R.id.pass_et);
        ln_et = findViewById(R.id.ln_et);
        fn_et = findViewById(R.id.fn_et);
        phone_et = findViewById(R.id.phone_et);
        add_et = findViewById(R.id.add_et);
        cpass_et = findViewById(R.id.cpass_et);
        age_et = findViewById(R.id.age_et);
        pb = findViewById(R.id.pb);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        iv = findViewById(R.id.iv);

        FirebaseApp.initializeApp(this);
        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reg(View view) {
        email = email_et.getText().toString().trim();
        pass = pass_et.getText().toString().trim();
        fn = fn_et.getText().toString().trim();
        ln = ln_et.getText().toString().trim();
        phone = phone_et.getText().toString().trim();
        add = add_et.getText().toString().trim();
        age = age_et.getText().toString().trim();
        cpass = cpass_et.getText().toString().trim();
        if (r1.isChecked()) {
            gender = "male";
        } else if (r2.isChecked()) {
            gender = "female";
        } else {
            gender = "other";
        }
        if (fn.isEmpty()) {
            fn_et.setError("Required");
        }
        else if (ln.isEmpty()) {
            ln_et.setError("Required");
        }
        else if (email.isEmpty()) {
            email_et.setError("Required");
        }
        else if (!email.matches(emailPattern)) {
            email_et.setError("Not Valid");
        }
        else if (phone.isEmpty()) {
            phone_et.setError("Required");
        }
        else if (phone.length() < 10) {
            phone_et.setError("10 digit Required");
        }
        else if (add.isEmpty()) {
            add_et.setError("Required");
        }
        else if (age.isEmpty()) {
            age_et.setError("Required");
        }
        else if (Integer.parseInt(age) < 15) {
            age_et.setError("minimum above 15 Required");
        }
        else if (pass.isEmpty()) {
            pass_et.setError("Required");
        }
        else if (pass.length() < 6) {
            pass_et.setError("minimum 6 character Required");
        }
        else if (pass.length() > 8) {
            pass_et.setError("maximum 8 character Required");
        }
        else if (cpass.isEmpty()) {
            cpass_et.setError("Required");
        }
        else if (!cpass.equals(pass)) {
            cpass_et.setError("Both Password Same");
        }
        else {
            pb.setVisibility(View.VISIBLE);
            uid = replaceString(email);
            rootRef.child(Database_Path).orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this, "This email is already existed.", Toast.LENGTH_SHORT).show();
                    } else {
                        //Username does not exist
                        rootRef.child(Database_Path).orderByChild("uid").equalTo(uid).removeEventListener(this);
                        registerUser(uid, this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(RegistrationActivity.this, "Registration failed, please try again later!.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void registerUser(final String uid, ValueEventListener valueEventListener) {
        check = email + "_" + pass;
        if (filePath.isEmpty()) {
            String staticimage = "https://cdn3.iconfinder.com/data/icons/users-23/64/_Male_Profile_Round_Circle_Users-512.png";
            Toast.makeText(RegistrationActivity.this, "Registration Successfully!!!!Go and Login", Toast.LENGTH_SHORT).show();
            UserItem userItem = new UserItem(uid, fn, ln, email, phone, add, gender, age, pass, check, staticimage);
            rootRef.child(Database_Path).child(userItem.getUid()).setValue(userItem);
            pb.setVisibility(View.GONE);
            finish();
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            Uri FilePathUri = Uri.fromFile(new File(filePath));
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + "jpeg");
            storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String ImageUploadId = rootRef.push().getKey();
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uri.isComplete()) ;
                    Uri url = uri.getResult();
                    Toast.makeText(RegistrationActivity.this, "Registration Successfully!!!!Go and Login", Toast.LENGTH_SHORT).show();
                    UserItem userItem = new UserItem(uid, fn, ln, email, phone, add, gender, age, pass, check, url.toString());
                    rootRef.child("user").child(userItem.getUid()).setValue(userItem);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            pb.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    String replaceString(String string) {
        return string.replaceAll("[@.-_;\\/:*?\"<>|&']", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            iv.setImageBitmap(selectedImage);
        }
    }
}
