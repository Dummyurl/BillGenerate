package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CSC on 11/29/2017.
 */

public class Traveller implements Serializable {

    @SerializedName("BookingTravellerId")
    private int TravellerId;

    @SerializedName("FirstName")
    private String FirstName;

    @SerializedName("MiddleName")
    private String MiddleName;

    @SerializedName("LastName")
    private String LastName;

    @SerializedName("Email")
    private String Email;

    @SerializedName("PhoneNumber")
    private String PhoneNumber;

    @SerializedName("Address")
    private String Address;

    @SerializedName("PinCode")
    private String PinCode;

    @SerializedName("UserRoleId")
    private int UserRoleId;

    @SerializedName("PlaceName")
    private String PlaceName;

    @SerializedName("booking")
    private Bookings1 booking;

    @SerializedName("BookingId")
    private int BookingId;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("DOB")
    private String dob;


   /* @SerializedName("travellerDocumentsList")
    private ArrayList<TravellerDocuments> travellerDocumentsList;*/

    public Traveller() {
    }

    public int getTravellerId() {
        return TravellerId;
    }

    public void setTravellerId(int travellerId) {
        TravellerId = travellerId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public int getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        UserRoleId = userRoleId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public Bookings1 getBooking() {
        return booking;
    }

    public void setBooking(Bookings1 booking) {
        this.booking = booking;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

   /* public ArrayList<TravellerDocuments> getTravellerDocumentsList() {
        return travellerDocumentsList;
    }

    public void setTravellerDocumentsList(ArrayList<TravellerDocuments> travellerDocumentsList) {
        this.travellerDocumentsList = travellerDocumentsList;
    }*/

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
