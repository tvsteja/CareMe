package com.careme.Fragments;

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

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.careme.Adapter.FavAdapter;
import com.careme.Adapter.HisAdapter;
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

public class FavFragment extends Fragment {

    private SearchView et;
    private TextView tvTitle, tvParentPosts, tvCaregiverPosts;
    private ImageView ivFilter;
    private RecyclerView rvPostList;
    private DatabaseReference rootRef;
    private FavAdapter favAdapter;
    private ProgressBar pb;
    private ArrayList<GetPostItem> parentPostList = new ArrayList<>();
    private ArrayList<GetPostItem> caregiverPostList = new ArrayList<>();
    private ArrayList<GetPostItem> previousParentPostList = new ArrayList<>();
    private ArrayList<GetPostItem> previousCaregiverPostList = new ArrayList<>();

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        tvTitle          = view.findViewById(R.id.tvTitle);
        tvParentPosts    = view.findViewById(R.id.tvParentPosts);
        tvCaregiverPosts = view.findViewById(R.id.tvCaregiverPosts);
        ivFilter         = view.findViewById(R.id.ivFilter);
        rvPostList       = view.findViewById(R.id.rvPostList);
        et = view.findViewById(R.id.et);
        pb = view.findViewById(R.id.pb);
        rvPostList.setNestedScrollingEnabled(false);
        rvPostList.setHasFixedSize(true);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return favAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        rvPostList.setLayoutManager(gridLayoutManager);
        rootRef = FirebaseDatabase.getInstance().getReference();
        pb.setVisibility(View.VISIBLE);
        et.setActivated(true);
        et.setQueryHint("Search here");
        et.onActionViewExpanded();
        et.setIconified(false);
        et.clearFocus();
        tvTitle.setVisibility(View.GONE);
        et.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                favAdapter.filter(newText);
                favAdapter.notifyDataSetChanged();
                return false;
            }
        });



        Query query = rootRef.child("fav").orderByChild("uid").equalTo(PreferenceManager.GetUID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pb.setVisibility(View.GONE);
                parentPostList.clear();
                caregiverPostList.clear();
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    long value = dataSnapshot.getChildrenCount();
                    Log.d("18888", "no of children: " + value);
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
                    favAdapter = new FavAdapter(getActivity(), parentPostList);
                    rvPostList.setAdapter(favAdapter);
                    tvParentPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    tvParentPosts.setTextColor(getResources().getColor(android.R.color.white));
                    tvCaregiverPosts.setBackgroundResource(R.drawable.post_tab_bg);
                    tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.black));
                    Log.d("18888", "no of Parent Posts : " + parentPostList.size()+" no of caregiver Posts : "+caregiverPostList.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pb.setVisibility(View.GONE);

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
                favAdapter = new FavAdapter(getActivity(), parentPostList);
                rvPostList.setAdapter(favAdapter);
                tvParentPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvParentPosts.setTextColor(getResources().getColor(android.R.color.white));
                tvCaregiverPosts.setBackgroundResource(R.drawable.post_tab_bg);
                tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.black));
            }
        });
        tvCaregiverPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favAdapter = new FavAdapter(getActivity(), caregiverPostList);
                rvPostList.setAdapter(favAdapter);
                tvCaregiverPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.white));
                tvParentPosts.setBackgroundResource(R.drawable.post_tab_bg);
                tvParentPosts.setTextColor(getResources().getColor(android.R.color.black));
            }
        });

        return view;
    }

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
        favAdapter = new FavAdapter(getActivity(), parentPostList);
        rvPostList.setAdapter(favAdapter);
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
        favAdapter = new FavAdapter(getActivity(), parentPostList);
        rvPostList.setAdapter(favAdapter);
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
        favAdapter = new FavAdapter(getActivity(), caregiverPostList);
        rvPostList.setAdapter(favAdapter);
        tvCaregiverPosts.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvCaregiverPosts.setTextColor(getResources().getColor(android.R.color.white));
        tvParentPosts.setBackgroundResource(R.drawable.post_tab_bg);
        tvParentPosts.setTextColor(getResources().getColor(android.R.color.black));
    }

}