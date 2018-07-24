package app.zingo.com.billgenerate.Activiies;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import app.zingo.com.billgenerate.Adapter.SettlementAdapters;
import app.zingo.com.billgenerate.HotelOperations;
import app.zingo.com.billgenerate.Model.BookingAndTraveller;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Payment;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.ThreadExecuter;
import app.zingo.com.billgenerate.WebApis.AccountApi;
import app.zingo.com.billgenerate.WebApis.PaymentApi;
import app.zingo.com.billgenerate.WebApis.TravellerApi;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class TillDateBookings extends AppCompatActivity {

    //View 
    RecyclerView mTillList;
    SettlementAdapters adapter;
    EditText mAdvancePayment;
    TextView allfromDate,alltoDate;
    Button mSearchallBookings,mAdvanceButton;
    Spinner mFilter;

    Button mGenerateReport;

    //variable
    ArrayList<BookingAndTraveller> bookingAndTravellerArrayList;
    String csvFile;

    Traveller traveller;
    HotelDetails details;
    DecimalFormat decimalFormat;
    int travellerCount=0;

    //intent
    int hotelID=0;

    //Variable
    String to,from;
    ArrayList<Traveller> travellerArrayList;
    ArrayList<Bookings1> bookings1ArrayList;
    ArrayList<Bookings1> paymentBookings;
    String allFromDate,allToDate;

    //Summarry Details data
    int totalRoomNights=0;
    double arr=0,totalGrossRevenue=0,totalOtaCommission=0,netRevenue=0,prepaidBookingRevenue=0,
            payHotelRevenue=0,hotelCollected=0,totalCollectedbyhotel=0,hotelPay=0,otaAdjust=0,
            pendingOta=0,prepaidZingo=0,zingoCommission=0,bookingComCommission=0,
            payableByZingo=0,AdvancePayment=0,paymentDone=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_till_date_bookings);

            mTillList = (RecyclerView) findViewById(R.id.settle_list);
            allfromDate = (TextView) findViewById(R.id.all_book_from_date);
            alltoDate = (TextView) findViewById(R.id.all_book_to_date);
            mSearchallBookings = (Button)findViewById(R.id.all_search_bookings);
            mGenerateReport = (Button) findViewById(R.id.generate_activebooking_report_btn);
            mAdvanceButton = (Button) findViewById(R.id.advance_payment_button);
            mAdvancePayment = (EditText) findViewById(R.id.advance_payment);
            mFilter = (Spinner) findViewById(R.id.filter_search);
            //mTillList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            setTitle("Till Date Settlement");

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

            mAdvanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String advancePayment = mAdvancePayment.getText().toString();

                    if(advancePayment==null||advancePayment.isEmpty()){
                        mAdvancePayment.setError("Can't Empty");
                        mAdvancePayment.requestFocus();
                    }else{
                        double payment = Double.parseDouble(advancePayment);
                        if(paymentBookings==null||paymentBookings.size()==0){
                            Toast.makeText(TillDateBookings.this, "There is No Successfull bookings between the date selected", Toast.LENGTH_SHORT).show();
                        }else{

                            double bookingAmount = 0;
                            for(int i =0;i<paymentBookings.size();i++){
                                Bookings1 bookings1 = paymentBookings.get(i);
                                if(bookings1!=null){
                                    double preBookingAmount = bookingAmount;
                                    bookingAmount = bookingAmount+bookings1.getTotalAmount();

                                    if(payment<bookingAmount){
                                        Toast.makeText(TillDateBookings.this, "First "+i+" bookings total amount is "+preBookingAmount, Toast.LENGTH_SHORT).show();
                                        popup(i,preBookingAmount);
                                        break;
                                    }else if(payment==bookingAmount){
                                        Toast.makeText(TillDateBookings.this, "First "+(i+1)+" bookings total amount is "+bookingAmount, Toast.LENGTH_SHORT).show();
                                        popup((i+1),bookingAmount);
                                        break;
                                    }

                                }
                            }
                        }
                    }
                }
            });

            mSearchallBookings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    totalRoomNights=0;
                    arr=0;
                    totalGrossRevenue=0;
                    totalOtaCommission=0;
                    netRevenue=0;
                    prepaidBookingRevenue=0;
                    payHotelRevenue=0;
                    hotelCollected=0;
                    totalCollectedbyhotel=0;
                    hotelPay=0;
                    otaAdjust=0;
                    pendingOta=0;
                    prepaidZingo=0;
                    zingoCommission=0;
                    bookingComCommission=0;
                    payableByZingo=0;
                    AdvancePayment=0;paymentDone=0;
                    allFromDate = allfromDate.getText().toString();
                    allToDate = alltoDate.getText().toString();

                    if(allFromDate == null || allFromDate.isEmpty())
                    {
                        //allfromDate.setError("Should not be empty");
                        Toast.makeText(TillDateBookings.this,"Please select from date",Toast.LENGTH_SHORT).show();
                        allfromDate.requestFocus();
                    }
                    else if(allToDate == null || allToDate.isEmpty())
                    {
                        //alltoDate.setError("Should not be empty");
                        Toast.makeText(TillDateBookings.this,"Please select to date",Toast.LENGTH_SHORT).show();
                        alltoDate.requestFocus();
                    }
                    else
                    {
                        getAll(allFromDate,allToDate);
                    }
                }
            });

            getHotel();

            mGenerateReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(bookingAndTravellerArrayList != null && details != null)
                    {
                        System.out.println("Booking Size = "+bookingAndTravellerArrayList.size());
                        Toast.makeText(TillDateBookings.this, "Booking Size = "+bookingAndTravellerArrayList.size(), Toast.LENGTH_SHORT).show();

                        Collections.sort(bookingAndTravellerArrayList, new Comparator<BookingAndTraveller>() {
                            @Override
                            public int compare(BookingAndTraveller o1, BookingAndTraveller o2) {
                                return o2.getRoomBooking().getCheckInDate().compareTo(o1.getRoomBooking().getCheckInDate());
                            }
                        });

                        boolean isfilecreated = generateReport(bookingAndTravellerArrayList);
                        if(isfilecreated)
                        {
                            sendEmailattache();
                        }

                    }
                }
            });
            
            
        }catch (Exception e){
            e.printStackTrace();
        }
       
    }


    private void sendEmailattache() {

        try{

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
            //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
            //emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
            File root = Environment.getExternalStorageDirectory();
            String pathToMyAttachedFile = "/BookingReconcilation/"+csvFile;
            File file = new File(root, pathToMyAttachedFile);
            if (!file.exists() || !file.canRead()) {
                return;
            }
            Uri uri = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                uri = FileProvider.getUriForFile(TillDateBookings.this, "app.zingo.com.hotelmanagement.fileprovider", file);
            }else{
                uri = Uri.fromFile(file);
            }

            //Uri uri = Uri.fromFile(file);
            if(uri!=null){
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            }else{
                Toast.makeText(TillDateBookings.this, "File cannot access", Toast.LENGTH_SHORT).show();
            }
           /* Uri uri = Uri.fromFile(file);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);*/
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void openDatePicker(final TextView tv) {
        // Get Current Date

        final Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay   = c.get(Calendar.DAY_OF_MONTH);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(TillDateBookings.this,
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

    public void getAll(final String from,final String to)
    {
        final ProgressDialog dialog = new ProgressDialog(TillDateBookings.this);
        dialog.setTitle("Loading...");
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(TillDateBookings.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
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

                                    ArrayList<Bookings1> till = new ArrayList<>();
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

                                            adapter = new SettlementAdapters(TillDateBookings.this,till);
                                            mTillList.setAdapter(adapter);

                                        }else if(filter.equalsIgnoreCase("No Show")){

                                            adapter = new SettlementAdapters(TillDateBookings.this,noShow);
                                            mTillList.setAdapter(adapter);


                                        }else  if(filter.equalsIgnoreCase("Cancelled")){

                                            adapter = new SettlementAdapters(TillDateBookings.this,cancel);
                                            mTillList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Quick")){

                                            adapter = new SettlementAdapters(TillDateBookings.this,quick);
                                            mTillList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Delay")){

                                            adapter = new SettlementAdapters(TillDateBookings.this,delay);
                                            mTillList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Completed")){

                                            adapter = new SettlementAdapters(TillDateBookings.this,completed);
                                            mTillList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Active")){

                                            adapter = new SettlementAdapters(TillDateBookings.this,active);
                                            mTillList.setAdapter(adapter);

                                        }else  if(filter.equalsIgnoreCase("Confirmed")){

                                            adapter = new SettlementAdapters(TillDateBookings.this,confirmed);
                                            mTillList.setAdapter(adapter);

                                        }


                                        bookingAndTravellerArrayList = new ArrayList<>();
                                        paymentBookings = new ArrayList<>();
                                        for(int i=0;i<confirmed.size();i++){



                                            Bookings1 bookings1 = confirmed.get(i);






                                                getTravellerName(confirmed.get(i).getTravellerId(),confirmed.get(i));

                                                String zingoPayment = "Payment Done By Zingo";
                                                int count = 0;
                                                int adjustCount = 0;

                                                if(bookings1.getPaymentList()!=null&&bookings1.getPaymentList().size()!=0){

                                                    for(int j=0;j<bookings1.getPaymentList().size();j++){

                                                        if(zingoPayment.equalsIgnoreCase(bookings1.getPaymentList().get(j).getPaymentStatus())){
                                                            count = 1;
                                                        }

                                                        if(bookings1.getPaymentList().get(j).getPaymentStatus().equalsIgnoreCase("Adjustment")){
                                                            adjustCount = 1;
                                                            otaAdjust = otaAdjust+bookings1.getPaymentList().get(j).getAmount();
                                                        }
                                                    }
                                                }else{
                                                    paymentBookings.add(bookings1);
                                                }

                                                if(count==0){
                                                    paymentBookings.add(bookings1);
                                                }

                                               /* if(adjustCount==0){
                                                    otaAdjust = otaAdjust+bookings1.getOTAToPayHotel();
                                                }*/

                                                if(bookings1.getOTAStatus()!=null){
                                                    if(bookings1.getOTAStatus().equalsIgnoreCase("Pending")){
                                                        pendingOta = pendingOta+bookings1.getOTAToPayHotel();
                                                    }
                                                }


                                                totalRoomNights = totalRoomNights +(bookings1.getNoOfRooms()*bookings1.getDurationOfStay());
                                                totalGrossRevenue = totalGrossRevenue+bookings1.getTotalAmount();

                                                if(bookings1.getBookingSource().equalsIgnoreCase("MAKEMY TRIP")||bookings1.getBookingSource().equalsIgnoreCase("GOIBIBO")){
                                                    totalOtaCommission = totalOtaCommission+bookings1.getOTATotalCommissionAmount();
                                                }else if(bookings1.getBookingSource().equalsIgnoreCase("BOOKING.COM")){
                                                    bookingComCommission = bookingComCommission+bookings1.getOTATotalCommissionAmount();
                                                    totalOtaCommission = totalOtaCommission+bookings1.getOTATotalCommissionAmount();
                                                }

                                                // hotelPay = hotelPay+bookings1.getOTAToPayHotel();
                                                zingoCommission = zingoCommission+bookings1.getZingoCommision();


                                                System.out.println("Added to list");
                                                if(bookings1.getTotalAmount()==bookings1.getBalanceAmount()||bookings1.getBalanceAmount()!=0){

                                                    payHotelRevenue = payHotelRevenue+(((bookings1.getTotalAmount()-bookings1.getOTATotalCommissionAmount())-bookings1.getOTAToPayHotel()));

                                                }else {

                                                    // prepaidBookingRevenue = prepaidBookingRevenue+(bookings1.getTotalAmount()-bookings1.getOTATotalCommissionAmount());

                                                }
                                                //payHotelRevenue = payHotelRevenue+bookings1.getCustomerPaymentAtOTA();
                                                prepaidBookingRevenue = prepaidBookingRevenue+bookings1.getOTAToPayHotel();
                                                hotelPay = hotelPay+bookings1.getHotelToPayOTA();

                                            System.out.println("Added to list");



                                        }

                                        arr = totalGrossRevenue/totalRoomNights;
                                        netRevenue = totalGrossRevenue - (totalOtaCommission);
                                        totalCollectedbyhotel = payHotelRevenue+hotelCollected;
                                        prepaidZingo = prepaidBookingRevenue-hotelPay-otaAdjust-pendingOta;
                                        payableByZingo = prepaidBookingRevenue-hotelCollected-otaAdjust-hotelPay-pendingOta-zingoCommission-bookingComCommission;
                                        paymentDone = payableByZingo-AdvancePayment;

                                    }
                                    else
                                    {
                                        Toast.makeText(TillDateBookings.this, "Till that date no checkin happened", Toast.LENGTH_LONG).show();
                                   /* Intent intent = new Intent(TillDateBookings.this, MainActivity.class);
                                    startActivity(intent);*/
                                        TillDateBookings.this.finish();
                                    }
                                }
                                else
                                {
                                /*Intent intent = new Intent(TillDateBookings.this, MainActivity.class);
                                startActivity(intent);
                                TillDateBookings.this.finish();*/
                                    Toast.makeText(TillDateBookings.this, "There is no data", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                        else
                        {
                            Toast.makeText(TillDateBookings.this,response.message(),
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

    private boolean generateReport(ArrayList<BookingAndTraveller> list) {
        boolean success = false;
        WritableWorkbook workbook = null;
        System.out.println("Booking Size = "+list.size());

        DateFormat dateFormat2 = new SimpleDateFormat("MMM_dd_yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        dateFormat2.format(cal.getTime());

        try {
            File sd = Environment.getExternalStorageDirectory();
            csvFile = dateFormat2.format(cal.getTime())+"_till.xls";

            File directory = new File(sd.getAbsolutePath()+"/BookingReconcilation");
            //create directory if not exist
            if (!directory.exists() && !directory.isDirectory()) {
                directory.mkdirs();
            }
            File file = new File(directory, csvFile);
            String sheetName = ""+dateFormat2.format(cal.getTime());;//name of sheet

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            WritableSheet sheets = workbook.createSheet("Reconcilliation Summary", 1);

            sheet.addCell(new Label(5,0,details.getHotelDisplayName()));
            /*CellView cell=sheet.getColumnView(5);
            cell.setAutosize(true);
            sheet.setColumnView(5, cell);*/
            sheet.mergeCells(5,0,10,0);
            sheet.addCell(new Label(5,1,details.getHotelStreetAddress()+", "+", "+details.getLocalty()+
                    ", "+details.getCity()+", "+details.getState()+" - "+details.getPincode()));
            sheet.mergeCells(5,1,10,1);
            sheet.addCell(new Label(5,2,"Chart As On "+new SimpleDateFormat("MM/dd/yyyy").format(new Date())));
            sheet.mergeCells(5,2,8,2);

            /*CellView cell1=sheet.getColumnView(3);
            cell1.setAutosize(true);
            sheet.setColumnView(3, cell1);*/
            /*sheet.addCell(new Label(3,3,"Generated On "+new SimpleDateFormat("MM/dd/yyyy, hh:mm aa").format(new Date())));
            sheet.mergeCells(3,3,5,3);
            sheet.addCell(new Label(5,3,"User : "+PreferenceHandler.getInstance(TillDateBookings.this).getUserFullName()));
            sheet.mergeCells(6,3,9,3);*/
            sheet.addCell(new Label(3,3,"Generated On "+new SimpleDateFormat("MM/dd/yyyy, hh:mm aa").format(new Date())));
            sheet.mergeCells(3,3,6,3);
            sheet.addCell(new Label(7,3,"User : "+"Zingohotels"));
            sheet.mergeCells(7,3,10,3);
            //sheet.addCell(new Label(5,0,details.getHotelDisplayName()));


            sheet.setColumnView(0, 5);
            sheet.setColumnView(1, 15);
            sheet.setColumnView(2, 20);
            sheet.setColumnView(3, 20);
            sheet.setColumnView(4, 20);
            sheet.setColumnView(5, 25);
            sheet.setColumnView(6, 15);
            sheet.setColumnView(7, 15);
            sheet.setColumnView(8, 10);
            sheet.setColumnView(9, 15);
            sheet.setColumnView(10, 15);
            sheet.setColumnView(11, 15);
            sheet.setColumnView(12, 20);
            sheet.setColumnView(13, 20);
            sheet.setColumnView(14, 20);
            sheet.setColumnView(15, 20);
            sheet.setColumnView(16, 20);
            sheet.setColumnView(17, 20);
            sheet.setColumnView(18, 20);
            sheet.setColumnView(19, 20);
            sheet.setColumnView(20, 20);
            sheet.setColumnView(21, 20);
            sheet.setColumnView(22, 20);
            sheet.setColumnView(23, 30);
            sheet.setColumnView(24, 30);
            sheet.setColumnView(25, 30);
            sheet.setColumnView(26, 20);

            WritableCellFormat cellFormats = new WritableCellFormat();
            cellFormats.setAlignment(Alignment.CENTRE);

            WritableCellFormat cellFormatsRight = new WritableCellFormat();
            cellFormatsRight.setAlignment(Alignment.RIGHT);

            WritableCellFormat cellFormatLeft = new WritableCellFormat();
            cellFormatLeft.setAlignment(Alignment.LEFT);

            WritableCellFormat summaryCellYellow = new WritableCellFormat();
            summaryCellYellow.setAlignment(Alignment.CENTRE);
            summaryCellYellow.setBackground(Colour.YELLOW);
            summaryCellYellow.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellYellowRight = new WritableCellFormat();
            summaryCellYellowRight.setAlignment(Alignment.RIGHT);
            summaryCellYellowRight.setBackground(Colour.YELLOW);
            summaryCellYellowRight.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellBrown = new WritableCellFormat();
            summaryCellBrown.setAlignment(Alignment.CENTRE);
            summaryCellBrown.setBackground(Colour.TAN);
            summaryCellBrown.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellBrownRight = new WritableCellFormat();
            summaryCellBrownRight.setAlignment(Alignment.RIGHT);
            summaryCellBrownRight.setBackground(Colour.TAN);
            summaryCellBrownRight.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellGreen = new WritableCellFormat();
            summaryCellGreen.setAlignment(Alignment.CENTRE);
            summaryCellGreen.setBackground(Colour.LIGHT_GREEN);
            summaryCellGreen.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellGreenRight = new WritableCellFormat();
            summaryCellGreenRight.setAlignment(Alignment.RIGHT);
            summaryCellGreenRight.setBackground(Colour.LIGHT_GREEN);
            summaryCellGreenRight.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellMerun = new WritableCellFormat();
            summaryCellMerun.setAlignment(Alignment.CENTRE);
            summaryCellMerun.setBackground(Colour.ROSE);
            summaryCellMerun.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellMerunRight = new WritableCellFormat();
            summaryCellMerunRight.setAlignment(Alignment.RIGHT);
            summaryCellMerunRight.setBackground(Colour.ROSE);
            summaryCellMerunRight.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellRed = new WritableCellFormat();
            summaryCellRed.setAlignment(Alignment.CENTRE);
            summaryCellRed.setBackground(Colour.LIGHT_ORANGE);
            summaryCellRed.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellRedRight = new WritableCellFormat();
            summaryCellRedRight.setAlignment(Alignment.RIGHT);
            summaryCellRedRight.setBackground(Colour.LIGHT_ORANGE);
            summaryCellRedRight.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellBlue = new WritableCellFormat();
            summaryCellBlue.setAlignment(Alignment.CENTRE);
            summaryCellBlue.setBackground(Colour.AQUA);
            summaryCellBlue.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellBlueRight = new WritableCellFormat();
            summaryCellBlueRight.setAlignment(Alignment.RIGHT);
            summaryCellBlueRight.setBackground(Colour.AQUA);
            summaryCellBlueRight.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellPurple = new WritableCellFormat();
            summaryCellPurple.setAlignment(Alignment.CENTRE);
            summaryCellPurple.setBackground(Colour.LAVENDER);
            summaryCellPurple.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            WritableCellFormat summaryCellPurpleRight = new WritableCellFormat();
            summaryCellPurpleRight.setAlignment(Alignment.RIGHT);
            summaryCellPurpleRight.setBackground(Colour.LAVENDER);
            summaryCellPurpleRight.setBorder(Border.ALL,jxl.format.BorderLineStyle.THIN, Colour.BLACK);

            sheets.setColumnView(0, 5);
            sheets.setColumnView(1, 30);
            sheets.setColumnView(2, 15);

            //Summary Sheet
            sheets.addCell(new Label(0, 0, "S.No",summaryCellYellow));
            sheets.addCell(new Label(1, 0, "Total Summary("+dateFormat2.format(cal.getTime())+")",summaryCellYellow));
            sheets.mergeCells(1,0,2,0);
            sheets.addCell(new Label(0, 1, "1",cellFormats));
            sheets.addCell(new Label(0, 2, "2",cellFormats));
            sheets.addCell(new Label(0, 3, "3",cellFormats));
            sheets.addCell(new Label(0, 4, "4",cellFormats));
            sheets.addCell(new Label(0, 6, "5",summaryCellGreen));
            sheets.addCell(new Label(0, 8, "6",summaryCellMerun));
            sheets.addCell(new Label(0, 10, "A",cellFormats));
            sheets.addCell(new Label(0, 11, "B",cellFormats));
            sheets.addCell(new Label(0, 12, "(A+B)",summaryCellYellow));
            sheets.addCell(new Label(0, 14, "C",summaryCellRed));
            sheets.addCell(new Label(0, 15, "D",cellFormats));
            sheets.addCell(new Label(0, 16, "E",summaryCellYellow));
            sheets.addCell(new Label(0, 18, "F",cellFormats));
            sheets.addCell(new Label(0, 20, "G",summaryCellBlue));
            sheets.addCell(new Label(0, 21, "H",summaryCellBrown));
            sheets.addCell(new Label(0, 24, "I",summaryCellGreen));
            sheets.addCell(new Label(0, 26, "J",summaryCellPurple));

            sheets.addCell(new Label(1, 1, "Total Room Nights",cellFormats));
            sheets.addCell(new Label(1, 2, "ARR",cellFormats));
            sheets.addCell(new Label(1, 3, "Total Gross Revenue",cellFormats));
            sheets.addCell(new Label(1, 4, "Total OTA Commission(Incl OTA Gst)",cellFormats));
            sheets.addCell(new Label(1, 6, "Total Net Revenue(2-4)",summaryCellGreen));
            sheets.addCell(new Label(1, 8, "Prepaid Booking",summaryCellMerun));
            sheets.addCell(new Label(1, 10, "Pay@Hotel Booking",cellFormats));
            sheets.addCell(new Label(1, 11, "Prepaid Amount Collected by "+details.getHotelName(),cellFormats));
            sheets.addCell(new Label(1, 12, "Total Amount Collected By Hotel",summaryCellYellow));
            sheets.addCell(new Label(1, 14, "Extra Payment By OTA(Hotel to Pay)",summaryCellRed));
            sheets.addCell(new Label(1, 15, "OTA Adjusted Amount",cellFormats));
            sheets.addCell(new Label(1, 16, "Pending Payment By OTA",summaryCellYellow));
            sheets.addCell(new Label(1, 18, "Prepaid Amount Collected by Zingo",cellFormats));
            sheets.addCell(new Label(1, 20, "Zingo Commmission",summaryCellBlue));
            sheets.addCell(new Label(1, 21, "Booking.com Commission",summaryCellBrown));
            sheets.addCell(new Label(1, 23, "Payable Amount by Zingo",cellFormats));
            sheets.addCell(new Label(1, 24, "Advance Payment Done by Zingo",summaryCellGreen));
            sheets.addCell(new Label(1, 26, "Payment to be Done",summaryCellPurple));


            decimalFormat = new DecimalFormat("#.##");
            sheets.addCell(new Label(2, 1, ""+totalRoomNights,cellFormatsRight));
            sheets.addCell(new Label(2, 2, "Rs. "+ decimalFormat.format(arr),cellFormatsRight));
            sheets.addCell(new Label(2, 3, "Rs. "+decimalFormat.format(totalGrossRevenue),cellFormatsRight));
            sheets.addCell(new Label(2, 4, "Rs. "+decimalFormat.format(totalOtaCommission),cellFormatsRight));
            sheets.addCell(new Label(2, 6, "Rs. "+decimalFormat.format(netRevenue),summaryCellGreenRight));
            sheets.addCell(new Label(2, 8, "Rs. "+decimalFormat.format(prepaidBookingRevenue),summaryCellMerunRight));
            sheets.addCell(new Label(2, 10, "Rs. "+decimalFormat.format(payHotelRevenue),cellFormatsRight));
            sheets.addCell(new Label(2, 11, "Rs. "+decimalFormat.format(hotelCollected),cellFormatsRight));
            sheets.addCell(new Label(2, 12, "Rs. "+decimalFormat.format(totalCollectedbyhotel),summaryCellYellowRight));
            sheets.addCell(new Label(2, 14, "Rs. "+decimalFormat.format(hotelPay),summaryCellRedRight));
            sheets.addCell(new Label(2, 15, "Rs. "+decimalFormat.format(otaAdjust),cellFormatsRight));
            sheets.addCell(new Label(2, 16, "Rs. "+decimalFormat.format(pendingOta),summaryCellYellowRight));
            sheets.addCell(new Label(2, 18, "Rs. "+decimalFormat.format(prepaidZingo),cellFormatsRight));
            sheets.addCell(new Label(2, 20, "Rs. "+decimalFormat.format(zingoCommission),summaryCellBlueRight));
            sheets.addCell(new Label(2, 21, "Rs. "+decimalFormat.format(bookingComCommission),summaryCellBrownRight));
            sheets.addCell(new Label(2, 23, "Rs. "+decimalFormat.format(payableByZingo),cellFormatsRight));
            sheets.addCell(new Label(2, 24, "Rs. "+decimalFormat.format(AdvancePayment),summaryCellGreenRight));
            sheets.addCell(new Label(2, 26, "Rs. "+decimalFormat.format(paymentDone),summaryCellPurpleRight));

            sheet.addCell(new Label(0, 6, "S.No",cellFormats));
            sheet.addCell(new Label(1, 6, "Booking ID",cellFormats));
            sheet.addCell(new Label(2, 6, "CheckIn Date",cellFormats));
            sheet.addCell(new Label(3, 6, "CheckOut Date",cellFormats));
            sheet.addCell(new Label(4, 6, "Booking Date",cellFormats));
            sheet.addCell(new Label(5, 6, "Traveller Name",cellFormats));
            sheet.addCell(new Label(6, 6, "Status",cellFormats));
            sheet.addCell(new Label(7, 6, "Booking Source",cellFormats));
            sheet.addCell(new Label(8, 6, "No Rooms",cellFormats));
            sheet.addCell(new Label(9, 6, "No of Nights",cellFormats));
            sheet.addCell(new Label(10, 6, "Total Room Nights",cellFormats));
            sheet.addCell(new Label(11, 6, "Room Charge",cellFormats));
            sheet.addCell(new Label(12, 6, "Extra Charges",cellFormats));
            sheet.addCell(new Label(13, 6, "Hotel Taxes",cellFormats));
            sheet.addCell(new Label(14, 6, "Hotel Gross Charges",cellFormats));
            sheet.addCell(new Label(15, 6, "Commission Charges",cellFormats));
            sheet.addCell(new Label(16, 6, "Commission GST Charges",cellFormats));
            sheet.addCell(new Label(17, 6, "Commission (Including GST)",cellFormats));
            sheet.addCell(new Label(18, 6, "Nett Amount",cellFormats));
            sheet.addCell(new Label(19, 6, "Mode of Payment",cellFormats));
            sheet.addCell(new Label(20, 6, "Customer Paid at Hotel",cellFormats));
            sheet.addCell(new Label(21, 6, "OTA To Pay Hotel",cellFormats));
            sheet.addCell(new Label(22, 6, "Hotel to Pay OTA",cellFormats));
            sheet.addCell(new Label(23, 6, "Payment Collected By",cellFormats));
            sheet.addCell(new Label(24, 6, "Zingo Total Commission",cellFormats));
            sheet.addCell(new Label(25, 6, "Payment Status",cellFormats));
           // sheet.addCell(new Label(26, 6, "Difference",cellFormats));
            /*sheet.addCell(new Label(22, 0, "Status"));
            sheet.addCell(new Label(23, 0, "Status"));*/

            for (int i=0;i<list.size();i++)
            {
                System.out.println(" SIze booking == "+list.size());
                Bookings1 bookings1 = list.get(i).getRoomBooking();
                Traveller traveller = list.get(i).getTravellers();
                if(bookings1 !=null && traveller != null)
                {
                        /*CellView cell=sheet.getColumnView(i);
                        cell.setAutosize(true);
                        sheet.setColumnView(i, cell);*/
                    System.out.println(" SIze data== "+i+" zi"+list.size());
                    sheet.addCell(new Label(0, i+7, ""+(i+1),cellFormatsRight));
                    sheet.addCell(new Label(1, i+7, ""+bookings1.getBookingId(),cellFormatsRight));
                    sheet.addCell(new Label(2, i+7, bookings1.getCheckInDate(),cellFormatsRight));
                    sheet.addCell(new Label(3, i+7, bookings1.getCheckOutDate(),cellFormatsRight));
                    sheet.addCell(new Label(4, i+7, bookings1.getBookingDate(),cellFormatsRight));
                    sheet.addCell(new Label(5, i+7, traveller.getFirstName(),cellFormatsRight));
                    sheet.addCell(new Label(6, i+7, bookings1.getBookingStatus()+"",cellFormatsRight));
                    sheet.addCell(new Label(7, i+7, bookings1.getBookingSource(),cellFormatsRight));
                    sheet.addCell(new Label(8, i+7, ""+bookings1.getNoOfRooms(),cellFormatsRight));
                    sheet.addCell(new Label(9, i+7, ""+bookings1.getDurationOfStay(),cellFormatsRight));
                    sheet.addCell(new Label(10, i+7, ""+(bookings1.getNoOfRooms()*bookings1.getDurationOfStay())));
                    sheet.addCell(new Label(11, i+7, ""+bookings1.getSellRate(),cellFormatsRight));
                    sheet.addCell(new Label(12, i+7, ""+bookings1.getExtraCharges()+"",cellFormatsRight));
                    sheet.addCell(new Label(13, i+7, ""+bookings1.getGstAmount()+"",cellFormatsRight));
                    sheet.addCell(new Label(14, i+7, ""+bookings1.getTotalAmount(),cellFormatsRight));
                    sheet.addCell(new Label(15, i+7, ""+bookings1.getOTACommissionAmount(),cellFormatsRight));
                    sheet.addCell(new Label(16, i+7, bookings1.getOTACommissionGSTAmount()+"",cellFormatsRight));
                    sheet.addCell(new Label(17, i+7, bookings1.getOTATotalCommissionAmount()+"",cellFormatsRight));
                    sheet.addCell(new Label(18, i+7, (bookings1.getTotalAmount()-bookings1.getOTATotalCommissionAmount())+"",cellFormatsRight));
                    if(bookings1.getTotalAmount()==bookings1.getBalanceAmount()||bookings1.getBalanceAmount()!=0){
                        sheet.addCell(new Label(19, i+7, "Pay@Hotel",cellFormatsRight));
                        sheet.addCell(new Label(20, i+7, (bookings1.getTotalAmount()-bookings1.getCustomerPaymentAtOTA()+bookings1.getOTAServiceFees())+"",cellFormatsRight));
                    }else if(bookings1.getBalanceAmount()==0){
                        sheet.addCell(new Label(19, i+7, "Prepaid",cellFormatsRight));
                        sheet.addCell(new Label(20, i+7, "0",cellFormatsRight));
                    }



                    sheet.addCell(new Label(21, i+7, bookings1.getOTAToPayHotel()+"",cellFormatsRight));
                    sheet.addCell(new Label(22, i+7, bookings1.getHotelToPayOTA()+"",cellFormatsRight));
                    if(bookings1.getTotalAmount()==bookings1.getBalanceAmount()||bookings1.getBalanceAmount()!=0){
                        sheet.addCell(new Label(23, i+7, details.getHotelName()+"",cellFormatsRight));
                    }else if(bookings1.getBalanceAmount()==0){
                        sheet.addCell(new Label(23, i+7, "Zingo",cellFormatsRight));
                    }

                    sheet.addCell(new Label(24, i+7, bookings1.getZingoCommision()+"",cellFormatsRight));
                    int adjustCount = 0;
                    if(bookings1.getPaymentList()!=null&&bookings1.getPaymentList().size()!=0){
                        for(int j=0;j<bookings1.getPaymentList().size();j++){
                            if(bookings1.getPaymentList().get(j).getPaymentStatus().equalsIgnoreCase("Adjustment")){
                              adjustCount =1;
                                    break;
                            }
                        }
                    }

                    if(adjustCount==1){
                        sheet.addCell(new Label(25, i+7, "Adjustment",cellFormatsRight));
                    }else{
                        if(bookings1.getOTAStatus()!=null&&!bookings1.getOTAStatus().isEmpty()){
                            sheet.addCell(new Label(25, i+7, bookings1.getOTAStatus()+"",cellFormatsRight));
                            //  sheet.addCell(new Label(26, i+7, bookings1.getAuditSettlementList().get(0).getDifferenceAmount()+"",cellFormatsRight));
                        }else{
                            sheet.addCell(new Label(25, i+7, "Pending",cellFormatsRight));
                            // sheet.addCell(new Label(26, i+7, "0",cellFormatsRight));
                        }
                    }



                       /* if(bookings1.getPaymentList() != null && bookings1.getPaymentList().size() != 0)
                        {
                            for (int j=0;j<bookings1.getPaymentList().size();j++)
                            {
                                if(bookings1.getPaymentList().get(j).getPaymentType().equalsIgnoreCase("Cash"))
                                {
                                    cashPayment = cashPayment+bookings1.getPaymentList().get(j).getAmount();
                                }
                                else if(bookings1.getPaymentList().get(j).getPaymentType().equalsIgnoreCase("Online"))
                                {
                                    onlinePayment = onlinePayment+bookings1.getPaymentList().get(j).getAmount();
                                }
                                else if(bookings1.getPaymentList().get(j).getPaymentType().equalsIgnoreCase("Card"))
                                {
                                    cardPayment = cardPayment+bookings1.getPaymentList().get(j).getAmount();
                                }
                                else if(bookings1.getPaymentList().get(j).getPaymentType().equalsIgnoreCase("BTC(Prepaid)"))
                                {
                                    BTCPrepaidPayment = BTCPrepaidPayment+bookings1.getPaymentList().get(j).getAmount();
                                }
                                else if(bookings1.getPaymentList().get(j).getPaymentType().equalsIgnoreCase("BTC(Postpaid)"))
                                {
                                    BTCPostPaid = BTCPostPaid+bookings1.getPaymentList().get(j).getAmount();
                                }
                            }
                        }*/
                }
            }

              /*  sheet.addCell(new Label(2, bookingsList.size()+10, "Total Pax"));
                sheet.addCell(new Label(3, bookingsList.size()+10, mAdvanceNoOfPax.getText().toString()));

                sheet.addCell(new Label(2, bookingsList.size()+11, "Occupied Rooms"));
                sheet.addCell(new Label(3, bookingsList.size()+11, mOccupiedRooms.getText().toString()));

                sheet.addCell(new Label(2, bookingsList.size()+12, "Not Ready Rooms"));
                sheet.addCell(new Label(3, bookingsList.size()+12, mDirtyRooms.getText().toString()));

                sheet.addCell(new Label(2, bookingsList.size()+13, "Blocked Rooms"));
                sheet.addCell(new Label(3, bookingsList.size()+13, mBlockedRooms.getText().toString()));

                sheet.addCell(new Label(2, bookingsList.size()+14, "Reserved Rooms"));
                sheet.addCell(new Label(3, bookingsList.size()+14, mReservedRooms.getText().toString()));

                sheet.addCell(new Label(2, bookingsList.size()+15, "Vacant Rooms"));
                sheet.addCell(new Label(3, bookingsList.size()+15, mVacantRooms.getText().toString()));

                //payment
                sheet.addCell(new Label(5, bookingsList.size()+10, "Cash"));
                sheet.addCell(new Label(6, bookingsList.size()+10, cashPayment+""));

                sheet.addCell(new Label(5, bookingsList.size()+11, "Card"));
                sheet.addCell(new Label(6, bookingsList.size()+11, cardPayment+""));

                sheet.addCell(new Label(5, bookingsList.size()+12, "Online"));
                sheet.addCell(new Label(6, bookingsList.size()+12, onlinePayment+""));

                sheet.addCell(new Label(5, bookingsList.size()+13, "BTC(Prepaid)"));
                sheet.addCell(new Label(6, bookingsList.size()+13, BTCPrepaidPayment+""));

                sheet.addCell(new Label(5, bookingsList.size()+14, "BTC(Postpaid)"));
                sheet.addCell(new Label(6, bookingsList.size()+14, BTCPostPaid+""));*/

                /*sheet.addCell(new Label(2, bookingsList.size()+15, "Vacant Rooms"));
                sheet.addCell(new Label(3, bookingsList.size()+15, mVacantRooms.getText().toString()));*/

            // column and row


            workbook.write();
            System.out.println("Your file is stored in "+file.toString());
            Toast.makeText(TillDateBookings.this,"Your file is stored in "+file.toString(),Toast.LENGTH_LONG).show();
            return true;
        } catch (WriteException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if(workbook != null)
            {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void getHotel() {

        /*final ProgressDialog dialog = new ProgressDialog(TillDateBookings.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();*/
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(TillDateBookings.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations hotelOperation = Util.getClient().create(HotelOperations.class);
                int userId = hotelID;
                Call<HotelDetails> response = hotelOperation.getHotelByHotelId(auth_string,userId);

                response.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {
                        System.out.println("GetHotelByProfileId = "+response.code());
                        HotelDetails hotelDetailseResponse = response.body();
                        /*if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }*/
                        try{
                            if(response.code() == 200)
                            {
                                if(hotelDetailseResponse != null )
                                {
                                    details = hotelDetailseResponse;

                                }
                            /*else
                            {
                                Toast.makeText(TillDateBookings.this,getResources().getString(R.string.no_hotels_added),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TillDateBookings.this,AddHotelActivity.class);
                                startActivity(intent);
                            }*/
                            }
                            else
                            {
                                Toast.makeText(TillDateBookings.this,"Check your internet connection or please try after some time",
                                        Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {
                        System.out.println("Failed");

                        /*if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(TillDateBookings.this,"Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public  void getTravellerName(final int i,final Bookings1 bookings1)
    {
        travellerCount = travellerCount+1;
        String auth_string = Util.getToken(TillDateBookings.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        TravellerApi api = Util.getClient().create(TravellerApi.class);
        Call<Traveller> getTrav = api.getTravellerDetails(auth_string,i);
        System.out.println("Traveller id = "+(travellerCount));



        getTrav.enqueue(new Callback<Traveller>() {
            @Override
            public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                if(response.code() == 200||response.code() == 201||response.code() == 204)
                {
                    BookingAndTraveller bookingAndTraveller = new BookingAndTraveller();
                    if(response.body() != null)
                    {
                        System.out.println("Traveller id 2 = "+(travellerCount));
                        bookingAndTraveller.setTravellers(response.body());
                        bookingAndTraveller.setRoomBooking(bookings1);
                        bookingAndTravellerArrayList.add(bookingAndTraveller);
                    }

                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Traveller> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                /*Intent intent = new Intent(TillDateBookings.this,MainActivity.class);
                startActivity(intent);*/
                back();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getAll(allFromDate,allToDate);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //getAll(allFromDate,allToDate);
    }

    public void back(){


        Intent back = new Intent(TillDateBookings.this, HotelListActivity.class);
        back.putExtra("ScreenName","Payment");
        startActivity(back);
        TillDateBookings.this.finish();


    }


    public void popup(final  int count,final double amount){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(TillDateBookings.this);
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View views = inflater.inflate(R.layout.payment_confirm_popup, null);

            builder.setView(views);
            final Button mNext = (Button) views.findViewById(R.id.submit_payment);
            final TextView mBookingInfo = (TextView) views.findViewById(R.id.booking_info);
            final EditText mAmount = (EditText) views.findViewById(R.id.advance_payment);
            final EditText mReAmount = (EditText) views.findViewById(R.id.advance_payment_confirm);
            final Spinner mPaymentMode = (Spinner) views.findViewById(R.id.payment_mode);
            final AlertDialog dialogs = builder.create();
            dialogs.show();
            dialogs.setCanceledOnTouchOutside(false);


            mBookingInfo.setText("First "+count+" bookings total amount is "+amount);



            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String enterAmount = mAmount.getText().toString();
                    String reenterAmount = mReAmount.getText().toString();
                    String paymentMode = mPaymentMode.getSelectedItem().toString();
                    if(enterAmount==null||enterAmount.isEmpty()){
                        mAmount.setError("Can't be Empty");
                    }else if(reenterAmount==null||reenterAmount.isEmpty()){
                        mReAmount.setError("Can't be Empty");
                    }else if(!reenterAmount.equals(enterAmount)){
                        mReAmount.setError("Amount not match");
                    }else{
                        double amountEnter = Double.parseDouble(enterAmount);
                        double amountRe = Double.parseDouble(reenterAmount);

                        if(amountEnter!=amountRe){
                            mReAmount.setError("Amount not match");
                        }else if(amountRe!=amount){
                            mReAmount.setError("Could not tally");
                        }else{

                            for(int i=0;i<count;i++){

                                final Payment payment = new Payment();
                                payment.setAmount(paymentBookings.get(i).getTotalAmount());
                                payment.setPaymentType(paymentMode);
                                payment.setBookingId(paymentBookings.get(i).getBookingId());
                                payment.setPaymentName("Zingo Payment");
                                SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                                payment.setPaymentDate(date.format(new Date()));
                                payment.setPaymentStatus("Payment Done By Zingo");

                               /* String paymentName = "Zingo Payment";
                                String paymentType = "Advance/Done";*/
                                addPayment(payment);
                            }
                            dialogs.dismiss();
                        }
                    }

                }
            });







        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addPayment(final Payment payment){
        final ProgressDialog dialog = new ProgressDialog(TillDateBookings.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                String auth_string = Util.getToken(TillDateBookings.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                PaymentApi paymentApi = Util.getClient().create(PaymentApi.class);

                Call<Payment> response = paymentApi.addPayment(auth_string,payment);
                response.enqueue(new Callback<Payment>() {
                    @Override
                    public void onResponse(Call<Payment> call, Response<Payment> response) {

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Payment serviceResponse = response.body();

                        if(response.code() == 200 && serviceResponse != null)
                        {

                            try{
                                Toast.makeText(TillDateBookings.this,"Payment added successfully",Toast.LENGTH_LONG).show();



                            }catch (Exception e){
                                e.printStackTrace();
                            }




                        }
                        else {

                            Toast.makeText(TillDateBookings.this,"Please try after some time",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Payment> call, Throwable t) {
                        System.out.println("onFailure");
                        Toast.makeText(TillDateBookings.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                });

            }
        });
    }

}
