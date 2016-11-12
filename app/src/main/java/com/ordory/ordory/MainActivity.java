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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.utils.Constant;
import com.utils.MyRunnable;

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
                   BracketFragment.OnFragmentInteractionListener, ListShoppingLishFragment.OnFragmentInteractionListener {

    private Button registerBtn;
    Fragment fragment = null;
    Button btnview = null;
    public static JSONObject resultJsonConnect;
    public static JSONObject mainObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //myThread.start();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*
        TODO: connect the user and redirect him in another page
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
            fragment = new ListShoppingLishFragment();
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

    //this.editEmail = (EditText) findViewById(R.id.email_connect);
    //editPwd = (EditText) findViewById(R.id.password_connect);

    // get values of email and password
    //String email = editEmail.getText().toString();
    //String password = editPwd.getText().toString();
    //String params = "?email="+email+"&password="+password;
    public static Thread myThread = new Thread(new Runnable() {
        public void run() {
            String result=null;
            InputStream in=null;
            URL url;
            HttpsURLConnection conn=null;

            try{
                // get URL content
                String test = "https://appspaces.fr/esgi/shopping_list/account/login.php?email=toto@gmail.com&password=azerty";
                url = new URL(test);
                conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-length", "0");
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                in=conn.getInputStream();
                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line=br.readLine())!= null) {
                    builder.append(line);
                }
                result=builder.toString();
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
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
            System.out.println(result);
        }
    });
}
