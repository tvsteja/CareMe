package com.careme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.careme.Model.GetPostItem;
import com.careme.Model.PostItem;
import com.careme.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class AddpostActivityParent extends AppCompatActivity {

    private EditText hl_et, add_et, hr_et, date_et, time_et, desc_et, phone_et;
    private TextView tvPostTitle;
    private ImageView ivProfilePic;
    private String hl, add, hr, time, date, des, phone, type = "parent";
    private ProgressBar pb;
    private Button btnAddPost;
    private DatabaseReference rootRef;
    private StorageReference storageReference;
    private String filePath = "";
    private String Storage_Path = "All_Image_Uploads/";
    private String Database_Path = "userpost";
    private String uid;
    private ImageView iv1,iv2;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static DecimalFormat formatter = new DecimalFormat("00");
    private String from = "", headLine = "", phoneNumber = "", address = "", hrate = "", pickDate = "", pickTime = "", jobDesc = "", postType = "", pid = "", img = "";
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_activity_parent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseApp.initializeApp(this);
        rootRef = FirebaseDatabase.getInstance().getReference(Database_Path);
        storageReference = FirebaseStorage.getInstance().getReference();
        intent = getIntent();
        if(intent.hasExtra("From")){
            from = intent.getExtras().getString("From");
            headLine = intent.getExtras().getString("t");
            hrate            = intent.getExtras().getString("r");
            img              = intent.getExtras().getString("img");
            pickDate         = intent.getExtras().getString("dt");
            pickTime         = intent.getExtras().getString("time");
            address          = intent.getExtras().getString("add");
            jobDesc          = intent.getExtras().getString("des");
            postType         = intent.getExtras().getString("type");
            phoneNumber      = intent.getExtras().getString("phone");
            pid              = intent.getExtras().getString("pid");

        }
        ivProfilePic         = findViewById(R.id.ivProfilePic);
        tvPostTitle          = findViewById(R.id.tvPostTitle);
        hl_et                = findViewById(R.id.hl_et);
        hr_et                = findViewById(R.id.rate_et);
        date_et              = findViewById(R.id.date_et);
        time_et              = findViewById(R.id.time_et);
        add_et               = findViewById(R.id.add_et);
        desc_et              = findViewById(R.id.desc_et);
        pb                   = findViewById(R.id.pb);
        phone_et             = findViewById(R.id.phone_et);
        iv1                  = findViewById(R.id.dateiv);
        iv2                  = findViewById(R.id.timeiv);
        btnAddPost           = findViewById(R.id.btnAddPost);
        if(!img.equalsIgnoreCase("")){
            tvPostTitle.setText("Update Post");
            Picasso.get().load(img).placeholder(R.drawable.logo).into(ivProfilePic);
        }
        else {
            tvPostTitle.setText("Add Post");
        }
        hl_et.setText(headLine);
        hr_et.setText(hrate);
        add_et.setText(address);
        phone_et.setText(phoneNumber);
        date_et.setText(pickDate);
        time_et.setText(pickTime);
        desc_et.setText(jobDesc);

        if(from.equalsIgnoreCase("")){
            btnAddPost.setText("Add Post");
        }
        else {
            btnAddPost.setText("Update Post");
        }

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddpostActivityParent.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date_et.setText(formatter.format(dayOfMonth) + "-" + formatter.format(monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddpostActivityParent.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                time_et.setText(formatter.format(hourOfDay)+ ":" + formatter.format(minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddpostActivityParent.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(v);
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

    private void add(View view) {

        hl = hl_et.getText().toString().trim();
        add = add_et.getText().toString().trim();
        hr = hr_et.getText().toString().trim();
        date = date_et.getText().toString().trim();
        time = time_et.getText().toString().trim();
        des = desc_et.getText().toString().trim();
        phone = phone_et.getText().toString().trim();

//        if (r1.isChecked()) {
//            type = r1.getText().toString();
//        } else if (r2.isChecked()) {
//            type = r2.getText().toString();
//        } else if (r3.isChecked()) {
//            type = r3.getText().toString();
//        } else if (r4.isChecked()) {
//            type = r4.getText().toString();
//        } else {
//            type = r1.getText().toString();
//        }


        if (hl.isEmpty()) {
            hl_et.setError("Required");
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
        else if (hr.isEmpty()) {
            hr_et.setError("Required");
        }
        else if (date.isEmpty()) {
            date_et.setError("Required");
        }
        else if (time.isEmpty()) {
            time_et.setError("Required");
        }
        else if (des.isEmpty()) {
            desc_et.setError("Required");
        }
        else {
            pb.setVisibility(View.VISIBLE);
            uid = replaceString(PreferenceManager.GetEmail());
            final String postBy = PreferenceManager.GetFn()+" "+PreferenceManager.GetLn();
            final String email = PreferenceManager.GetEmail();
            if(from.equalsIgnoreCase("")){
                addNewPost(postBy, email);
            }
            else {
                updatePost(postBy, email);
            }
        }
    }

    private void addNewPost(final String postBy, final String email){
        if (filePath.isEmpty()) {
            String ImageUploadId = rootRef.push().getKey();
            String staticimage = "https://cdn3.iconfinder.com/data/icons/users-23/64/_Male_Profile_Round_Circle_Users-512.png";
            PostItem postItem = new PostItem(PreferenceManager.GetUID(), hl, add, hr, date, time, "", des, staticimage, type, phone,
                    PreferenceManager.GetUID() + ImageUploadId, postBy, email, "");
            rootRef.child(ImageUploadId).setValue(postItem);
            pb.setVisibility(View.GONE);
            Toast.makeText(AddpostActivityParent.this, "Post Added Successfully!!!", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else {
            Uri FilePathUri = Uri.fromFile(new File(filePath));
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + "jpeg");
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String ImageUploadId = rootRef.push().getKey();
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            PostItem postItem = new PostItem(PreferenceManager.GetUID(), hl, add, hr, date, time, "", des,
                                    url.toString(), type, phone, PreferenceManager.GetUID() + ImageUploadId, postBy, email, "");
                            rootRef.child(ImageUploadId).setValue(postItem);
                            pb.setVisibility(View.GONE);
                            Toast.makeText(AddpostActivityParent.this, "Post Add Success!!!", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(AddpostActivityParent.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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

    private void updatePost(final String postBy, final String email){
        final Query query = FirebaseDatabase.getInstance().getReference().child(Database_Path).orderByChild("pid").equalTo(pid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        GetPostItem postItem = child.getValue(GetPostItem.class);
                        if(pid.equalsIgnoreCase(postItem.getPid())){
                            child.getRef().child("add").setValue(add);
                            child.getRef().child("date").setValue(date);
                            child.getRef().child("desc").setValue(des);
                            child.getRef().child("email").setValue(email);
                            child.getRef().child("hl").setValue(hl);
                            child.getRef().child("hr").setValue(hr);
                            child.getRef().child("phone").setValue(phone);
                            child.getRef().child("pid").setValue(pid);
                            child.getRef().child("postBy").setValue(postBy);
                            child.getRef().child("time").setValue(time);
                            child.getRef().child("type").setValue(type);
                            child.getRef().child("uid").setValue(PreferenceManager.GetUID());
                            if(filePath.equalsIgnoreCase("")){
                                child.getRef().child("img").setValue(img);
                            }
                            else {
                                Uri FilePathUri = Uri.fromFile(new File(filePath));
                                StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + "jpeg");
                                storageReference2nd.putFile(FilePathUri);
                            }
                            Toast.makeText(AddpostActivityParent.this, "Post Updated Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddpostActivityParent.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            break;
                        }
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pb.setVisibility(View.GONE);
            }
        });
    }

    private String replaceString(String string) {
        return string.replaceAll("[@.-_;\\/:*?\"<>|&']", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            ivProfilePic.setImageBitmap(selectedImage);
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
