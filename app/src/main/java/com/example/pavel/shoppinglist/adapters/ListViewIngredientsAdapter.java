package com.example.pavel.shoppinglist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.models.GoodsModel;
import com.example.pavel.shoppinglist.models.RecipeModel;

/**
 * Created by Pavel on 06.09.2015.
 */
public class ListViewIngredientsAdapter extends BaseAdapter {

    private MainActivity activity ;

    private LayoutInflater inflater;

    private RecipeModel model;


    public ListViewIngredientsAdapter(MainActivity activity){
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public ListViewIngredientsAdapter(MainActivity activity,RecipeModel model){
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.model = model;
    }

    public void addModel(RecipeModel model){
        this.model = model;
        notifyDataSetChanged();
    }

    public void clear(){
        this.model = null;
    }

    public RecipeModel getRecipe(){
        return model;
    }


    @Override
    public int getCount() {
        return model.getIngredients().size();
    }

    @Override
    public GoodsModel getItem(int position) {
        return model.getIngredient(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v;
        if(convertView!=null){
            v=convertView;
        }else {
            v = inflater.inflate(R.layout.item_list_view_ingredients,null);
        }

        TextView tvName = (TextView) v.findViewById(R.id.elv_ch_tv_name);
        TextView tvCount = (TextView) v.findViewById(R.id.elv_ch_tv_count);
        CheckBox chbHave= (CheckBox) v.findViewById(R.id.elv_ch_chb_have);

        chbHave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   model.getIngredient(position).setBought(false);
                    notifyDataSetChanged();
                }else {
                    model.getIngredient(position).setBought(true);
                }
            }
        });


        tvName.setText(model.getIngredient(position).getName());
        tvCount.setText(String.format("%.2f",Double.parseDouble(model.getIngredient(position).getQuantity()))+" "+model.getIngredient(position).getUnits());


        return v;
    }
}
