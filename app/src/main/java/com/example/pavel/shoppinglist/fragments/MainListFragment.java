package com.example.pavel.shoppinglist.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.listeners.OnTitleChangeListener;


public class MainListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> lvAdapter;
    private MainActivity activity;
    private OnTitleChangeListener listener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
        listener = (OnTitleChangeListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        lvAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, new String[]{"Shopping lists", "Recipes", "Help"});

        View v = inflater.inflate(R.layout.fragment_navigation_main_list, null);
        ListView lvMain = (ListView) v.findViewById(R.id.fragment_main_list);
        lvMain.setAdapter(lvAdapter);
        lvMain.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:

                activity.replaceFragment(new ShoppingListsFragment(), R.id.main_activity_gl_fragment_content, false);


                break;

            case 1:

                activity.replaceFragment(new RecipeListFragment(), R.id.main_activity_gl_fragment_content, false);


                break;

            case 2:


                break;

            case 3:


                break;
        }


        listener.onTitleChange((String) parent.getAdapter().getItem(position));
        activity.closeDrawer();
    }
}
