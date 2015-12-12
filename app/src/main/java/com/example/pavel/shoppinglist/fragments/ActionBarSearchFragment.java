package com.example.pavel.shoppinglist.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.listeners.OnSearchListener;


public class ActionBarSearchFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private MainActivity activity;
    private ImageButton imbtnCloseSearch;
    private AutoCompleteTextView tvSearch;
    private static final String TAG = "searchFragTag";


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_action_bar_search, null);
        imbtnCloseSearch = (ImageButton) v.findViewById(R.id.fragment_search_imbtn_close);
        imbtnCloseSearch.setOnClickListener(this);
        tvSearch = (AutoCompleteTextView) v.findViewById(R.id.fragment_search_atv);

        tvSearch.setAdapter(activity.getSearchAdapter());
        tvSearch.addTextChangedListener(this);
        return v;
    }

    @Override
    public void onDestroy() {
        activity.setSearchName("");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_search_imbtn_close:
                this.activity.delFragment();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            activity.setSearchName(s.toString());
        }catch(NullPointerException e){
            Log.d(TAG,""+e);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
