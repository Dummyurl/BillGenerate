package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by new on 2/28/2018.
 */

public class AuditSettlement implements Serializable {

    @SerializedName("AuditSettlementId")
    private int AuditSettlementId;

    @SerializedName("roomBookings")
    private Bookings1 roomBookings;

    @SerializedName("BookingId")
    private int BookingId;

    @SerializedName("SettlementDate")
    private String SettlementDate;

    @SerializedName("PaymentMode")
    private String PaymentMode;

    @SerializedName("PaymentStatus")
    private String PaymentStatus;

    @SerializedName("AuditingSellRate")
    private int AuditingSellRate;

    @SerializedName("AuditingGST")
    private int AuditingGST;

    @SerializedName("AuditingExtra")
    private int AuditingExtra;

    @SerializedName("DifferenceAmount")
    private int DifferenceAmount;

    @SerializedName("Remark")
    private String Remark;

    @SerializedName("CreatedBy")
    private String CreatedBy;


    public int getAuditSettlementId() {
        return AuditSettlementId;
    }

    public void setAuditSettlementId(int auditSettlementId) {
        AuditSettlementId = auditSettlementId;
    }

    public Bookings1 getRoomBookings() {
        return roomBookings;
    }

    public void setRoomBookings(Bookings1 roomBookings) {
        this.roomBookings = roomBookings;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public String getSettlementDate() {
        return SettlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        SettlementDate = settlementDate;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public int getAuditingSellRate() {
        return AuditingSellRate;
    }

    public void setAuditingSellRate(int auditingSellRate) {
        AuditingSellRate = auditingSellRate;
    }

    public int getAuditingGST() {
        return AuditingGST;
    }

    public void setAuditingGST(int auditingGST) {
        AuditingGST = auditingGST;
    }

    public int getAuditingExtra() {
        return AuditingExtra;
    }

    public void setAuditingExtra(int auditingExtra) {
        AuditingExtra = auditingExtra;
    }

    public int getDifferenceAmount() {
        return DifferenceAmount;
    }

    public void setDifferenceAmount(int differenceAmount) {
        DifferenceAmount = differenceAmount;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }
}
