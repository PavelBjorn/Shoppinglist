package com.example.pavel.shoppinglist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.pavel.shoppinglist.AddItemActivity;
import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.adapters.GoodsAdapter;
import com.example.pavel.shoppinglist.comparators.ComparatorForChecked;
import com.example.pavel.shoppinglist.comparators.ComparatorForDate;
import com.example.pavel.shoppinglist.comparators.ComparatorForName;
import com.example.pavel.shoppinglist.data.PreferencesManager;
import com.example.pavel.shoppinglist.listeners.OnGoodsCountChangeListener;
import com.example.pavel.shoppinglist.listeners.OnSearchListener;
import com.example.pavel.shoppinglist.models.GoodsModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;


public class GoodsListFragment extends android.support.v4.app.Fragment implements View.OnClickListener, OnSearchListener {

    /*Views*/
    private ImageButton imBtnAdd, imBtnShoppingCardAdd, imBtnSend, imBtnClear;
    private GridView gvGoods;


    /*Activity*/
    private MainActivity activity;


    /*Adapters*/
    private GoodsAdapter goodsAdapter;

    /*Flags*/
    private boolean addBtnFlag = false;
    private int sortFlaPosition = 0;



    /*Methods*/
    public void removeBtn() {
        if (addBtnFlag) {
            imBtnShoppingCardAdd.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_add_one_produckt_c_anim));
            imBtnSend.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_send_shopping_list_c_anim));
            imBtnClear.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_clear_c_anim));

            imBtnShoppingCardAdd.setVisibility(View.INVISIBLE);
            imBtnSend.setVisibility(View.INVISIBLE);
            imBtnClear.setVisibility(View.INVISIBLE);
            imBtnAdd.setImageResource(R.drawable.btn_add_selector);
            addBtnFlag = false;

        }
    }

    private void loadGoodsModels(String loadJSON) {
        loadJSON.replace(" ", "_");
        activity.getSearchAdapter().clear();
        try {
            JSONArray jsonArray = new JSONArray(loadJSON.replace(" ", "_"));
            for (int i = 0; i < jsonArray.length(); i++) {

                GoodsModel gm = new GoodsModel();
                gm.convertFromJSONObject(jsonArray.getJSONObject(i));
                goodsAdapter.addGoodsModel(gm);
                activity.addToSearchAdapter(goodsAdapter.getItem(i).getName());
            }
            activity.getSearchAdapter().notifyDataSetChanged();
            goodsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d("myTag", "" + e);
        }
    }

    /*-fragment lifecycle*/
    @Override
    public void onAttach(Activity activity) {
        Log.d("goodsListFragment", "onAttach");
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
        this.activity.setMenuResId(R.menu.menu_goods_list);
        this.activity.setMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_sort:
                        showSortDialog();
                        break;

                }

                return false;
            }
        });


        this.activity.setDrawerEnable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("goodsListFragment", "onCreateView");

        addFromGoodsRecipe();

        activity.replaceFragment(new ActionBarFragmentForGoodsList(), R.id.ll_myActionBar_content, false, true);

        View v = inflater.inflate(R.layout.fragment_goods_list, null);

        goodsAdapter = new GoodsAdapter(activity);

        /*finding views*/
        imBtnAdd = (ImageButton) v.findViewById(R.id.imbtn_add);
        imBtnShoppingCardAdd = (ImageButton) v.findViewById(R.id.imbtn_shopping_card_add);
        imBtnSend = (ImageButton) v.findViewById(R.id.imbtn_send_list);
        imBtnClear = (ImageButton) v.findViewById(R.id.imbtn_clear);
        gvGoods = (GridView) v.findViewById(R.id.gv_goods);


        /*-set views stat*/
        imBtnAdd.setOnClickListener(this);
        imBtnShoppingCardAdd.setOnClickListener(this);
        imBtnShoppingCardAdd.setVisibility(View.INVISIBLE);
        imBtnSend.setOnClickListener(this);
        imBtnSend.setVisibility(View.INVISIBLE);
        imBtnClear.setOnClickListener(this);
        imBtnClear.setVisibility(View.INVISIBLE);
        gvGoods.setAdapter(goodsAdapter);
        goodsAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                PreferencesManager.saveData(activity, activity.getListName()
                        , MainActivity.toJSONArray(goodsAdapter.getCollection()));

            }
        });


        gvGoods.setOnScrollListener(new AbsListView.OnScrollListener() {

                                        int firstVisibleItem;

                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                                            removeBtn();
                                            if ((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) && (imBtnAdd.getVisibility() == View.VISIBLE)) {
                                                imBtnAdd.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_create_listt_c_anim));

                                                imBtnAdd.setVisibility(View.INVISIBLE);
                                            } else if ((firstVisibleItem == 0) && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) && (imBtnAdd.getVisibility() == View.INVISIBLE)) {
                                                imBtnAdd.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_create_listt_anim));
                                                imBtnAdd.setVisibility(View.VISIBLE);
                                            }

                                        }
                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            this.firstVisibleItem = firstVisibleItem;
                                        }
                                    }
        );

        loadGoodsModels(PreferencesManager.loadObject(activity, activity.getListName()));


        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("goodsListFragment", "onActivityResult");

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_FOR_PRODUCT && resultCode == MainActivity.RESULT_OK) {

            String comments = data.getStringExtra(MainActivity.COMMENTS_KEY);

            GoodsModel model = new GoodsModel(data.getStringExtra(MainActivity.PHOTO_ID_KEY)
                    , data.getStringExtra(MainActivity.ITEM_NAME_KEY)
                    , data.getStringExtra(MainActivity.QUANTITY_KEY)
                    , data.getStringExtra(MainActivity.UNITS_KEY)
                    , (comments.isEmpty() ? "noComments" : comments)
                    , data.getStringExtra(MainActivity.CATEGORY_NAME_KEY)
                    , System.currentTimeMillis());
            model.setPrice(data.getDoubleExtra(MainActivity.PRICE_KEY, 0.0));

            activity.addToSearchAdapter(data.getStringExtra(MainActivity.ITEM_NAME_KEY));

            goodsAdapter.addGoodsModel(model);
            goodsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        if (activity.isSearchActive()) {
            activity.delFragment();
        }
        this.activity.setOpenDrawerFlag(true);
        this.activity.setSearchListener(this);
        super.onResume();
    }

    /*- implemented listeners*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imbtn_add:

                if (!addBtnFlag) {

                    imBtnAdd.setImageResource(R.drawable.btn_add_cancel_selector);
                    imBtnShoppingCardAdd.setVisibility(View.VISIBLE);
                    imBtnShoppingCardAdd.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_add_one_produckt_anim));

                    imBtnSend.setVisibility(View.VISIBLE);
                    imBtnSend.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_send_shopping_list_anim));

                    imBtnClear.setVisibility(View.VISIBLE);
                    imBtnClear.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.btn_clear_anim));
                    addBtnFlag = true;


                } else {
                    removeBtn();
                }

                break;

            case R.id.imbtn_shopping_card_add:

                Intent intent = new Intent(activity, AddItemActivity.class);
                intent.putExtra(MainActivity.HAVE_BUDGET_KEY, activity.haveBudget());
                intent.putExtra(MainActivity.UNITS_KEY, activity.getBudgetUnits());

                startActivityForResult(intent, MainActivity.REQUEST_FOR_PRODUCT);

                break;

            case R.id.imbtn_send_list:


                break;

            case R.id.imbtn_clear:

                AlertDialog.Builder dialogClear = new AlertDialog.Builder(activity)
                        .setTitle("Clear")
                        .setMessage("Remove all goods?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < goodsAdapter.getCount(); i++) {
                                    if (goodsAdapter.getItem(i).isBought()) {
                                        activity.culcBudgetChange(goodsAdapter.getItem(i).getPrice(), 1);
                                    }
                                }

                                goodsAdapter.clear();
                                goodsAdapter.notifyDataSetChanged();
                                ((OnGoodsCountChangeListener) activity).countChange("0/0");

                            }
                        }).setNegativeButton("No", null);

                dialogClear.show();

                break;
        }

    }

    @Override
    public void onSearch(String name) {

        for (int i = 0; i < goodsAdapter.getCount(); i++) {
            if (goodsAdapter.getItem(i).getName().trim().equals(name.trim())) {
                goodsAdapter.setFound(true);
                goodsAdapter.setFoundPosition(i);
                goodsAdapter.notifyDataSetChanged();
                return;
            }
        }

        goodsAdapter.setFound(false);
    }

    private void showSortDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity).setTitle("Sorting type")
                .setSingleChoiceItems(R.array.array_sort_dailog, sortFlaPosition , null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        switch (position) {

                            case 0:
                                Collections.sort(goodsAdapter.getCollection(), new ComparatorForDate());
                                sortFlaPosition = 0;
                                goodsAdapter.setSortFlag(0);
                                goodsAdapter.notifyDataSetChanged();
                                break;

                            case 1:
                                Collections.sort(goodsAdapter.getCollection(), new ComparatorForChecked());
                                sortFlaPosition = 1;
                                goodsAdapter.setSortFlag(1);
                                goodsAdapter.notifyDataSetChanged();
                                break;

                            case 2:
                                Collections.sort(goodsAdapter.getCollection(), new ComparatorForName());
                                sortFlaPosition = 2;
                                goodsAdapter.setSortFlag(2);
                                goodsAdapter.notifyDataSetChanged();
                                break;

                        }
                    }
                })
                .setNegativeButton("Cancel",null);
                 alertDialog.show();
    }

    private void addFromGoodsRecipe(){
        if(activity.getRecipeModel()!=null){

            try {

                JSONArray array = PreferencesManager.loadObjectInJSONArray(activity, activity.getListName());

                for (int i = 0;i<activity.getRecipeModel().getIngredients().size();i++){
                    if(!activity.getRecipeModel().getIngredient(i).isBought()) {
                        array.put(activity.getRecipeModel().getIngredient(i).getModelJOSNObject());
                    }
                }
                PreferencesManager.saveData(activity, activity.getListName(), array);


            } catch (JSONException e) {
                try {
                JSONArray array = new JSONArray();
                for (int i = 0;i<activity.getRecipeModel().getIngredients().size();i++){

                       if(!activity.getRecipeModel().getIngredient(i).isBought()){
                           array.put(activity.getRecipeModel().getIngredient(i).getModelJOSNObject());
                       }

                }
                    PreferencesManager.saveData(activity,activity.getListName(),array);
                } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

            }
            activity.setRecipeModel(null);
        }

    }

}

