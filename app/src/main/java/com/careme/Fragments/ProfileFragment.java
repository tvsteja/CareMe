package com.careme.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.careme.ChangePasswordActivity;
import com.careme.LoginActivity;
import com.careme.R;
import com.careme.Utils.CommonUtils;
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

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class ProfileFragment extends Fragment {

    private EditText email_et, fn_et, ln_et, phone_et, add_et, age_et;
    private TextView tvName, tvChangePassword;
    private ImageView ivProfilePic, ivEditFirstName, ivEditLastName, ivEditEmail, ivEditPhone, ivEditAddress, ivEditAge;
    private String email, pass, fn, ln, age, cpass, add, gender, phone;
    ProgressBar pb;
    RadioButton r1, r2, r3;
    DatabaseReference rootRef;
    String uid;
    private Button btnUpdate;
    private LinearLayout llLogout;
    String filePath = "";
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    StorageReference storageReference;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getActivity());
        storageReference            = FirebaseStorage.getInstance().getReference();
        rootRef                     = FirebaseDatabase.getInstance().getReference();
        View view                   = inflater.inflate(R.layout.profile_fragment, container, false);
        email_et                    = view.findViewById(R.id.email_et);
        ln_et                       = view.findViewById(R.id.ln_et);
        fn_et                       = view.findViewById(R.id.fn_et);
        phone_et                    = view.findViewById(R.id.phone_et);
        add_et                      = view.findViewById(R.id.add_et);
        age_et                      = view.findViewById(R.id.age_et);
        pb                          = view.findViewById(R.id.pb);
        r1                          = view.findViewById(R.id.r1);
        r2                          = view.findViewById(R.id.r2);
        r3                          = view.findViewById(R.id.r3);
        ivProfilePic                = view.findViewById(R.id.ivProfilePic);
        llLogout                    = view.findViewById(R.id.llLogout);
        btnUpdate                   = view.findViewById(R.id.btnUpdate);
        tvName                      = view.findViewById(R.id.tvName);
        tvChangePassword            = view.findViewById(R.id.tvChangePassword);
        ivEditFirstName             = view.findViewById(R.id.ivEditFirstName);
        ivEditLastName              = view.findViewById(R.id.ivEditLastName);
        ivEditEmail                 = view.findViewById(R.id.ivEditEmail);
        ivEditPhone                 = view.findViewById(R.id.ivEditPhone);
        ivEditAddress               = view.findViewById(R.id.ivEditAddress);
        ivEditAge                   = view.findViewById(R.id.ivEditAge);
        disableFields();
        ivEditFirstName.setOnClickListener(onClickListener);
        ivEditLastName.setOnClickListener(onClickListener);
        ivEditEmail.setOnClickListener(onClickListener);
        ivEditPhone.setOnClickListener(onClickListener);
        ivEditAddress.setOnClickListener(onClickListener);
        ivEditAge.setOnClickListener(onClickListener);
        tvChangePassword.setOnClickListener(onClickListener);
        llLogout.setOnClickListener(onClickListener);

        dataload();
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fn      = fn_et.getText().toString().trim();
                ln      = ln_et.getText().toString().trim();
                phone   = phone_et.getText().toString().trim();
                add     = add_et.getText().toString().trim();
                age     = age_et.getText().toString().trim();
                if (r1.isChecked()) {
                    gender = "male";
                }
                else if (r2.isChecked()) {
                    gender = "female";
                }
                else {
                    gender = "other";
                }
                if (fn.isEmpty()) {
                    fn_et.setError("Required");
                }
                else if (ln.isEmpty()) {
                    ln_et.setError("Required");
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
                else {
                    pb.setVisibility(View.VISIBLE);
                    PreferenceManager.SetphoneNo(phone);
                    PreferenceManager.SetFn(fn);
                    PreferenceManager.SetLn(ln);
                    PreferenceManager.SetAge(age);
                    PreferenceManager.SetAddress(add);
                    PreferenceManager.SetGender(gender);
                    uid = PreferenceManager.GetUID();
                    if (filePath.isEmpty()) {
                        final Query query = rootRef.child("user").orderByChild("uid").equalTo(PreferenceManager.GetUID());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                pb.setVisibility(View.GONE);
                                if (snapshot.exists()) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        child.getRef().child("fn").setValue(fn);
                                        child.getRef().child("ln").setValue(ln);
//                                        child.getRef().child("email").setValue(email);
                                        child.getRef().child("phone").setValue(phone);
                                        child.getRef().child("add").setValue(add);
                                        child.getRef().child("age").setValue(age);
                                        child.getRef().child("gender").setValue(gender);
                                        disableFields();
                                        Toast.makeText(getActivity(), "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else {
                        Query selectQuery = rootRef.child("user").orderByChild("uid").equalTo(PreferenceManager.GetUID());
                        selectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                pb.setVisibility(View.GONE);
                                if (dataSnapshot.exists()) {
                                    for (final DataSnapshot child : dataSnapshot.getChildren()) {
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
                                                        child.getRef().child("fn").setValue(fn);
                                                        child.getRef().child("ln").setValue(ln);
                                                        child.getRef().child("phone").setValue(phone);
                                                        child.getRef().child("add").setValue(add);
                                                        child.getRef().child("age").setValue(age);
                                                        child.getRef().child("gender").setValue(gender);
                                                        child.getRef().child("img").setValue(url.toString());
                                                        PreferenceManager.SetProfile(url.toString());
                                                        disableFields();
                                                        Toast.makeText(getActivity(), "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                        pb.setVisibility(View.GONE);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        pb.setVisibility(View.GONE);
                                                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                        pb.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                pb.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    private void disableFields() {
        fn_et.setEnabled(false);
        ln_et.setEnabled(false);
        phone_et.setEnabled(false);
        email_et.setEnabled(false);
        add_et.setEnabled(false);
        age_et.setEnabled(false);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            v.requestFocus();
            switch (v.getId()){
                case R.id.ivEditFirstName:
                    fn_et.setEnabled(true);
                    break;
                case R.id.ivEditLastName:
                    ln_et.setEnabled(true);
                    break;
                case R.id.ivEditPhone:
                    phone_et.setEnabled(true);
                    break;
                case R.id.ivEditEmail:
                    email_et.setEnabled(true);
                    break;
                case R.id.ivEditAddress:
                    add_et.setEnabled(true);
                    break;
                case R.id.ivEditAge:
                    age_et.setEnabled(true);
                    break;
                case R.id.tvChangePassword:
                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                    break;
                case R.id.llLogout:
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    Toast.makeText(getActivity(), "Logout Successfully!!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                    break;
            }
        }
    };

    private void dataload() {
        email_et.setText(PreferenceManager.GetEmail());
        fn_et.setText(PreferenceManager.GetFn());
        ln_et.setText(PreferenceManager.GetLn());
        age_et.setText(PreferenceManager.GetAge());
        add_et.setText(PreferenceManager.GetAddress());
        phone_et.setText(PreferenceManager.GetphoneNo());
        tvName.setText(PreferenceManager.GetFn()+"\n"+PreferenceManager.GetLn());
        Picasso.get().load(PreferenceManager.GetProfile()).into(ivProfilePic);

        if (PreferenceManager.GetGender().equalsIgnoreCase("male")) {
            r1.setChecked(true);
        } else if (PreferenceManager.GetGender().equalsIgnoreCase("female")) {
            r2.setChecked(true);
        } else if (PreferenceManager.GetGender().equalsIgnoreCase("other")) {
            r3.setChecked(true);
        } else {
            r1.setChecked(false);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            ivProfilePic.setImageBitmap(selectedImage);
        }
    }

}