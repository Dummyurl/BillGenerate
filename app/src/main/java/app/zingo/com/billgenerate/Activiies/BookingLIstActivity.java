package app.zingo.com.billgenerate.Activiies;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.zingo.com.billgenerate.Adapter.BookingConfirmOtaAdapter;
import app.zingo.com.billgenerate.Adapter.SettlementAdapters;
import app.zingo.com.billgenerate.Model.*;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import app.zingo.com.billgenerate.WebApis.AccountApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingLIstActivity extends AppCompatActivity {

    RecyclerView mBookingList;
    TextView allfromDate,alltoDate;
    Button mSearchallBookings;
    Spinner mFilter;

    EditText mBookingID;
    Button mGet;
    RadioButton mOTAId,mZingoId;
    LinearLayout mBookingLayout;

    String to,from;
    ArrayList<Bookings1> till;
    String allFromDate,allToDate;

    BookingConfirmOtaAdapter adapter;

    //intent
    int hotelID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_booking_list);

            setTitle("Booking List");

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){
                allFromDate = bundle.getString("From");
                allToDate = bundle.getString("To");
            }

            mBookingList = (RecyclerView) findViewById(R.id.booking_list);
            allfromDate = (TextView) findViewById(R.id.all_book_from_date);
            alltoDate = (TextView) findViewById(R.id.all_book_to_date);
            mSearchallBookings = (Button)findViewById(R.id.all_search_bookings);
            mFilter = (Spinner) findViewById(R.id.filter_search);

            mBookingID = (EditText)findViewById(R.id.bill_booking_id);
            mGet = (Button)findViewById(R.id.search_booking);
            mBookingLayout = (LinearLayout) findViewById(R.id.filter_by_id);
            mBookingLayout.setVisibility(View.GONE);

            mOTAId = (RadioButton) findViewById(R.id.ota_id);
            mZingoId = (RadioButton) findViewById(R.id.zingo_id);
            mZingoId.setChecked(true);

            hotelID = getIntent().getIntExtra("HotelId",0);

            allfromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDatePicker(allfromDate);
                }
            });

            alltoDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDatePicker(alltoDate);
                }
            });

            if(allFromDate!=null && !allFromDate.isEmpty() && allToDate!=null &&!allToDate.isEmpty()){
                getAll(allFromDate,allToDate);
            }

            mSearchallBookings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    allFromDate = allfromDate.getText().toString();
                    allToDate = alltoDate.getText().toString();

                    if(allFromDate == null || allFromDate.isEmpty())
                    {
                        //allfromDate.setError("Should not be empty");
                        Toast.makeText(BookingLIstActivity.this,"Please select from date",Toast.LENGTH_SHORT).show();
                        allfromDate.requestFocus();
                    }
                    else if(allToDate == null || allToDate.isEmpty())
                    {
                        //alltoDate.setError("Should not be empty");
                        Toast.makeText(BookingLIstActivity.this,"Please select to date",Toast.LENGTH_SHORT).show();
                        alltoDate.requestFocus();
                    }
                    else
                    {
                        getAll(allFromDate,allToDate);
                    }
                }
            });

            mBookingID.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                        filterBookings(charSequence.toString().toLowerCase());



                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getAll(final String from,final String to)
    {
        final ProgressDialog dialog = new ProgressDialog(BookingLIstActivity.this);
        dialog.setTitle("Loading...");
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BookingLIstActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                AccountApi api = Util.getClient().create(AccountApi.class);
                Call<ArrayList<Bookings1>> allBookings = api.getAllBookingByHotelId
                        (auth_string, hotelID);

                allBookings.enqueue(new Callback<ArrayList<Bookings1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            try{
                                if(response.body() != null && response.body().size() != 0)
                                {
                                    ArrayList<Bookings1> list = response.body();
                                    String filter = mFilter.getSelectedItem().toString();

                                    till = new ArrayList<>();
                                    ArrayList<Bookings1> noShow = new ArrayList<>();
                                    ArrayList<Bookings1> cancel = new ArrayList<>();
                                    ArrayList<Bookings1> quick = new ArrayList<>();
                                    ArrayList<Bookings1> delay = new ArrayList<>();
                                    ArrayList<Bookings1> completed = new ArrayList<>();
                                    ArrayList<Bookings1> active = new ArrayList<>();
                                    ArrayList<Bookings1> confirmed = new ArrayList<>();
                                /*adapter = new SettlementAdapters(TillDateBookings.this,list);
                                mWeekList.setAdapter(adapter);*/
                                    for (int i=0;i<list.size();i++)
                                    {
                                        try {
                                            System.out.println(list.get(i).getCheckOutDate());

                                            Date checkout,bookDate,checkinDate,payment = null;
                                            Date froms;
                                            Date too;

                                            if(list.get(i).getCheckOutDate().contains("-"))
                                            {
                                                checkout  = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).getCheckOutDate());
                                                froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                too = new SimpleDateFormat("MM/dd/yyyy").parse(to);
                                            }
                                            else
                                            {
                                                checkout  = new SimpleDateFormat("MM/dd/yyyy").parse(list.get(i).getCheckOutDate());
                                                froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                too = new SimpleDateFormat("MM/dd/yyyy").parse(to);
                                            }
                                            if(list.get(i).getCheckInDate().contains("-"))
                                            {
                                                checkinDate  = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).getCheckInDate());
                                                froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                too = new SimpleDateFormat("MM/dd/yyyy").parse(to);
                                            }
                                            else
                                            {
                                                checkinDate  = new SimpleDateFormat("MM/dd/yyyy").parse(list.get(i).getCheckInDate());
                                                froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                too = new SimpleDateFormat("MM/dd/yyyy").parse(to);
                                            }
                                            if(list.get(i).getBookingDate().contains("-"))
                                            {
                                                bookDate  = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).getBookingDate());
                                                froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                too = new SimpleDateFormat("MM/dd/yyyy").parse(to);
                                            }
                                            else
                                            {
                                                bookDate  = new SimpleDateFormat("MM/dd/yyyy").parse(list.get(i).getBookingDate());
                                                froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                too = new SimpleDateFormat("MM/dd/yyyy").parse(to);
                                            }

                                            if(list.get(i).getPaymentList()!=null&&list.get(i).getPaymentList().size()!=0){

                                                for(int j=0;j<list.get(i).getPaymentList().size();j++){

                                                    String paymentDate[] = list.get(i).getPaymentList().get(j).getPaymentDate().split("T");
                                                    payment = new SimpleDateFormat("yyyy-MM-dd").parse(paymentDate[0]);
                                                    froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                    too = new SimpleDateFormat("MM/dd/yyyy").parse(to);

                                                    if(froms.getTime() == payment.getTime()||too.getTime() == payment.getTime())
                                                        break;

                                                }

                                            }else{
                                                payment = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/1970");
                                                froms = new SimpleDateFormat("MM/dd/yyyy").parse(from);
                                                too = new SimpleDateFormat("MM/dd/yyyy").parse(to);
                                            }
                                           /* if((froms.getTime() <= checkout.getTime() && too.getTime() >= checkout.getTime())||(froms.getTime() <= checkinDate.getTime() && too.getTime() >= checkinDate.getTime())||(froms.getTime() <= bookDate.getTime() && too.getTime() >= bookDate.getTime())||(froms.getTime() <= payment.getTime() && too.getTime() >= payment.getTime()))
                                            {
                                                till.add(list.get(i));
                                            }*/

                                            if((froms.getTime() <= checkinDate.getTime() && too.getTime() >= checkinDate.getTime()))
                                            {
                                                till.add(list.get(i));

                                                if(list.get(i).getBookingStatus().equalsIgnoreCase("Abandoned")){

                                                    noShow.add(list.get(i));

                                                }else  if(list.get(i).getBookingStatus().equalsIgnoreCase("Cancelled")){

                                                    cancel.add(list.get(i));

                                                }else  if(list.get(i).getBookingStatus().equalsIgnoreCase("Quick")){

                                                    quick.add(list.get(i));
                                                    confirmed.add(list.get(i));

                                                }else  if(list.get(i).getBookingStatus().equalsIgnoreCase("Delay")){

                                                    delay.add(list.get(i));
                                                    confirmed.add(list.get(i));

                                                }else  if(list.get(i).getBookingStatus().equalsIgnoreCase("Completed")){

                                                    completed.add(list.get(i));
                                                    confirmed.add(list.get(i));

                                                }else  if(list.get(i).getBookingStatus().equalsIgnoreCase("Active")){

                                                    active.add(list.get(i));
                                                    confirmed.add(list.get(i));

                                                }
                                            }



                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    if(till.size() != 0)
                                    {


                                        if(filter.equalsIgnoreCase("All")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,till,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);

                                        }else if(filter.equalsIgnoreCase("No Show")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,noShow,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);


                                        }else  if(filter.equalsIgnoreCase("Cancelled")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,cancel,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Quick")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,quick,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Delay")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,delay,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Completed")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,completed,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Active")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,active,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Confirmed")){

                                            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,confirmed,allFromDate,allToDate);
                                            mBookingList.setAdapter(adapter);

                                        }


                                        mBookingLayout.setVisibility(View.VISIBLE);



                                    }
                                    else
                                    {
                                        Toast.makeText(BookingLIstActivity.this, "Till that date no checkin happened", Toast.LENGTH_LONG).show();
                                   /* Intent intent = new Intent(TillDateBookings.this, MainActivity.class);
                                    startActivity(intent);*/
                                        BookingLIstActivity.this.finish();
                                    }
                                }
                                else
                                {
                                /*Intent intent = new Intent(TillDateBookings.this, MainActivity.class);
                                startActivity(intent);
                                TillDateBookings.this.finish();*/
                                    Toast.makeText(BookingLIstActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                        else
                        {
                            Toast.makeText(BookingLIstActivity.this,response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }


    public void openDatePicker(final TextView tv) {
        // Get Current Date

        final Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay   = c.get(Calendar.DAY_OF_MONTH);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingLIstActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("Date", "DATE SELECTED "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year,monthOfYear,dayOfMonth);

                        String date1 = (monthOfYear + 1)  + "/" + dayOfMonth + "/" + year;

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");


                        if (tv.equals(allfromDate)){

                            try {
                                Date tdate = sdf.parse(date1);
                                from = sdf.format(tdate);
                                System.out.println("To = "+from);
                                tv.setText(from);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                        }else if (tv.equals(alltoDate)) {

                            try {
                                Date tdate = sdf.parse(date1);
                                to = sdf.format(tdate);
                                System.out.println("To = "+to);
                                tv.setText(to);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        /*else
                        {
                            dateOfBirth = date1;
                            dob_traveler.setText(dateOfBirth);
                        }*/


                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();

    }

    private void filterBookings(String s) {

        ArrayList<Bookings1> filteredList = new ArrayList<>();

        try{
            for(int i=0;i<till.size();i++)
            {

                String otaId = "";

                if(mZingoId.isChecked()){
                    int id = till.get(i).getBookingId();
                    String ids = String.valueOf(id);
                    if(ids.contains(s))
                    {
                        filteredList.add(till.get(i));
                    }
                }else if(mOTAId.isChecked()){
                    if(till.get(i).getOTABookingID()!=null){
                        otaId= till.get(i).getOTABookingID();
                    }

                    if(otaId.contains(s))
                    {
                        filteredList.add(till.get(i));
                    }
                }


            }

            adapter = new BookingConfirmOtaAdapter(BookingLIstActivity.this,filteredList,allFromDate,allToDate);
            mBookingList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("OTA Error "+e.getMessage());
            //Toast.makeText(this, "OTA Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try{
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent main = new Intent(BookingLIstActivity.this,MainActivity.class);
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
            Intent main = new Intent(BookingLIstActivity.this,MainActivity.class);
            startActivity(main);
            this.finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
