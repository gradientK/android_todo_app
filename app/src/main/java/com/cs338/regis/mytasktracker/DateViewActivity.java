/*DateViewActivity
 *Lists all tasks by date. Allows user to create a new Task for local date or edit existing Task.
 */

package com.cs338.regis.mytasktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**@author Grant Kim
 * @version 2.0, Regis University CS338 Summer 8-week 1
 */
public class DateViewActivity extends AppCompatActivity {
    private static final String TAG = "DateViewActivity";       //used for Log

    private Bundle passAlong;                   //re-Bundle chosenDate Bundle from CalendarView
    private int month;                          //assigned from bundle, month chosen by user
    private int day;                            //assigned from bundle, day of month chosen by user
    private int year;                           //assigned from bundle, year chosen by user

    DatabaseHelper taskDB;                      //task database
    Cursor cursor;                              //reads through database
    TaskCursorHelper cursorHelper;              //extends CursorAdapter to populate ListView
    ListView taskListDate;                       //displays tasks for selected day

    /**onCreate
     * Initializes Date View Activity
     * @param savedInstanceState    Bundle passed into activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_view);

        //Floating Action Button - click to go finish and go to Calendar Activity
        FloatingActionButton goToCalendar = findViewById(R.id.fabGoCalendar2);
        goToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            //click and go to Home Screen
            public void onClick(View view) {
                finish();
            }
        });

        //unpacks Bundle and displays date at top TextView
        setTextDate();

        //binds ViewList with instance in activity_date_view to display tasks in database for
        //selected day
        taskListDate = findViewById(R.id.list_date_view);
        //retrieves database contents and configures ViewList to display all tasks for selected day
        populateList();

        //sets onClickListener to ListView, passes Item selected to EditDelActivity
        listOnClickAction();

        //configures Button to Add new Task
        setupAddButton();
    }

    /**setTextDate
     * Unpacks Bundle which contains Date information chosen by user. Formats to String,
     * displays at top in TextView
     */
    private void setTextDate() {
        //get Bundle containing date integers from CalendarActivity chosen by user
        Bundle chosenDate = this.getIntent().getBundleExtra("chosenDate");
        //assign variables month, day, year from bundle chosenDate
        month = chosenDate.getInt(getString(R.string.bundleKey_month));
        day = chosenDate.getInt(getString(R.string.bundleKey_day));
        year = chosenDate.getInt(getString(R.string.bundleKey_year));

        //get month, day, year from Bundle passed from CalendarActivity, format to String
        String formattedDate = (month + 1) + " / " + day + " / " + year;  //month + 1 cause Android
        //calibrates TextView at top of screen to show date selected by user
        TextView displayDate = findViewById(R.id.displayDate);
        displayDate.setText(formattedDate);

        Log.d(TAG, "date displayed for TextView banner");
    }

    /**populateList
     * Shows lists all tasks in database for selected day
     */
    private void populateList() {
        taskDB = new DatabaseHelper(this);   //Task Database

        //Query for all items in database matching selected day
        cursor = taskDB.getDataByDate(month, day, year);

        //Setup Cursor Adapter using data from cursor above
        cursorHelper = new TaskCursorHelper(this, cursor);

        //Attach cursor adapter to ListView
        taskListDate.setAdapter(cursorHelper);

        Log.d(TAG, "ListView populated from database with tasks for specified date");
    }

    /**listOnClickAction
     * Set onItemClickLister to ListView to enter Edit / Delete Activity
     */
    private void listOnClickAction() {
        taskListDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: List item click with id " + id);

                Intent goEditTask = new Intent(DateViewActivity.this, EditDelActivity.class);
                //extra, pass long id from ListView onClickListener
                goEditTask.putExtra(getString(R.string.text_id), id);
                startActivity(goEditTask);
            }
        });
    }

    /**setupAddButton
     * Button labeled "Add Task", user clicks to switch to blank Add Activity to create
     * new Task. Passes date selected information as ints, passes to AddActivity as Extras
     */
    private void setupAddButton() {
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            //Click and go to new (blank) AddActivity
            public void onClick(View view) {
                Intent goAddTask = new Intent(DateViewActivity.this, AddActivity.class);
                //re-bundle month, dayOfMonth, year from CalendarActivity to passAlong to AddActivity
                passAlong = new Bundle();
                passAlong.putInt(getString(R.string.bundleKey_month), month);   //key from string.xml
                passAlong.putInt(getString(R.string.bundleKey_day), day);
                passAlong.putInt(getString(R.string.bundleKey_year), year);
                goAddTask.putExtra("chosenDate", passAlong);
                startActivity(goAddTask);
            }
        });
    }

    /**onResume
     * Re-populates list with most current database contents upon resuming activity
     */
    protected void onResume() {
        super.onResume();
        populateList();
    }
}
