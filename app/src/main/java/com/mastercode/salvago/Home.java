package com.mastercode.salvago;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;
import com.mastercode.salvago.fragments.Fg_Home_Navigation;
import com.mastercode.salvago.fragments.Fg_Nav_Promos;
import com.mastercode.salvago.tools.AppNavigation;
import com.mastercode.salvago.tools.MySession;
import com.mastercode.salvago.tools.Popups;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Context ctx;
    View headerview;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initvars();

        setSupportActionBar(toolbar);
        setTitle("Inicio");

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(headerview.getWindowToken(), 0);
                super.onDrawerClosed(drawerView);
            }
        };

        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        headerview = navigationView.getHeaderView(0);
        ChangeFragment(MySession.home_fragment);
    }

    public void initvars() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ctx = this;
        getLocation();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            AlertDialog dialog = new Popups(this).CloseApp(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_giftcard){
            AppNavigation.goGiftcards(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_promo:
                ChangeFragment(0);
                break;
            case R.id.nav_restaurant:
                ChangeFragment(1);
                break;
            case R.id.nav_turist:
                ChangeFragment(2);
                break;
            case R.id.nav_hotel:
                ChangeFragment(3);
                break;
            case R.id.nav_store:
                ChangeFragment(4);
                break;
            case R.id.nav_service:
                ChangeFragment(5);
                break;
            case R.id.nav_new:
                AppNavigation.goRegister(this);
                break;
            case R.id.nav_login:
                AppNavigation.goAccess(this);
                break;
            case R.id.nav_bug:
                AppNavigation.goBugReport(this);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ChangeFragment(int index){
        Fragment frag = null;
        FragmentManager manager = getSupportFragmentManager();
        MySession.home_fragment = index;

        Bundle bun = new Bundle();
        bun.putInt("option", index);

        if(index == 0){
            frag = new Fg_Nav_Promos();
        }else if(index > 0){
            frag = new Fg_Home_Navigation();
        }

        frag.setArguments(bun);
        manager.beginTransaction().replace(R.id.fgcontainer,frag).commit();
        drawer.closeDrawer(GravityCompat.START);
    }


    private void getLocation(){
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(check == PackageManager.PERMISSION_GRANTED){
            LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    MySession.location = new LatLng(location.getLatitude(), location.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        }else{
            Log.e("Home", "Sin permiso de ubicacion");
        }
    }


}
