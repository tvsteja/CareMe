package com.careme.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.careme.JobDetailsActivity;
import com.careme.Model.GetPostItem;
import com.careme.R;
import com.careme.Utils.PreferenceManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


@Keep
public class FavAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public Context context;
    DatabaseReference rootRef;
    private ArrayList<GetPostItem> arrayList;
    private ArrayList<GetPostItem> temparrayList;

    public FavAdapter(Context context, ArrayList<GetPostItem> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        temparrayList = new ArrayList<GetPostItem>();
        temparrayList.addAll(arrayList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_post_item, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pb_item, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            final GetPostItem album = arrayList.get(position);
            final String image = album.getHl();
            final String rate = album.getHr();
            final String time = album.getDate();
            final String add = album.getAdd();

            if(album.getType().equalsIgnoreCase("parent")){
                myViewHolder.llPostCellBg.setBackgroundColor(context.getResources().getColor(R.color.parent_post_bg));
            }
            else {
                myViewHolder.llPostCellBg.setBackgroundColor(context.getResources().getColor(R.color.caregiver_post_bg));
            }
//        holder.mImageView.getLayoutParams().height = getRandomIntInRange(Integer.parseInt(album.getImages().getOrig().getHeight()), Integer.parseInt(album.getImages().getlowImage().getHeight()));
//        holder.mImageView.getLayoutParams().height = getRandomIntInRange(450, 350);
//            Glide.with(context)
//                    .load(image)
//                    .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
//                    .into(myViewHolder.mImageView);

            myViewHolder.tv_title.setText(image);
            myViewHolder.tv_rate.setText("Hourly Rate: $" + rate);
            myViewHolder.tv_time.setText(time);
            myViewHolder.tv_add.setText("Address : "+add);
            
            myViewHolder.cv_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, JobDetailsActivity.class);
                    intent.putExtra("t", album.getHl());
                    intent.putExtra("r", album.getHr());
                    intent.putExtra("img", album.getImg());
                    intent.putExtra("dt", album.getDate());
                    intent.putExtra("time", album.getTime());
                    intent.putExtra("exp", album.getExp());
                    intent.putExtra("add", album.getAdd());
                    intent.putExtra("des", album.getDesc());
                    intent.putExtra("type", album.getType());
                    intent.putExtra("phone", album.getPhone());
                    intent.putExtra("pid", album.getPid());
                    intent.putExtra("postBy", album.getPostBy());
                    intent.putExtra("email", album.getEmail());
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseApp.initializeApp(context);
                    //database reference pointing to root of database
                    rootRef = FirebaseDatabase.getInstance().getReference();

                    Query selectQuery = rootRef.child("fav").orderByChild("pid").equalTo(album.getPid());
                    selectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                    String uid = datas.child("uid").getValue().toString();
                                    if (uid.equalsIgnoreCase(PreferenceManager.GetUID())) {
                                        datas.getRef().removeValue();
                                        Toast.makeText(context, "Remove success!", Toast.LENGTH_SHORT).show();
                                        arrayList.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, "Try Again!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
        else {
            if (getItemCount() == 1) {
            }
        }
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }


    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(temparrayList);

        } else {
            for (GetPostItem postDetail : temparrayList) {
                if (charText.length() > 0 && (postDetail.getHl().toLowerCase(Locale.getDefault()).contains(charText)
                       // || postDetail.getHr().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getDate().toLowerCase(Locale.getDefault()).contains(charText)
                        || postDetail.getAdd().toLowerCase(Locale.getDefault()).contains(charText))) {
                    arrayList.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {


        private ProgressViewHolder(View v) {
            super(v);

        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cv_text;
        TextView tv_title;
        TextView tv_rate;
        private ImageView ivDelete;
        TextView tv_time;
        TextView tv_add;
        private LinearLayout llPostCellBg;
        public MyViewHolder(View view) {
            super(view);
            cv_text                  = view.findViewById(R.id.cv_text);
            tv_title                 = view.findViewById(R.id.tv_title);
            tv_rate                  = view.findViewById(R.id.tv_rate);
            ivDelete                 = view.findViewById(R.id.ivDelete);
            tv_time                  = view.findViewById(R.id.tv_time);
            tv_add                   = view.findViewById(R.id.tv_add);
            llPostCellBg             = view.findViewById(R.id.llPostCellBg);



        }
    }
}


