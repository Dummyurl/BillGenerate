package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CSC on 11/29/2017.
 */

public class Payment implements Serializable {



    @SerializedName("PaymentId")
    private int PaymentId;

    @SerializedName("PaymentName")
    private String PaymentName;

    @SerializedName("Amount")
    private int Amount ;

    @SerializedName("PaymentType")
    private String PaymentType;

    @SerializedName("roomBookings")
    private Bookings1 roomBookings;

    @SerializedName("BookingId")
    private int BookingId;

    @SerializedName("PaymentStatus")
    private String PaymentStatus	;

    @SerializedName("Remarks")
    private String Remarks	;

    @SerializedName("PaymentDate")
    private String PaymentDate	;

    public Bookings1 getRoomBookings() {
        return roomBookings;
    }

    public void setRoomBookings(Bookings1 roomBookings) {
        this.roomBookings = roomBookings;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public Payment() {
    }

    public Payment(String payment,int amount,String paidType)
    {
        this.PaymentName = payment;
        this.Amount = amount;
        this.PaymentType = paidType;

    }

    public Payment(String payment,int amount,String paidType,int bookingId)
    {
        this.PaymentName = payment;
        this.Amount = amount;
        this.PaymentType = paidType;
        this.BookingId = bookingId;
    }

    public Payment(int paymentId,String payment,int amount,String paymentType,int bookingId)
    {
        this.PaymentId = paymentId;
        this.PaymentName = payment;
        this.Amount = amount;
        this.PaymentType = paymentType;
        this.BookingId = bookingId;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public int getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(int paymentId) {
        PaymentId = paymentId;
    }

    public String getPaymentName() {
        return PaymentName;
    }

    public void setPaymentName(String paymentName) {
        PaymentName = paymentName;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }
}
