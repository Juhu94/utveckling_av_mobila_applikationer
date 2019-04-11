package com.example.julian.da345a_mobilia_applikationer_p2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Controller class for handling most of the logic in the application.
 */
public class Controller {
    private final String TAG = "Controller";
    private MainActivity main;

    private LoginFragment loginFragment;
    private MenuFragment menuFragment;
    private GmapFragment gmapFragment;
    private DetailedGroupFragment detailedGroupFragment;

    private ServerConnection mServerConnection;
    private boolean alreadyInAGrouo = false;
    private boolean connected = false;
    private boolean mapsFragmentActive = false;

    private String username;
    private String userID;

    private ArrayList<String> groupMembers = new ArrayList<>();
    private String groupName = "";
    private String activeGroup = "";

    private FusedLocationProviderClient client;
    private double latitude;
    private double longitude;

    public Controller(MainActivity main) {
        this.main = main;
        client = LocationServices.getFusedLocationProviderClient(main);
        if (connected) {
            menuFragment = new MenuFragment();
            menuFragment.setController(this);
            main.changeFragment(menuFragment, "menu");
        } else {
            loginFragment = new LoginFragment();
            loginFragment.setController(this);
            main.changeFragment(loginFragment, "login");
        }
    }

    /**
     * Method for connecting to the server.
     * Uses the ServerConnection class
     * @param username
     */
    public void connect(String username) {
        this.username = username;

        menuFragment = new MenuFragment();
        menuFragment.setController(this);
        main.changeFragment(menuFragment, "menu");

        mServerConnection = new ServerConnection(this);
        mServerConnection.connect();

        connected = true;
    }

    /**
     * Method for creating a group.
     * Uses the ServerConnection class
     * @param groupName, the name of the group.
     */
    public void createGroup(String groupName) {
        try {
            mServerConnection.createGroup(groupName, username);
            alreadyInAGrouo = true;
            activeGroup = groupName;
            sendMyLocation();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for joining an existing group.
     * Uses the ServerConnection class
     * @param groupName, the name of the group.
     */
    public void joinGroup(String groupName) {
        try {
            mServerConnection.joinGroup(groupName, username);
            if (connected) {
                activeGroup = groupName;
                alreadyInAGrouo = true;
                sendMyLocation();
                menuFragment.updateInfo("Active group: " + groupName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for leaving a group.
     * Uses the ServerConnection class.
     */
    public void leaveGroup() {
        try {
            mServerConnection.leaveGroup(userID);
            alreadyInAGrouo = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for retriving the location of the device and sending it to the server.
     * The method sends the location with a 20 seconds interval.
     * Uses the ServerConnection class.
     */
    public void sendMyLocation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(alreadyInAGrouo){
                        if (ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(main, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.d(TAG, location.toString());
                                if(location != null){
                                    Log.d(TAG, location.getLatitude() + " " + location.getLongitude());

                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        });
                        mServerConnection.sendMyLocation(userID, latitude, longitude);
                        Thread.sleep(20000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Method for asking for the already existing groups from the server.
     * Uses the ServerConnection class.
     */
    public void refreshGroups(){
        try {
            mServerConnection.getGroups();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for retriving the members of a given group.
     * Uses the ServerConnection class.
     * @param groupName, the name of the group.
     */
    public void getMembersInGroup(String groupName){
        try {
            mServerConnection.getMembers(groupName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for going to the main menu.
     */
    public void goToMainMenu(){
        menuFragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activeGroup", activeGroup);
        bundle.putBoolean("alreadyInAGrouo", alreadyInAGrouo);
        menuFragment.setArguments(bundle);
        menuFragment.setController(this);
        main.changeFragment(menuFragment, "menu");
        mapsFragmentActive = false;
        //menuFragment.updateActiveGroupname(activeGroup);
    }

    /**
     * Method for going to the google maps fragment and displaying the group members on the map.
     */
    public void displayGroupOnMap(){
        gmapFragment = new GmapFragment();
        main.changeFragment(gmapFragment, "map");
        gmapFragment.setController(this);
        mapsFragmentActive = true;
    }

    /**
     * Method for showing a more detailed view of the chosen group.
     * @param groupName, the name of the group
     * @param groupMembers, the list containing all of the group members.
     * @param alreadyInAGrouo, boolean to see if the user is already in a group. True = in a group, false = not in group.
     */
    private void showDetailedGroup(String groupName, ArrayList<String> groupMembers, boolean alreadyInAGrouo){
        detailedGroupFragment = new DetailedGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString("groupName", groupName);
        bundle.putStringArrayList("groupMembers", groupMembers);
        bundle.putBoolean("bool", alreadyInAGrouo);
        detailedGroupFragment.setArguments(bundle);
        detailedGroupFragment.setController(this);
        main.changeFragment(detailedGroupFragment, "DetailedGroup");
    }

    /**
     * Method for handing all of the incoming messages from the server.
     * Filters and handles the message.
     * @param message, the JSON message from the sever.
     * @throws JSONException
     */
    public void handleMessage(String message) throws JSONException {
        JSONObject jsonResponse = new JSONObject(message);
        Log.d(TAG, jsonResponse.getString("type"));

        if(jsonResponse.getString("type").equals("groups")){
            int length = jsonResponse.getJSONArray("groups").length();
            if(length > 0){
               ArrayList<String> groupList = new ArrayList<>();
                Log.d(TAG, String.valueOf(length));
                Log.d(TAG, jsonResponse.getJSONArray("groups").getJSONObject(0).get("group").toString());
                for(int i = 0; i < length; i++){
                    groupList.add(jsonResponse.getJSONArray("groups").getJSONObject(i).get("group").toString());
                }

                Log.d(TAG,"GroupList size = " + groupList.size());

                menuFragment.updateGroupList(groupList);
            }
            else{
               //Finns inga grupper, hantera
                Log.d(TAG, "No active groups available");
                menuFragment.updateInfo("No active groups available");
                menuFragment.updateGroupList(new ArrayList<String>());
            }
        }
        else if(jsonResponse.getString("type").equals("register")){
            userID = jsonResponse.getString("id");
        }
        else if(jsonResponse.getString("type").equals("members")){
            groupMembers.clear();
            groupName = "";
            Log.d(TAG,jsonResponse.get("type").toString());
            int length = jsonResponse.getJSONArray("members").length();
            groupName = jsonResponse.getString("group");
            for(int i = 0; i < length; i++){
                groupMembers.add(jsonResponse.getJSONArray("members").getJSONObject(i).get("member").toString());
            }
            showDetailedGroup(groupName, groupMembers, alreadyInAGrouo);
        }
        else if(jsonResponse.getString("type").equals("locations")){
            Log.d(TAG, jsonResponse.getJSONArray("location").toString());

            if(mapsFragmentActive)
                gmapFragment.updateGroupsMarkers(jsonResponse.getJSONArray("location").toString());

        }
    }
}
