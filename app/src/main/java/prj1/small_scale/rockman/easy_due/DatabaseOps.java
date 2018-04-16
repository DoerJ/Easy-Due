package prj1.small_scale.rockman.easy_due;

/**
 * Created by Rockman on 2017-08-23.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOps extends SQLiteOpenHelper {

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE " + TableData.Table.TABLE_NAME + "(" + TableData.Table.TASK_NAME
             + " TEXT," + TableData.Table.TASK_YEAR + " INTEGER," + TableData.Table.TASK_MONTH + " INTEGER,"
            + TableData.Table.TASK_DAY + " INTEGER," + TableData.Table.TASK_HOUR + " INTEGER," + TableData.Table.TASK_MINUTE + " INTEGER);";

    public DatabaseOps(Context context) {     // default constructor

        super(context, TableData.Table.DATABASE_NAME, null, database_version);
        Log.d("database operation", "table is created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   // create a new SQLiteDatabase object

        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {

        db.execSQL(CREATE_QUERY);
        onCreate(db);
    }

    public void insertInfo(DatabaseOps db, String task_name, int year, int month, int day, int hour, int minute) {    // insert new_task info

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues content_list = new ContentValues();
        content_list.put(TableData.Table.TASK_NAME, task_name);
        content_list.put(TableData.Table.TASK_YEAR, year);
        content_list.put(TableData.Table.TASK_MONTH, month);
        content_list.put(TableData.Table.TASK_DAY, day);
        content_list.put(TableData.Table.TASK_HOUR, hour);
        content_list.put(TableData.Table.TASK_MINUTE, minute);

        // returning the primary key value of new row
        long row_id = sql.insert(TableData.Table.TABLE_NAME, null, content_list);
        Log.d("database operation", "a new task has been inserted");
    }

        // delete row from table
    public int deleteInfo(DatabaseOps db, String param) {

        SQLiteDatabase sql = db.getWritableDatabase();
        String selection = TableData.Table.TASK_NAME + " LIKE ?";
        String[] selectionArgus = { param };
        int delete_id = sql.delete(TableData.Table.TABLE_NAME, selection, selectionArgus);
        return delete_id;
    }

    public Cursor getInfo(DatabaseOps db) {         // query table

        Cursor cursor;
        SQLiteDatabase sql = db.getReadableDatabase();
        String[] query = {TableData.Table.TASK_NAME, TableData.Table.TASK_YEAR, TableData.Table.TASK_MONTH, TableData.Table.TASK_DAY, TableData.Table.TASK_HOUR, TableData.Table.TASK_MINUTE};
        cursor = sql.query(TableData.Table.TABLE_NAME, query, null, null, null, null, null);
        return cursor;
    }

    public long getRow(DatabaseOps db) {

        long row_count;
        SQLiteDatabase sql = db.getReadableDatabase();
        row_count = DatabaseUtils.queryNumEntries(sql, TableData.Table.TABLE_NAME);
        return row_count;
    }

}
