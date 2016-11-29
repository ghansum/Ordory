package com.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.holder.ShoppingListViewHolder;
import com.models.ShoppingList;
import com.ordory.ordory.IConnectListner;
import com.ordory.ordory.ListShoppingListFragment;
import com.ordory.ordory.MainActivity;
import com.ordory.ordory.R;
import com.ordory.ordory.ShopDetailsFragment;
import com.utils.Constant;
import com.utils.MyAsynctask;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Patri on 17/11/2016.
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingList>{

    private SharedPreferences sharedPreferences;

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


        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {
            @Override
            public void onSuccess(JSONObject obj) {
                Fragment fragment = new ListShoppingListFragment();
                FragmentTransaction fragmentTransaction = ((Activity) getContext()).getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_products, fragment).addToBackStack(null).commit();
            }

            @Override
            public void onFailed() {

            }
        });


        viewHolder.editSLButton.setOnClickListener(new View.OnClickListener(){
             @Override
            public void onClick(View v) {
                //TODO renvoyer vers la page d'édition
            }
        });

        viewHolder.deleteSLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String url = Constant.WS_REMOVE_SHOPPINGLIST_URL + "?token=" + Constant.tokenUser + "&id=" + shoppingList.getId();
                sharedPreferences = ((Activity) getContext()).getSharedPreferences("mySharedPref",0);
                String userToken = sharedPreferences.getString("userToken", "");
                if (userToken != null && userToken != "") {
                    String url = Constant.WS_REMOVE_SHOPPINGLIST_URL+"?token="+userToken+"&id="+shoppingList.getId();
                    asyncTask.execute(url);
                }
            }
        });

        return convertView;
    }
}
