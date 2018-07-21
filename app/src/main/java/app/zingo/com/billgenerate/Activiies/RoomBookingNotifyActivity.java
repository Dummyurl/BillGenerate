package app.zingo.com.billgenerate.Activiies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;

/*import app.zingo.com.hotelmanagement.Customviews.CustomFontTextView;
import app.zingo.com.hotelmanagement.DataBase.DataBaseHelper;
import app.zingo.com.hotelmanagement.Model.Bookings1;
import app.zingo.com.hotelmanagement.Model.CurrentDateStastics;
import app.zingo.com.hotelmanagement.Model.OverAllStastics;
import app.zingo.com.hotelmanagement.Model.Traveller;
import app.zingo.com.hotelmanagement.Util.CloseButton;
import app.zingo.com.hotelmanagement.Util.PreferenceHandler;
import app.zingo.com.hotelmanagement.WebApi.BookingApi;
import app.zingo.com.hotelmanagement.WebApi.DashBoardApi;
import app.zingo.com.hotelmanagement.WebApi.TravellerApi;*/
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.WebApis.BookingApi;
import app.zingo.com.billgenerate.WebApis.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoomBookingNotifyActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView mTitle,mBody;
    TextView mName,mGuestCount,mBookingNumber,mCID,mCOD,mTotal,mCommision,mNet;
    Button mClose,mAllocateRoom,mBookingList;
    ScrollView sv;

    String title,message;
    String bookingNumber;
    int agentId,hotelId;
    String activity;

    //DataBaseHelper db= new DataBaseHelper(RoomBookingNotifyActivity.this);



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_room_booking_notify);

            title = getIntent().getExtras().getString("Title");
            message = getIntent().getExtras().getString("Message");
            activity = getIntent().getExtras().getString("Activity");

            mClose = (Button) findViewById(R.id.btnClose);
            mAllocateRoom = (Button) findViewById(R.id.btnAllocate);
            mBookingList = (Button) findViewById(R.id.btnList);
            mTitle = (TextView) findViewById(R.id.title_message);
            mBody = (TextView) findViewById(R.id.body_message);
            sv = (ScrollView) findViewById(R.id.scrool_notification);
            mGuestCount = (TextView) findViewById(R.id.brief_detail_pax_details);
            mName = (TextView) findViewById(R.id.brief_detail_traveller_name);
            //  mBookingNumber = (TextView) findViewById(R.id.brief_detail_booking_number);
            mCID = (TextView) findViewById(R.id.brief_detail_check_in_date);
            mCOD = (TextView) findViewById(R.id.brief_detail_check_out_date);
            mTotal = (TextView) findViewById(R.id.total_amount_info);
            mCommision = (TextView) findViewById(R.id.paid_amount_info);
            mNet = (TextView) findViewById(R.id.balance_amount_info);

            mTitle.setText(title);

            System.out.println("Message = "+message);
            System.out.println("Title = "+title);
            if(message.contains(":")){
                String bookin[] = message.split(":");
                // bookingNumber = bookin[1];
                if(bookin[1].contains(",")){
                    String bookinn[] = bookin[1].split(",");
                    bookingNumber = bookinn[0];
                    agentId = Integer.parseInt(bookinn[1]);
                    mBody.setText(bookin[0]+bookinn[0]);

                }else{
                    bookingNumber = bookin[1];
                    mBody.setText(message);
                }
                if(bookingNumber!=null){

                    System.out.println("Bookinlll=="+bookingNumber);
                    getDetails(bookingNumber);
                    //mBody.setText(bookin[0]);
                    // mBody.setVisibility(View.GONE);
                    //  mBookingNumber.setText("Booking Number: #"+bookingNumber);
                }else{

                }
                // getDetails(bookingNumber);
                System.out.println("Boo=="+bookingNumber);
            }else{
                mAllocateRoom.setVisibility(View.GONE);
                mBookingList.setVisibility(View.GONE);
                sv.setVisibility(View.GONE);


            }

            if(title.equalsIgnoreCase("Cancelled Booking")){
                mAllocateRoom.setVisibility(View.GONE);
            }



           /* mAllocateRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent active = new Intent(RoomBookingNotifyActivity.this,AvailableRoom.class);
                    active.putExtra("BookingNumber",bookingNumber);
                    active.putExtra("AgentId",agentId);
                    active.putExtra("BookingName","Quick");
                    startActivity(active);
                    RoomBookingNotifyActivity.this.finish();
                }
            });


            mBookingList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent active = new Intent(RoomBookingNotifyActivity.this,ActiveBookingActivity.class);
                    startActivity(active);
                    RoomBookingNotifyActivity.this.finish();
                }
            });*/


            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                /*Intent active = new Intent(RoomBookingNotifyActivity.this,MainActivity.class);
                startActivity(active);*/
                   // RoomBookingNotifyActivity.this.finish();
                    /*try {
                        CloseButton.closeButton(RoomBookingNotifyActivity.this,hotelId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }



    }


    public void getDetails(final String bookingsnum)
    {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(RoomBookingNotifyActivity.this);
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                Call<ArrayList<Bookings1>> getBookingsApiResponse = bookingApi.getBookingByNum(authenticationString,bookingsnum);

                getBookingsApiResponse.enqueue(new Callback<ArrayList<Bookings1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                        int code = response.code();

                        if(code == 200)
                        {

                           // System.out.println("Nou"+response.body().get(0).getBookingNumber());

                            try{
                                mGuestCount.setText("No of Guest: "+response.body().get(0).getNoOfAdults());
                                String cit   = response.body().get(0).getCheckInDate();
                                String cot = response.body().get(0).getCheckOutDate();
                                hotelId = response.body().get(0).getHotelId();
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                                //mCID.setText(simpleDateFormat1.format(cit));
                                // mCOD.setText(simpleDateFormat1.format(cot));
                                /*getDashBoardDetails(response.body().get(0).getHotelId());
                                getOverAllStastics(response.body().get(0).getHotelId());*/


                                if(cit != null && cot != null)
                                {
                                    try {
                                        Date CIDate = null,CODate= null;
                                        if(cit.contains("-"))
                                        {
                                            CIDate  =  simpleDateFormat2.parse(cit);
                                            CODate  =  simpleDateFormat2.parse(cot);
                                        }
                                        else
                                        {
                                            CIDate  =  simpleDateFormat.parse(cit);
                                            CODate  =  simpleDateFormat.parse(cot);
                                        }
                                        mCID.setText(simpleDateFormat1.format(CIDate));
                                        mCOD.setText(simpleDateFormat1.format(CODate));
                                    /*mCIT.setText(booked.getCheckInTime());
                                    mCOT.setText(booked.getCheckOutTime());*/

                                    }
                                    catch (ParseException ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }

                                mTotal.setText("₹ "+response.body().get(0).getTotalAmount());
                                mCommision.setText("₹ "+response.body().get(0).getCommissionAmount());
                                double total = response.body().get(0).getTotalAmount();
                                double commision = response.body().get(0).getCommissionAmount();
                                double net = total-commision;
                                mNet.setText("₹ "+net);
                                getTravellerDetails(response.body().get(0).getTravellerId(),response.body().get(0).getHotelId());
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                        else
                        {
                            Toast.makeText(RoomBookingNotifyActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {

                        System.out.println(t.getMessage());
                        Toast.makeText(RoomBookingNotifyActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getTravellerDetails(final int travellerId, final int hotelid) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(RoomBookingNotifyActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                TravellerApi travellerApi = Util.getClient().create(TravellerApi.class);
                Call<Traveller> getTravellerDetails = travellerApi.getTravellerDetails(auth_string,travellerId);

                getTravellerDetails.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {

                        if(response.code() == 200)
                        {
                            try{
                                mName.setText(response.body().getFirstName());

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            System.out.println("traveller = "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {

                    }
                });
            }
        });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(activity!=null&&activity.equalsIgnoreCase("DashBoard")){
            Intent back  = new Intent(RoomBookingNotifyActivity.this,MainActivity.class);
            startActivity(back);
            RoomBookingNotifyActivity.this.finish();
        }else if(activity!=null&&activity.equalsIgnoreCase("Notification")){
            Intent back  = new Intent(RoomBookingNotifyActivity.this,NotificationListActivity.class);
            startActivity(back);
            RoomBookingNotifyActivity.this.finish();
        }else{
            RoomBookingNotifyActivity.this.finish();
        }

    }

    public void back(){


        try {
            //CloseButton.closeButton(RoomBookingNotifyActivity.this,hotelId);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}