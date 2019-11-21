package com.careme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.careme.Utils.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword, etConfNewPassword;
    private Button btnSubmit;
    private ProgressBar pbLoader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
        pbLoader                    = findViewById(R.id.pbLoader);
        etOldPassword               = findViewById(R.id.etOldPassword);
        etNewPassword               = findViewById(R.id.etNewPassword);
        etConfNewPassword           = findViewById(R.id.etConfNewPassword);
        btnSubmit                   = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPassword = etOldPassword.getText().toString().trim();
                final String newPassword = etNewPassword.getText().toString().trim();
                String confNewPassword = etConfNewPassword.getText().toString().trim();

                if(oldPassword.equalsIgnoreCase("")){
                    etOldPassword.setError("Required");
                }
                else if (oldPassword.length() < 6) {
                    etOldPassword.setError("minimum 6 character required");
                }
                else if (oldPassword.length() > 8) {
                    etOldPassword.setError("maximum 8 character required");
                }
                else if(newPassword.equalsIgnoreCase("")) {
                    etNewPassword.setError("Required");
                }
                else if (newPassword.length() < 6) {
                    etOldPassword.setError("minimum 6 character required");
                }
                else if (newPassword.length() > 8) {
                    etOldPassword.setError("maximum 8 character required");
                }
                else if(confNewPassword.equalsIgnoreCase("")){
                    etConfNewPassword.setError("Required");
                }
                else if(!confNewPassword.equalsIgnoreCase(newPassword)){
                    etConfNewPassword.setError("not same");
                }
                else {
//                    update password overher
                    pbLoader.setVisibility(View.VISIBLE);
                    DatabaseReference rootRef                     = FirebaseDatabase.getInstance().getReference();
                    Query selectQuery = rootRef.child("user").orderByChild("uid").equalTo(PreferenceManager.GetUID());
                    selectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            pbLoader.setVisibility(View.GONE);
                            if (snapshot.exists()) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    if(oldPassword.equalsIgnoreCase(child.child("pass").getValue().toString())){
                                        child.getRef().child("check").setValue(PreferenceManager.GetEmail() + "_" + newPassword);
                                        child.getRef().child("pass").setValue(""+newPassword);
                                        Toast.makeText(ChangePasswordActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    }
                                    else {
                                        Toast.makeText(ChangePasswordActivity.this, "Please enter valid old password!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            pbLoader.setVisibility(View.GONE);
                            Toast.makeText(ChangePasswordActivity.this, "Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private String getOldPassword(){
        String oldPassword = "";
        Query selectQuery = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("check");
        selectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("user_data", dataSnapshot.toString());
                if (dataSnapshot.exists()) {


                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String emailadd = datas.child("email").getValue().toString();
                        String pass = datas.child("pass").getValue().toString();
                        String uid = datas.child("uid").getValue().toString();
                        String fn = datas.child("fn").getValue().toString();
                        String ln = datas.child("ln").getValue().toString();
                        String age = datas.child("age").getValue().toString();
                        String add = datas.child("add").getValue().toString();
                        String phone = datas.child("phone").getValue().toString();
                        String gender = datas.child("gender").getValue().toString();
                        String profile = datas.child("img").getValue().toString();

                        PreferenceManager.SetphoneNo(phone);
                        PreferenceManager.SetEmail(emailadd);
                        PreferenceManager.SetPassword(pass);
                        PreferenceManager.SetFn(fn);
                        PreferenceManager.SetLn(ln);
                        PreferenceManager.SetAge(age);
                        PreferenceManager.SetAddress(add);
                        PreferenceManager.SetGender(gender);
                        PreferenceManager.SetUID(uid);
                        PreferenceManager.SetProfile(profile);
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return oldPassword;
    }
}
