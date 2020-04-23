/*DatabaseHelper
 *Creates database for Tasks, implements methods to store, retrieve and edit data
 */

package com.cs338.regis.mytasktracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**@author Grant Kim
 * @version 1.3, Regis University CS338 Summer 8-week 1
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";         //used for Log
    private static final String DATABASE_NAME = "tasks.db";     //database name
    private static final String TABLE_NAME = "tasks_table";     //table name
    private static final String COL_0 = "_id";                  //_id
    private static final String COL_1 = "month";                //data month
    private static final String COL_2 = "day";                  //data day
    private static final String COL_3 = "year";                 //data year
    private static final String COL_4 = "taskData";             //data task content

    /**DatabaseHelper
     * Create a helper object to create, open, and/or manage a database.
     * @param context   to use, to open or create the database
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    /**onCreate
     * Create table. Column 1 = database ID, Column 2 = month, Column 3 = day, Column 4 = year,
     * Column 5 = task contents as String
     * @param db    database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME
                + " (" + COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_1 + " INTEGER, " + COL_2 + " INTEGER, "
                + COL_3 + " INTEGER, " + COL_4 + " TEXT)";
        db.execSQL(createTable);
    }

    /**onUpgrade
     * Called when the database needs to be upgraded
     * @param db            The database
     * @param oldVersion    The old database version
     * @param newVersion    The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**addData
     * Adds date and task contents into SQLite database
     * @param month     month
     * @param day       day
     * @param year      year
     * @param taskData  text data for task
     * @return          boolean, -1 = error = false
     */
    public boolean addData(int month, int day, int year, String taskData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, month);
        contentValues.put(COL_2, day);
        contentValues.put(COL_3, year);
        contentValues.put(COL_4, taskData);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {
            return false;
        } else {
            Log.d(TAG, "addData: Row inserted");
            return true;
        }
    }

    /**updateData
     * Matches row with _id and updates all columns
     * @param id        to match _id
     * @param month     updates month
     * @param day       updates day
     * @param year      updates year
     * @param taskData  update taskData
     */
    public void updateData(int id, int month, int day, int year, String taskData) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET month = " + month + ", day = " + day + ", "
                + "year = " + year + ", taskData = '" + taskData + "' WHERE _id = " + id;
        Log.d(TAG, "updateData: Row updated");
        db.execSQL(query);
    }

    /**getAllData
     * Get all data from database, order by _id
     * @return          allData
     */
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor allData = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if(allData != null) {
            allData.moveToFirst();
        }
        return allData;
    }

    /**getSortedData
     * Get all data from database, sorted by date order
     * @return          sortedData
     */
    public Cursor getSortedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = new String[]{COL_0, COL_1, COL_2, COL_3, COL_4};
        Cursor sortedData = db.query(TABLE_NAME, columns, null, null,
                null, null, "year, " + "month, " + "day");
        if(sortedData != null) {
            sortedData.moveToFirst();
        }
        return sortedData;
    }

    /**getRowByID
     * Get data from one row matching passed in _id value
     * @param id        to match with column 0 _id
     * @return          rowData
     */
    public Cursor getRowByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rowData;
        rowData = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_0 + " = " + id,
                null);
        if(rowData != null) {
            rowData.moveToFirst();
        }
        return rowData;
    }

    /**getDataByDate
     * Get all data matching month, day, year
     * @return          dateData
     */
    public Cursor getDataByDate(int month, int day, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dateData = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE month = " + month
                + " AND day = " + day + " AND year = " + year, null);
        if(dateData != null) {
            dateData.moveToFirst();
        }
        return dateData;
    }

    /**deleteRow
     * Deletes row matching passed in id
     * @param id        id matches in column 0
     */
    public void deleteRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + "_id = " + id;
        Log.d(TAG, "deleteRow: Row deleted");
        db.execSQL(query);
    }
}
