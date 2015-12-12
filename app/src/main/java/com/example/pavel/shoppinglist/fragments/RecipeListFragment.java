package com.example.pavel.shoppinglist.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.adapters.RecipesViewPagerAdapter;
import com.example.pavel.shoppinglist.anim.DepthPageTransformer;
import com.example.pavel.shoppinglist.data.PreferencesManager;
import com.example.pavel.shoppinglist.listeners.OnSearchListener;
import com.example.pavel.shoppinglist.listeners.OnTitleChangeListener;
import com.example.pavel.shoppinglist.models.GoodsModel;
import com.example.pavel.shoppinglist.models.RecipeModel;

import java.util.ArrayList;

public class RecipeListFragment extends Fragment implements OnSearchListener {

    /*Params*/
    private MainActivity activity;
    private static final String TAG = "recipeListFragmentTag";
    private RecipesViewPagerAdapter adapter;
    private ViewPager vpRecipes;
    private OnTitleChangeListener listener;
    private int currentRecipe = 0;
    public static final String CURRENT_RECIPE_KEY = "currentRecipe";

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this.activity = (MainActivity) activity;
        this.activity.setDrawerEnable(true);
        this.listener = (OnTitleChangeListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        ArrayList<RecipeModel> models = new ArrayList<>();

        models.add(borschtRecipe());
        models.add(pancakesRecipe());
        models.add(sandwichRecipe());

        adapter = new RecipesViewPagerAdapter(activity.getSupportFragmentManager(), models);
        View v = inflater.inflate(R.layout.fragment_recipe_list, null);

        vpRecipes = (ViewPager) v.findViewById(R.id.fragment_recipe_vp_recipe);
        vpRecipes.setPageTransformer(true, new DepthPageTransformer());
        vpRecipes.setAdapter(adapter);

        String data = PreferencesManager.loadObject(activity, CURRENT_RECIPE_KEY);

        if(!data.isEmpty()) {
            vpRecipes.setCurrentItem(Integer.valueOf(PreferencesManager.loadObject(activity, CURRENT_RECIPE_KEY)));
        }

        vpRecipes.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentRecipe=vpRecipes.getCurrentItem();
                PreferencesManager.saveData(activity, CURRENT_RECIPE_KEY, "" + currentRecipe);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;
    }

    @Override
    public void onResume() {

        super.onResume();
        activity.setSearchListener(this);
        activity.getSearchAdapter().clear();
        for (int i = 0; i < adapter.getCount(); i++) {
            activity.addToSearchAdapter(adapter.getRecipeModel(i).getName());
        }
        listener.onTitleChange("Recipes");
    }

    @Override
    public void onDetach() {

        super.onDetach();
        Log.d(TAG, "onCreateView");

    }

    @Override
    public void onSearch(String name) {

        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getRecipeModel(i).getName().trim().toLowerCase().equals(name.trim().toLowerCase())) {
                vpRecipes.setCurrentItem(i);
                break;
            }
        }

    }

    private RecipeModel borschtRecipe() {

        ArrayList<GoodsModel> ing = new ArrayList<>();

        GoodsModel meat = new GoodsModel("assets://category_meat.jpg", "Мясо", "0.5", "kg");
        meat.setCategory("Meat");
        meat.setBought(true);
        ing.add(meat);

        GoodsModel potato = new GoodsModel("assets://category_vegetables.jpg", "Картофель", "0.4", "kg");
        potato.setCategory("Vegetables");
        potato.setBought(true);
        ing.add(potato);

        GoodsModel cabbage = new GoodsModel("assets://category_salad.jpg", "Белокочанная капуста", "0.4", "kg");
        cabbage.setCategory("Salad");
        cabbage.setBought(true);
        ing.add(cabbage);


        GoodsModel beetroot = new GoodsModel("assets://category_vegetables.jpg", "Свекла", "0.2", "kg");
        beetroot.setCategory("Vegetables");
        beetroot.setBought(true);
        ing.add(beetroot);

        GoodsModel sourCream = new GoodsModel("assets://category_vegetables.jpg", "Сметана", "0.1", "kg");
        sourCream.setCategory("Dairy");
        sourCream.setBought(true);
        ing.add(sourCream);

        GoodsModel tomatoPaste = new GoodsModel("assets://category_vegetables.jpg", "Томатная паста", "0.1", "kg");
        tomatoPaste.setCategory("Vegetables");
        tomatoPaste.setBought(true);
        ing.add(tomatoPaste);

        GoodsModel onions = new GoodsModel("assets://category_vegetables.jpg", "Лук репчатый", "1", "pcs");
        onions.setCategory("Vegetables");
        onions.setBought(true);
        ing.add(onions);


        return new RecipeModel("assets://recipe_borscht.jpg", "Борщ", "1.  Сварить мясной бульон и процедить.\n" +
                "2.  Очищенные коренья и свеклу нарезать соломкой. Свеклу тушить 20-30 минут, добавив при этом жир, томат-пюре, уксус и бульон (можно влить также хлебный или свекольный квас). Нарезанные коренья и лук слегка поджарить с маслом, смешать с поджаренной мукой, развести бульоном и довести до кипения.\n" +
                "3.  В приготовленный для борща бульон положить картофель, нарезанный крупными кубиками, крупно нарезанную капусту, тушеную свеклу, соль и варить 10-15 минут, потом добавить поджаренные с мукой коренья, лавровый лист, душистый и горький перец и варить до тех пор, пока картофель и капуста не будут готовы.\n" +
                "4.  Готовый борщ заправить салом, растертым с чесноком, добавить помидоры, нарезанные дольками, быстро довести до кипения, после чего дать борщу настояться в течение 15-20 минут.\n" +
                "5.  Разливая в тарелки, положить в борщ сметану и посыпать мелко нарезанной зеленью петрушки.", ing);

    }

    private RecipeModel pancakesRecipe() {

        ArrayList<GoodsModel> ing = new ArrayList<>();

        GoodsModel i1 = new GoodsModel("assets://category_bakery.jpg", "Мука пшеничная", "0.2", "kg");
        i1.setCategory("Bakery");
        i1.setBought(true);
        ing.add(i1);

        GoodsModel i2 = new GoodsModel("assets://category_dairy.jpg", "Яйцо куринное", "2", "pcs");
        i2.setCategory("Dairy");
        i2.setBought(true);
        ing.add(i2);

        GoodsModel cabbage = new GoodsModel("assets://category_dairy.jpg", "Молоко", "0.2", "kg");
        cabbage.setCategory("Dairy");
        cabbage.setBought(true);
        ing.add(cabbage);


        GoodsModel beetroot = new GoodsModel("assets://category_spices.jpg", "Соль", "0.01", "kg");
        beetroot.setCategory("Spices");
        beetroot.setBought(true);
        ing.add(beetroot);

        GoodsModel sourCream = new GoodsModel("assets://category_spices.jpg", "Масло растительное", "0.01", "kg");
        sourCream.setCategory("Spices");
        sourCream.setBought(true);
        ing.add(sourCream);


        return new RecipeModel("assets://recipe_pancakes.jpg", "Блины на молоке", "1.  Взбить в пену яйца с солью.\n" +
                "2.  Не переставая взбивать, влить стакан кипятка.\n" +
                "3.  Не переставая взбивать, влить стакан холодного молока.\n" +
                "4.  Всыпать 1 стакан муки.\n" +
                "5.  В тесто добавить растительное масло.\n" +
                "6.  Печь на горячей сковородке, смазанной оливковым или топленым сливочным маслом.", ing);

    }

    private RecipeModel sandwichRecipe() {

        ArrayList<GoodsModel> ing = new ArrayList<>();

        GoodsModel i1 = new GoodsModel("assets://category_meat.jpg", "Бекон", "0.01", "kg");
        i1.setCategory("Meat");
        i1.setBought(true);
        ing.add(i1);

        GoodsModel i2 = new GoodsModel("assets://category_dairy.jpg", "Сыр эдам", "0.02", "kg");
        i2.setCategory("Dairy");
        i2.setBought(true);
        ing.add(i2);

        GoodsModel cabbage = new GoodsModel("assets://category_vegetables.jpg", "Помидоры", "1", "pcs");
        cabbage.setCategory("Vegetables");
        cabbage.setBought(true);
        ing.add(cabbage);


        GoodsModel i4 = new GoodsModel("assets://category_spices.jpg", "Майонез", "0.01", "kg");
        i4.setCategory("Spices");
        i4.setBought(true);
        ing.add(i4);

        GoodsModel i5 = new GoodsModel("assets://category_dairy.jpg", "Масло сливочное", "0.01", "kg");
        i5.setCategory("Dairy");
        i5.setBought(true);
        ing.add(i5);

        GoodsModel i6 = new GoodsModel("assets://category_spices.jpg", "Листья латука", "3", "pcs");
        i6.setCategory("Spices");
        i6.setBought(true);
        ing.add(i6);


        return new RecipeModel("assets://recipe_sandwich.jpg", "Сендвич с беконом", "1.  Поджарить тонкие ломтики бекона до золотистой корочки.\n" +
                "2.  Подогреть хлеб и одну половину сэндвича намазать майонезом, на вторую положить сыр, ломтики помидоров, бекон и латук.\n" +
                "3.  Тем временем на сливочном масле поджарить небольшую глазунью — такую, чтобы желток был полуготовым.\n" +
                "4.  Выложить глазунью на латук, закрыть другой половинкой сэндвича и подавать.", ing);

    }
}
