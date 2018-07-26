package app.zingo.com.billgenerate.Activiies;

import android.Manifest;
import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import app.zingo.com.billgenerate.Adapter.AdapterDevices;
import app.zingo.com.billgenerate.Adapter.AutocompleteCustomArrayAdapter;
import app.zingo.com.billgenerate.CustomViews.RecyclerTouchListener;
import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.Model.BookingsNotificationManagers;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.BillDataBase;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.ContactDetails;
import app.zingo.com.billgenerate.Utils.CustomAutoCompleteView;
import app.zingo.com.billgenerate.Utils.DataBaseHelper;
import app.zingo.com.billgenerate.Model.FireBaseModel;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Adapter.PaidStatusSpinnerAdapter;
import app.zingo.com.billgenerate.Utils.PlanDataBase;
import app.zingo.com.billgenerate.Adapter.PropertyAdapter;
import app.zingo.com.billgenerate.Adapter.RoomAdapter;
import app.zingo.com.billgenerate.Model.RoomCategories;
import app.zingo.com.billgenerate.Adapter.RoomCategorySpinnerAdapter;
import app.zingo.com.billgenerate.Utils.PreferenceHandler;
import app.zingo.com.billgenerate.Utils.RoomDataBase;
import app.zingo.com.billgenerate.Model.Rooms;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetails extends AppCompatActivity {

    Spinner mRoomCount, mPayment, mRate, mDesc, mProperty,mOTA,mDataBase,
            mSourceType,room_category_spinner,mRoom,mAddOnMode;
    LinearLayout mDataLayout,mOtherLayout,mAddLayout,
            mCustomerLayout,mOtaService,mOtaGSTLay,mOTAComLay,
            mOTAPER,mRoomLay,mOtaTransaction,mPartPayment,mAddOnLayout;
    CheckBox mAddOnCheck;
    EditText mLocation, mCity, mMobile, mRoomType,
            mGuestCount, mTotal, mBooking, mZingo,mOtherProperty,
            mBookingID, mEmail,  mNet, mNights, mArr,mOtaFee,
            mRoomCharge,mExtraCharge,mHotelTaxes,mAdditional,mCustomerPay,mOTAPerce,
            mOTACommison,mOTAGST,mOtherbookingSource,mGuestEmail,mComments,mPartPay,mAddOnServices,mAddonCharges;
    TextView mBook, mCID, mCOD;
    CustomAutoCompleteView mGuest;
    Button mSave, mCalculate,mSendGuest;
    String[] bookingSourceArray;
    int hotelId,travellerIid,roomId=0;
    ArrayList<HotelDetails> chainsList;
    Traveller dtos;
    Bookings1 bookings;
    double commisionAmt,commisionGST;
    HotelDetails list;
    ArrayList<Traveller> tlist;
    String[] bookingSourceTitleStringArray,otaPaymentMode;

    //Databaases
    DataBaseHelper dbHelper;
    RoomDataBase room;
    PlanDataBase plan;
    BillDataBase bill;
    String zingoBookingId,bookingTime,bookingRoomType;

    boolean book = false;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;


    //pdf
    String property,propertyGuest, email, ota, city, location, guest,
            mobile, bdate, cit, cot, rooms, roomNum, count, plans,
            payment, desc, total, booking, zingo, net, nights,comments,
            arr, cits, cots,roomCharge,extraCharge,hoteltaxes,addtional,customer,otaFee,otaCommission,otaGST;
    double addOnTotals,totals,otaAmt,zingoAmt,otaToHotel,addtionalChrg,payCustomer,
            customerToHotel,otaToHotelPay,otaFeeAmount,gstValue,otaComAmount,otaGstAmount,partPaid,partBalance,addOnChargesPayment;
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

    private static Font subFontColor = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD,BaseColor.BLUE);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font smallBolds = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.UNDERLINE, BaseColor.RED);


    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);
    private static Font smallColor = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL,BaseColor.BLUE);
    private static Font smallRed = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL,BaseColor.RED);

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

    //Phone OTP
    private FirebaseAuth mAuth;
    private static final String TAG = "BillDetails";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    //Guest Pdf
    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;
    String guestpdfFilename = "";
    String guestPdf;
    String guestpdfFile;
    int pdfMinLen;

    public static Font greyFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.GRAY);



    //Print pdf from Bluetooth
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    ArrayList<BluetoothDevice> deviceList;
    volatile boolean stopWorker;

    RecyclerView mDeviceList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_bill_details);
           //setContentView(R.layout.new_bill_booking_activity);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Create Bill");

            mDeviceList = (RecyclerView) findViewById(R.id.device_list);

            mProperty = (Spinner) findViewById(R.id.bill_property_name);
            mDataBase = (Spinner) findViewById(R.id.bill_data_base);
            mAddLayout = (LinearLayout)findViewById(R.id.additional_layout);
            mOtaService = (LinearLayout)findViewById(R.id.ota_service);
            mCustomerLayout = (LinearLayout)findViewById(R.id.customer_pay_layout);
            mOTAComLay = (LinearLayout)findViewById(R.id.ota_commision_amt_layout);
            mOTAPER = (LinearLayout)findViewById(R.id.ota_commision_per_layout);
            mRoomLay = (LinearLayout)findViewById(R.id.room_layout);
            mOtaTransaction = (LinearLayout)findViewById(R.id.ota_transaction);
            mPartPayment = (LinearLayout)findViewById(R.id.part_payment);
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
            mGuestEmail = (EditText) findViewById(R.id.bill_guest_email);
            mComments = (EditText) findViewById(R.id.bill_comment);
            mPartPay = (EditText) findViewById(R.id.bill_part_paid);
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
            mSave = (Button) findViewById(R.id.send_email);
            mSendGuest = (Button) findViewById(R.id.send_sms);
            //mSendGuest.setVisibility(View.GONE);
            mCalculate = (Button) findViewById(R.id.bill_calculate);
            mOtherbookingSource = (EditText) findViewById(R.id.other_booking_source);
            room_category_spinner = (Spinner) findViewById(R.id.room_category_spinner);
            mRoom = (Spinner) findViewById(R.id.room_spinner);
            mAddonCharges = (EditText)findViewById(R.id.bill_add_on_service_amount);
            mAddOnServices = (EditText)findViewById(R.id.bill_add_on_service);
            mAddOnMode = (Spinner) findViewById(R.id.bill_add_on_payment);
            mAddOnLayout = (LinearLayout) findViewById(R.id.add_on_layout);
            mAddOnLayout.setVisibility(View.GONE);
            mAddOnCheck = (CheckBox) findViewById(R.id.add_on_check);

            bookingSourceTitleStringArray = getResources().getStringArray(R.array.booking_source_title);
            otaPaymentMode = getResources().getStringArray(R.array.ota_payment_mode);

            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(BillDetails.this,bookingSourceTitleStringArray);
            mSourceType.setAdapter(spinneradapter);

          //  findBT();

            mSourceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if(bookingSourceTitleStringArray[position].equals("OTA"))
                        {
                            String[] bookingSourceArray = getResources().getStringArray(R.array.OTA_items);

                            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(BillDetails.this,bookingSourceArray);
                            mOTA.setAdapter(spinneradapter);

                            PaidStatusSpinnerAdapter adapterS = new PaidStatusSpinnerAdapter(BillDetails.this,otaPaymentMode);
                            mPayment.setAdapter(adapterS);
                            mOtaTransaction.setVisibility(View.VISIBLE);
                        }
                        else if(bookingSourceTitleStringArray[position].equals("B2B"))
                        {
                            String[] bookingSourceArray = getResources().getStringArray(R.array.B2B_items);

                            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(BillDetails.this,bookingSourceArray);
                            mOTA.setAdapter(spinneradapter);

                            String[] otherPayment = getResources().getStringArray(R.array.payment_mode);

                            PaidStatusSpinnerAdapter adapters = new PaidStatusSpinnerAdapter(BillDetails.this,otherPayment);
                            mPayment.setAdapter(adapters);

                            mOtaTransaction.setVisibility(View.GONE);

                        }
                        else if(bookingSourceTitleStringArray[position].equals("Offline"))
                        {
                            String[] bookingSourceArray = getResources().getStringArray(R.array.Offline_items);

                            PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(BillDetails.this,bookingSourceArray);
                            mOTA.setAdapter(spinneradapter);
                            mOtaTransaction.setVisibility(View.GONE);
                            String[] otherPayment = getResources().getStringArray(R.array.payment_mode);

                            PaidStatusSpinnerAdapter adapters = new PaidStatusSpinnerAdapter(BillDetails.this,otherPayment);
                            mPayment.setAdapter(adapters);
                            String bookingnumber = randomByDate();
                            mBookingID.setText(""+bookingnumber);
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

            mAddOnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(mAddOnCheck.isChecked()){
                        mAddOnLayout.setVisibility(View.VISIBLE);
                    }else{
                        mAddOnLayout.setVisibility(View.GONE);
                    }
                }
            });
            //bookingSourceArray = getResources().getStringArray(R.array.OTA_items);
            /*PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(BillDetails.this,bookingSourceArray);
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

            mZingo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    generalCalculation();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            mDataBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(mDataBase.getSelectedItem().toString().equalsIgnoreCase("OTHERS")){
                        mDataLayout.setVisibility(View.GONE);
                        mOtherLayout.setVisibility(View.VISIBLE);
                        mLocation.setText("");
                        mCity.setText("");
                        mEmail.setText("");


                    }else{
                        mDataLayout.setVisibility(View.VISIBLE);
                        mOtherLayout.setVisibility(View.GONE);
                        getHotels();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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
                        mPartPayment.setVisibility(View.GONE);

                    }else if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PART PAYMENT")){
                        mCustomerLayout.setVisibility(View.GONE);
                        mAddLayout.setVisibility(View.GONE);
                        mOtaService.setVisibility(View.GONE);
                        mPartPayment.setVisibility(View.VISIBLE);
                    }else{
                        mCustomerLayout.setVisibility(View.GONE);
                        mAddLayout.setVisibility(View.GONE);
                        mOtaService.setVisibility(View.GONE);
                        mPartPayment.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(mRate.getSelectedItem().toString().equalsIgnoreCase("CP")){

                        mDesc.setSelection(0);

                    }else if(mRate.getSelectedItem().toString().equalsIgnoreCase("EP")){
                        mDesc.setSelection(1);

                    }else if(mRate.getSelectedItem().toString().equalsIgnoreCase("AP")){
                        mDesc.setSelection(2);

                    } else if(mRate.getSelectedItem().toString().equalsIgnoreCase("MAP")){
                        mDesc.setSelection(3);

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
                            Toast.makeText(BillDetails.this, "File not generate but booking happened", Toast.LENGTH_SHORT).show();
                            createPdf();
                        }

                    }else{
                        String bookingrefno = PreferenceHandler.getInstance(BillDetails.this).getBookingRefNumber();
                        String bookingRefno1 = mBookingID.getText().toString();
                        if(!bookingrefno.isEmpty() && bookingrefno.equals(bookingRefno1))
                        {
                            Toast.makeText(BillDetails.this,"Booking Is Already Done For This Referrence ID",Toast.LENGTH_LONG)
                                    .show();
                            if(csvFile != null)
                            {
                                onShareClick();
                            }

                        }
                        else
                        {
                            validate();
                        }
                        //validate();
                    }

                }
            });

            mCalculate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String partPayment = mPartPay.getText().toString();
                    String addOnCharges = mAddonCharges.getText().toString();
                    cit = mCID.getText().toString();
                    cot = mCOD.getText().toString();
                    total = mTotal.getText().toString();
                   // zingo = mZingo.getText().toString();
                    roomNum = mRoomCount.getSelectedItem().toString();
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

                   /* if (booking == null || booking.isEmpty()) {
                        //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mBooking.setError("Should not be Empty");
                        mBooking.requestFocus();

                    }*/ /*else if (zingo == null || zingo.isEmpty()) {
                        //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mZingo.setError("Should not be Empty");
                        mZingo.requestFocus();

                    }*/  if (cit == null || cit.isEmpty()) {
                        //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mCID.setError("Should not be Empty");
                        mCID.requestFocus();

                    } else if (cot == null || cot.isEmpty()) {
                        // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mCOD.setError("Should not be Empty");
                        mCOD.requestFocus();

                    } else if (total == null || total.isEmpty()) {
                        // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mTotal.setError("Should not be Empty");
                        mTotal.requestFocus();

                    } else if (roomCharge == null || roomCharge.isEmpty()) {
                        Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();


                    } else if (extraCharge == null || extraCharge.isEmpty()) {
                        Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();

                    } else if (hoteltaxes == null || hoteltaxes.isEmpty()) {
                        Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();

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
                        //178.57
                        //333.33
                        //159

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

                        if(mAddOnCheck.isChecked()){
                            if(addOnCharges==null||addOnCharges.isEmpty()){
                                addOnChargesPayment =0;
                            }else{
                                addOnChargesPayment = Double.parseDouble(addOnCharges);
                            }
                        }else{
                            addOnChargesPayment=0;
                        }

                        //Net Amount

                        DecimalFormat df = new DecimalFormat("#,###.##");
                        totals = Double.parseDouble(total);
                        addOnTotals = totals+addOnChargesPayment;
                        System.out.println("Totals=="+totals);
                        if(booking==null||booking.isEmpty()){
                            otaAmt = 0;
                        }else{
                            otaAmt = Double.parseDouble(booking);
                        }

                       // zingoAmt = Double.parseDouble(zingo);
                        double zingoAmts=0;
                        System.out.println(" Property == "+property);
                        //Toast.makeText(BillDetails.this, "Property name"+property, Toast.LENGTH_SHORT).show();
                        if(property.contains("Holiday Homes")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_holiday));
                        }else if(property.equalsIgnoreCase("Zingo Nagananda Residency")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_naga));
                        }else if(property.equalsIgnoreCase("RB Hospitality")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_rb));
                        }else if(property.contains("SS Lumina Hotel")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_ssl));
                        }else if(property.equalsIgnoreCase("Sanctum Manor")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_sanctum));
                        }else if(property.equalsIgnoreCase("Hotel Ashapura International")){

                            if(rooms.contains("Non")){
                                zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_asha_nonac));
                                System.out.println("Room spinner=="+rooms);
                            }else{
                                zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_asha_ac));
                                System.out.println("Room spinner=="+room_category_spinner.getSelectedItem().toString());
                            }


                        }else if(property.equalsIgnoreCase("Farmers Corner")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_farmers));
                        }else if(property.equalsIgnoreCase("Coffe Bean Inn")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_coffee));
                        }else if(property.equalsIgnoreCase("Kalyan Residency")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_kalyan));
                        }else if(property.equalsIgnoreCase("Emirates Suites")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_emirates));
                        }else if(property.equalsIgnoreCase("Tranquil Homes")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_tranquil));
                        }else if(property.contains("Woodlands Hotel")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_woodlands));
                        }else if(property.equalsIgnoreCase("Airport Stay Inn")){
                            zingoAmts = Double.parseDouble(getResources().getString(R.string.commission_airport));
                        }else if(property.equalsIgnoreCase("Shree Vara Residency")){
                            zingoAmts = 200.00;
                        }else{
                            zingoAmts=0;
                        }



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
                            double arrAmt = addOnTotals / diffDays;
                            double arrRamt = arrAmt / rooms;
                            mArr.setText("" + df.format(arrRamt));
                        } else {
                            //double arrAmt = total/diffDays;
                            double arrRamt = addOnTotals / rooms;
                            mArr.setText("" + df.format(arrRamt));
                        }

                        double roomCount = Double.parseDouble(roomNum);
                        zingoAmt = diffDays * zingoAmts * roomCount * 1.0;
                        mZingo.setText(""+df.format(zingoAmt));
                        zingo = mZingo.getText().toString();
                        zingo = ""+df.format(zingoAmt);

                        commisionAmt = otaAmt + zingoAmt+otaFeeAmount;
                        double gst = commisionAmt*18;
                        commisionGST = gst/100;
                        otaToHotel = addOnTotals-otaAmt;





                        if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PART PAYMENT")){
                            if(partPayment==null||partPayment.isEmpty()){
                                partPaid = 0;
                            }else{
                                partPaid = Double.parseDouble(partPayment);
                            }
                        }else{
                            partPaid=0;
                        }


                        partBalance = addOnTotals- partPaid;
                        //hotelToZingo = otaToHotel-
                        double netAmt = addOnTotals - (commisionAmt-otaFeeAmount);
                        mNet.setText("" + df.format(netAmt));

                        if(source!=null&&source.equalsIgnoreCase("MAKEMY TRIP")){

                            if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                                otaToHotelPay = (payCustomer) - (otaAmt+otaFeeAmount);
                                customerToHotel = (addOnTotals+addtionalChrg+otaFeeAmount)-payCustomer;
                            }else{
                                otaToHotelPay = (otaAmt+otaFeeAmount);
                                customerToHotel = (addOnTotals+addtionalChrg+otaFeeAmount);
                            }

                        }else{

                            if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                                otaToHotelPay = payCustomer - otaAmt;
                                customerToHotel = (addOnTotals+addtionalChrg)-payCustomer;
                            }else{
                                otaToHotelPay =  otaAmt;
                                customerToHotel = (addOnTotals+addtionalChrg);
                            }

                        }

                    }

                }
            });


            mProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    hotelId = chainsList.get(i).getHotelId();
                    property = chainsList.get(i).getHotelName();
                    propertyGuest = chainsList.get(i).getHotelName();

                    mLocation.setText(chainsList.get(i).getLocalty());
                    mCity.setText(chainsList.get(i).getCity());
                    roomsArrayList = (ArrayList<Rooms>)chainsList.get(i).getRooms();
                    //getContactByHotelId(chainsList.get(i).getHotelId());
                    if(chainsList.get(i).getContact()!=null&&chainsList.get(i).getContact().size()!=0){
                        mEmail.setText(""+chainsList.get(i).getContact().get(0).getEmailList());
                    }else{
                        mEmail.setText("");
                    }

                    mRoomType.setVisibility(View.GONE);
                    room_category_spinner.setVisibility(View.VISIBLE);
                    loadRoomCategoriesSpinner(chainsList.get(i).getHotelId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            room_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    rooms = roomCategories.get(i).getCategoryName();
                    mRoomType.setText(rooms);
                    roomList = new ArrayList<>();
                    System.out.println("Before Room SIze=="+roomsArrayList.size());
                    if(roomsArrayList!=null&&roomsArrayList.size()!=0){
                        for(int j=0;j<roomsArrayList.size();j++){

                            /*System.out.println("Room Category id=="+roomCategories.get(i).getRoomCategoryId());
                            System.out.println("Rooms Category id=="+roomsArrayList.get(j).getRoomNo());*/
                            if((roomCategories.get(i).getRoomCategoryId()==roomsArrayList.get(j).getRoomCategoryId())&&roomsArrayList.get(j).getStatus().equalsIgnoreCase("Available")){
                                roomList.add(roomsArrayList.get(j));
                            }
                        }

                        if(roomList!=null&&roomList.size()!=0){
                           // roomsArrayList = new ArrayList<>();
                            System.out.println("After Room SIze=="+roomsArrayList.size());
                            mRoomLay.setVisibility(View.VISIBLE);

                            loadRoomSpinner(roomList);
                        }else{
                            roomId =0;
                            Toast.makeText(BillDetails.this, "No available rooms in this hotel", Toast.LENGTH_SHORT).show();
                            mRoomLay.setVisibility(View.GONE);
                        }
                    }else{
                        roomId =0;
                        Toast.makeText(BillDetails.this, "There is no rooms in this hotel", Toast.LENGTH_SHORT).show();
                        mRoomLay.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

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


            mSendGuest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{

                        //Print
                      /*  boolean isfilecreated =  createPDFGuest();
                        if (isfilecreated) {
                            sendData();
                        }
*/

                        //Uncomment

                        if(book){
                            if(mGuestEmail.getText().toString()==null&&mGuestEmail.getText().toString().isEmpty()){
                                Toast.makeText(BillDetails.this, "Guest Email should not be empty", Toast.LENGTH_SHORT).show();
                                mGuestEmail.setError("Please fill the field");
                            }else{

                                boolean isfilecreated =  createPDFGuest();
                                if (isfilecreated) {
                                    sendEmailattacheGuest();
                                }



                            }

                        }else{
                            Toast.makeText(BillDetails.this, "After Booking only voucher send to Guest", Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });



            mDeviceList.addOnItemTouchListener(new RecyclerTouchListener(this,mDeviceList, new RecyclerTouchListener.ClickListener(){

                @Override
                public void onClick(View view, int position) {


                    try {
                        mmDevice = deviceList.get(position);
                        openBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void validate() {

//        String properties = mProperty.getSelectedItem().toString();
        try{
            String data = mDataBase.getSelectedItem().toString();


            rooms = mRoomType.getText().toString();
            plans = mRate.getSelectedItem().toString();
            roomNum = mRoomCount.getSelectedItem().toString();
            payment = mPayment.getSelectedItem().toString();
            bookingID = mBookingID.getText().toString();
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
            comments = mComments.getText().toString();

            System.out.println("Print" + String.valueOf(path));

            if (location == null || location.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (city == null || city.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (guest == null || guest.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            }/* else if (mobile == null || mobile.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }*/ else if (count == null || count.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (desc == null || desc.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (total == null || total.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } /*else if (booking == null || booking.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            }*/ else if (zingo == null || zingo.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (bdate == null || bdate.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (cit == null || cit.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (cot == null || cot.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (email == null || email.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (ota == null || ota.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else if (net == null || net.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please click calculate button", Toast.LENGTH_SHORT).show();
            } else if (nights == null || nights.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please click calculate button", Toast.LENGTH_SHORT).show();
            } else if (arr == null || arr.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please click calculate button", Toast.LENGTH_SHORT).show();
            }else if (roomCharge == null || roomCharge.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } /*else if (addtional == null || addtional.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        } else if (customer == null || customer.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }*/else if (extraCharge == null || extraCharge.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            }else if (hoteltaxes == null || hoteltaxes.isEmpty()) {
                Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            } else {

                if(data.equalsIgnoreCase("OTHERS")){

                    if(mOtherProperty.getText().toString()==null||mOtherProperty.getText().toString().isEmpty()){
                        mOtherProperty.setError("Please enter property name");
                        mOtherProperty.requestFocus();
                    }else{
                        property = mOtherProperty.getText().toString();
                    }
                }else{
                    property = chainsList.get(mProperty.getSelectedItemPosition()).getHotelDisplayName();
                }

                //createPdf();
                System.out.println("Property name==" + property);


                if(data.equalsIgnoreCase("OTHERS")){
                    // sendEmailattache();
                    boolean isfilecreated = createPdf();
                    if (isfilecreated) {
                        onShareClick();
                    }
                }else{
                    /*if (mobile == null || mobile.isEmpty()) {
                        //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        addTraveler();
                    }else if(mobile.equalsIgnoreCase("0")){
                        addTraveler();
                    }else{
                        getTravelerByPhone(mobile);
                    }*/

                    if(travellerIid==0){
                        addTraveler();
                        //System.out.println("Not exist");
                    }else if(tlist != null && !isexist(tlist)){
                        addTraveler();
                        //updateTraveller("paylater");
                        //System.out.println("Not exist "+isexist(tlist));
                    }
                    else
                    {
                        setData(travellerIid);
                    }
                }


                //System.out.println("oooo" + isfilecreated);
           /* if (isfilecreated) {
                //sendEmailattache();

                if(data.equalsIgnoreCase("OTHERS")){
                   // sendEmailattache();
                    onShareClick();
                }else{
                    if (mobile == null || mobile.isEmpty()) {
                        //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        addTraveler();
                    }else{
                        getTravelerByPhone(mobile);
                    }
                }



            }*/

            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void setData(int tid){

        try{
            bookings = new Bookings1();
            String bookingnumber = randomByDate();
            bookings.setBookingNumber(bookingnumber);

            bookings.setTravellerId(tid);
            bookings.setCheckInDate(cits);
            bookings.setOptCheckInDate(cits);//----------------change in case of customer app booking-----------
            bookings.setCheckOutDate(cots);
            bookings.setOptCheckOutDate(cots);
            bookings.setHotelId(hotelId);
            bookings.setBookingSourceType(mSourceType.getSelectedItem().toString());
            bookings.setNoOfAdults(Integer.parseInt(count));
            bookings.setBookingStatus("Quick");
            bookings.setBookingSource(mOTA.getSelectedItem().toString());
            bookings.setOTACommissionGSTAmount(otaGstAmount);
            bookings.setOTACommissionAmount(otaComAmount);
            bookings.setOTATotalCommissionAmount(otaAmt);
            bookings.setOTAServiceFees(otaFeeAmount);

            double roomCount = Double.parseDouble(mRoomCount.getSelectedItem().toString());
            if(roomCount>1){
                bookings.setRoomId(0);
            }else{
                bookings.setRoomId(roomId);
            }

            double otaHotel=0;
            double netAmount =  (Double.parseDouble(total)-otaAmt);

            if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                otaHotel = netAmount - customerToHotel;
            }else{
                otaHotel = netAmount;
            }

            System.out.println("Ota hotel == "+otaHotel);
           // Toast.makeText(this, "Ota hotel == "+otaHotel, Toast.LENGTH_SHORT).show();
            if(otaHotel<0){
                bookings.setOTAToPayHotel(otaHotel);
                bookings.setHotelToPayOTA(Math.abs(otaHotel));
            }else{
                bookings.setOTAToPayHotel(Math.abs(otaHotel));
                bookings.setHotelToPayOTA(0);
            }
            bookings.setZingoCommision(zingoAmt);
            bookings.setCustomerPaymentAtOTA(payCustomer);
            bookings.setAdditionalCharges(addtionalChrg);
            bookings.setOTABookingID(bookingID);
            bookings.setGstAmount((int)gstValue);
            bookings.setReason(comments);
            DecimalFormat df = new DecimalFormat("##.##");
            bookings.setCommisionGSTAmount(commisionGST);
            if(mRoomCharge.getText().toString()==null||mRoomCharge.getText().toString().isEmpty()){
                bookings.setSellRate(0);
            }else{

                double sellRate = Double.parseDouble(mRoomCharge.getText().toString());
                bookings.setSellRate((int) sellRate);
            }

            if(mExtraCharge.getText().toString()==null||mExtraCharge.getText().toString().isEmpty()){
                bookings.setExtraCharges(0);
            }else{


                double sellRate = Double.parseDouble(mExtraCharge.getText().toString());
                bookings.setExtraCharges((int) sellRate);
            }
            bookings.setCommissionAmount((int) commisionAmt);
            bookings.setDurationOfStay(Integer.parseInt(nights));
            bookings.setTotalAmount((int) Double.parseDouble(total));
            if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
                bookings.setBalanceAmount(((int) customerToHotel));
                bookings.setHotelToZingo(zingoAmt);
            }else{
                bookings.setBalanceAmount(0);
                bookings.setZingoToHotel(zingoAmt);
            }

            bookings.setBookingPlan(mRate.getSelectedItem().toString());
            bookings.setRoomCategory(mRoomType.getText().toString());

            if(mRoomCount.getSelectedItem().toString().isEmpty()){
                bookings.setNoOfRooms(1);
            }else{
                bookings.setNoOfRooms(Integer.parseInt(mRoomCount.getSelectedItem().toString()));
            }


            //Current time and date for booking

            long date = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");

            if(mBook.getText().toString()==null||mBook.getText().toString().isEmpty()){
                String bookingDate = sdf.format(date);
                System.out.println("Booking Date==="+bookingDate);
                bookings.setBookingDate(bookingDate);
            }else{

                Date date1 = sdfs.parse(mBook.getText().toString());
                bookings.setBookingDate(sdf.format(date1));
            }

       /* String bookingDate = sdf.format(date);
        System.out.println("Booking Date==="+bookingDate);
        bookings.setBookingDate(bookingDate);*/

            SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
            Date d = new Date();
            String time = sdft.format(d);

            bookings.setBookingTime(time);

            if(book){
                boolean fileCreated = createPdf();
                if(fileCreated){
                    onShareClick();
                }else{
                    Toast.makeText(this, "File not generate but booking happened", Toast.LENGTH_SHORT).show();
                    createPdf();
                }

            }else{
              updateRoomBooking(bookings);
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

            String additional = "ADDITIONAL NOTE:";
            String addFooter = comments;
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
           /* if(mOTA.getSelectedItem().toString().equalsIgnoreCase("ZINGO DIRECT")){
                paragraph.add(new Paragraph("",smallBolds));
            }else{
                paragraph.add(new Paragraph("Booking ID: " + bookingID, smallBolds));
            }*/

            paragraph.add(new Paragraph("Booking Source: " + ota, smallBolds));
            if(zingoBookingId!=null&&!zingoBookingId.isEmpty()){
                paragraph.add(new Paragraph("Booking ID: " + zingoBookingId, smallBolds));
            }

            addEmptyLine(paragraph, 1);
            paragraph.add(new Paragraph(text, smallBold));
            addEmptyLine(paragraph, 1);
            createTables(paragraph);
            addEmptyLine(paragraph, 1);
            paragraph.add(new Paragraph("Payment Breakup", smallBold));
            addEmptyLine(paragraph, 1);
            if(!mSourceType.getSelectedItem().toString().equalsIgnoreCase("OTA")){

                if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PART PAYMENT")){
                    createTablesPaymentDirectPart(paragraph);
                }else{
                    createTablesPaymentDirect(paragraph);
                }


            }else{
                if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")||mPayment.getSelectedItem().toString().equalsIgnoreCase("B2C POSTPAID")){
                    createTablesPaymentHotel(paragraph);
                }else if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PREPAID/ONLINE")||mPayment.getSelectedItem().toString().equalsIgnoreCase("B2C PREPAID")){
                    createTablesPayment(paragraph);
                }
            }


            if(comments!=null&&!comments.isEmpty()){
                addEmptyLine(paragraph, 2);
                addChild(new Paragraph(additional, subFontColor));
                if(mAddOnCheck.isChecked()){
                    if(mAddOnMode.getSelectedItem().toString().equalsIgnoreCase("PREPAID/ONLINE")){
                        if(mAddOnServices.getText().toString()!=null&&!mAddOnServices.getText().toString().isEmpty()){
                            addChild(new Paragraph("Customer have already paid Rs."+addOnChargesPayment+" for "+mAddOnServices.getText().toString(), subFontColor));
                           // createTablesAdditional(paragraph);
                        }

                    }else{

                        if(mAddOnServices.getText().toString()!=null&&!mAddOnServices.getText().toString().isEmpty()){
                            addChild(new Paragraph("Collect Rs."+addOnChargesPayment+" for "+mAddOnServices.getText().toString()+" From Customer", subFontColor));
                           // createTablesAdditionalPayAt(paragraph);
                        }
                    }

                }

                addChild(new Paragraph(comments, smallColor));
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
        if(mOTA.getSelectedItem().toString().equalsIgnoreCase("ZINGO DIRECT")){
            table.addCell(zingoBookingId);
        }else{
            table.addCell(zingoBookingId);
        }

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

    private void createTablesPayment(Paragraph para) throws BadElementException {
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
        if(mAddOnCheck.isChecked()){

            PdfPCell ac = new PdfPCell(new Phrase("(AA) Add-On Charges", smallColor));
            table.addCell(ac);
            ac = new PdfPCell(new Phrase("INR " + addOnChargesPayment, smallColor));
            table.addCell(ac);

            table.addCell("Total Amount(A+AA)");
            table.addCell("INR " + addOnTotals);
        }
        table.addCell("OTA Commission");
        table.addCell("INR " + otaComAmount);
        table.addCell("OTA GST @ 18 %\n(Incl IGST/CGST/SGST)");
        table.addCell("INR " + otaGstAmount);
        table.addCell("(B) OTA COMMISSION(Incl GST)");
        table.addCell("INR " + booking);
        table.addCell("(C) ZINGOHOTELS.COM COMMISION");
        table.addCell("INR " + mZingo.getText().toString());
        table.addCell("OTA to Pay Hotel(A-B)");
        table.addCell("INR " + dfs.format(otaToHotel));
        table.addCell("Hotel to Pay Zingo(C)");
        table.addCell("INR " +  mZingo.getText().toString());
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

        if(mAddOnCheck.isChecked()){

            PdfPCell ac = new PdfPCell(new Phrase("(AA) Add-On Charges", smallColor));
            table.addCell(ac);
            ac = new PdfPCell(new Phrase("INR " + addOnChargesPayment, smallColor));
            table.addCell(ac);

            table.addCell("Total Amount(A+AA)");
            table.addCell("INR " + addOnTotals);
        }
        if(!mOTA.getSelectedItem().toString().equalsIgnoreCase("BOOKING.COM")&&!mOTA.getSelectedItem().toString().equalsIgnoreCase("EXPEDIA")){
            table.addCell("OTA Commission");
            table.addCell("INR " + otaComAmount);
            table.addCell("OTA GST @ 18 %\n(Incl IGST/CGST/SGST)");
            table.addCell("INR " + otaGstAmount);
        }

        table.addCell("(B) OTA COMMISSION(Incl GST)");
        table.addCell("INR " + booking);
        table.addCell("(C) ZINGOHOTELS.COM COMMISION");
        table.addCell("INR " +  mZingo.getText().toString());
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
        table.addCell("INR " +  mZingo.getText().toString());
        table.addCell("NET AMOUNT (A-B-C)");
        table.addCell("INR " + net);
        table.addCell("ARR");
        table.addCell("INR " + arr);

        para.add(table);

    }


    private void createTablesAdditional(Paragraph para) throws BadElementException {
        PdfPTable table = new PdfPTable(1);


        DecimalFormat dfs = new DecimalFormat("#.##");
       // addChild(new Paragraph("Customer have already paid Rs."+addOnChargesPayment+" for "+mAddOnServices.getText().toString(), subFontColor));

        PdfPCell c1 = new PdfPCell(new Phrase("Customer have already paid Rs."+addOnChargesPayment+" for "+mAddOnServices.getText().toString(), subFontColor));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);


        table.setHeaderRows(1);





        para.add(table);

    }

    private void createTablesAdditionalPayAt(Paragraph para) throws BadElementException {
        PdfPTable table = new PdfPTable(1);


        DecimalFormat dfs = new DecimalFormat("#.##");
        // addChild(new Paragraph("Customer have already paid Rs."+addOnChargesPayment+" for "+mAddOnServices.getText().toString(), subFontColor));
        PdfPCell c1 = new PdfPCell(new Phrase("Collect Rs."+addOnChargesPayment+" for "+mAddOnServices.getText().toString()+" From Customer", subFontColor));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.BLUE);
        table.addCell(c1);


        table.setHeaderRows(1);





        para.add(table);

    }

    private void createTablesPaymentDirect(Paragraph para) throws BadElementException {
        PdfPTable table = new PdfPTable(2);


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



        table.addCell("(i) ROOM CHARGES\n"+roomNum+" Room, "+nights+" Night");
        table.addCell("INR " + roomCharge);
        table.addCell("(ii) EXTRA CHARGES");
        table.addCell("INR " + extraCharge);
        table.addCell("(iii) HOTEL TAXES");
        table.addCell("INR " + hoteltaxes);
        table.addCell("(A) HOTEL GROSS CHARGES (i+ii+iii)");
        table.addCell("INR " + total);

        table.addCell("ZINGOHOTELS.COM COMMISION(B)");
        table.addCell("INR " +  mZingo.getText().toString());

        table.addCell("Hotel to Pay Zingo");
        table.addCell("INR " +  mZingo.getText().toString());

        table.addCell("NET AMOUNT (A-B)");
        table.addCell("INR " + net);
        table.addCell("ARR");
        table.addCell("INR " + arr);

        para.add(table);

    }

    private void createTablesPaymentDirectPart(Paragraph para) throws BadElementException {
        PdfPTable table = new PdfPTable(2);


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



        table.addCell("(i) ROOM CHARGES\n"+roomNum+" Room, "+nights+" Night");
        table.addCell("INR " + roomCharge);
        table.addCell("(ii) EXTRA CHARGES");
        table.addCell("INR " + extraCharge);
        table.addCell("(iii) HOTEL TAXES");
        table.addCell("INR " + hoteltaxes);
        table.addCell("(A) HOTEL GROSS CHARGES (i+ii+iii)");
        table.addCell("INR " + total);

        table.addCell("ZINGOHOTELS.COM COMMISION(B)");
        table.addCell("INR " +  mZingo.getText().toString());

        table.addCell("Hotel to Pay Zingo");
        table.addCell("INR " +  mZingo.getText().toString());

        table.addCell("NET AMOUNT (A-B)");
        table.addCell("INR " + net);
        table.addCell("AMOUNT PAID On ONLINE");
        table.addCell("INR " + partPaid);
        table.addCell("BALANCE AMOUNT Payable at Hotel");
        table.addCell("INR " + partBalance);
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
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BillDetails.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BillDetails.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BillDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BillDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
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
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(BillDetails.this);
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



                                if(mDataBase.getSelectedItem().toString().equalsIgnoreCase("OTHERS")){
                                    mLocation.setText("");
                                    mCity.setText("");
                                    mEmail.setText("");
                                }else{
                                    mLocation.setText(list.getLocalty());
                                    mCity.setText(list.getCity());
                                    getContactByHotelId(id);
                                }



                            }

//


                        } else {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(BillDetails.this, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
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

    private void getHotels() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BillDetails.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi hotelOperation = Util.getClient().create(LoginApi.class);
                Call<ArrayList<HotelDetails>> response = hotelOperation.getHotelsList(auth_string/*userId*/);

                response.enqueue(new Callback<ArrayList<HotelDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HotelDetails>> call, Response<ArrayList<HotelDetails>> response) {
                        System.out.println("GetHotelByProfileId = " + response.code());
                        //chainsList = response.body();
                        ArrayList<HotelDetails> taggedProfiles = response.body();

                        if (progressDialog != null)
                            progressDialog.dismiss();
                        try{
                            if (response.code() == 200) {
                                if (taggedProfiles != null && taggedProfiles.size() != 0) {

                                   /* chainsList = new ArrayList<>();
                                    if(chainsList != null && chainsList.size() != 0)
                                    {
                                        chainsList.clear();
                                    }

                                    for (int i=0;i<taggedProfiles.size();i++)
                                    {
                                        if(taggedProfiles.get(i) != null && taggedProfiles.get(i).getApproved())
                                        {
                                            chainsList.add(taggedProfiles.get(i));
                                        }
                                    }*/
                                   chainsList = response.body();
                                    PropertyAdapter chainAdapter = new PropertyAdapter(BillDetails.this, chainsList);
                                    mProperty.setAdapter(chainAdapter);
//


                                } else {
                                    Toast.makeText(BillDetails.this, "No Hotels", Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent(BillDetails.this,AddHotelActivity.class);
                                startActivity(intent);*/
                                }
                            } else {
                                Toast.makeText(BillDetails.this, "Check your internet connection or please try after some time",
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

                        Toast.makeText(BillDetails.this, "Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public void getContactByHotelId(final int id) {
      /* final ProgressDialog dialog = new ProgressDialog(BillDetails.this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(BillDetails.this);
                LoginApi operations = Util.getClient().create(LoginApi.class);
                Call<ArrayList<ContactDetails>> getContactResponse = operations.getContactByHotelId(authenticationString, id);

                getContactResponse.enqueue(new Callback<ArrayList<ContactDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ContactDetails>> call, Response<ArrayList<ContactDetails>> response) {
                        System.out.println("Code  = " + response.code());
                      /*  if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }*/
                        try{
                            ArrayList<ContactDetails> contactResponse = response.body();

                            if (response.code() == 200 && contactResponse != null && contactResponse.size() != 0) {
                                final ContactDetails contactInfo = contactResponse.get(contactResponse.size() - 1);


                                mEmail.setText(contactInfo.getEmailList());


                            } else {
                           /* hotelMob =  "No Mobile";
                            hotelPhone =  "No Phone";
                            hotelEmail =  "No Email";*/
                                mEmail.setText("");
                            }

                        }catch (Exception e){
                           /* if(dialog != null && dialog.isShowing())
                            {
                                dialog.dismiss();
                            }*/
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(Call<ArrayList<ContactDetails>> call, Throwable t) {
                        System.out.println("Contact failed");
                        Toast.makeText(BillDetails.this, "Failed Due to: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                       /* if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }*/
                    }
                });
            }
        });
    }

    //QuickBook
//Do bookings
    private void updateRoomBooking(final Bookings1 bookings) {

        final ProgressDialog progressDialog = new ProgressDialog(BillDetails.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BillDetails.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);
                Call<Bookings1> call = apiService.postBooking(auth_string, bookings);

                call.enqueue(new Callback<Bookings1>() {
                    @Override
                    public void onResponse(Call<Bookings1> call, Response<Bookings1> response) {

                        int statusCode = response.code();
                        try{
                            if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                Bookings1 dto = response.body();
                                if (dto != null) {
                                    book = true;
                                    zingoBookingId = ""+response.body().getBookingId();
                                    bookingTime = ""+response.body().getBookingTime();
                                    bookingRoomType = ""+response.body().getRoomCategory();

                                    //Storing ota booking id for check of booking created or not
                                    PreferenceHandler.getInstance(BillDetails.this).setBookingRefNumber(bookings.getOTABookingID());

                                    Toast.makeText(BillDetails.this, "Booking done successfully", Toast.LENGTH_SHORT).show();
                                    //sendEmailattache();
                                    FireBaseModel fm = new FireBaseModel();
                                    fm.setSenderId("415720091200");
                                    fm.setServerId("AIzaSyBFdghUu7AgQVnu27xkKKLHJ6oSz9AnQ8M");
                                    fm.setHotelId(hotelId);
                                    fm.setTitle("New Booking from Zingo Hotels");
                                    fm.setMessage("Congrats! "+property+" got one new booking for "+nights +" nights from "+cit+" to "+cot+"\nBooking ID:"+dto.getBookingId());
                                    //registerTokenInDB(fm);
                                    fm.setTravellerName(guest);
                                    fm.setNoOfGuest("No of Guest: "+dto.getNoOfAdults());
                                    fm.setCheckInDate(mCID.getText().toString());
                                    fm.setCheckOutDate(mCOD.getText().toString());
                                    fm.setTotalAmount("Rs. "+dto.getTotalAmount());
                                    fm.setCommissionAmount("Rs. "+dto.getCommissionAmount());
                                    fm.setNetAmount("Rs. "+net);
                                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
                                    SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
                                    fm.setNotificationDate(sdf.format(new Date()));
                                    fm.setNotificationTime(sdft.format(new Date()));
                                    fm.setBookingDateTime(mBook.getText().toString()+","+dto.getBookingTime());

                                    sendNotification(fm,dto.getBookingStatus());
                                    double roomCount = Double.parseDouble(mRoomCount.getSelectedItem().toString());
                                    if(roomCount>1){

                                        Toast.makeText(BillDetails.this, "Please Allocate Room from Hotel Main App", Toast.LENGTH_SHORT).show();

                                    }else{
                                        if(roomObject!=null&&roomId!=0){

                                           // bookedRoom();
                                        }
                                    }




                                }else{
                                    String subject=null;



                                    //sendEmailattache();
                                    onShareClick();
                                }

                            } else {
                                if (progressDialog != null)
                                    progressDialog.dismiss();

                                Toast.makeText(BillDetails.this, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<Bookings1> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                        Toast.makeText(BillDetails.this, " failed due to : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });
    }

    public void sendNotification(final FireBaseModel fireBaseModel,final String bookingStatus) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BillDetails.this);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);


                System.out.println("Model" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.sendBookingNotification(auth_string, fireBaseModel)/*getString()*/;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, retrofit2.Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        try{
                            if (statusCode == 200) {

                                ArrayList<String> list = response.body();

                                Toast.makeText(BillDetails.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                                //sendEmailattache();
                               /* NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(fireBaseModel.getTitle());
                                nf.setNotificationFor(fireBaseModel.getMessage());
                                nf.setHotelId(fireBaseModel.getHotelId());
                                savenotification(nf);*/

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
                                nf.setBooking(bookingStatus);
                                saveBookingnotification(nf);



                            } else {

                                Toast.makeText(BillDetails.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BillDetails.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);
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
                                /* dtos = list.get(0);

                                if (dtos != null) {
                                    *//*travellerIid = dtos.getTravellerId();
                                    mGuest.setText(dtos.getFirstName());*//*
                                    //updateTraveller(travellerIid);

                                }*/

                                AutocompleteCustomArrayAdapter autocompleteCustomArrayAdapter =
                                        new AutocompleteCustomArrayAdapter(BillDetails.this,R.layout.hotels_row,tlist,"BillDetails");
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
                            Toast.makeText(BillDetails.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
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

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BillDetails.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);
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
                                Toast.makeText(BillDetails.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
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

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BillDetails.this);
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);
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
                                Toast.makeText(BillDetails.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BillDetails.this, "No apps to share !", Toast.LENGTH_SHORT).show();
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

        final ProgressDialog dialog = new ProgressDialog(BillDetails.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(BillDetails.this);
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
                                /*Toast.makeText(BillDetails.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                    //SelectRoom.this.finish();
                                    boolean fileCreated = createPdf();
                                    if(fileCreated){
                                        onShareClick();
                                    }else{
                                        Toast.makeText(BillDetails.this, "File not created", Toast.LENGTH_SHORT).show();
                                        createPdf();
                                    }


                                    //Toast.makeText(BillDetails.this, "Save Notification", Toast.LENGTH_SHORT).show();




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

    private void saveBookingnotification(final BookingsNotificationManagers notification) {

        final ProgressDialog dialog = new ProgressDialog(BillDetails.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(BillDetails.this);
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
                                /*Toast.makeText(BillDetails.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                    //SelectRoom.this.finish();
                                    boolean fileCreated = createPdf();
                                    if(fileCreated){
                                        onShareClick();
                                    }else{
                                        Toast.makeText(BillDetails.this, "File not created", Toast.LENGTH_SHORT).show();
                                        createPdf();
                                    }


                                    //Toast.makeText(BillDetails.this, "Save Notification", Toast.LENGTH_SHORT).show();




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
                    Intent main = new Intent(BillDetails.this,MainActivity.class);
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
            Intent main = new Intent(BillDetails.this,MainActivity.class);
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
       /* mBookingID.setText("");
        mMobile.setText("");
        mGuest.setText("");
        mBook.setText("");
        mCID.setText("");
        mCOD.setText("");
        mRoomType.setText("");
        mRoomCount.setSelection(0);
        mPayment.setSelection(0);
        mRate.setSelection(0);
        mGuestCount.setText("");
        mDesc.setSelection(0);
        mProperty.setSelection(0);
        mRoomCharge.setText("");
        mExtraCharge.setText("");
        mHotelTaxes.setText("");
        mTotal.setText("");
        mOtaFee.setText("");
        mBooking.setText("");
        mZingo.setText("");
        mAdditional.setText("");
        mCustomerPay.setText("");
        mNights.setText("");
        mNet.setText("");
        mArr.setText("");
        book = false;
        zingoBookingId = "";
        dtos = null;
        bookings = null;
        commisionAmt = 0.0;
        commisionGST = 0.0;
        travellerIid = 0;

        if(tlist != null)
        {
            tlist.clear();
        }*/
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

    public void loadRoomCategoriesSpinner(final int hotelId)
    {
        final ProgressDialog dialog = new ProgressDialog(BillDetails.this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final LoginApi getRoomCategories = Util.getClient().create(LoginApi.class);


                String authenticationString = Util.getToken(BillDetails.this);
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
                                    RoomCategorySpinnerAdapter adapter = new RoomCategorySpinnerAdapter(BillDetails.this,roomCategories);
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
                        Toast.makeText(BillDetails.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mRoomType.setVisibility(View.VISIBLE);
                        room_category_spinner.setVisibility(View.GONE);
                    }
                });


            }
        });
    }

    public void loadRoomSpinner(ArrayList<Rooms> rooms)
    {


        RoomAdapter adapter = new RoomAdapter(BillDetails.this,rooms);
        mRoom.setAdapter(adapter);

    }

    private void bookedRoom() throws  Exception{


            final Rooms room = roomObject;

            room.setStatus("Quick");

            new ThreadExecuter().execute(new Runnable() {
                @Override
                public void run() {
                    String auth_string = Util.getToken(BillDetails.this);
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


                                        Toast.makeText(BillDetails.this, "Room Updated", Toast.LENGTH_SHORT).show();
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



    //Guest Pdf

    private boolean createPDFGuest () throws Exception{

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            File sd = Environment.getExternalStorageDirectory();
            guestPdf = System.currentTimeMillis() + ".pdf";

            File directory = new File(sd.getAbsolutePath()+"/BillGenerate/Pdf/Guest/");
            //create directory if not exist
            if (!directory.exists() && !directory.isDirectory()) {
                directory.mkdirs();
            }

            guestpdfFile = sd.getAbsolutePath()+"/BillGenerate/Pdf/Guest/"+guestPdf;

            File file = new File(directory, guestPdf);
            String path = "docs/" + guestpdfFilename;
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(file));
            doc.addAuthor("Lucida Hospitality Pvt Ltd");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("zingohotels.com");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.LETTER);

            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            boolean beginPage = true;
            int y = 0;


                if(beginPage){
                    generateLayout(doc, cb);
                    printPageNumber(cb);
                    doc.newPage();
                    generateLayoutNext(doc, cb);
                    printPageNumber(cb);
                }

           // printPageNumber(cb);
            return  true;

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
            return false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
            if (docWriter != null)
            {
                docWriter.close();
            }

        }
    }

    private void generateLayout(Document doc, PdfContentByte cb)  {

        try {

            cb.setLineWidth(1f);

            createHeadings(cb,400,730,"Hotel Confirmation Voucher");

            cb.moveTo(25,680);
            cb.lineTo(600,680);

            createHeadingss(cb,50,665,"Guest Name: ");
            createHeadings(cb,135,665,mGuest.getText().toString());

            cb.moveTo(25,650);
            cb.lineTo(600,650);
            cb.stroke();

            // createHeadingsTitles(cb,50,630,"Zingo Premium Hotel Richmond Road");

            createHeadingsTitles(cb,50,630,propertyGuest);
            createHeadingsTitle(cb,50,610,mLocation.getText().toString());
            createHeadingsTitle(cb,50,590,mCity.getText().toString());


            createContent(cb,200,620, "Check IN",PdfContentByte.ALIGN_LEFT);
            createHeadingsTitles(cb,200,605,mCID.getText().toString());


            createContent(cb,450,620, "Check OUT",PdfContentByte.ALIGN_LEFT);
            createHeadingsTitles(cb,450,605,mCOD.getText().toString());

            cb.moveTo(25,580);
            cb.lineTo(600,580);
            cb.stroke();



            //Normal
            createHeadingsContent(cb,50,550,"Booking ID : ");
            createHeadingsTitle(cb,200,550,zingoBookingId);

            createHeadingsContent(cb,50,520,"Date of Booking : ");
            createHeadingsTitle(cb,200,520,mBook.getText().toString()+","+bookingTime);
            //createHeadingsTitle(cb,200,520,mBook.getText().toString()+",12:31 am");

            createHeadingsContent(cb,50,490,"Room Type: ");
            createHeadingsTitle(cb,200,490,bookingRoomType);
            //createHeadingsTitle(cb,200,490,"Deluxe Executive Family");

            createHeadingsContent(cb,50,460,"No of Rooms: ");
            createHeadingsTitle(cb,200,460,mRoomCount.getSelectedItem().toString());

            createHeadingsContent(cb,50,430,"No of PAX: ");
            createHeadingsTitle(cb,200,430,mGuestCount.getText().toString());

            createHeadingsContent(cb,50,400,"Inclusion: ");
            createHeadingsTitle(cb,200,400,mDesc.getSelectedItem().toString());


            cb.moveTo(25,380);
            cb.lineTo(600,380);
            cb.stroke();


            createHeadingsContent(cb,200,360,"Payment BreakUp");

            createHeadingsContent(cb,50,340,"Room Charges : ");
            createHeadingsTitle(cb,200,340,"Rs "+mRoomCharge.getText().toString());

            createHeadingsContent(cb,50,310,"Extra Charges : ");
            createHeadingsTitle(cb,200,310,"Rs "+mExtraCharge.getText().toString());

            createHeadingsContent(cb,50,280,"Hotel Taxes : ");
            createHeadingsTitle(cb,200,280,"Rs "+mHotelTaxes.getText().toString());

            createHeadingsContent(cb,50,250,"Net Amount : ");
            createHeadingsTitle(cb,200,250,"Rs "+mTotal.getText().toString());


            if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PART PAYMENT")){
                createHeadingsContent(cb,50,220,"Paid Amount : ");
                createHeadingsTitle(cb,200,220,"Rs "+partPaid);
                createHeadingsContent(cb,50,190,"Balance Amount : ");
                createHeadingsTitle(cb,200,190,"Rs "+partBalance);
            }

            createHeadingsContent(cb,400,340,"Payment Mode ");
            createHeadingsTitle(cb,400,320, mPayment.getSelectedItem().toString());

            cb.moveTo(25,175);
            cb.lineTo(600,175);
            cb.stroke();




            createHeadings(cb,50,100,"Additional Information");
            createHeadingsContent(cb,55,80,getResources().getString(R.string.hotel_policy));
            createHeadingsContent(cb,55,60,getResources().getString(R.string.cancel_policy));


            //Add logo
            Drawable d = getResources ().getDrawable (R.drawable.logo_zingo);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream streams = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, streams);
            byte[] bitmapData = streams.toByteArray();

            Image companyLogo = Image.getInstance(bitmapData);
            // Image companyLogo = Image.getInstance("logo.png");
            companyLogo.setAbsolutePosition(25,700);
            companyLogo.scalePercent(75);
            doc.add(companyLogo);
        }


        catch (Exception ex){
            ex.printStackTrace();
        }

    }


    private void generateLayoutNext(Document doc, PdfContentByte cb)  {

        try {

            cb.setLineWidth(1f);

            /*createHeadingsTitles(cb,50,680,"Hotel Policy");
            createHeadingss(cb,50,665,getResources().getString(R.string.hotel_policy_1));*/


            doc.add(new Paragraph("Hotel Policy", subFont));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_1), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_2), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_3), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_4), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_5), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_6), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_7), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_8), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_9), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_10), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_11), small));
            doc.add(new Paragraph(getResources().getString(R.string.hotel_policy_12), small));

            addEmptyLine(new Paragraph(), 2);
            //doc.addE
            //doc.add( addEmptyLine(new Paragraph(), 2));
            doc.add(new Paragraph("Cancellation & Amendment Policy", subFont));
            doc.add(new Paragraph(getResources().getString(R.string.cancel_policy_1), small));
            doc.add(new Paragraph(getResources().getString(R.string.cancel_policy_2), small));
            doc.add(new Paragraph(getResources().getString(R.string.cancel_policy_3), small));
            doc.add(new Paragraph("    ", small));
            doc.add(new Paragraph(getResources().getString(R.string.zingo_policy), smallRed));

            cb.rectangle(50,20,550,90);


            cb.moveTo(340,20);
            cb.lineTo(340,110);


            createHeadingss(cb,60,100,"Zingo Hotel Contact Info ");
            createHeadingss(cb,60,80,"Lucida Hospitality Pvt Ltd");
            createHeadingss(cb,60,65,"No 88,First Floor,Koramangala Industrial Layout ");
            createHeadingss(cb,60,50,"5th Block Near Jyothi Niwas College., Bengaluru");
            createHeadingss(cb,60,35,"Karanataka 560095");

            createHeadingss(cb,360,70,"Email: hello@zingohotels.com");
            createHeadingss(cb,360,50,"Telephone: +91-7065651651");
            createHeadingss(cb,360,30,"Website: www.zingohotels.com ");

            cb.stroke();




        }


        catch (Exception ex){
            ex.printStackTrace();
        }


    }



    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void createHeadings(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 12);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void createHeadingss(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bf, 12);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void createHeadingsContent(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bf, 18);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void createHeadingsTitle(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 15);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void createHeadingsTitles(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 18);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }


    private void printPageNumber(PdfContentByte cb){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 10, 0);
        cb.endText();

        pageNumber++;

    }

    private void createContent(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(bf, 8);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    private void createContentTariff(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(bf, 15);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    private void createTableZingoAddress(Paragraph para) throws BadElementException {
        PdfPTable table = new PdfPTable(2);


        DecimalFormat dfs = new DecimalFormat("#.##");


        PdfPCell c1 = new PdfPCell(new Phrase("Zingo Hotel Contact Info \n Lucida Hospitality Pvt Ltd \n "+
                "No 88,First Floor,Koramangala Industrial Layout \n"+
                "5th Block Near Jyothi Niwas College., Bengaluru\n Karanataka 560095", small));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Email: hello@zingohotels.com \n Telephone: +91-7065651651 \n "+
                "Website: www.zingohotels.com \n", small));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);
        table.setHeaderRows(1);

        para.add(table);

    }

    private void sendEmailattacheGuest() throws Exception {
        String[] mailto = {mGuestEmail.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Your Hotel Booking is Confirmed at "+propertyGuest+","+location+", "+city+",India "+" - "+zingoBookingId);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Booking ID:"+zingoBookingId+"\n"
                +"Booked on:"+mBook.getText().toString()+","+bookingTime+"\n"
                +"BOOKING CONFIRMED\n"+
                "Dear "+mGuest.getText().toString()+",\n" +
                    "Thank you for choosing "+propertyGuest+" sold and Marketed by ZingoHotels. \n\n"
                +"Your hotel booking is confirmed. No need to call us to reconfirm this booking. \n" +
                "Your eTicket is attached with the email sent to your email-Id.\n\n" +
                "\n Hotel Details:\n\n"+
                propertyGuest+"\n"+location+"\n"+city+"\n"+
                "\n\n\nThis hotel is powered by Zingo Hotels.\n Customer care-number: \n+91-7065651651");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "/BillGenerate/Pdf/Guest/"+guestPdf;
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
            Toast.makeText(BillDetails.this, "File cannot access", Toast.LENGTH_SHORT).show();
        }
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

    public void generalCalculation(){



        cit = mCID.getText().toString();
        cot = mCOD.getText().toString();
        total = mTotal.getText().toString();
        zingo = mZingo.getText().toString();
        roomNum = mRoomCount.getSelectedItem().toString();
        booking = mBooking.getText().toString();
        roomCharge = mRoomCharge.getText().toString();
        extraCharge = mExtraCharge.getText().toString();
        hoteltaxes = mHotelTaxes.getText().toString();
        addtional = mAdditional.getText().toString();
        customer = mCustomerPay.getText().toString();
        otaFee = mOtaFee.getText().toString();
        otaCommission = mOTACommison.getText().toString();
        otaGST = mOTAGST.getText().toString();

        if(zingo.contains(",")){
            zingo = zingo.replace(",","");
        }

        String source = mOTA.getSelectedItem().toString();

                   /* if (booking == null || booking.isEmpty()) {
                        //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mBooking.setError("Should not be Empty");
                        mBooking.requestFocus();

                    }*/ /*else if (zingo == null || zingo.isEmpty()) {
                        //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                        mZingo.setError("Should not be Empty");
                        mZingo.requestFocus();

                    }*/  if (cit == null || cit.isEmpty()) {
            //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            mCID.setError("Should not be Empty");
            mCID.requestFocus();

        } else if (cot == null || cot.isEmpty()) {
            // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            mCOD.setError("Should not be Empty");
            mCOD.requestFocus();

        } else if (total == null || total.isEmpty()) {
            // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            mTotal.setError("Should not be Empty");
            mTotal.requestFocus();

        } else if (roomCharge == null || roomCharge.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();


        } else if (extraCharge == null || extraCharge.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();

        } else if (hoteltaxes == null || hoteltaxes.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();

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

            if(mAddOnCheck.isChecked()){
                if(mAddonCharges.getText().toString()==null||mAddonCharges.getText().toString().isEmpty()){
                    addOnChargesPayment =0;
                }else{
                    addOnChargesPayment = Double.parseDouble(mAddonCharges.getText().toString());
                }
            }else{
                addOnChargesPayment=0;
            }

            //Net Amount

            DecimalFormat df = new DecimalFormat("#,###.##");
            totals = Double.parseDouble(total);
            addOnTotals = totals+addOnChargesPayment;
            //Net Amount

//            DecimalFormat df = new DecimalFormat("#,###.##");
//            totals = Double.parseDouble(total);
            if(booking==null||booking.isEmpty()){
                otaAmt = 0;
            }else{
                otaAmt = Double.parseDouble(booking);
            }

            if(zingo!=null&&!zingo.isEmpty()){
                zingoAmt = Double.parseDouble(zingo);
            }else{
                zingoAmt = 0;
            }

            //double zingoAmts=0;
            System.out.println(" Property == "+property);
            //Toast.makeText(BillDetails.this, "Property name"+property, Toast.LENGTH_SHORT).show();



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
                double arrAmt = addOnTotals / diffDays;
                double arrRamt = arrAmt / rooms;
                mArr.setText("" + df.format(arrRamt));
            } else {
                //double arrAmt = total/diffDays;
                double arrRamt = addOnTotals / rooms;
                mArr.setText("" + df.format(arrRamt));
            }

            double roomCount = Double.parseDouble(roomNum);
           // zingoAmt = diffDays * zingoAmts * roomCount;
          //  mZingo.setText(""+df.format(zingoAmt));
            zingo = mZingo.getText().toString();
            zingo = ""+df.format(zingoAmt);

            commisionAmt = otaAmt + zingoAmt+otaFeeAmount;
            double gst = commisionAmt*18;
            commisionGST = gst/100;
            otaToHotel = addOnTotals-otaAmt;


            //hotelToZingo = otaToHotel-
            double netAmt = addOnTotals - (commisionAmt-otaFeeAmount);
            mNet.setText("" + df.format(netAmt));

            if(source!=null&&source.equalsIgnoreCase("MAKEMY TRIP")){
                otaToHotelPay = (payCustomer) - (otaAmt+otaFeeAmount);
                customerToHotel = (addOnTotals+addtionalChrg+otaFeeAmount)-payCustomer;
            }else{
                otaToHotelPay = payCustomer - otaAmt;
                customerToHotel = (addOnTotals+addtionalChrg)-payCustomer;
            }

        }
    }


    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                Toast.makeText(BillDetails.this, "No bluetooth adapter available", Toast.LENGTH_SHORT).show();
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            deviceList = new ArrayList<>();

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // Set<BluetoothDevice> getDevices = mBluetoothAdapter.get();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    deviceList.add(device);
                    System.out.println("UUID == "+device.getUuids().toString());
                   /* if (device.getName().equals("Bluetooth music")) {
                        mmDevice = device;
                        break;
                    }*/
                }
            }

            if(deviceList!=null&&deviceList.size()!=0){
                AdapterDevices adapterDevices = new AdapterDevices(BillDetails.this,deviceList);
                mDeviceList.setAdapter(adapterDevices);
            }
            Toast.makeText(BillDetails.this, "Bluetooth device found.", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

          //  beginListenForData();

            Toast.makeText(this, "Bluetooth Opened", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Toast.makeText(BillDetails.this, "Data sent"+data, Toast.LENGTH_SHORT).show();


                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException {
        try {

            // the text typed by the user
            //String msg = myTextbox.getText().toString();
            //msg += "\n";

            InputStream is = this.openFileInput(guestpdfFile); // Where this is Activity
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead = is.read(b);
            while (bytesRead != -1) {
                bos.write(b, 0, bytesRead);
            }
            byte[] bytes = bos.toByteArray();

            byte[] printformat = { 27, 33, 0 }; //try adding this print format

            mmOutputStream.write(printformat);
            mmOutputStream.write(bytes);

            // tell the user data were sent
            Toast.makeText(this, "Data Sent", Toast.LENGTH_SHORT).show();

            closeBT();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();

            Toast.makeText(this, "Bluetooth Closed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




