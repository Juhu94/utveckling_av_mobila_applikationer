package com.example.julian.da345a_mobilia_applikationer_p2;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
/**
 * Class for connection to the server and handle request to the server.
 */
public class ServerConnection {
    private final String TAG = "ServerConnection";
    private final String ip = "195.178.227.53";
    private final int port = 8443;
    private boolean connected;

    private Socket socket;
    private DataInputStream dataInputStream;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    private Controller controller;

    /**
     * Contructor
     * @param controller, takes a reference to the controller class.
     */
    public ServerConnection(Controller controller){
        this.controller = controller;
    }

    /**
     * Method for sending a message to the server.
     * @param message
     */
    private void sendMessage(final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }).start();
    }

    /**
     * Method for getting all of the groups from the server.
     * @throws JSONException
     */
    public void getGroups() throws JSONException {
        JSONObject groups = new JSONObject();
        groups.put("type", "groups");
        String message = groups.toString();
        Log.d(TAG, "Getting groups....\n" +message);

        sendMessage(message);
    }

    /**
     * Method for creating a new group on the server.
     * @param groupName, the name of the group.
     * @param userName, the name of the user creating the group(our user).
     * @throws JSONException
     */
    public void createGroup(String groupName, String userName) throws JSONException {
        JSONObject newGroup = new JSONObject();
        newGroup.put("type", "register");
        newGroup.put("group", groupName);
        newGroup.put("member", userName);
        String message = newGroup.toString();
        Log.d(TAG,"Creating a new group....\n" + message);

        sendMessage(message);
    }

    /**
     * Method for joining an existing group on the server.
     * @param groupName, the name of the group,
     * @param userName, the name of the user that wants to join the group(our user).
     * @throws JSONException
     */
    public void joinGroup(String groupName, String userName) throws JSONException{
        JSONObject newGroup = new JSONObject();
        newGroup.put("type", "register");
        newGroup.put("group", groupName);
        newGroup.put("member", userName);
        String message = newGroup.toString();
        Log.d(TAG,"Joining a group....\n" + message);

        sendMessage(message);
    }

    /**
     * Method for getting all of the members of a given group from the server.
     * @param groupName, the name of the group
     * @throws JSONException
     */
    public void getMembers(String groupName) throws JSONException {
        JSONObject groupMemebers = new JSONObject();
        groupMemebers.put("type", "members");
        groupMemebers.put("group", groupName);
        String message = groupMemebers.toString();
        Log.d(TAG, "Getting group memebers....\n" +message);

        sendMessage(message);
    }

    /**
     * Method for sending a location to the server.
     * @param id, the id of the user that wants to send his/her location.
     * @param latitude, the latitude position.
     * @param longitude, the longitude position.
     * @throws JSONException
     */
    public void sendMyLocation(String id, double latitude, double longitude) throws JSONException {
        JSONObject myLocation = new JSONObject();
        myLocation.put("type", "location");
        myLocation.put("id", id);
        myLocation.put("longitude", String.valueOf(longitude));
        myLocation.put("latitude", String.valueOf(latitude));
        String message = myLocation.toString();
        Log.d(TAG,"Sending my location....\n" +message);

        sendMessage(message);
    }

    /**
     * Method for leaving a group.
     * @param userID, the userID of the user that wants to leave a group(our user).
     * @throws JSONException
     */
    public void leaveGroup(String userID) throws JSONException{
        JSONObject leaveGroup = new JSONObject();
        leaveGroup.put("type", "unregister");
        leaveGroup.put("id", userID);
        String message = leaveGroup.toString();
        Log.d(TAG,"Leaving group....\n" + message);

        sendMessage(message);
    }

    /**
     * Method for connecting to the server via a thread.
     * Class the method "listen" when done.
     */
    public void connect(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d(TAG, "Trying to connect....");
                    socket  = new Socket(ip, port);
                    inputStream = socket.getInputStream();
                    dataInputStream = new DataInputStream(inputStream);
                    outputStream = socket.getOutputStream();
                    dataOutputStream = new DataOutputStream(outputStream);
                    connected = true;

                    //getGroups();
                    //testGroup();
                    listen();
                }catch (IOException e){
                    e.printStackTrace();
                    connected = false;
                }
            }
        }).start();
    }

    /**
     * Method for listening for messages from the server.
     * When a message comes in, the method logs the message and sends it off to the
     * controller class that handles the message.
     *
     * Runs on a thread.
     */
    private void listen(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d(TAG,"Listening....");
                    while(connected){
                        if(dataInputStream.available() > 0){
                            String message = dataInputStream.readUTF();
                            Log.d(TAG, "Message from server....\n" +message);

                            controller.handleMessage(message);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    connected = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
