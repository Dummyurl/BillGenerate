package app.zingo.com.billgenerate;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.NotificationManager;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 10-05-2018.
 */

public interface NotificationManagerApi {

    @GET("NotificationManagers")
    Call<ArrayList<NotificationManager>> getNotification(@Header("Authorization") String authKey);

    @POST("NotificationManagers")
    Call<NotificationManager> saveNotification(@Header("Authorization") String auth, @Body NotificationManager hotelNotification);
}
