package com.careme;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.careme.Model.FeedbackDo;
import com.careme.Model.GetPostItem;
import com.careme.Model.GetReviewDo;
import com.careme.Model.ReviewDo;
import com.careme.Utils.PreferenceManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeedbackActivity extends AppCompatActivity {

    private Button btnSubmit;
    private TextView tvName;
    private RatingBar rbRating;
    private EditText etReview;
    private RecyclerView rvReviewList;
    private DatabaseReference rootRef;
    private String postId = "", name = "", email = "";
    private static final String review_table = "review";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        FirebaseApp.initializeApp(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rootRef       = FirebaseDatabase.getInstance().getReference();
        postId        = getIntent().getExtras().getString("postId");
        name          = PreferenceManager.GetFn()+" "+PreferenceManager.GetLn();//getIntent().getExtras().getString("name");
        email         = PreferenceManager.GetEmail();//getIntent().getExtras().getString("email");
        tvName        = findViewById(R.id.tvName);
        rvReviewList  = findViewById(R.id.rvReviewList);
        etReview      = findViewById(R.id.etReview);
        btnSubmit     = findViewById(R.id.btnSubmit);
        rbRating      = findViewById(R.id.rbRating);
        rvReviewList.setLayoutManager(new LinearLayoutManager(FeedbackActivity.this));// 1, false));
        tvName.setText(PreferenceManager.GetFn()+" "+PreferenceManager.GetLn());

        loadReviews(postId);
//        Query selectQuery1 = rootRef.child("review").orderByChild("postId").equalTo(postId);
//        selectQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
//                        String pId = datas.child("postId").getValue().toString();
//                        String rating = datas.child("rating").getValue().toString();
//                        String review = datas.child("review").getValue().toString();
//                        if (postId.equalsIgnoreCase(pId)) {
//                            if(rating.length()>0){
//                                rbRating.setRating(Float.parseFloat(rating));
//                            }
//                            else {
//                                rbRating.setRating(0);
//                            }
//                            etReview.setText(review);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbRating.getRating() < 1){
                    Toast.makeText(FeedbackActivity.this, "Please give us rating", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitFeedback();
                }
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

    private void submitFeedback(){
        String rootKey = rootRef.push().getKey();
        ReviewDo reviewDo = new ReviewDo(postId, name, email, etReview.getText().toString().trim(), rbRating.getRating());
        rootRef.child(review_table).child(rootKey).setValue(reviewDo);
        Toast.makeText(this, "Thankyou for your valuable feedback", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private ArrayList<FeedbackDo> reviewDos = new ArrayList<>();
    private void loadReviews(String postId){
        Query selectQuery1 = rootRef.child(review_table).orderByChild("postId").equalTo(postId);
        selectQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float review = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FeedbackDo feedbackDo = snapshot.getValue(FeedbackDo.class);
                        review = review + feedbackDo.getRating();
                        reviewDos.add(feedbackDo);
                        if(feedbackDo.email.equalsIgnoreCase(PreferenceManager.GetEmail())){
                            rbRating.setEnabled(false);
                            rbRating.setRating(feedbackDo.getRating());
                            etReview.setVisibility(View.GONE);
                            btnSubmit.setVisibility(View.GONE);
                        }
                    }
                }
                if(reviewDos.size() > 0){
                    rvReviewList.setAdapter(new ReviewsAdapter(FeedbackActivity.this, reviewDos));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private class ReviewsAdapter extends RecyclerView.Adapter<ReviewHolder>{

        private ArrayList<FeedbackDo> reviewDos;
        private Context context;
        private ReviewsAdapter(Context context, ArrayList<FeedbackDo> reviewDos){
            this.context = context;
            this.reviewDos = reviewDos;
        }
        @NonNull
        @Override
        public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View convertView = LayoutInflater.from(context).inflate(R.layout.review_item_cell, parent, false);
            return new ReviewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
            FeedbackDo reviewDo = reviewDos.get(position);
            holder.rbRating.setRating(reviewDo.getRating());
            holder.tvName.setText("Name : "+reviewDo.getName());
            holder.tvEmail.setText("Email : "+reviewDo.getEmail());
            holder.tvReviewComments.setText("Review : "+reviewDo.getReview());
        }

        @Override
        public int getItemCount() {
            return reviewDos.size();
        }
    }

    private class ReviewHolder extends RecyclerView.ViewHolder{

        private TextView tvName, tvEmail, tvReviewComments;
        private RatingBar rbRating;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            tvName                      = itemView.findViewById(R.id.tvName);
            tvEmail                     = itemView.findViewById(R.id.tvEmail);
            tvReviewComments            = itemView.findViewById(R.id.tvReviewComments);
            rbRating                    = itemView.findViewById(R.id.rbRating);
        }
    }

}
