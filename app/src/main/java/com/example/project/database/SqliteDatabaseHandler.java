package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.project.models.Expense;
import com.example.project.models.JsonCloud;
import com.example.project.models.Trip;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SqliteDatabaseHandler extends SQLiteOpenHelper {

    private final SQLiteDatabase database;
    Context context;

    private static final String DATABASE_NAME = "ExpenseManagement.db";
    private static final int DATABASE_VERSION = 1;

    //region TABLES DATA
    private static final String TRIP_TABLE_NAME = "Trips";
    public static final String TRIP_ID_COLUMN = "id";
    public static final String TRIP_NAME_COLUMN = "name";
    public static final String TRIP_DESTINATION_COLUMN = "destination";
    public static final String TRIP_DATE_COLUMN = "date";
    public static final String TRIP_RISK_ASSESSMENT_COLUMN = "risk_assessment";
    public static final String TRIP_DESCRIPTION_COLUMN = "description";
    public static final String TRIP_DURATION_COLUMN = "duration";
    public static final String TRIP_AIM_COLUMN = "aim";
    public static final String TRIP_STATUS_COLUMN = "status";

    private static final String EXPENSE_TABLE_NAME = "Expenses";
    public static final String EXPENSE_ID_COLUMN = "id";
    public static final String EXPENSE_TYPE_COLUMN = "type";
    public static final String EXPENSE_AMOUNT_COLUMN = "amount";
    public static final String EXPENSE_TIME_COLUMN = "time";
    public static final String EXPENSE_COMMENTS_COLUMN = "additional_comments";
    public static final String EXPENSE_TRIP_ID_COLUMN = "trip_id";
    //endregion

    public SqliteDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
                TRIP_TABLE_NAME, TRIP_ID_COLUMN, TRIP_NAME_COLUMN, TRIP_DESTINATION_COLUMN, TRIP_DATE_COLUMN,
                TRIP_RISK_ASSESSMENT_COLUMN, TRIP_DESCRIPTION_COLUMN, TRIP_DURATION_COLUMN, TRIP_AIM_COLUMN, TRIP_STATUS_COLUMN);

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TRIP_TABLE_NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", EXPENSE_TABLE_NAME));
        Log.v(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " + newVersion + " - old data lost");
        onCreate(db);
    }

    public void insertTrip(String name, String destination, String date, String risk, String description, String duration, String aim, String status) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(TRIP_NAME_COLUMN, name);
        rowValues.put(TRIP_DESTINATION_COLUMN, destination);
        rowValues.put(TRIP_DATE_COLUMN, date);
        rowValues.put(TRIP_RISK_ASSESSMENT_COLUMN, risk);
        rowValues.put(TRIP_DESCRIPTION_COLUMN, description);
        rowValues.put(TRIP_DURATION_COLUMN, duration);
        rowValues.put(TRIP_AIM_COLUMN, aim);
        rowValues.put(TRIP_STATUS_COLUMN, status);

        long result = database.insert(TRIP_TABLE_NAME, null, rowValues);
        if (result == -1) {
            Toast.makeText(context.getApplicationContext(), "Insert Trip operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "Insert Trip operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    public void insertExpense(String type, String amount, String time, String additional_comments, String trip_id) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(EXPENSE_TYPE_COLUMN, type);
        rowValues.put(EXPENSE_AMOUNT_COLUMN, amount);
        rowValues.put(EXPENSE_TIME_COLUMN, time);
        rowValues.put(EXPENSE_COMMENTS_COLUMN, additional_comments);
        rowValues.put("trip_id", trip_id);

        long result = database.insert(EXPENSE_TABLE_NAME, null, rowValues);
        if (result == -1) {
            Toast.makeText(context.getApplicationContext(), "Insert Expense operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "Insert Expense operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    public void updateTrip(String id, String name, String destination, String date, String risk, String description, String duration, String aim, String status) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(TRIP_NAME_COLUMN, name);
        rowValues.put(TRIP_DESTINATION_COLUMN, destination);
        rowValues.put(TRIP_DATE_COLUMN, date);
        rowValues.put(TRIP_RISK_ASSESSMENT_COLUMN, risk);
        rowValues.put(TRIP_DESCRIPTION_COLUMN, description);
        rowValues.put(TRIP_DURATION_COLUMN, duration);
        rowValues.put(TRIP_AIM_COLUMN, aim);
        rowValues.put(TRIP_STATUS_COLUMN, status);

        long result = database.update(TRIP_TABLE_NAME, rowValues, "id=?", new String[]{ id });
        if (result == -1) {
            Toast.makeText(context, "Update Trip operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Update Trip operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    public void updateExpense(String id, String type, String amount, String time, String additional_comments) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(EXPENSE_TYPE_COLUMN, type);
        rowValues.put(EXPENSE_AMOUNT_COLUMN, amount);
        rowValues.put(EXPENSE_TIME_COLUMN, time);
        rowValues.put(EXPENSE_COMMENTS_COLUMN, additional_comments);

        long result = database.update(EXPENSE_TABLE_NAME, rowValues, "id=?", new String[]{ id });
        if (result == -1) {
            Toast.makeText(context, "Update Expense operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Update Expense operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteTripAndExpenses(String trip_id) {
        long result_expense = database.delete(EXPENSE_TABLE_NAME, "trip_id=?", new String[]{ trip_id });
        long result_trip = database.delete(TRIP_TABLE_NAME, "id=?", new String[]{ trip_id });

        if (result_expense == -1 && result_trip == -1) {
            Toast.makeText(context, "Delete operation has failed!", Toast.LENGTH_LONG).show();
        } else if (result_expense == 0 && result_trip == 0) {
            Toast.makeText(context, "Delete operation has failed! The id parameter could be null!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Delete operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteTrip(String id) {
        long result = database.delete(TRIP_TABLE_NAME, "id=?", new String[]{ id });
        if (result == -1) {
            Toast.makeText(context, "Delete Trip operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Delete Trip operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteExpense(String id) {
        long result = database.delete(EXPENSE_TABLE_NAME, "id=?", new String[]{ id });
        if (result == -1) {
            Toast.makeText(context, "Delete Expense operation has failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Delete Expense operation was successful!", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAllData() {
        database.execSQL(String.format("DELETE FROM %s", EXPENSE_TABLE_NAME));
        database.execSQL(String.format("DELETE FROM %s", TRIP_TABLE_NAME));
    }

    public Trip getTrip(String trip_id) {
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s=?", TRIP_TABLE_NAME, TRIP_ID_COLUMN), new String[] { trip_id });
        }
        assert cursor != null;
        cursor.moveToFirst();

        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String destination = cursor.getString(2);
        String date = cursor.getString(3);
        String risk_assessment = cursor.getString(4);
        String description = cursor.getString(5);
        String duration = cursor.getString(6);
        String aim = cursor.getString(7);
        String status = cursor.getString(8);

        cursor.close();
        return new Trip(id, name, destination, date, risk_assessment, description, duration, aim, status);
    }

    public List<Trip> getAllTrips() {
        List<Trip> tripsList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", TRIP_TABLE_NAME);
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String destination = cursor.getString(2);
            String date = cursor.getString(3);
            String risk_assessment = cursor.getString(4);
            String description = cursor.getString(5);
            String duration = cursor.getString(6);
            String aim = cursor.getString(7);
            String status = cursor.getString(8);

            Trip trip = new Trip(id, name, destination, date, risk_assessment, description, duration, aim, status);
            tripsList.add(trip);
        }
        cursor.close();
        return tripsList;
    }

    public List<Trip> getSimpleSearchFilter(String nameSearched) {
        List<Trip> tripsListFilter = new ArrayList<>();
        String query = MessageFormat.format("SELECT * FROM {0} WHERE {1} LIKE ''%{2}%''", TRIP_TABLE_NAME, TRIP_NAME_COLUMN, nameSearched);
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String destination = cursor.getString(2);
            String date = cursor.getString(3);
            String risk_assessment = cursor.getString(4);
            String description = cursor.getString(5);
            String duration = cursor.getString(6);
            String aim = cursor.getString(7);
            String status = cursor.getString(8);

            Trip trip = new Trip(id, name, destination, date, risk_assessment, description, duration, aim, status);
            tripsListFilter.add(trip);
        }
        cursor.close();
        return tripsListFilter;
    }

    public List<Trip> getAdvanceSearchFilter(String name_search, String destination_search, String date_search) {
        List<Trip> tripsListAdvanced = new ArrayList<>();
        String query = MessageFormat.format("SELECT * FROM {0} WHERE {1} LIKE ''%{2}%'' AND {3} LIKE ''%{4}%'' AND {5} LIKE ''%{6}%''",
            TRIP_TABLE_NAME, TRIP_NAME_COLUMN, name_search, TRIP_DESTINATION_COLUMN, destination_search, TRIP_DATE_COLUMN, date_search);
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String destination = cursor.getString(2);
            String date = cursor.getString(3);
            String risk_assessment = cursor.getString(4);
            String description = cursor.getString(5);
            String duration = cursor.getString(6);
            String aim = cursor.getString(7);
            String status = cursor.getString(8);

            Trip trip = new Trip(id, name, destination, date, risk_assessment, description, duration, aim, status);
            tripsListAdvanced.add(trip);
        }
        cursor.close();
        return tripsListAdvanced;
    }

    public List<JsonCloud> getJsonCloudData() {
        List<JsonCloud> jsonCloudData = new ArrayList<>();
        String query = "SELECT trips.name, expenses.type, expenses.amount, expenses.time, expenses.additional_comments FROM expenses JOIN trips on expenses.trip_id = trips.id";
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            String trip_name = cursor.getString(0);
            String expense_type = cursor.getString(1);
            String expense_amount = cursor.getString(2);
            String expense_time = cursor.getString(3);
            String expense_additional_comments = cursor.getString(4);

            JsonCloud expense = new JsonCloud(trip_name, expense_type, expense_amount, expense_time, expense_additional_comments);
            jsonCloudData.add(expense);
        }
        cursor.close();
        return jsonCloudData;
    }

    public List<Expense> getTripExpenses(String selected_trip_id) {
        List<Expense> tripExpensesList = new ArrayList<>();
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE trip_id=?", EXPENSE_TABLE_NAME), new String[] { selected_trip_id });
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String amount = cursor.getString(2);
            String time = cursor.getString(3);
            String additional_comments = cursor.getString(4);
            int trip_id = cursor.getInt(5);

            Expense expense = new Expense(id, type, amount, time, additional_comments, trip_id);
            tripExpensesList.add(expense);
        }
        cursor.close();
        return tripExpensesList;
    }

    public Expense getExpense(String expense_id) {
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s=?", EXPENSE_TABLE_NAME, EXPENSE_ID_COLUMN), new String[] { expense_id });
        }
        assert cursor != null;
        cursor.moveToFirst();

        int id = cursor.getInt(0);
        String type = cursor.getString(1);
        String amount = cursor.getString(2);
        String time = cursor.getString(3);
        String additional_comments = cursor.getString(4);
        int trip_id = cursor.getInt(5);

        cursor.close();
        return new Expense(id, type, amount, time, additional_comments, trip_id);
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expensesList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", EXPENSE_TABLE_NAME);
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String amount = cursor.getString(2);
            String time = cursor.getString(3);
            String additional_comments = cursor.getString(4);
            int trip_id = cursor.getInt(5);

            Expense expense = new Expense(id, type, amount, time, additional_comments, trip_id);
            expensesList.add(expense);
        }
        cursor.close();
        return expensesList;
    }
}
