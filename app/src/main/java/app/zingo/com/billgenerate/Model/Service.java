package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels.com on 30-11-2017.
 */

public class Service implements Serializable{

    @SerializedName("ServicesId")
    private int serviceId;
    @SerializedName("Description")
    private String service;
    @SerializedName("Amount")
    private int amount;
    @SerializedName("bookings")
    private Bookings1 bookings;
    @SerializedName("BookingNumber")
    private String bookingNumber;
    @SerializedName("BookingId")
    private int bookingId;
    @SerializedName("PaidStatus")
    private String paidStatus;
    @SerializedName("PaymentMode")
    private String paymentMode;

    //PaymentDate
    @SerializedName("PaymentDate")
    private String PaymentDate;

    @SerializedName("StatusReason")
    private String StatusReason;

    @SerializedName("Quantity")
    private int Quantity;


    @SerializedName("ServiceStatus")
    private String ServiceStatus;

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getServiceStatus() {
        return ServiceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        ServiceStatus = serviceStatus;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public Service()
    {
    }

    public Service(String service,int amount,String paidStatus)
    {
        this.service = service;
        this.amount = amount;
        this.paidStatus = paidStatus;

    }

    public Service(String service,int amount,String paidStatus,int bookingId)
    {
        this.service = service;
        this.amount = amount;
        this.paidStatus = paidStatus;
        this.bookingId = bookingId;
    }

    public Service(int serviceId,String service,int amount,String paidStatus,int bookingId)
    {
        this.serviceId = serviceId;
        this.service = service;
        this.amount = amount;
        this.paidStatus = paidStatus;
        this.bookingId = bookingId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookings(Bookings1 bookings) {
        this.bookings = bookings;
    }

    public Bookings1 getBookings() {
        return bookings;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getStatusReason() {
        return StatusReason;
    }

    public void setStatusReason(String statusReason) {
        StatusReason = statusReason;
    }
}
