package app.zingo.com.billgenerate;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import app.zingo.com.billgenerate.Model.BillDataBase;
import app.zingo.com.billgenerate.Model.DataBaseHelper;
import app.zingo.com.billgenerate.Model.Permission;
import app.zingo.com.billgenerate.Model.PlanDataBase;
import app.zingo.com.billgenerate.Model.RoomDataBase;

public class BillDetails extends AppCompatActivity {

    Spinner mRoomCount, mPayment;
    EditText mLocation, mCity, mGuest, mMobile, mProperty, mRoomType, mRate,
            mGuestCount, mDesc, mTotal, mBooking, mZingo, mBookingID, mEmail, mOTA;
    TextView mBook, mCID, mCOD;
    Button mSave;

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
    String property, email, ota, city, location, guest, mobile, bdate, cit, cot, rooms, roomNum, count, plans, payment, desc, total, booking, zingo;
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

        mProperty = (EditText) findViewById(R.id.bill_property_name);
        mRoomType = (EditText) findViewById(R.id.bill_property_room);
        mRate = (EditText) findViewById(R.id.bill_property_rate);
        mRoomCount = (Spinner) findViewById(R.id.bill_property_room_num);
        mPayment = (Spinner) findViewById(R.id.bill_property_payment);
        mLocation = (EditText) findViewById(R.id.bill_property_location);
        mEmail = (EditText) findViewById(R.id.bill_property_emal);
        mBookingID = (EditText) findViewById(R.id.bill_booking_id);
        mCity = (EditText) findViewById(R.id.bill_property_city);
        mGuest = (EditText) findViewById(R.id.bill_guest_name);
        mMobile = (EditText) findViewById(R.id.bill_guest_mobile);
        mGuestCount = (EditText) findViewById(R.id.bill_property_guest_num);
        mDesc = (EditText) findViewById(R.id.bill_plan_inclusion);
        mTotal = (EditText) findViewById(R.id.bill_property_amount);
        mBooking = (EditText) findViewById(R.id.bill_booking_com);
        mZingo = (EditText) findViewById(R.id.bill_zingo_com);
        mOTA = (EditText) findViewById(R.id.bill_booking_ota);
        mBook = (TextView) findViewById(R.id.bill_property_booking);
        mCID = (TextView) findViewById(R.id.bill_property_checkiin);
        mCOD = (TextView) findViewById(R.id.bill_property_checkout);
        mSave = (Button) findViewById(R.id.send_email);

        bill = new BillDataBase(this);


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

    }

    public void validate() {

        property = mProperty.getText().toString();
        rooms = mRoomType.getText().toString();
        plans = mRate.getText().toString();
        roomNum = mRoomCount.getSelectedItem().toString();
        payment = mPayment.getSelectedItem().toString();
        bookingID = mBookingID.getText().toString();
        location = mLocation.getText().toString();
        city = mCity.getText().toString();
        guest = mGuest.getText().toString();
        mobile = mMobile.getText().toString();
        email = mEmail.getText().toString();
        count = mGuestCount.getText().toString();
        desc = mDesc.getText().toString();
        total = mTotal.getText().toString();
        booking = mBooking.getText().toString();
        zingo = mZingo.getText().toString();
        bdate = mBook.getText().toString();
        cit = mCID.getText().toString();
        cot = mCOD.getText().toString();
        ota = mOTA.getText().toString();
        System.out.println("Print"+String.valueOf(path));

        if (location == null || location.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        } else if (city == null || city.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        } else if (guest == null || guest.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        } else if (mobile == null || mobile.isEmpty()) {
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        } else if (count == null || count.isEmpty()) {
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
        } else {

            //createPdf();
            boolean isfilecreated=  createPdf();
            System.out.println("oooo"+isfilecreated);
            if(isfilecreated)
            {
                sendEmailattache();
            }
           /* if (bill.insertBILL(bookingID, property, city, location, guest, mobile, bdate, cit, cot, rooms, roomNum, count, plans, payment, desc, total, booking, zingo)) {



            } else {
                Toast.makeText(getApplicationContext(), "Could not Insert Bill", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }*/
        }


    }

    private void sendEmailattache() {
        String[] mailto = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Hotel Partner,\n" +
                "We Thank you for your continued support in ensuring the highest level of service Standards. \n" +
                "\n" +
                "Please find the attached reservation for you.");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "/BillGenerate/Pdf/"+csvFile;
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

    private boolean createPdf() {

        document =null;

        try {

            File sd = Environment.getExternalStorageDirectory();
            csvFile = System.currentTimeMillis() + ".pdf";
            File directory = new File(sd.getAbsolutePath()+"/BillGenerate/Pdf");
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
        }finally {
            if(document != null)
            {
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
                            "#702, 6th A Cross, Behind BDA Complex, Above Kanti Sweets, Koramangala 3rd Block, Blore-560034\n" +
                            "                                                                        www.Zingohotels.com";
            String note = "Under no circumstances must you charge guest for services listed on this voucher!\n" + "Only payments for extra services are to be collected from clients\n" + "Hotel shall issue invoice to the customer/guests, as and when required by the customer/Guest";
            addChild(new Paragraph("                                   Lucida Hospitality Pvt Ltd", catFont));
            //addImage(paragraph);
            addEmptyLine(paragraph, 1);
            addChild(new Paragraph("                        Mob: +91- 7065 651 651 | Email- hello@zingohotels.com", subFont));
            // addChild(new Paragraph("Email- hello@zingohotels.com",subFont));


            //paragraph = new Paragraph(text,smallBold);
            addEmptyLine(paragraph, 2);
            paragraph.add(new Paragraph("Booking ID: " + bookingID, smallBolds));
            paragraph.add(new Paragraph("Booking Source: " + ota, smallBolds));
            addEmptyLine(paragraph,1);
            paragraph.add(new Paragraph(text, smallBold));
            addEmptyLine(paragraph, 2);
            createTables(paragraph);
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

        PdfPCell c1 = new PdfPCell(new Phrase("Description",catFontw));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Details",catFontw));
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
        table.addCell("TOTAL AMOUNT");
        table.addCell("INR " + total);
        table.addCell("OTA COMMISSION");
        table.addCell("INR " + booking);
        table.addCell("ZINGOHOTELS.COM COMMISION");
        table.addCell("INR " + zingo);

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

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }*/

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

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");


                        if (tv.equals(mBook)) {

                            try {
                                Date fdate = simpleDateFormat.parse(date1);

                                from = simpleDateFormat.format(fdate);

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
                                to = simpleDateFormat.format(tdate);
                                System.out.println("To = " + to);
                                tv.setText(to);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (tv.equals(mCOD)) {
                            //to = date1;
                            try {
                                Date tdate = simpleDateFormat.parse(date1);
                                to = simpleDateFormat.format(tdate);
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

    /*public void getProperty(){

        final Cursor cursor = dbHelper.getAllProperty();
       // propertyN = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PROPERTY_COLUMN_NAME));
        String [] columns = new String[] {
                DataBaseHelper.PROPERTY_COLUMN_NAME
        };

        int [] widgets = new int[] {
                R.id.property_spinner_item
        };

        //String uname = cursor.getString(cursor.getColumnIndex("name"));

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.property_spinner_adapter,
                cursor, columns, widgets, 0);
       // mProperty.setAdapter(cursorAdapter);
    }

    public void getRoom(){

        final Cursor cursor = room.getAllROOM();
        String [] columns = new String[] {
                RoomDataBase.ROOM_COLUMN_NAME
        };
        int [] widgets = new int[] {
                R.id.property_spinner_item
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.property_spinner_adapter,
                cursor, columns, widgets, 0);
        mRoomType.setAdapter(cursorAdapter);
    }

    public void getPlan(){

        final Cursor cursor = plan.getAllPLAN();
        String [] columns = new String[] {
                PlanDataBase.PLAN_COLUMN_NAME

        };
        int [] widgets = new int[] {
                R.id.property_spinner_item,
               // R.id.bill_plan_inclusion
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.property_spinner_adapter,
                cursor, columns, widgets, 0);
        mRate.setAdapter(cursorAdapter);
    }*/

   /* public void getPlanDesc(String name){

        Cursor rs = plan.getPLAN(name);
        rs.moveToFirst();
        String desc = rs.getString(rs.getColumnIndex(PlanDataBase.PLAN_COLUMN_DESC));
        if (!rs.isClosed()) {
            rs.close();
        }

        mDesc.setText(desc);
        mDesc.setFocusable(false);
        mDesc.setClickable(false);

    }

    public void getPropertyDet(String name){

        Cursor rs = dbHelper.getProperty(name);
        rs.moveToFirst();
        String city = rs.getString(rs.getColumnIndex(DataBaseHelper.PROPERTY_COLUMN_CITY));
        String location = rs.getString(rs.getColumnIndex(DataBaseHelper.PROPERTY_COLUMN_LOCATION));
        if (!rs.isClosed()) {
            rs.close();
        }

        mLocation.setText(location);
        mLocation.setFocusable(false);
        mLocation.setClickable(false);

        mCity.setText(city);
        mCity.setFocusable(false);
        mCity.setClickable(false);

    }*/

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
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


}
