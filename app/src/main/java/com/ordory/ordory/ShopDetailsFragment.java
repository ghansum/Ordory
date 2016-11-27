package com.ordory.ordory;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.ProductAdapter;
import com.holder.ProductViewHolder;
import com.models.Product;
import com.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listProductsView;
    private Button addProductButton;

    public ShopDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopDetailsFragment newInstance(String param1, String param2) {
        ShopDetailsFragment fragment = new ShopDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_details, container, false);

        listProductsView = (ListView) view.findViewById(R.id.product_list_view);
        addProductButton = (Button) view.findViewById(R.id.button_add_product);
        TextView title = (TextView)view.findViewById(R.id.titleListProduct);
        title.setText(Constant.listSelected);
        String url = null;
        String name;
        JSONObject tmpObj;
        int id, qty;
        double price;
        List<Product> products = new ArrayList<Product>();
        if(Constant.IS_CONNECTED){
            url = Constant.WS_LIST_PRODUCT_URL+"?token="+Constant.tokenUser+"&shopping_list_id="+Constant.idList;
            MainActivity.startRequestHttp(url,"GET","");

            try {
                JSONArray lisProducts = Constant.mainObject.getJSONArray("result");
                System.out.println("Data Product : "+ lisProducts);
                for (int i = 0; i < lisProducts.length(); i++) {
                    tmpObj = lisProducts.getJSONObject(i);
                    id = Integer.parseInt(tmpObj.getString("id"));
                    price = Double.parseDouble(tmpObj.getString("price"));
                    qty = Integer.parseInt(tmpObj.getString("quantity"));
                    name = tmpObj.getString("name");
                    products.add(new Product(id, name, qty, price));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ProductAdapter productAdapter = new ProductAdapter(this.getActivity(), products);

        listProductsView.setAdapter(productAdapter);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProductFormFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_shoppingList, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
