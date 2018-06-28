package app.zingo.com.billgenerate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.FireBaseModel;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Adapter.PropertyAdapter;
import app.zingo.com.billgenerate.Model.SearchBook;
import app.zingo.com.billgenerate.Model.SearchBooking;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBookingByHotel extends AppCompatActivity {
    
    Spinner mProperty;
    EditText mMobileET,mCheckinET,mCheckoutET;
    private RecyclerView booking_list;
    ArrayList<SearchBook> bookings1ArrayList;
    ArrayList<Traveller> travellerArrayList;
    LinearLayout mMobileLay,mCheckinLay,mCheckOutLay;
    Button mSearch;
    int mYear,mMonth,mDay;
    String to,from;
    ArrayList<HotelDetails> chainsList;
    int hotelId;
    Bookings1 updateBooking;
    String property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_get_booking_by_hotel);
            mProperty = (Spinner) findViewById(R.id.bill_property_name);
            mMobileLay = (LinearLayout)findViewById(R.id.visible_layout_mobile);
            mCheckinLay = (LinearLayout)findViewById(R.id.checkin_layout);
            mMobileET = (EditText)findViewById(R.id.search_mobile);
            mCheckinET = (EditText)findViewById(R.id.search_checkin);
            mCheckoutET = (EditText)findViewById(R.id.search_checkout);
            mCheckinET.setFocusable(false);
            mCheckinET.setFocusableInTouchMode(false);
            mCheckoutET.setFocusable(false);
            mCheckoutET.setFocusableInTouchMode(false);
            mCheckOutLay = (LinearLayout)findViewById(R.id.checkout_layout);
            mSearch = (Button)findViewById(R.id.all_search_bookings);
            booking_list = (RecyclerView) findViewById(R.id.all_bookings_list);
            mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String allFromDate = mCheckinET.getText().toString();
                    String allToDate = mCheckoutET.getText().toString();
                    String mobile = mMobileET.getText().toString();

                    if(allFromDate == null || allFromDate.isEmpty())
                    {
                        if(allToDate == null || allToDate.isEmpty())
                        {
                            if(mobile != null || !mobile.isEmpty())
                            {
                                getBookings();

                            }else{
                                Toast.makeText(GetBookingByHotel.this,"Please fill the field",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if(allToDate == null || allToDate.isEmpty())
                    {
                        if(allFromDate == null || allFromDate.isEmpty())
                        {
                            if(mobile != null || !mobile.isEmpty())
                            {
                                getBookings();

                            }else{
                                Toast.makeText(GetBookingByHotel.this,"Please fill the field",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if(allFromDate != null || !allFromDate.isEmpty()){
                        if (allToDate!=null|| !allToDate.isEmpty()){
                            if(mobile==null||mobile.isEmpty() ||mobile!=null||!mobile.isEmpty() ){
                                getBookings();
                            }else{
                                Toast.makeText(GetBookingByHotel.this,"Please fill the field",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(GetBookingByHotel.this,"Please fill the field",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        getBookings();
                    }

                }
            });

            mCheckinET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDatePicker(mCheckinET);
                }
            });

            mCheckoutET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDatePicker(mCheckoutET);
                }
            });

            getHotels();

            mProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               /* System.out.println("Id=="+chainsList.get(i).getDocumentId());
                getDoc(chainsList.get(i).getDocumentId());*/

                    hotelId = chainsList.get(i).getHotelId();
                    property = chainsList.get(i).getHotelName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //addAllBooking();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void openDatePicker(final TextView tv) {
        // Get Current Date

        try{
            final Calendar c = Calendar.getInstance();
            mYear  = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay   = c.get(Calendar.DAY_OF_MONTH);
            //launch datepicker modal
            DatePickerDialog datePickerDialog = new DatePickerDialog(GetBookingByHotel.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Log.d("Date", "DATE SELECTED "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year,monthOfYear,dayOfMonth);

                            String date1 = (monthOfYear + 1)  + "/" + dayOfMonth + "/" + year;

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                            if (tv.equals(mCheckinET)){
                                // from = date1;



                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                try {
                                    Date fdate = simpleDateFormat.parse(date1);

                                    from = simpleDateFormat.format(fdate);
                                    System.out.println("To = "+from);
                                    tv.setText(from);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //


                            }else  {
                                //to = date1;


                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                try {
                                    Date tdate = simpleDateFormat.parse(date1);

                                    /*from = simpleDateFormat.format(fdate);

                                    tv.setText(from);*/
                                    to = simpleDateFormat.format(tdate);
                                    System.out.println("To = "+to);
                                    tv.setText(to);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }



                        }
                    }, mYear, mMonth, mDay);


            datePickerDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    private void getBookings() {
        final ProgressDialog dialog = new ProgressDialog(GetBookingByHotel.this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();
        final SearchBooking search = new SearchBooking();
        search.setHotelId(hotelId);
        search.setMobile(mMobileET.getText().toString());
        search.setFromdate(mCheckinET.getText().toString());
        search.setToDate(mCheckoutET.getText().toString());

        System.out.println("Hotel id1=="+search.getHotelId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi bookingApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(GetBookingByHotel.this);
                final Call<ArrayList<SearchBook>> getAllBookings = bookingApi.
                        getSearchBookings(authenticationString,search);

                getAllBookings.enqueue(new Callback<ArrayList<SearchBook>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SearchBook>> call, Response<ArrayList<SearchBook>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            SearchBook book = new SearchBook();
                            if(response.body() != null)
                            {
                                System.out.println("Success "+response.body());
                                bookings1ArrayList = response.body();
                                SearchBookingAdapter bookingRecyclerViewAdapter =
                                        new SearchBookingAdapter(GetBookingByHotel.this,bookings1ArrayList);
                                booking_list.setAdapter(bookingRecyclerViewAdapter);

                            }
                            //getTavellers(bookings1ArrayList);
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList<SearchBook>> call, Throwable t) {

                        Toast.makeText(GetBookingByHotel.this,"Fail",Toast.LENGTH_LONG);
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });


    }

    public class SearchBookingAdapter extends RecyclerView.Adapter<SearchBookingAdapter.ViewHolder> {


        private Context context;
        private ArrayList<SearchBook> bookingArrayList;
        //ArrayList<Traveller> travellerArrayList;

        public SearchBookingAdapter(Context context, ArrayList<SearchBook> bookingArrayList)
        {
            this.context = context;
            this.bookingArrayList = bookingArrayList;
            // this.travellerArrayList = travellerArrayList;
        }
        @Override
        public SearchBookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            try{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_booking_adapter,
                        parent,false);
                return new SearchBookingAdapter.ViewHolder(view);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }


        }

        @Override
        public void onBindViewHolder(final SearchBookingAdapter.ViewHolder holder, final int position) {

            try{
                final SearchBook bookings1 = bookingArrayList.get(position);
                //Traveller traveller = travellerArrayList.get(position);

                if(bookings1 != null)
                {
                    holder.mBookedPersonName.setText(bookings1.getFirstName());
                    holder.mBookedNumber.setText(bookings1.getBookingNumber());
                    holder.mBookedFrom.setText(bookings1.getCheckInDate());
                    holder.mBookedTo.setText(bookings1.getCheckOutDate());
                    holder.mBookedStatus.setText(bookings1.getBookingStatus());
                    holder.mRoom.setText(bookings1.getRoomNo());
                    holder.mEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getBookings(bookings1.getBookingId());
                        }
                    });

                }
            }catch (Exception e){
                e.printStackTrace();
            }



        }

        @Override
        public int getItemCount() {
            return bookingArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mBookedPersonName,mBookedNumber,mBookedFrom,mBookedTo,mBookedStatus,mEdit,mRoom;

            public ViewHolder(View itemView) {
                super(itemView);
                try{
                    mBookedPersonName = (TextView) itemView.findViewById(R.id.booked_person_name_s);
                    mBookedNumber = (TextView) itemView.findViewById(R.id.booked_number);
                    mBookedFrom = (TextView) itemView.findViewById(R.id.booked_from);
                    mBookedTo = (TextView) itemView.findViewById(R.id.booked_to);
                    mRoom = (TextView) itemView.findViewById(R.id.booked_room);
                    mBookedStatus = (TextView) itemView.findViewById(R.id.booked_status);
                    mEdit = (TextView) itemView.findViewById(R.id.edit);


                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        }





    }

    private void cancelBooking() {
        final ProgressDialog dialog = new ProgressDialog(GetBookingByHotel.this);
        dialog.setTitle("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi bookingApi = Util.getClient().create(LoginApi.class);
                final Bookings1 dBook = updateBooking;
                dBook.setBookingStatus("Cancelled");


                String authenticationString = Util.getToken(GetBookingByHotel.this);
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

                                Toast.makeText(GetBookingByHotel.this,"Booking cancelled",Toast.LENGTH_SHORT).show();

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

                                String cit = sdf.format(format.parse(cits));
                                String cot = sdf.format(format.parse(cots));

                                FireBaseModel fm = new FireBaseModel();
                                fm.setSenderId("415720091200");
                                fm.setServerId("AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M");
                                fm.setHotelId(updateBooking.getHotelId());
                                fm.setTitle("Cancelled Booking");
                                fm.setMessage("Sorry! "+property+" got one cancel booking for "+nights +" nights from "+cit+" to "+cot+"\nBooking Number:"+updateBooking.getBookingNumber());
                                //registerTokenInDB(fm);
                               sendNotification(fm);
                            }
                            else
                            {
                                if(dialog != null)
                                {
                                    dialog.dismiss();
                                }
                                Toast.makeText(GetBookingByHotel.this,"Please try after some time",Toast.LENGTH_SHORT).show();
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


    private void getHotels() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(GetBookingByHotel.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi hotelOperation = Util.getClient().create(LoginApi.class);
                Call<ArrayList<HotelDetails>> response = hotelOperation.getHotelsList(auth_string/*userId*/);

                response.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        System.out.println("GetHotelByProfileId = " + response.code());
                        chainsList = response.body();

                        if (progressDialog != null)
                            progressDialog.dismiss();

                        if (response.code() == 200) {
                            if (chainsList != null && chainsList.size() != 0) {
                                PropertyAdapter chainAdapter = new PropertyAdapter(GetBookingByHotel.this, chainsList);
                                mProperty.setAdapter(chainAdapter);
//


                            } else {
                                Toast.makeText(GetBookingByHotel.this, "No Hotels", Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent(GetBookingByHotel.this,AddHotelActivity.class);
                                startActivity(intent);*/
                            }
                        } else {
                            Toast.makeText(GetBookingByHotel.this, "Check your internet connection or please try after some time",
                                    Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelDetails>> call, Throwable t) {
                        System.out.println("Failed");
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Toast.makeText(GetBookingByHotel.this, "Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public  void getBookings(final int bookingId)
    {
        LoginApi api = Util.getClient().create(LoginApi.class);
        String authenticationString = Util.getToken(GetBookingByHotel.this);
        Call<Bookings1> getBooking = api.getBookingById(authenticationString,bookingId);

        getBooking.enqueue(new Callback<Bookings1>() {
            @Override
            public void onResponse(Call<Bookings1> call, Response<Bookings1> response) {
                if(response.code() == 200)
                {
                    try{

                        updateBooking = response.body();
                        if(response.body() != null)
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(GetBookingByHotel.this);
                            builder.setTitle("Do you want to edit Booking");
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                              /*  System.out.println("room id = "+list.get(pos).getRoomId());
                                Intent intent = new Intent(GetBookingByHotel.this,AddRoomsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("roomupdate",list.get(pos));
                                intent.putExtras(bundle);
                                startActivity(intent);*/

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelBooking();
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
                           

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Bookings1> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void sendNotification(final FireBaseModel fireBaseModel) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(GetBookingByHotel.this);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.send(auth_string, fireBaseModel)/*getString()*/;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, retrofit2.Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if (statusCode == 200) {

                            ArrayList<String> list = response.body();

                            Toast.makeText(GetBookingByHotel.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                            //sendEmailattache();
                            NotificationManager nf = new NotificationManager();
                            nf.setNotificationText(fireBaseModel.getTitle());
                            nf.setNotificationFor(fireBaseModel.getMessage());
                            nf.setHotelId(fireBaseModel.getHotelId());
                            savenotification(nf);



                        } else {

                            Toast.makeText(GetBookingByHotel.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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

        final ProgressDialog dialog = new ProgressDialog(GetBookingByHotel.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(GetBookingByHotel.this);
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


                                Toast.makeText(GetBookingByHotel.this, "Notification Save Successfully", Toast.LENGTH_SHORT).show();

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
}
