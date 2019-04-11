package com.example.julian.da345a_mobilia_applikationer_p2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Fragment for showing a more detailed view of a given group.
 * Members of the group and group name.
 */
public class DetailedGroupFragment extends Fragment {
    private TextView tv_groupName;
    private TextView tv_activeGroup;
    private ListView lv_memberList;
    private Button btn_join;
    private Button btn_back;

    private Controller controller;
    private String groupName;

    private ArrayList<String> memberList;
    private boolean alreadyInAGroup;

    public DetailedGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed_group, container, false);

        tv_groupName = view.findViewById(R.id.tv_detailed_groupname);
        tv_activeGroup = view.findViewById(R.id.tv_detailed_activegroup);
        lv_memberList = view.findViewById(R.id.lv_detailed_memberlist);
        btn_join = view.findViewById(R.id.btn_join_group);
        btn_back = view.findViewById(R.id.btn_detailedGroup_back);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            groupName = bundle.getString("groupName");
            tv_groupName.setText(groupName);
            memberList = bundle.getStringArrayList("groupMembers");

            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, memberList);
            lv_memberList.setAdapter(adapter);

            alreadyInAGroup = bundle.getBoolean("bool");

            if(alreadyInAGroup){
                btn_join.setEnabled(false);
            }
            else{
                btn_join.setEnabled(true);
            }
        }

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.joinGroup(groupName);
                Toast.makeText(getActivity(), "You joined: " + groupName, Toast.LENGTH_SHORT).show();
                btn_join.setEnabled(false);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.goToMainMenu();
            }
        });

        return view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}
