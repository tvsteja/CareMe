package com.careme.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.careme.Adapter.PostAdapter;
import com.careme.Model.GetPostItem;
import com.careme.R;
import com.careme.Utils.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView rvPostList;
    private TextView tvParentPosts, tvCaregiverPosts;
    private ImageView ivFilter;
    private DatabaseReference databaseReference;
    private String Database_Path = "UserPost";
    private PostAdapter postAdapter;
    private ArrayList<GetPostItem> parentPostList = new ArrayList<>();
    private ArrayList<GetPostItem> caregiverPostList = new ArrayList<>();
    private ArrayList<GetPostItem> previousParentPostList = new ArrayList<>();
    private ArrayList<GetPostItem> previousCaregiverPostList = new ArrayList<>();
    private ProgressBar pb;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view        = inflater.inflate(R.layout.home_fragment, container, false);
        tvParentPosts    = view.findViewById(R.id.tvParentPosts);
        tvCaregiverPosts = view.findViewById(R.id.tvCaregiverPosts);
        rvPostList       = view.findViewById(R.id.rvPostList);
        searchView       = view.findViewById(R.id.et);
        ivFilter         = view.findViewById(R.id.ivFilter);
        pb               = view.findViewById(R.id.pb);
        rvPostList.setNestedScrollingEnabled(false);
        rvPostList.setHasFixedSize(true);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return postAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        rvPostList.setLayoutManager(gridLayoutManager);
        pb.setVisibility(View.VISIBLE);
        searchView.setActivated(true);
        searchView.setQueryHint("Search here");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                adapter.getFilter().filter(newText);
                postAdapter.filter(newText);
                postAdapter.notifyDataSetChanged();

                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query selectQuery = databaseReference.child("userpost");//.orderByChild("uid").equalTo(PreferenceManager.GetUID());
        selectQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value = dataSnapshot.getChildrenCount();
                parentPostList.clear();
                caregiverPostList.clear();
                Log.d("18888", "no of children: " + value);
                pb.setVisibility(View.GONE);
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    GetPostItem tD = d.getValue(GetPostItem.class);
                    if(tD.getType().equalsIgnoreCase("parent")){
                        parentPostList.add(tD);
                    }
                    else {
                        caregiverPostList.add(tD);
                    }
                }
                Log.d("18888", "no of Parent Posts : " + parentPostList.size()+" no of caregiver Posts : "+caregiverPostList.size());
                Collections.reverse(parentPostList);
                Collections.reverse(caregiverPostList);
                previousParentPostList.addAll(parentPostList);
                previousCaregiverPostList.addAll(caregiverPostList);
                postAdapter = new PostAdapter(getActivity(), parentPostList);
                rvPostList.setAdapter(postAdapter);
                tvParentPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvParentPosts.setTextColor(getResources().getColor(android.R.color.white));
                tvCaregiverPosts.setBackgroundResource(R.drawable.post_tab_bg);
                tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.black));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                pb.setVisibility(View.GONE);
                // Failed to read value
                Log.w("18888", "Failed to read value.", error.toException());
            }
        });

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu(v);
            }
        });

        tvParentPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAdapter = new PostAdapter(getActivity(), parentPostList);
                rvPostList.setAdapter(postAdapter);
                tvParentPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvParentPosts.setTextColor(getResources().getColor(android.R.color.white));
                tvCaregiverPosts.setBackgroundResource(R.drawable.post_tab_bg);
                tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.black));
            }
        });
        tvCaregiverPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAdapter = new PostAdapter(getActivity(), caregiverPostList);
                rvPostList.setAdapter(postAdapter);
                tvCaregiverPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.white));
                tvParentPosts.setBackgroundResource(R.drawable.post_tab_bg);
                tvParentPosts.setTextColor(getResources().getColor(android.R.color.black));
            }
        });
        return view;
    }
//  for filters
    private void showFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.posts_filter_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.tvExpLessFive:
                        filterPostsByExperience(0,5);
                        return true;
                    case R.id.tvExpLessTen:
                        filterPostsByExperience(6,10);
                        return true;
                    case R.id.tvExpLessTwenty:
                        filterPostsByExperience(11,20);
                        return true;
                    case R.id.tvPriceLessTwenty:
                        filterPostsByPrice(0,20);
                        return true;
                    case R.id.tvPriceLessFifty:
                        filterPostsByPrice(21,50);
                        return true;
                    case R.id.tvPriceLessHundred:
                        filterPostsByPrice(51,100);
                        return true;
                    case R.id.tvClearFilter:
                        clearFilter();
                        return true;
                    default:
                        return false;
                }
            }
        });
//        popup.inflate(R.menu.posts_filter_menu);
        popup.show();
    }

    private void clearFilter(){
        parentPostList = previousParentPostList;
        caregiverPostList = previousCaregiverPostList;
        postAdapter = new PostAdapter(getActivity(), parentPostList);
        rvPostList.setAdapter(postAdapter);
        tvParentPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvParentPosts.setTextColor(getResources().getColor(android.R.color.white));
        tvCaregiverPosts.setBackgroundResource(R.drawable.post_tab_bg);
        tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void filterPostsByPrice(int toprice,int fromprice ){
        ArrayList<GetPostItem> filteredParentPostList = new ArrayList<>();
        ArrayList<GetPostItem> filteredCaregiverPostList = new ArrayList<>();
        if (previousCaregiverPostList != null && previousCaregiverPostList.size() > 0) {
            for (int i = 0; i < previousCaregiverPostList.size(); i++) {
                if (previousCaregiverPostList.get(i).getHr().length() > 0) {
                    int pri = Integer.parseInt(previousCaregiverPostList.get(i).getHr());
                    if (pri >= toprice && pri<=fromprice) {
                        filteredCaregiverPostList.add(previousCaregiverPostList.get(i));
                    }
                }
            }

        }
        if (previousParentPostList != null && previousParentPostList.size() > 0) {
            for (int i = 0; i < previousParentPostList.size(); i++) {
                if (previousParentPostList.get(i).getHr().length() > 0) {
                    int pri = Integer.parseInt(previousParentPostList.get(i).getHr());
                    if (pri >= toprice && pri<=fromprice) {
                        filteredParentPostList.add(previousParentPostList.get(i));
                    }
                }
            }
        }
        parentPostList = filteredParentPostList;
        caregiverPostList = filteredCaregiverPostList;
        postAdapter = new PostAdapter(getActivity(), parentPostList);
        rvPostList.setAdapter(postAdapter);
        tvParentPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvParentPosts.setTextColor(getResources().getColor(android.R.color.white));
        tvCaregiverPosts.setBackgroundResource(R.drawable.post_tab_bg);
        tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void filterPostsByExperience(int fromexp , int toexp) {
        ArrayList<GetPostItem> filteredParentPostList = new ArrayList<>();
        ArrayList<GetPostItem> filteredCaregiverPostList = new ArrayList<>();
        if (previousCaregiverPostList != null && previousCaregiverPostList.size() > 0) {
            for (int i = 0; i < previousCaregiverPostList.size(); i++) {
                if (previousCaregiverPostList.get(i).getExp().length() > 0) {
                    int exp = Integer.parseInt(previousCaregiverPostList.get(i).getExp());
                    if (exp>= fromexp && exp <=toexp) {
                        filteredCaregiverPostList.add(previousCaregiverPostList.get(i));
                    }
                }
            }

        }
        parentPostList = filteredParentPostList;
        caregiverPostList = filteredCaregiverPostList;
        postAdapter = new PostAdapter(getActivity(), caregiverPostList);
        rvPostList.setAdapter(postAdapter);
        tvCaregiverPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.white));
        tvParentPosts.setBackgroundResource(R.drawable.post_tab_bg);
        tvParentPosts.setTextColor(getResources().getColor(android.R.color.black));
    }

}