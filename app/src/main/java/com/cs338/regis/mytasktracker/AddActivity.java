/*AddActivity
 *Activity where user can add a Task.
 */

package com.cs338.regis.mytasktracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**@author Grant Kim
 * @version 1.0, Regis University CS338 Summer 8-week 1
 */
public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";    //used for Log
    private DatabaseHelper taskDB;                      //creates instance of DatabaseHelper class

    private int month;                          //assigned from bundle, month chosen by user
    private int day;                            //assigned from bundle, day of month chosen by user
    private int year;                           //assigned from bundle, year chosen by user
    private EditText taskContents;              //allows user to enter their task description

    /**onCreate
     * Initializes Add Activity
     * @param savedInstanceState    Bundle passed into activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //initializes task database
        taskDB = new DatabaseHelper(this);

        //unpacks Bundle and displays date at top TextView
        setTextDate();

        //assigns EditText field
        taskContents = findViewById(R.id.textAddTask);

        //configures Save Button to add task to database
        setupSaveButton();

        //configures Cancel Button to exit Activity
        setupCancelButton();
    }

    /**setTextDate
     * Unpacks Bundle which contains Date information chosen by user. Formats to String,
     * displays at top in TextView
     */
    private void setTextDate() {
        //assigns textView's in activity_add.xml
        TextView displayMonth = findViewById(R.id.textAddMonth);
        TextView displayDay = findViewById(R.id.textAddDay);
        TextView displayYear = findViewById(R.id.textAddYear);

        //get Bundle containing date integers from CalendarActivity chosen by user
        Bundle chosenDate = this.getIntent().getBundleExtra("chosenDate");
        //assign variables month, day, year from bundle chosenDate
        month = chosenDate.getInt(getString(R.string.bundleKey_month));
        day = chosenDate.getInt(getString(R.string.bundleKey_day));
        year = chosenDate.getInt(getString(R.string.bundleKey_year));

        //converts int's to string for date display
        int visualMonth = month + 1;               //Jan in Android is zero, +1 for all months
        String sMonth = String.valueOf(visualMonth);
        String sDay = String.valueOf(day);
        String sYear = String.valueOf(year);

        //populates month, day, year from Bundle to textView's
        displayMonth.setText(sMonth);
        displayDay.setText(sDay);
        displayYear.setText(sYear);

        Log.d(TAG, "date displayed for TextView banner");
    }

    /**setupSaveButton
     * When user clicks save, takes String from EditText field and inserts into database,
     * and exits Activity
     */
    private void setupSaveButton() {
        Button buttonSave = findViewById(R.id.buttonSave2);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks if field is empty, if not, continues to add data to database
                if(taskContents.length() != 0){
                    //inserts data into database, calling method from DatabaseHelper class
                    //using date from Bundle passed from CalendarActivity and EditText from user
                    //returns false from DatabaseHelper if add error
                    Boolean insertData = taskDB.addData(month, day, year, taskContents.getText().toString());

                    //insertData returns as true if SQLite assigns ID to data successfully
                    if(insertData) {
                        toastMessage("Task Added");
                    } else {                                    //if -1 returned, insertData = false
                        toastMessage("Unknown error occurred");
                    }
                    finish();                                   //exit activity
                } else {                                        //if EditText field is empty
                    toastMessage("Error: Text field is empty");
                }
            }
        });
    }

    /**setupCancelButton
     * Exits Activity with finish();
     */
    private void setupCancelButton() {
        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
