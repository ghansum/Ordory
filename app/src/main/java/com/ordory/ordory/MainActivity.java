package com.ordory.ordory;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RegisterFragment.OnFragmentInteractionListener, ConnectFragment.OnFragmentInteractionListener,
                   BracketFragment.OnFragmentInteractionListener, ListShoppingListFragment.OnFragmentInteractionListener, ListFormularFragment.OnFragmentInteractionListener ,
                   ShopDetailsFragment.OnFragmentInteractionListener, ProductFormFragment.OnFragmentInteractionListener{

    private static final String USER_CONNECTED = null;
    private Button registerBtn;
    private Fragment fragment = null;
    private Button btnview = null;
    public static JSONObject resultJsonConnect;
    public static JSONObject mainObject;
    public static String responseHttp;
    public static Boolean IS_CONNECTED = false;
    MenuItem item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*
        TODO: connect the user and redirect him in another page ff
         */
        btnview =(Button)findViewById(R.id.redirectConnect);
        btnview.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment = new ConnectFragment().newInstance("","");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeFragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        );

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        invalidateOptionsMenu();
        getMenuInflater().inflate(R.menu.main, menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(IS_CONNECTED){
            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(true);
            navigationView.getMenu().getItem(3).setVisible(true);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(true); // because now the new size is 3
        }else{
            navigationView.getMenu().getItem(0).setVisible(true);
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(true);
            navigationView.getMenu().getItem(5).setVisible(false);
        }

        menu.clear();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_login) {
            // Handle the login action
            fragment = new ConnectFragment();
        } else if (id == R.id.nav_products) {
            fragment = new ListShoppingListFragment();
        } else if (id == R.id.nav_addList) {
            fragment = new ListFormularFragment();
        } else if (id == R.id.nav_logout) {
            IS_CONNECTED = false;
            setContentView(R.layout.activity_main);
        } else if (id == R.id.nav_subscribe) {
            fragment = new RegisterFragment();
        }else if (id == R.id.nav_home) {
            setContentView(R.layout.activity_main);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.homeFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //this.editEmail = (EditText) findViewById(R.id.email_connect);
    //editPwd = (EditText) findViewById(R.id.password_connect);

    // get values of email and password
    //String email = editEmail.getText().toString();
    //String password = editPwd.getText().toString();
    //String params = "?email="+email+"&password="+password;

    public static void startRequestHttp(final String urlApi, final String method)
    {

        Runnable runnable = new Runnable() {
            InputStream in;
            URL url;
            String result=null;
            HttpURLConnection conn=null;

            @Override
            public void run() {
                if(method.equals("GET")){
                    try{
                        // get URL content
                        System.out.println("Entree 1....");
                        url = new URL(urlApi);
                        conn = (HttpURLConnection) url.openConnection();
                        System.out.println("Entree Try....");
                        conn.setReadTimeout(5000);
                        conn.setConnectTimeout(5000);
                        conn.setUseCaches(false);
                        conn.setDoInput(true);
                        conn.setDoOutput(false);
                        conn.setRequestMethod(method);
                        conn.setRequestProperty("Content-length", "0");
                        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                        in=conn.getInputStream();
                        System.out.println("Before while....");
                        // open the stream and put it into BufferedReader
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String line;
                        StringBuilder builder = new StringBuilder();
                        while ((line=br.readLine())!= null) {
                            builder.append(line);
                        }
                        result=builder.toString();
                        responseHttp = builder.toString();
                        mainObject = new JSONObject(result);
                        resultJsonConnect = mainObject.getJSONObject("result");
                        System.out.println("Code : "+mainObject.getString("code"));
                        System.out.print("Result : "+result);
                        br.close();
                    }catch(MalformedURLException e) {
                        result=null;
                    } catch (IOException e) {
                        result=null;
                    } catch (Exception e) {
                        result=null;
                    }

                    try {
                        conn.getInputStream().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    conn.disconnect();
                }else{
                    URL url = null;
                    HttpURLConnection conn = null;
                    try {
                        url = new URL(urlApi);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000 /* milliseconds */);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("POST");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); conn.setDoInput(true);
                    String input = null; // Exemple : "{ \"music\": {\"title\": \"Drake\",\"resourceId\": {\"videoId\": \""+videoId+"\",\"kind\": \"youtube#video\"},\"position\": 0}}";
                    OutputStream os = null;
                    try {
                        os = conn.getOutputStream();
                        os.write(input.getBytes("UTF-8"));
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    /*static Thread myThread = new Thread(new Runnable() {


        }); */

}
