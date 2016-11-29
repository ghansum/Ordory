package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.holder.ShoppingListViewHolder;
import com.models.ShoppingList;
import com.ordory.ordory.IConnectListner;
import com.ordory.ordory.MainActivity;
import com.ordory.ordory.R;
import com.utils.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            viewHolder.editSLButton = (Button) convertView.findViewById(R.id.button_edit_SL);
            viewHolder.deleteSLButton = (Button) convertView.findViewById(R.id.button_delete_SL);

            convertView.setTag(viewHolder);
        }

        final ShoppingList shoppingList = getItem(position);

        viewHolder.name.setText(shoppingList.getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");

        viewHolder.createdDate.setText("Date de création : "+"\n"+dateFormat.format(shoppingList.getCreated_date()));

       viewHolder.editSLButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO renvoyer vers la page d'édition
            }
        });

        viewHolder.deleteSLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(Constant.tokenUser!=null && shoppingList.getId()>=0) {
                    String url = Constant.WS_REMOVE_SHOPPINGLIST_URL + "?token=" + Constant.tokenUser + "&id=" + shoppingList.getId();
                    MainActivity.startRequestHttp(url, "GET", "");

                    //TODO Ajouter le refresh de page

                }*/
            }
        });

        return convertView;
    }
}
