package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CSC on 11/9/2017.
 */

public class RoomView implements Serializable{
    @SerializedName("RoomViewId")
    private int roomViewId;
    @SerializedName("RoomViewName")
    private String roomViewName;
    @SerializedName("Description")
    private String description;

    public RoomView(int roomViewId, String roomViewName, String description)
    {
        this.roomViewId = roomViewId;
        this.roomViewName = roomViewName;
        this.description = description;
    }

    public RoomView(String roomViewName, String description)
    {
        //this.roomViewId = roomViewId;
        this.roomViewName = roomViewName;
        this.description = description;
    }

    public RoomView(){}
    public void setRoomViewId(int roomViewId) {
        this.roomViewId = roomViewId;
    }

    public int getRoomViewId() {
        return roomViewId;
    }

    public void setRoomViewName(String roomViewName) {
        this.roomViewName = roomViewName;
    }

    public String getRoomViewName() {
        return roomViewName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
