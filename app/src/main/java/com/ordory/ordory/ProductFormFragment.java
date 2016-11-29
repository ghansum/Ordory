package com.ordory.ordory;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utils.Constant;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFormFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btnCreateProduct;
    private EditText productName_edit;
    private EditText price_edit;
    private EditText quantity_edit;
    private TextView blocInfo;
    private String tokenShared;
    SharedPreferences sharedPreferences;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String productName;
    private String quantity;
    private String price;

    private OnFragmentInteractionListener mListener;



    public ProductFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFormFragment newInstance(String param1, String param2) {
        ProductFormFragment fragment = new ProductFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_form, container, false);

        productName_edit = (EditText) view.findViewById(R.id.productName);
        price_edit = (EditText) view.findViewById(R.id.price);
        quantity_edit = (EditText) view.findViewById(R.id.quantity);
        sharedPreferences = getActivity().getSharedPreferences("mySharedPref",0);
        tokenShared = sharedPreferences.getString("token", null);
        blocInfo = (TextView) view.findViewById(R.id.createProductInfo);

        btnCreateProduct = (Button)view.findViewById(R.id.btn_create_shoplist);
        btnCreateProduct.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                productName = productName_edit.getText().toString();
                price = price_edit.getText().toString();
                quantity = quantity_edit.getText().toString();
               // String url = Constant.WS_CREATE_PRODUCT_URL+"?token="+tokenShared+"&shopping_list_id="+Constant.idList+"&name="+productName+"&quantity="+quantity+"&price="+price;
               /* if(!productName.isEmpty() && !price.isEmpty() && !quantity.isEmpty()){
                    MainActivity.startRequestHttp(url, "GET","");
                    try{
                        if(Constant.mainObject != null && Constant.mainObject.getString("code").equals("0")){
                            System.out.println("Resultat Create Product : "+Constant.mainObject.getJSONArray("result"));
                            //Add registration of user in the application
                            Fragment frg = new ListShoppingListFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_form_shopping_list, frg);
                            transaction.commit();
                        }else{
                            blocInfo.setText("Erreur lors de la creation du produit");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    blocInfo.setText("Erreur, Veuillez remplir les champs du formulaire");
                } */
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
