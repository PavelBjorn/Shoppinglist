package com.example.pavel.shoppinglist.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavel.shoppinglist.App;
import com.example.pavel.shoppinglist.InfoActivity;
import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.comparators.ComparatorForChecked;
import com.example.pavel.shoppinglist.listeners.OnGoodsCountChangeListener;
import com.example.pavel.shoppinglist.models.GoodsModel;
import com.example.pavel.shoppinglist.view.MySquareImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;

public class GoodsAdapter extends BaseAdapter {

    /*Params*/
    private Context context;
    private MainActivity activity;
    private LayoutInflater inflater;
    private ArrayList<GoodsModel> models = new ArrayList<>();
    private AlertDialog.Builder dialog;
    private View vdEdit;
    private EditText edtName, edtNum, edtComments, edtPrice;
    private Spinner spUnits;
    private ArrayAdapter<String> spAdapter;
    public static final String NAME_KEY = "name", PHOTO_KEY = "photo", NUM_KEY = "num", UNITS_KEY = "units", COMMENTS_KEY = "coments",
            PRICE_KEY = "PRICE", CATEGORY_KEY = "category";
    public static final String TAG = "goodsAdapterTag";
    private int checkedCount = 0;
    private boolean isFound = false;
    private int foundPosition;
    private int sortFlag = 0;

    /*-interface anonymous implementation  */
    OnGoodsCountChangeListener countListener;

    /*Methods*/
    public GoodsAdapter(Activity activity) {
        this.activity = (MainActivity) activity;
        this.context = this.activity;
        inflater = (LayoutInflater) context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        spAdapter = new ArrayAdapter<String>(this.context, R.layout.item_spiner, new String[]{"kg", "L.", "pcs"});
        try {
            countListener = (OnGoodsCountChangeListener) this.activity;
        } catch (ClassCastException e) {
            Log.d("myTag", "activity mast implement  OnGoodsCountChangeListener");
        }
    }

    public GoodsAdapter(Activity activity, ArrayList<GoodsModel> models) {
        this.activity = (MainActivity) activity;
        this.context = this.activity;
        inflater = (LayoutInflater) context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.models.addAll(models);
        spAdapter = new ArrayAdapter<String>(this.context, R.layout.item_spiner, new String[]{"kg", "L.", "pcs"});
        try {
            countListener = (OnGoodsCountChangeListener) this.activity;
        } catch (ClassCastException e) {
            Log.d("myTag", "activity mast implement  OnGoodsCountChangeListener");
        }
    }

    public ArrayList<GoodsModel> getCollection() {
        return models;
    }

    public void addGoodsModel(GoodsModel model) {
        this.models.add(model);
    }

    public void updatePosition(int position, GoodsModel model) throws ArrayIndexOutOfBoundsException {
        models.set(position, model);
    }

    public void removeModel(int position) throws ArrayIndexOutOfBoundsException {
        models.remove(position);
    }

    public void removeModel(GoodsModel model) {
        models.remove(model);
    }

    public void addAllModels(ArrayList<GoodsModel> models) {
        this.models.addAll(models);
        notifyDataSetChanged();
    }

    public void clear() {
        models.clear();
    }

    public void setFound(boolean isFound) {
        this.isFound = isFound;
    }

    public void setFoundPosition(int position) {
        this.foundPosition = position;
    }

    public void setSortFlag(int sortFlag) {
        this.sortFlag = sortFlag;
    }

    @Override
    public int getCount() {
        if (isFound) {
            return 1;
        }
        return models.size();
    }

    @Override
    public GoodsModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) throws ArrayIndexOutOfBoundsException {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       /*Find view for Grid VIew */
        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.item_grid_view_goods_list, null);
        } else {
            v = convertView;
        }

        final int trm;

        if (isFound) {
            trm = foundPosition;
        } else {
            trm = position;
        }


        if (models.size() != 0) {

            final ImageView imvChecked = (ImageView) v.findViewById(R.id.item_gv_imv_checked);
            final FrameLayout flBought = (FrameLayout) v.findViewById(R.id.item_grid_view_fl_bought);


            /*Listener for Layout. When pressed mean bought */
            flBought.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (models.get(trm).isBought()) {
                            checkedCount--;
                            models.get(trm).setBought(false);
                            flBought.setBackgroundColor(Color.TRANSPARENT);
                            imvChecked.setVisibility(View.INVISIBLE);
                            notifyDataSetChanged();

                            if (activity.haveBudget()) {
                                activity.culcBudgetChange(models.get(trm).getPrice(), 1);
                            }

                        } else {
                            if ((activity.haveBudget()) && (models.get(trm).getPrice() > activity.getBudget())) {
                                Toast.makeText(activity, "To big price", Toast.LENGTH_LONG).show();
                                return;
                            }
                            checkedCount++;
                            flBought.setBackgroundColor(context.getResources().getColor(R.color.gv_item_checked_color));
                            imvChecked.setVisibility(View.VISIBLE);
                            models.get(trm).setBought(true);
                            notifyDataSetChanged();
                            if (activity.haveBudget()) {
                                activity.culcBudgetChange(models.get(trm).getPrice(), 0);
                            }
                        }

                        if (sortFlag == 1) {
                            Collections.sort(models, new ComparatorForChecked());
                        }

                    } catch (ArrayIndexOutOfBoundsException e) {
                        notifyDataSetChanged();
                    }
                }

            });

            /*View for GridView*/
            FrameLayout flComments = (FrameLayout) v.findViewById(R.id.item_grid_view_ll_coments);
            TextView tvProductName = (TextView) v.findViewById(R.id.tv_product_name);
            tvProductName.setText(models.get(trm).getName());
            TextView tvNumOfGoods = (TextView) v.findViewById(R.id.tv_num_of_goods);
            tvNumOfGoods.setText(models.get(trm).getQuantity());
            TextView tvUnits = (TextView) v.findViewById(R.id.tv_units);
            tvUnits.setText(models.get(trm).getUnits());
            MySquareImage imvGoodsPhoto = (MySquareImage) v.findViewById(R.id.imv_photo);



            ImageLoader.getInstance().displayImage(models.get(trm).getPhotoUrl(), imvGoodsPhoto, App.displayImageOptionsForGv());


            ImageButton imBtnComments = (ImageButton) v.findViewById(R.id.imbtn_comments);
            ImageButton imBtnDel = (ImageButton) v.findViewById(R.id.imbtn_del);
            TextView tvPrice = (TextView) v.findViewById(R.id.tv_product_price);
            tvPrice.setText(models.get(trm).getPrice() + " " + activity.getBudgetUnits());
            imBtnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showItemMenu(trm,v);
                }
            });

            /*Check is product model have comments. If true - gives comments button visible...*/
            if (!models.get(trm).getComments().isEmpty() && !models.get(trm).getComments().equals("noComments")) {
                flComments.setVisibility(View.VISIBLE);
                imBtnComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("Comments");
                        dialog.setMessage(models.get(trm).getComments()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                });
            } else {
                flComments.setVisibility(View.INVISIBLE);
            }


            /*Check is product bought*/
            if (models.get(trm).isBought()) {
                flBought.setBackgroundColor(context.getResources().getColor(R.color.gv_item_checked_color));
                imvChecked.setVisibility(View.VISIBLE);
            } else {
                flBought.setBackgroundColor(Color.TRANSPARENT);
                imvChecked.setVisibility(View.INVISIBLE);
            }

            if (activity.haveBudget()) {
                if (models.get(trm).getPrice() > activity.getBudget() && !models.get(trm).isBought()) {
                    flBought.setEnabled(false);
                    flBought.setBackgroundColor(Color.RED);
                } else {
                    flBought.setEnabled(true);
                }
            }
        }
        culckNumOfChecked();
        return v;
    }

    private void culckNumOfChecked() {
        checkedCount = 0;
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).isBought()) {
                checkedCount++;
            }
        }
        countListener.countChange(checkedCount + "/" + models.size());
    }

    private void showEditDialog(final int position) {
        AlertDialog.Builder dialogEdit;


        vdEdit = inflater.inflate(R.layout.item_dialog_add, null);
        edtName = (EditText) vdEdit.findViewById(R.id.dialog_add_edt_name);
        edtName.setText(models.get(position).getName());
        edtNum = (EditText) vdEdit.findViewById(R.id.dialog_add_edt_number_of_goods);
        edtNum.setText(models.get(position).getQuantity());
        edtComments = (EditText) vdEdit.findViewById(R.id.dialog_add_edt_comments);
        String comments = models.get(position).getComments();
        edtComments.setText((comments.equals("noComments") ? "" : comments));
        edtPrice = (EditText) vdEdit.findViewById(R.id.dialog_add_edt_price);
        if (activity.haveBudget()) {
            edtPrice.setEnabled(true);
            edtPrice.setText("" + (models.get(position).getPrice() / Double.parseDouble(models.get(position).getQuantity())));
        }
        spUnits = (Spinner) vdEdit.findViewById(R.id.dialog_add_sp_units);
        spUnits.setAdapter(spAdapter);
        spUnits.setSelection(spAdapter.getPosition(models.get(position).getUnits()));

        spUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spPosition, long id) {


                models.get(position).setUnits((String) parent.getAdapter().getItem(spPosition));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialogEdit = new AlertDialog.Builder(context).setTitle("Edit");
        dialogEdit.setView(vdEdit);
        dialogEdit.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = edtName.getText().toString();
                String num = edtNum.getText().toString();
                String comments = edtComments.getText().toString();

                String price = edtPrice.getText().toString();


                if (!name.isEmpty()) {
                    activity.getSearchAdapter().remove(models.get(position).getName());
                    models.get(position).setName(name);
                    activity.addToSearchAdapter(name);
                    activity.getSearchAdapter().notifyDataSetChanged();
                }
                if (!num.isEmpty()) {
                    models.get(position).setQuantity(num);

                }
                if (!comments.isEmpty()) {
                    models.get(position).setComments(comments);
                }
                if (!price.isEmpty()) {
                    models.get(position).setPrice(Double.parseDouble(price));
                }

                notifyDataSetChanged();

            }
        }).setNegativeButton("Cancel", null);

        dialogEdit.show();
    }

    private void showDelDialog(final int position) {
        AlertDialog.Builder dialogClear = new AlertDialog.Builder(context)

                .setMessage("Remove?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (models.get(position).isBought()) {
                            checkedCount--;
                            if (activity.haveBudget()) {
                                activity.culcBudgetChange(models.get(position).getPrice(), 1);
                            }
                        }
                        activity.getSearchAdapter().remove(models.get(position).getName());
                        models.remove(position);
                        notifyDataSetChanged();
                        activity.getSearchAdapter().notifyDataSetChanged();

                    }

                }).setNegativeButton("No", null);

        dialogClear.show();
    }

    private void showItemMenu(final int position, View view) {

        PopupMenu menu = new PopupMenu(activity, view);
        menu.inflate(R.menu.menu_goods_item);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    // Edit product info
                    case R.id.menu_goods_item_edit:
                        showEditDialog(position);
                        break;

                    //Call Activity with full info about product
                    case R.id.menu_goods_item_open:
                        Intent intent = new Intent(context, InfoActivity.class);
                        intent.putExtra(NAME_KEY, models.get(position).getName());
                        intent.putExtra(NUM_KEY, models.get(position).getQuantity());
                        intent.putExtra(UNITS_KEY, models.get(position).getUnits());
                        intent.putExtra(COMMENTS_KEY, models.get(position).getComments());
                        intent.putExtra(PHOTO_KEY, models.get(position).getPhotoId());
                        intent.putExtra(PRICE_KEY, models.get(position).getPrice() + " " + activity.getBudgetUnits());
                        intent.putExtra(CATEGORY_KEY, models.get(position).getCategory());
                        context.startActivity(intent);
                        break;

                    //remove item from list
                    case R.id.menu_goods_item_del:
                        showDelDialog(position);
                        break;

                }
                return false;
            }
        });

        menu.show();
    }

}
