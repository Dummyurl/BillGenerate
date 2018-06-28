package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by CSC on 11/9/2017.
 */


public class RoomOccupancy implements Serializable {

    @SerializedName("RoomOccupancyId")
    private int roomOccupancyId;
    @SerializedName("NoOfAdults")
    private int noOfAdults;
    @SerializedName("NoOfChild")
    private int noOfChild;
    @SerializedName("MaximumNoOfAdultAllowed")
    private int maximumNoOfAdultAllowed;
    @SerializedName("MaximumNoOfChildAllowed")
    private int maximumNoOfChildAllowed;
    @SerializedName("MaximumNoOfInfantAllowed")
    private int maximumNoOfInfantAllowed;
    @SerializedName("MaximumNoOfGuestAllowed")
    private int maximumNoOfGuestAllowed;

    public RoomOccupancy()
    {}
    public RoomOccupancy(int roomOccupancyId, int noOfAdults, int noOfChild, int maximumNoOfAdultAllowed, int maximumNoOfChildAllowed,
                         int maximumNoOfInfantAllowed, int maximumNoOfGuestAllowed)
    {
        this.roomOccupancyId = roomOccupancyId;
        this.noOfAdults = noOfAdults;
        this.noOfChild = noOfChild;
        this.maximumNoOfAdultAllowed = maximumNoOfAdultAllowed;
        this.maximumNoOfChildAllowed = maximumNoOfChildAllowed;
        this.maximumNoOfInfantAllowed = maximumNoOfInfantAllowed;
        this.maximumNoOfGuestAllowed = maximumNoOfGuestAllowed;
    }

    public RoomOccupancy(int noOfAdults, int noOfChild, int maximumNoOfAdultAllowed, int maximumNoOfChildAllowed,
                         int maximumNoOfInfantAllowed, int maximumNoOfGuestAllowed)
    {
        //this.roomOccupancyId = roomOccupancyId;
        this.noOfAdults = noOfAdults;
        this.noOfChild = noOfChild;
        this.maximumNoOfAdultAllowed = maximumNoOfAdultAllowed;
        this.maximumNoOfChildAllowed = maximumNoOfChildAllowed;
        this.maximumNoOfInfantAllowed = maximumNoOfInfantAllowed;
        this.maximumNoOfGuestAllowed = maximumNoOfGuestAllowed;
    }


    public void setRoomOccupancyId(int roomOccupancyId) {
        this.roomOccupancyId = roomOccupancyId;
    }

    public int getRoomOccupancyId() {
        return roomOccupancyId;
    }

    public void setNoOfChild(int noOfChild) {
        this.noOfChild = noOfChild;
    }

    public int getNoOfChild() {
        return noOfChild;
    }

    public void setNoOfAdults(int noOfAdults) {
        this.noOfAdults = noOfAdults;
    }

    public int getNoOfAdults() {
        return noOfAdults;
    }

    public void setMaximumNoOfAdultAllowed(int maximumNoOfAdultAllowed) {
        this.maximumNoOfAdultAllowed = maximumNoOfAdultAllowed;
    }

    public int getMaximumNoOfAdultAllowed() {
        return maximumNoOfAdultAllowed;
    }

    public void setMaximumNoOfChildAllowed(int maximumNoOfChildAllowed) {
        this.maximumNoOfChildAllowed = maximumNoOfChildAllowed;
    }

    public int getMaximumNoOfChildAllowed() {
        return maximumNoOfChildAllowed;
    }

    public void setMaximumNoOfGuestAllowed(int maximumNoOfGuestAllowed) {
        this.maximumNoOfGuestAllowed = maximumNoOfGuestAllowed;
    }

    public int getMaximumNoOfGuestAllowed() {
        return maximumNoOfGuestAllowed;
    }

    public void setMaximumNoOfInfantAllowed(int maximumNoOfInfantAllowed) {
        this.maximumNoOfInfantAllowed = maximumNoOfInfantAllowed;
    }

    public int getMaximumNoOfInfantAllowed() {
        return maximumNoOfInfantAllowed;
    }
}
