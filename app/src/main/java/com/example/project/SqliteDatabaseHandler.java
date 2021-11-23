package com.example.project;

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
    List<Expense> expensesList = new ArrayList<>();
    List<Expense> tripExpensesList = new ArrayList<>();
    List<JsonCloudModel> jsonCloudData = new ArrayList<>();
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
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);
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

        // Code from https://stackoverflow.com/questions/9798473/sqlite-in-android-how-to-update-a-specific-row
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
        database.execSQL("DELETE FROM " + EXPENSE_TABLE_NAME);
        database.execSQL("DELETE FROM " + TRIP_TABLE_NAME);
    }

    public Trip getTrip(String trip_id) {
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery("SELECT * FROM " + TRIP_TABLE_NAME + " WHERE " + TRIP_ID_COLUMN + "=?", new String[] { trip_id });
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
        String query =" SELECT * FROM " + TRIP_TABLE_NAME;
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

    // https://stackoverflow.com/questions/53219223/get-specific-row-of-sqlite-android
    public Expense getExpense(String expense_id) {
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery("SELECT * FROM " + EXPENSE_TABLE_NAME + " WHERE " + EXPENSE_ID_COLUMN + "=?", new String[] { expense_id });
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
        String query =" SELECT * FROM " + EXPENSE_TABLE_NAME;
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

    public List<JsonCloudModel> getJsonCloudData() {
        String query = "SELECT trips.name, expenses.type, expenses.amount, expenses.time, expenses.additional_comments" +
                " FROM expenses JOIN trips on expenses.trip_id = trips.id";
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

            JsonCloudModel expense = new JsonCloudModel(trip_name, expense_type, expense_amount, expense_time, expense_additional_comments);
            jsonCloudData.add(expense);
        }
        cursor.close();
        return jsonCloudData;
    }

    public List<Expense> getTripExpenses(String selected_trip_id) {
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery("SELECT * FROM " + EXPENSE_TABLE_NAME + " WHERE trip_id=?", new String[] { selected_trip_id });
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
}
