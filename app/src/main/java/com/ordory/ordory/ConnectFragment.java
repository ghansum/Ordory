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

import com.utils.Constant;
import com.utils.MyAsynctask;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConnectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConnectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectFragment extends Fragment implements IConnectListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button connectButton;
    private EditText editEmail;
    private EditText editPwd;
    private String email;
    private String password;
    private TextView infoConnectText;
    private Fragment frg = null;
    public SharedPreferences sharedPreference;
    private JSONObject resultJSON;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ConnectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConnectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectFragment newInstance(String param1, String param2) {
        ConnectFragment fragment = new ConnectFragment();
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
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_connect, container, false);

        connectButton = (Button)view.findViewById(R.id.connect_button);

        infoConnectText = (TextView)view.findViewById(R.id.errorConnect);
        editEmail = (EditText) view.findViewById(R.id.email_connect);
        editPwd = (EditText) view.findViewById(R.id.password_connect);

        // on click event
        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {
            @Override
            public void onSuccess(JSONObject json) {
                sharedPreference = getActivity().getSharedPreferences("mySharedPref",0);
                SharedPreferences.Editor editor = sharedPreference.edit();

                try {
                    resultJSON = json.getJSONObject("result");
                    editor.putString("firstName", resultJSON.getString("firstname"));
                    editor.putString("lastName", resultJSON.getString("lastname"));
                    editor.putString("email", resultJSON.getString("email"));
                    editor.putString("userToken", resultJSON.getString("token"));
                    editor.putBoolean("is_connected",true);
                    editor.commit();

                    frg = new ListShoppingListFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.connectFragment, frg);
                    transaction.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed() {
                Log.e("Error_Connection","Erreur lors de la requette HTTP");
                TextView txtView = (TextView)view.findViewById(R.id.errorConnect);
                txtView.setText("Erreur de connection, veuillez réessayer !");
                txtView.setTextColor(getResources().getColor(R.color.colorAccent));

            }
        });

        connectButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        email = editEmail.getText().toString();
                        password = editPwd.getText().toString();
                        String url = Constant.WS_CONNECT_URL+"?email="+email+"&password="+password;
                        asyncTask.execute(url);
                    }
                }
        );
        // Inflate the layout for this fragment
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

    @Override
    public void onSuccess(JSONObject obj) {

    }

    @Override
    public void onFailed() {

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
