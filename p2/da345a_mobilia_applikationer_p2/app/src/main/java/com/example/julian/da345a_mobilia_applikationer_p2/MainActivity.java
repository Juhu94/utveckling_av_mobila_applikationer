package com.example.julian.da345a_mobilia_applikationer_p2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * MainActivity.
 * Have a method for changing the current fragment.
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private FragmentManager mFragmentManager;
    private FusedLocationProviderClient client;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().getFragment(savedInstanceState, "activeFragment");
        }
        else {
            mFragmentManager = getSupportFragmentManager();
            client = LocationServices.getFusedLocationProviderClient(this);
            controller = new Controller(this);
        }
    }

    /**
     * Method for replacing a fragment with another.
     * @param fragment, the new fragment.
     * @param tag, the tag for the new fragment.
     */
    public void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        //transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * Method for retriving a fragment by tag.
     * @param tag, the tag used when to find the fragment.
     * @return return a fragment.
     */
    public Fragment getFragment(String tag) {
        return mFragmentManager.findFragmentByTag(tag);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Fragment activeFragment = mFragmentManager.getFragments().get(0);
        getSupportFragmentManager().putFragment(outState, "activeFragment", activeFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

    }
}
