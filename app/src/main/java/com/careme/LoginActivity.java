package com.careme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.careme.Utils.PreferenceManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText email_et, pass_et;
    String email, pass;
    ProgressBar pb;
    DatabaseReference rootRef;
    CheckBox cb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        email_et = findViewById(R.id.email_et);
        pass_et = findViewById(R.id.pass_et);
        pb = findViewById(R.id.pb);
        cb = findViewById(R.id.cb);

        FirebaseApp.initializeApp(this);
        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();

//        if (PreferenceManager.GetValid()) {
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            finish();
//        }

        cb.setChecked(PreferenceManager.GetValid());
        if (PreferenceManager.GetValid()) {

            email_et.setText(PreferenceManager.GetEmail());
            pass_et.setText(PreferenceManager.GetPassword());


        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    PreferenceManager.SetValid(true);
                } else {
                    PreferenceManager.SetValid(false);
                }

            }
        });
    }

    public void goforgot(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void goreg(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+");
    private boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public void login(View view) {

        email = email_et.getText().toString().trim();
        pass = pass_et.getText().toString().trim();


        if (email.isEmpty()) {
            email_et.setError("Required");
        }
        else if (!isValidEmail(email)) {
            email_et.setError("Not Valid");
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
        else {
            pb.setVisibility(View.VISIBLE);
//            UserItem userItem = new UserItem("","","","","","","","","");

            Query selectQuery = rootRef.child("user").orderByChild("check").equalTo(email + "_" + pass);
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

                        Toast.makeText(LoginActivity.this, "Login Suucess!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } else {

                        Toast.makeText(LoginActivity.this, "Wrong Email and Password!!!", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pb.setVisibility(View.GONE);
                }
            });
        }
    }
}
