package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels.com on 09-11-2017.
 */


public class HotelDetails implements Serializable{

    @SerializedName("HotelId")
    private int hotelId;
    @SerializedName("HotelName")
    private String hotelName;
    @SerializedName("AmenitiesList")
    private ArrayList aminetiesList;
   /* @SerializedName("AccountsInfo")
    private ArrayList<BankDetails> accountsInfo;*/
    @SerializedName("room")
    private ArrayList<Rooms> rooms;
    @SerializedName("contact")
    private ArrayList<ContactDetails> contact;
    @SerializedName("DisplayName")
    private String hotelDisplayName;
    @SerializedName("HotelType")
    private String hotelType;
    @SerializedName("StarRating")
    private String starRating;
    @SerializedName("ChainId")
    private int chainId;
    @SerializedName("ChainName")
    private String chainName;
    @SerializedName("BuiltYear")
    private String hotelBuiltYear;
    @SerializedName("NoOfRestaurant")
    private String noofRestuarentsInHotel;
    @SerializedName("NoOfRooms")
    private String noOfRoomsInHotel;
    @SerializedName("NoOfFloors")
    private String noOfFloorsInHotel;
    @SerializedName("Currency")
    private String currencyAccepted;
    @SerializedName("VCCCurrency")
    private String vccCurrencyAccepted;
    @SerializedName("CheckInTime")
    private String standardCheckInTime;
    @SerializedName("CheckOutTime")
    private String standardCheckOutTime;
    @SerializedName("Timezone")
    private String hotelTimeZone;
    @SerializedName("Is24HourCheckIn")
    private boolean is24HourCheckIn;
   /* @SerializedName("document")
    private ArrayList<HotelDocuments> hotelDocuments;*/
    @SerializedName("PlaceName")
    private String hotelStreetAddress;
    @SerializedName("ProfileId")
    private int userId;
    @SerializedName("Localty")
    private String localty;
    @SerializedName("State")
    private String state;
    @SerializedName("Country")
    private String country;
    @SerializedName("City")
    private String city;
    @SerializedName("PinCode")
    private String pincode;
    @SerializedName("IsApproved")
    private boolean isApproved;
   /* @SerializedName("bookingList")
    private Bookings bookingList;*/

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public HotelDetails()
    {

    }

    public HotelDetails(int hotelId, String hotelName, ArrayList aminetiesList,  ArrayList rooms, String hotelDisplayName,
                        String hotelType, String starRating, int chainId, String chainName, String hotelBuiltYear, String noofRestuarentsInHotel,
                        String noOfRoomsInHotel, String noOfFloorsInHotel, String currencyAccepted, String vccCurrencyAccepted,
                        String standardCheckInTime, String standardCheckOutTime, String hotelTimeZone, boolean is24HourCheckIn,
                        String hotelStreetAddress, int userId, String localty, String state, String country, String city, String pincode,ArrayList contacts)
    {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.aminetiesList = aminetiesList;

        this.rooms = rooms;
        this.contact = contacts;
        this.hotelDisplayName = hotelDisplayName;
        this.hotelType = hotelType;
        this.starRating = starRating;
        this.chainId = chainId;
        this.chainName = chainName;
        this.hotelBuiltYear = hotelBuiltYear;
        this.noofRestuarentsInHotel = noofRestuarentsInHotel;
        this.noOfRoomsInHotel = noOfRoomsInHotel;
        this.noOfFloorsInHotel = noOfFloorsInHotel;
        this.currencyAccepted = currencyAccepted;
        this.vccCurrencyAccepted = vccCurrencyAccepted;
        this.standardCheckInTime = standardCheckInTime;
        this.standardCheckOutTime = standardCheckOutTime;
        this.hotelTimeZone = hotelTimeZone;
        this.is24HourCheckIn = is24HourCheckIn;
       // this.hotelDocuments = hotelDocuments;
        this.hotelStreetAddress = hotelStreetAddress;
        this.userId = userId;
        this.localty = localty;
        this.state = state;
        this.country=country;
        this.city=city;
        this.pincode=pincode;
    }

    public HotelDetails(int hotelId, String hotelName, String hotelDisplayName, String hotelType, String starRating, int chainId, String hotelBuiltYear, String noofRestuarentsInHotel,
                        String noOfRoomsInHotel, String noOfFloorsInHotel, String currencyAccepted, String vccCurrencyAccepted,
                        String standardCheckInTime, String standardCheckOutTime, String hotelTimeZone, boolean is24HourCheckIn,
                        String hotelStreetAddress, int userId, String localty, String state, String country, String city, String pincode,ArrayList contact)
    {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        //this.isApproved = isApproved;
        /*this.aminetiesList = aminetiesList;
        this.accountsInfo = accountsInfo;
        this.rooms = rooms;*/
        this.hotelDisplayName = hotelDisplayName;
        this.hotelType = hotelType;
        this.starRating = starRating;
        this.chainId = chainId;
        //this.chainName = chainName;
        this.hotelBuiltYear = hotelBuiltYear;
        this.noofRestuarentsInHotel = noofRestuarentsInHotel;
        this.noOfRoomsInHotel = noOfRoomsInHotel;
        this.noOfFloorsInHotel = noOfFloorsInHotel;
        this.currencyAccepted = currencyAccepted;
        this.vccCurrencyAccepted = vccCurrencyAccepted;
        this.standardCheckInTime = standardCheckInTime;
        this.standardCheckOutTime = standardCheckOutTime;
        this.hotelTimeZone = hotelTimeZone;
        this.is24HourCheckIn = is24HourCheckIn;
        //this.hotelDocuments = hotelDocuments;
        this.hotelStreetAddress = hotelStreetAddress;
        this.userId = userId;
        this.localty = localty;
        this.state = state;
        this.country=country;
        this.city=city;
        this.pincode=pincode;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
    public boolean getApproved()
    {
        return isApproved;
    }


    public void setAminetiesList(ArrayList aminetiesList) {
        this.aminetiesList = aminetiesList;
    }

    public ArrayList getAminetiesList() {
        return aminetiesList;
    }

  /*  public void setAccountsInfo(ArrayList accountsInfo) {
        this.accountsInfo = accountsInfo;
    }

    public ArrayList getAccountsInfo() {
        return accountsInfo;
    }*/

    public void setRooms(ArrayList rooms) {
        this.rooms = rooms;
    }

    public ArrayList getRooms() {
        return rooms;
    }

/*
    public void setRooms(ArrayList<Rooms> rooms) {
        this.rooms = rooms;
    }
*/

    public ArrayList<ContactDetails> getContact() {
        return contact;
    }

    public void setContact(ArrayList<ContactDetails> contact) {
        this.contact = contact;
    }

    public boolean isIs24HourCheckIn() {
        return is24HourCheckIn;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setHotelDisplayName(String hotelDisplayName) {
        this.hotelDisplayName = hotelDisplayName;
    }

    public String getHotelDisplayName() {
        return hotelDisplayName;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getChainName() {
        return chainName;
    }

    public void setHotelBuiltYear(String hotelBuiltYear) {
        this.hotelBuiltYear = hotelBuiltYear;
    }

    public String getHotelBuiltYear() {
        return hotelBuiltYear;
    }

    public void setNoofRestuarentsInHotel(String noofRestuarentsInHotel) {
        this.noofRestuarentsInHotel = noofRestuarentsInHotel;
    }

    public String getNoofRestuarentsInHotel() {
        return noofRestuarentsInHotel;
    }

    public void setNoOfRoomsInHotel(String noOfRoomsInHotel) {
        this.noOfRoomsInHotel = noOfRoomsInHotel;
    }

    public String getNoOfRoomsInHotel() {
        return noOfRoomsInHotel;
    }

    public void setNoOfFloorsInHotel(String noOfFloorsInHotel) {
        this.noOfFloorsInHotel = noOfFloorsInHotel;
    }

    public String getNoOfFloorsInHotel() {
        return noOfFloorsInHotel;
    }

    public void setCurrencyAccepted(String currencyAccepted) {
        this.currencyAccepted = currencyAccepted;
    }

    public String getCurrencyAccepted() {
        return currencyAccepted;
    }

    public void setVccCurrencyAccepted(String vccCurrencyAccepted) {
        this.vccCurrencyAccepted = vccCurrencyAccepted;
    }

    public String getVccCurrencyAccepted() {
        return vccCurrencyAccepted;
    }

    public void setStandardCheckInTime(String standardCheckInTime) {
        this.standardCheckInTime = standardCheckInTime;
    }

    public String getStandardCheckInTime() {
        return standardCheckInTime;
    }

    public void setStandardCheckOutTime(String standardCheckOutTime) {
        this.standardCheckOutTime = standardCheckOutTime;
    }

    public String getStandardCheckOutTime() {
        return standardCheckOutTime;
    }

    public void setHotelTimeZone(String hotelTimeZone) {
        this.hotelTimeZone = hotelTimeZone;
    }

    public String getHotelTimeZone() {
        return hotelTimeZone;
    }

    public void setIs24HourCheckIn(boolean is24HourCheckIn) {
        this.is24HourCheckIn = is24HourCheckIn;
    }
    public boolean getIs24HoursCheckIn()
    {
        return is24HourCheckIn;
    }

    /*public void setHotelDocuments(ArrayList<HotelDocuments> hotelDocuments) {
        this.hotelDocuments = hotelDocuments;
    }

    public ArrayList<HotelDocuments> getHotelDocuments() {
        return hotelDocuments;
    }*/

    public void setHotelStreetAddress(String hotelStreetAddress) {
        this.hotelStreetAddress = hotelStreetAddress;
    }

    public String getHotelStreetAddress() {
        return hotelStreetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setLocalty(String localty) {
        this.localty = localty;
    }

    public String getLocalty() {
        return localty;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
