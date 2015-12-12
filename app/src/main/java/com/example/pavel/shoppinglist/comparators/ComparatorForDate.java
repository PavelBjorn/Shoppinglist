package com.example.pavel.shoppinglist.comparators;


import com.example.pavel.shoppinglist.models.GoodsModel;

import java.util.Comparator;

public class ComparatorForDate implements Comparator<GoodsModel> {



    @Override
    public int compare(GoodsModel lhs, GoodsModel rhs) {
        if(lhs.getDate()>rhs.getDate()){
            return 1;
        }else {
            return -1 ;
        }

    }
}
