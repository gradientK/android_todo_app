/*EditDelActivity
  Activity where user can create/edit and save a Task, or delete Task.
 */

package com.cs338.regis.mytasktracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**@author Grant Kim
 * @version 1.1, Regis University CS338 Summer 8-week 1
 */
public class EditDelActivity extends AppCompatActivity {
    private static final String TAG = "EditDelActivity";    //used for Log
    private DatabaseHelper taskDB;              //creates instance of DatabaseHelper class

    private int id;                             //retrieved from extra, long converted to int

    private EditText displayMonth;              //displays month to user
    private EditText displayDay;                //displays day to user
    private EditText displayYear;               //displays year to user
    private EditText taskContents;              //allows user to enter in their task as text

    /**onCreate
     * Initializes Add Activity
     * @param savedInstanceState    Bundle passed into activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_del);

        //initializes task database
        taskDB = new DatabaseHelper(this);

        //initializes EditText fields month, day, year, and allows editing
        displayMonth = findViewById(R.id.textEditMonth);
        displayDay = findViewById(R.id.textEditDay);
        displayYear = findViewById(R.id.textEditYear);
        //initializes EditText field to populate task selected, and allows editing
        taskContents = findViewById(R.id.textEditTask);

        //unpacks extra from previous Activity, populates EditText fields
        populateFields();

        //error checks for valid EditText fields, click Save Button updates row data in database
        saveUpdateButton();

        //button deletes task from database
        deleteTaskButton();
    }

    /**populateFields
     * Using id from previous activity, gets associated row from database, and assigns values to var's.
     * Populates EditText fields with retrieved data
     */
    private void populateFields() {
        //get extra from previous Activity, assigns to long
        Intent chosenID = getIntent();
        long longID = chosenID.getLongExtra(getString(R.string.text_id), -1);

        //converts long to integer
        id = (int) longID;

        //gets row of data from database matching id
        Cursor cursor = taskDB.getRowByID(id);

        //assigns cursor data to variables
        int month = cursor.getInt(cursor.getColumnIndex("month"));
        int day = cursor.getInt(cursor.getColumnIndex("day"));
        int year = cursor.getInt(cursor.getColumnIndex("year"));
        String taskString = cursor.getString(cursor.getColumnIndex("taskData"));

        //month +1 cause android starts January as 0, February as 1, etc.
        month = month + 1;
        //populates EditText fields with data selected by user
        displayMonth.setText(String.valueOf(month));
        displayDay.setText(String.valueOf(day));
        displayYear.setText(String.valueOf(year));
        taskContents.setText(taskString);

        Log.d(TAG, "EditTexts populated with date data");
    }

    /**saveUpdateButton
     * Configures Save Button, onClick validates date input and task description input from user,
     * updates database row for selected task
     */
    private void saveUpdateButton() {
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean goodYear = true;            //default true, invalid input switches to false
                Boolean goodMonth = true;           //default true, invalid input switches to false
                Boolean goodDay = true;             //default true, invalid input switches to false
                int newYear = -1;                   //-1 not used, default for error checking only
                int newMonth = -1;                  //-1 not used, default for error checking only
                int newDay = -1;                    //-1 not used, default for error checking only

                //Error checks for year
                //Checks if empty
                if(displayYear.length() != 0) {
                    //Error checks for EditText, try to convert to integer
                    try {
                        newYear = Integer.parseInt(displayYear.getText().toString());

                        //Error checks for year value 2018 to 2200
                        if(newYear <= 2000 || newYear >= 3000) {
                            toastMessage("Error: Year must be within millennium");
                            goodYear = false;
                        } //else do nothing, valid year input
                    } catch (NumberFormatException e) {
                        toastMessage("Error: Year must be number");
                        goodYear = false;
                    }
                } else {
                    toastMessage("Error: Year field empty");
                    goodYear = false;
                }

                //Error checks for month
                //Checks if empty, and passed valid Year error checking
                if(displayMonth.length() != 0) {
                    //Error checks for EditText, try to convert to integer
                    try {
                        newMonth = Integer.parseInt(displayMonth.getText().toString());

                        //Error checks for month value 1-12
                        if(newMonth < 1 || newMonth > 12) {
                            toastMessage("Error: Month must be 1-12");
                            goodMonth = false;
                        } else {
                            newMonth = newMonth - 1;//valid month input, variable updated cause Android
                        }
                    } catch (NumberFormatException e) {
                        toastMessage("Error: Month must be number");
                        goodMonth = false;
                    }
                } else {
                    toastMessage("Error: Month field empty");
                    goodMonth = false;
                }

                //Error checks for day
                //Checks if empty, and passed valid Year and Month error checking
                if(displayDay.length() != 0) {
                    //Error checks for EditText, try to convert to integer
                    try {
                        newDay = Integer.parseInt(displayDay.getText().toString());

                        //Error checks for day value 1-31 for Jan, Mar, May, July, Aug, Oct, Dec
                        if(newMonth == 0 || newMonth == 2 || newMonth == 4 || newMonth == 6
                                || newMonth == 7 || newMonth == 9 || newMonth == 11) {
                            if(newDay < 1 || newDay > 31) {
                                toastMessage("Error: Day must be 1-31");
                                goodDay = false;
                            } //else do nothing, valid day input
                        }

                        //Error checks for day value 1-30 for Apr, June, Sept, Nov
                        else if(newMonth == 3 || newMonth == 5 || newMonth == 8 || newMonth == 10) {
                            if(newDay < 1 || newDay > 30) {
                                toastMessage("Error: Day must be 1-30");
                                goodDay = false;
                            } //else do nothing, valid day input
                        }

                        //Error checks for day value 1-28 or 1-29 for Feb
                        else if(newMonth == 1) {
                            if(newYear%4 == 0) {                //leap year, year modulo every 4 years
                                if(newDay < 1 || newDay > 29) {
                                    toastMessage("Error: Day must be 1-29");
                                    goodDay = false;
                                } //else do nothing, valid day input
                            }
                            else {                              //non-leap year
                                if(newDay < 1 || newDay > 28) {
                                    toastMessage("Error: Day must be 1-28");
                                    goodDay = false;
                                } //else do nothing, valid day input
                            }
                        }

                        //should not occur, debug only
                        else {
                            toastMessage("Unknown error occurred");
                            goodDay = false;
                        }
                    } catch (NumberFormatException e) {
                        toastMessage("Error: Day must be number");
                        goodDay = false;
                    }
                } else {
                    toastMessage("Error: Day field empty");
                    goodDay = false;
                }

                //all Boolean true, all error checking passed, continue to update database
                if(goodMonth && goodDay && goodYear) {
                    //if EditText for task description empty error checking
                    if(taskContents.length() != 0) {
                        //updates row in database with new values and leave Activity
                        taskDB.updateData(id, newMonth, newDay, newYear, taskContents.getText().toString());
                        toastMessage("Task Saved");
                        finish();                               //exit
                    } else {
                        toastMessage("Error: Task input field empty");
                    }
                } //else nothing, date input invalid, Error Toast already thrown
            }
        });
    }

    /**deleteTaskButton
     * Configures Delete Button to delete task by id from database, leaves Activity
     */
    private void deleteTaskButton() {
        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDB.deleteRow(id);
                toastMessage("Task Deleted");
                finish();                                       //exit
            }
        });
    }

    /**toastMessage
     * general method to display toast
     * @param message   //String
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
