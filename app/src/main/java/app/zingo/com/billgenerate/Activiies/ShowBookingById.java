package app.zingo.com.billgenerate.Activiies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.BookingsNotificationManagers;
import app.zingo.com.billgenerate.Model.FireBaseModel;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.Utils.Util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBookingById extends AppCompatActivity {

    EditText mBookingID;
    Button mGet;
    LinearLayout mLayout;
    HotelDetails list;
    RadioButton mOTAId,mZingoId;

    TextView mBookedPersonName,mBookedDate,mBookingDates,mNoofRooms,
            mNetAmount,mShortName,mStatus,mPayAtHotel,mBookedRoom,mBookingSourceType,mOtaBookingId;
    LinearLayout mparent;
    int bookingId;
    Bookings1 updateBooking;
    String property;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_show_booking_by_id);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Booking");

            mBookingID = (EditText)findViewById(R.id.bill_booking_id);
            mGet = (Button)findViewById(R.id.search_booking);

            mBookedPersonName = (TextView) findViewById(R.id.booked_person_name);
            mOTAId = (RadioButton) findViewById(R.id.ota_id);
            mZingoId = (RadioButton) findViewById(R.id.zingo_id);
            mZingoId.setChecked(true);
            mLayout = (LinearLayout) findViewById(R.id.layout_booking);
            mBookedDate = (TextView) findViewById(R.id.booked_date);
            mBookingDates = (TextView) findViewById(R.id.booked_from_to_date);
            mOtaBookingId = (TextView) findViewById(R.id.ota_booking_id);
            mNoofRooms = (TextView) findViewById(R.id.booked_no_rooms_night);
            mNetAmount = (TextView) findViewById(R.id.net_amount);
            mShortName = (TextView) findViewById(R.id.person_short_name);
            mStatus = (TextView) findViewById(R.id.booking_status);
            mPayAtHotel = (TextView) findViewById(R.id.pay_at_hotel);
            mBookedRoom = (TextView) findViewById(R.id.call_booked_room_no);
            mparent = (LinearLayout) findViewById(R.id.parent_layout_for_user_details);
            mBookingSourceType = (TextView) findViewById(R.id.booking_source_type);

            mGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = mBookingID.getText().toString();

                    if(id==null||id.isEmpty()){
                        mBookingID.setError("Should not be Empty");
                        mBookingID.requestFocus();
                    }else{
                        //bookingId = Integer.parseInt(id);
                        if(mOTAId.isChecked()){
                            getBookings(id);
                        }else if(mZingoId.isChecked()){
                            try{

                                getBookingsByZingoId(Integer.parseInt(id));

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(ShowBookingById.this, "Please Select any option", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(updateBooking==null){
                        Toast.makeText(ShowBookingById.this, "No Bookings Found", Toast.LENGTH_SHORT).show();
                    }else{
                        try{
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ShowBookingById.this);
                            builder.setTitle("Do you want to edit Booking");
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ShowBookingById.this,UpdateBookingsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("BOOKINGS",updateBooking);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelBookingBox();
                                }
                            });

                            builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void cancelBookingBox(){

        try{
            final AlertDialog.Builder builder = new AlertDialog.Builder(ShowBookingById.this);
            builder.setTitle("Do you want to cancel Booking");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelBooking();
                }
            });

            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });



            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void cancelBooking() {
        final ProgressDialog dialog = new ProgressDialog(ShowBookingById.this);
        dialog.setTitle("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi bookingApi = Util.getClient().create(LoginApi.class);
                final Bookings1 dBook = updateBooking;
                dBook.setBookingStatus("Cancelled");


                String authenticationString = Util.getToken(ShowBookingById.this);
                Call<String> checkout = bookingApi.updateBookingStatus(authenticationString,dBook.getBookingId(),dBook);
                checkout.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {


                        try
                        {
                            if(response.code() == 204)
                            {
                               // updateRoom(dialog,dBook);
                                if(dialog != null)
                                {
                                    dialog.dismiss();
                                }

                                Toast.makeText(ShowBookingById.this,"Booking cancelled",Toast.LENGTH_SHORT).show();

                                String from = updateBooking.getCheckInDate() + " 00:00:00";
                                String to = updateBooking.getCheckOutDate() + " 00:00:00";


                                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                                Date d1 = null;
                                Date d2 = null;
                                long diffDays = 0;
                                String nights = "1";
                                try {
                                    d1 = format.parse(from);
                                    d2 = format.parse(to);
                                    long diff = d2.getTime() - d1.getTime();
                                    diffDays = diff / (24 * 60 * 60 * 1000);
                                    nights = ""+diffDays;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
                                String cits = updateBooking.getCheckInDate() + " 00:00:00";
                                String cots = updateBooking.getCheckOutDate() + " 00:00:00";
                                String bookingDate = updateBooking.getBookingDate() + " 00:00:00";

                                String cit = sdf.format(format.parse(cits));
                                String cot = sdf.format(format.parse(cots));


                                String bookDates = sdf.format(format.parse(bookingDate));

                                DecimalFormat df = new DecimalFormat("#,###.##");
                                double netAmt = updateBooking.getTotalAmount() - (updateBooking.getCommissionAmount()-updateBooking.getOTAServiceFees());

                                FireBaseModel fm = new FireBaseModel();
                                fm.setSenderId("415720091200");
                                fm.setServerId("AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M");
                                fm.setHotelId(updateBooking.getHotelId());
                                fm.setTitle("Cancelled Booking");
                                fm.setMessage("Sorry! "+property+" got one cancel booking for "+nights +" nights from "+cit+" to "+cot+"\nBooking ID:"+updateBooking.getBookingId());
                                fm.setTravellerName(mBookedPersonName.getText().toString());
                                fm.setNoOfGuest("No of Guest: "+updateBooking.getNoOfAdults());
                                fm.setCheckInDate(cit);
                                fm.setCheckOutDate(cot);
                                fm.setTotalAmount("Rs. "+updateBooking.getTotalAmount());
                                fm.setCommissionAmount("Rs. "+updateBooking.getCommissionAmount());
                                fm.setNetAmount("Rs. "+df.format(netAmt));
                                SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
                                SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
                                fm.setNotificationDate(sdfs.format(new Date()));
                                fm.setNotificationTime(sdft.format(new Date()));
                                fm.setBookingDateTime(bookDates+","+updateBooking.getBookingTime());
                                sendNotification(fm);
                            }
                            else
                            {
                                if(dialog != null)
                                {
                                    dialog.dismiss();
                                }
                                Toast.makeText(ShowBookingById.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
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
                    Intent main = new Intent(ShowBookingById.this,CancelOptions.class);
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
        try{
            Intent main = new Intent(ShowBookingById.this,CancelOptions.class);
            startActivity(main);
            this.finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void getBookingsByZingoId(final int bookingId)
    {
        LoginApi api = Util.getClient().create(LoginApi.class);
        String authenticationString = Util.getToken(ShowBookingById.this);
        Call<Bookings1> getBooking = api.getBookingById(authenticationString,bookingId);

        getBooking.enqueue(new Callback<Bookings1>() {
            @Override
            public void onResponse(Call<Bookings1> call, Response<Bookings1> response) {
                if(response.code() == 200)
                {
                    try{


                        if(response.body() != null)
                        {
                            updateBooking = response.body();
                            mBookedDate.setText("Booked On: "+getBookedOnDateFormate(response.body().getBookingDate()));
                            if(response.body().getCheckInDate()!=null && !response.body().getCheckInDate().isEmpty()&&response.body().getCheckOutDate()!=null && !response.body().getCheckOutDate().isEmpty()){
                                mBookingDates.setText(getBookingDateFormate(response.body().getCheckInDate())
                                        +" To "+getBookingDateFormate(response.body().getCheckOutDate()));
                            }
                            mNetAmount.setText("Net Amount: ₹ "+response.body().getTotalAmount());
                            if(response.body().getBookingSourceType() != null)
                            {
                                mBookingSourceType.setText(response.body().getBookingSourceType());
                            }

                            if(response.body().getOTABookingID()!=null&&!response.body().getOTABookingID().isEmpty()){
                                mOtaBookingId.setText(""+response.body().getOTABookingID());
                            }

                            if(response.body().getBookingStatus().equalsIgnoreCase("Active")){
                                mStatus.setText("Confirmed");
                                mStatus.setBackgroundColor(Color.GREEN);
                            }else  if(response.body().getBookingStatus().equalsIgnoreCase("Quick")|| response.body().getBookingStatus().equalsIgnoreCase("Delay")){
                                mStatus.setText("Confirmed");
                                mStatus.setBackgroundColor(Color.GREEN);
                            }else if(response.body().getBookingStatus().equalsIgnoreCase("Cancelled")){

                                mStatus.setText("Cancelled");
                                mStatus.setBackgroundColor(Color.RED);
                            }else if(response.body().getBookingStatus().equalsIgnoreCase("Abandoned")){
                                mStatus.setText("No Show");
                                mStatus.setBackgroundColor(Color.CYAN);
                            }else{
                                mStatus.setText("Confirmed");
                                mStatus.setBackgroundColor(Color.GREEN);
                            }
                            mNoofRooms.setText((long)getDays(response.body().getCheckInDate(),response.body().getCheckOutDate())+" Night(s)");
                            getTravellerDetails(response.body().getTravellerId());
                            getHotelName(response.body().getHotelId());

                        }else{
                            Toast.makeText(ShowBookingById.this, "No Bookings Found", Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else
                {
                    System.out.println(response.message());
                    Toast.makeText(ShowBookingById.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Bookings1> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(ShowBookingById.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public  void getBookings(final String bookingId)
    {
        LoginApi api = Util.getClient().create(LoginApi.class);
        String authenticationString = Util.getToken(ShowBookingById.this);
        Call<ArrayList<Bookings1>> getBooking = api.getBookingByOTAId(authenticationString,bookingId);

        getBooking.enqueue(new Callback<ArrayList<Bookings1>>() {
            @Override
            public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                if(response.code() == 200)
                {
                    try{


                        if(response.body() != null)
                        {
                            updateBooking = response.body().get(0);
                            mBookedDate.setText("Booked On: "+getBookedOnDateFormate(response.body().get(0).getBookingDate()));
                            if(response.body().get(0).getCheckInDate()!=null && !response.body().get(0).getCheckInDate().isEmpty()&&response.body().get(0).getCheckOutDate()!=null && !response.body().get(0).getCheckOutDate().isEmpty()){
                                mBookingDates.setText(getBookingDateFormate(response.body().get(0).getCheckInDate())
                                        +" To "+getBookingDateFormate(response.body().get(0).getCheckOutDate()));
                            }
                            mNetAmount.setText("Net Amount: ₹ "+response.body().get(0).getTotalAmount());
                            if(response.body().get(0).getBookingSourceType() != null)
                            {
                                mBookingSourceType.setText(response.body().get(0).getBookingSourceType());
                            }

                            if(response.body().get(0).getOTABookingID()!=null&&!response.body().get(0).getOTABookingID().isEmpty()){
                                mOtaBookingId.setText(""+response.body().get(0).getOTABookingID());
                            }

                            mNoofRooms.setText((long)getDays(response.body().get(0).getCheckInDate(),response.body().get(0).getCheckOutDate())+" Night(s)");
                            getTravellerDetails(response.body().get(0).getTravellerId());
                            getHotelName(response.body().get(0).getHotelId());

                            if(response.body().get(0).getBookingStatus().equalsIgnoreCase("Active")){
                                mStatus.setText("Confirmed");
                                mStatus.setBackgroundColor(Color.GREEN);
                            }else  if(response.body().get(0).getBookingStatus().equalsIgnoreCase("Quick")|| response.body().get(0).getBookingStatus().equalsIgnoreCase("Delay")){
                                mStatus.setText("Confirmed");
                                mStatus.setBackgroundColor(Color.GREEN);
                            }else if(response.body().get(0).getBookingStatus().equalsIgnoreCase("Cancelled")){

                                mStatus.setText("Cancelled");
                                mStatus.setBackgroundColor(Color.RED);
                            }else if(response.body().get(0).getBookingStatus().equalsIgnoreCase("Abandoned")){
                                mStatus.setText("No Show");
                                mStatus.setBackgroundColor(Color.CYAN);
                            }else{
                                mStatus.setText("Confirmed");
                                mStatus.setBackgroundColor(Color.GREEN);
                            }

                        }else{
                            Toast.makeText(ShowBookingById.this, "There is no Booking", Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else
                {
                    System.out.println(response.message());
                    Toast.makeText(ShowBookingById.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(ShowBookingById.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String getBookedOnDateFormate(String sdate)
    {
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(sdate);
            String sDate = new SimpleDateFormat("dd MMM yyyy").format(date);
            System.out.println("sDate = "+sDate);
            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    public String getBookingDateFormate(String bdate)
    {
        String sDate = null;
        try {
            if(bdate.contains("-"))
            {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(bdate);
                sDate = new SimpleDateFormat("dd MMM").format(date);
            }
            else
            {
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(bdate);
                sDate = new SimpleDateFormat("dd MMM").format(date);
            }
            System.out.println("sDate = "+sDate);
            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long getDays(String checkInDate, String checkOutDate) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat myFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        /*String inputString1 = "01/10/2018";
        String inputString2 = "01/19/2018";*/
        Date date1,date2;
        try {
            if(checkInDate.contains("-"))
            {
                date1 = myFormat1.parse(checkInDate);
                date2 = myFormat1.parse(checkOutDate);
            }
            else
            {
                date1 = myFormat.parse(checkInDate);
                date2 = myFormat.parse(checkOutDate);
            }
            long diff = date2.getTime() - date1.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void getTravellerDetails(final int travellerId) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(ShowBookingById.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi travellerApi = Util.getClient().create(LoginApi.class);
                Call<Traveller> getTravellerDetails = travellerApi.getTravellerDetails(auth_string,travellerId);

                getTravellerDetails.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {

                        if(response.code() == 200&&response.body()!=null)
                        {
                            try{
                                Traveller traveller = response.body();


                                    if(traveller.getFirstName() != null && !traveller.getFirstName().isEmpty())
                                    {

                                        mBookedPersonName.setText(traveller.getFirstName());

                                        String[] ab = traveller.getFirstName().split(" ");
                                        if(ab.length > 1)
                                        {
                                            //if(ab[1].charAt(0) != "")
                                            mShortName.setText(ab[0].charAt(0)+"");//+""+ab[1].charAt(0));
                                        }
                                        else
                                        {
                                            mShortName.setText(ab[0].charAt(0)+"");
                                        }
                                    }


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

    private void getHotelName(final int id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(ShowBookingById.this);
                Call<HotelDetails> call = apiService.getHotelsById(authenticationString, id);

                call.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog != null)
                                progressDialog.dismiss();
                            list = response.body();

                            if (list != null) {


                                property = list.getHotelName();
                            }

//


                        } else {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(ShowBookingById.this, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    public void sendNotification(final FireBaseModel fireBaseModel) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(ShowBookingById.this);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.sendBookingNotification(auth_string, fireBaseModel)/*getString()*/;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, retrofit2.Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if (statusCode == 200) {

                            ArrayList<String> list = response.body();

                            Toast.makeText(ShowBookingById.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                            //sendEmailattache();
                       /*     NotificationManager nf = new NotificationManager();
                            nf.setNotificationText(fireBaseModel.getTitle());
                            nf.setNotificationFor(fireBaseModel.getMessage());
                            nf.setHotelId(fireBaseModel.getHotelId());
                            savenotification(nf);
*/

                            BookingsNotificationManagers nf = new BookingsNotificationManagers();
                            nf.setTitle(fireBaseModel.getTitle());
                            nf.setMessage(fireBaseModel.getMessage());
                            nf.setHotelId(fireBaseModel.getHotelId());
                            nf.setTravellerName(fireBaseModel.getTravellerName());
                            nf.setCheckInDate(fireBaseModel.getCheckInDate());
                            nf.setCheckOutDate(fireBaseModel.getCheckOutDate());
                            nf.setTotalAmount(fireBaseModel.getTotalAmount());
                            nf.setCommissionAmount(fireBaseModel.getCommissionAmount());
                            nf.setNetAmount(fireBaseModel.getNetAmount());
                            nf.setNoOfGuest(fireBaseModel.getNoOfGuest());
                            nf.setNotificationDate(fireBaseModel.getNotificationDate());
                            nf.setNotificationTime(fireBaseModel.getNotificationTime());
                            nf.setBookingDateTime(fireBaseModel.getBookingDateTime());
                            nf.setBooking("Cancelled");
                            saveBookingnotification(nf);



                        } else {

                            Toast.makeText(ShowBookingById.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        // Log error here since request failed

                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private void savenotification(final NotificationManager notification) {

        final ProgressDialog dialog = new ProgressDialog(ShowBookingById.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(ShowBookingById.this);
                LoginApi travellerApi = Util.getClient().create(LoginApi.class);
                Call<NotificationManager> response = travellerApi.saveNotification(auth_string,notification);

                response.enqueue(new Callback<NotificationManager>() {
                    @Override
                    public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());
                        if(response.code() == 200||response.code() == 201)
                        {
                            if(response.body() != null)
                            {


                                Toast.makeText(ShowBookingById.this, "Notification Save Successfully", Toast.LENGTH_SHORT).show();

                            }
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


    private void saveBookingnotification(final BookingsNotificationManagers notification) {

        final ProgressDialog dialog = new ProgressDialog(ShowBookingById.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(ShowBookingById.this);
                LoginApi travellerApi = Util.getClient().create(LoginApi.class);
                Call<BookingsNotificationManagers> response = travellerApi.saveBookingNotification(auth_string,notification);

                response.enqueue(new Callback<BookingsNotificationManagers>() {
                    @Override
                    public void onResponse(Call<BookingsNotificationManagers> call, Response<BookingsNotificationManagers> response) {

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
                                    Toast.makeText(ShowBookingById.this, "Notification Save Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<BookingsNotificationManagers> call, Throwable t) {
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
