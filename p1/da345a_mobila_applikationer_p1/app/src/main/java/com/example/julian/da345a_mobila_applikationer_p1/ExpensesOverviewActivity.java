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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Activity for showing a list of expenses.
 * The user can also choose to se all expenses of a given date interval.
 * The date interval is also chosen in this activity.
 */
public class ExpensesOverviewActivity extends AppCompatActivity {
    private String user ="";

    private TextView tvUser;
    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnUpdate;
    //private TextView tvBalance;
    private ListView lvIncomeList;
    private ListAdapter mListAdapter;

    private DataBaseHelper dbHelper;
    ArrayList<UtgiftModel> mExpensesList = new ArrayList<UtgiftModel>();
    private DatePickerDialog dpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_overview);

        Intent intent = getIntent();
        user = intent.getStringExtra("User");
        dbHelper = new DataBaseHelper(getApplicationContext());

        etStartDate = findViewById(R.id.etExpenseStartDate);
        etEndDate = findViewById(R.id.etExpenseEndDate);
        btnUpdate = findViewById(R.id.btnExpenseOverviewUpdate);
        tvUser = findViewById(R.id.tvExpenseOverviewUser);
        //tvBalance = findViewById(R.id.tvIncomeOverviewBalance);
        lvIncomeList = findViewById(R.id.lvExpenseOverviewList);

        //Hämta inkomster från databas
        getCompleteExpensesList();

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
                dpDialog = new DatePickerDialog(ExpensesOverviewActivity.this,new DatePickerDialog.OnDateSetListener() {
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
                dpDialog = new DatePickerDialog(ExpensesOverviewActivity.this,new DatePickerDialog.OnDateSetListener() {
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
                    Toast.makeText(ExpensesOverviewActivity.this, "Hämtar alla utgifter", Toast.LENGTH_SHORT).show();
                     getCompleteExpensesList();
                    etStartDate.setText("");
                    etEndDate.setText("");
                }else{
                    Toast.makeText(ExpensesOverviewActivity.this, "Hämtar utgifter för ett intervall", Toast.LENGTH_SHORT).show();
                    updateExpensesList();
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
                intent.putExtra("titel", mExpensesList.get(position).mTitel);
                intent.putExtra("summa", mExpensesList.get(position).mPris);
                intent.putExtra("datum", mExpensesList.get(position).mDatum);
                intent.putExtra("kategori", mExpensesList.get(position).mKategori);
                intent.putExtra("typ", "Utgift");
                startActivity(intent);
            }
        });
    }

    /**
     * Method for reading a list of expenses from a date interval, from the database.
     */
    private void updateExpensesList(){
        if(mExpensesList.size() != 0){
            mExpensesList.clear();
            mListAdapter.notifyDataSetChanged();
        }

        String startDate = etStartDate.getText().toString();
        String endDate = etEndDate.getText().toString();

        mExpensesList = dbHelper.readUtgiftBetweenDates(startDate,endDate);

        if(mExpensesList.size() == 0){
            Toast.makeText(this, "Listan är tom", Toast.LENGTH_SHORT).show();
        }

        mListAdapter = new ListAdapter(mExpensesList, this);
        lvIncomeList.setAdapter(mListAdapter);
    }

    /**
     * Method for reading the complete expense list from the database.
     */
    private void getCompleteExpensesList(){
        if(mExpensesList.size() != 0){
            mExpensesList.clear();
            mListAdapter.notifyDataSetChanged();
        }

        mExpensesList = dbHelper.readUtgift();

        if(mExpensesList.size() == 0){
            Toast.makeText(this, "Listan är tom", Toast.LENGTH_SHORT).show();
        }

        mListAdapter = new ListAdapter(mExpensesList, this);
        lvIncomeList.setAdapter(mListAdapter);
    }

    /**
     * Inner class that extends BaseAdapter for customizing the view of the Listview.
     */
    class ListAdapter extends BaseAdapter {
        TextView mExpensesModelText;
        ArrayList<UtgiftModel> mExpensesList;
        Context context;

        public ListAdapter(ArrayList<UtgiftModel> mExpensesList, Context context){
            this.mExpensesList = mExpensesList;
            this.context = context;

        }
        @Override
        public int getCount() {
            return mExpensesList.size();
        }

        @Override
        public Object getItem(int position) {
            return mExpensesList.get(position);
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
            mExpensesModelText = (TextView)convertView.findViewById(R.id.tvIncomeRow);
            mExpensesModelText.setText("Titel: " + mExpensesList.get(position).mTitel + " - Datum: " + mExpensesList.get(position).mDatum);
            return convertView;
        }
    }
}
