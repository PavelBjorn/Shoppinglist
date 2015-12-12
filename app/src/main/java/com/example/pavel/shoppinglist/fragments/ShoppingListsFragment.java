package com.example.pavel.shoppinglist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.adapters.SavedListAdapter;
import com.example.pavel.shoppinglist.data.PreferencesManager;
import com.example.pavel.shoppinglist.listeners.OnSearchListener;
import com.example.pavel.shoppinglist.models.SavedListModel;

import org.json.JSONArray;
import org.json.JSONException;


public class ShoppingListsFragment extends Fragment implements View.OnClickListener, OnSearchListener {

    private ListView lvFiles;
    private SavedListAdapter savedListAdapter;
    private ImageButton imBtnCreate;
    private ArrayAdapter<String> spAdapter;
    private MainActivity activity;
    private String units;


    /*-fragment lifecycle*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
        this.activity.setDrawerEnable(true);

        Log.d("focus", "onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("focus", "onCreateView");

        activity.replaceFragment(new ActionBarFragmentLists(), R.id.ll_myActionBar_content, false);



        View v = inflater.inflate(R.layout.fragment_shoping_lists, null);

        /*View*/
        lvFiles = (ListView) v.findViewById(R.id.activity_saved_lv_names);
        imBtnCreate = (ImageButton) v.findViewById(R.id.activity_saved_imdtn_create);

        lvFiles.setOnScrollListener(new AbsListView.OnScrollListener() {

                                        int firstVisibleItem;

                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                                            if ((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) && (imBtnCreate.getVisibility() == View.VISIBLE)) {
                                                imBtnCreate.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_create_listt_c_anim));
                                                imBtnCreate.setVisibility(View.INVISIBLE);
                                            } else if ((firstVisibleItem == 0) && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) && (imBtnCreate.getVisibility() == View.INVISIBLE)) {
                                                imBtnCreate.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_create_listt_anim));
                                                imBtnCreate.setVisibility(View.VISIBLE);
                                            }

                                        }

                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            this.firstVisibleItem = firstVisibleItem;
                                        }
                                    }
        );

        /*Адаптеры*/
        savedListAdapter = new SavedListAdapter(activity);
        spAdapter = new ArrayAdapter<>(activity, R.layout.item_spiner, new String[]{"UAH", "USD", "EUR", "RUB"});
        savedListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if(savedListAdapter.getCount()==0&&imBtnCreate.getVisibility()==View.INVISIBLE){
                    imBtnCreate.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_create_listt_anim));
                    imBtnCreate.setVisibility(View.VISIBLE);
                }
                super.onChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });
        lvFiles.setAdapter(savedListAdapter);
        imBtnCreate.setOnClickListener(this);

        loadLists();

        if(activity.getRecipeModel()!=null){
          showCreateDialog();
        }


        return v;
    }

    @Override
    public void onResume() {
        this.activity.setMenuResId(R.menu.menu_actvity_save_list);
        this.activity.setMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_clear_list:
                        showClearDialog();
                        break;
                }
                return true;
            }
        });
        this.activity.setOpenDrawerFlag(false);
        if (activity.isSearchActive()) {
            activity.delFragment();
        }

        this.activity.setSearchListener(this);

        super.onResume();
    }

    /*-implemented listeners*/
    @Override
    public void onClick(View v) {
        showCreateDialog();
    }

    private void loadLists() {
        activity.getSearchAdapter().clear();
        try {
            JSONArray jsonArray = PreferencesManager.loadObjectInJSONArray(activity, MainActivity.SHOPPING_LISTS_PREFERENCES_NAME);
            for (int i = 0; i < jsonArray.length(); i++) {
                SavedListModel slm = new SavedListModel();
                slm.convertFromJSONObject(jsonArray.getJSONObject(i));
                savedListAdapter.addList(slm);

                activity.addToSearchAdapter(savedListAdapter.getItem(i).getName());

            }
            activity.getSearchAdapter().notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d("myTag", "" + e);
        }
    }

    private void showClearDialog() {

        AlertDialog.Builder dialogClear = new AlertDialog.Builder(activity)
                .setMessage("Remove all lists?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < savedListAdapter.getCount(); i++) {
                            PreferencesManager.delFile(activity, savedListAdapter.getItem(i).getName());
                        }
                        savedListAdapter.clear();
                        activity.getSearchAdapter().clear();
                        PreferencesManager.delFile(activity, MainActivity.SHOPPING_LISTS_PREFERENCES_NAME);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        dialogClear.show();
    }

    private void showCreateDialog(){
        AlertDialog.Builder alertDialog;
        View vDialog = activity.getLayoutInflater().inflate(R.layout.item_dialog_create_list, null);
        final EditText edtListName = (EditText) vDialog.findViewById(R.id.dialog_create_list_name);
        final EditText edtBudget = (EditText) vDialog.findViewById(R.id.dialog_create_list_budget);
        final Spinner spUnits = (Spinner) vDialog.findViewById(R.id.dialog_create_list_sp_units);
        final CheckBox rbBudget = (CheckBox) vDialog.findViewById(R.id.dialog_create_rb_budget);

        if(activity.getRecipeModel()!=null){
            edtListName.setText(activity.getRecipeModel().getName());
        }

        rbBudget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    edtBudget.setEnabled(true);
                } else {
                    edtBudget.setEnabled(false);
                }
            }
        });

        spUnits.setAdapter(spAdapter);
        spUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                units = (String) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialog = new AlertDialog.Builder(activity).setTitle("New list");
        alertDialog.setView(vDialog)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!edtListName.getText().toString().isEmpty()) {
                            long date = System.currentTimeMillis();
                            SavedListModel slm = new SavedListModel(edtListName.getText().toString()
                                    , Float.parseFloat(edtBudget.getText().toString())
                                    , units
                                    , date);
                            savedListAdapter.addList(slm);
                            activity.addToSearchAdapter(edtListName.getText().toString());
                            activity.getSearchAdapter().notifyDataSetChanged();
                            if (edtBudget.isEnabled()) {
                                slm.setHaveBudget(true);
                            } else {
                                slm.setHaveBudget(false);
                            }

                            PreferencesManager.saveData(activity,
                                    MainActivity.SHOPPING_LISTS_PREFERENCES_NAME,
                                    MainActivity.toJSONArray(savedListAdapter.getModels()));
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        alertDialog.show();

    }


    @Override
    public void onSearch(String name) {

        for(int i = 0;i<savedListAdapter.getCount();i++){
            if (savedListAdapter.getItem(i).getName().trim().equals(name.trim())){
                savedListAdapter.setFound(true);
                savedListAdapter.setFoundPosition(i);
                return;
            }
        }

        savedListAdapter.setFound(false);

    }
}
