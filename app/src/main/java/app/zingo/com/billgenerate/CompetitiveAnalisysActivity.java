package app.zingo.com.billgenerate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.google.android.gms.maps.model.Marker;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
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
import java.util.List;

import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Utils.Constatnts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitiveAnalisysActivity extends AppCompatActivity {

    EditText mHotelName,mPrice;
    TextView mDate,mEnteredHotelName,mLowestPrice,mHighestPrice,mAveragePrice,mMarketLowPrice,mMarketHighPrice,
            mMarketAvaragePrice,mLowestPriceText,mHighestPriceText,mMarketDemand,mAvgComparision,mMessageText;
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
    String[]competitorsHotelList;
    int hotelListSize;
    boolean notificationSend = false;
    String email="";
     int hotelid,noofrooms;
    int occupancy = 0;

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
        mAvgComparision = (TextView)findViewById(R.id.average_price_comparision);
        mHighestPriceText = (TextView)findViewById(R.id.highest_price_text);
        mMarketDemand = (TextView)findViewById(R.id.remark_price_text);
        mMessageText = (TextView)findViewById(R.id.message_line);
        mMessageText.setVisibility(View.GONE);
        mMarketDemandSpinner= (Spinner)findViewById(R.id.market_demand_spinner);
        mGeneratePdf= (Button) findViewById(R.id.generate_pdf);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        getNotification();

        marketDemandArray = getResources().getStringArray(R.array.market_demand_array);
        GeneralAdapter adapter = new GeneralAdapter(CompetitiveAnalisysActivity.this,marketDemandArray);
        mMarketDemandSpinner.setAdapter(adapter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy");
        mDate.setText(simpleDateFormat.format(new Date()));

        hotelName = getIntent().getStringExtra("HotelName");
        hotelid = getIntent().getIntExtra("HotelId",0);
        String s = getIntent().getStringExtra("Rooms");
        if(s != null && !s.trim().isEmpty())
        {
            noofrooms = Integer.parseInt(s);
        }

        if(hotelName!=null&&!hotelName.isEmpty()){
            if(hotelName.equalsIgnoreCase("Holiday Homes ")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_hoilday);
                email = getResources().getString(R.string.email_hoilday);

            }else if(hotelName.equalsIgnoreCase("Zingo Nagananda Residency")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_nagananda);
                email = getResources().getString(R.string.email_naga);

            }else if(hotelName.equalsIgnoreCase("RB Hospitality")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_rb);
                email = getResources().getString(R.string.email_rb);

            }else if(hotelName.equalsIgnoreCase("Hotel Ashapura International")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_ashapura);
                email = getResources().getString(R.string.email_ashapura);

            }else if(hotelName.equalsIgnoreCase("SS Lumina Hotel")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_ssl);
                email = getResources().getString(R.string.email_ssl);

            }else if(hotelName.equalsIgnoreCase("Emirates suites")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_emirates);
                email = getResources().getString(R.string.email_emir);

            }else if(hotelName.equalsIgnoreCase("Tranquil Homes")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_tranquil);
                email = getResources().getString(R.string.email_tranquil);

            }else if(hotelName.equalsIgnoreCase("Woodlands Hotel")){

                competitorsHotelList = getResources().getStringArray(R.array.competitor_hotel_woodlands);
                email = getResources().getString(R.string.email_woodlands);

            }
        }

        if(competitorsHotelList!=null&&competitorsHotelList.length!=0){
            hotelListSize = competitorsHotelList.length;

            for(int i=0;i<hotelListSize;i++){
                onAddField(i);
            }
        }


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

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm a");
                    String hotelprices = mPrice.getText().toString();
                    int hotelprice=0;
                    try{
                         hotelprice = Integer.parseInt(hotelprices);
                        if(hotelprice==0){

                            hotelprices = "Sold out";
                        }else{

                            hotelprices = mPrice.getText().toString();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }




                    message = hotelName+","+hotelprices+","+""+":"+message+"="+mMarketLowPrice.getText().toString()+","+mMarketHighPrice.getText().toString()+","+
                            mMarketAvaragePrice.getText().toString()+","+mMarketDemandSpinner.getSelectedItem().toString()+","+
                            simpleDateFormat.format(new Date())+"="+mLowestPriceText.getText().toString()+","+
                    mHighestPriceText.getText().toString()+","+mMarketDemand.getText().toString();

                    //System.out.println("Message = "+message);
                    HotelNotification hotelNotification = new HotelNotification();
                    hotelNotification.setHotelId(hotelid);
                    hotelNotification.setMessage(message);
                    hotelNotification.setSenderId(Util.senderId);
                    hotelNotification.setServerId(Util.serverId);


                    hotelNotification.setTitle("Dear "+hotelName+", competative analysis report for the day "+simpleDateFormat.format(new Date()));


                    if(notificationSend){
                        boolean isfilecreated = createPDF();
                        if(isfilecreated)
                        {
                            File sd = Environment.getExternalStorageDirectory();
                            sendEmail(sd.getAbsolutePath()+"/ZingoCompetitiveAnalysis/"+mEnteredHotelName.getText().toString()+
                                    " "+mDate.getText().toString()+ ".pdf");
                        }

                    }else{
                       sendNotification(hotelNotification);

                      /*  boolean isfilecreated = createPDF();
                        if(isfilecreated)
                        {
                            File sd = Environment.getExternalStorageDirectory();
                            sendEmail(sd.getAbsolutePath()+"/ZingoCompetitiveAnalysis/"+mEnteredHotelName.getText().toString()+
                                    " "+mDate.getText().toString()+ ".pdf");
                        }*/
                    }



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
                    mMessageText.setVisibility(View.VISIBLE);
                    System.out.println("occupancy = "+occupancy);
                    int i = mRatesLinearlayout.getChildCount();
                    int count = i;
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
                            }else{
                                count = count-1;
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
                        mAveragePrice.setText("Rs. "+(total/count)+"");
                        mMarketAvaragePrice.setText("Rs. "+(total/count)+"");
                        double avgPrice = (total/count);
                        double marketDemand = (((i*1.0)-(count*1.0))/(i*1.0))*100;
                        System.out.println("marketDemand = "+marketDemand);
                        if(marketDemand <= 40)
                        {
                            //System.out.println("Low");
                            mMessageText.setText("The Current Market Demand in your Area is - Low ");
                        }
                        else if(marketDemand > 40 && marketDemand <= 60)
                        {
                            //System.out.println("average");
                            mMessageText.setText("The Current Market Demand in your Area is - Average");
                        }
                        else
                        {
                            //System.out.println("high");
                            mMessageText.setText("The Current Market Demand in your Area is - High");
                        }

                        double price = Double.parseDouble(mPrice.getText().toString().trim());
                        double difference,percentage,value;
                        if(avgPrice>price){
                            difference = avgPrice - price;
                            percentage = difference/avgPrice;
                            value = percentage * 100;
                            mAvgComparision.setText("Your Hotel price is Rs. "+df.format(difference)+"("+df.format(value)+"%) lower from Market Average Price.");
                        }else if(avgPrice<price){

                            difference = price - avgPrice;
                            percentage = difference/price;
                            value = percentage * 100;
                            mAvgComparision.setText("Your Hotel price is Rs. "+df.format(difference)+"("+df.format(value)+"%) higher than Market Average Price.");
                        }else if(avgPrice==price){

                            mAvgComparision.setText("Your Hotel price is equal to Market Average Price");
                        }

                        String s = mMessageText.getText().toString();
                        double peroccu = 0;
                        if(occupancy != 0)
                        {
                            peroccu = (((noofrooms-occupancy)*1.0)/(noofrooms*1.0))*100;
                        }
                        if(price == 0)
                        {
                            mMessageText.setText("Congratulations! "+s+",\n"+"Your hotel running sold out with 100% occupancy.");
                        }
                        else if(peroccu >= 60 )//&& avgPrice < price)
                        {
                            mMessageText.setText("Congratulations! "+s+",\n"+"You " +
                                    "are ahead of your competition with other hotels");
                                mMessageText.setText(mMessageText.getText()+" \nand maintaining "+df.format(peroccu)+" % of occupancy");

                        }
                        else
                        {
                            /*mMessageText.setText(s+",\n"+"You " +
                                    "are ahead of your competition with other hotels.");*/
                               // double peroccu = (((noofrooms-occupancy)*1.0)/(noofrooms*1.0))*100;
                                mMessageText.setText(mMessageText.getText()+" \nYou are currently maintaining "+df.format(peroccu)+" % of occupancy");

                        }


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

                                mLowestPriceText.setText("Your Hotel price is "+df.format(per)+"% higher than lowest priced hotel "+hotelName.get(pos));

                            }
                            else
                            {
                                int pos = rates1.indexOf(rates.get(0));
                                mLowestPriceText.setText("Your Hotel price is "+0+"% higher than lowest priced hotel "+hotelName.get(pos));
                            }

                            if(price < rates.get(rates.size()-1))
                            {
                                double d = (rates.get(rates.size()-1)-price);
                                double dd = d/rates.get(rates.size()-1);
                                double per = dd*100;
                                int pos = rates1.indexOf(rates.get(rates.size()-1));
                                mHighestPriceText.setText("Your Hotel price is "+df.format(per)+"% lower than highest priced hotel "+hotelName.get(pos));

                            }
                            else
                            {
                                int pos = rates1.indexOf(rates.get(rates.size()-1));
                                mHighestPriceText.setText("Your Hotel price is "+0+"% lower than highest priced hotel "+hotelName.get(pos));
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




    private void getNotification(){
       /* final ProgressDialog progressDialog = new ProgressDialog(TabMainActivity.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
*/


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                app.zingo.com.billgenerate.WebApis.LoginApi apiService =
                        Util.getClient().create(app.zingo.com.billgenerate.WebApis.LoginApi.class);
                String authenticationString = Util.key;
                Call<ArrayList<NotificationManager>> call = apiService.getNotificationByHotelID(authenticationString,hotelid)/*getRooms()*/;

                call.enqueue(new Callback<ArrayList<NotificationManager>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NotificationManager>> call, Response<ArrayList<NotificationManager>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        try{
                            int statusCode = response.code();
                            System.out.println("occupancy = "+statusCode);
                      /*  if (progressDialog!=null) {
                            progressDialog.dismiss();
                        }*/
                            if (statusCode == 200) {


                                ArrayList<NotificationManager>  list =  response.body();
                                ArrayList<NotificationManager> nfm = new ArrayList<>();

//                            docList = list.get(1).getProfileList();
                                if(list != null && list.size() != 0)
                                {
                                for (int i=0;i<list.size();i++)
                                {
                                    System.out.println("Inventary = "+list.get(i).getNotificationText()+" == "+
                                            list.get(i).getNotificationFor());
                                        if(list.get(i).getNotificationText().equalsIgnoreCase("Inventory Update"))
                                        {
                                            nfm.add(list.get(i));
                                        }
                                }

                                if(nfm != null && nfm.size() != 0)
                                {
                                    System.out.println("occupancy = "+occupancy);
                                    Collections.reverse(nfm);
                                    String s = nfm.get(0).getNotificationFor();
                                    System.out.println("occupancy = "+s);
                                    System.out.println("occupancy = "+nfm.get(nfm.size()-1).getNotificationFor());
                                    if(!s.isEmpty())
                                    {
                                        String[] sp = s.split("-");
                                        if(sp != null && sp.length != 0)
                                        {
                                            String st = sp[1];

                                            if(st != null && !st.isEmpty())
                                            {
                                                String[] sst = st.split(",");

                                                if(sst != null && sst.length != 0)
                                                {
                                                    for (int i=0;i<sst.length;i++)
                                                    {
                                                        int j= Integer.parseInt(sst[i]);
                                                        occupancy = occupancy+j;
                                                        System.out.println("occupancy2 = "+occupancy);
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }



                                }



//                            Object dto = response.body();
//                            listCities.add(dto);



                            }else {

                                //Toast.makeText(TabMainActivity.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<NotificationManager>> call, Throwable t) {
                        // Log error here since request failed
                        /*if (progressDialog!=null)
                            progressDialog.dismiss();*/
                        Log.e("TAG", t.toString());
                    }
                });
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

                                notificationSend = true;

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

        final ProgressDialog dialog = new ProgressDialog(CompetitiveAnalisysActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

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
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }

                        System.out.println(response.code());
                        try{
                            if(response.code() == 200||response.code() == 201||response.code() == 204)
                            {
                                if(dialog != null && dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }
                                if(response.body() != null)
                                {

                                    boolean isfilecreated = createPDF();
                                    if(isfilecreated)
                                    {
                                        File sd = Environment.getExternalStorageDirectory();
                                        sendEmail(sd.getAbsolutePath()+"/ZingoCompetitiveAnalysis/"+mEnteredHotelName.getText().toString()+
                                                " "+mDate.getText().toString()+ ".pdf");
                                    }
                                    Toast.makeText(CompetitiveAnalisysActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<NotificationManager> call, Throwable t) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }


    public void onAddField(final int i) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View rowView = inflater.inflate(R.layout.competition_linear_layout_, null);
        // Add the new row before the add field button.


        mCompitionLinearLayout.addView(rowView);
        if(i!=-1){
            ((EditText)rowView).setText(""+competitorsHotelList[i]);
        }
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
            SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy HHmmss");
            csvFile = mEnteredHotelName.getText().toString()+
                    " "+mDate.getText().toString()+ ".pdf";
            //+" "+format.format(new Date())

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

            ColumnText ct = new ColumnText(cb);
            ct.setSimpleColumn(30f, 590f, 700f, 30f);
            //Font f = new Font();
            Font f = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            f.setColor(new BaseColor(255,0,0));

            Paragraph pz = new Paragraph(new Phrase(18, mMessageText.getText().toString(), f));
            ct.addElement(pz);
            ct.go();

            //hotel list
            cb.rectangle(30,500,220,30);
            //cb.moveTo(30,510);
            //cb.lineTo(230,510);
            cb.stroke();

            cb.rectangle(250,500,100,30);
            //cb.moveTo(230,510);
            //cb.lineTo(310,510);
            /*cb.stroke();
            cb.rectangle(280,540,80,30);*/
            //cb.moveTo(230,510);
            //cb.lineTo(310,510);
            cb.stroke();
            cb.rectangle(350,500,65,30);
            //cb.moveTo(310,510);
            //cb.lineTo(360,510);
            cb.stroke();

            cb.rectangle(415,500,65,30);
//            cb.moveTo(360,510);
            //          cb.lineTo(410,510);
            cb.stroke();

            cb.rectangle(480,500,65,30);
            //        cb.moveTo(410,510);
            //      cb.lineTo(460,510);
            cb.stroke();



            int y=480;
            int x=480;

            for(int i=0;i<mRatesLinearlayout.getChildCount();i++)
            {
                cb.rectangle(30,y,220,20);
                //        cb.moveTo(410,510);
                //      cb.lineTo(460,510);
                cb.stroke();
                cb.rectangle(250,y,100,20);
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
                createContent(cb,255,y+5, editText1.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
                //createContent(cb,285,y+5, editText1.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
                y = y-20;
                //x = y;
            }

            cb.rectangle(350,y+20,65,x-y);
            cb.stroke();
            createContent(cb,355,(y+20)+((x-y)/2)-10,mLowestPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            cb.rectangle(415,y+20,65,x-y);
            cb.stroke();
            createContent(cb,420,(y+20)+((x-y)/2)-10,mHighestPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            cb.rectangle(480,y+20,65,x-y);
            cb.stroke();
            createContent(cb,485,(y+20)+((x-y)/2)-10,mAveragePrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            System.out.println("y value = "+y);


            //market lowest elling price
            cb.rectangle(100,230,300,30);
            cb.stroke();
            cb.rectangle(300,230,100,30);
            cb.stroke();

            //Market highest selling price
            cb.rectangle(100,200,300,30);
            cb.stroke();
            cb.rectangle(300,200,100,30);
            cb.stroke();

            //Market average price
            cb.rectangle(100,170,300,30);
            cb.stroke();
            cb.rectangle(300,170,100,30);
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



    private void generateDetail(Document doc, PdfContentByte cb) {

        try {
            createHeadingsTitle(cb,35,730, mEnteredHotelName.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.RED);
            createHeadingsTitle(cb,100,270, "Competitive Overview",PdfContentByte.ALIGN_LEFT,BaseColor.RED);
            createHeadingsTitle(cb,35,515, "Hotel Name",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,255,515, "Rates",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            //createHeadingsTitle(cb,285,550, "Rates",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,355,515, "Lowest P.",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,420,515, "Highest P.",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);
            createHeadingsTitle(cb,485,515, "Average",PdfContentByte.ALIGN_CENTER,BaseColor.BLACK);


            createContent(cb,35,613, mEnteredHotelName.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,335,613,"Rs. "+mPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            //market selling price
            createContent(cb,110,245, "Market Lowest Selling Price",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,310,245, mMarketLowPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            //Market highest selling price
            createContent(cb,110,215, "Market Highest Selling Price",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,310,215, mMarketHighPrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            //Market average price
            createContent(cb,110,185, "Average Market Selling Price",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,310,185, mMarketAvaragePrice.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);

            ColumnText ct = new ColumnText(cb);
            ct.setSimpleColumn(30f, 150f, 700f, 30f);
            /*Font f = new Font();*/
            Font f = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            f.setColor(new BaseColor(255,0,0));

            Paragraph pz = new Paragraph(new Phrase(18, mLowestPriceText.getText().toString(), f));
            ct.addElement(pz);
            ct.go();

            ColumnText ct1 = new ColumnText(cb);
            ct1.setSimpleColumn(30f, 130f, 700f, 30f);
            /*Font f = new Font();*/
            /*Font f1 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            f.setColor(new BaseColor(255,0,0));*/

            Paragraph pz1 = new Paragraph(new Phrase(18, mHighestPriceText.getText().toString(), f));
            ct1.addElement(pz1);
            ct1.go();

            ColumnText ct2 = new ColumnText(cb);
            ct2.setSimpleColumn(30f, 100, 700f, 30f);
            Font f1 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            f.setColor(new BaseColor(51,51,255));

            Paragraph pz2 = new Paragraph(new Phrase(18, mAvgComparision.getText().toString(), f));
            ct2.addElement(pz2);
            ct2.go();

            /*ColumnText ct3 = new ColumnText(cb);
            ct3.setSimpleColumn(30f, 80, 750f, 30f);
            Font f2 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
            f.setColor(new BaseColor(102,0,204));

            Paragraph pz3 = new Paragraph(new Phrase(18, mAvgComparision.getText().toString(), f2));
            ct3.addElement(pz3);
            ct3.go();
*/
            //createContent2(cb,30,150, mLowestPriceText.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.RED);
            //createContent2(cb,30,130, mHighestPriceText.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.RED);
            //createContent2(cb,30,100, mMarketDemand.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.RED);
            //createContent2(cb,30,80, mAvgComparision.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.RED);

            createHeadingsTitle(cb,35,700, mDate.getText().toString(),PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createHeadingsTitle(cb,35,670, "Performance Intelligence Report",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            createContent(cb,200,20,"This is computer generated analysis report",PdfContentByte.ALIGN_LEFT,BaseColor.BLACK);
            //createContent(cb,300,525, "₹ ",PdfContentByte.ALIGN_RIGHT);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void createContent2(PdfContentByte cb, float x, float y, String text, int align,BaseColor bc){


        cb.beginText();
        cb.setFontAndSize(bfBold, 15);
        cb.setColorFill(bc);

        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

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
            try{
                List<Intent> intentShareList = new ArrayList<Intent>();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(shareIntent, 0);

                for (ResolveInfo resInfo : resolveInfoList) {
                    String packageName = resInfo.activityInfo.packageName;
                    String name = resInfo.activityInfo.name;


                    if (packageName.contains("com.google") || packageName.contains("whatsapp")) {
                        if(packageName.contains("com.google")){
                            String[] mailto = {email};

                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Performance Intelligence Report - "+hotelName+" "+mDate.getText().toString());

                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Hotel Partner,\n" +
                                    "We Thank you for your continued support in ensuring the highest level of service Standards. \n" +
                                    "\n" +
                                    "Please find attached performance Intelligence  Report for your hotel for "+mDate.getText().toString());

                            Uri uri = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                uri = FileProvider.getUriForFile(this, "app.zingo.com.billgenerate.fileprovider", file);
                            }else{
                                uri = Uri.fromFile(file);
                            }


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


                            Uri uris = Uri.fromFile(file);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                                uris = FileProvider.getUriForFile(this, "app.zingo.com.billgenerate.fileprovider", file);

                            }else{
                                uris = Uri.fromFile(file);
                            }

                            if(uris!=null){
                                intent.putExtra(Intent.EXTRA_STREAM, uris);
                            }else{
                                Toast.makeText(this, "File cannot access", Toast.LENGTH_SHORT).show();
                            }


                            intentShareList.add(intent);
                        }

                    }
                }

                if (intentShareList.isEmpty()) {
                    Toast.makeText(CompetitiveAnalisysActivity.this, "No apps to share !", Toast.LENGTH_SHORT).show();
                } else {
                    Intent chooserIntent = Intent.createChooser(intentShareList.remove(0), "Share via");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentShareList.toArray(new Parcelable[]{}));
                    startActivity(chooserIntent);
                }

                    }catch (Exception e){
                e.printStackTrace();
            }
           // Intent emailIntent = new Intent(Intent.ACTION_SEND);
            //emailIntent.setType("text/plain");

         //   Uri uri = Uri.fromFile(file);
           // emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
          //  startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
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
                onAddField(-1);
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
