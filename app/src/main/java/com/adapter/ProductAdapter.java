package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.holder.ProductViewHolder;
import com.models.Product;
import com.ordory.ordory.R;

import java.util.List;

/**
 * Created by Patri on 17/11/2016.
 */
public class ProductAdapter extends ArrayAdapter<Product>{

    public ProductAdapter(Context context, List<Product> products){
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_row_view, parent, false);
        }

        ProductViewHolder viewHolder = (ProductViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new ProductViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.productPrice);
            //viewHolder.quantity = (TextView) convertView.findViewById(R.id.productQuantity);

            convertView.setTag(viewHolder);
        }

        Product product = getItem(position);

        viewHolder.name.setText(product.getQuantity()+"x "+product.getName());
        viewHolder.price.setText("Prix unitaire : "+product.getPrice()+" €");

        return convertView;
    }
}
