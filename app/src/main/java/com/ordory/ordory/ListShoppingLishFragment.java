package com.ordory.ordory;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListShoppingLishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListShoppingLishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListShoppingLishFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Object MODE_PRIVATE = "MODE_PRIVATE";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView productListView;

    private Button buttonAddShoppingList;

    private Fragment fragment;

    private FragmentTransaction transaction;

    public ListShoppingLishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListShoppingLishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListShoppingLishFragment newInstance(String param1, String param2) {
        ListShoppingLishFragment fragment = new ListShoppingLishFragment();
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

        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_products, container, false);
        productListView = (ListView) view.findViewById(R.id.product_list_view);
        buttonAddShoppingList = (Button) view.findViewById(R.id.button_add_shopping_list);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1);
        listAdapter.add("Anniversaire de Dany");
        listAdapter.add("Repas du week-end");
        listAdapter.add("Shopping enfant");

        productListView.setAdapter(listAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
                ArrayAdapter<String> newAdapter = (ArrayAdapter) arg0.getAdapter();
                String value = (String) productListView.getItemAtPosition(position);
                Toast.makeText(arg0.getContext(), value, Toast.LENGTH_LONG).show();

                 Fragment fragment = new ShopDetailsFragment();
                 FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                 fragmentTransaction.add(R.id.fragment_products, fragment).addToBackStack(null).commit();



            }
        });

        buttonAddShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListFormularFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_products, fragment).addToBackStack(null).commit();
            }
        });



        return view;
      // return inflater.inflate(R.layout.fragment_products, container, false);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }
}
