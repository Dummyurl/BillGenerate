package app.zingo.com.billgenerate.WebApis;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.Bookings1;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by CSC on 2/20/2018.
 */

public interface AccountApi {

    @GET("RoomBooking/GetAllBooking/{HotelId}")
    Call<ArrayList<Bookings1>> getAllBookingByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int id);
}
