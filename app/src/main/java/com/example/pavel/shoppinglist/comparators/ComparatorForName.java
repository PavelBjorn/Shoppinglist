package com.example.pavel.shoppinglist.comparators;


import com.example.pavel.shoppinglist.models.GoodsModel;

import java.util.Comparator;

public class ComparatorForName implements Comparator<GoodsModel> {



    @Override
    public int compare(GoodsModel lhs, GoodsModel rhs) {
        String lhsName =lhs.getName();
        String rhsName = rhs.getName();

        int lnsNum=0;
        int rhsNum=0;

        for(int i =0;i<(lhsName.length()<rhsName.length()?lhsName.length():rhsName.length());i++){
            lnsNum+=lhsName.charAt(i);
            rhsNum+=rhsName.charAt(i);
        }

        return lnsNum-rhsNum;
    }

}
