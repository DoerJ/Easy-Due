package prj1.small_scale.rockman.easy_due;

/**
 * Created by Rockman on 2017-08-23.
 */

import android.provider.BaseColumns;

public class TableData {

    // constructor
    public TableData(){

    }

    public static abstract class Table implements BaseColumns{

        public static final String DATABASE_NAME =  "time_table";
        public static final String TABLE_NAME = "time";
        public static final String TASK_NAME = "task";
        public static final String TASK_YEAR = "year";
        public static final String TASK_MONTH = "month";
        public static final String TASK_DAY = "day";
        public static final String TASK_HOUR = "hour";
        public static final String TASK_MINUTE = "minute";
    }
}
