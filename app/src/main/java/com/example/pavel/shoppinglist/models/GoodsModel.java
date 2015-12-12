package com.example.pavel.shoppinglist.models;


import com.example.pavel.shoppinglist.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsModel extends Model {

    /*Params*/

    /**
     * Contains id of product image
     */
    private int photoId;

    private String photoUrl="";
    /**
     * Contains product price
     */
    private double price = 0.0;
    /**
     * Contains num of goods
     */
    private String quantity="1";
    /**
     * Contains goods units
     */
    private String units="pcs";
    /**
     * Contains comments  left by user
     */
    private String comments = "";
    /**
     * Contains product category
     */
    private String category = "";
    /**
     * Contain true ore false. If true product bought
     */
    private boolean isBought = false;



    /*Constructors*/

    public GoodsModel(){}

    public GoodsModel(String photoUrl, String name) {
        this.photoUrl = photoUrl;
        super.setName(name);
    }

    public GoodsModel(String name,String num, String units) {
        this.photoUrl = photoUrl;
        super.setName(name);
        this.units = units;
        this.quantity = num;
    }

    public GoodsModel(String photoUrl, String name, String num, String units) {
        this.photoUrl = photoUrl;
        super.setName(name);
        this.units = units;
        this.quantity = num;
    }

    public GoodsModel(String photoUrl, String name, String num, String units, String comments) {
        this.photoUrl = photoUrl;
        super.setName(name);
        this.units = units;
        this.quantity = num;
        this.comments = comments;
    }

    public GoodsModel(String photoUrl, String name, String num, String units, String comments, boolean isBought) {
        this.photoUrl = photoUrl;
        super.setName(name);
        this.units = units;
        this.quantity = num;
        this.comments = comments;
        this.isBought = isBought;
    }

    public GoodsModel(String photoUrl, String name, String num, String units, String comments, String category, long date) {
        this.photoUrl = photoUrl;
        super.setName(name);
        super.setDate(date);
        this.units = units;
        this.quantity = num;
        this.comments = comments;
        this.category = category;

    }

    public GoodsModel(String photoUrl, String name, String num, String units, String comments, boolean isBought, String category, long date) {
        this.photoUrl = photoUrl;
        super.setName(name);
        super.setDate(date);
        this.units = units;
        this.quantity = num;
        this.comments = comments;
        this.isBought = isBought;
        this.category = category;
    }

    /*Setters*/
    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setPhotoId(int photoId) {

        this.photoId = photoId;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price*Double.parseDouble(quantity);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBought(boolean isChecked) {
        this.isBought = isChecked;
    }

    /*Getters*/
    public String getQuantity() {
        return quantity;

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getPhotoId() {
        return photoId;
    }

    public double getPrice() {
        return price;
    }


    public String getComments() {
        return comments;
    }

    public String getCategory() {
        return category;
    }

    public boolean isBought() {
        return this.isBought;
    }

    public String getUnits() {
        return units;
    }


    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        GoodsModel gm = (GoodsModel) o;
        if (o instanceof GoodsModel) {
            equal = photoId == gm.getPhotoId()
                    && name.equals(gm.getName())
                    && units.equals(gm.getUnits())
                    && date == gm.getDate();
        }
        return equal;
    }

    @Override
    public JSONObject getModelJOSNObject() throws JSONException {

        JSONObject object = new JSONObject();

        object.put(MainActivity.PHOTO_ID_KEY, photoUrl);
        object.put(MainActivity.ITEM_NAME_KEY, name);
        object.put(MainActivity.QUANTITY_KEY, quantity);
        object.put(MainActivity.UNITS_KEY, units);
        object.put(MainActivity.IS_BOUGHT_PRODUCT_KEY, isBought);
        object.put(MainActivity.COMMENTS_KEY, comments);
        object.put(MainActivity.DATE_KEY, date);
        object.put(MainActivity.PRICE_KEY, price);
        object.put(MainActivity.CATEGORY_NAME_KEY, category);


        return object;
    }

    @Override
    public void convertFromJSONObject(JSONObject jsonObjectModel) throws JSONException {

        photoUrl =jsonObjectModel.getString(MainActivity.PHOTO_ID_KEY);
        name = jsonObjectModel.getString(MainActivity.ITEM_NAME_KEY).replace("_", " ");
        quantity = jsonObjectModel.getString(MainActivity.QUANTITY_KEY);
        units = jsonObjectModel.getString(MainActivity.UNITS_KEY);
        isBought = jsonObjectModel.getBoolean(MainActivity.IS_BOUGHT_PRODUCT_KEY);
        comments = jsonObjectModel.getString(MainActivity.COMMENTS_KEY).replace("_", " ");
        date = jsonObjectModel.getLong(MainActivity.DATE_KEY);
        price = jsonObjectModel.getDouble(MainActivity.PRICE_KEY);
        category = jsonObjectModel.getString(MainActivity.CATEGORY_NAME_KEY).replace("_", " ");

    }

}
