package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CSC on 1/20/2018.
 */

public class SearchBook {

    @SerializedName("FirstName")
    private String FirstName;

    @SerializedName("LastName")
    private String LastName;
    @SerializedName("BookingNumber")
    private String BookingNumber;
    @SerializedName("CheckInDate")
    private String CheckInDate;
    @SerializedName("CheckOutDate")
    private String CheckOutDate;
    @SerializedName("BookingStatus")
    private String BookingStatus;
    @SerializedName("RoomId")
    private int RoomId;
    @SerializedName("RoomNo")
    private String RoomNo;
    @SerializedName("BookingId")
    private int BookingId;

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getBookingNumber() {
        return BookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        BookingNumber = bookingNumber;
    }

    public String getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return CheckOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        CheckOutDate = checkOutDate;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        BookingStatus = bookingStatus;
    }
}
