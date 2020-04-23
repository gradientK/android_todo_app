/*CalendarActivity
  Integrates Android CalendarView, allows users to select date for recording tasks.
 */

package com.cs338.regis.mytasktracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

/**@author Grant Kim
 * @version 1.0, Regis University CS338 Summer 8-week 1
 */
public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";   //used for Log
    private Bundle bundleDate;                  //contains month, date, year int's from CalendarView

    /**onCreate
     * Initializes myCalendarView
     * @param savedInstanceState    Bundle passed into activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //displays Toolbar
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        //Floating Action Button - click to go finish and go to MainActivity
        FloatingActionButton goToHome = findViewById(R.id.fabGoHome);
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            //click and go to Home Screen
            public void onClick(View view) {
                finish();
            }
        });

        //calls setupCalendar() to define CalendarView actions
        clickCalendarView();
    }

    /**clickCalendarView
     * Defines CalendarView and its actions for listeners when user clicks on a date.
     * Passes date selected information as ints, passes to DateViewActivity as Extras.
     */
    private void clickCalendarView() {
        CalendarView calendarView = findViewById(R.id.myCalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateChange: year " + year + ", month " + month + ", dayOfMonth"
                        + dayOfMonth);

                Intent goDateView = new Intent(CalendarActivity.this, DateViewActivity.class);
                //bundle month, dayOfMonth, year, (key set in strings.xml)
                bundleDate = new Bundle();
                bundleDate.putInt(getString(R.string.bundleKey_month), month);
                bundleDate.putInt(getString(R.string.bundleKey_day), dayOfMonth);
                bundleDate.putInt(getString(R.string.bundleKey_year), year);
                goDateView.putExtra("chosenDate", bundleDate);
                startActivity(goDateView);
            }
        });
    }
}
