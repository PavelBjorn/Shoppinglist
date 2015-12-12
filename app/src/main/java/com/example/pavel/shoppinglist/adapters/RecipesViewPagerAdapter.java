package com.example.pavel.shoppinglist.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.pavel.shoppinglist.fragments.RecipeFragment;
import com.example.pavel.shoppinglist.models.RecipeModel;

import org.json.JSONException;

import java.util.ArrayList;

public class RecipesViewPagerAdapter  extends FragmentStatePagerAdapter {


    private ArrayList<RecipeModel> models = new ArrayList<>();
    public static final String RECIPE_KEY = "recipe";
    private static final String TAG = "recipesAdapterTag";


    public RecipesViewPagerAdapter(FragmentManager fm) {

        super(fm);

    }

    public RecipesViewPagerAdapter(FragmentManager fm, ArrayList<RecipeModel>models) {
        super(fm);
        this.models.addAll(models);
    }

    public void addAll(ArrayList<RecipeModel>models){

        this.models.addAll(models);
        notifyDataSetChanged();

    }

    public void clear(){

        this.models.clear();

    }

    public RecipeModel getRecipeModel(int position){
      return models.get(position);
    }

    @Override
    public Fragment getItem(int position) {

        Log.d(TAG , "getItem");

        RecipeFragment recipeFragment = new RecipeFragment();

        Bundle b = new Bundle();
        try {
            b.putString(RECIPE_KEY,models.get(position).getModelJOSNObject().toString());
        } catch (JSONException e) {
            Log.d(TAG,""+e);
        }
        recipeFragment.setArguments(b);
        
        return recipeFragment;
    }

    @Override
    public int getCount() {

        return models.size();

    }

}
