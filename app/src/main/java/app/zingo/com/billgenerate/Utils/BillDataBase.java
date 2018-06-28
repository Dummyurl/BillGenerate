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

public class BillDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteBill.db";
    private static final int DATABASE_VERSION = 2;

    public static final String BILL_TABLE_NAME = "bill";
    public static final String BILL_COLUMN_ID = "_id";
    public static final String BILL_COLUMN_BOOKINGID = "bookingid";
    public static final String BILL_COLUMN_PROPERTY = "property";
    public static final String BILL_COLUMN_LOCATION = "location";
    public static final String BILL_COLUMN_CITY = "city";
    public static final String BILL_COLUMN_GUEST = "guest";
    public static final String BILL_COLUMN_MOBILE = "mobile";
    public static final String BILL_COLUMN_BDATE = "bdate";
    public static final String BILL_COLUMN_CID = "cid";
    public static final String BILL_COLUMN_COD = "cod";
    public static final String BILL_COLUMN_ROOMTYPE = "type";
    public static final String BILL_COLUMN_NOR = "nor";
    public static final String BILL_COLUMN_TOTGUEST = "totguest";
    public static final String BILL_COLUMN_PLAN = "plan";
    public static final String BILL_COLUMN_PAYMENT = "payment";
    public static final String BILL_COLUMN_INCLUSION = "desc";
    public static final String BILL_COLUMN_AMOUNT = "amount";
    public static final String BILL_COLUMN_BOOKING = "booking";
    public static final String BILL_COLUMN_ZINGO = "zingo";



    public BillDataBase(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + BILL_TABLE_NAME +
                        "(" + BILL_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        BILL_COLUMN_BOOKINGID + " TEXT, " +
                        BILL_COLUMN_PROPERTY + " TEXT, " +
                        BILL_COLUMN_LOCATION + " TEXT, " +
                        BILL_COLUMN_CITY + " TEXT, " +
                        BILL_COLUMN_GUEST + " TEXT, " +
                        BILL_COLUMN_MOBILE + " TEXT, " +
                        BILL_COLUMN_BDATE + " TEXT, " +
                        BILL_COLUMN_CID + " TEXT, " +
                        BILL_COLUMN_COD + " TEXT, " +
                        BILL_COLUMN_ROOMTYPE + " TEXT, " +
                        BILL_COLUMN_NOR + " TEXT, " +
                        BILL_COLUMN_TOTGUEST + " TEXT, " +
                        BILL_COLUMN_PLAN + " TEXT, " +
                        BILL_COLUMN_PAYMENT + " TEXT, " +
                        BILL_COLUMN_INCLUSION + " TEXT, " +
                        BILL_COLUMN_AMOUNT + " TEXT, " +
                        BILL_COLUMN_BOOKING + " TEXT, " +
                        BILL_COLUMN_ZINGO + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BILL_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertBILL(String bookingID,String property,String city,String location,String guest,
                              String mobile,String bdate,String cit,String cot,String room,
                              String nor,String totguest,String plan,String payment,
                              String inclusion,String amount,String booking,String zingo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BILL_COLUMN_BOOKINGID, bookingID);
        contentValues.put(BILL_COLUMN_PROPERTY, property);
        contentValues.put(BILL_COLUMN_LOCATION, city);
        contentValues.put(BILL_COLUMN_CITY, location);
        contentValues.put(BILL_COLUMN_GUEST, guest);
        contentValues.put(BILL_COLUMN_MOBILE, mobile);
        contentValues.put(BILL_COLUMN_BDATE, bdate);
        contentValues.put(BILL_COLUMN_CID, cit);
        contentValues.put(BILL_COLUMN_COD, cot);
        contentValues.put(BILL_COLUMN_ROOMTYPE, room);
        contentValues.put(BILL_COLUMN_NOR, nor);
        contentValues.put(BILL_COLUMN_TOTGUEST, totguest);
        contentValues.put(BILL_COLUMN_PLAN, plan);
        contentValues.put(BILL_COLUMN_PAYMENT, payment);
        contentValues.put(BILL_COLUMN_INCLUSION, inclusion);
        contentValues.put(BILL_COLUMN_AMOUNT, amount);
        contentValues.put(BILL_COLUMN_BOOKING, booking);
        contentValues.put(BILL_COLUMN_ZINGO, zingo);



        db.insert(BILL_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BILL_TABLE_NAME);
        return numRows;
    }


    public Cursor getBILL(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + BILL_TABLE_NAME + " WHERE " +
                BILL_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllBILL() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + BILL_TABLE_NAME, null );
        return res;
    }
}
