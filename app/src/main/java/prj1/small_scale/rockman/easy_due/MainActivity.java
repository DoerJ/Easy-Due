package prj1.small_scale.rockman.easy_due;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    // the entry of the system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up two buttons: new_task, my_schedule
        setupNewTaskBtn();
        setupMyScheduleBtn();
    }

    private void setupNewTaskBtn(){

        Button btn = (Button) findViewById(R.id.newtask_button);  // create button by id
        btn.setOnClickListener(new View.OnClickListener() {  // initialize a new click event
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewTask.class));
            }
        });


    }

    private void setupMyScheduleBtn(){

        Button btn = (Button) findViewById(R.id.myschedule_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, MySchedule.class));
            }
        });
    }
}
