package com.example.julian.da345a_mobila_applikationer_p1;


import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Fragment for adding an income to the database.
 * Uses the "DatabaseHelper" class for storing in the database.
 * When done, it finishes the activity responsible for this fragment, and the app returns
 * to the main menu.
 */
public class AddIncomeFragment extends Fragment {

    private TextView tvUser;
    private String user = "";
    private RadioGroup rg;
    private RadioButton selectedRb;
    private Button btnAdd;
    private EditText etDate;
    private EditText etTitel;
    private EditText etBelopp;
    private DatePickerDialog dpDialog;
    private DataBaseHelper dbHelper;

    public AddIncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_income, container, false);
        dbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        Bundle bundle = this.getArguments();
        user = bundle.getString("User");
        initComp(view);

        if(savedInstanceState != null){
            Log.d("HEJ HEJ", savedInstanceState.getString("etDate"));
            etDate.setText(savedInstanceState.getString("etDate"));
            etTitel.setText(savedInstanceState.getString("etTitel"));
            etBelopp.setText(savedInstanceState.getString("etBelopp"));
            rg.check(bundle.getInt("rg"));
        }

        tvUser.setText("   " + user);


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                dpDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfyear, int dayOfMonth) {
                        if(dayOfMonth < 10 && (monthOfyear+1) < 10){
                            etDate.setText(year + "-0" + (monthOfyear+1) + "-0" + dayOfMonth);
                        }
                        else if(dayOfMonth < 10){
                            etDate.setText(year + "-" + (monthOfyear+1) + "-0" + dayOfMonth);
                        }
                        else if((monthOfyear+1) < 10){
                            etDate.setText(year + "-0" + (monthOfyear+1) + "-" + dayOfMonth);
                        }
                        else{
                            etDate.setText(year + "-" + (monthOfyear+1) + "-" + dayOfMonth);
                        }
                    }
                },mYear, mMonth, mDay);
                dpDialog.show();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rg.getCheckedRadioButtonId() == -1 || etTitel.getText().toString().equals("") || etBelopp.getText().toString().equals("") || etDate.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Fyll i alla fält", Toast.LENGTH_SHORT).show();
                }
                else{
                    int selectedId = rg.getCheckedRadioButtonId();
                    selectedRb = view.findViewById(selectedId);
                    String formattedString =
                                "Titel: " + etTitel.getText().toString()
                            +"\nKategori: " + selectedRb.getText().toString()
                            +"\nBelopp: " + etBelopp.getText().toString()
                            +"\nDatum: " + etDate.getText().toString();
                    Log.d("INKOMST FRAGMENT", formattedString);
                    Toast.makeText(getActivity().getApplicationContext(), formattedString, Toast.LENGTH_SHORT).show();
                    dbHelper.createInkomst(etTitel.getText().toString(), selectedRb.getText().toString(), etBelopp.getText().toString(), etDate.getText().toString());
                    getActivity().finish();
                }
            }
        });



        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("SAVE SAVE", "SAVE");
        outState.putString("etDate", etDate.getText().toString());
        outState.putString("etTitel", etTitel.getText().toString());
        outState.putString("etBelopp", etBelopp.getText().toString());
        outState.putInt("rg", rg.getCheckedRadioButtonId());
        super.onSaveInstanceState(outState);
    }

    private void initComp(View view) {
        tvUser = view.findViewById(R.id.tvInkomstUser);
        rg = view.findViewById(R.id.rbInkomstKategoriKnappar);
        btnAdd = view.findViewById(R.id.btnInkomstAdd);
        etDate = view.findViewById(R.id.etInkomstDatum);
        etTitel = view.findViewById(R.id.etInkomstTitel);
        etBelopp = view.findViewById(R.id.etInkomstBelopp);
    }

}
