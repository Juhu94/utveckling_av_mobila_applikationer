package com.example.julian.da345a_mobila_applikationer_p1;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity that handles both the ExpenseFragment and IncomeFragment.
 * It is used to determine which fragment that should be shown.
 */
public class AddActivity extends AppCompatActivity {

    private String user = "";
    private AddExpenseFragment expense;
    private AddIncomeFragment income;
    private boolean isIncomeActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if(savedInstanceState != null){
            isIncomeActive = savedInstanceState.getBoolean("activeFragment");
            if(isIncomeActive){
                income = (AddIncomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            }
            else {
                expense = (AddExpenseFragment) getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            }
        }
        else {
            Intent intent = getIntent();
            String mode = intent.getStringExtra("Mode");
            user = intent.getStringExtra("User");

            if(mode.equals("income")){
                Bundle bundle = new Bundle();
                bundle.putString("User", user);

                FragmentManager fm = getSupportFragmentManager();
                income = new AddIncomeFragment();
                income.setArguments(bundle);

                FragmentTransaction fmTransaction = fm.beginTransaction();
                fmTransaction.replace(R.id.fragment_container, income);
                fmTransaction.commit();

                isIncomeActive = true;
            }
            if (mode.equals("expense")){
                Bundle bundle = new Bundle();
                bundle.putString("User", user);

                FragmentManager fm = getSupportFragmentManager();
                expense = new AddExpenseFragment();
                expense.setArguments(bundle);

                FragmentTransaction fmTransaction = fm.beginTransaction();
                fmTransaction.replace(R.id.fragment_container, expense);
                fmTransaction.commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(isIncomeActive){
            getSupportFragmentManager().putFragment(outState, "currentFragment", income);
        }
        else {
            getSupportFragmentManager().putFragment(outState, "currentFragment", expense);
        }
        outState.putBoolean("activeFragment", isIncomeActive);
        super.onSaveInstanceState(outState);
    }
}
