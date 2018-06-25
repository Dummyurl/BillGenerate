package app.zingo.com.billgenerate.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CSC on 2/17/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteProperty.db";
    private static final int DATABASE_VERSION = 2;

    public static final String PROPERTY_TABLE_NAME = "property";
    public static final String PROPERTY_COLUMN_ID = "_id";
    public static final String PROPERTY_COLUMN_NAME = "name";
    public static final String PROPERTY_TARGET_PRICE = "target";
    public static final String PROPERTY_SELL_PRICE = "sell";

    public String property_name;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + PROPERTY_TABLE_NAME +
                        "(" + PROPERTY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        PROPERTY_COLUMN_NAME + " TEXT, " +
                        PROPERTY_TARGET_PRICE + " REAL, " +

                        PROPERTY_SELL_PRICE + " REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROPERTY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertProperty(String name, double target, double sell) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROPERTY_COLUMN_NAME, name);
        contentValues.put(PROPERTY_TARGET_PRICE, target);
        contentValues.put(PROPERTY_SELL_PRICE, sell);


        db.insert(PROPERTY_TABLE_NAME, null, contentValues);
        System.out.println(" Added");
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PROPERTY_TABLE_NAME);
        return numRows;
    }


    public Cursor getProperty(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + PROPERTY_TABLE_NAME + " WHERE " +
                PROPERTY_COLUMN_NAME + "=?", new String[]{name});
        property_name = res.getString(res.getColumnIndex(name));
        System.out.println("Name=="+property_name);
        return res;
    }

    public Cursor getAllProperty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + PROPERTY_TABLE_NAME, null );
       // String s1 = res.getString(res.getColumnNames());
        List<Double> array = new ArrayList<Double>();
        List<Double> arrays = new ArrayList<Double>();
        while(res.moveToNext()){
            double target = res.getDouble(res.getColumnIndex("target"));
            double sell = res.getDouble(res.getColumnIndex("sell"));

            array.add(target);
            array.add(sell);
        }
        return res;
    }
}