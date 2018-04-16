package prj1.small_scale.rockman.easy_due;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.Spannable;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rockman on 2017-08-23.
 */

public class MySchedule extends Activity {

    int row_count;
    int day_length, hour_length, minute_length, second_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        listDate();
        setupBackBtn();
    }

    public void listDate() {

        final LinearLayout ll = (LinearLayout) findViewById(R.id.task_layout);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        DatabaseOps db = new DatabaseOps(getBaseContext());
        final Cursor find = db.getInfo(db);

        // count the numbers of rows of table
        row_count = find.getCount();
        Log.d("database", "row count: " + Integer.toString(row_count));

        // get the current time in the format of "dd/MM/yyyy hh:mm"
        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        String current_time = date_format.format(cal.getTime());

        // convert current_time string to current_time Date object
        Date convert_current = new Date();

        try {

            convert_current = date_format.parse(current_time);
        } catch(ParseException e) {

            e.printStackTrace();
        }

        Log.d("activity", "current time is: " + current_time);

        // move the cursor to the first row of table
        if(find.getCount() > 0) {

            find.moveToFirst();
        }

        Date convert_task = new Date();
        long difference;

        if(find.getCount() > 0) {
            do {

                // selectionArgus(task_name) for deleting selected task
                final String selectionArgus = find.getString(0);

                // TextView: task_name
                final TextView task_name = new TextView(this);
                task_name.setLayoutParams(lp);
                task_name.setText(find.getString(0));
                task_name.setTextColor(Color.parseColor("#ffffff"));
                task_name.setTypeface(null, Typeface.BOLD);
                task_name.setPadding(0, 200, 0, 0);
                task_name.setTextSize(16);
                task_name.setGravity(Gravity.CENTER);

                // remove child for task_name
                if (task_name.getParent() != null) {

                    ((ViewGroup) task_name.getParent()).removeView(task_name);
                }
                ll.addView(task_name);

                // set TextView(task_name) clickable ---------------------
                task_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // dialog appears
                        AlertDialog.Builder builder = new AlertDialog.Builder(MySchedule.this);
                        builder.setMessage("Delete this task?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // delete button, delete task from database
                                DatabaseOps db = new DatabaseOps(getBaseContext());
                                db.deleteInfo(db, selectionArgus);
                                startActivity(new Intent(getBaseContext(), MySchedule.class));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // back to MySchedule interface
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                // ---------------------------------------------------

                // TextView: due
                final TextView task = new TextView(this);
                task.setLayoutParams(lp);
                task.setGravity(Gravity.CENTER);

                // remove child
                if (task.getParent() != null) {

                    ((ViewGroup) task.getParent()).removeView(task);
                }
                ll.addView(task);

                // format: "dd/MM/yyyy hh:mm:ss"
                // the time of task
                String task_time = find.getString(3) + "/" + find.getString(2) + "/" + find.getString(1) +
                        " " + find.getString(4) + ":" + find.getString(5) + ":" + "00";

                // convert the task_time string to task_time Date
                try {

                    convert_task = date_format.parse(task_time);
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                // calculate the date difference in milliseconds
                // - 12 hours
                difference = convert_task.getTime() - convert_current.getTime() - 43200000;

                // show the time difference in seconds
                Log.d("activity", "The time difference is: " + difference / 1000);

                // countDownTimer
                // start counting down
                CountDownTimer count_down = new CountDownTimer(difference, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        // DAY count_down
                        long days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

                        // HOUR count_down
                        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) -
                                TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));

                        // MINUTE count_down
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

                        // SECOND count_down
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                        // get the length of days, hours, minutes, and seconds
                        day_length = String.valueOf(days).length();
                        hour_length = String.valueOf(hours).length();
                        minute_length = String.valueOf(minutes).length();
                        second_length = String.valueOf(seconds).length();

                        // set Spannable to selectively color text
                        Spannable due_time = new SpannableString("Time Left: " + days + " Days " + hours + " Hours " + minutes + " Minutes "
                                + seconds + " seconds ");

                        // days color change
                        due_time.setSpan(new ForegroundColorSpan(Color.parseColor("#30e3a9")), 11, 11 + day_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // hours color change
                        due_time.setSpan(new ForegroundColorSpan(Color.parseColor("#30e3a9")), 17 + day_length, 17 + day_length + hour_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // minutes color change
                        due_time.setSpan(new ForegroundColorSpan(Color.parseColor("#30e3a9")), 24 + day_length + hour_length, 24 + day_length +
                                hour_length + minute_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // seconds color change
                        due_time.setSpan(new ForegroundColorSpan(Color.parseColor("#30e3a9")), 33 + day_length + hour_length + minute_length, 33 +
                                day_length + hour_length + minute_length + second_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        task.setText(due_time);
                        task.setTextColor(Color.parseColor("#ffffff"));
                    }

                    @Override
                    public void onFinish() {

                        // remove child for task_name
                        if (task_name.getParent() != null) {

                            ((ViewGroup) task_name.getParent()).removeView(task_name);
                        }
                        ll.addView(task_name);

                        // remove child
                        if (task.getParent() != null) {

                            ((ViewGroup) task.getParent()).removeView(task);
                        }

                        task.setText("Time is Up!");
                        task.setTextColor(Color.parseColor("#ffffff"));
                        ll.addView(task);
                    }

                }.start();


            } while (find.moveToNext());
        }
    }

    // set up get_back button to MainActivity.class
    public void setupBackBtn() {

        Button getBack = (Button) findViewById(R.id.get_backfromSchedule);
        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MySchedule.this, MainActivity.class));
            }

        });
    }
}
