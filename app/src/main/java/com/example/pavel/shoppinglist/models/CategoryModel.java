package com.example.pavel.shoppinglist.models;

import com.example.pavel.shoppinglist.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryModel extends Model {


    private int photoId;
    private String photoUrl = "";

    /*Constructors*/
    public CategoryModel(int id, String name, int photoId) {
        super.name = name;
        this.id = id;
        this.photoId = photoId;
    }

    public CategoryModel(String name, int photoId) {
        super.name = name;
        this.photoId = photoId;
    }

    public CategoryModel(String name, String photoUrl) {
        super.name = name;
        this.photoUrl = photoUrl;
    }

    /*Methods*/
    public int getPhotoId() {
        return photoId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    @Override
    public boolean equals(Object model) {

        return name.equals(((CategoryModel) model).getName()) && id == ((CategoryModel) model).getId();

    }

    @Override
    public JSONObject getModelJOSNObject() throws JSONException {


        JSONObject object = new JSONObject();
        object.put(MainActivity.PHOTO_ID_KEY ,photoUrl);
        object.put(MainActivity.ITEM_NAME_KEY ,name);


        return object;
    }

    @Override
    public void convertFromJSONObject(JSONObject jsonObjectModel) throws JSONException {

        name = jsonObjectModel.getString(MainActivity.ITEM_NAME_KEY).replace("_"," ");
        photoId = jsonObjectModel.getInt(MainActivity.PHOTO_ID_KEY);

    }
}
