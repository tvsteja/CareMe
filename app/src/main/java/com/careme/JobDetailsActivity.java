package com.careme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.careme.Model.FeedbackDo;
import com.careme.Model.GetReviewDo;
import com.careme.Model.PostItem;
import com.careme.Model.ReviewDo;
import com.careme.Utils.PreferenceManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class JobDetailsActivity extends AddpostActivity {

    private TextView tvName, tvEmail, title, desc, tvDate, tvTime, tvExp, rate, add, tv_type, tv_call;
    private ImageView ivProfilePic,ivFav;
    private String t = "", r = "", des = "", time = "", exp = "", dt = "", img = "", ad = "", type = "", phone = "", pid = "", postBy = "", email = "";
    private LinearLayout ll_text_share, ll_text_call, sharejob;
    private DatabaseReference rootRef;
    private RatingBar rbRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobdetails_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseApp.initializeApp(this);
        rootRef          = FirebaseDatabase.getInstance().getReference();
        tvName           = findViewById(R.id.tvName);
        tvEmail          = findViewById(R.id.tvEmail);
        title            = findViewById(R.id.tv_title);
        rate             = findViewById(R.id.tv_rate);
        tvDate           = findViewById(R.id.tv_date);
        tvTime           = findViewById(R.id.tvTime);
        tvExp            = findViewById(R.id.tvExp);
        desc             = findViewById(R.id.tv_desc);
        add              = findViewById(R.id.tv_add);
        ivProfilePic     = findViewById(R.id.ivProfilePic);
        ivFav            = findViewById(R.id.ivFav);
        ll_text_share    = findViewById(R.id.ll_text_share);
        ll_text_call     = findViewById(R.id.ll_text_call);
        tv_type          = findViewById(R.id.tv_type);
        tv_call          = findViewById(R.id.tv_call);
        sharejob         = findViewById(R.id.share_ll);
        rbRating         = (RatingBar) findViewById(R.id.rbRating);
        ImageView ivRating =  findViewById(R.id.ivRating);
//        simpleRatingBar.setNumStars(5);
//        simpleRatingBar.setRating(5f);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            t = bundle.getString("t");
            r = bundle.getString("r");
            img = bundle.getString("img");
            dt = bundle.getString("dt");
            time = bundle.getString("time");
            ad = bundle.getString("add");
            des = bundle.getString("des");
            type = bundle.getString("type");
            phone = bundle.getString("phone");
            pid = bundle.getString("pid");
            postBy = bundle.getString("postBy");
            email = bundle.getString("email");
            try {
                exp = bundle.getString("exp");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        title.setText(""+t);
        rate.setText("$" + r);
        tvDate.setText("" + dt);
        tvTime.setText("" + time);
        desc.setText("" + des);
        add.setText("" + ad);
        tv_type.setText("" + type);
        tv_call.setText("" + phone);
        tvName.setText(postBy);
        tvExp.setText(""+exp);
        tvEmail.setText(email);
        if(!img.equalsIgnoreCase("")){
            Picasso.get().load(img).placeholder(R.drawable.logo).into(ivProfilePic);
        }
        ivRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobDetailsActivity.this, FeedbackActivity.class);
                intent.putExtra("postId", pid);
                intent.putExtra("name", postBy);
                intent.putExtra("email", email);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        Query selectQuery = rootRef.child("fav").orderByChild("pid").equalTo(pid);
        selectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String uid = datas.child("uid").getValue().toString();
                        if (uid.equalsIgnoreCase(PreferenceManager.GetUID())) {
                            ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        }
                    }
                }
                else {
                    ivFav.setImageResource(R.drawable.ic_black_heart);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        loadReviews(pid);
//        Query selectQuery1 = rootRef.child("review").orderByChild("postId").equalTo(pid);
//        selectQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
//                        String postId = datas.child("postId").getValue().toString();
//                        String rating = datas.child("rating").getValue().toString();
//                        if (pid.equalsIgnoreCase(postId)) {
//                            if(rating.length()>0){
//                                rbRating.setRating(Float.parseFloat(rating));
//                            }
//                        }
//                    }
//                }
//                else {
//                    rbRating.setRating(0);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        ll_text_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query selectQuery = rootRef.child("fav").orderByChild("pid").equalTo(pid);
                selectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                String uid = datas.child("uid").getValue().toString();
                                if (uid.equalsIgnoreCase(PreferenceManager.GetUID())) {
                                    Toast.makeText(JobDetailsActivity.this, "Already Favourite Added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else {
                            String ImageUploadId = rootRef.push().getKey();
                            PostItem postItem = new PostItem(PreferenceManager.GetUID(), t, ad, r, dt, dt, exp, des, img, type, phone, pid, postBy, email, "");
                            // Getting image upload ID.
                            // Adding image upload id s child element into databaseReference.
                            rootRef.child("fav").child(ImageUploadId).setValue(postItem);
                            ivFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(JobDetailsActivity.this, "Add Favourite Success!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pb.setVisibility(View.GONE);
                    }
                });
            }
        });

        ll_text_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sharejob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "Job Details: " + "\n" + t + "\n" + r + "\n" + ad + "\n" + dt + "\n" + des + "\n" + type + "\n" + phone;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        if(type.equalsIgnoreCase("Parent")){
        tvExp.setVisibility(View.GONE);
    }
        else {

        tvExp.setVisibility(View.VISIBLE);
    }
    }

    private void loadReviews(String postId){
        Query selectQuery1 = rootRef.child("review").orderByChild("postId").equalTo(postId);
        selectQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float review = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FeedbackDo feedbackDo = snapshot.getValue(FeedbackDo.class);
                        review = review + feedbackDo.getRating();
                    }
                rbRating.setRating(review/dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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

    String replaceString(String string) {
        return string.replaceAll("[@.-_;\\/:*?\"<>|&']", "");
    }
}
