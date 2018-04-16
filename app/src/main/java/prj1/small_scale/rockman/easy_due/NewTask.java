package prj1.small_scale.rockman.easy_due;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.TextView;

import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Rockman on 2017-08-23.
 */

public class NewTask extends Activity {

    // EditText and button
    EditText TASK_NAME;
    TextView TASK_DATE;
    TextView TASK_TIME;
    Context context = this;

    // datePickerDialog
    DatePickerDialog.OnDateSetListener my_datePicker;

    // timePickerDialog
    TimePickerDialog.OnTimeSetListener my_timePicker;

    String task_name, task_date, task_time;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);

        // initialize EditText
        TASK_NAME = (EditText) findViewById(R.id.task_name);
        TASK_DATE = (TextView) findViewById(R.id.time_inputforday);
        TASK_TIME = (TextView) findViewById(R.id.time_inputfortime);

        // get the year, month, day
        getDate();

        // get the hour and minute
        getTime();

        // set up the complete button
        setupCompleteBtn();

        // set up the back button
        setupGetBackBtn();
    }

    public void getDate() {     // get the year, month, day of the new task

        TASK_DATE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // create a new calendar
                Calendar calendar = Calendar.getInstance();

                // get the year on calendar
                int year = calendar.get(Calendar.YEAR);

                // get the month on calendar
                int month = calendar.get(Calendar.MONTH);

                // get the day of month on calendar
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // create a new datePickerDialog
                // (context, style, datePickerDialog.OnDateSetListener, year, month, day of month)
                DatePickerDialog date_dialog = new DatePickerDialog(NewTask.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, my_datePicker, year, month, day);

                // configure the background of the dialog
                date_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show the dialog and display it on screen
                date_dialog.show();
            }
        });

        // set up the OnDateSetListener
        // setDateSetListener is used to indicate user has finished selecting the date
        my_datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet (DatePicker date_picker, int year, int month, int day) {

                // Jan is 0, Feb is 1 and so on, we need to increment month by 1
                month = month + 1;

                String show_date = year + ". " + month + ". " + day + ". ";
                TASK_DATE.setText(show_date);
                TASK_DATE.setTextColor(Color.parseColor("#ffffff"));
            }
        };

    }

    public void getTime() {

        TASK_TIME.setOnClickListener(new View.OnClickListener() {

          @Override
            public void onClick(View view) {

              Calendar calendar1 = Calendar.getInstance();

              // get the hour on calendar
              int hour = calendar1.get(Calendar.HOUR_OF_DAY);
              int minute = calendar1.get(Calendar.MINUTE);

              // create a new timePickerDialog
              // (context, timePickerDialog.OnTimeSetListener, int hourOfDay, int minute, boolean is24HourView)
              TimePickerDialog time_dialog = new TimePickerDialog(NewTask.this, my_timePicker, hour, minute, true);
              time_dialog.show();
          }
        });

        // set up the OnTimeSetListener
        my_timePicker = new TimePickerDialog.OnTimeSetListener() {

          @Override
            public void onTimeSet(TimePicker time_picker, int hour, int minute) {

              String show_time = hour + ": " + minute;
              TASK_TIME.setText(show_time);
              TASK_TIME.setTextColor(Color.parseColor("#ffffff"));
          }
        };

    }

    public void setupCompleteBtn() {

        Log.d("activity", "complete button");

        // initialize button COMPLETE
        Button TASK_COMPLETE = (Button) findViewById(R.id.task_complete);

        TASK_COMPLETE.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                int field_count = 0;

                Log.d("activity", Integer.toString(field_count));

                task_name = TASK_NAME.getText().toString();
                task_time = TASK_TIME.getText().toString();

                // convert the hour and minute into integers
                if(!task_time.isEmpty()) {

                    String time_segment[] = task_time.split(": ");
                    hour = Integer.parseInt(time_segment[0]);
                    minute = Integer.parseInt(time_segment[1]);
                }

                // extract the year, month, day of month out of TextView
                task_date = TASK_DATE.getText().toString();

                // count empty input field
                if (task_name.isEmpty()) {

                    field_count = field_count + 1;
                }

                if (task_date.isEmpty()) {

                    field_count = field_count + 1;
                }

                if (task_time.isEmpty()) {

                    field_count = field_count + 1;
                }

                // -------------------------------------------------------
                // when hit COMPLETE button, check if the inputs are valid
                if (task_name.isEmpty() && field_count == 1) {

                    // a Toast appears, indicating a task name is required
                    Toast.makeText(getApplicationContext(), "PLEASE SPECIFY THE NAME OF NEW TASK", Toast.LENGTH_LONG).show();
                } else if (task_time.isEmpty() && field_count == 1) {

                    // a Toast indicating user needs to input the minute of due date
                    Toast.makeText(getApplicationContext(), "PLEASE SPECIFY THE DUE TIME", Toast.LENGTH_LONG).show();
                } else if (task_date.isEmpty() && field_count == 1) {

                    // a Toast indicating user needs to input the year, month, and day of due date
                    Toast.makeText(getApplicationContext(), "PLEASE SPECIFY YEAR, MONTH, DAY OF DUE", Toast.LENGTH_LONG).show();
                } else if (field_count >= 1) {

                    Toast.makeText(getApplicationContext(), "PLEASE ENTER COMPLETE INFO", Toast.LENGTH_LONG).show();
                } else {

                    // extract year, month, day of month out of str
                    String date_segment[] = task_date.split(". ");
                    int year = Integer.parseInt(date_segment[0]);
                    int month = Integer.parseInt(date_segment[1]);
                    int day = Integer.parseInt(date_segment[2]);

                    // insert the task_name, year, month, day of month, hour, minute into database
                    DatabaseOps db = new DatabaseOps(getBaseContext());
                    db.insertInfo(db, task_name, year, month, day, hour, minute);

                    // back to MainActivity.class
                    Toast.makeText(getApplicationContext(), "YOUR TASK HAS BEEN ADDED", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(NewTask.this, MainActivity.class));
                }
            }
        });
    }
    // set up button for exitting
    private void setupGetBackBtn() {

        Button getBack_task = (Button) findViewById(R.id.get_backfromTask);
        getBack_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NewTask.this, MainActivity.class));
            }
        });

    }

}
