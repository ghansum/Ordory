package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.holder.ShoppingListViewHolder;
import com.models.ShoppingList;
import com.ordory.ordory.R;

import java.util.List;

/**
 * Created by Patri on 17/11/2016.
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingList>{

    public ShoppingListAdapter(Context context, List<ShoppingList> shoppingLists){
        super(context, 0, shoppingLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_row_view, parent, false);
        }

        ShoppingListViewHolder viewHolder = (ShoppingListViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new ShoppingListViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.shopping_list_name);
            viewHolder.createdDate = (TextView) convertView.findViewById(R.id.shopping_list_createdDate);
            //viewHolder.quantity = (TextView) convertView.findViewById(R.id.productQuantity);

            convertView.setTag(viewHolder);
        }

        ShoppingList shoppingList = getItem(position);

        viewHolder.name.setText(shoppingList.getName());
        viewHolder.createdDate.setText("Date de création : "+shoppingList.getCreated_date());

        return convertView;
    }
}
