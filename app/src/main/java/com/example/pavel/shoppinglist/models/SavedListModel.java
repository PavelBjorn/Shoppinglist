package com.example.pavel.shoppinglist.models;


import com.example.pavel.shoppinglist.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SavedListModel extends Model {

    /*Params*/

    /**Contains goods list budget*/
    private double budget = 0;
    /**Contains budget money units*/
    private String units;
    /**Contains true or false. true  if goods list have budget */
    private boolean haveBudget = false;
    /**Contains how many goods was bought/(add to lis). Default - 0/0*/
    private String goodsCount = "0_0";

    /*Constructors*/
    public SavedListModel(){

    }

    public SavedListModel(String name, long date) {
        this.name = name;
        this.date = date;
    }

    public SavedListModel(String name, double budget, long date) {
        super.setName(name);
        super.setDate(date);
        this.budget = budget;
    }

    public SavedListModel(String name, double budget, String units, long date) {
        super.setName(name);
        super.setDate(date);
        this.budget = budget;
        this.units = units;
    }

    /*Setters*/

    public void setBudget(double money) {
        this.budget = money;
    }


    public void setUnits(String units) {
        this.units = units;
    }


    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount.replace("/","_");
    }

    public void setHaveBudget(boolean haveBudget) {
        this.haveBudget = haveBudget;
    }

    /*Getters*/
    public boolean haveBudget() {
        return haveBudget;
    }

    public double getBudget() {
        return budget;
    }

    public String getUnits() {
        return units;
    }

    public String getGoodsCount() {
        return goodsCount;
    }


    @Override
    public JSONObject getModelJOSNObject() throws JSONException {

        JSONObject object = new JSONObject();

        object.put(MainActivity.ITEM_NAME_KEY, name);
        object.put(MainActivity.BUDGET_KEY, budget);
        object.put(MainActivity.UNITS_KEY, units);
        object.put(MainActivity.COUNT_KEY, goodsCount);
        object.put(MainActivity.DATE_KEY, date);
        object.put(MainActivity.HAVE_BUDGET_KEY, haveBudget);


        return object;

    }

    @Override
    public void convertFromJSONObject(JSONObject jsonObjectModel) throws JSONException {

        name = jsonObjectModel.getString(MainActivity.ITEM_NAME_KEY);
        budget = jsonObjectModel.getDouble(MainActivity.BUDGET_KEY);
        date = jsonObjectModel.getLong(MainActivity.DATE_KEY);
        units = jsonObjectModel.getString(MainActivity.UNITS_KEY);
        goodsCount = jsonObjectModel.getString(MainActivity.COUNT_KEY);
        haveBudget = jsonObjectModel.getBoolean(MainActivity.HAVE_BUDGET_KEY);

    }
}
