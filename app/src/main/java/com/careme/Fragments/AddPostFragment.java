package com.careme.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.careme.AddpostActivity;
import com.careme.AddpostActivityParent;
import com.careme.R;

public class AddPostFragment extends Fragment {

    private ImageView postbtn;
    private Button btnp, btnc;

    public AddPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view       = inflater.inflate(R.layout.addpost_fragment, container, false);
        postbtn         = view.findViewById(R.id.postbtn);
        btnp            = view.findViewById(R.id.btnp);
        btnc            = view.findViewById(R.id.btnc);

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), AddpostActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        btnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), AddpostActivityParent.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), AddpostActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        return view;
    }

}