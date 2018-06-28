package app.zingo.com.billgenerate;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels.com on 4/11/2018.
 */


public class HotelNotification implements Serializable {

    @SerializedName("HotelId")
    private int HotelId;

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int HotelId) {
        this.HotelId = HotelId;
    }

    @SerializedName("Title")
    private String Title;

    @SerializedName("Message")
    private String Message;

    @SerializedName("ServerId")
    private String ServerId;

    @SerializedName("SenderId")
    private String SenderId;



    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getServerId() {
        return ServerId;
    }

    public void setServerId(String serverId) {
        ServerId = serverId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }
}
