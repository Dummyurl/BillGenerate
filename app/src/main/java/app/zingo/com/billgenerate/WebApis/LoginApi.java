package app.zingo.com.billgenerate.WebApis;

import java.util.ArrayList;

/*import app.zingo.com.agentapp.Model.ReferCodeModel;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.Utils.API;*/
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Model.TravellerAgentProfiles;
import app.zingo.com.billgenerate.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 18-04-2018.
 */

public interface LoginApi {

      /*@POST(API.PROFILE)
    Call<Profile1> loginApi(@Header("Authorization") String authKey,@Body Profile1 body);

    @POST(API.GET_PROFILE_BY_UNIQUE_ID)
    Call<ArrayList<Profile1>> loginApiBuUniqueId(@Header("Authorization") String authKey,@Body Profile1 body );

    @GET(API.GET_PROFILE_BY_USER_NAME+"/{UserName}")
    Call<String> getProfileByUserName(@Header("Authorization") String authKey,@Path("UserName") String UserName);

    @POST(API.GET_PROFILE_BY_PHONE)
    Call<ArrayList<Profile1>> getProfileByPhone(@Header("Authorization") String authKey,@Body Profile1 body);


    @POST(API.GET_PROFILE_BY_EMAIL)
    Call<String> getProfileByEmail(@Header("Authorization") String authKey,@Body Profile1 body);

    @GET("Profiles/{id}")
    Call<Profile1> getProfileByID(@Header("Authorization") String authKey,@Path("id") int id );

    @POST(API.GET_PROFILE_BY_USER_NAME_AND_PASSWORD)
    Call<ArrayList<Profile1>> loginApiByUsernamePassword(@Header("Authorization") String authKey, @Body Profile1 body);

    @PUT("Profiles/{id}")
    Call<String> updateProfileById(@Header("Authorization") String authKey,@Path("id") int id,@Body Profile1 userProfile);*/


    @POST(API.PROFILE)
    Call<TravellerAgentProfiles> loginApi(@Header("Authorization") String authKey, @Body TravellerAgentProfiles body);

    @POST(API.GET_PROFILE_BY_UNIQUE_ID)
    Call<ArrayList<TravellerAgentProfiles>> loginApiBuUniqueId(@Header("Authorization") String authKey, @Body TravellerAgentProfiles body);

    @GET(API.GET_PROFILE_BY_USER_NAME+"/{UserName}")
    Call<String> getProfileByUserName(@Header("Authorization") String authKey, @Path("UserName") String UserName);

    @POST(API.GET_PROFILE_BY_PHONE)
    Call<ArrayList<TravellerAgentProfiles>> getProfileByPhone(@Header("Authorization") String authKey, @Body TravellerAgentProfiles body);


    @POST(API.GET_PROFILE_BY_EMAIL)
    Call<String> getProfileByEmail(@Header("Authorization") String authKey, @Body TravellerAgentProfiles body);

    /*@POST("TravellerAgentProfile/GetProfileByReferralCode")
    Call<ArrayList<TravellerAgentProfiles>> getProfileByReferCode(@Header("Authorization") String authKey, @Body ReferCodeModel body);

    @POST("TravellerAgentProfile/GetTravellerAgentProfilesByReferralCodeUsed")
    Call<ArrayList<TravellerAgentProfiles>> getProfilesByUsedReferCode(@Header("Authorization") String authKey, @Body ReferCodeModel body);
*/
    @GET("TravellerAgentProfiles/{id}")
    Call<TravellerAgentProfiles> getProfileByID(@Header("Authorization") String authKey, @Path("id") int id);

    @POST(API.GET_PROFILE_BY_USER_NAME_AND_PASSWORD)
    Call<ArrayList<TravellerAgentProfiles>> loginApiByUsernamePassword(@Header("Authorization") String authKey, @Body TravellerAgentProfiles body);

    @PUT("TravellerAgentProfiles/{id}")
    Call<String> updateProfileById(@Header("Authorization") String authKey, @Path("id") int id, @Body TravellerAgentProfiles userProfile);

    @GET("NotificationManger/GetNotificationManagersByHotelId/{HotelId}")
    Call<ArrayList<NotificationManager>> getNotificationByHotelID(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);
}
