package app.zingo.com.billgenerate.Activiies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.Model.FireBaseModel;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.PreferenceHandler;
import app.zingo.com.billgenerate.Adapter.PropertyAdapter;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneHotelInventoryActivity extends AppCompatActivity {
    
    Spinner mProperty;
    Button mSend;
    ArrayList<HotelDetails> hotelList;
    int hotelId;
    String hotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_one_hotel_inventory);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Send Inventory");
            
            mProperty = (Spinner)findViewById(R.id.bill_property_name);
            mSend = (Button)findViewById(R.id.send_notify);

            getHotels();

            mProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    hotelId = hotelList.get(i).getHotelId();
                    hotelName = hotelList.get(i).getHotelName();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            
            mSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("Hotel Name=="+hotelName);

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");

                    Date date = new Date();
                    String notifyDate = sdf.format(date);
                    FireBaseModel fm = new FireBaseModel();
                    fm.setSenderId("415720091200");
                    fm.setServerId("AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M");
                    fm.setHotelId(hotelId);
                    fm.setTitle("Please Update Inventory");

                    fm.setMessage(hotelId+"-"+ PreferenceHandler.getInstance(OneHotelInventoryActivity.this).getUserId()+"-"+hotelName+"-"+notifyDate);
                    //registerTokenInDB(fm);
                    sendNotification(fm);
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }
        
    }

    private void getHotels() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(OneHotelInventoryActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi hotelOperation = Util.getClient().create(LoginApi.class);
                Call<ArrayList<HotelDetails>> response = hotelOperation.getHotelsList(auth_string/*userId*/);

                response.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        System.out.println("GetHotelByProfileId = " + response.code());
                        hotelList = response.body();

                        if (progressDialog != null)
                            progressDialog.dismiss();
                        try{
                            if (response.code() == 200) {
                                if (hotelList != null && hotelList.size() != 0) {
                                    PropertyAdapter chainAdapter = new PropertyAdapter(OneHotelInventoryActivity.this, hotelList);
                                    mProperty.setAdapter(chainAdapter);
//


                                } else {
                                    Toast.makeText(OneHotelInventoryActivity.this, "No Hotels", Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent(OneHotelInventoryActivity.this,AddHotelActivity.class);
                                startActivity(intent);*/
                                }
                            } else {
                                Toast.makeText(OneHotelInventoryActivity.this, "Check your internet connection or please try after some time",
                                        Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelDetails>> call, Throwable t) {
                        System.out.println("Failed");
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Toast.makeText(OneHotelInventoryActivity.this, "Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public void sendNotification(final FireBaseModel fireBaseModel) {

        final ProgressDialog dialog = new ProgressDialog(OneHotelInventoryActivity.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(OneHotelInventoryActivity.this);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.send(auth_string, fireBaseModel)/*getString()*/;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, retrofit2.Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        try{
                            if (statusCode == 200) {

                                ArrayList<String> list = response.body();

                                Toast.makeText(OneHotelInventoryActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                                //sendEmailattache();
                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(fireBaseModel.getTitle());
                                nf.setNotificationFor(fireBaseModel.getMessage());
                                nf.setHotelId(fireBaseModel.getHotelId());
                                savenotification(nf);



                            } else {

                                Toast.makeText(OneHotelInventoryActivity.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            e.printStackTrace();
                        }


//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        // Log error here since request failed
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private void savenotification(final NotificationManager notification) {

        final ProgressDialog dialog = new ProgressDialog(OneHotelInventoryActivity.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(OneHotelInventoryActivity.this);
                LoginApi travellerApi = Util.getClient().create(LoginApi.class);
                Call<NotificationManager> response = travellerApi.saveNotification(auth_string,notification);

                response.enqueue(new Callback<NotificationManager>() {
                    @Override
                    public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        try{
                            System.out.println(response.code());
                            if(response.code() == 200||response.code() == 201)
                            {
                                if(response.body() != null)
                                {


                                        Toast.makeText(OneHotelInventoryActivity.this, "Notification Saved", Toast.LENGTH_SHORT).show();
                                       



                                    //Toast.makeText(OneHotelInventoryActivity.this, "Save Notification", Toast.LENGTH_SHORT).show();




                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<NotificationManager> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent main = new Intent(OneHotelInventoryActivity.this,InventoryOptionsActivity.class);
                    startActivity(main);
                    this.finish();
                    return true;


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }
}
