package com.example.pavel.shoppinglist;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.pavel.shoppinglist.data.PreferencesManager;
import com.example.pavel.shoppinglist.fragments.ActionBarFragmentForGoodsList;
import com.example.pavel.shoppinglist.fragments.ActionBarFragmentLists;
import com.example.pavel.shoppinglist.fragments.ActionBarSearchFragment;
import com.example.pavel.shoppinglist.fragments.GoodsListFragment;
import com.example.pavel.shoppinglist.fragments.RecipeListFragment;
import com.example.pavel.shoppinglist.fragments.ShoppingListsFragment;
import com.example.pavel.shoppinglist.listeners.OnGoodsCountChangeListener;
import com.example.pavel.shoppinglist.listeners.OnSearchListener;
import com.example.pavel.shoppinglist.listeners.OnTitleChangeListener;
import com.example.pavel.shoppinglist.models.Model;
import com.example.pavel.shoppinglist.models.RecipeModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnGoodsCountChangeListener, OnTitleChangeListener {


    /*View elements*/
    private ImageButton imBtnOpenDrawer, imBtnMenu, imBtnSearch;
    private DrawerLayout drawerLayout;
    private FrameLayout flDrawerContent;

    /*Transaction data */
    private String listName = "";
    private static double budget = 0.0;
    private String goodsCount = "0/0";
    private static String budgetUnits = "UAH";
    private boolean haveBudget = false;
    private int menuId;
    private PopupMenu.OnMenuItemClickListener menuItemClickListener;
    private ArrayAdapter<String> searchAdapter;
    private RecipeModel recipeModel;


    /*Flags*/
    private int replFragmentFlag = 0;
    private boolean openDrawerFlag = false;
    private boolean searchFlag = false;

    /*Constant Keys*/
    public static final String
            MODEL_ID_KEY = "modelID",
            ITEM_NAME_KEY = "listName",
            BUDGET_KEY = "budget",
            MONEY_KEY = "moneyUnits",
            HAVE_BUDGET_KEY = "haveBudget",
            IS_BOUGHT_PRODUCT_KEY = "checked",
            DATE_KEY = "date", PRICE_KEY = "price",
            COUNT_KEY = "count", PHOTO_ID_KEY = "photoId",
            UNITS_KEY = "units", QUANTITY_KEY = "num",
            COMMENTS_KEY = "comments",
            CATEGORY_NAME_KEY = "category";

    public static final String SHOPPING_LISTS_PREFERENCES_NAME = "list";

    public static final int REQUEST_FOR_PRODUCT = 1;

    private OnSearchListener listener;

    /*Methods*/
    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setGoodsCount(String count) {

        this.goodsCount = count;

        try {
            ((ActionBarFragmentForGoodsList) getSupportFragmentManager().findFragmentById(R.id.ll_myActionBar_content)).setCountTitle(goodsCount.replace("_", "/"));
        } catch (ClassCastException | NullPointerException e) {
            Log.d("myTag", "for method setCountTitle \"fragment\" Must contain ActionBarFragmentForGoodsList ");
        }

    }

    public void setBudgetUnits(String budgetUnits) {
        this.budgetUnits = budgetUnits;
    }

    public void setHaveBudget(boolean haveBudget) {
        this.haveBudget = haveBudget;
    }

    public void setMenuResId(int menuId) {
        this.menuId = menuId;
    }

    public void setRecipeModel(RecipeModel recipeModel){
        this.recipeModel = recipeModel;
    }

    /*-Fragment switch*/
    public void replaceFragment(Fragment fragment, int conteinerId, boolean backstackFlag) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(conteinerId, fragment);
        if (backstackFlag) {
            transaction.addToBackStack("myTag");
        }
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, int conteinerId, boolean backstackFlag, boolean isActionBarFragment) {

        replaceFragment(fragment, conteinerId, backstackFlag);

        if (isActionBarFragment) {
            fragment = getSupportFragmentManager().findFragmentById(conteinerId);
        }
    }

    public void delFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fm.findFragmentById(R.id.fl_action_bar_search));
        transaction.commit();
        searchFlag = false;
    }

    /*-Setters*/
    public void setSearchName(String name) {
        listener.onSearch(name);
    }

    public void setMenuItemClickListener(PopupMenu.OnMenuItemClickListener listener) {
        menuItemClickListener = listener;
    }

    public void setOpenDrawerFlag(boolean flag) {
        openDrawerFlag = flag;
        try {
            if (!flag) {
                imBtnOpenDrawer.setImageResource(R.drawable.btn_open_drawer);
            } else {
                imBtnOpenDrawer.setImageResource(R.drawable.btn_back);
            }
        } catch (NullPointerException e) {

        }
    }

    public void setSearchListener(OnSearchListener listener){
       this.listener = listener;
    }

    /*-Getters*/
    public double getBudget() {
        return budget;
    }

    public String getBudgetUnits() {
        return budgetUnits;
    }

    public String getListName() {
        return listName;
    }

    public boolean haveBudget() {
        return haveBudget;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public RecipeModel getRecipeModel(){
        return recipeModel;
    }

    public void culcBudgetChange(double price, int operation) {

        switch (operation) {
            case 0:
                budget -= price;
                break;
            case 1:
                budget += price;
                break;
        }

        try {
            JSONArray array = PreferencesManager.loadObjectInJSONArray(this, SHOPPING_LISTS_PREFERENCES_NAME);

            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).getString(ITEM_NAME_KEY).replace("_", " ").equals(listName)) {
                    array.getJSONObject(i).put(BUDGET_KEY, budget);
                }
            }


            PreferencesManager.saveData(this, MainActivity.SHOPPING_LISTS_PREFERENCES_NAME, array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ((ActionBarFragmentForGoodsList) getSupportFragmentManager().findFragmentById(R.id.ll_myActionBar_content)).setBudgetTitle(String.format("%.2f", budget) + " " + budgetUnits);
        } catch (ClassCastException e) {
            Log.d("myTag", "for method setBudgetTitle \"fragment\" Must contain ActionBarFragmentForGoodsList ");
        }


    }

    public boolean isSearchActive() {
        return searchFlag;
    }

    /*-drawer state*/
    public void setDrawerEnable(boolean enable) {
        try {


            int lockMode;
            if (enable) {
                lockMode = DrawerLayout.LOCK_MODE_UNLOCKED;
            } else {
                lockMode = DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
            }
            drawerLayout.setDrawerLockMode(lockMode);
        } catch (NullPointerException e) {

        }
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    /*JSON data work*/
    public static JSONArray toJSONArray(ArrayList<? extends Model> models) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < models.size(); i++) {
            try {
                array.put(models.get(i).getModelJOSNObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    /*Work with adapter*/
    public void addToSearchAdapter(String name) {
        searchAdapter.add(name);
        searchAdapter.notifyDataSetChanged();
    }

    public ArrayAdapter<String> getSearchAdapter() {
        return searchAdapter;
    }

    /*-Activity lifecycle*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        /*-restore activity's state*/
        if (savedInstanceState != null) {
            listName = savedInstanceState.getString("name");
            budget = savedInstanceState.getDouble("budget");
            goodsCount = savedInstanceState.getString("count");
            haveBudget = savedInstanceState.getBoolean("haveBudget");
            budgetUnits = savedInstanceState.getString("budgetUnits");
        }

        /*-find activity view elements */
        imBtnOpenDrawer = (ImageButton) findViewById(R.id.imbtn_open_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_navigation);
        imBtnMenu = (ImageButton) findViewById(R.id.imbtn_menu);
        imBtnSearch = (ImageButton) findViewById(R.id.imbtn_search);
        flDrawerContent = (FrameLayout) findViewById(R.id.main_activity_navigation_content);

        /*-set listeners stat to view elements*/
        imBtnMenu.setOnClickListener(this);
        imBtnOpenDrawer.setOnClickListener(this);
        imBtnSearch.setOnClickListener(this);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                try {
                    Fragment goodsListFragmet = getSupportFragmentManager().findFragmentById(R.id.main_activity_gl_fragment_content);
                    ((GoodsListFragment) goodsListFragmet).removeBtn();
                } catch (ClassCastException e) {
                    Log.d("myTag", "must contain GoodsListFragment");
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {


            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        Fragment f;

        if(getLastCustomNonConfigurationInstance()!=null){
            try {
                f = (Fragment) getLastCustomNonConfigurationInstance();
            }catch (ClassCastException e){
                Log.d("mainActivityTag",""+e);
                f = new ShoppingListsFragment();
            }
        }else {
            f = new ShoppingListsFragment();
        }

        boolean backStackFlag = f instanceof GoodsListFragment;

        if(backStackFlag){
            getSupportFragmentManager().popBackStack();
            replaceFragment(new ShoppingListsFragment(), R.id.main_activity_gl_fragment_content, false);
        }else if(f instanceof RecipeListFragment){
            f = new RecipeListFragment();
        }

        /*-restore current fragment */
        replaceFragment(f, R.id.main_activity_gl_fragment_content, backStackFlag);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("name", listName);
        savedInstanceState.putDouble("budget", budget);
        savedInstanceState.putString("count", goodsCount);
        savedInstanceState.putBoolean("haveBudget", haveBudget);
        savedInstanceState.putString("budgetUnits", budgetUnits);


    }

    @Override
    public Fragment onRetainCustomNonConfigurationInstance() {

        return getSupportFragmentManager().findFragmentById(R.id.main_activity_gl_fragment_content);

    }


    /*-Implemented listeners*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /*button for opening drawer */
            case R.id.imbtn_open_drawer:
                if (!openDrawerFlag) {

                    int grav = Gravity.LEFT;
                    if (drawerLayout.isDrawerOpen(grav)) {
                        drawerLayout.closeDrawers();
                    } else {
                        drawerLayout.openDrawer(grav);
                    }

                } else {

                    onBackPressed();
                    openDrawerFlag = false;
                }
                break;

            case R.id.imbtn_menu:
                PopupMenu popupMenu = new PopupMenu(this, imBtnMenu);
                popupMenu.inflate(menuId);
                popupMenu.setOnMenuItemClickListener(menuItemClickListener);
                popupMenu.show();
                break;

            case R.id.imbtn_search:

                replaceFragment(new ActionBarSearchFragment(), R.id.fl_action_bar_search, false);
                searchFlag = true;

                break;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            if (searchFlag) {
                delFragment();
            }
            try {
                Fragment goodsListFragmnet = getSupportFragmentManager().findFragmentById(R.id.main_activity_gl_fragment_content);
                ((GoodsListFragment) goodsListFragmnet).removeBtn();
            } catch (ClassCastException e) {
                Log.d("myTag", "must contain GoodsListFragment");
            }
        }
    }

    @Override
    public void countChange(String count) {

        this.goodsCount = count.replace("_", "/");

        try {
            ((ActionBarFragmentForGoodsList) getSupportFragmentManager().findFragmentById(R.id.ll_myActionBar_content)).setCountTitle(goodsCount.replace("_", "/"));
        } catch (ClassCastException | NullPointerException e) {
            Log.d("myTag", "for method setCountTitle \"fragment\" Must contain ActionBarFragmentForGoodsList ");
        }

        try {

            JSONArray array = PreferencesManager.loadObjectInJSONArray(this, SHOPPING_LISTS_PREFERENCES_NAME);

            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).getString(ITEM_NAME_KEY).replace("_", " ").equals(listName)) {
                    array.getJSONObject(i).put(COUNT_KEY, count.replace("/", "_"));
                }
            }

            PreferencesManager.saveData(this, MainActivity.SHOPPING_LISTS_PREFERENCES_NAME, array.toString());

        } catch (JSONException e) {

        }
    }

    @Override
    public void onTitleChange(String title) {

        try {
            ActionBarFragmentLists fragment = (ActionBarFragmentLists) getSupportFragmentManager().findFragmentById(R.id.ll_myActionBar_content);
            fragment.setTitle(title);
        } catch (ClassCastException e ) {
            Log.d("mainActivityTag", "" + e);
        }

    }
}
