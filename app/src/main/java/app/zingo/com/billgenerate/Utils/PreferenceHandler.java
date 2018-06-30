package app.zingo.com.billgenerate.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ZingoHotels.com on 10-11-2017.
 */

public class PreferenceHandler {

    private SharedPreferences sh;

    private PreferenceHandler() {

    }

    private PreferenceHandler(Context mContext) {
        sh = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private static PreferenceHandler instance = null;

    /**
     *
     * @param mContext
     * @return {@link PreferenceHandler}
     */
    public synchronized static PreferenceHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new PreferenceHandler(mContext);
        }
        return instance;
    }


    public void setUserId(int id)
    {
        sh.edit().putInt("user_id",id).apply();
    }

    public int getUserId()
    {
        return sh.getInt("user_id",0);
    }
    public void setListSize(int id)
    {
        sh.edit().putInt("List",id).apply();
    }

    public int getListSize()
    {
        return sh.getInt("List",0);
    }


    public void setUserName(String username)
    {
        sh.edit().putString("user_name",username).apply();
    }

    public String getUserName()
    {
        return sh.getString("user_name","");
    }

    public void setPhoneNumber(String phonenumber)
    {
        sh.edit().putString("user_phonenumber",phonenumber).apply();
    }

    public String getPhoneNumber()
    {
        return sh.getString("user_phonenumber","");
    }

    public void setInventory(String inventoryKey,String inventoryCount )
    {
        sh.edit().putString(inventoryKey,inventoryCount).apply();
    }

    public String getInventory(String inventoryKey)
    {
        return sh.getString(inventoryKey,"");
    }

    public void setBookingFileName(String bookingFileName )
    {
        sh.edit().putString(Constatnts.BOOKING_FILE_NAME,bookingFileName).apply();
    }

    public String getBookingFileName()
    {
        return sh.getString(Constatnts.BOOKING_FILE_NAME,"");
    }

    public void setBookingRefNumber(String bookingRefNumber )
    {
        sh.edit().putString(Constatnts.BOOKING_REF_NO,bookingRefNumber).apply();
    }

    public String getBookingRefNumber()
    {
        return sh.getString(Constatnts.BOOKING_REF_NO,"");
    }

    public void setInventoryTime(String inventoryTimeKey,long inventoryTime )
    {
        sh.edit().putLong(inventoryTimeKey,inventoryTime).apply();
    }

    public long getInventoryTime(String inventoryTimeKey)
    {
        return sh.getLong(inventoryTimeKey,0);
    }


    public void clear(){
        sh.edit().clear().apply();

    }


}
