package prj1.small_scale.rockman.easy_due;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
/**
 * Created by Rockman on 2017-09-17.
 */

public class WelcomePage extends Activity {

    private static int splash_time_out = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // set up the handler
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                startActivity(new Intent(WelcomePage.this, MainActivity.class));
                finish();
            }
        }, splash_time_out);
    }
}
