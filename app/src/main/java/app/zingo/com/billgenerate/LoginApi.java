package app.zingo.com.billgenerate;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.ContactDetails;
import app.zingo.com.billgenerate.Model.Documents;
import app.zingo.com.billgenerate.Model.FireBaseModel;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Model.Profile1;
import app.zingo.com.billgenerate.Model.SearchBook;
import app.zingo.com.billgenerate.Model.SearchBooking;
import app.zingo.com.billgenerate.Model.Traveller;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 04-04-2018.
 */

public interface LoginApi {


    @POST("Profiles/GetProfileByUserNameAndPassword")
    Call<ArrayList<Profile1>> loginApiByUsernamePassword(@Header("Authorization") String authKey,@Body Profile1 body /*@Field("userid") String userid, @Field("password") String password, @Field("imei_no") String imei_no*/);

    @GET("Documents/GetDocumentsByProfileId/{id}")
    Call<ArrayList<Documents>> getHotels(@Header("Authorization") String authKey, @Path("id") int ProfileId);

    @POST("Documents/AddDocument")
    Call<Documents> addHotels(@Header("Authorization") String authKey,@Body Documents body);

    @GET("Documents/{id}")
    Call<Documents> getDocumentsById(@Header("Authorization") String authKey,@Path("id") int id);

    @GET("Hotels/GetContactsByHotelId/{HotelId}")
    Call<ArrayList<ContactDetails>> getContactByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @GET("Hotels")
    Call<ArrayList<HotelDetails>> getHotelsList(@Header("Authorization") String authKey);

    @GET("Hotels/{id}")
    Call<HotelDetails> getHotelsById(@Header("Authorization") String authKey,@Path("id") int id);

    @POST("RoomBookings")
    Call<Bookings1> postBooking(@Header("Authorization") String authKey, @Body Bookings1 body);

    @POST("Calculation/SendNotificationToDevice")
    Call<String> addNotification(@Header("Authorization") String authKey, @Body FireBaseModel body);


    @POST("Calculation/SendNotificationForMultipleDeviceByHotelId")
    Call<ArrayList<String>> send(@Header("Authorization") String authKey, @Body FireBaseModel body);

    @GET("Travellers/GetTravellerByPhoneNumber/{PhoneNumber}")
    Call<ArrayList<Traveller>> fetchTravelerByPhone(@Header("Authorization") String authKey, @Path("PhoneNumber") String PhoneNumber);

    @POST("Travellers")
    Call<Traveller> addTraveler(@Header("Authorization") String authKey,@Body Traveller body);

    @PUT("Travellers/{id}")
    Call<Traveller> updateTravellerDetails(@Header("Authorization") String authKey,@Path("id") int id,@Body Traveller body);
    @POST("NotificationManagers")
    Call<NotificationManager> saveNotification(@Header("Authorization") String auth, @Body NotificationManager hotelNotification);

    @GET("RoomBookings/{id}")
    Call<Bookings1> getBookingById(@Header("Authorization") String authKey,@Path("id") int id);

    @GET("RoomBookings/GetRoomBookingsByOTABookingId/{OTABookingId}")
    Call<ArrayList<Bookings1>> getBookingByOTAId(@Header("Authorization") String authKey,@Path("OTABookingId") String id);


    @GET("Travellers/{id}")
    Call<Traveller> getTravellerDetails(@Header("Authorization") String authKey, @Path("id") int id);

    @PUT("RoomBookings/{id}")
    Call<String> updateBookingStatus(@Header("Authorization") String authKey,@Path("id") int id, @Body Bookings1 bookings1);

    @POST("RoomBookings/SearchBooking")
    Call<ArrayList<SearchBook>> getSearchBookings(@Header("Authorization") String authKey, @Body SearchBooking jsonObject);



}
