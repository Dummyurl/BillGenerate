package app.zingo.com.billgenerate.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CSC on 2/17/2018.
 */

public class PlanDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLitePlan.db";
    private static final int DATABASE_VERSION = 2;

    public static final String PLAN_TABLE_NAME = "plan";
    public static final String PLAN_COLUMN_ID = "_id";
    public static final String PLAN_COLUMN_NAME = "name";
    public static final String PLAN_COLUMN_DESC = "desc";

    public PlanDataBase(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + PLAN_TABLE_NAME +
                        "(" + PLAN_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        PLAN_COLUMN_NAME + " TEXT, " +
                        PLAN_COLUMN_DESC + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLAN_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPLAN(String name, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLAN_COLUMN_NAME, name);
        contentValues.put(PLAN_COLUMN_DESC, desc);

        db.insert(PLAN_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PLAN_TABLE_NAME);
        return numRows;
    }


    public Cursor getPLAN(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + PLAN_TABLE_NAME + " WHERE " +
                PLAN_COLUMN_NAME + "=?", new String[]{name});
        return res;
    }

    public Cursor getAllPLAN() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + PLAN_TABLE_NAME, null );
        return res;
    }
}