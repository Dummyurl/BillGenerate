package app.zingo.com.billgenerate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitiveAnalisysActivity extends AppCompatActivity {

    EditText mHotelName,mPrice;
    TextView mDate,mEnteredHotelName,mLowestPrice,mHighestPrice,mAveragePrice,mMarketLowPrice,mMarketHighPrice,
            mMarketAvaragePrice,mLowestPriceText,mHighestPriceText,mMarketDemand;
    FloatingActionButton fab;
    LinearLayout mCompitionLinearLayout,mRatesLinearlayout,mAvailableLinearLayout;
    Spinner mMarketDemandSpinner;
    Button mGeneratePdf;

    String[] marketDemandArray;
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;
    private BaseFont bfBold;
    private BaseFont bf;
    String csvFile;
    String hotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competitive_activity_main);

        setTitle("Analysis");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkPermission();
        mHotelName = (EditText)findViewById(R.id.ca_enter_hotel_name);
        mPrice = (EditText)findViewById(R.id.ca_enter_hotel_price);
        mCompitionLinearLayout = (LinearLayout)findViewById(R.id.competition_linear_layout);
        mRatesLinearlayout = (LinearLayout)findViewById(R.id.rates_linear_layout);
        mAvailableLinearLayout = (LinearLayout)findViewById(R.id.competition_status_linear_layout);

        mDate = (TextView)findViewById(R.id.today_date);
        mEnteredHotelName = (TextView)findViewById(R.id.entered_hotel_name);
        mLowestPrice = (TextView)findViewById(R.id.hotels_lowest_price);
        mHighestPrice = (TextView)findViewById(R.id.hotels_highest_price);
        mAveragePrice = (TextView)findViewById(R.id.hotels_average_price);
        mMarketLowPrice = (TextView)findViewById(R.id.market_lowest_selling_price);
        mMarketHighPrice = (TextView)findViewById(R.id.market_highest_selling_price);
        mMarketAvaragePrice = (TextView)findViewById(R.id.average_market_selling_price);
        mLowestPriceText = (TextView)findViewById(R.id.lowest_price_text);
        mHighestPriceText = (TextView)findViewById(R.id.highest_price_text);
        mMarketDemand = (TextView)findViewById(R.id.remark_price_text);
        mMarketDemandSpinner= (Spinner)findViewById(R.id.market_demand_spinner);
        mGeneratePdf= (Button) findViewById(R.id.generate_pdf);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        marketDemandArray = getResources().getStringArray(R.array.market_demand_array);
        GeneralAdapter adapter = new GeneralAdapter(CompetitiveAnalisysActivity.this,marketDemandArray);
        mMarketDemandSpinner.setAdapter(adapter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy");
        mDate.setText(simpleDateFormat.format(new Date()));

        hotelName = getIntent().getStringExtra("HotelName");
        final int hotelid = getIntent().getIntExtra("HotelId",0);


        mHotelName.setText(hotelName);
        mHotelName.setEnabled(false);
        mEnteredHotelName.setText(hotelName);
        //createPDF();
        mGeneratePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean isfilecreated = createPDF();
                //System.out.println("is = "+checkcondition());


                boolean isdatapresent = checkcondition();
                if(isdatapresent) {
                    /*FireBaseModel fireBaseModel = new FireBaseModel();
                    fireBaseModel.setHotelId(hotelid);
                    sendNotification(fireBaseModel);*/

                    int i = mCompitionLinearLayout.getChildCount();
                    String message = "";
                    for (int j = 0; j < i; j++) {
                        EditText editText = (EditText) mRatesLinearlayout.getChildAt(j);
                        EditText editText1 = (EditText) mCompitionLinearLayout.getChildAt(j);
                        Spinner spinner = (Spinner)mAvailableLinearLayout.getChildAt(j);
                    //System.out.println();
                        String hotelname = editText1.getText().toString();
                        String rate = editText.getText().toString();
                        String status = spinner.getSelectedItem().toString();

                        message = message+hotelname+","+rate+","+""+":";
                    }

                    message = hotelName+","+mPrice.getText().toString()+","+""+":"+message+"="+mMarketLowPrice.getText().toString()+","+mMarketHighPrice.getText().toString()+","+
                            mMarketAvaragePrice.getText().toString()+","+mMarketDemandSpinner.getSelectedItem().toString()+","+
                            mDate.getText().toString()+"="+mLowestPriceText.getText().toString()+","+
                    mHighestPriceText.getText().toString()+","+mMarketDemand.getText().toString();

                    //System.out.println("Message = "+message);
                    HotelNotification hotelNotification = new HotelNotification();
                    hotelNotification.setHotelId(hotelid);
                    hotelNotification.setMessage(message);
                    hotelNotification.setSenderId(Util.senderId);
                    hotelNotification.setServerId(Util.serverId);
                    hotelNotification.setTitle("Dear "+hotelName+", competative analysis report for the day "+mDate.getText().toString());
                    sendNotification(hotelNotification);

                    /*boolean isfilecreated = createPDF();
                    if(isfilecreated)
                    {
                        File sd = Environment.getExternalStorageDirectory();
                        sendEmail(sd.getAbsolutePath()+"/ZingoCompetitiveAnalysis/"+mEnteredHotelName.getText().toString()+
                                " "+mDate.getText().toString()+ ".pdf");
                    }*/
                }



            }
        });

        //createPDF();

        mHotelName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEnteredHotelName.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onAddField(v);
            }
        });

        mMarketDemandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mMarketDemand.setText("The Current Market Demand in your Area is - "+mMarketDemandSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLowestPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPrice.getText().toString().isEmpty())
                {
                    Toast.makeText(CompetitiveAnalisysActivity.this,"Please enter the price of Hotel",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int i = mRatesLinearlayout.getChildCount();
                    System.out.println("childs = "+i);
                    ArrayList<Integer> rates = new ArrayList<>();
                    ArrayList<Integer> rates1 = new ArrayList<>();
                    ArrayList<String> hotelName = new ArrayList<>();
                    NumberFormat df = new DecimalFormat("##.##");

                    int total = 0;
                    for(int j=0;j<i;j++)
                    {
                        EditText editText = (EditText) mRatesLinearlayout.getChildAt(j);
                        EditText editText1 = (EditText) mCompitionLinearLayout.getChildAt(j);
                    /*System.out.println();*/
                        if(!editText.getText().toString().isEmpty() && !editText1.getText().toString().isEmpty())
                        {
                            if(isNumeric(editText.getText().toString().trim()))
                            {
                                int rate = Integer.parseInt(editText.getText().toString().trim());
                                rates.add(rate);
                                rates1.add(rate);
                                total = total+rate;
                                hotelName.add(editText1.getText().toString().trim());
                            }
                        }
                        else
                        {
                            Toast.makeText(CompetitiveAnalisysActivity.this,"Please Fill the Blank Space",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Collections.sort(rates);
                    if(total != 0)
                    {
                    /*System.out.println(total/i);*/
                        mAveragePrice.setText("Rs. "+(total/i)+"");
                        mMarketAvaragePrice.setText("Rs. "+(total/i)+"");
                    }
                    else
                    {
                        mAveragePrice.setText("Rs. "+0+"");
                        mMarketAvaragePrice.setText("Rs. "+0+"");
                    }
                    if(rates.size() != 0)
                    {
                        if(rates.get(0) != 0)
                        {
                            mLowestPrice.setText("Rs. "+rates.get(0)+"");
                            mMarketLowPrice.setText("Rs. "+rates.get(0)+"");
                        }
                        else
                        {
                            for(int k:rates)
                            {
                                if(k!= 0)
                                {
                                    mLowestPrice.setText("Rs. "+k+"");
                                    mMarketLowPrice.setText("Rs. "+k+"");
                                    break;
                                }
                            }

                        }
                        mHighestPrice.setText("Rs. "+rates.get(rates.size()-1)+"");
                        mMarketHighPrice.setText("Rs. "+rates.get(rates.size()-1)+"");
                        if(!mPrice.getText().toString().isEmpty())
                        {
                            int price = Integer.parseInt(mPrice.getText().toString().trim());
                            if(price > rates.get(0))
                            {
                                double d = (price-rates.get(0));
                                double dd = d/price;
                                /*double per = dd*100;*/
                                double per = dd*100;
                                int pos = rates1.indexOf(rates.get(0));

                                mLowestPriceText.setText("Your Hotel price is "+df.format(per)+"% higher than "+hotelName.get(pos));

                            }
                            else
                            {
                                int pos = rates1.indexOf(rates.get(0));
                                mLowestPriceText.setText("Your Hotel price is "+0+"% higher than "+hotelName.get(pos));
                            }

                            if(price < rates.get(rates.size()-1))
                            {
                                double d = (rates.get(rates.size()-1)-price);
                                double dd = d/rates.get(rates.size()-1);
                                double per = dd*100;
                                int pos = rates1.indexOf(rates.get(rates.size()-1));
                                mHighestPriceText.setText("Your Hotel price is "+df.format(per)+"% lower than "+hotelName.get(pos));

                            }
                            else
                            {
                                int pos = rates1.indexOf(rates.get(rates.size()-1));
                                mHighestPriceText.setText("Your Hotel price is "+0+"% lower than "+hotelName.get(pos));
                                //mHighestPriceText.setText("Your Hotel price is "+0+"% lower than ABC hotel");
                            }
                            //System.out.println(d/100);
                        }
                        mMarketDemand.setText("The Current Market Demand in your Area is - "+mMarketDemandSpinner.getSelectedItem().toString());
                    }
                }

                /*System.out.println("Low = "+rates.get(0));
                System.out.println("high = "+rates.get(rates.size()-1));*/
            }
        });

    }




    public static boolean isNumeric(String str)
    {
        return str.matches("^(?:(?:\\-{1})?\\d+(?:\\.{1}\\d+)?)$");
    }

    public boolean checkcondition()
    {
        int i = mRatesLinearlayout.getChildCount();
        if(mEnteredHotelName.getText().toString().isEmpty())
        {
            Toast.makeText(CompetitiveAnalisysActivity.this,"Please enter hotel name",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mPrice.getText().toString().isEmpty())
        {
            Toast.makeText(CompetitiveAnalisysActivity.this,"Please enter the price of Hotel",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(i != 0) {





            if(i != 0)
            {
                for (int j = 0; j < i; j++) {
                    EditText editText = (EditText) mRatesLinearlayout.getChildAt(j);
                    EditText editText1 = (EditText) mCompitionLinearLayout.getChildAt(j);
                    /*System.out.println();*/
                    if (editText.getText().toString().isEmpty() || editText1.getText().toString().isEmpty()) {
                        Toast.makeText(CompetitiveAnalisysActivity.this, "Please Fill the Blank Space", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

                if(mLowestPrice.getText().toString().isEmpty())
                {
                    Toast.makeText(CompetitiveAnalisysActivity.this, "Lowest price is not present", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if(mHighestPrice.getText().toString().isEmpty())
                {
                    Toast.makeText(CompetitiveAnalisysActivity.this, "Highest price is not present", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if(mAveragePrice.getText().toString().isEmpty())
                {
                    Toast.makeText(CompetitiveAnalisysActivity.this, "Average price is not present", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {

                    return true;
                }


        }
        else
        {
            Toast.makeText(CompetitiveAnalisysActivity.this, "Please add the competitive hotels", Toast.LENGTH_SHORT).show();
            return false;
        }
        //return true;

    }

    public void sendNotification(final HotelNotification notification)
    {
        final ProgressDialog dialog = new ProgressDialog(CompetitiveAnalisysActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                HotelOperations hotelOperations = Util.getClient().create(HotelOperations.class);
                Call<ArrayList<String>> notifyResponse = hotelOperations.sendNotification(Util.key,notification);
                notifyResponse.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());
                        System.out.println(response.body());
                        if(response.code() == 200 || response.code() == 201 || response.code() == 204)
                        {
                            if(response.body() != null)
                            {

                                    boolean isfilecreated = createPDF();
                                    if(isfilecreated)
                                    {
                                        File sd = Environment.getExternalStorageDirectory();
                                        sendEmail(sd.getAbsolutePath()+"/ZingoCompetitiveAnalysis/"+mEnteredHotelName.getText().toString()+
                                                " "+mDate.getText().toString()+ ".pdf");
                                    }
                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(notification.getTitle());
                                nf.setNotificationFor(notification.getMessage());
                                nf.setHotelId(notification.getHotelId());
                                savenotification(nf);
                                //}
                            }

                        }
                        else
                        {
                            Toast.makeText(CompetitiveAnalisysActivity.this,response.message()+" == ",Toast.LENGTH_SHORT).show();
                            System.out.println("message  "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        System.out.println("message == "+t.getMessage());
                        Toast.makeText(CompetitiveAnalisysActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void savenotification(final NotificationManager notification) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                //String auth_string = Util.getToken(context);
                HotelOperations travellerApi = Util.getClient().create(HotelOperations.class);
                Call<NotificationManager> response = travellerApi.saveNotification(Util.key,notification);

                response.enqueue(new Callback<NotificationManager>() {
                    @Override
                    public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {


                        System.out.println(response.code());
                        try{
                            if(response.code() == 200)
                            {
                                if(response.body() != null)
                                {



                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<NotificationManager> call, Throwable t) {

                    }
                });
            }
        });
    }


    public void onAddField() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View rowView = inflater.inflate(R.layout.competition_linear_layout_, null);
        // Add the new row before the add field button.

        mCompitionLinearLayout.addView(rowView);
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View rowView1 = inflater1.inflate(R.layout.competition_linear_layout_, null);
        mRatesLinearlayout.addView(rowView1);

        String[] avail = getResources().getStringArray(R.array.status);
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View rowView2 = inflater1.inflate(R.layout.status_layout, null);
        Spinner mSpinner = rowView2.findViewById(R.id.property_available_status);
        GeneralAdapter adapter = new GeneralAdapter(CompetitiveAnalisysActivity.this,avail);
        mSpinner.setAdapter(adapter);
        mAvailableLinearLayout.addView(rowView2);
        // Add the new row before the add field button.

        /*((EditText)rowView1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((EditText)rowView1).setText("Rs. "+s);
            }
        });*/
    }
    public void removeView() {
        /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.competition_linear_layout_, null);
        // Add the new row before the add field button.

        mCompitionLinearLayout.addView(rowView);
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView1 = inflater1.inflate(R.layout.competition_linear_layout_, null);
        // Add the new row before the add field button.
        mRatesLinearlayout.addView(rowView1);*/
        int no = mCompitionLinearLayout.getChildCount();
        if(no >0)
        {
            /*System.out.println(mCompitionLinearLayout.getChildAt(no-1));*/
            mCompitionLinearLayout.removeView(mCompitionLinearLayout.getChildAt(no-1));
            mRatesLinearlayout.removeView(mRatesLinearlayout.getChildAt(no-1));
            mAvailableLinearLayout.removeView(mAvailableLinearLayout.getChildAt(no-1));
        }
        else
        {
            Toast.makeText(CompetitiveAnalisysActivity.this,"No views available",Toast.LENGTH_SHORT).show();
        }
        /*((EditText)rowView1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((EditText)rowView1).setText("Rs. "+s);
            }
        });*/
    }


    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(CompetitiveAnalisysActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(CompetitiveAnalisysActivity.this, Manifest.permission.CAMERA))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(CompetitiveAnalisysActivity.this,
                        new String[]{

                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_RESULT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(CompetitiveAnalisysActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_RESULT);


            }
            return false;
        } else {
            return true;
        }
    }

    private boolean createPDF (){

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            File sd = Environment.getExternalStorageDirectory();
            csvFile = mEnteredHotelName.getText().toString()+" "+mDate.getText().toString()+ ".pdf";

            File directory = new File(sd.getAbsolutePath()+"/ZingoCompetitiveAnalysis/");
            //create directory if not exist
            if (!directory.exists() && !directory.isDirectory()) {
                directory.mkdirs();
            }

            //pdfFile = sd.getAbsolutePath()+"/Sea Pearl/Pdf/Checkout/"+csvFile;

            File file = new File(directory, csvFile);
            //String path = "docs/" + pdfFilename;
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
                // beginPage = false;
                generateLayout(doc, cb);
                /*generateHeader(doc, cb);
                generateDetail(doc, cb);*/
                generateDetail(doc, cb);
                y = 615;
            }

            /*for(int i=0; i < 100; i++ ){
                if(beginPage){
                    beginPage = false;
                    generateLayout(doc, cb);
                    //generateHeader(doc, cb);
                    y = 615;
                }
                //generateDetail(doc, cb, i, y);
                y = y - 15;
                if(y < 50){
                   // printPageNumber(cb);
                    doc.newPage();
                    beginPage = true;
                }
            }*/
            // createTables(cb);
            //printPageNumber(cb);

            //sendEmail(sd.getAbsolutePath()+"/ZingoCompetitiveAnalysis/");
            return true;

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
            //cb.set(244,67,54);
            // Invoice Header box layout
            cb.rectangle(30,660,200,90);
            //cb.setColorFill(BaseColor.BLUE);
            cb.moveTo(30,690);
            cb.lineTo(230,690);
            cb.moveTo(30,720);
            cb.lineTo(230,720);
            cb.stroke();

            /*cb.setLineWidth(1f);*/
            // Invoice hotel name layout
            cb.rectangle(30,600,300,30);
            cb.moveTo(30,630);
            cb.lineTo(330,630);
            cb.rectangle(330,600,100,30);
            cb.moveTo(330,630);
            cb.lineTo(360,630);
            cb.stroke();

            //hotel list
            cb.rectangle(30,540,200,30);
            //cb.moveTo(30,510);
            //cb.lineTo(230,510);
            cb.stroke();

            cb.rectangle(230,540,80,30);
            //cb.moveTo(230,510);
            //cb.lineTo(310,510);
            /*cb.stroke();
            cb.rectangle(280,540,80,30);*/
            //cb.moveTo(230,510);
            //cb.lineTo(310,510);
            cb.stroke();
            cb.rectangle(310,540,80,30);
            //cb.moveTo(310,510);
            //cb.lineTo(360,510);
            cb.stroke();

            cb.rectangle(390,540,80,30);
//            cb.moveTo(360,510);
  //          cb.lineTo(410,510);
            cb.stroke();

            cb.rectangle(470,540,80,30);
    //        cb.moveTo(410,510);
      //      cb.lineTo(460,510);
            cb.stroke();

            int y=520;
            int x=520;

            for(int i=0;i<mRatesLinearlayout.getChildCount();i++)
            {
                cb.rectangle(30,y,200,20);
                //        cb.moveTo(410,510);
                //      cb.lineTo(460,510);
                cb.stroke();
                cb.rectangle(230,y,80,20);
                //cb.moveTo(230,510);
                //cb.lineTo(310,510);
                /*cb.stroke();
                cb.rectangle(280,y,80,20);*/
                //cb.moveTo(230,510);
                //cb.lineTo(310,510);
                cb.stroke();
                EditText editText1 = (EditText) mRatesLinearlayout.getChildAt(i);
                EditText editText = (EditText) mCompitionLinearLayout.getChildAt(i);
                Spinner sp = (Spinner) mAvailableLinearLayout.getChildAt(i);
                createContent(cb,35,y+5, editText.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
                //createContent(cb,235,y+5, sp.getSelectedItem().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
                createContent(cb,235,y+5, editText1.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
                //createContent(cb,285,y+5, editText1.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
                y = y-20;
                //x = y;
            }

            cb.rectangle(310,y+20,80,x-y);
            cb.stroke();
            createContent(cb,330,(y+20)+((x-y)/2)-10,mLowestPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            cb.rectangle(390,y+20,80,x-y);
            cb.stroke();
            createContent(cb,410,(y+20)+((x-y)/2)-10,mHighestPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            cb.rectangle(470,y+20,80,x-y);
            cb.stroke();
            createContent(cb,490,(y+20)+((x-y)/2)-10,mAveragePrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            System.out.println("y value = "+y);


            //market lowest elling price
            cb.rectangle(100,270,300,30);
            cb.stroke();
            cb.rectangle(300,270,100,30);
            cb.stroke();

            //Market highest selling price
            cb.rectangle(100,240,300,30);
            cb.stroke();
            cb.rectangle(300,240,100,30);
            cb.stroke();

            //Market average price
            cb.rectangle(100,210,300,30);
            cb.stroke();
            cb.rectangle(300,210,100,30);
            cb.stroke();

            //cb.rectangle();

            /*PdfPTable table;
            PdfPCell cell = new PdfPCell();
            for (int i = 1; i <= 5; i++)
                cell.addElement(new Paragraph("Line " + i));
            *//*table = new PdfPTable(1);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            addTable(doc, cb, table);
            doc.newPage();*//*
            table = new PdfPTable(1);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);
            addTable(doc, cb, table);*/
            // step 5

            /*for(int i=0;i<mCompitionLinearLayout.getChildCount();i++)
            {

            }*/

            //add the images
            InputStream ims = getAssets().open("app_logo.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image companyLogo = Image.getInstance(stream.toByteArray());

            // Image image = Image.getInstance(stream.toByteArray());
            // Image companyLogo = Image.getInstance("logo.png");
            companyLogo.setAbsolutePosition(450,640);
            companyLogo.scalePercent(8);
            doc.add(companyLogo);

        }

       /* catch (DocumentException dex){
            dex.printStackTrace();
        }*/
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /*public void addTable(Document document, PdfContentByte canvas, PdfPTable table) throws DocumentException {
        Rectangle pagedimension = new Rectangle(36, 36, 559, 806);
        drawColumnText(document, canvas, pagedimension, table, true);
        Rectangle rect;
        if (ColumnText.hasMoreText(status)) {
            rect = pagedimension;
        }
        else {
            rect = new Rectangle(36, 36, 559, 806 - ((y_position - 36) / 2));
        }
        drawColumnText(document, canvas, rect, table, false);
    }

    public void drawColumnText(Document document, PdfContentByte canvas, Rectangle rect, PdfPTable table, boolean simulate) throws DocumentException {
        ColumnText ct = new ColumnText(canvas);
        //ct.setSimpleColumn(rect);
        ct.setSimpleColumn(300f, 500f, 430f, 780f);
        ct.addElement(table);
        status = ct.go(simulate);
        y_position = ct.getYLine();
        while (!simulate && ColumnText.hasMoreText(status)) {
            document.newPage();
            //ct.setSimpleColumn(rect);
            ct.setSimpleColumn(300f, 500f, 430f, 780f);
            status = ct.go(simulate);
        }
    }

    private int status = ColumnText.START_COLUMN;
    private float y_position = 0;*/
    private void generateDetail(Document doc, PdfContentByte cb) {

        try {
            createHeadingsTitle(cb,35,730, mEnteredHotelName.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.RED);
            createHeadingsTitle(cb,100,310, "Competitive Overview",PdfContentByte.ALIGN_LEFT,BaseColor.RED);
            createHeadingsTitle(cb,35,550, "Hotel Name",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,235,550, "Rates",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            //createHeadingsTitle(cb,285,550, "Rates",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,315,550, "LP",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,395,550, "HP",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,475,550, "Average",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);


            createContent(cb,35,613, mEnteredHotelName.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,335,613,"Rs. "+mPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            //market selling price
            createContent(cb,110,280, "Market Lowest Selling Price",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,310,280, mMarketLowPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            //Market highest selling price
            createContent(cb,110,250, "Market Highest Selling Price",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,310,250, mMarketHighPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            //Market average price
            createContent(cb,110,220, "Average Market Selling Price",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,310,220, mMarketAvaragePrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            createContent(cb,30,190, mLowestPriceText.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,30,170, mHighestPriceText.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,30,150, mMarketDemand.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            createHeadingsTitle(cb,35,700, mDate.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createHeadingsTitle(cb,35,670, "Performance Intelligence Report",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,200,20,"This is computer generated analysis",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            //createContent(cb,300,525, "₹ ",PdfContentByte.ALIGN_RIGHT);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void createContent(PdfContentByte cb, float x, float y, String text, int align,BaseColor bc){


        cb.beginText();
        cb.setFontAndSize(bfBold, 10);
        cb.setColorFill(bc);

        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }
    private void createContent1(PdfContentByte cb, float x, float y, String text, int align,BaseColor bc){


        cb.beginText();
        cb.setFontAndSize(bf, 10);
        cb.setColorFill(bc);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    /*private void createHeadingsTitle(PdfContentByte cb, float x, float y, String text,int align){


        cb.beginText();
        cb.setFontAndSize(bfBold, 12);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }*/

    private void createHeadingsTitle(PdfContentByte cb, float x, float y, String text, int align, BaseColor bc){


        cb.beginText();
        cb.setFontAndSize(bfBold, 12);
        cb.setTextMatrix(x,y);
        cb.setColorFill(bc);
        cb.showText(text.trim());
        cb.endText();

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


    private void sendEmail(String s) {
        File file = new File(s);

        if(file.exists() )
        {
            //File[] files = file.listFiles();
            //System.out.println("File = "+files[files.length -1].getAbsolutePath());
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
            //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
            //emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
            //File root = Environment.getExternalStorageDirectory();
            //String pathToMyAttachedFile = "/BookingReport/AllBookings.xls";
            /*File file1 = files[files.length-1];//new File(root, pathToMyAttachedFile);
            if (!file1.exists() || !file1.canRead()) {
                return;
            }*/
            Uri uri = Uri.fromFile(file);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.submit_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.action_add:
                //logout();
                onAddField();
                return true;
            case R.id.action_delete:
                //logout();
                removeView();
                return true;
            case android.R.id.home:
                // app icon action bar is clicked; go to parent activity

                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
