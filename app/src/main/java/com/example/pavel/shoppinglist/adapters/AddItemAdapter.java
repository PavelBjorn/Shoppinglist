package com.example.pavel.shoppinglist.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.models.CategoryModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.example.pavel.shoppinglist.App.*;

public class AddItemAdapter extends BaseAdapter {

    /*Params*/

    /* Inflater*/
    private LayoutInflater inflater;

    /* Context*/
    private Context context;


    /*Models Arrays*/
    private ArrayList<CategoryModel> models = new ArrayList<>();

    /*Constructors*/
    public AddItemAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
    }

    public AddItemAdapter(Context context, ArrayList<CategoryModel> models) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.models.addAll(models);
    }


    /*Methods*/
    /* - add*/
    public void add(CategoryModel model){
        models.add(model);
    }

    public void addAll(ArrayList<CategoryModel> models){
        this.models.addAll(models);
    }

    public void removeModel(int position) throws ArrayIndexOutOfBoundsException{
        models.remove(position);
    }

    public void clear() {
        models.clear();
    }

    /*-overrides*/
    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public CategoryModel getItem(int position) throws ArrayIndexOutOfBoundsException {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return models.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;

        if (convertView != null) {
            v = convertView;
        } else {
            v = inflater.inflate(R.layout.item_grid_view_add_product,null);
        }

        /*Find vew elements for gv */

        ImageView imvPhoto = (ImageView) v.findViewById(R.id.fragment_add_imv_photo);
        TextView tvName = (TextView) v.findViewById(R.id.fragment_add_tv_product_name);

        ImageLoader.getInstance().displayImage(models.get(position).getPhotoUrl(),imvPhoto, displayImageOptionsForGv());
        /*imvPhoto.setImageResource(models.get(position).getPhotoId());*/
        tvName.setText(models.get(position).getName());

        return v;

    }



}
