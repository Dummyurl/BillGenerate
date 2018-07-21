package app.zingo.com.billgenerate.Activiies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.Model.*;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.*;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InventoryOptionsActivity extends AppCompatActivity {

    Button mAll,mOne,mNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_inventory_options);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Inventory");

            /*
            mAll.setVisibility(View.GONE);*/
            mOne = (Button)findViewById(R.id.send_one_hotel);
            mNotifications = (Button)findViewById(R.id.notifications);
            mAll = (Button)findViewById(R.id.all_hotel);

            mOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent one = new Intent(InventoryOptionsActivity.this,OneHotelInventoryActivity.class);
                    startActivity(one);
                }
            });

            mNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent one = new Intent(InventoryOptionsActivity.this,NotificationListActivity.class);
                    startActivity(one);
                }
            });

            mAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getHotels();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent main = new Intent(InventoryOptionsActivity.this,MainActivity.class);
                    startActivity(main);
                    this.finish();
                    return true;


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent main = new Intent(InventoryOptionsActivity.this,MainActivity.class);
        startActivity(main);
        this.finish();
    }

    private void getHotels() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(InventoryOptionsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi hotelOperation = Util.getClient().create(LoginApi.class);
                Call<ArrayList<HotelDetails>> response = hotelOperation.getHotelsList(auth_string/*userId*/);

                response.enqueue(new Callback<ArrayList<app.zingo.com.billgenerate.Model.HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<app.zingo.com.billgenerate.Model.HotelDetails>> call, Response<ArrayList<app.zingo.com.billgenerate.Model.HotelDetails>> response) {
                        System.out.println("GetHotelByProfileId = " + response.code());


                        if (progressDialog != null)
                            progressDialog.dismiss();
                        try{
                            if (response.code() == 200) {
                                if (response.body() != null && response.body() .size() != 0) {


                                    for(int i=0;i<response.body().size();i++){

                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");

                                        Date date = new Date();
                                        String notifyDate = sdf.format(date);
                                        FireBaseModel fm = new FireBaseModel();
                                        fm.setSenderId("415720091200");
                                        fm.setServerId("AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M");
                                        fm.setHotelId(response.body().get(i).getHotelId());
                                        fm.setTitle("Please Update Inventory");

                                        fm.setMessage(response.body().get(i).getHotelId()+"-"+ PreferenceHandler.getInstance(InventoryOptionsActivity.this).getUserId()+"-"+response.body().get(i).getHotelName()+"-"+notifyDate);
                                        //registerTokenInDB(fm);
                                        sendNotification(fm);
                                    }

//


                                } else {
                                    Toast.makeText(InventoryOptionsActivity.this, "No Hotels", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(InventoryOptionsActivity.this, "Check your internet connection or please try after some time",
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

                        Toast.makeText(InventoryOptionsActivity.this, "Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }


    public void sendNotification(final FireBaseModel fireBaseModel) {

        final ProgressDialog dialog = new ProgressDialog(InventoryOptionsActivity.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();


        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(InventoryOptionsActivity.this);
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

                                Toast.makeText(InventoryOptionsActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                                //sendEmailattache();
                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(fireBaseModel.getTitle());
                                nf.setNotificationFor(fireBaseModel.getMessage());
                                nf.setHotelId(fireBaseModel.getHotelId());
                                savenotification(nf);



                            } else {

                                Toast.makeText(InventoryOptionsActivity.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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

        final ProgressDialog dialog = new ProgressDialog(InventoryOptionsActivity.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(InventoryOptionsActivity.this);
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


                                    Toast.makeText(InventoryOptionsActivity.this, "Notification Saved", Toast.LENGTH_SHORT).show();




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
}
