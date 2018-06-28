package app.zingo.com.billgenerate;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 10-05-2018.
 */


public class NotificationManager implements Serializable {

    @SerializedName("NotificationManagerId")
    private int NotificationManagerId;

    @SerializedName("NotificationText")
    private String NotificationText;

    @SerializedName("NotificationFor")
    private String NotificationFor;

    @SerializedName("Image")
    private String Image;

    @SerializedName("Images")
    private ArrayList<Byte> Images;

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("TravellerAgentId")
    private int TravellerAgentId;

    @SerializedName("TravellerId")
    private int TravellerId;

    public int getNotificationManagerId() {
        return NotificationManagerId;
    }

    public void setNotificationManagerId(int notificationManagerId) {
        NotificationManagerId = notificationManagerId;
    }

    public String getNotificationText() {
        return NotificationText;
    }

    public void setNotificationText(String notificationText) {
        NotificationText = notificationText;
    }

    public String getNotificationFor() {
        return NotificationFor;
    }

    public void setNotificationFor(String notificationFor) {
        NotificationFor = notificationFor;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public ArrayList<Byte> getImages() {
        return Images;
    }

    public void setImages(ArrayList<Byte> images) {
        Images = images;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public int getTravellerAgentId() {
        return TravellerAgentId;
    }

    public void setTravellerAgentId(int travellerAgentId) {
        TravellerAgentId = travellerAgentId;
    }

    public int getTravellerId() {
        return TravellerId;
    }

    public void setTravellerId(int travellerId) {
        TravellerId = travellerId;
    }
}
