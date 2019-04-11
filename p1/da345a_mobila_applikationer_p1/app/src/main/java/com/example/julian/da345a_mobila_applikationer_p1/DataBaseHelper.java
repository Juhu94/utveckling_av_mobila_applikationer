package com.example.julian.da345a_mobila_applikationer_p1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * DatabaseHelper class for reading and writing from the internal database.
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DataBaseHelper";


    private static final String ID = "id";
    private static final String TABLE_INKOMST = "Inkomster";
    private static final String TABLE_UTGIFT = "Utgifter";
    private static final String TITEL = "titel";
    private static final String KATEGORI = "kategori";
    private static final String PRIS = "pris";
    private static final String BELOPP = "belopp";
    private static final String DATUM = "datum";


    private static final String CREATE_TABLE_INKOMST = "CREATE TABLE " +TABLE_INKOMST + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITEL + " TEXT, "
            + KATEGORI + " TEXT, " + BELOPP + " TEXT, " + DATUM + " TEXT);";

    private static final String CREATE_TABLE_UTGIFT = "CREATE TABLE " + TABLE_UTGIFT + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITEL + " TEXT, "
            + KATEGORI + " TEXT, " + PRIS + " TEXT, " + DATUM + " TEXT);";


    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INKOMST);
        db.execSQL(CREATE_TABLE_UTGIFT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Method for creating a new income in the database
     * @param titel, title of the income.
     * @param kategori, which category.
     * @param belopp, the amount.
     * @param datum, the date.
     */
    public void createInkomst(String titel, String kategori, String belopp, String datum) {
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TITEL, titel);
            values.put(KATEGORI, kategori);
            values.put(BELOPP, belopp);
            values.put(DATUM, datum);
            db.insert(TABLE_INKOMST, null, values);

            Log.d(DATABASE_NAME, "CREATE INKOMST BELOPP = " + belopp);

            db.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method for creating a new expense in the database.
     * @param titel, the title of the expense.
     * @param kategori, the category.
     * @param pris, the amount.
     * @param datum, the date.
     */
    public void createUtgift(String titel, String kategori, String pris, String datum) {
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TITEL, titel);
            values.put(KATEGORI, kategori);
            values.put(PRIS, pris);
            values.put(DATUM, datum);
            db.insert(TABLE_UTGIFT, null, values);

            Log.d(DATABASE_NAME, "CREATE UTGIFT");

            db.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method for reading all of the income entries in the database.
     * @return An ArrayList containing all of the income entries.
     */
    public ArrayList<InkomstModel> readInkomst(){
        ArrayList<InkomstModel> mInkomstList = new ArrayList<InkomstModel>();
        String selectQuery = "SELECT * FROM " + TABLE_INKOMST + " ORDER BY " + DATUM;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if(c.moveToFirst()){
                do{
                    InkomstModel mInkomstModel = new InkomstModel();
                    mInkomstModel.mTitel = c.getString(c.getColumnIndex(TITEL));
                    mInkomstModel.mKategori = c.getString(c.getColumnIndex(KATEGORI));
                    mInkomstModel.mBelopp = c.getString(c.getColumnIndex(BELOPP));
                    mInkomstModel.mDatum = c.getString(c.getColumnIndex(DATUM));
                    mInkomstList.add(mInkomstModel);
                    Log.d("INCOME DATABAS", mInkomstList.get(0).mBelopp);
                }while(c.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return mInkomstList;
    }

    /**
     * Method for reading income entries given a date interval.
     * @param start, the start date
     * @param end, the end date
     * @return an ArrayList containing the income entries in the given date interval.
     */
    public ArrayList<InkomstModel> readInkomstBetweenDates(String start, String end){
        ArrayList<InkomstModel> mInkomstList = new ArrayList<InkomstModel>();
        String selectQuery = "SELECT * FROM " + TABLE_INKOMST + " WHERE " + DATUM + " BETWEEN '" + start + "' AND '" + end + "'";
        Log.d("INCOME", selectQuery);
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if(c.moveToFirst()){
                do{
                    InkomstModel mInkomstModel = new InkomstModel();
                    mInkomstModel.mTitel = c.getString(c.getColumnIndex(TITEL));
                    mInkomstModel.mKategori = c.getString(c.getColumnIndex(KATEGORI));
                    mInkomstModel.mBelopp = c.getString(c.getColumnIndex(BELOPP));
                    mInkomstModel.mDatum = c.getString(c.getColumnIndex(DATUM));
                    mInkomstList.add(mInkomstModel);
                    Log.d("INCOME DATABAS", mInkomstList.get(0).mDatum);
                }while(c.moveToNext());
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return mInkomstList;
    }

    /**
     * Method for reading all of the expense entries.
     * @return an ArrayList containing all of the expense entries.
     */
    public ArrayList<UtgiftModel> readUtgift(){
        ArrayList<UtgiftModel> mUtgiftList = new ArrayList<UtgiftModel>();
        String selectQuery = "SELECT * FROM " + TABLE_UTGIFT + " ORDER BY " + DATUM;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if(c.moveToFirst()){
                do{
                    UtgiftModel mUtgiftModel = new UtgiftModel();
                    mUtgiftModel.mTitel = c.getString(c.getColumnIndex(TITEL));
                    mUtgiftModel.mKategori = c.getString(c.getColumnIndex(KATEGORI));
                    mUtgiftModel.mPris = c.getString(c.getColumnIndex(PRIS));
                    mUtgiftModel.mDatum = c.getString(c.getColumnIndex(DATUM));
                    mUtgiftList.add(mUtgiftModel);
                }while(c.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return mUtgiftList;
    }

    /**
     * Method for reading expense entries given a date interval.
     * @param start, start date
     * @param end, end date
     * @return an ArrayList containing the expense entries between the date interval.
     */
    public ArrayList<UtgiftModel> readUtgiftBetweenDates(String start, String end){
        ArrayList<UtgiftModel>mUtgiftList = new ArrayList<UtgiftModel>();
        String selectQuery = "SELECT * FROM " + TABLE_UTGIFT + " WHERE " + DATUM + " BETWEEN '" + start + "' AND '" + end + "'";
        Log.d("INCOME", selectQuery);
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if(c.moveToFirst()){
                do{
                    UtgiftModel mUtgiftModel = new UtgiftModel();
                    mUtgiftModel.mTitel = c.getString(c.getColumnIndex(TITEL));
                    mUtgiftModel.mKategori = c.getString(c.getColumnIndex(KATEGORI));
                    mUtgiftModel.mPris = c.getString(c.getColumnIndex(PRIS));
                    mUtgiftModel.mDatum = c.getString(c.getColumnIndex(DATUM));
                    mUtgiftList.add(mUtgiftModel);
                    Log.d("INCOME DATABAS", mUtgiftList.get(0).mDatum);
                }while(c.moveToNext());
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mUtgiftList;
    }

    /**
     * Method for counting the total income and returning the number.
     * @return a double containing the total income.
     */
    public double getIncomeSum(){
        double sum = 0;
        String selecQuery = "SELECT " + BELOPP + " FROM " + TABLE_INKOMST;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selecQuery, null);
            if(c.moveToFirst()){
                do{
                    double number = Double.parseDouble(c.getString(c.getColumnIndex(BELOPP)));
                    sum += number;
                }while(c.moveToNext());
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * Method for counting the total expense amount.
     * @return a double containing the total expense amount.
     */
    public double getExpensesSum(){
        double sum = 0;
        String selecQuery = "SELECT " + PRIS + " FROM " + TABLE_UTGIFT;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selecQuery, null);
            if(c.moveToFirst()){
                do{
                    double number = Double.parseDouble(c.getString(c.getColumnIndex(PRIS)));
                    sum += number;
                }while(c.moveToNext());
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sum;
    }
}
