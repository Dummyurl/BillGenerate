package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CSC on 1/20/2018.
 */

public class SearchBooking {

    @SerializedName("CheckInDate")
    private String fromdate;

    @SerializedName("CheckOutDate")
    private String toDate;

    @SerializedName("PhoneNumber")
    private String mobile;

    @SerializedName("HotelId")
    private int hotelId;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
