package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels.com on 2/19/2018.
 */

public class BookingAndTraveller implements Serializable{

    @SerializedName("roomBooking")
    private Bookings1 roomBooking;

    @SerializedName("travellers")
    private Traveller travellers;


    public BookingAndTraveller()
    {}

    public BookingAndTraveller(Bookings1 roomBooking, Traveller travellers)
    {
        this.roomBooking = roomBooking;
        this.travellers = travellers;
    }

    public Bookings1 getRoomBooking() {
        return roomBooking;
    }

    public Traveller getTravellers() {
        return travellers;
    }

    public void setRoomBooking(Bookings1 roomBooking) {
        this.roomBooking = roomBooking;
    }

    public void setTravellers(Traveller travellers) {
        this.travellers = travellers;
    }
}
