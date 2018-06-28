package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels.com on 10-11-2017.
 */


public class ContactDetails implements Serializable{

    @SerializedName("ContactId")
    private int ContactId;
    @SerializedName("HotelPhone")
    private String HotelPhone;
    @SerializedName("HotelMobile")
    private String HotelMobile;
    @SerializedName("HotelEmail")
    private String HotelEmail;
    @SerializedName("PhoneList")
    private String PhoneList;
    @SerializedName("EmailList")
    private String EmailList;
    @SerializedName("WebsiteList")
    private String WebsiteList;
    @SerializedName("HotelId")
    private int HotelId;
    @SerializedName("Hotels")
    ArrayList<HotelDetails> Hotels;

    public ContactDetails()
    {

    }

    public ContactDetails(int ContactId, String HotelPhone, String HotelMobile, String HotelEmail, String PhoneList,
                          String EmailList, String WebsiteList, int HotelId, ArrayList<HotelDetails> Hotels)
    {
        this.ContactId = ContactId;
        this.HotelPhone = HotelPhone;
        this.HotelMobile = HotelMobile;
        this.HotelEmail = HotelEmail;
        this.PhoneList = PhoneList;
        this.EmailList = EmailList;
        this.WebsiteList = WebsiteList;
        this.HotelId = HotelId;
        this.Hotels = Hotels;
    }

    public void setContactId(int contactId) {
        ContactId = contactId;
    }

    public int getContactId() {
        return ContactId;
    }

    public void setHotelPhone(String hotelPhone) {
        HotelPhone = hotelPhone;
    }

    public String getHotelPhone() {
        return HotelPhone;
    }

    public void setHotelMobile(String hotelMobile) {
        HotelMobile = hotelMobile;
    }

    public String getHotelMobile() {
        return HotelMobile;
    }

    public void setHotelEmail(String hotelEmail) {
        HotelEmail = hotelEmail;
    }

    public String getHotelEmail() {
        return HotelEmail;
    }

    public void setEmailList(String emailList) {
        EmailList = emailList;
    }

    public String getEmailList() {
        return EmailList;
    }

    public void setPhoneList(String phoneList) {
        PhoneList = phoneList;
    }

    public String getPhoneList() {
        return PhoneList;
    }

    public void setWebsiteList(String websiteList) {
        WebsiteList = websiteList;
    }

    public String getWebsiteList() {
        return WebsiteList;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotels(ArrayList<HotelDetails> hotels) {
        Hotels = hotels;
    }

    public ArrayList<HotelDetails> getHotels() {
        return Hotels;
    }
}
