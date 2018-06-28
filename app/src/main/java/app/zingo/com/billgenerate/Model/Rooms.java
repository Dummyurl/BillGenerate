package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CSC on 11/9/2017.
 */


public class Rooms implements Serializable{

    @SerializedName("RoomId")
    private int RoomId;
    @SerializedName("LongDescription")
    private String LongDescription;
    @SerializedName("RoomCategoryId")
    private int RoomCategoryId;
    @SerializedName("RoomType")
    private RoomCategories RoomType;

    @SerializedName("DisplayName")
    private String DisplayName;
    @SerializedName("RoomSizeInLength")
    private String RoomSizeInLength;
    @SerializedName("RoomSizeInBreadth")
    private String RoomSizeInBreadth;
    @SerializedName("TotalRooms")
    private String TotalRooms;

    @SerializedName("BedTypeId")
    private int BedTypeId;

    @SerializedName("BedTypes")
    private BedType BedTypes;
    @SerializedName("ExtraBedType")
    private BedType ExtraBedType;
    @SerializedName("RoomViewId")
    private int RoomViewId;
    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("_RoomView")
    private RoomView _RoomView;
    @SerializedName("RoomOccupancyId")
    private int RoomOccupancyId;
    @SerializedName("_RoomOccupancy")
    private RoomOccupancy _RoomOccupancy;

    @SerializedName("Floor")
    private String Floor;

    @SerializedName("RoomNo")
    private String RoomNo;

    @SerializedName("Status")
    private String Status;

    @SerializedName("DeclaredRate")
    private int DisplayRate;


    @SerializedName("SellRate")
    private int SellRate;

    @SerializedName("ExtraRate")
    private int ExtraRate;

    @SerializedName("DiscountPer")
    private int DiscountPer;

    @SerializedName("GstPer")
    private int GstPer;

    @SerializedName("GstAmnt")
    private int GstAmnt;

    @SerializedName("BlockingReason")
    private String blockingReason;
    //HourlyCharges
    @SerializedName("HourlyCharges")
    private int HourlyCharges;

    public int getHourlyCharges() {
        return HourlyCharges;
    }

    public void setHourlyCharges(int hourlyCharges) {
        HourlyCharges = hourlyCharges;
    }

    public int getSellRate() {
        return SellRate;
    }

    public void setSellRate(int sellRate) {
        SellRate = sellRate;
    }

    public int getExtraRate() {
        return ExtraRate;
    }

    public void setExtraRate(int extraRate) {
        ExtraRate = extraRate;
    }

    public int getDiscountPer() {
        return DiscountPer;
    }

    public void setDiscountPer(int discountPer) {
        DiscountPer = discountPer;
    }

    public int getGstPer() {
        return GstPer;
    }

    public void setGstPer(int gstPer) {
        GstPer = gstPer;
    }

    public int getGstAmnt() {
        return GstAmnt;
    }

    public void setGstAmnt(int gstAmnt) {
        GstAmnt = gstAmnt;
    }

    public String getRoomCat() {
        return RoomCat;
    }

    public void setRoomCat(String roomCat) {
        RoomCat = roomCat;
    }

    public int getTotatRent() {
        return TotatRent;
    }

    public void setTotatRent(int totatRent) {
        TotatRent = totatRent;
    }

    public String getPreRent() {
        return PreRent;
    }

    public void setPreRent(String preRent) {
        PreRent = preRent;
    }

    public String getBalRent() {
        return BalRent;
    }

    public void setBalRent(String balRent) {
        BalRent = balRent;
    }

    @SerializedName("RoomCat")
    private String RoomCat;

    @SerializedName("TotatRent")
    private int TotatRent;

    @SerializedName("PreRent")
    private String PreRent;

    @SerializedName("BalRent")
    private String BalRent;

    public int getDisplayRate() {
        return DisplayRate;
    }

    public void setDisplayRate(int displayRate) {
        DisplayRate = displayRate;
    }

    public Rooms()
    {}

    /*public Rooms(String longDescription,)
    {}*/


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public int getRoomCategoryId() {
        return RoomCategoryId;
    }

    public void setRoomCategoryId(int roomCategoryId) {
        RoomCategoryId = roomCategoryId;
    }

    public RoomCategories getRoomType() {
        return RoomType;
    }

    public void setRoomType(RoomCategories roomType) {
        RoomType = roomType;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getRoomSizeInLength() {
        return RoomSizeInLength;
    }

    public void setRoomSizeInLength(String roomSizeInLength) {
        RoomSizeInLength = roomSizeInLength;
    }

    public String getRoomSizeInBreadth() {
        return RoomSizeInBreadth;
    }

    public void setRoomSizeInBreadth(String roomSizeInBreadth) {
        RoomSizeInBreadth = roomSizeInBreadth;
    }

    public String getTotalRooms() {
        return TotalRooms;
    }

    public void setTotalRooms(String totalRooms) {
        TotalRooms = totalRooms;
    }

    public int getBedTypeId() {
        return BedTypeId;
    }

    public void setBedTypeId(int bedTypeId) {
        BedTypeId = bedTypeId;
    }

    public BedType getBedTypes() {
        return BedTypes;
    }

    public void setBedTypes(BedType bedTypes) {
        BedTypes = bedTypes;
    }

    public BedType getExtraBedType() {
        return ExtraBedType;
    }

    public void setExtraBedType(BedType extraBedType) {
        ExtraBedType = extraBedType;
    }

    public int getRoomViewId() {
        return RoomViewId;
    }

    public void setRoomViewId(int roomViewId) {
        RoomViewId = roomViewId;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public RoomView get_RoomView() {
        return _RoomView;
    }

    public void set_RoomView(RoomView _RoomView) {
        this._RoomView = _RoomView;
    }

    public int getRoomOccupancyId() {
        return RoomOccupancyId;
    }

    public void setRoomOccupancyId(int roomOccupancyId) {
        RoomOccupancyId = roomOccupancyId;
    }

    public RoomOccupancy get_RoomOccupancy() {
        return _RoomOccupancy;
    }

    public void set_RoomOccupancy(RoomOccupancy _RoomOccupancy) {
        this._RoomOccupancy = _RoomOccupancy;
    }



    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public void setBlockingReason(String blockingReason) {
        this.blockingReason = blockingReason;
    }

    public String getBlockingReason() {
        return blockingReason;
    }
}
