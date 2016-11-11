package com.ordory.ordory;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
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

import com.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RegisterFragment.OnFragmentInteractionListener, ConnectFragment.OnFragmentInteractionListener,
                   BracketFragment.OnFragmentInteractionListener, ProductsFragment.OnFragmentInteractionListener {

    private Button registerBtn;
    Fragment fragment = null;
    Button btnview = null;
    private String resultConnectJson = null;
    public String email = "toto@gmail.com";
    public String password = "azerty";
    JSONObject mainObject;
    JSONObject resultJsonConnect;


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
        TODO: connect the user and redirect him in another page
         */
        btnview =(Button)findViewById(R.id.connect_button);
        btnview.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment = new ConnectFragment().newInstance("","");

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeFragment, fragment);
                    Log.e("click","click sur le bouton");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        );

        threadConnect.start();
        //JSONObject codeObject = null;
        /*try {
            resultJsonConnect = mainObject.getJSONObject("result");
            //codeObject = mainObject.getJSONObject("code");
            //Log.e("Code",codeObject.getString("code"));
            Log.e("LastName",resultJsonConnect.getString("lastname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

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
        getMenuInflater().inflate(R.menu.main, menu);
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
            fragment = new ProductsFragment();
        } else if (id == R.id.nav_bracket) {
            fragment = new BracketFragment();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {

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

    Thread threadConnect = new Thread(new Runnable() {
        public void run() {
            // your logic
            String result=null;
            InputStream in=null;
            URL url;
            HttpsURLConnection conn=null;
            try{
                System.out.println("Entree dans la methode readTwitter");
                // get URL content
                url = new URL(Constant.WS_CONNECT_URL+"?email="+email+"&password="+password);
                conn = (HttpsURLConnection) url.openConnection();

                //System.setProperty("http.keepAlive", "false");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-length", "0");
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                in=conn.getInputStream(); // slowest part so far, several seconds spent there
                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line=br.readLine())!= null) {
                    builder.append(line);
                }

                result=builder.toString();
                mainObject = new JSONObject(result);
                System.out.print(result);
                br.close();

            }catch(MalformedURLException e) {
                result=null;
            } catch (IOException e) {
                result=null;
            } catch (Exception e) {
                result=null;
            }

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
            System.out.println(result);
        }
    });
}
