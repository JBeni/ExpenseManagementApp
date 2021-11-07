package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SqliteDatabaseHandler extends SQLiteOpenHelper {

    List<Trip> tripsList = new ArrayList<>();
    private SQLiteDatabase database;
    Context context;

    private static final String DATABASE_NAME = "ExpenseManagement.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TRIP_TABLE_NAME = "Trips";
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String DESTINATION_COLUMN = "destination";
    public static final String DATE_COLUMN = "date";
    public static final String RISK_ASSESSMENT_COLUMN = "risk_assessment";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DURATION_COLUMN = "duration";
    public static final String AIM_COLUMN = "aim";
    public static final String STATUS_COLUMN = "status";

    private static final String EXPENSE_TABLE_NAME = "Expenses";
    public static final String EXPENSE_ID_COLUMN = "id";
    public static final String EXPENSE_TYPE_COLUMN = "type";
    public static final String EXPENSE_AMOUNT_COLUMN = "amount";
    public static final String EXPENSE_TIME_COLUMN = "time";
    public static final String EXPENSE_COMMENTS_COLUMN = "additional_comments";
    public static final String EXPENSE_TRIP_ID_COLUMN = "trip_id";


    private static final String TABLE_NAME = "contacts";
    private static final String KEY_ROWID = "_id";
    private static final String KEY_NAME="name";
    private static final String KEY_EMAIL = "email";

    private static final String CREATE_CONTACTS_TABLE = "create table "+ TABLE_NAME+
            " ("+ KEY_ROWID+" integer primary key autoincrement, "+ KEY_NAME + " text, "+ KEY_EMAIL+ " text)";

    public SqliteDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTrip = String.format(
                "CREATE TABLE %s (" +
                        "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT)",
                TRIP_TABLE_NAME, ID_COLUMN, NAME_COLUMN, DESTINATION_COLUMN, DATE_COLUMN,
                RISK_ASSESSMENT_COLUMN, DESCRIPTION_COLUMN, DURATION_COLUMN, AIM_COLUMN, STATUS_COLUMN);

        String queryExpense = String.format(
                "CREATE TABLE %s (" +
                        "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s TEXT, " +
                        "   %s INTEGER, " +
                        "   FOREIGN KEY(trip_id) REFERENCES Trips(id))",
                EXPENSE_TABLE_NAME, EXPENSE_ID_COLUMN, EXPENSE_TYPE_COLUMN,
                EXPENSE_AMOUNT_COLUMN, EXPENSE_TIME_COLUMN, EXPENSE_COMMENTS_COLUMN, EXPENSE_TRIP_ID_COLUMN);

        db.execSQL(queryTrip);
        db.execSQL(queryExpense);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.v(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " + newVersion + " - old data lost");
        onCreate(db);
    }

    public void insertTrip(String trip_name, String trip_destination, String trip_date, String risk, String description, String duration, String aim, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rowValues = new ContentValues();

        rowValues.put(NAME_COLUMN, trip_name);
        rowValues.put(DESTINATION_COLUMN, trip_destination);
        rowValues.put(DATE_COLUMN, trip_date);
        rowValues.put(RISK_ASSESSMENT_COLUMN, risk);
        rowValues.put(DESCRIPTION_COLUMN, description);
        rowValues.put(DURATION_COLUMN, duration);
        rowValues.put(AIM_COLUMN, aim);
        rowValues.put(STATUS_COLUMN, status);

        long result = db.insert(TRIP_TABLE_NAME, null, rowValues);
        if (result == -1) {
            Toast.makeText(context, "Insert operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Insert operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Code inspired from https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial/blob/master/app/src/main/java/com/jovanovic/stefan/sqlitetutorial/MyDatabaseHelper.java
     */
    public void updateTrip(String trip_id, String trip_name, String trip_destination, String trip_date, String risk, String description, String duration, String aim, String status) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(NAME_COLUMN, trip_name);
        rowValues.put(DESTINATION_COLUMN, trip_destination);
        rowValues.put(DATE_COLUMN, trip_date);
        rowValues.put(RISK_ASSESSMENT_COLUMN, risk);
        rowValues.put(DESCRIPTION_COLUMN, description);
        rowValues.put(DURATION_COLUMN, duration);
        rowValues.put(AIM_COLUMN, aim);
        rowValues.put(STATUS_COLUMN, status);

        long result = database.update(TRIP_TABLE_NAME, rowValues, "id=?", new String[]{ trip_id });
        if (result == -1) {
            Toast.makeText(context, "Update operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Update operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Code inspired from https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial/blob/master/app/src/main/java/com/jovanovic/stefan/sqlitetutorial/MyDatabaseHelper.java
     */
    public void deleteTrip(String trip_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = database.delete(TRIP_TABLE_NAME, "id=?", new String[]{ trip_id });
        if (result == -1) {
            Toast.makeText(context, "Delete operation has failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Delete operation was successful!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Code inspired from https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial/blob/master/app/src/main/java/com/jovanovic/stefan/sqlitetutorial/MyDatabaseHelper.java
     */
    public void deleteAllTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TRIP_TABLE_NAME);
    }

    /**
     * Code inspired from https://github.com/stevdza-san/SQLite_Android-Complete_Tutorial/blob/master/app/src/main/java/com/jovanovic/stefan/sqlitetutorial/MyDatabaseHelper.java
     */
    public Cursor getTrips() {
        String query =" SELECT * FROM " + TRIP_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
           cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    /**
     * https://github.com/sandipapps/Populate-RecyclerView-From-SQLite
     */
    public List<Trip> getAllTrips() {
        String query =" SELECT * FROM " + TRIP_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        while(cursor.moveToNext()){
            /*
            int index1 = cursor.getColumnIndex(KEY_ROWID);
            int rowid = cursor.getInt(index1);
            int index2 = cursor.getColumnIndex(KEY_NAME);
            String name = cursor.getString(index2);
            int index3 = cursor.getColumnIndex(KEY_EMAIL);
            String email = cursor.getString(index3);
            */

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String destination = cursor.getString(2);
            String date = cursor.getString(3);
            String risk_assessment = cursor.getString(4);
            String description = cursor.getString(5);
            String duration = cursor.getString(6);
            String aim = cursor.getString(7);
            String status = cursor.getString(8);

            Trip contact = new Trip(id, name, destination, date, risk_assessment, description, duration, aim, status);
            tripsList.add(contact);
        }
        return tripsList;
    }
}
