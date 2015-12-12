package com.example.pavel.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pavel.shoppinglist.adapters.AddItemAdapter;
import com.example.pavel.shoppinglist.models.CategoryModel;


public class AddItemActivity extends Activity implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    /*Params*/
    /*-views*/
    private GridView gvCategory;
    private EditText edtName, edtNum, edtComments,edtPrice;
    private ImageButton imBtnMenu, imBtnBack;
    private Spinner spUnits;

    /*-adapters*/
    private AddItemAdapter adapter;
    private ArrayAdapter<String> spinerAdapter;

    /*-intents*/
    private Intent intent;

    /*-dialogs*/
    private AlertDialog.Builder addDialog;

    /*-menus*/
    private PopupMenu popupMenu;

    /*Methods*/
    private void fillAdapter() {



        adapter.add(new CategoryModel("Default","assets://category_default.jpg"));
        adapter.add(new CategoryModel("Fruit","assets://category_fruit.jpg"));
        adapter.add(new CategoryModel("Drinks", "assets://category_drinks.jpg"));
        adapter.add(new CategoryModel("Salad", "assets://category_salad.jpg"));
        adapter.add(new CategoryModel("Vegetables", "assets://category_vegetables.jpg"));
        adapter.add(new CategoryModel("Bakery", "assets://category_bakery.jpg"));
        adapter.add(new CategoryModel("Confection", "assets://category_confection.jpg"));
        adapter.add(new CategoryModel("Seafood", "assets://category_seafood.jpg"));
        adapter.add(new CategoryModel("Dairy",  "assets://category_dairy.jpg"));
        adapter.add(new CategoryModel("Meat", "assets://category_meat.jpg"));
        adapter.add(new CategoryModel("Spices", "assets://category_spices.jpg"));
        adapter.add(new CategoryModel("Kitchenware","assets://category_kitchenware.jpg"));
        adapter.add(new CategoryModel("Cleaning", "assets://category_cleaning.jpg"));
        adapter.add(new CategoryModel("Clothe", "assets://category_clothes.jpg"));
        adapter.add(new CategoryModel("Spares", "assets://category_spares.png"));
        adapter.add(new CategoryModel("Medicine", "assets://category_medicine.jpg"));

    }


    /*-activity lifecycle*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        adapter = new AddItemAdapter(this);
        spinerAdapter = new ArrayAdapter<String>(this,R.layout.item_spiner,new String[]{"pcs","kg","L."});

        intent = new Intent();

        gvCategory = (GridView) findViewById(R.id.activity_addGoods_gv_category);
        gvCategory.setOnItemClickListener(this);
        imBtnBack = (ImageButton) findViewById(R.id.activity_addGoods_imbtn_back);
        imBtnBack.setOnClickListener(this);
        imBtnMenu = (ImageButton) findViewById(R.id.activity_addGoods_imbtn_menu);
        imBtnMenu.setOnClickListener(this);
        fillAdapter();

        gvCategory.setAdapter(adapter);

    }

    /*-implemented listeners*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        View v = getLayoutInflater().inflate(R.layout.item_dialog_add, null);

        intent.putExtra(MainActivity.PHOTO_ID_KEY, adapter.getItem(position).getPhotoUrl());
        intent.putExtra(MainActivity.CATEGORY_NAME_KEY, adapter.getItem(position).getName());

        /*-find view elements for dialog*/
        edtName = (EditText) v.findViewById(R.id.dialog_add_edt_name);
        edtNum = (EditText) v.findViewById(R.id.dialog_add_edt_number_of_goods);
        edtNum.setText("1");
        edtComments = (EditText) v.findViewById(R.id.dialog_add_edt_comments);
        edtPrice = (EditText) v.findViewById(R.id.dialog_add_edt_price);
        edtPrice.setHint("pr."+"/"+"item");
        if(getIntent().getBooleanExtra(MainActivity.HAVE_BUDGET_KEY,false)){
            edtPrice.setEnabled(true);
        }
        spUnits = (Spinner) v.findViewById(R.id.dialog_add_sp_units);

        /*-set view stat*/
        spUnits.setAdapter(spinerAdapter);


        spUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra(MainActivity.UNITS_KEY, "" + parent.getAdapter().getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addDialog = new AlertDialog.Builder(this);
        addDialog.setTitle("Set item params");
        addDialog.setView(v);
        addDialog.setPositiveButton("Add", this).setNegativeButton("Cancel", this);
        addDialog.show();

    }

      /*-dialog click listener*/
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                if (!edtName.getText().toString().isEmpty() && !edtNum.getText().toString().isEmpty()) {

                    intent.putExtra(MainActivity.ITEM_NAME_KEY,edtName.getText().toString());
                    intent.putExtra(MainActivity.QUANTITY_KEY,edtNum.getText().toString());
                    intent.putExtra(MainActivity.COMMENTS_KEY,edtComments.getText().toString());

                    if(!edtPrice.getText().toString().isEmpty()){
                        intent.putExtra(MainActivity.PRICE_KEY,Double.parseDouble(edtPrice.getText().toString()));
                    }else {
                        intent.putExtra(MainActivity.PRICE_KEY,0.0);
                    }

                    setResult(RESULT_OK, intent);
                    finish();

                } else {

                    Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                }

                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                break;
        }
    }
      /*-view click listener*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_addGoods_imbtn_menu:

                break;
            case R.id.activity_addGoods_imbtn_back:
                finish();
                break;
        }
    }
      /*-menu click listener*/
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }
}
