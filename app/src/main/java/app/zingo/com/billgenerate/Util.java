package app.zingo.com.billgenerate;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ZingoHotels.com on 6/20/2018.
 */


public class Util {


    private static Retrofit retrofit = null;
    private static final int PERMISSION_RESULT = 1;
    public static final String key = "Basic YmhhcmFtdTIxOjg5MDQxNTE0MDQ=";
    public static final String senderId = "415720091200";
    public static final String serverId = "AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M";

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://zingotesting.azurewebsites.net/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
