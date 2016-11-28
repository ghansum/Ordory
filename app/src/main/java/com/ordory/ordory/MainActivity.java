package com.ordory.ordory;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.models.User;
import com.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

    private Fragment fragment = null;
    private Button btnview = null;

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
        Constant.sharedPref = this.getSharedPreferences("mySharedPref",0);

        //Toast.makeText(MainActivity.this, "Inscription réussi avec succès !", Toast.LENGTH_SHORT).show();

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

    public void saveInfoUser() throws JSONException {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        invalidateOptionsMenu();
        getMenuInflater().inflate(R.menu.main, menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("mySharedPref",0);
        boolean statusConnect = sharedPreferences.getBoolean("is_connected",false);
        if(statusConnect){
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
            Constant.IS_CONNECTED = false;
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


    //String params = "?email="+email+"&password="+password;

    public static void startRequestHttp(final String urlApi, final String method, final String data)
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
                        Constant.responseHttp = builder.toString();
                        Constant.mainObject = new JSONObject(result);
                        Constant.resultJsonConnect = Constant.mainObject.getJSONObject("result");
                        System.out.println("Code : "+Constant.mainObject.getString("code"));
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
                    String input = data; //  "{ \"music\": {\"title\": \"Drake\",\"resourceId\": {\"videoId\": \""+videoId+"\",\"kind\": \"youtube#video\"},\"position\": 0}}";
                    OutputStream os = null;
                    try {
                        os = conn.getOutputStream();
                        os.write(input.getBytes("UTF-8"));
                        os.flush();

                        // Starts the query
                        conn.connect();
                        int response = conn.getResponseCode();
                        Log.e("POST",""+data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

   static Thread threadConnect = new Thread(new Runnable() {
        public void run() {
            Log.e("userConn1","hoho");
            try {
                Constant.userConnected = new User(Constant.resultJsonConnect.getString("firstname"),Constant.resultJsonConnect.getString("lastname"),Constant.resultJsonConnect.getString("email"),Constant.resultJsonConnect.getString("token"));
                Constant.tokenUser = Constant.resultJsonConnect.getString("token");
                SharedPreferences.Editor editor = Constant.sharedPref.edit();
                editor.clear();
                editor.putString("email", Constant.resultJsonConnect.getString("email"));
                editor.putString("firstname", Constant.resultJsonConnect.getString("firstname"));
                editor.putString("lastname", Constant.resultJsonConnect.getString("lastname"));
                editor.putString("token", Constant.resultJsonConnect.getString("token"));
                editor.putBoolean("is_connected",true);
                editor.commit();
                Log.e("userConnectMain",Constant.tokenUser);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
}
