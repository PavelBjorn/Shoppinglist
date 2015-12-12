package com.example.pavel.shoppinglist.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pavel.shoppinglist.App;
import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.adapters.ListViewIngredientsAdapter;
import com.example.pavel.shoppinglist.adapters.RecipesViewPagerAdapter;
import com.example.pavel.shoppinglist.data.PreferencesManager;
import com.example.pavel.shoppinglist.models.RecipeModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class RecipeFragment extends Fragment /*implements OnRecipeListener*/ {

    private MainActivity activity;
    private RecipeModel recipe;
    private ListViewIngredientsAdapter adapter;

    private static final String TAG = "recipeFragTag";



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        recipe = new RecipeModel();
        String data = getArguments().getString(RecipesViewPagerAdapter.RECIPE_KEY);

        try {
            recipe.convertFromJSONObject(new JSONObject(data));
        } catch (JSONException e) {
            Log.d(TAG, "" + e);
        }

        View v = inflater.inflate(R.layout.fragment_recipe, null);

        Log.d(TAG, "onCreateViewRecipeFragment");

        String[] groupName = {"Ingredients", "Description"};

        Map<String, String> map;

        ArrayList<Map<String, String>> groupData = new ArrayList<>();

        ArrayList<Map<String, String>> childItem;

        ArrayList<ArrayList<Map<String, String>>> childData = new ArrayList<>();

        for (String name : groupName) {
            map = new HashMap<>();
            map.put("groupName", name);
            groupData.add(map);
        }


        String[] groupFrom = {"groupName"};
        int[] groupTo = {android.R.id.text1};

        childItem = new ArrayList<>();

        try {
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                map = new HashMap<>();
                map.put("ingredientsName", recipe.getIngredient(i).getName()
                        + " (" + recipe.getIngredient(i).getQuantity() + " "
                        + recipe.getIngredient(i).getUnits() + ")");
                childItem.add(map);
            }


            childData.add(childItem);


            map = new HashMap<>();
            map.put("ingredientsName", recipe.getDescription());
            childItem = new ArrayList<>();
            childItem.add(map);

            childData.add(childItem);

            String[] childFrom = {"ingredientsName"};
            int[] childTo = {android.R.id.text1};

            adapter = new ListViewIngredientsAdapter(activity, recipe);


            TextView tvRecipeName = (TextView) v.findViewById(R.id.fragment_recipe_name);
            tvRecipeName.setText(recipe.getName());

            TextView tvDescription = (TextView) v.findViewById(R.id.fragment_recipe_tv_description);
            tvDescription.setText(recipe.getDescription());

            Button btnShowIgr = (Button) v.findViewById(R.id.fragment_recipe_ib_open_ing);

            btnShowIgr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupWindow(v);
                }
            });

            ImageView imvPhoto = (ImageView) v.findViewById(R.id.fragment_recipe_imv_photo);

            ImageLoader.getInstance().displayImage(recipe.getPhotoId(), imvPhoto, App.displayImageOptionsForGv());


        } catch (NullPointerException e) {
            Log.d(TAG, "" + e);

        }
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    public void showPopupWindow(View view){


        View v =activity.getLayoutInflater().inflate(R.layout.popup_window_igredients, null);
        ListView lvIngredients = (ListView) v.findViewById(R.id.popup_window_lv_ingredients);
        ImageButton ibAddToList = (ImageButton) v.findViewById(R.id.popup_window_ingredients_ib_add_to_list);
        ImageButton ibPlus = (ImageButton)  v.findViewById(R.id.popup_window_ingredients_ib_plus);
        ImageButton ibMinus = (ImageButton)  v.findViewById(R.id.popup_window_ingredients_ib_minus);


        final TextView tvResult = (TextView) v.findViewById(R.id.popup_window_ingredients_tv_result);
        tvResult.setText(""+adapter.getRecipe().getServings());

        lvIngredients.setAdapter(adapter);

        final PopupWindow popupWindow = new PopupWindow(v, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);

        if (!popupWindow.isShowing()) {
            if(activity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                popupWindow.showAsDropDown(view);
            }else {
                popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
            }
        }

        ibAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setRecipeModel(recipe);
                activity.replaceFragment(new ShoppingListsFragment(), R.id.main_activity_gl_fragment_content, false);
                popupWindow.dismiss();
            }
        });

        ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int beforeChange = Integer.valueOf(tvResult.getText().toString());
                adapter.getRecipe().setServings(beforeChange + 1);
                tvResult.setText("" + adapter.getRecipe().getServings());

                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    adapter.getItem(i).setQuantity("" + (Double.parseDouble(adapter.getItem(i).getQuantity()) / beforeChange * (beforeChange + 1)));
                }
                adapter.notifyDataSetChanged();
            }
        });

        ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvResult.getText().toString().equals("1")) {
                    int beforeChange = Integer.valueOf(tvResult.getText().toString());
                    adapter.getRecipe().setServings(beforeChange - 1);
                    tvResult.setText("" + adapter.getRecipe().getServings());
                    for (int i = 0; i < recipe.getIngredients().size(); i++) {
                        adapter.getItem(i).setQuantity("" + (Double.parseDouble(adapter.getItem(i).getQuantity()) / beforeChange * (beforeChange - 1)));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

}
