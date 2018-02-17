package app.zingo.com.billgenerate;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import app.zingo.com.billgenerate.Model.BillDataBase;
import app.zingo.com.billgenerate.Model.DataBaseHelper;
import app.zingo.com.billgenerate.Model.PlanDataBase;
import app.zingo.com.billgenerate.Model.RoomDataBase;

public class BillDetails extends AppCompatActivity {
    
    Spinner mProperty,mRoomType,mRoomCount,mRate,mPayment;
    EditText mLocation,mCity,mGuest,mMobile,
            mGuestCount,mDesc,mTotal,mBooking,mZingo;
    TextView mBook,mCID,mCOD;
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

    public static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    File destination = new File(Environment.getExternalStorageDirectory(),
            System.currentTimeMillis() + ".pdf");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        
        mProperty = (Spinner) findViewById(R.id.bill_property_name);
        mRoomType = (Spinner) findViewById(R.id.bill_property_room);
        mRate = (Spinner) findViewById(R.id.bill_property_rate);
        mRoomCount = (Spinner) findViewById(R.id.bill_property_room_num);
        mPayment = (Spinner) findViewById(R.id.bill_property_payment);
        mLocation = (EditText) findViewById(R.id.bill_property_location);
        mCity = (EditText) findViewById(R.id.bill_property_city);
        mGuest = (EditText) findViewById(R.id.bill_guest_name);
        mMobile = (EditText) findViewById(R.id.bill_guest_mobile);
        mGuestCount = (EditText) findViewById(R.id.bill_property_guest_num);
        mDesc = (EditText) findViewById(R.id.bill_plan_inclusion);
        mTotal = (EditText) findViewById(R.id.bill_property_amount);
        mBooking = (EditText) findViewById(R.id.bill_booking_com);
        mZingo = (EditText) findViewById(R.id.bill_zingo_com);
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

        getProperty();
        getRoom();
        getPlan();
        fn_permission();

        mRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String plan = mRate.getSelectedItem().toString();
               // getPlanDesc(plan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = mProperty.getSelectedItem().toString();
                //getPropertyDet(name);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();
            }
        });

    }

    public void validate(){

        String property = mProperty.getSelectedItem().toString();
        String room = mRoomType.getSelectedItem().toString();
        String plan = mRate.getSelectedItem().toString();
        String roomNum = mRoomCount.getSelectedItem().toString();
        String payment = mPayment.getSelectedItem().toString();
        String location = mLocation.getText().toString();
        String city = mCity.getText().toString();
        String guest = mGuest.getText().toString();
        String mobile = mMobile.getText().toString();
        String count = mGuestCount.getText().toString();
        String desc = mDesc.getText().toString();
        String total = mTotal.getText().toString();
        String booking = mBooking.getText().toString();
        String zingo = mZingo.getText().toString();
        String bdate = mBook.getText().toString();
        String cit = mCID.getText().toString();
        String cot = mCOD.getText().toString();

        if(location == null || location.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(city == null || city.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(guest == null || guest.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(mobile == null || mobile.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(count == null || count.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(desc == null || desc.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(total == null || total.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(booking == null || booking.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(zingo == null || zingo.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(bdate == null || bdate.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(cit == null || cit.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else if(cot == null || cot.isEmpty()){
            Toast.makeText(BillDetails.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
        }else{
            if(bill.insertBILL(property,city,location,guest,mobile,bdate,cit,cot,room,roomNum,count,plan,payment,desc,total,booking,zingo)) {
                createPdf();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert Bill", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

    }

    private void createPdf(){

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(destination));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /*private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


//        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        //canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".pdf");
       *//* String targetPdf = "/0/test.pdf";
        File filePath = new File(targetPdf);*//*
        try {
            document.writeTo(new FileOutputStream(destination));
           // btn_generate.setText("Check PDF");
            boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }
*/

   /* public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "newFile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont= new Font(Font.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        viewPdf("newFile.pdf", "Dir");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(TableActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }*/



    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void openDatePicker(final TextView tv) {
        // Get Current Date



        final Calendar c = Calendar.getInstance();
        int mYear  = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay   = c.get(Calendar.DAY_OF_MONTH);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String from,to;
                        Log.d("Date", "DATE SELECTED "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year,monthOfYear,dayOfMonth);


                        String date1 = (monthOfYear + 1)  + "/" + dayOfMonth + "/" + year;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");


                        if (tv.equals(mBook)){

                            try {
                                Date fdate = simpleDateFormat.parse(date1);

                                from = simpleDateFormat.format(fdate);

                                System.out.println("To = "+from);
                                tv.setText(from);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //


                        }else if(tv.equals(mCID)) {
                            //to = date1;
                            try {
                                Date tdate = simpleDateFormat.parse(date1);
                                to = simpleDateFormat.format(tdate);
                                System.out.println("To = "+to);
                                tv.setText(to);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else if(tv.equals(mCOD)) {
                            //to = date1;
                            try {
                                Date tdate = simpleDateFormat.parse(date1);
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

    }

    public void getProperty(){

        final Cursor cursor = dbHelper.getAllProperty();
        String [] columns = new String[] {
                DataBaseHelper.PROPERTY_COLUMN_NAME
        };
        int [] widgets = new int[] {
                R.id.property_spinner_item
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.property_spinner_adapter,
                cursor, columns, widgets, 0);
        mProperty.setAdapter(cursorAdapter);
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
    }

    public void getPlanDesc(String name){

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

    }
}
