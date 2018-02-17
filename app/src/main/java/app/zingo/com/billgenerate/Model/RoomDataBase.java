package app.zingo.com.billgenerate.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CSC on 2/17/2018.
 */

public class RoomDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteRoom.db";
    private static final int DATABASE_VERSION = 2;

    public static final String ROOM_TABLE_NAME = "room";
    public static final String ROOM_COLUMN_ID = "_id";
    public static final String ROOM_COLUMN_NAME = "name";

    public RoomDataBase(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + ROOM_TABLE_NAME +
                        "(" + ROOM_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        ROOM_COLUMN_NAME + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ROOM_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertROOM(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ROOM_COLUMN_NAME, name);

        db.insert(ROOM_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ROOM_TABLE_NAME);
        return numRows;
    }


    public Cursor getROOM(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + ROOM_TABLE_NAME + " WHERE " +
                ROOM_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllROOM() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + ROOM_TABLE_NAME, null );
        return res;
    }
}
