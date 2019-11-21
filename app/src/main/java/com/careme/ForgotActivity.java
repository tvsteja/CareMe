package com.careme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ForgotActivity extends AppCompatActivity {

    EditText email_et;
    String email;
    String uid;
    ProgressBar pb;
    DatabaseReference rootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email_et = findViewById(R.id.email_et);
        pb = findViewById(R.id.pb);


        FirebaseApp.initializeApp(this);
        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
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

    public void goreg(View view) {
        startActivity(new Intent(ForgotActivity.this, RegistrationActivity.class));
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void login(View view) {

        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
//        startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
    }

    public void forgot(View view) {
        email = email_et.getText().toString().trim();
        if (email.isEmpty()) {
            email_et.setError("Required");
        }
        if (!isValidEmail(email)) {
            email_et.setError("Not Valid");
        }
        else {
            pb.setVisibility(View.VISIBLE);
            Query selectQuery = rootRef.child("user").orderByChild("email").equalTo(email);
            selectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pb.setVisibility(View.GONE);
                    Log.e("user_data", dataSnapshot.toString());
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            String emailadd = datas.child("email").getValue().toString();
                            String pass = datas.child("pass").getValue().toString();
                            GMailSender.withAccount("caremeapp40@gmail.com", "Test@123")
                                    .withTitle("Forgot Password")
                                    .withBody("Your Password is: " + pass)
                                    .withSender(emailadd)
                                    .toEmailAddress(emailadd) // one or multiple addresses separated by a comma
                                    .withListenner(new GmailListener() {
                                        @Override
                                        public void sendSuccess() {
                                            Toast.makeText(ForgotActivity.this, "Success check your Email", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void sendFail(String err) {
                                            Toast.makeText(ForgotActivity.this, "Fail: " + err, Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .send();

                        }
//                        startActivity(new Intent(UserLoginActvity.this, FirstSettingActivity.class));
//                        finish();
                    } else {
                        Toast.makeText(ForgotActivity.this, "Wrong Email!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pb.setVisibility(View.GONE);
                }
            });

        }
    }
    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+");
    private boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    String replaceString(String string) {
        return string.replaceAll("[@.-_;\\/:*?\"<>|&']", "");
    }
}
