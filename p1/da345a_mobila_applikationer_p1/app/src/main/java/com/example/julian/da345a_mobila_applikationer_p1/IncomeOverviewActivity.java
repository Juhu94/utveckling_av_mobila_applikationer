package com.example.julian.da345a_mobila_applikationer_p1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Activity for showing a list of incomes
 * The user can also choose to se a list of income of a given date interval.
 * The date interval is also chosen in this activity.
 */
public class IncomeOverviewActivity extends AppCompatActivity {

    private String user ="";

    private TextView tvUser;
    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnUpdate;
    //private TextView tvBalance;
    private ListView lvIncomeList;
    private ListAdapter mListAdapter;

    private DataBaseHelper dbHelper;
    ArrayList<InkomstModel> mIncomeList = new ArrayList<InkomstModel>();
    private DatePickerDialog dpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_overview);

        Intent intent = getIntent();
        user = intent.getStringExtra("User");
        dbHelper = new DataBaseHelper(getApplicationContext());

        tvUser = findViewById(R.id.tvIncomeOverviewUser);
        etStartDate = findViewById(R.id.etIncomeStartDate);
        etEndDate = findViewById(R.id.etIncomeEndDate);
        btnUpdate = findViewById(R.id.btnIncomeOverviewUpdate);
        //tvBalance = findViewById(R.id.tvIncomeOverviewBalance);
        lvIncomeList = findViewById(R.id.lvIncomeOverviewList);

        //Hämta inkomster från databas
        getCompleteIncomeList();

        double incomeSum = dbHelper.getIncomeSum();
        double expensesSum = dbHelper.getExpensesSum();

        double sum = incomeSum - expensesSum;

        tvUser.setText(user + "\nBalans: " + String.valueOf(sum) + ":-");

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                dpDialog = new DatePickerDialog(IncomeOverviewActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfyear, int dayOfMonth) {
                        if(dayOfMonth < 10 && (monthOfyear+1) < 10){
                            etStartDate.setText(year + "-0" + (monthOfyear+1) + "-0" + dayOfMonth);
                        }
                        else if(dayOfMonth < 10){
                            etStartDate.setText(year + "-" + (monthOfyear+1) + "-0" + dayOfMonth);
                        }
                        else if((monthOfyear+1) < 10){
                            etStartDate.setText(year + "-0" + (monthOfyear+1) + "-" + dayOfMonth);
                        }
                        else{
                            etStartDate.setText(year + "-" + (monthOfyear+1) + "-" + dayOfMonth);
                        }
                    }
                },mYear, mMonth, mDay);
                dpDialog.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                dpDialog = new DatePickerDialog(IncomeOverviewActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfyear, int dayOfMonth) {
                        if(dayOfMonth < 10 && (monthOfyear+1) < 10){
                            etEndDate.setText(year + "-0" + (monthOfyear+1) + "-0" + dayOfMonth);
                        }
                        else if(dayOfMonth < 10){
                            etEndDate.setText(year + "-" + (monthOfyear+1) + "-0" + dayOfMonth);
                        }
                        else if((monthOfyear+1) < 10){
                            etEndDate.setText(year + "-0" + (monthOfyear+1) + "-" + dayOfMonth);
                        }
                        else{
                            etEndDate.setText(year + "-" + (monthOfyear+1) + "-" + dayOfMonth);
                        }
                    }
                },mYear, mMonth, mDay);
                dpDialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etStartDate.getText().toString().equals("") || etEndDate.getText().toString().equals("")){
                    Toast.makeText(IncomeOverviewActivity.this, "Hämtar alla inkomster", Toast.LENGTH_SHORT).show();
                    getCompleteIncomeList();
                    etStartDate.setText("");
                    etEndDate.setText("");
                }else{
                    Toast.makeText(IncomeOverviewActivity.this, "Hämtar inkomster för ett intervall", Toast.LENGTH_SHORT).show();
                    updateIncomeList();
                    etStartDate.setText("");
                    etEndDate.setText("");
                }

            }
        });

        lvIncomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DetailedViewActivity.class);
                intent.putExtra("user", tvUser.getText().toString());
                intent.putExtra("titel", mIncomeList.get(position).mTitel);
                intent.putExtra("summa", mIncomeList.get(position).mBelopp);
                intent.putExtra("datum", mIncomeList.get(position).mDatum);
                intent.putExtra("kategori", mIncomeList.get(position).mKategori);
                intent.putExtra("typ", "Inkomst");
                startActivity(intent);
            }
        });
    }

    /**
     * Method for showing a list of incomes from a given date interval.
     */
    private void updateIncomeList(){
        if(mIncomeList.size() != 0){
            mIncomeList.clear();
            mListAdapter.notifyDataSetChanged();
        }

        String startDate = etStartDate.getText().toString();
        String endDate = etEndDate.getText().toString();

        mIncomeList = dbHelper.readInkomstBetweenDates(startDate,endDate);

        if(mIncomeList.size() == 0){
            Toast.makeText(this, "Listan är tom", Toast.LENGTH_SHORT).show();
        }

        mListAdapter = new ListAdapter(mIncomeList, this);
        lvIncomeList.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();
    }

    /**
     * Method for reading all of the income entries in the database.
     */
    private void getCompleteIncomeList(){
        if(mIncomeList.size() != 0){
            mIncomeList.clear();
            mListAdapter.notifyDataSetChanged();
        }

        mIncomeList = dbHelper.readInkomst();

        if(mIncomeList.size() == 0){
            Toast.makeText(this, "Listan är tom", Toast.LENGTH_SHORT).show();
        }

        mListAdapter = new ListAdapter(mIncomeList, this);
        lvIncomeList.setAdapter(mListAdapter);
    }

    /**
     * Inne class that extends BaseAdapter for customizing the view of the ListView.
     */
    class ListAdapter extends BaseAdapter {
        TextView mIncomeModelText;
        ArrayList<InkomstModel> mIncomeList;
        Context context;

        public ListAdapter(ArrayList<InkomstModel> mIncomeList, Context context){
            this.mIncomeList = mIncomeList;
            this.context = context;

        }
        @Override
        public int getCount() {
            return mIncomeList.size();
        }

        @Override
        public Object getItem(int position) {
            return mIncomeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.list_income_row, parent, false);
            }
            mIncomeModelText = (TextView)convertView.findViewById(R.id.tvIncomeRow);
            mIncomeModelText.setText("Titel: " + mIncomeList.get(position).mTitel + " - Datum: " + mIncomeList.get(position).mDatum);
            return convertView;
        }
    }
}
