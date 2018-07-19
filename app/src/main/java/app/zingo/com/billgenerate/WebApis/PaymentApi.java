package app.zingo.com.billgenerate.WebApis;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.Payment;
import app.zingo.com.billgenerate.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by CSC on 1/26/2018.
 */

public interface PaymentApi {

    @POST(API.PAYMENT)
    Call<Payment> addPayment(@Header("Authorization") String authKey, @Body Payment Payment);

    @PUT(API.PAYMENT+"/{id}")
    Call<Payment> updatePayment(@Header("Authorization") String authKey, @Path("id") int id, @Body Payment Payment);

    @DELETE(API.PAYMENT+"/{id}")
    Call<Payment> deletePayment(@Header("Authorization") String authKey, @Path("id") int id);

    @GET(API.PAYMENT+"/GetPaymentsByBookingNumber/{BookingNumber}")
    Call<ArrayList<Payment>> getPaymentsByBookingNum(@Header("Authorization") String authKey, @Path("BookingNumber") String BookingNumber);

    @GET("Payments/{id}")
    Call<Payment> getPayment(@Header("Authorization") String authKey, @Path("id") int id);

}
