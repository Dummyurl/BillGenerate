package app.zingo.com.billgenerate.WebApis;

import java.util.ArrayList;

/*import app.zingo.com.hotelmanagement.Model.Bookings1;
import app.zingo.com.hotelmanagement.Model.Traveller;
import app.zingo.com.hotelmanagement.Model.TravellerDocuments;
import app.zingo.com.hotelmanagement.Util.API;*/
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 06-01-2018.
 */

public interface TravellerApi {

    @GET("Travellers/{id}")
    Call<Traveller> getTravellerDetails(@Header("Authorization") String authKey, @Path("id") int id);


    @GET("Travellers/GetTravellerByPhoneNumber/{PhoneNumber}")
    Call<ArrayList<Traveller>> getTravellerByPhone(@Header("Authorization") String authKey, @Path("PhoneNumber") String PhoneNumber);

    @PUT("Travellers/{id}")
    Call<Traveller> updateTravellerDetails(@Header("Authorization") String authKey, @Path("id") int id, @Body Traveller body);

    /*@GET(API.HOTEL_DOCUMENTS)
    Call<ArrayList<TravellerDocuments>> getTravelerDocuments(@Header("Authorization") String authKey);

    @PUT(API.GET_TRAVELLER_DOCU_BY_ID+"/{id}")
    Call<TravellerDocuments> updateTravellerDoc(@Header("Authorization") String authKey, @Path("id") int id, @Body TravellerDocuments body);


    @POST(API.ADD_TRAVELER_DOCUMENTS)
    Call<TravellerDocuments> addTravelerDocuments(@Header("Authorization") String authKey, @Body TravellerDocuments body);*/


    @GET("RoomBookings/GetAllRoomBookingsByTravellerId/{TravellerId}")
    Call<ArrayList<Bookings1>> getBookingsByTravellerId(@Header("Authorization") String authKey, @Path("TravellerId") int TravellerId);
}
