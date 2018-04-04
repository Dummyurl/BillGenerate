package app.zingo.com.billgenerate;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.Documents;
import app.zingo.com.billgenerate.Model.Profile1;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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
    Call<Documents> getHotelDocumentsById(@Header("Authorization") String authKey,@Path("id") int id);




}
