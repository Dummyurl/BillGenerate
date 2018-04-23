package app.zingo.com.billgenerate;

import android.Manifest;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


import app.zingo.com.billgenerate.Model.BillDataBase;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.ContactDetails;
import app.zingo.com.billgenerate.Model.DataBaseHelper;
import app.zingo.com.billgenerate.Model.FireBaseModel;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.Model.PaidStatusSpinnerAdapter;
import app.zingo.com.billgenerate.Model.PlanDataBase;
import app.zingo.com.billgenerate.Model.PropertyAdapter;
import app.zingo.com.billgenerate.Model.RoomDataBase;
import app.zingo.com.billgenerate.Model.ThreadExecuter;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.Model.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetails extends AppCompatActivity {

    Spinner mRoomCount, mPayment, mRate, mDesc, mProperty,mOTA,mDataBase;
    LinearLayout mDataLayout,mOtherLayout,mAddLayout,mCustomerLayout;
    EditText mLocation, mCity, mGuest, mMobile, mRoomType,
            mGuestCount, mTotal, mBooking, mZingo,mOtherProperty,
            mBookingID, mEmail,  mNet, mNights, mArr,
                        mRoomCharge,mExtraCharge,mHotelTaxes,mAdditional,mCustomerPay;
    TextView mBook, mCID, mCOD;
    Button mSave, mCalculate;
    String[] bookingSourceArray;
    int hotelId,travellerIid;
    ArrayList<HotelDetails> chainsList;
    Traveller dtos;
    Bookings1 bookings;
    double commisionAmt;
    HotelDetails list;

    //Databaases
    DataBaseHelper dbHelper;
    RoomDataBase room;
    PlanDataBase plan;
    BillDataBase bill;

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
            arr, cits, cots,roomCharge,extraCharge,hoteltaxes,addtional,customer;
    double totals,otaAmt,zingoAmt,otaToHotel,addtionalChrg,payCustomer,customerToHotel,otaToHotelPay;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        mProperty = (Spinner) findViewById(R.id.bill_property_name);
        mDataBase = (Spinner) findViewById(R.id.bill_data_base);
        mAddLayout = (LinearLayout)findViewById(R.id.additional_layout);
        mCustomerLayout = (LinearLayout)findViewById(R.id.customer_pay_layout);
        mDataLayout = (LinearLayout)findViewById(R.id.data_property_layout);
        mOtherLayout = (LinearLayout)findViewById(R.id.other_property_layout);
        mRoomType = (EditText) findViewById(R.id.bill_property_room);
        mRate = (Spinner) findViewById(R.id.bill_property_rate);
        mRoomCount = (Spinner) findViewById(R.id.bill_property_room_num);
        mPayment = (Spinner) findViewById(R.id.bill_property_payment);
        mOtherProperty = (EditText) findViewById(R.id.bill_property_other);
        mLocation = (EditText) findViewById(R.id.bill_property_location);
        mEmail = (EditText) findViewById(R.id.bill_property_emal);
        mBookingID = (EditText) findViewById(R.id.bill_booking_id);
        mCity = (EditText) findViewById(R.id.bill_property_city);
        mGuest = (EditText) findViewById(R.id.bill_guest_name);
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
        mZingo = (EditText) findViewById(R.id.bill_zingo_com);
        mOTA = (Spinner) findViewById(R.id.bill_booking_ota);
        mNet = (EditText) findViewById(R.id.bill_net_amount);
        mNights = (EditText) findViewById(R.id.bill_total_nights);
        mArr = (EditText) findViewById(R.id.bill_arr);
        mBook = (TextView) findViewById(R.id.bill_property_booking);
        mCID = (TextView) findViewById(R.id.bill_property_checkiin);
        mCOD = (TextView) findViewById(R.id.bill_property_checkout);
        mSave = (Button) findViewById(R.id.send_email);
        mCalculate = (Button) findViewById(R.id.bill_calculate);

        bookingSourceArray = getResources().getStringArray(R.array.OTA_items);
        PaidStatusSpinnerAdapter spinneradapter = new PaidStatusSpinnerAdapter(BillDetails.this,bookingSourceArray);
        mOTA.setAdapter(spinneradapter);

        bill = new BillDataBase(this);

        getHotels();

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

        mDataBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(mDataBase.getSelectedItem().toString().equalsIgnoreCase("OTHERS")){
                    mDataLayout.setVisibility(View.GONE);
                    mOtherLayout.setVisibility(View.VISIBLE);

                }else{
                    mDataLayout.setVisibility(View.VISIBLE);
                    mOtherLayout.setVisibility(View.GONE);
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

                }else{
                    mCustomerLayout.setVisibility(View.GONE);
                    mAddLayout.setVisibility(View.GONE);
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

                validate();
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

                if (booking == null || booking.isEmpty()) {
                    //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                    mBooking.setError("Should not be Empty");
                    mBooking.requestFocus();

                } else if (zingo == null || zingo.isEmpty()) {
                    //Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                    mZingo.setError("Should not be Empty");
                    mZingo.requestFocus();

                } else if (cit == null || cit.isEmpty()) {
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
                    // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                    mTotal.setError("Should not be Empty");
                    mTotal.requestFocus();

                } else if (extraCharge == null || extraCharge.isEmpty()) {
                    // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                    mTotal.setError("Should not be Empty");
                    mTotal.requestFocus();

                } else if (hoteltaxes == null || hoteltaxes.isEmpty()) {
                    // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                    mTotal.setError("Should not be Empty");
                    mTotal.requestFocus();

                } else if (addtional == null || addtional.isEmpty()) {
                    // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                    addtionalChrg = 0;

                } else if (customer == null || customer.isEmpty()) {
                    // Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                    payCustomer = 0;

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

                    if(customer==null||customer.isEmpty()){
                        payCustomer = 0;
                    }else{
                        payCustomer = Double.parseDouble(mCustomerPay.getText().toString());
                    }


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


                    commisionAmt = otaAmt + zingoAmt;
                    otaToHotel = totals-otaAmt;
                    customerToHotel = (totals+addtionalChrg)-payCustomer;
                    otaToHotelPay = payCustomer - otaAmt;
                    //hotelToZingo = otaToHotel-
                    double netAmt = totals - (otaAmt + zingoAmt);
                    mNet.setText("" + df.format(netAmt));

                }

            }
        });


        mProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               /* System.out.println("Id=="+chainsList.get(i).getDocumentId());
                getDoc(chainsList.get(i).getDocumentId());*/
                getHotelName(chainsList.get(i).getHotelId());
                hotelId = chainsList.get(i).getHotelId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void validate() {

        String properties = mProperty.getSelectedItem().toString();
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
        } else if (booking == null || booking.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        } else if (zingo == null || zingo.isEmpty()) {
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
                property = list.getHotelDisplayName();
            }

            //createPdf();
            System.out.println("Property name==" + property);


            boolean isfilecreated = createPdf();
            System.out.println("oooo" + isfilecreated);
            if (isfilecreated) {
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



            }

        }


    }


    public void setData(){

        bookings = new Bookings1();
        String bookingnumber = randomByDate();
        bookings.setBookingNumber(bookingnumber);

        bookings.setTravellerId(travellerIid);
        bookings.setCheckInDate(cits);
        bookings.setOptCheckInDate(cits);//----------------change in case of customer app booking-----------
        bookings.setCheckOutDate(cots);
        bookings.setOptCheckOutDate(cots);
        bookings.setHotelId(hotelId);
        bookings.setBookingSourceType(mOTA.getSelectedItem().toString());
        bookings.setNoOfAdults(Integer.parseInt(count));
        bookings.setBookingStatus("Quick");
        bookings.setBookingSource("OTA");
        bookings.setCommissionAmount((int) commisionAmt);
        bookings.setDurationOfStay(Integer.parseInt(nights));
        bookings.setTotalAmount((int) Double.parseDouble(total));
        if(mPayment.getSelectedItem().toString().equalsIgnoreCase("PaY@HOTEL")){
            bookings.setBalanceAmount((int) Double.parseDouble(total));
        }else{
            bookings.setBalanceAmount(0);
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
        String bookingDate = sdf.format(date);
        System.out.println("Booking Date==="+bookingDate);
        bookings.setBookingDate(bookingDate);

        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
        Date d = new Date();
        String time = sdft.format(d);

        bookings.setBookingTime(time);

        updateRoomBooking(bookings);
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
        table.addCell("(B) OTA COMMISSION(Incl GST)");
        table.addCell("INR " + booking);
        table.addCell("(C) ZINGOHOTELS.COM COMMISION");
        table.addCell("INR " + zingo);
        table.addCell("(D) Additional Charges");
        table.addCell("INR " + addtional);
        table.addCell("(E) Customer payment at OTA\n Customer Pre-Payment includes discount coupons offered by OTA or payments made through Wallet.");
        table.addCell("INR " + customer);
        table.addCell("Customer To Pay At Hotel (A + D - E)");
        table.addCell("INR " + dfs.format(customerToHotel));
        table.addCell("Amount To Be Paid By OTA (E - B)");
        table.addCell("INR " + dfs.format(otaToHotelPay));
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
                        chainsList = response.body();

                        if (response.code() == 200) {
                            if (chainsList != null && chainsList.size() != 0) {
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


                    }

                    @Override
                    public void onFailure(Call<ArrayList<HotelDetails>> call, Throwable t) {
                        System.out.println("Failed");


                        Toast.makeText(BillDetails.this, "Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    public void getContactByHotelId(final int id) {
       /* final ProgressDialog dialog = new ProgressDialog(BillDetails.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
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

                        ArrayList<ContactDetails> contactResponse = response.body();
                       /* if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }*/
                        if (response.code() == 200 && contactResponse != null && contactResponse.size() != 0) {
                            final ContactDetails contactInfo = contactResponse.get(contactResponse.size() - 1);


                            mEmail.setText(contactInfo.getHotelEmail());


                        } else {
                           /* hotelMob =  "No Mobile";
                            hotelPhone =  "No Phone";
                            hotelEmail =  "No Email";*/
                            mEmail.setText("");
                        }


                    }

                    @Override
                    public void onFailure(Call<ArrayList<ContactDetails>> call, Throwable t) {
                        System.out.println("Contact failed");
                        Toast.makeText(BillDetails.this, "Failed Due to: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                      /*  if(dialog != null && dialog.isShowing())
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
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Bookings1 dto = response.body();
                            if (dto != null) {

                                Toast.makeText(BillDetails.this, "Booking done successfully", Toast.LENGTH_SHORT).show();
                                //sendEmailattache();
                                FireBaseModel fm = new FireBaseModel();
                                fm.setSenderId("415720091200");
                                fm.setServerId("AIzaSyA56O6PwNfegBxT9Om8PDWfqfmSU9SUFL8");
                                fm.setHotelId(hotelId);
                                fm.setTitle("New Booking from Zingo Hotels");
                                fm.setMessage("Congrats! You got one new booking for "+nights +" nights from "+cit+" to "+cot+"\nBooking Number:"+dto.getBookingNumber());
                                //registerTokenInDB(fm);
                                sendNotification(fm);
                                /*Intent quick = new Intent(BillDetails.this, BillDetails.class);
                                startActivity(quick);*/
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

    public void sendNotification(final FireBaseModel fireBaseModel) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(BillDetails.this);
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

                            Toast.makeText(BillDetails.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();



                           //sendEmailattache();
                            onShareClick();


                        } else {

                            Toast.makeText(BillDetails.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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
        progressDialog.setTitle("please wait..");
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
                            ArrayList<Traveller> list = response.body();

                            if (list.size()!=0) {
                                 dtos = list.get(0);

                                if (dtos != null) {
                                    travellerIid = dtos.getTravellerId();
                                    updateTraveller(travellerIid);
                                   
                                }
                            }else{
                                //travellerIid = 0;
                                addTraveler();
                                
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
        dto.setFirstName(guest);
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
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();

                            Traveller dto = response.body();
                            System.out.println("Response Traveller==="+response.body());

                            if (dto != null) {
                                travellerIid  = dto.getTravellerId();
                                setData();

                            }
//                           


                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(BillDetails.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
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
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();

                            Traveller dto = response.body();
                            System.out.println("Response Traveller==="+response.body());

                            if (dto != null) {

                                setData();
                            }
                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(BillDetails.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
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
        List<Intent> intentShareList = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(shareIntent, 0);

        for (ResolveInfo resInfo : resolveInfoList) {
            String packageName = resInfo.activityInfo.packageName;
            String name = resInfo.activityInfo.name;


            if (packageName.contains("android.email")||packageName.contains("whatsapp")) {


                if(packageName.contains("android.email")){
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
                    intentShareList.add(emailIntent);
                }else{
                    Intent intent = new Intent();
                    // intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("application/pdf");

                    File roots = Environment.getExternalStorageDirectory();
                    String pathToMyAttachedFiles = "/BillGenerate/Pdf/" + csvFile;
                    File files = new File(roots, pathToMyAttachedFiles);
                    if (!files.exists() || !files.canRead()) {
                        return;
                    }
                    Uri uris = Uri.fromFile(files);
                    intent.putExtra(Intent.EXTRA_STREAM, uris);
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
    }
}




