package com.example.pavel.shoppinglist.models;


import org.json.JSONException;
import org.json.JSONObject;

abstract public class Model {

    /*Params*/
    /**
     * Contains model name
     */
    protected String name;
    /**
     * Contains unique model id
     */
    protected int id;
    /**
     * Contain date in milliseconds when product was add
     */
    protected long date;

    /*Constructors*/

    public Model() {

    }

    public Model(String name, int id, long date) {
        this.name = name;
        this.id = id;
        this.date = date;
    }

    /*Setters*/

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }
    /*Getters*/

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    /*Work with JSON*/
    /**Convert model data to JSONObject end return it*/
    public abstract JSONObject getModelJOSNObject() throws JSONException;

    /**Convert JSONObject to current model data
     * jsonObjectModel - JSONObject corresponding with current model
     */
    public abstract void convertFromJSONObject(JSONObject jsonObjectModel) throws JSONException;
}
