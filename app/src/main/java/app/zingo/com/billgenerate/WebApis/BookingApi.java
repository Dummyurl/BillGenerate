package app.zingo.com.billgenerate.WebApis;

import java.util.ArrayList;

/*import app.zingo.com.hotelmanagement.Model.BetweenDates;
import app.zingo.com.hotelmanagement.Model.BookingAndTraveller;
import app.zingo.com.hotelmanagement.Model.Bookings1;
import app.zingo.com.hotelmanagement.Model.HotelTagBookings;
import app.zingo.com.hotelmanagement.Model.SearchBook;
import app.zingo.com.hotelmanagement.Model.SearchBooking;
import app.zingo.com.hotelmanagement.Util.API;*/
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 19-12-2017.
 */

public interface BookingApi {

    @GET(API.ACTIVEBOOKING+"/GetActiveBooking/{HotelId}")
    Call<ArrayList<Bookings1>> getActiveBookingsByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int id);

    @GET(API.ACTIVEBOOKING+"/GetBookingByBookingNumber/{BookingNumber}")
    Call<ArrayList<Bookings1>> getBookingByNum(@Header("Authorization") String authKey, @Path("BookingNumber") String bookNumber);

    @GET("RoomBooking/GetActiveBookingByRoomId/{HotelId}/{RoomId}")
    Call<ArrayList<Bookings1>> getDetailsByHotelRoomId(@Header("Authorization") String authKey, @Path("HotelId") int id, @Path("RoomId") int idr);

    @GET("RoomBooking/GetDelayBookingByHotelIdAndRoomId/{HotelId}/{RoomId}")
    Call<ArrayList<Bookings1>> getDelayByHotelRoomId(@Header("Authorization") String authKey, @Path("HotelId") int id, @Path("RoomId") int idr);

    @GET("RoomBookings/GetRoomBookingByProfileId/{ProfileId}")
    Call<ArrayList<Bookings1>> getBookingsByProfileId(@Header("Authorization") String authKey, @Path("ProfileId") int ProfileId);

    /*@GET("ProfileHotelMapping/GetRoomBookingsByProfileID/{ProfileId}")
    Call<ArrayList<HotelTagBookings>> getBookingsByProfileMappingId(@Header("Authorization") String authKey, @Path("ProfileId") int ProfileId);*/


    @PUT("RoomBookings/{id}")
    Call<String> updateBookingStatus(@Header("Authorization") String authKey, @Path("id") int id, @Body Bookings1 bookings1);

    @GET("RoomBooking/GetBookingByBookingStatus/{HotelId}/{BookingStatus}")
    Call<ArrayList<Bookings1>> getBookingsByStatus(@Header("Authorization") String authKey, @Path("HotelId") int id, @Path("BookingStatus") String bookingStatus);

    @GET("RoomBooking/GetAllBooking/{HotelId}")
    Call<ArrayList<Bookings1>> getBookingsByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int id);

   /* @POST("RoomBookings/GetBookingBetweenTheDates")
    Call<ArrayList<Bookings1>> getBookingsBetweenDates(@Header("Authorization") String authKey, @Body BetweenDates jsonObject);

    @POST("RoomBookings/SearchBooking")
    Call<ArrayList<SearchBook>> getSearchBookings(@Header("Authorization") String authKey, @Body SearchBooking jsonObject);

    @GET("RoomBooking/GetBookingByBookingStatusCustomAPI/{HotelId}/{BookingStatus}")
    Call<ArrayList<BookingAndTraveller>> getBookingByStatus(@Header("Authorization") String authKey, @Path("HotelId") int id, @Path("BookingStatus") String status);

    @GET("RoomBooking/GetAllBookingCustomAPI/{HotelId}")
    Call<ArrayList<BookingAndTraveller>> getAllBooking(@Header("Authorization") String authKey, @Path("HotelId") int id);*/

    @GET("RoomBookings/{id}")
    Call<Bookings1> getBookingById(@Header("Authorization") String authKey, @Path("id") int id);
}
