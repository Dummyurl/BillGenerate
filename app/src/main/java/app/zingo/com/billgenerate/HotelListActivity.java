package app.zingo.com.billgenerate;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.zingo.com.billgenerate.Utils.RecyclerToutchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class HotelListActivity extends AppCompatActivity {

    RecyclerView mHotelList;
    TextView mWelComeMessage,mUserName;
    FloatingActionButton mAddHotelFabBtn;
    String brokerMain = null;

    ArrayList<HotelDetails> hotelDetailsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_hotel_list);


            setTitle("Hotels");

            mHotelList = (RecyclerView)findViewById(R.id.hotel_list);
            //mWelComeMessage =(TextView) findViewById(R.id.welcome_message);
            //mUserName = (TextView) findViewById(R.id.user_name);
           // mAddHotelFabBtn = (FloatingActionButton) findViewById(R.id.add_hotel_fab_btn);
            //mUserName.setText(PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName());
            //getTimeFromAndroid();

            Bundle bundle = getIntent().getExtras();

               final String screen = getIntent().getStringExtra("ScreenName");


            System.out.println("Notification = "+screen);


            mHotelList.addOnItemTouchListener(new RecyclerToutchListener(HotelListActivity.this, new RecyclerToutchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {

                    if(screen == null)
                    {
                        Intent intent = new Intent(HotelListActivity.this,CompetitiveAnalisysActivity.class);
                        intent.putExtra("HotelName",hotelDetailsArrayList.get(position).getHotelName());
                        intent.putExtra("HotelId",hotelDetailsArrayList.get(position).getHotelId());
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(HotelListActivity.this,NotificationDetailsActivity.class);
                        intent.putExtra("HotelName",hotelDetailsArrayList.get(position).getHotelName());
                        intent.putExtra("HotelId",hotelDetailsArrayList.get(position).getHotelId());
                        startActivity(intent);
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        /*getHotelsByProfileId();*/

            getTaggedHotels();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getTimeFromAndroid() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        try{
            if(hours>=1 && hours<=12){
                //Toast.makeText(this, "Good Morning "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
                //mWelComeMessage.setText("Good Morning");
            }else if(hours>=12 && hours<=16){
                //Toast.makeText(this, "Good Afternoon "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
                //mWelComeMessage.setText("Good Afternoon");
            }else if(hours>=16 && hours<=21){
                //mWelComeMessage.setText("Good Evening");
                //Toast.makeText(this, "Good Evening "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
            }else if(hours>=21 && hours<=24){
                //mWelComeMessage.setText("Good Night");
                //Toast.makeText(this, "Good Night "+PreferenceHandler.getInstance(HotelListActivity.this).getUserFullName(), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void getTaggedHotels()
    {
        final ProgressDialog dialog = new ProgressDialog(HotelListActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                //System.out.println("BASE64 = "+token);
                /*String email = PreferenceHandler.getInstance(HotelListActivity.this).getUserName();
                String password = PreferenceHandler.getInstance(HotelListActivity.this).getPhoneNumber();*/

                //String authentication = email+":"+password;
                //String authenticationString = Util.getToken(HotelListActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations hotelOperation = Util.getClient().create(HotelOperations.class);
                Call<ArrayList<HotelDetails>> response = hotelOperation.getHotel(Util.key);

                response.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        //System.out.println("GetHotelByProfileId = "+response.code());
                        ArrayList<HotelDetails> hotelDetailseResponse = response.body();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200 || response.code() == 201 || response.code() == 204)
                        {
                            try{
                                if(hotelDetailseResponse != null && hotelDetailseResponse.size() != 0)
                                {

                                /*if(hotelDetailseResponse.size() ==1)
                                {
                                    HotelDetails details = hotelDetailseResponse.get(0);
                                    PreferenceHandler.getInstance(HotelListActivity.this).setHotelId(details.getHotelId());
                                    PreferenceHandler.getInstance(HotelListActivity.this).setHotelName(details.getHotelName());
                                    PreferenceHandler.getInstance(HotelListActivity.this).setHotelPlace(details.getCity());
                                    PreferenceHandler.getInstance(HotelListActivity.this).setHotelApproved(details.getApproved());
                                    Intent intent = new Intent(HotelListActivity.this, PendingCheckOutActivities.class);
                                    startActivity(intent);
                                }


                                else
                                {*/
                                    ArrayList<HotelDetails> taggedProfiles = response.body();
                                    hotelDetailsArrayList = new ArrayList<>();
                                    if(hotelDetailsArrayList != null && hotelDetailsArrayList.size() != 0)
                                    {
                                        hotelDetailsArrayList.clear();
                                    }

                                    for (int i=0;i<taggedProfiles.size();i++)
                                    {
                                        if(taggedProfiles.get(i) != null && taggedProfiles.get(i).getApproved())
                                        {
                                            hotelDetailsArrayList.add(taggedProfiles.get(i));
                                        }
                                    }
                                    HotelListAdapter adapter = new HotelListAdapter(HotelListActivity.this,hotelDetailsArrayList);
                                    mHotelList.setAdapter(adapter);


                                    //mAddHotelFabBtn.setVisibility(View.GONE);
                                    //}
                                }
                                else
                                {
                                    /*Toast.makeText(HotelListActivity.this,getResources().getString(R.string.no_hotels_added),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(HotelListActivity.this,MainActivity.class);
                                    startActivity(intent);*/

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                        else
                        {
                            Toast.makeText(HotelListActivity.this,"Check your internet connection or please try after some time",
                                    Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelDetails>> call, Throwable t) {
                        System.out.println("Failed");
                        System.out.println(" Exception = "+t.getMessage());
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(HotelListActivity.this,"Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }






    public void exit()
    {
        //PreferenceHandler.getInstance(HotelListActivity.this).clear();
        //SharedPreferenceHandler.getInstance(HotelsListActivity.this).setUserPhoneNumber("");

        /*Intent logoutIntent = new Intent(HotelListActivity.this,LogoutConfirm.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        this.finish();*/
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(HotelListActivity.this);
            builder.setTitle("Are you sure you want to exit?");
            //builder.setCancelable(false);
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finishAffinity();

                    System.exit(0);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {


            exit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        /*getHotelsByProfileId();*/

    }


}
