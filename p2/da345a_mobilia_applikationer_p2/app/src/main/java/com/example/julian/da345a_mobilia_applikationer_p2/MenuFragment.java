package com.example.julian.da345a_mobilia_applikationer_p2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Menu fragment.
 */
public class MenuFragment extends Fragment {
    private final String TAG = "MenuFragment";

    private Button btn_createGroup;
    private Button btn_RefreshGroups;
    private Button btn_unregisterFromGroup;
    private Button btn_displayGroupOnMap;
    private ListView lv_groupList;
    private TextView activeGroup;
    private ArrayList<String> groupList = new ArrayList<>();
    private Controller controller;
    private String activeGroupName = "";
    boolean alreadyInAGroup = false;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container,false);
        btn_createGroup = view.findViewById(R.id.btn_create_group);
        btn_RefreshGroups = view.findViewById(R.id.btn_refresh_groups);
        btn_unregisterFromGroup = view.findViewById(R.id.btn_unregister);
        btn_displayGroupOnMap = view.findViewById(R.id.btn_display_map);
        lv_groupList = view.findViewById(R.id.lv_groupList);
        activeGroup = view.findViewById(R.id.tv_activeGroup);

        btn_unregisterFromGroup.setEnabled(false);
        btn_displayGroupOnMap.setEnabled(false);

        initListeners();

        if(savedInstanceState != null){
            groupList = savedInstanceState.getStringArrayList("groupList");
            activeGroupName = savedInstanceState.getString("activeGroup");
            activeGroup.setText("Active group: " + activeGroupName);
            alreadyInAGroup = savedInstanceState.getBoolean("alreadyInAGroup");

            if(alreadyInAGroup){
                activeGroup.setText("Active group: " + activeGroupName);
                btn_unregisterFromGroup.setEnabled(true);
                btn_displayGroupOnMap.setEnabled(true);
                btn_createGroup.setEnabled(false);
            }
            else{
                activeGroup.setText("No active group");
                btn_unregisterFromGroup.setEnabled(false);
                btn_displayGroupOnMap.setEnabled(false);
                btn_createGroup.setEnabled(true);
            }
        }
        else{
            Bundle bundle = this.getArguments();
            if(bundle != null){
                activeGroupName = bundle.getString("activeGroup");
                alreadyInAGroup = bundle.getBoolean("alreadyInAGrouo", false);
                if(alreadyInAGroup){
                    activeGroup.setText("Active group: " + activeGroupName);
                    btn_unregisterFromGroup.setEnabled(true);
                    btn_displayGroupOnMap.setEnabled(true);
                    btn_createGroup.setEnabled(false);
                }
                else{
                    activeGroup.setText("No active group");
                    btn_unregisterFromGroup.setEnabled(false);
                    btn_displayGroupOnMap.setEnabled(false);
                    btn_createGroup.setEnabled(true);
                }
            }
        }
        return view;
    }

    private void initListeners(){
        btn_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Skapa ny grupp via controllern?
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View view = getLayoutInflater().inflate(R.layout.input_dialog, null);
                final EditText groupName = view.findViewById(R.id.et_groupName);
                Button createGroup = view.findViewById(R.id.btnCreateGroup);
                mBuilder.setTitle("Creating a new group");
                mBuilder.setMessage("Group name");
                mBuilder.setView(view);
                final AlertDialog dialog = mBuilder.create();
                createGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!groupName.getText().toString().equals("")){
                            Log.d(TAG, groupName.getText().toString());
                            activeGroupName = groupName.getText().toString();
                            controller.createGroup(groupName.getText().toString());
                            activeGroup.setText("Active group: " + groupName.getText().toString());
                            btn_unregisterFromGroup.setEnabled(true);
                            btn_displayGroupOnMap.setEnabled(true);
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();

                btn_createGroup.setEnabled(false);
            }
        });

        btn_RefreshGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.refreshGroups();
            }
        });

        btn_displayGroupOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.displayGroupOnMap();
            }
        });

        btn_unregisterFromGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activeGroupName.equals(""))
                controller.leaveGroup();
                activeGroupName = "";
                activeGroup.setText("No active group");
                btn_unregisterFromGroup.setEnabled(false);
                btn_displayGroupOnMap.setEnabled(false);
                btn_createGroup.setEnabled(true);
            }
        });

        lv_groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.getMembersInGroup(groupList.get(position));
            }
        });
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    /**
     * Method for showing available groups in a ListView.
     * @param list
     */
    public void updateGroupList(final ArrayList<String> list){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //if (list != null) {
                    groupList.clear();
                    groupList = list;
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, groupList);
                    lv_groupList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                //}
            }
        });

    }

    /**
     * Method for updating information for the user.
     * @param message
     */
    public void updateInfo(String message){
        activeGroup.setText(message);
    }

    /**
     * Method for updating the active group text.
     * @param groupName
     */
    public void updateActiveGroupname(String groupName){
        activeGroup.setText("Active group: " + groupName);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "SaveInstanceState");
        outState.putStringArrayList("groupList", groupList);
        outState.putString("activeGroup", activeGroupName);
        outState.putBoolean("alreadyInAGroup", alreadyInAGroup);
        super.onSaveInstanceState(outState);
    }
}
