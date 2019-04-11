package com.example.julian.da345a_mobila_applikationer_p1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Menu activity.
 * Contains four buttons for either adding income/expense or showing incomes/expenses.
 */
public class MenuActivity extends AppCompatActivity {
    private TextView tvUser;
    private Button btnAddIncome;
    private Button btnAddExpense;
    private Button btnShowIncome;
    private Button btnShowExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvUser = findViewById(R.id.tvIncomeUser);
        btnAddIncome = findViewById(R.id.btnAddIncome);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnShowIncome = findViewById(R.id.btnShowIncome);
        btnShowExpenses = findViewById(R.id.btnShowExpenses);

        Intent intent = getIntent();
        String user = getIntent().getStringExtra("Firstname") + " " + getIntent().getStringExtra("Surname");
        tvUser.setText(user);

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
                Toast.makeText(getApplicationContext(), "ADD INCOME", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
                Toast.makeText(getApplicationContext(), "ADD EXPENSE", Toast.LENGTH_SHORT).show();
            }
        });

        btnShowIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIncomeOverview();
                Toast.makeText(getApplicationContext(), "SHOW INCOME OVERVIEW", Toast.LENGTH_SHORT).show();
            }
        });

        btnShowExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpensesOverview();
                Toast.makeText(getApplicationContext(), "SHOW EXPENSES OVERVIEW", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addIncome(){
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("Mode", "income");
        intent.putExtra("User", tvUser.getText().toString());
        startActivity(intent);

    }

    private void addExpense(){
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("Mode", "expense");
        intent.putExtra("User", tvUser.getText().toString());
        startActivity(intent);
    }

    private void showIncomeOverview(){
        Intent intent = new Intent(this, IncomeOverviewActivity.class);
        intent.putExtra("User", tvUser.getText().toString());
        startActivity(intent);
    }

    private void showExpensesOverview(){
        Intent intent = new Intent(this, ExpensesOverviewActivity.class);
        intent.putExtra("User", tvUser.getText().toString());
        startActivity(intent);
    }
}
