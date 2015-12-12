package com.example.pavel.shoppinglist.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pavel.shoppinglist.MainActivity;
import com.example.pavel.shoppinglist.R;
import com.example.pavel.shoppinglist.data.PreferencesManager;
import com.example.pavel.shoppinglist.fragments.GoodsListFragment;
import com.example.pavel.shoppinglist.listeners.OnSearchListener;
import com.example.pavel.shoppinglist.models.SavedListModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SavedListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SavedListModel> models = new ArrayList<>();
    private MainActivity activity;
    private boolean isFound = false;
    private int foundPosition;


    public SavedListAdapter(Activity activity) {
        this.activity = (MainActivity) activity;
        this.context = this.activity;
        inflater = (LayoutInflater) context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);

    }

    public SavedListAdapter(Context context, ArrayList<SavedListModel> models) {

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.models.addAll(models);

    }

    public void addList(SavedListModel savedListModel) {
        this.models.add(savedListModel);
        notifyDataSetChanged();
    }

    public void clear() {
        models.clear();
        notifyDataSetChanged();
    }

    public void setFound(boolean isFound) {
        this.isFound = isFound;
        notifyDataSetChanged();
    }

    public void setFoundPosition(int position) {
        this.foundPosition = position;
    }

    public SavedListModel getObject(String name) {

        for (int i = 0; i < models.size(); i++) {
            if (name.equals(models.get(i).getName())) {
                return models.get(i);
            }
        }

        return null;

    }

    public ArrayList <SavedListModel> getModels() {
        return models;
    }

    @Override
    public int getCount() {
        if (isFound) {
            return 1;
        }
        return models.size();
    }

    @Override
    public SavedListModel getItem(int position) throws ArrayIndexOutOfBoundsException {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View v;
        if (convertView != null) {
            v = convertView;
        } else {
            v = inflater.inflate(R.layout.item_lv_save_list, null);
        }


        final int trm;

        if (isFound) {
            trm = foundPosition;
        } else {
            trm = position;
        }

        TextView tvFileName = (TextView) v.findViewById(R.id.item_lv_save_list_tv_name);


        TextView tvDate = (TextView) v.findViewById(R.id.item_lv_save_list_tv_date);
        TextView tvBudget = (TextView) v.findViewById(R.id.item_lv_save_list_tv_budget);
        TextView tvCont = (TextView) v.findViewById(R.id.item_lv_save_list_tv_count);


        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        String date = stf.format(new Date(models.get(trm).getDate())) + ", " + sdf.format(new Date(models.get(trm).getDate()));


        tvCont.setText(models.get(trm).getGoodsCount().replace("_", "/"));
        tvDate.setText(date);
        tvFileName.setText(models.get(trm).getName());


        String budget = "";
        if (models.get(trm).haveBudget()) {
            budget = String.format("%.2f", models.get(trm).getBudget()) + " " + models.get(trm).getUnits();
        } else {
            budget = "No budget";
        }
        tvBudget.setText(budget);


        ImageButton imBtnDel = (ImageButton) v.findViewById(R.id.item_lv_save_list_imbtn_del);
        imBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelDialog(trm);
            }
        });

        ImageButton imBtnPrice = (ImageButton) v.findViewById(R.id.item_lv_save_list_imbtn_price);

        imBtnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder secondaryDial;

                View vdEdit = inflater.inflate(R.layout.item_dialog_budget, null);

                final EditText edtPrice = (EditText) vdEdit.findViewById(R.id.dialog_price_edt_price);


                edtPrice.setHint(String.format("%.2f", models.get(trm).getBudget()) + "");

                secondaryDial = new AlertDialog.Builder(context).setTitle("Set budget");

                secondaryDial.setView(vdEdit);

                secondaryDial.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String price = edtPrice.getText().toString();

                        if (!price.isEmpty()) {
                            models.get(trm).setBudget(Float.parseFloat(price));
                            models.get(trm).setHaveBudget(true);

                            try {
                                JSONArray jsonArray = PreferencesManager.loadObjectInJSONArray(activity, MainActivity.SHOPPING_LISTS_PREFERENCES_NAME);
                                jsonArray.getJSONObject(trm).put(MainActivity.BUDGET_KEY, models.get(trm).getBudget());
                                jsonArray.getJSONObject(trm).put(MainActivity.HAVE_BUDGET_KEY, models.get(trm).haveBudget());
                                Log.d("focus", jsonArray.toString());
                                PreferencesManager.saveData(activity, MainActivity.SHOPPING_LISTS_PREFERENCES_NAME, jsonArray.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                secondaryDial.show();
            }
        });

        LinearLayout llLvItem = (LinearLayout) v.findViewById(R.id.item_lv_save_ll_files);
        llLvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SavedListModel svm = models.get(trm);

                activity.setBudget(svm.getBudget());
                activity.setListName(svm.getName());
                activity.setGoodsCount(svm.getGoodsCount());
                activity.setBudgetUnits(svm.getUnits());
                activity.setHaveBudget(svm.haveBudget());
                activity.replaceFragment(new GoodsListFragment(), R.id.main_activity_gl_fragment_content, true);

            }
        });

        return v;
    }

    private void showDelDialog(final int position) {

        AlertDialog.Builder dialogClear = new AlertDialog.Builder(context)
                .setMessage("Remove?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PreferencesManager.delFile(activity, models.get(position).getName());
                        models.remove(position);
                        notifyDataSetChanged();
                        PreferencesManager.saveData(activity,
                                MainActivity.SHOPPING_LISTS_PREFERENCES_NAME
                                , MainActivity.toJSONArray(models));

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialogClear.show();
    }

}
