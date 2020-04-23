/*Task Tracker
 *Program allows tasks to be inputted by user, and later referenced as reminders.
 *MainActivity lists upcoming to-do tasks by date.
 */

package com.cs338.regis.mytasktracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

/**@author Grant Kim
 * @version 2.0, Regis University CS338 Summer 8-week 1
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";       //used for Log

    DatabaseHelper taskDB;                      //task database
    Cursor cursor;                              //reads through database
    TaskCursorHelper cursorHelper;              //extends CursorAdapter to populate ListView
    ListView taskList;                          //displays all tasks

    /**onCreate
     * Initializes main activity
     * @param savedInstanceState    Bundle passed into activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //displays Main Toolbar
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        //Floating Action Button - click to go to CalendarActivity
        FloatingActionButton goToCalendar = findViewById(R.id.fabGoCalendar);
        goToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            //click and go to Calendar
            public void onClick(View view) {
                Intent launchCalendar = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(launchCalendar);
            }
        });

        //binds ViewList with instance in activity_main to display all tasks in database
        taskList = findViewById(R.id.list_main);
        //retrieves database contents and configures ViewList to display all tasks
        populateList();

        //sets onClickListener to ListView, passes Item selected to EditDelActivity
        listOnClickAction();
    }

    /**populateList
     * Shows lists all task in database
     */
    private void populateList() {
        taskDB = new DatabaseHelper(this);   //Task Database

        //Query for all items in database, sorted by date
        cursor = taskDB.getSortedData();

        //Setup Cursor Adapter using data from cursor above
        cursorHelper = new TaskCursorHelper(this, cursor);

        //Attach cursor adapter to ListView
        taskList.setAdapter(cursorHelper);

        Log.d(TAG, "ListView populated from database.");
    }

    /**listOnClickAction
     * Set onItemClickLister to ListView to enter Edit / Delete Activity
     */
    private void listOnClickAction() {
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: List item click with id " + id);

                Intent goEditTask = new Intent(MainActivity.this, EditDelActivity.class);
                //extra, pass long id from ListView onClickListener
                goEditTask.putExtra(getString(R.string.text_id), id);
                startActivity(goEditTask);
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
