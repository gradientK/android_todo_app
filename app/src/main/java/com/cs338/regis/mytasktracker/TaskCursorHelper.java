/*TaskCursorHelper
 *Extends CursorAdapter to take data from Cursor and apply to task_row layout, and ListViews
 */

package com.cs338.regis.mytasktracker;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**@author Grant Kim
 * @version 1.0, Regis University CS338 Summer 8-week 1
 */
public class TaskCursorHelper extends CursorAdapter {
    private static final String TAG = "TaskCursorHelper";       //used for Log

    /**TaskCursorHelper
     * Creates instance of CursorAdapter Helper for Tasks
     * @param context   context
     * @param cursor    cursor
     */
    public TaskCursorHelper(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    /**newView
     * Makes a new view to hold the data pointed to by cursor.
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_row, parent, false);
    }

    /**bindView
     * Bind an existing view to the data pointed to by cursor
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Find fields to bind within layout
        TextView monthView = view.findViewById(R.id.textMonth);
        TextView dayView = view.findViewById(R.id.textDay);
        TextView yearView = view.findViewById(R.id.textYear);
        TextView taskView = view.findViewById(R.id.textTaskDisplay);

        //Extract from cursor
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
        int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
        int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        String taskData = cursor.getString(cursor.getColumnIndexOrThrow("taskData"));

        //Convert month int to full word as String, calls monthConverter()
        String monthString = monthConverter(month);
        //Converts day/year int to String
        String dayString = " " + String.valueOf(day) + ", ";
        String yearString = String.valueOf(year);

        //Bind data to fields
        monthView.setText(monthString);
        dayView.setText(dayString);
        yearView.setText(yearString);
        taskView.setText(taskData);

        Log.d(TAG, "cursor applied to TextViews");
    }

    /**monthConverter
     * passes int and converts to Month. (January starts with 0 cause Android)
     */
    public String monthConverter(int month) {
        String monthSpelled = "Error";
        switch (month) {
            case 0:     monthSpelled = "January";
                        break;
            case 1:     monthSpelled = "February";
                        break;
            case 2:     monthSpelled = "March";
                        break;
            case 3:     monthSpelled = "April";
                        break;
            case 4:     monthSpelled = "May";
                        break;
            case 5:     monthSpelled = "June";
                        break;
            case 6:     monthSpelled = "July";
                        break;
            case 7:     monthSpelled = "August";
                        break;
            case 8:     monthSpelled = "September";
                        break;
            case 9:     monthSpelled = "October";
                        break;
            case 10:    monthSpelled = "November";
                        break;
            case 11:    monthSpelled = "December";
                        break;
            default:    break;
        }
        return monthSpelled;
    }
}