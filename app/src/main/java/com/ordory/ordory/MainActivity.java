package com.ordory.ordory;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
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

import org.json.JSONException;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RegisterFragment.OnFragmentInteractionListener, ConnectFragment.OnFragmentInteractionListener,
        MyspaceFragment.OnFragmentInteractionListener, ListShoppingListFragment.OnFragmentInteractionListener, ListFormularFragment.OnFragmentInteractionListener ,
                   ShopDetailsFragment.OnFragmentInteractionListener, ProductFormFragment.OnFragmentInteractionListener, EditProductFormFragment.OnFragmentInteractionListener,
                   EditShoppingListFormFragment.OnFragmentInteractionListener{



    private Fragment fragment = null;
    private Button btnview = null;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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

        sharedPreferences = getSharedPreferences("mySharedPref",0);
        editor = sharedPreferences.edit();
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
        boolean statusConnect = sharedPreferences.getBoolean("is_connected",false);
        if(statusConnect){
            navigationView.getMenu().findItem(R.id.nav_userspace).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_products).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_addList).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_subscribe).setVisible(false);
        }else{
            navigationView.getMenu().findItem(R.id.nav_userspace).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_products).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_addList).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_subscribe).setVisible(true);
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
            editor.clear();
            editor.commit();
            fragment = new ConnectFragment();
            // setContentView(R.layout.activity_main);
        } else if (id == R.id.nav_subscribe) {
            fragment = new RegisterFragment();
        }else if (id == R.id.nav_userspace) {
            fragment = new MyspaceFragment();
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
}
