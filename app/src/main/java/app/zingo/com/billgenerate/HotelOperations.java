package app.zingo.com.billgenerate;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Activiies.HotelDetails;
import app.zingo.com.billgenerate.Activiies.HotelNotification;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 09-11-2017.
 */


public interface HotelOperations {

    @POST("Calculation/SendNotificationForMultipleDeviceByHotelId")
    Call<ArrayList<String>> sendNotification(@Header("Authorization") String authKey, @Body HotelNotification fireBaseModel);

    @PUT("Hotels/UpdateHotels/{id}")
    Call<HotelDetails> updateHotel(@Header("Authorization") String authKey, @Path("id") int id, @Body HotelDetails hotelDetails);

    @GET("Profiles/GetHotelsByProfileId/{ProfileId}")
    Call<ArrayList<HotelDetails>> getHotelByProfileId(@Header("Authorization") String authKey, @Path("ProfileId") int ProfileId);

    @GET("Hotels/GetAllHotelsWithoutImages")
    Call<ArrayList<HotelDetails>> getHotel(@Header("Authorization") String authKey);

    @POST("NotificationManagers")
    Call<NotificationManager> saveNotification(@Header("Authorization") String auth, @Body NotificationManager hotelNotification);


    @GET(API.HOTELS+"/{HotelId}")
    Call<HotelDetails> getHotelByHotelId(@Header("Authorization") String authKey,@Path("HotelId") int HotelId);



}
