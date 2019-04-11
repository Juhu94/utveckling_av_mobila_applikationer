package com.example.julian.da345a_mobilia_applikationer_p2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Fragment for login in to the application.
 * Takes a name thats used to display the user for the other users.
 */
public class LoginFragment extends Fragment {
    private final String TAG = "LoginFragment";
    private Button btnConnect;
    private EditText et_name;
    private String name;

    private Controller controller;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnConnect = view.findViewById(R.id.btn_connect);
        et_name = view.findViewById(R.id.et_loginName);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, et_name.getText().toString());
                if(et_name.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Enter a name", Toast.LENGTH_SHORT).show();
                }
                else{
                    controller.connect(et_name.getText().toString());
                }
            }
        });
        return view;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}
