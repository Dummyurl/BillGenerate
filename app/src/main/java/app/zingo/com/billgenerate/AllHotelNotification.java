package app.zingo.com.billgenerate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.zingo.com.billgenerate.Activiies.NotificationOptions;
import app.zingo.com.billgenerate.Utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.billgenerate.Model.*;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllHotelNotification extends AppCompatActivity {


    EditText mTitle,mMessage;
    Button mSend;
    ArrayList<HotelDetails> hotelList;
    int hotelId;
    String hotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_all_hotel_notification);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Send Inventory");


            mTitle = (EditText) findViewById(R.id.notification_title);

            mMessage = (EditText) findViewById(R.id.notification_message);
            mSend = (Button)findViewById(R.id.send_notify);


           /* mProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    hotelId = hotelList.get(i).getHotelId();
                    hotelName = hotelList.get(i).getHotelName();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
*/
            mSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mTitle.getText().toString()==null||mTitle.getText().toString().isEmpty()){
                        mTitle.setError("Please Enter Title");
                        mTitle.requestFocus();
                    }else  if(mMessage.getText().toString()==null||mMessage.getText().toString().isEmpty()){
                        mMessage.setError("Please Enter Title");
                        mMessage.requestFocus();
                    }else{

                        getHotels();


                    }

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


        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(AllHotelNotification.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi hotelOperation = Util.getClient().create(LoginApi.class);
                Call<ArrayList<HotelDetails>> response = hotelOperation.getHotelsList(auth_string/*userId*/);

                response.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<app.zingo.com.billgenerate.Model.HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        System.out.println("GetHotelByProfileId = " + response.code());
                        hotelList = response.body();

                        if (progressDialog != null)
                            progressDialog.dismiss();
                        try{
                            if (response.code() == 200) {
                                if (hotelList != null && hotelList.size() != 0) {

                                    for(int i=0;i<response.body().size();i++){
                                        System.out.println("Hotel Name=="+hotelName);

                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");

                                        Date date = new Date();
                                        String notifyDate = sdf.format(date);
                                        FireBaseModel fm = new FireBaseModel();
                                        fm.setSenderId("415720091200");
                                        fm.setServerId("AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M");
                                        fm.setHotelId(response.body().get(i).getHotelId());
                                        fm.setTitle("Dear "+"!"+notifyDate+"="+mTitle.getText().toString());

                                        fm.setMessage(mMessage.getText().toString());
                                        //registerTokenInDB(fm);
                                        sendNotification(fm);
                                    }

//


                                } else {
                                    Toast.makeText(AllHotelNotification.this, "No Hotels", Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent(AllHotelNotification.this,AddHotelActivity.class);
                                startActivity(intent);*/
                                }
                            } else {
                                Toast.makeText(AllHotelNotification.this, "Check your internet connection or please try after some time",
                                        Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(Call<ArrayList<app.zingo.com.billgenerate.Model.HotelDetails>> call, Throwable t) {
                        System.out.println("Failed");
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Toast.makeText(AllHotelNotification.this, "Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public void sendNotification(final FireBaseModel fireBaseModel) {

        final ProgressDialog dialog = new ProgressDialog(AllHotelNotification.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();


        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(AllHotelNotification.this);
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

                                Toast.makeText(AllHotelNotification.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                                //sendEmailattache();
                                app.zingo.com.billgenerate.Model.NotificationManager nf = new app.zingo.com.billgenerate.Model.NotificationManager();
                                nf.setNotificationText(fireBaseModel.getTitle());
                                nf.setNotificationFor(fireBaseModel.getMessage());
                                nf.setHotelId(fireBaseModel.getHotelId());
                                savenotification(nf);



                            } else {

                                Toast.makeText(AllHotelNotification.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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

    private void savenotification(final app.zingo.com.billgenerate.Model.NotificationManager notification) {

        final ProgressDialog dialog = new ProgressDialog(AllHotelNotification.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(AllHotelNotification.this);
                LoginApi travellerApi = Util.getClient().create(LoginApi.class);
                Call<app.zingo.com.billgenerate.Model.NotificationManager> response = travellerApi.saveNotification(auth_string,notification);

                response.enqueue(new Callback<app.zingo.com.billgenerate.Model.NotificationManager>() {
                    @Override
                    public void onResponse(Call<app.zingo.com.billgenerate.Model.NotificationManager> call, Response<app.zingo.com.billgenerate.Model.NotificationManager> response) {

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


                                    Toast.makeText(AllHotelNotification.this, "Notification Saved", Toast.LENGTH_SHORT).show();




                                    //Toast.makeText(AllHotelNotification.this, "Save Notification", Toast.LENGTH_SHORT).show();




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
                    Intent main = new Intent(AllHotelNotification.this,NotificationOptions.class);
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
