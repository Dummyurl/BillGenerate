package app.zingo.com.billgenerate;

import android.*;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.zingo.com.billgenerate.Adapter.AutocompleteCustomArrayAdapter;
import app.zingo.com.billgenerate.Adapter.PaidStatusSpinnerAdapter;
import app.zingo.com.billgenerate.Adapter.PropertyAdapter;
import app.zingo.com.billgenerate.Adapter.RoomAdapter;
import app.zingo.com.billgenerate.Adapter.RoomCategorySpinnerAdapter;
import app.zingo.com.billgenerate.Model.*;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Utils.*;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBookingsActivity extends AppCompatActivity {

    Spinner mRoomCount, mPayment, mRate, mDesc,mOTA,
            mSourceType,mRoom;
    LinearLayout mDataLayout,mOtherLayout,mAddLayout,mCustomerLayout,mOtaService,mOtaGSTLay,mOTAComLay,mOTAPER,mRoomLay;
    EditText mLocation, mCity, mMobile, mRoomType,mProperty,
            mGuestCount, mTotal, mBooking, mZingo,mOtherProperty,
            mBookingID, mEmail,  mNet, mNights, mArr,mOtaFee,
            mRoomCharge,mExtraCharge,mHotelTaxes,mAdditional,mCustomerPay,mOTAPerce,
            mOTACommison,mOTAGST,mOtherbookingSource,room_category_spinner;
    TextView mBook, mCID, mCOD;
    CustomAutoCompleteView mGuest;
    Button mSave, mCalculate;
    String[] bookingSourceArray;
    int hotelId,travellerIid,roomId=0;
    ArrayList<HotelDetails> chainsList;
    Traveller dtos;
    Bookings1 bookings;
    double commisionAmt,commisionGST;
    HotelDetails list;
    ArrayList<Traveller> tlist;
    String[] bookingSourceTitleStringArray;

    //Databaases
    DataBaseHelper dbHelper;
    RoomDataBase room;
    PlanDataBase plan;
    BillDataBase bill;
    String zingoBookingId;

    boolean book = false;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;


    //pdf
    String property, email, ota, city, location, guest,
            mobile, bdate, cit, cot, rooms, roomNum, count, plans,
            payment, desc, total, booking, zingo, net, nights,
            arr, cits, cots,roomCharge,extraCharge,hoteltaxes,addtional,customer,otaFee,otaCommission,otaGST;
    double totals,otaAmt,zingoAmt,otaToHotel,addtionalChrg,payCustomer,
            customerToHotel,otaToHotelPay,otaFeeAmount,gstValue,otaComAmount,otaGstAmount;
    Document document;
    Paragraph paragraph;
    String propertyN;
    String[] headers = {"Description", "Details"};

    public static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, BaseColor.RED);

    public static Font catFonts = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    public static Font catFontw = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.WHITE);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font smallBolds = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.UNDERLINE, BaseColor.RED);


    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    File destination = new File(Environment.getExternalStorageDirectory(),
            System.currentTimeMillis() + ".pdf");
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".pdf");
    private String bookingID;
    private int STORAGE_PERMISSION_CODE = 23;
    private String csvFile;

    private ArrayList<RoomCategories> roomCategories;
    ArrayList<Rooms> roomsArrayList;
    ArrayList<Rooms> roomList;
    Rooms roomObject = null;
    int positionRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_update_bookings);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Update Booking");

            mProperty = (EditText) findViewById(R.id.bill_property_name);
            mAddLayout = (LinearLayout)findViewById(R.id.additional_layout);
            mOtaService = (LinearLayout)findViewById(R.id.ota_service);
            mCustomerLayout = (LinearLayout)findViewById(R.id.customer_pay_layout);
            mOTAComLay = (LinearLayout)findViewById(R.id.ota_commision_amt_layout);
            mOTAPER = (LinearLayout)findViewById(R.id.ota_commision_per_layout);
            mRoomLay = (LinearLayout)findViewById(R.id.room_layout);
            mOtaGSTLay = (LinearLayout)findViewById(R.id.ota_gst_layout);
            mDataLayout = (LinearLayout)findViewById(R.id.data_property_layout);
            mOtherLayout = (LinearLayout)findViewById(R.id.other_property_layout);
            mOTAPerce = (EditText) findViewById(R.id.bill_booking_com_percentage);
            mRoomType = (EditText) findViewById(R.id.bill_property_room);
            mOtaFee = (EditText) findViewById(R.id.bill_booking_com_service);
            mRate = (Spinner) findViewById(R.id.bill_property_rate);
            mRoomCount = (Spinner) findViewById(R.id.bill_property_room_num);
            mPayment = (Spinner) findViewById(R.id.bill_property_payment);
            mOtherProperty = (EditText) findViewById(R.id.bill_property_other);
            mLocation = (EditText) findViewById(R.id.bill_property_location);
            mEmail = (EditText) findViewById(R.id.bill_property_emal);
            mBookingID = (EditText) findViewById(R.id.bill_booking_id);
            mCity = (EditText) findViewById(R.id.bill_property_city);
            mGuest = (CustomAutoCompleteView) findViewById(R.id.bill_guest_name);
            mMobile = (EditText) findViewById(R.id.bill_guest_mobile);
            mGuestCount = (EditText) findViewById(R.id.bill_property_guest_num);
            mDesc = (Spinner) findViewById(R.id.bill_plan_inclusion);
            mRoomCharge = (EditText) findViewById(R.id.bill_room_charge);
            mExtraCharge = (EditText) findViewById(R.id.bill_extra_charge);
            mHotelTaxes = (EditText) findViewById(R.id.bill_hotel_taxes);
            mAdditional = (EditText) findViewById(R.id.bill_additional_charge);
            mCustomerPay = (EditText) findViewById(R.id.bill_customer_pay);
            mTotal = (EditText) findViewById(R.id.bill_property_amount);
            mBooking = (EditText) findViewById(R.id.bill_booking_com);
            mOTACommison = (EditText) findViewById(R.id.bill_booking_com_amount);
            mOTAGST = (EditText) findViewById(R.id.bill_booking_com_gst);
            mZingo = (EditText) findViewById(R.id.bill_zingo_com);
            mOTA = (Spinner) findViewById(R.id.bill_booking_ota);
            mSourceType = (Spinner) findViewById(R.id.bill_booking_source);
            mNet = (EditText) findViewById(R.id.bill_net_amount);
            mNights = (EditText) findViewById(R.id.bill_total_nights);
            mArr = (EditText) findViewById(R.id.bill_arr);
            mBook = (TextView) findViewById(R.id.bill_property_booking);
            mCID = (TextView) findViewById(R.id.bill_property_checkiin);
            mCOD = (TextView) findViewById(R.id.bill_property_checkout);
            mSave = (Button) findViewById(R.id.update);
            mCalculate = (Button) findViewById(R.id.bill_calculate);
            mOtherbookingSource = (EditText) findViewById(R.id.other_booking_source);
            room_category_spinner = (EditText) findViewById(R.id.room_category_spinner);
            mRoom = (Spinner) findViewById(R.id.room_spinner);

            bookingSourceTitleStringArray = getResources().getStringArray(R.array.booking_source_title);

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                bookings = (Bookings1) bundle.getSerializable("BOOKINGS");
            }
            
            if(bookings!=null){
                
                setFields(bookings);
                
            }
            



            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(UpdateBookingsActivity.this,bookingSourceTitleStringArray);
            mSourceType.setAdapter(spinneradapter);

            mSourceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if(bookingSourceTitleStringArray[position].equals("OTA"))
                        {
                            String[] bookingSourceArray = getResources().getStringArray(R.array.OTA_items);

                            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(UpdateBookingsActivity.this,bookingSourceArray);
                            mOTA.setAdapter(spinneradapter);
                        }
                        else if(bookingSourceTitleStringArray[position].equals("B2B"))
                        {
                            String[] bookingSourceArray = getResources().getStringArray(R.array.B2B_items);

                            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(UpdateBookingsActivity.this,bookingSourceArray);
                            mOTA.setAdapter(spinneradapter);
                        }
                        else if(bookingSourceTitleStringArray[position].equals("Offline"))
                        {
                            String[] bookingSourceArray = getResources().getStringArray(R.array.Offline_items);

                            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(UpdateBookingsActivity.this,bookingSourceArray);
                            mOTA.setAdapter(spinneradapter);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //bookingSourceArray = getResources().getStringArray(R.array.OTA_items);
            /*PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(UpdateBookingsActivity.this,bookingSourceArray);
            mOTA.setAdapter(spinneradapter);*/

            mOTA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if(mOTA.getSelectedItem().toString().equals("Other"))
                        {
                            mOtherbookingSource.setVisibility(View.VISIBLE);
                        }else{
                            mOtherbookingSource.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            bill = new BillDataBase(this);

            // getHotels();

            mBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openDatePicker(mBook);
                }
            });

            mCID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openDatePicker(mCID);
                }
            });

            mCOD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openDatePicker(mCOD);
                }
            });

            mGuest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mGuest.setText(tlist.get(position).getFirstName());
                    travellerIid = tlist.get(position).getTravellerId();
                }
            });

            mGuest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        String mobi = mMobile.getText().toString();
                        if(mobi != null && !mobi.isEmpty() && mobi.length() >= 10)
                        {
                            getTravelerByPhone(mobi);
                        }
                        else
                        {
                            //mMobile.requestFocus();
                            mGuest.setText("");
                        }
                    } else {

                    }
                }
            });

            mOTAPerce.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    try {
                        String room = mRoomCharge.getText().toString();
                        String pere = mOTAPerce.getText().toString();

                        if(room!=null && !room.isEmpty()){
                            double roomCharge = Double.parseDouble(room);
                            double percentage = Double.parseDouble(pere);
                            double value = roomCharge * percentage;
                            double amount = value/100;
                            mBooking.setText(""+amount);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mRoomCharge.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    priceCalculation();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mExtraCharge.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    priceCalculation();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mHotelTaxes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    priceCalculation();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mOTACommison.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    otaCalculation();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mOTAGST.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    otaCalculation();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mOTA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(mOTA.getSelectedItem().toString().equalsIgnoreCase("MAKEMY TRIP")){
                        mOtaService.setVisibility(View.VISIBLE);
                        mOTAComLay.setVisibility(View.VISIBLE);
                        mOtaGSTLay.setVisibility(View.VISIBLE);
                        mCustomerLayout.setVisibility(View.VISIBLE);
                        mOTAPER.setVisibility(View.GONE);

                    }else if(mOTA.getSelectedItem().toString().equalsIgnoreCase("BOOKING.COM")||mOTA.getSelectedItem().toString().equalsIgnoreCase("EXPEDIA")){
                        mOtaService.setVisibility(View.GONE);
                        mOTAComLay.setVisibility(View.GONE);
                        mOtaGSTLay.setVisibility(View.GONE);
                        mCustomerLayout.setVisibility(View.GONE);

                        if(mOTA.getSelectedItem().toString().equalsIgnoreCase("EXPEDIA")){
                            mOTAPER.setVisibility(View.VISIBLE);
                        }else{
                            mOTAPER.setVisibility(View.GONE);
                        }

                    } else{
                        mOtaService.setVisibility(View.GONE);
                        mOTAComLay.setVisibility(View.VISIBLE);
                        mOtaGSTLay.setVisibility(View.VISIBLE);
                        mCustomerLayout.setVisibility(View.VISIBLE);
                        mOTAPER.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                        mCustomerLayout.setVisibility(View.VISIBLE);
                        mAddLayout.setVisibility(View.VISIBLE);
                        mOtaService.setVisibility(View.VISIBLE);

                    }else{
                        mCustomerLayout.setVisibility(View.GONE);
                        mAddLayout.setVisibility(View.GONE);
                        mOtaService.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //Database call
            dbHelper = new DataBaseHelper(this);
            room = new RoomDataBase(this);
            plan = new PlanDataBase(this);


            fn_permission();


            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(book){
                        boolean fileCreated = createPdf();
                        if(fileCreated){
                            onShareClick();
                        }else{
                            Toast.makeText(UpdateBookingsActivity.this, "File not generate but booking happened", Toast.LENGTH_SHORT).show();
                            createPdf();
                        }

                    }else{

                            validate();

                        //validate();
                    }

                }
            });

            mCalculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    cit = mCID.getText().toString();
                    cot = mCOD.getText().toString();
                    total = mTotal.getText().toString();
                    zingo = mZingo.getText().toString();
                    booking = mBooking.getText().toString();
                    roomCharge = mRoomCharge.getText().toString();
                    extraCharge = mExtraCharge.getText().toString();
                    hoteltaxes = mHotelTaxes.getText().toString();
                    addtional = mAdditional.getText().toString();
                    customer = mCustomerPay.getText().toString();
                    otaFee = mOtaFee.getText().toString();
                    otaCommission = mOTACommison.getText().toString();
                    otaGST = mOTAGST.getText().toString();

                    String source = mOTA.getSelectedItem().toString();

                    if (booking == null || booking.isEmpty()) {
                        //Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mBooking.setError("Should not be Empty");
                        mBooking.requestFocus();

                    } else if (zingo == null || zingo.isEmpty()) {
                        //Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mZingo.setError("Should not be Empty");
                        mZingo.requestFocus();

                    } else if (cit == null || cit.isEmpty()) {
                        //Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mCID.setError("Should not be Empty");
                        mCID.requestFocus();

                    } else if (cot == null || cot.isEmpty()) {
                        // Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mCOD.setError("Should not be Empty");
                        mCOD.requestFocus();

                    } else if (total == null || total.isEmpty()) {
                        // Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mTotal.setError("Should not be Empty");
                        mTotal.requestFocus();

                    } else if (roomCharge == null || roomCharge.isEmpty()) {
                        Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();


                    } else if (extraCharge == null || extraCharge.isEmpty()) {
                        Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();

                    } else if (hoteltaxes == null || hoteltaxes.isEmpty()) {
                        Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();

                    } else {

                        //Date Calculation
                        String from = cits + " 00:00:00";
                        String to = cots + " 00:00:00";

                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                        Date d1 = null;
                        Date d2 = null;
                        long diffDays = 0;
                        try {
                            d1 = format.parse(from);
                            d2 = format.parse(to);
                            long diff = d2.getTime() - d1.getTime();
                            diffDays = diff / (24 * 60 * 60 * 1000);
                            mNights.setText(String.valueOf(diffDays));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(otaCommission==null||otaCommission.isEmpty()){
                            otaComAmount = 0;
                        }else{
                            otaComAmount = Double.parseDouble(otaCommission);
                        }

                        if(otaGST==null||otaGST.isEmpty()){
                            otaGstAmount = 0;
                        }else{
                            otaGstAmount = Double.parseDouble(otaGST);
                        }

                        //Net Amount

                        DecimalFormat df = new DecimalFormat("#,###.##");
                        totals = Double.parseDouble(total);
                        otaAmt = Double.parseDouble(booking);
                        zingoAmt = Double.parseDouble(zingo);
                        if(addtional==null||addtional.isEmpty()){
                            addtionalChrg = 0;
                        }else{
                            addtionalChrg = Double.parseDouble(mAdditional.getText().toString());
                        }

                        if(otaFee==null||otaFee.isEmpty()){
                            otaFeeAmount = 0;
                        }else{
                            otaFeeAmount = Double.parseDouble(otaFee);
                        }

                        if(customer==null||customer.isEmpty()){
                            payCustomer = 0;
                        }else{
                            payCustomer = Double.parseDouble(mCustomerPay.getText().toString());
                        }

                        gstValue = Double.parseDouble(hoteltaxes);

                        double rooms = Double.parseDouble(mRoomCount.getSelectedItem().toString());
                        if (diffDays != 0) {
                            double arrAmt = totals / diffDays;
                            double arrRamt = arrAmt / rooms;
                            mArr.setText("" + df.format(arrRamt));
                        } else {
                            //double arrAmt = total/diffDays;
                            double arrRamt = totals / rooms;
                            mArr.setText("" + df.format(arrRamt));
                        }

                        commisionAmt = otaAmt + zingoAmt+otaFeeAmount;
                        double gst = commisionAmt*18;
                        commisionGST = gst/100;
                        otaToHotel = totals-otaAmt;


                        //hotelToZingo = otaToHotel-
                        double netAmt = totals - (commisionAmt);
                        mNet.setText("" + df.format(netAmt));

                        if(source!=null&&source.equalsIgnoreCase("MAKEMY TRIP")){
                            otaToHotelPay = (payCustomer) - (otaAmt+otaFeeAmount);
                            customerToHotel = (totals+addtionalChrg+otaFeeAmount)-payCustomer;
                        }else{
                            otaToHotelPay = payCustomer - otaAmt;
                            customerToHotel = (totals+addtionalChrg)-payCustomer;
                        }

                    }

                }
            });



    /*        room_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    rooms = roomCategories.get(i).getCategoryName();
                    mRoomType.setText(rooms);
                    roomList = new ArrayList<>();
                    System.out.println("Before Room SIze=="+roomsArrayList.size());
                    if(roomsArrayList!=null&&roomsArrayList.size()!=0){
                        for(int j=0;j<roomsArrayList.size();j++){

                            *//*System.out.println("Room Category id=="+roomCategories.get(i).getRoomCategoryId());
                            System.out.println("Rooms Category id=="+roomsArrayList.get(j).getRoomNo());*//*
                            if((roomCategories.get(i).getRoomCategoryId()==roomsArrayList.get(j).getRoomCategoryId())&&roomsArrayList.get(j).getStatus().equalsIgnoreCase("Available")){
                                roomList.add(roomsArrayList.get(j));
                            }
                        }

                        if(roomList!=null&&roomList.size()!=0){
                            // roomsArrayList = new ArrayList<>();
                            System.out.println("After Room SIze=="+roomsArrayList.size());
                            mRoomLay.setVisibility(View.VISIBLE);

                            //loadRoomSpinner(roomList);
                        }else{
                            roomId =0;
                            Toast.makeText(UpdateBookingsActivity.this, "No available rooms in this hotel", Toast.LENGTH_SHORT).show();
                            mRoomLay.setVisibility(View.GONE);
                        }
                    }else{
                        roomId =0;
                        Toast.makeText(UpdateBookingsActivity.this, "There is no rooms in this hotel", Toast.LENGTH_SHORT).show();
                        mRoomLay.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
*/
            mRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    roomId = roomList.get(i).getRoomId();
                    positionRoom = i;
                    roomObject = roomList.get(i);
                    System.out.println("Room Id Selected=="+roomId);
                    System.out.println("Room Id Selected Object=="+roomObject.getRoomNo());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void validate() {

//        String properties = mProperty.getSelectedItem().toString();
        try{
            //String data = mDataBase.getSelectedItem().toString();


            rooms = room_category_spinner.getText().toString();
            plans = mRate.getSelectedItem().toString();
            roomNum = mRoomCount.getSelectedItem().toString();
            payment = mPayment.getSelectedItem().toString();
            bookingID = mBookingID.getText().toString();
            property = mProperty.getText().toString();
            location = mLocation.getText().toString();
            city = mCity.getText().toString();
            guest = mGuest.getText().toString();
            mobile = mMobile.getText().toString();
            email = mEmail.getText().toString();
            count = mGuestCount.getText().toString();
            desc = mDesc.getSelectedItem().toString();
            roomCharge = mRoomCharge.getText().toString();
            extraCharge = mExtraCharge.getText().toString();
            hoteltaxes = mHotelTaxes.getText().toString();
            addtional = mAdditional.getText().toString();
            customer = mCustomerPay.getText().toString();
            total = mTotal.getText().toString();
            booking = mBooking.getText().toString();
            zingo = mZingo.getText().toString();
            nights = mNights.getText().toString();
            net = mNet.getText().toString();
            arr = mArr.getText().toString();
            bdate = mBook.getText().toString();
            cit = mCID.getText().toString();
            cot = mCOD.getText().toString();
            ota = mOTA.getSelectedItem().toString();

            System.out.println("Print" + String.valueOf(path));

            if (location == null || location.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (city == null || city.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (guest == null || guest.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (count == null || count.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (desc == null || desc.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (total == null || total.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (booking == null || booking.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (zingo == null || zingo.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (bdate == null || bdate.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (cit == null || cit.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (cot == null || cot.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (email == null || email.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (ota == null || ota.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (net == null || net.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please click calculate button", Toast.LENGTH_SHORT).show();
            } else if (nights == null || nights.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please click calculate button", Toast.LENGTH_SHORT).show();
            } else if (arr == null || arr.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please click calculate button", Toast.LENGTH_SHORT).show();
            }else if (roomCharge == null || roomCharge.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (extraCharge == null || extraCharge.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            }else if (hoteltaxes == null || hoteltaxes.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else {



                System.out.println("Property name==" + property);





                    if(travellerIid==0){
                        addTraveler();
                    }else if(tlist != null && !isexist(tlist)){
                        addTraveler();

                    }
                    else
                    {
                        setData(travellerIid);
                    }
                }





        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void setData(int tid){

        try{
            Bookings1 updateBooking = bookings;

            updateBooking.setCheckInDate(cits);
            updateBooking.setOptCheckInDate(cits);//----------------change in case of customer app booking-----------
            updateBooking.setCheckOutDate(cots);
            updateBooking.setOptCheckOutDate(cots);
            updateBooking.setTravellerId(travellerIid);
            updateBooking.setBookingSourceType(mSourceType.getSelectedItem().toString());
            updateBooking.setNoOfAdults(Integer.parseInt(count));
            updateBooking.setBookingSource(mOTA.getSelectedItem().toString());
            updateBooking.setOTACommissionGSTAmount(otaGstAmount);
            updateBooking.setOTACommissionAmount(otaComAmount);
            updateBooking.setOTATotalCommissionAmount(otaAmt);
            updateBooking.setOTAServiceFees(otaFeeAmount);

            double roomCount = Double.parseDouble(mRoomCount.getSelectedItem().toString());


            if(otaToHotelPay<0){
                updateBooking.setOTAToPayHotel(0);
                updateBooking.setHotelToPayOTA(Math.abs(otaToHotelPay));
            }else{
                updateBooking.setOTAToPayHotel(Math.abs(otaToHotelPay));
                updateBooking.setHotelToPayOTA(0);
            }
            updateBooking.setZingoCommision(zingoAmt);
            updateBooking.setCustomerPaymentAtOTA(payCustomer);
            updateBooking.setAdditionalCharges(addtionalChrg);
            updateBooking.setOTABookingID(bookingID);
            updateBooking.setGstAmount((int)gstValue);
            DecimalFormat df = new DecimalFormat("##.##");
            updateBooking.setCommisionGSTAmount(commisionGST);
            if(mRoomCharge.getText().toString()==null||mRoomCharge.getText().toString().isEmpty()){
                updateBooking.setSellRate(0);
            }else{

                double sellRate = Double.parseDouble(mRoomCharge.getText().toString());
                updateBooking.setSellRate((int) sellRate);
            }

            if(mExtraCharge.getText().toString()==null||mExtraCharge.getText().toString().isEmpty()){
                updateBooking.setExtraCharges(0);
            }else{

                double sellRate = Double.parseDouble(mExtraCharge.getText().toString());
                updateBooking.setExtraCharges((int) sellRate);
            }
            updateBooking.setCommissionAmount((int) commisionAmt);
            updateBooking.setDurationOfStay(Integer.parseInt(nights));
            updateBooking.setTotalAmount((int) Double.parseDouble(total));
            if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                updateBooking.setBalanceAmount((int) Double.parseDouble(total));
                updateBooking.setHotelToZingo(zingoAmt);
            }else{
                updateBooking.setBalanceAmount(0);
                updateBooking.setZingoToHotel(zingoAmt);
            }

            updateBooking.setBookingPlan(mRate.getSelectedItem().toString());
            updateBooking.setRoomCategory(room_category_spinner.getText().toString());

            if(mRoomCount.getSelectedItem().toString().isEmpty()){
                updateBooking.setNoOfRooms(1);
            }else{
                updateBooking.setNoOfRooms(Integer.parseInt(mRoomCount.getSelectedItem().toString()));
            }


            //Current time and date for booking

            long date = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");

            if(mBook.getText().toString()==null||mBook.getText().toString().isEmpty()){
                String bookingDate = sdf.format(date);
                System.out.println("Booking Date==="+bookingDate);
                updateBooking.setBookingDate(bookingDate);
            }else{

                Date date1 = sdfs.parse(mBook.getText().toString());
                updateBooking.setBookingDate(sdf.format(date1));
            }

       /* String bookingDate = sdf.format(date);
        System.out.println("Booking Date==="+bookingDate);
        bookings.setBookingDate(bookingDate);*/

            SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
            Date d = new Date();
            String time = sdft.format(d);

            updateBooking.setBookingTime(time);

            if(book){
                boolean fileCreated = createPdf();
                if(fileCreated){
                    onShareClick();
                }else{
                    Toast.makeText(this, "File not generate but booking happened", Toast.LENGTH_SHORT).show();
                    createPdf();
                }

            }else{
                updateRoomBooking(updateBooking);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }



    private boolean createPdf() {

        document = null;

        try {

            File sd = Environment.getExternalStorageDirectory();
            csvFile = System.currentTimeMillis() + ".pdf";
            File directory = new File(sd.getAbsolutePath() + "/BillGenerate/Pdf");
            //create directory if not exist
            if (!directory.exists() && !directory.isDirectory()) {
                directory.mkdirs();
            }
            File file = new File(directory, csvFile);
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addParagraph();
            return true;
            //document.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void addMetaData(Document document) {

        document.addTitle("Title");
        document.addSubject("Subject");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public void addTitles() {

        try {
            paragraph = new Paragraph();

            //paragraph.setSpacingAfter(30);
            addParagraph();

            document.add(paragraph);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addChild(Paragraph para) {
        para.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(para);

    }

    public void addFooter(Paragraph para) {
        para.setAlignment(Element.ALIGN_BOTTOM);
        paragraph.add(para);

    }


    public void addParagraph() {
        try {
            paragraph = new Paragraph();

            String text = "Dear Hotel Partner,\n" + "We Thank you for your continued support in ensuring the highest level of service Standards. Please find the reservation for you. ";
            String important = "IMPORTANT NOTE:";
            String footer =
                    "                                                                         ZingoHotels.com\n" +
                            "#88, 1st Floor, Koramangala Industrial Layout 5th Block, Near JNC College, Bangalore-560095\n" +
                            "                                                                        www.Zingohotels.com";
            String note = "Under no circumstances must you charge guest for services listed on this voucher!\n" + "Only payments for extra services are to be collected from clients\n" + "Hotel shall issue invoice to the customer/guests, as and when required by the customer/Guest";
            addChild(new Paragraph("                                   Lucida Hospitality Pvt Ltd", catFont));
            //addImage(paragraph);
            addChild(new Paragraph("                        Mob: +91- 7065 651 651 | Email- hello@zingohotels.com", subFont));
            // addChild(new Paragraph("Email- hello@zingohotels.com",subFont));


            //paragraph = new Paragraph(text,smallBold);
            addEmptyLine(paragraph, 2);
            paragraph.add(new Paragraph("Booking ID: " + bookingID, smallBolds));
            paragraph.add(new Paragraph("Booking Source: " + ota, smallBolds));
            if(zingoBookingId!=null&&!zingoBookingId.isEmpty()){
                paragraph.add(new Paragraph("Zingo Booking ID: " + zingoBookingId, smallBolds));
            }

            addEmptyLine(paragraph, 1);
            paragraph.add(new Paragraph(text, smallBold));
            addEmptyLine(paragraph, 1);
            createTables(paragraph);
            addEmptyLine(paragraph, 1);
            paragraph.add(new Paragraph("Payment Breakup", smallBold));
            addEmptyLine(paragraph, 1);
            if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                createTablesPaymentHotel(paragraph);
            }else{
                createTablesPayment(paragraph);
            }

            addEmptyLine(paragraph, 2);
            addChild(new Paragraph(important, subFont));
            addChild(new Paragraph(note, small));
            addEmptyLine(paragraph, 2);
            addFooter(new Paragraph(footer, catFonts));

            document.add(paragraph);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createTables(Paragraph para)
            throws BadElementException {
        PdfPTable table = new PdfPTable(2);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Description", catFontw));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Details", catFontw));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);
        table.setHeaderRows(1);

        /*c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);*/

        table.addCell("BOOKING ID");
        table.addCell(bookingID);
        table.addCell("Booking Source");
        table.addCell(ota);
        table.addCell("HOTEL NAME");
        table.addCell(property);
        table.addCell("LOCATION");
        table.addCell(location);
        table.addCell("CITY");
        table.addCell(city);
        table.addCell("GUEST NAME");
        table.addCell(guest);
        table.addCell("GUEST MOBILE");
        table.addCell(mobile);
        table.addCell("BOOKING DATE");
        table.addCell(bdate);
        table.addCell("CHECK-IN DATE");
        table.addCell(cit);
        table.addCell("CHECK-OUT DATE");
        table.addCell(cot);
        table.addCell("NUMBER OF NIGHTS");
        table.addCell(nights);
        table.addCell("ROOM TYPE");
        table.addCell(rooms);
        table.addCell("TOTAL ROOM(S)");
        table.addCell(roomNum);
        table.addCell("TOTAL GUEST(S)");
        table.addCell(count);
        table.addCell("RATE PLAN");
        table.addCell(plans);
        table.addCell("PAYMENT MODE");
        table.addCell(payment);
        table.addCell("INCLUSION");
        table.addCell(desc);
      /*  table.addCell("TOTAL AMOUNT");
        table.addCell("INR " + total);
        table.addCell("OTA COMMISSION");
        table.addCell("INR " + booking);
        table.addCell("ZINGOHOTELS.COM COMMISION");
        table.addCell("INR " + zingo);
        table.addCell("NET AMOUNT");
        table.addCell("INR " + net);
        table.addCell("ARR");
        table.addCell("INR " + arr);*/

        para.add(table);

    }

    private void createTablesPayment(Paragraph para)
            throws BadElementException {
        PdfPTable table = new PdfPTable(2);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Description", catFontw));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount", catFontw));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);
        table.setHeaderRows(1);

        /*c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);*/

        DecimalFormat dfs = new DecimalFormat("#.##");

        table.addCell("(i) ROOM CHARGES\n"+roomNum+" Room, "+nights+" Night");
        table.addCell("INR " + roomCharge);
        table.addCell("(ii) EXTRA CHARGES");
        table.addCell("INR " + extraCharge);
        table.addCell("(iii) HOTEL TAXES");
        table.addCell("INR " + hoteltaxes);
        table.addCell("(A) HOTEL GROSS CHARGES (i+ii+iii)");
        table.addCell("INR " + total);
        table.addCell("OTA Commission");
        table.addCell("INR " + otaComAmount);
        table.addCell("OTA GST @ 18 %\n(Incl IGST/CGST/SGST)");
        table.addCell("INR " + otaGstAmount);
        table.addCell("(B) OTA COMMISSION(Incl GST)");
        table.addCell("INR " + booking);
        table.addCell("(C) ZINGOHOTELS.COM COMMISION");
        table.addCell("INR " + zingo);
        table.addCell("OTA to Pay Hotel(A-B)");
        table.addCell("INR " + dfs.format(otaToHotel));
        table.addCell("Hotel to Pay Zingo(C)");
        table.addCell("INR " + zingo);
        table.addCell("NET AMOUNT (A-B-C)");
        table.addCell("INR " + net);
        table.addCell("ARR");
        table.addCell("INR " + arr);

        para.add(table);

    }

    private void createTablesPaymentHotel(Paragraph para)
            throws BadElementException {
        PdfPTable table = new PdfPTable(2);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        DecimalFormat dfs = new DecimalFormat("#.##");


        PdfPCell c1 = new PdfPCell(new Phrase("Description", catFontw));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount", catFontw));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);
        table.setHeaderRows(1);

        /*c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);*/

        table.addCell("(i) ROOM CHARGES\n"+roomNum+" Room, "+nights+" Night");
        table.addCell("INR " + roomCharge);
        table.addCell("(ii) EXTRA CHARGES");
        table.addCell("INR " + extraCharge);
        table.addCell("(iii) HOTEL TAXES");
        table.addCell("INR " + hoteltaxes);
        table.addCell("(A) HOTEL GROSS CHARGES (i+ii+iii)");
        table.addCell("INR " + total);
        if(!mOTA.getSelectedItem().toString().equalsIgnoreCase("BOOKING.COM")&&!mOTA.getSelectedItem().toString().equalsIgnoreCase("EXPEDIA")){
            table.addCell("OTA Commission");
            table.addCell("INR " + otaComAmount);
            table.addCell("OTA GST @ 18 %\n(Incl IGST/CGST/SGST)");
            table.addCell("INR " + otaGstAmount);
        }

        table.addCell("(B) OTA COMMISSION(Incl GST)");
        table.addCell("INR " + booking);
        table.addCell("(C) ZINGOHOTELS.COM COMMISION");
        table.addCell("INR " + zingo);
        table.addCell("(D) Additional Charges");
        table.addCell("INR " + addtional);
        if(!mOTA.getSelectedItem().toString().equalsIgnoreCase("BOOKING.COM")&&!mOTA.getSelectedItem().toString().equalsIgnoreCase("EXPEDIA")){
            table.addCell("(E) Customer payment at OTA\n Customer Pre-Payment includes discount coupons offered by OTA or payments made through Wallet.");
            table.addCell("INR " + customer);
        }


       /* else{
            mOtaService.setVisibility(View.GONE);
            mOTACommison.setVisibility(View.VISIBLE);
            mOTAGST.setVisibility(View.VISIBLE);
            mCustomerPay.setVisibility(View.VISIBLE);
        }*/

        if(mOTA.getSelectedItem().toString().equalsIgnoreCase("MAKEMY TRIP")){
            table.addCell("(F) MMT SERVICE FEE\n (Including GST)");
            table.addCell("INR " + otaFeeAmount);
            if(otaToHotelPay<0){
                table.addCell("Amount To Be Paid By HOTEL to OTA (E - (B+F))");
                table.addCell("INR " + dfs.format(Math.abs(otaToHotelPay)));
            }else{
                table.addCell("Amount To Be Paid By OTA (E - (B+F))");
                table.addCell("INR " + dfs.format(otaToHotelPay));
            }
        }else{
            if(otaToHotelPay<0){
                if(!mOTA.getSelectedItem().toString().equalsIgnoreCase("BOOKING.COM")&& !mOTA.getSelectedItem().toString().equalsIgnoreCase("EXPEDIA")){
                    table.addCell("Amount To Be Paid By  HOTEL to OTA  (E - B)");
                    table.addCell("INR " + dfs.format(Math.abs(otaToHotelPay)));
                }else{
                    table.addCell("Amount To Be Paid By  HOTEL to OTA  (B)");
                    table.addCell("INR " + dfs.format(Math.abs(otaToHotelPay)));
                }

            }else{
                table.addCell("Amount To Be Paid By OTA (E - B)");
                table.addCell("INR " + dfs.format(otaToHotelPay));
            }
        }


        if(mOTA.getSelectedItem().toString().equalsIgnoreCase("MAKEMY TRIP")){
            table.addCell("Customer To Pay At Hotel (A + D + F - E)");
            table.addCell("INR " + dfs.format(customerToHotel));
        }else{
            if(mOTA.getSelectedItem().toString().equalsIgnoreCase("BOOKING.COM")||mOTA.getSelectedItem().toString().equalsIgnoreCase("EXPEDIA")){
                table.addCell("Customer To Pay At Hotel (A + D)");
                table.addCell("INR " + dfs.format(customerToHotel));
            }else{
                table.addCell("Customer To Pay At Hotel (A + D - E)");
                table.addCell("INR " + dfs.format(customerToHotel));

            }

        }





        table.addCell("Hotel to Pay Zingo(C)");
        table.addCell("INR " + zingo);
        table.addCell("NET AMOUNT (A-B-C)");
        table.addCell("INR " + net);
        table.addCell("ARR");
        table.addCell("INR " + arr);

        para.add(table);

    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(UpdateBookingsActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(UpdateBookingsActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(UpdateBookingsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(UpdateBookingsActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }


    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                Log.v("Hi", "Permission is granted");
                return true;
            } else {

                Log.v("hi", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("hi", "Permission is granted");
            return true;
        }
    }



    public void openDatePicker(final TextView tv) {
        // Get Current Date
        try{
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            //launch datepicker modal
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            String from, to;
                            Log.d("Date", "DATE SELECTED " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);


                            String date1 = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;

                            //String date2 = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");


                            if (tv.equals(mBook)) {

                                try {
                                    Date fdate = simpleDateFormat.parse(date1);
                                    Date fd = newDate.getTime();

                                    //cits = simpleDateFormat.format(fdate);
                                    from = sdf.format(fd);

                                    System.out.println("To = " + from);
                                    tv.setText(from);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //


                            } else if (tv.equals(mCID)) {
                                //to = date1;
                                try {
                                    Date tdate = simpleDateFormat.parse(date1);
                                    Date fd = newDate.getTime();
                                    cits = simpleDateFormat.format(tdate);
                                    to = sdf.format(fd);
                                    System.out.println("To = " + to);
                                    tv.setText(to);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (tv.equals(mCOD)) {
                                //to = date1;
                                try {
                                    Date tdate = simpleDateFormat.parse(date1);
                                    Date fd = newDate.getTime();
                                    cots = simpleDateFormat.format(tdate);
                                    to = sdf.format(fd);
                                    System.out.println("To = " + to);
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

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getHotelName(final int id) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi apiService =
                        app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
                String authenticationString = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);
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




                                    mProperty.setText(list.getHotelDisplayName());
                                    mLocation.setText(list.getLocalty());
                                    mCity.setText(list.getCity());
                                    getContactByHotelId(id);




                            }

//


                        } else {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(UpdateBookingsActivity.this, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
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



    public void getContactByHotelId(final int id) {

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);
                LoginApi operations = app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
                Call<ArrayList<ContactDetails>> getContactResponse = operations.getContactByHotelId(authenticationString, id);

                getContactResponse.enqueue(new Callback<ArrayList<ContactDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ContactDetails>> call, Response<ArrayList<ContactDetails>> response) {
                        System.out.println("Code  = " + response.code());

                        try{
                            ArrayList<ContactDetails> contactResponse = response.body();

                            if (response.code() == 200 && contactResponse != null && contactResponse.size() != 0) {
                                final ContactDetails contactInfo = contactResponse.get(contactResponse.size() - 1);


                                mEmail.setText(contactInfo.getEmailList());


                            } else {

                                mEmail.setText("");
                            }

                        }catch (Exception e){

                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(Call<ArrayList<ContactDetails>> call, Throwable t) {
                        System.out.println("Contact failed");
                        Toast.makeText(UpdateBookingsActivity.this, "Failed Due to: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void updateRoomBooking(final Bookings1 bookings) {

        final ProgressDialog progressDialog = new ProgressDialog(UpdateBookingsActivity.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi apiService =
                        app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
                Call<String> call = apiService.updateBookingStatus(auth_string, bookings.getBookingId(),bookings);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        int statusCode = response.code();
                        try{
                            if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                                if (progressDialog != null)
                                    progressDialog.dismiss();


                                    book = true;
                                    zingoBookingId = ""+bookings.getBookingId();

                                    //Storing ota booking id for check of booking created or not
                                    PreferenceHandler.getInstance(UpdateBookingsActivity.this).setBookingRefNumber(bookings.getOTABookingID());

                                    Toast.makeText(UpdateBookingsActivity.this, "Booking done successfully", Toast.LENGTH_SHORT).show();
                                    //sendEmailattache();
                                    FireBaseModel fm = new FireBaseModel();
                                    fm.setSenderId("415720091200");
                                    fm.setServerId("AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M");
                                    fm.setHotelId(hotelId);
                                    fm.setTitle("Modified booking");
                                    fm.setMessage("Dear "+property+" got Modified booking for "+mGuest.getText().toString() +"\nBooking Number:"+bookings.getBookingNumber());
                                    //registerTokenInDB(fm);
                                    sendNotification(fm);
                                    double roomCount = Double.parseDouble(mRoomCount.getSelectedItem().toString());



                            } else {
                                if (progressDialog != null)
                                    progressDialog.dismiss();

                                Toast.makeText(UpdateBookingsActivity.this, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                        Toast.makeText(UpdateBookingsActivity.this, " failed due to : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });
    }

    public void sendNotification(final FireBaseModel fireBaseModel) {


        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);
                LoginApi apiService =
                        app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.send(auth_string, fireBaseModel)/*getString()*/;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, retrofit2.Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        try{
                            if (statusCode == 200) {

                                ArrayList<String> list = response.body();

                                Toast.makeText(UpdateBookingsActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                                //sendEmailattache();
                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(fireBaseModel.getTitle());
                                nf.setNotificationFor(fireBaseModel.getMessage());
                                nf.setHotelId(fireBaseModel.getHotelId());
                                savenotification(nf);



                            } else {

                                Toast.makeText(UpdateBookingsActivity.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
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


    private void getTravelerByPhone(final String dto){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait we are fetching traveller details..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi apiService =
                        app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
                Call<ArrayList<Traveller>> call = apiService.fetchTravelerByPhone(auth_string,dto);

                call.enqueue(new Callback<ArrayList<Traveller>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Traveller>> call, Response<ArrayList<Traveller>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();


                            if (response.body().size()!=0) {
                                tlist = response.body();


                                AutocompleteCustomArrayAdapter autocompleteCustomArrayAdapter =
                                        new AutocompleteCustomArrayAdapter(UpdateBookingsActivity.this,R.layout.hotels_row,tlist,"UpdateBookingsActivity");
                                mGuest.setThreshold(1);
                                mGuest.setAdapter(autocompleteCustomArrayAdapter);

                            }else{
                                //travellerIid = 0;
                                //addTraveler();
                                mGuest.setText("");

                            }


                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(UpdateBookingsActivity.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Traveller>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }


    private void getTravelerById(final int id){



        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi apiService =
                        app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
                Call<Traveller> call = apiService.getTravellerDetails(auth_string,id);

                call.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {



                            if (response.body()!=null) {


                                if(response.body().getFirstName()==null||response.body().getFirstName().isEmpty()){
                                    mGuest.setText("");
                                }else {
                                    mGuest.setText(""+response.body().getFirstName());
                                }
                                if(response.body().getPhoneNumber()==null||response.body().getPhoneNumber().isEmpty()){
                                    mMobile.setText("");
                                }else {
                                    mMobile.setText(""+response.body().getPhoneNumber());
                                }

                            }else{
                                //travellerIid = 0;
                                //addTraveler();
                                mGuest.setText("");

                            }


                        }else {

                            Toast.makeText(UpdateBookingsActivity.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        // Log error here since request failed

                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private void addTraveler(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Traveller dto = new Traveller();
        dto.setFirstName(guest.trim());
        if (mobile == null || mobile.isEmpty()) {
            //dto.setPhoneNumber("");
        }else{
            dto.setPhoneNumber(mobile);
        }

        dto.setUserRoleId(1);

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi apiService =
                        app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
                Call<Traveller> call = apiService.addTraveler(auth_string,dto);

                call.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        try{
                            if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                                if (progressDialog!=null)
                                    progressDialog.dismiss();

                                Traveller dto = response.body();
                                System.out.println("Response Traveller==="+response.body());

                                if (dto != null) {
                                    travellerIid  = dto.getTravellerId();
                                    setData(dto.getTravellerId());

                                }
//


                            }else {
                                if (progressDialog!=null)
                                    progressDialog.dismiss();
                                Toast.makeText(UpdateBookingsActivity.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private void updateTraveller(final int travellerIid){
        final Traveller dto = dtos;

        dto.setTravellerId(travellerIid);
        dto.setPhoneNumber(mobile);
        dto.setFirstName(guest);
        dto.setUserRoleId(1);

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);
                LoginApi apiService =
                        app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
                Call<Traveller> call = apiService.updateTravellerDetails(auth_string,travellerIid,dto);

                call.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        try{
                            if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                                if (progressDialog!=null)
                                    progressDialog.dismiss();

                                Traveller dto = response.body();
                                System.out.println("Response Traveller==="+response.body());

                                if (dto != null) {

                                    setData(dto.getTravellerId());
                                }
                            }else {
                                if (progressDialog!=null)
                                    progressDialog.dismiss();
                                Toast.makeText(UpdateBookingsActivity.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    public String randomByDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date random = new Date();
        String bookNumber = dateFormat.format(random);
        System.out.println("Z"+bookNumber);
        return bookNumber;
    }

    public void shareIntentSpecificApps() {
        Resources resources = getResources();

        String[] mailto = {email};

        Intent emailIntent = new Intent();
        //  emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);


        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "/BillGenerate/Pdf/" + csvFile;
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        //emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));

        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("application/pdf");


        Intent openInChooser = Intent.createChooser(emailIntent, "Pick an Email provider");

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if(packageName.contains("android.email")) {
                if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- "+payment+"-"+ota+" voucher - "+property);
                }else{
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- Confirm Prepaid -"+ota+" voucher - "+property);
                }

                emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Hotel Partner,\n" +
                        "We Thank you for your continued support in ensuring the highest level of service Standards. \n" +
                        "\n" +
                        "Please find the attached reservation for you.");

                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                // startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
               /* Intent intent = new Intent();
               // intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
              //  intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, mailto);
                if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- "+payment+"-"+ota+" voucher - "+property);
                }else{
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- Confirm Prepaid -"+ota+" voucher - "+property);
                }

                intent.putExtra(Intent.EXTRA_TEXT, "Dear Hotel Partner,\n" +
                        "We Thank you for your continued support in ensuring the highest level of service Standards. \n" +
                        "\n" +
                        "Please find the attached reservation for you.");

                File roots = Environment.getExternalStorageDirectory();
                String pathToMyAttachedFiles = "/BillGenerate/Pdf/" + csvFile;
                File files = new File(roots, pathToMyAttachedFiles);
                if (!files.exists() || !files.canRead()) {
                    return;
                }
                Uri uris = Uri.fromFile(files);
                intent.putExtra(Intent.EXTRA_STREAM, uris);*/


                // intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            } else {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                //intent.setAction(Intent.ACTION_SEND);
                intent.setType("application/pdf");

                File roots = Environment.getExternalStorageDirectory();
                String pathToMyAttachedFiles = "/BillGenerate/Pdf/" + csvFile;
                File files = new File(roots, pathToMyAttachedFiles);
                if (!files.exists() || !files.canRead()) {
                    return;
                }
                Uri uris = Uri.fromFile(files);
                intent.putExtra(Intent.EXTRA_STREAM, uris);


                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }

    private void sendEmailattache() {
        String[] mailto = {email};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- "+payment+"-"+ota+" voucher - "+property);
        }else{
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- Confirm Prepaid -"+ota+" voucher - "+property);
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Hotel Partner,\n" +
                "We Thank you for your continued support in ensuring the highest level of service Standards. \n" +
                "\n" +
                "Please find the attached reservation for you.");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "/BillGenerate/Pdf/" + csvFile;
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

    public void onShareClick() {

        try{
            List<Intent> intentShareList = new ArrayList<Intent>();
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(shareIntent, 0);

            for (ResolveInfo resInfo : resolveInfoList) {
                String packageName = resInfo.activityInfo.packageName;
                String name = resInfo.activityInfo.name;


                if (packageName.contains("com.google")||packageName.contains("whatsapp")) {


                    if(packageName.contains("com.google")){
                        String[] mailto = {email};

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       /* if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- "+payment+"-"+ota+" voucher - "+property);
                        }else{
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ZINGO- Confirm Prepaid -"+ota+" voucher - "+property);
                        }*/
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, ota+" -Modified Booking!("+mBookingID.getText().toString()+","+mCID.getText().toString()+")");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Hotel Partner,\n" +
                                "We Thank you for your continued support in ensuring the highest level of service Standards. \n" +
                                "\n" +
                                "Please find the attached modified reservation for you.");
                        File root = Environment.getExternalStorageDirectory();
                        String pathToMyAttachedFile = "/BillGenerate/Pdf/" + csvFile;
                        File file = new File(root, pathToMyAttachedFile);
                        if (!file.exists() || !file.canRead()) {
                            return;
                        }
                        Uri uri = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            uri = FileProvider.getUriForFile(this, "app.zingo.com.billgenerate.fileprovider", file);
                        }else{
                            uri = Uri.fromFile(file);
                        }

                        //Uri uri = Uri.fromFile(file);
                        if(uri!=null){
                            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        }else{
                            Toast.makeText(this, "File cannot access", Toast.LENGTH_SHORT).show();
                        }

                        intentShareList.add(emailIntent);
                    }
                    else if(packageName.contains("whatsapp")){
                        Intent intent = new Intent();
                        // intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        File roots = Environment.getExternalStorageDirectory();
                        String pathToMyAttachedFiles = "/BillGenerate/Pdf/" + csvFile;
                        File files = new File(roots, pathToMyAttachedFiles);
                        if (!files.exists() || !files.canRead()) {
                            return;
                        }
                        Uri uris = Uri.fromFile(files);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            uris = FileProvider.getUriForFile(this, "app.zingo.com.billgenerate.fileprovider", files);

                        }else{
                            uris = Uri.fromFile(files);
                        }

                        if(uris!=null){
                            intent.putExtra(Intent.EXTRA_STREAM, uris);
                        }else{
                            Toast.makeText(this, "File cannot access", Toast.LENGTH_SHORT).show();
                        }


                        intentShareList.add(intent);
                    }

                    // startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                }/*else if(packageName.contains("whatsapp")){

               // startActivity(Intent.createChooser(intent, "Pick an Email provider"));
            }*/
            }

            if (intentShareList.isEmpty()) {
                Toast.makeText(UpdateBookingsActivity.this, "No apps to share !", Toast.LENGTH_SHORT).show();
            } else {
                Intent chooserIntent = Intent.createChooser(intentShareList.remove(0), "Share via");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentShareList.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void savenotification(final NotificationManager notification) {

        final ProgressDialog dialog = new ProgressDialog(UpdateBookingsActivity.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);
                LoginApi travellerApi = app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);
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
                                /*Toast.makeText(UpdateBookingsActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                    //SelectRoom.this.finish();
                                    boolean fileCreated = createPdf();
                                    if(fileCreated){
                                        onShareClick();
                                    }else{
                                        Toast.makeText(UpdateBookingsActivity.this, "File not created", Toast.LENGTH_SHORT).show();
                                        createPdf();
                                    }


                                    //Toast.makeText(UpdateBookingsActivity.this, "Save Notification", Toast.LENGTH_SHORT).show();




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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.reset_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent main = new Intent(UpdateBookingsActivity.this,MainActivity.class);
                    startActivity(main);
                    this.finish();
                    return true;

                case R.id.action_reset:
                    //resetValues();
                    clearForm((ViewGroup) findViewById(R.id.bill_details_layout));
                    break;
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
            Intent main = new Intent(UpdateBookingsActivity.this,MainActivity.class);
            startActivity(main);
            this.finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
            mCID.setText("");
            mCOD.setText("");
            mBook.setText("");
            book = false;
            zingoBookingId = "";
            dtos = null;
            bookings = null;
            commisionAmt = 0.0;
            commisionGST = 0.0;
            travellerIid= 0;
            mProperty.setSelection(0);
            mRoomCount.setSelection(0);
            mPayment.setSelection(0);
            mRate.setSelection(0);
            mDesc.setSelection(0);
            roomId =0;
            roomObject = null;

            if(tlist != null)
            {
                tlist.clear();
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }

    }

    private boolean isexist(ArrayList<Traveller> travellers) {

        if(travellers.size() != 0)
        {
            String name = mGuest.getText().toString().toLowerCase().trim();
            String mobile = mMobile.getText().toString().trim();
            //String email = mGuestEmail.getText().toString().trim();
            for (int i=0;i<travellers.size();i++)
            {
                Traveller traveller = travellers.get(i);
                if(traveller.getFirstName() != null && traveller.getPhoneNumber() != null && traveller.getEmail() != null)
                {

                    if(traveller.getFirstName().equalsIgnoreCase(name) && traveller.getPhoneNumber().equalsIgnoreCase(mobile)
                            )
                    {
                        travellerIid = traveller.getTravellerId();

                        return true;
                        //break;
                    }
                }
            }
        }
        return false;
    }

   /* public void loadRoomCategoriesSpinner(final int hotelId)
    {
        final ProgressDialog dialog = new ProgressDialog(UpdateBookingsActivity.this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final LoginApi getRoomCategories = app.zingo.com.billgenerate.Utils.Util.getClient().create(LoginApi.class);


                String authenticationString = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);
                Call<ArrayList<RoomCategories>> getRoomCategoriesResponse = getRoomCategories.fetchRoomCategoriesByHotelId(authenticationString, hotelId);



                getRoomCategoriesResponse.enqueue(new Callback<ArrayList<RoomCategories>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RoomCategories>> call, Response<ArrayList<RoomCategories>> response) {

                        try
                        {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            if(response.code() == 200)
                            {
                                if(response.body() != null && response.body().size() != 0)
                                {
                                    roomCategories = response.body();
                                    RoomCategorySpinnerAdapter adapter = new RoomCategorySpinnerAdapter(UpdateBookingsActivity.this,roomCategories);
                                    room_category_spinner.setAdapter(adapter);



                                }
                                else
                                {
                                    mRoomType.setVisibility(View.VISIBLE);
                                    room_category_spinner.setVisibility(View.GONE);
                                }
//                            loadRoomCategoriesSpinner();
                            }else{
                                mRoomType.setVisibility(View.VISIBLE);
                                room_category_spinner.setVisibility(View.GONE);
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RoomCategories>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(UpdateBookingsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mRoomType.setVisibility(View.VISIBLE);
                        room_category_spinner.setVisibility(View.GONE);
                    }
                });


            }
        });
    }*/

    public void loadRoomSpinner(ArrayList<Rooms> rooms)
    {


        RoomAdapter adapter = new RoomAdapter(UpdateBookingsActivity.this,rooms);
        mRoom.setAdapter(adapter);

    }

    private void bookedRoom() throws  Exception{


        final Rooms room = roomObject;

        room.setStatus("Quick");

        new app.zingo.com.billgenerate.Utils.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = app.zingo.com.billgenerate.Utils.Util.getToken(UpdateBookingsActivity.this);
                LoginApi api = Util.getClient().create(LoginApi.class);
                Call<Rooms> response = api.updateRoom(auth_string,room.getRoomId(), room);

                response.enqueue(new Callback<Rooms>() {
                    @Override
                    public void onResponse(Call<Rooms> call, Response<Rooms> response) {

                        try
                        {
                            int code = response.code();
                            if (code == 200||code ==204 ||code==201) {
                                Rooms roomresponse = response.body();
                                if (roomresponse != null) {


                                    Toast.makeText(UpdateBookingsActivity.this, "Room Updated", Toast.LENGTH_SHORT).show();
                                    roomList.remove(positionRoom);


                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Rooms> call, Throwable t) {

                    }
                });
            }
        });

    }
    private void setFields(Bookings1 booking) {

        DecimalFormat df = new DecimalFormat("#,###.##");
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        cits = booking.getCheckInDate();
        cots = booking.getCheckOutDate();


        try {
            mCID.setText(sdfs.format(sdf.parse(booking.getCheckInDate()))+"");
            mCOD.setText(sdfs.format(sdf.parse(booking.getCheckOutDate()))+"");
            mBook.setText(sdfs.format(sdf.parse(booking.getBookingDate()))+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mGuestCount.setText(booking.getNoOfAdults()+"");


        getTravelerById(booking.getTravellerId());
        getHotelName(booking.getHotelId());
        hotelId = booking.getHotelId();
        travellerIid = booking.getTravellerId();

        mRoomCharge.setText(booking.getSellRate()+"");
        mExtraCharge.setText(booking.getExtraCharges()+"");
        mHotelTaxes.setText(booking.getGstAmount()+"");
     //   room_category_spinner.setText(booking.getRoomCategory()+"");
        mTotal.setText(booking.getTotalAmount()+"");
       
        String bookSource = booking.getBookingSourceType();

        int ind = Arrays.asList(bookingSourceTitleStringArray).indexOf(bookSource);
        System.out.println("Booking Plan=="+booking.getBookingSourceType()+" i = "+ind);
        mSourceType.setSelection(ind);

        mOTACommison.setText(""+booking.getOTACommissionAmount());
        mBookingID.setText(""+booking.getOTABookingID());
        mOTAGST.setText(""+booking.getOTACommissionGSTAmount());
        mBooking.setText(""+booking.getOTATotalCommissionAmount());
        mOtaFee.setText(""+booking.getOTAServiceFees());
        mZingo.setText(""+booking.getZingoCommision());
        mAdditional.setText(""+booking.getAdditionalCharges());
        mCustomerPay.setText(""+booking.getCustomerPaymentAtOTA());
        mNights.setText(""+booking.getDurationOfStay());
        double net = booking.getTotalAmount() - booking.getCommissionAmount();
        mNet.setText(""+net);
        double arrAmt = totals / booking.getDurationOfStay();
        double arrRamt = arrAmt / booking.getNoOfRooms();
        mArr.setText("" + df.format(arrRamt));
        room_category_spinner.setText(""+booking.getRoomCategory());//room_count

        String[] rooms = getResources().getStringArray(R.array.room_count);


        int j = Arrays.asList(rooms).indexOf(booking.getNoOfRooms());
        System.out.println("Rooms Plan=="+booking.getNoOfRooms()+" i = "+j);
        mRoomCount.setSelection(j);

        if(booking.getBalanceAmount()!=0){
            String[] payment = getResources().getStringArray(R.array.payment_mode);


            int i = Arrays.asList(payment).indexOf("PaY@HOTEL");
            mPayment.setSelection(i);
        }else{
            String[] payment = getResources().getStringArray(R.array.payment_mode);


            int i = Arrays.asList(payment).indexOf("PREPAID/ONLINE");
            mPayment.setSelection(i);
        }


        if(bookSource.equals("OTA"))
        {
            String[] otaArray = getResources().getStringArray(R.array.OTA_items);

            mOTA.setAdapter(new GeneralAdapter(UpdateBookingsActivity.this,otaArray));
            int i = Arrays.asList(bookingSourceTitleStringArray).indexOf(bookSource);
            System.out.println("Booking Plan=="+booking.getBookingSource()+" i = "+i);
            mOTA.setSelection(i);
        }
        else if(bookSource.equals("B2B"))
        {
            String[] otaArray = getResources().getStringArray(R.array.B2B_items);
            mOTA.setAdapter(new GeneralAdapter(UpdateBookingsActivity.this,otaArray));
            int i = Arrays.asList(bookingSourceTitleStringArray).indexOf(bookSource);
            mOTA.setSelection(i);
        }
        else if(bookSource.equals("Offline_items"))
        {
            String[] otaArray = getResources().getStringArray(R.array.Offline_items);
            mOTA.setAdapter(new GeneralAdapter(UpdateBookingsActivity.this,otaArray));
            int i = Arrays.asList(bookingSourceTitleStringArray).indexOf(bookSource);
            mOTA.setSelection(i);
        }

        if(booking.getBookingPlan() != null)
        {

            String[] ratePlanArray = getResources().getStringArray(R.array.room_plan);


            int i = Arrays.asList(ratePlanArray).indexOf(booking.getBookingPlan());
            System.out.println("Booking Plan=="+booking.getBookingPlan()+" i = "+i);
            mRate.setSelection(i);

        }
    }

    public void priceCalculation(){
        try {
            String room = mRoomCharge.getText().toString();
            String extra = mExtraCharge.getText().toString();
            String gst = mHotelTaxes.getText().toString();

            double roomCharge = 0,extraCharge =0,gstCharge=0;

            if(room!=null&&!room.isEmpty()){
                roomCharge = Double.parseDouble(room);
            }

            if(extra!=null&&!extra.isEmpty()){
                extraCharge = Double.parseDouble(extra);
            }

            if(gst!=null&&!gst.isEmpty()){
                gstCharge = Double.parseDouble(gst);
            }

            double total = roomCharge+extraCharge+gstCharge;
            mTotal.setText(""+total);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public void otaCalculation(){
        try {
            String otaCA = mOTACommison.getText().toString();
            String otaGST = mOTAGST.getText().toString();


            double otaCharge = 0,otaGstCharge =0;

            if(otaCA!=null&&!otaCA.isEmpty()){
                otaCharge = Double.parseDouble(otaCA);
            }

            if(otaGST!=null&&!otaGST.isEmpty()){
                otaGstCharge = Double.parseDouble(otaGST);
            }



            double total = otaCharge+otaGstCharge;
            mBooking.setText(""+total);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }


}




