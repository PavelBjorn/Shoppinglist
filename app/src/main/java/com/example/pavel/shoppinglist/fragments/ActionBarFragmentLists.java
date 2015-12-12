package com.example.pavel.shoppinglist.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.pavel.shoppinglist.R;


public class ActionBarFragmentLists extends Fragment {

    TextView tvTitle;


    public void setTitle(String title){
        this.tvTitle.setText(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_action_bar_lists,null);

         tvTitle = (TextView) v.findViewById(R.id.fragment_ab_tv_title);


        return v;
    }
}
