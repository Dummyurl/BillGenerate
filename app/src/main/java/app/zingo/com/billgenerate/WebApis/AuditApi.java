package app.zingo.com.billgenerate.WebApis;

import app.zingo.com.billgenerate.Model.AuditSettlement;
import app.zingo.com.billgenerate.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by new on 2/28/2018.
 */

public interface AuditApi {

    @POST(API.AUDIT)
    Call<AuditSettlement> postAudit(@Header("Authorization") String authKey, @Body AuditSettlement body);

    @GET(API.AUDIT+"/{id}")
    Call<AuditSettlement> getAudit(@Header("Authorization") String authKey, @Path("id") int id);

    @PUT(API.AUDIT+"/{id}")
    Call<AuditSettlement> updateAudit(@Header("Authorization") String authKey, @Path("id") int id, @Body AuditSettlement body);
}
