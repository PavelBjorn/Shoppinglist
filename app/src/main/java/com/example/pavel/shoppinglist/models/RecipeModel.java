package com.example.pavel.shoppinglist.models;


import com.example.pavel.shoppinglist.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeModel extends Model {


    /*Params*/
    private String photoId;
    private String description = "";
    private ArrayList<GoodsModel> ingredients = new ArrayList<>();
    private int servings =1;
    public static final String INGREDIENTS_KEY = "ingredients";
    public static final String SERVINGS_KEY = "servings";
    public static final String DESCRIPTION_KEY = "description";



    /*Constructors*/
    public RecipeModel() {

    }

    public RecipeModel(String photoURL, String name, String description, ArrayList<GoodsModel> ingredients) {

        this.photoId = photoURL;
        super.name = name;
        this.description = description;
        this.ingredients.addAll(ingredients);

    }


    /*Setters*/

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoId(String photoURL) {
        this.photoId = photoURL;
    }

    public void addIngredient(GoodsModel ingredient) {
        this.ingredients.add(ingredient);
    }

    /*Getters*/

    public int getServings() {
        return servings;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getDescription() {
        return description;
    }

    public GoodsModel getIngredient(int position) throws ArrayIndexOutOfBoundsException {
        return ingredients.get(position);
    }

    public ArrayList<GoodsModel> getIngredients() {
        return ingredients;
    }

    /*JSON work*/

    @Override
    public JSONObject getModelJOSNObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MainActivity.PHOTO_ID_KEY, photoId);
        jsonObject.put(MainActivity.ITEM_NAME_KEY, name);
        jsonObject.put(DESCRIPTION_KEY, description);
        jsonObject.put(SERVINGS_KEY,servings);
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < ingredients.size(); i++) {
            jsonArray.put(ingredients.get(i).getModelJOSNObject());
        }

        jsonObject.put(INGREDIENTS_KEY, jsonArray);

        return jsonObject;
    }

    @Override
    public void convertFromJSONObject(JSONObject jsonObjectModel) throws JSONException {

        photoId = jsonObjectModel.getString(MainActivity.PHOTO_ID_KEY);
        name = jsonObjectModel.getString(MainActivity.ITEM_NAME_KEY);
        description = jsonObjectModel.getString(DESCRIPTION_KEY);
        servings = jsonObjectModel.getInt(SERVINGS_KEY);
        JSONArray array = jsonObjectModel.getJSONArray(INGREDIENTS_KEY);

        for (int i = 0; i < array.length(); i++) {

            GoodsModel model = new GoodsModel();
            model.convertFromJSONObject(array.getJSONObject(i));
            ingredients.add(model);

        }

    }


}
