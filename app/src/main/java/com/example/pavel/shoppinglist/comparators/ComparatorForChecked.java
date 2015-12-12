package com.example.pavel.shoppinglist.comparators;


import com.example.pavel.shoppinglist.models.GoodsModel;

import java.util.Comparator;

public class ComparatorForChecked implements Comparator<GoodsModel> {



    @Override
    public int compare(GoodsModel lhs, GoodsModel rhs) {

        if(lhs.isBought()&&rhs.isBought()){
            return  (int)(lhs.getDate()-rhs.getDate());
        }
        else if(lhs.isBought()&&!rhs.isBought()) {
            return 1;
        }
        else if(!lhs.isBought()&&!rhs.isBought()){
            return  (int)(lhs.getDate()-rhs.getDate());
        }
        else {
            return  -1;
        }

}
}
