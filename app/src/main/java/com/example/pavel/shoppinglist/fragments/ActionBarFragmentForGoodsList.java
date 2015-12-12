package com.example.pavel.shoppinglist.fragments;


import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;



public class ActionBarFragmentForGoodsList extends Fragment {

    /*View*/
    private TextView tvListName, tvBudget, tvCount;

    /*Activity*/
    private MainActivity activity;


    /*Methods*/
    public void setBudgetTitle(String budget){
        tvBudget.setText(budget);
    }

    public void setCountTitle(String count){
        tvCount.setText(count);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = (MainActivity) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_action_bar_for_shoping_list,null);

        tvListName = (TextView) v.findViewById(R.id.tv_title);
        tvBudget = (TextView) v.findViewById(R.id.tv_budget);
        tvCount = (TextView) v.findViewById(R.id.tv_count_goods);

        tvListName.setText(activity.getListName());

        String budget;

        if(activity.haveBudget()) {
            budget =String.format("%.2f", activity.getBudget()) + " " + activity.getBudgetUnits();
        }else {
            budget="No budget";
        }

        tvBudget.setText(budget);
        tvCount.setText(activity.getGoodsCount().replace("_","/"));

        return v;
    }

}
