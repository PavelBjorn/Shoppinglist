package com.example.pavel.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pavel.shoppinglist.adapters.GoodsAdapter;


public class InfoActivity extends Activity implements View.OnClickListener {


    private TextView tvName, tvNum, tvPrice, tvComments, tvCategory;
    private ImageButton imBtnBack;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        Intent intent = getIntent();


        tvName = (TextView) findViewById(R.id.dialog_info_tv_name);
        tvNum = (TextView) findViewById(R.id.dialog_info_tv_number_of_goods);
        tvPrice = (TextView) findViewById(R.id.dialog_info_tv_price);
        tvComments = (TextView) findViewById(R.id.dialog_info_tv_comments);
        tvCategory = (TextView) findViewById(R.id.dialog_info_tv_category);
        imBtnBack =(ImageButton) findViewById(R.id.dialog_info_imbtn_back);

        tvName.setText(intent.getStringExtra(GoodsAdapter.NAME_KEY));
        tvNum.setText(intent.getStringExtra(GoodsAdapter.NUM_KEY) + " " + intent.getStringExtra(GoodsAdapter.UNITS_KEY));
        tvPrice.setText("" + intent.getStringExtra(GoodsAdapter.PRICE_KEY));
        tvComments.setText("" + (intent.getStringExtra(GoodsAdapter.COMMENTS_KEY).equals("noComments")
                ? "" : intent.getStringExtra(GoodsAdapter.COMMENTS_KEY)));
        tvCategory.setText(intent.getStringExtra(GoodsAdapter.CATEGORY_KEY));
        imBtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
