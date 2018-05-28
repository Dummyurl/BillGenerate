package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CSC on 11/29/2017.
 */

public class Bookings1 implements Serializable {

    @SerializedName("BookingId")
    private int BookingId;

    @SerializedName("BookingNumber")
    private String BookingNumber;

    @SerializedName("hotels")
    private HotelDetails hotels;

    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("BookingPlan")
    private String BookingPlan;

    @SerializedName("TravellerId")
    private int TravellerId;

/*

    @SerializedName("travellerList")
    private ArrayList<Traveller> travellerList;*/

    @SerializedName("BookingDate")
    private String BookingDate;

    @SerializedName("BookingTime")
    private String BookingTime;

    @SerializedName("OptCheckInDate")
    private String OptCheckInDate;

    @SerializedName("CheckInDate")
    private String CheckInDate;

    @SerializedName("NoOfAdults")
    private int NoOfAdults;

    @SerializedName("NoOfChild")
    private int NoOfChild;

    @SerializedName("CommissionPercentage")
    private int commissionPercentage;

    @SerializedName("CommissionAmount")
    private int commissionAmount;

    @SerializedName("BookingSourceType")
    private String bookingSourceType;

    @SerializedName("GST")
    private int gst;

    @SerializedName("Percentage")
    private int percentage;

    @SerializedName("GSTAmount")
    private int gstAmount;

    @SerializedName("BookingSource")
    private String BookingSource;

    @SerializedName("BookingStatus")
    private String BookingStatus;

    @SerializedName("CheckInTime")
    private String CheckInTime;


    @SerializedName("DeclaredRate")
    private int DeclaredRate;

    @SerializedName("SellRate")
    private int SellRate;

    @SerializedName("ExtraCharges")
    private int ExtraCharges;

    @SerializedName("Discount")
    private int Discount;

    @SerializedName("DiscountAmount")
    private int DiscountAmount;

    @SerializedName("TotalAmount")
    private int TotalAmount;

    @SerializedName("BalanceAmount")
    private int BalanceAmount;

    @SerializedName("RoomCategory")
    private String RoomCategory;

    @SerializedName("OptCheckOutDate")
    private String OptCheckOutDate ;

    @SerializedName("CheckOutDate")
    private String CheckOutDate ;

    @SerializedName("CheckOutTime")
    private String CheckOutTime;

    @SerializedName("RoomId")
    private int RoomId ;

    @SerializedName("Company")
    private String Company ;

    /*@SerializedName("servicesList")
    private ArrayList<Service> servicesList;*/

    @SerializedName("HourlyCharges")
    private int HourlyCharges ;

    @SerializedName("CommisionGSTAmount")
    private double CommisionGSTAmount ;

   /* @SerializedName("paymentList")
    private ArrayList<Payment> paymentList;*/

    @SerializedName("Reason")
    private String Reason ;

    @SerializedName("RefundAmount")
    private String RefundAmount ;

    @SerializedName("RefundsMode")
    private String RefundsMode ;

    @SerializedName("NoOfRooms")
    private int NoOfRooms ;

  /*  @SerializedName("rooms")
    private ArrayList<Rooms> rooms;

    @SerializedName("auditSettlementList")
    private ArrayList<AuditSettlement> auditSettlementList;*/

    @SerializedName("ProfileId")
    private int ProfileId ;

    @SerializedName("DurationOfStay")
    private int DurationOfStay ;

    @SerializedName("NoOfHoursStayed")
    private int NoOfHoursStayed ;



   /* @SerializedName("RoomId")
    private ArrayList<Integer> RoomId ;*/

    @SerializedName("room")
    private String room;

    @SerializedName("netAmount")
    private String netAmount;

    @SerializedName("OTACommissionAmount")
    private double OTACommissionAmount;

    @SerializedName("OTACommissionGSTAmount")
    private double OTACommissionGSTAmount;

    @SerializedName("OTATotalCommissionAmount")
    private double OTATotalCommissionAmount;

    @SerializedName("OTAServiceFees")
    private double OTAServiceFees;

    @SerializedName("OTAToPayHotel")
    private double OTAToPayHotel;

    @SerializedName("HotelToPayOTA")
    private double HotelToPayOTA;

    @SerializedName("ZingoToHotel")
    private double ZingoToHotel;

    @SerializedName("HotelToZingo")
    private double HotelToZingo;

    @SerializedName("ZingoCommision")
    private double ZingoCommision;

    @SerializedName("CustomerPaymentAtOTA")
    private double CustomerPaymentAtOTA;

    @SerializedName("AdditionalCharges")
    private double AdditionalCharges;

    @SerializedName("OTABookingID")
    private String OTABookingID;

    public double getOTACommissionAmount() {
        return OTACommissionAmount;
    }

    public void setOTACommissionAmount(double OTACommissionAmount) {
        this.OTACommissionAmount = OTACommissionAmount;
    }

    public double getOTACommissionGSTAmount() {
        return OTACommissionGSTAmount;
    }

    public void setOTACommissionGSTAmount(double OTACommissionGSTAmount) {
        this.OTACommissionGSTAmount = OTACommissionGSTAmount;
    }

    public double getOTATotalCommissionAmount() {
        return OTATotalCommissionAmount;
    }

    public void setOTATotalCommissionAmount(double OTATotalCommissionAmount) {
        this.OTATotalCommissionAmount = OTATotalCommissionAmount;
    }

    public double getOTAServiceFees() {
        return OTAServiceFees;
    }

    public void setOTAServiceFees(double OTAServiceFees) {
        this.OTAServiceFees = OTAServiceFees;
    }

    public double getOTAToPayHotel() {
        return OTAToPayHotel;
    }

    public void setOTAToPayHotel(double OTAToPayHotel) {
        this.OTAToPayHotel = OTAToPayHotel;
    }

    public double getHotelToPayOTA() {
        return HotelToPayOTA;
    }

    public void setHotelToPayOTA(double hotelToPayOTA) {
        HotelToPayOTA = hotelToPayOTA;
    }

    public double getZingoToHotel() {
        return ZingoToHotel;
    }

    public void setZingoToHotel(double zingoToHotel) {
        ZingoToHotel = zingoToHotel;
    }

    public double getHotelToZingo() {
        return HotelToZingo;
    }

    public void setHotelToZingo(double hotelToZingo) {
        HotelToZingo = hotelToZingo;
    }

    public double getZingoCommision() {
        return ZingoCommision;
    }

    public void setZingoCommision(double zingoCommision) {
        ZingoCommision = zingoCommision;
    }

    public double getCustomerPaymentAtOTA() {
        return CustomerPaymentAtOTA;
    }

    public void setCustomerPaymentAtOTA(double customerPaymentAtOTA) {
        CustomerPaymentAtOTA = customerPaymentAtOTA;
    }

    public double getAdditionalCharges() {
        return AdditionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        AdditionalCharges = additionalCharges;
    }

    public String getOTABookingID() {
        return OTABookingID;
    }

    public void setOTABookingID(String OTABookingID) {
        this.OTABookingID = OTABookingID;
    }

    /*  @SerializedName("travellerDocument")
    private ArrayList<TravellerDocuments> travellerDocument;*/

    public int getHourlyCharges() {
        return HourlyCharges;
    }

    public void setHourlyCharges(int hourlyCharges) {
        HourlyCharges = hourlyCharges;
    }

    public double getCommisionGSTAmount() {
        return CommisionGSTAmount;
    }

    public void setCommisionGSTAmount(double commisionGSTAmount) {
        CommisionGSTAmount = commisionGSTAmount;
    }

   /* public ArrayList<AuditSettlement> getAuditSettlementList() {
        return auditSettlementList;
    }

    public void setAuditSettlementList(ArrayList<AuditSettlement> auditSettlementList) {
        this.auditSettlementList = auditSettlementList;
    }*/

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public int getDurationOfStay() {
        return DurationOfStay;
    }

    public void setDurationOfStay(int durationOfStay) {
        DurationOfStay = durationOfStay;
    }

    public int getNoOfHoursStayed() {
        return NoOfHoursStayed;
    }

    public void setNoOfHoursStayed(int noOfHoursStayed) {
        NoOfHoursStayed = noOfHoursStayed;
    }

    public int getNoOfRooms() {
        return NoOfRooms;
    }

    public void setNoOfRooms(int noOfRooms) {
        NoOfRooms = noOfRooms;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getBookingPlan() {
        return BookingPlan;
    }

    public void setBookingPlan(String bookingPlan) {
        BookingPlan = bookingPlan;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getRefundAmount() {
        return RefundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        RefundAmount = refundAmount;
    }

    public String getRefundsMode() {
        return RefundsMode;
    }

    public void setRefundsMode(String refundsMode) {
        RefundsMode = refundsMode;
    }

    public Bookings1() {
    }

    public String getBookingNumber() {
        return BookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        BookingNumber = bookingNumber;
    }

    public int getExtraCharges() {
        return ExtraCharges;
    }

    public void setExtraCharges(int extraCharges) {
        ExtraCharges = extraCharges;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public int getTravellerId() {
        return TravellerId;
    }

    public void setTravellerId(int travellerId) {
        TravellerId = travellerId;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public HotelDetails getHotels() {
        return hotels;
    }

    public void setHotels(HotelDetails hotels) {
        this.hotels = hotels;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

  /*  public ArrayList<Traveller> getTravellerList() {
        return travellerList;
    }

    public void setTravellerList(ArrayList<Traveller> travellerList) {
        this.travellerList = travellerList;
    }*/

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingTime) {
        BookingTime = bookingTime;
    }

    public String getOptCheckInDate() {
        return OptCheckInDate;
    }

    public void setOptCheckInDate(String optCheckInDate) {
        OptCheckInDate = optCheckInDate;
    }

    public String getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    public int getNoOfAdults() {
        return NoOfAdults;
    }

    public void setNoOfAdults(int noOfAdults) {
        NoOfAdults = noOfAdults;
    }

    public String getBookingSource() {
        return BookingSource;
    }

    public void setBookingSource(String bookingSource) {
        BookingSource = bookingSource;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        BookingStatus = bookingStatus;
    }

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
    }

    public int getDeclaredRate() {
        return DeclaredRate;
    }

    public void setDeclaredRate(int declaredRate) {
        DeclaredRate = declaredRate;
    }

    
    public int getSellRate() {
        return SellRate;
    }

    public void setSellRate(int sellRate) {
        SellRate = sellRate;
    }

    public int getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        DiscountAmount = discountAmount;
    }

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(int balanceAmount) {
        BalanceAmount = balanceAmount;
    }

    public String getRoomCategory() {
        return RoomCategory;
    }

    public void setRoomCategory(String roomCategory) {
        RoomCategory = roomCategory;
    }

    public String getOptCheckOutDate() {
        return OptCheckOutDate;
    }

    public void setOptCheckOutDate(String optCheckOutDate) {
        OptCheckOutDate = optCheckOutDate;
    }

    public String getCheckOutDate() {
        return CheckOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        CheckOutDate = checkOutDate;
    }

    public String getCheckOutTime() {
        return CheckOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        CheckOutTime = checkOutTime;
    }

    /*public ArrayList<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Rooms> rooms) {
        this.rooms = rooms;
    }*/

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

   /* public ArrayList<Service> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ArrayList<Service> servicesList) {
        this.servicesList = servicesList;
    }*/

  /*  public ArrayList<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(ArrayList<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public ArrayList<TravellerDocuments> getTravellerDocument() {
        return travellerDocument;
    }

    public void setTravellerDocument(ArrayList<TravellerDocuments> travellerDocument) {
        this.travellerDocument = travellerDocument;
    }*/

    public void setNoOfChild(int noOfChild) {
        NoOfChild = noOfChild;
    }

    public int getNoOfChild() {
        return NoOfChild;
    }

    public void setGstAmount(int gstAmount) {
        this.gstAmount = gstAmount;
    }

    public String getBookingSourceType() {
        return bookingSourceType;
    }

    public void setGst(int gst) {
        this.gst = gst;
    }

    public int getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionPercentage(int commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public int getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionAmount(int commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public int getGst() {
        return gst;
    }

    public void setBookingSourceType(String bookingSourceType) {
        this.bookingSourceType = bookingSourceType;
    }

    public int getGstAmount() {
        return gstAmount;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }


}
