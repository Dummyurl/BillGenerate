package app.zingo.com.billgenerate.Activiies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*import app.zingo.com.hotelmanagement.Customviews.CustomFontTextView;
import app.zingo.com.hotelmanagement.Model.FireBaseModel;
import app.zingo.com.hotelmanagement.Model.TravellerAgentProfiles;
import app.zingo.com.hotelmanagement.Util.CloseButton;
import app.zingo.com.hotelmanagement.Util.Constants;
import app.zingo.com.hotelmanagement.Util.PreferenceHandler;
import app.zingo.com.hotelmanagement.WebApi.FireBaseApi;
import app.zingo.com.hotelmanagement.WebApi.NotificationManagerApi;
import app.zingo.com.hotelmanagement.login.IRegistrasionService;*/
import app.zingo.com.billgenerate.R;

public class AgentBookingRequestActivity extends AppCompatActivity {

    Button mClose,mAccept,mReject,mSuggest;
    TextView mTitle,mAgentName;
    TextView mBiddingPrice,mGuestCount,mRoomCount,mCID,mCOD,mCIT,mCOT;

    String title,message,fds,tds,price,room,adult,child,cit,cot;
    //String bookingNumber;
    LinearLayout mButton,mRejected,mSelect;
    private int profileId,bookingId,hotelId;
    String preBookingSelectedRoomNumber="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_agent_booking_request);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Room Request");

            //hotelId = PreferenceHandler.getInstance(AgentBookingRequestActivity.this).getHotelID();

        /*roomNumberWithFloorsWithFaciltyList = new ArrayList<>();
        rooms =new ArrayList<Rooms>();*/

            title = getIntent().getExtras().getString("Title");
            message = getIntent().getExtras().getString("Message");

            mTitle = (TextView) findViewById(R.id.title_message);

            mButton = (LinearLayout)findViewById(R.id.buttons);
       /* mRejected = (LinearLayout)findViewById(R.id.rejected_layout);
        mSelect = (LinearLayout)findViewById(R.id.select_room_lay);*/

            mAgentName = (TextView) findViewById(R.id.agent_name);
            mClose = (Button)findViewById(R.id.btnClose);
            mAccept = (Button)findViewById(R.id.btnaccept);
            mReject = (Button)findViewById(R.id.btnReject);
            mSuggest = (Button)findViewById(R.id.btnSuggest);
            mGuestCount = (TextView) findViewById(R.id.brief_detail_pax_details);
            mRoomCount = (TextView) findViewById(R.id.total_rooms);
            mBiddingPrice = (TextView) findViewById(R.id.bidding_amount);
            //  mBookingNumber = (TextView) findViewById(R.id.brief_detail_booking_number);
            mCID = (TextView) findViewById(R.id.brief_detail_check_in_date);
            mCOD = (TextView) findViewById(R.id.brief_detail_check_out_date);
            mCIT = (TextView) findViewById(R.id.brief_detail_check_in_time);
            mCOT = (TextView) findViewById(R.id.brief_detail_check_out_time);

            if(title!=null){
                if(title.contains("Bidding Request Accepted"))
                {

                    //getAgentProfile(dto.getProfileId(),holder.mTitle,"Accepted");
                    mTitle.setText(title);

                }
                else if(title.contains("Bidding Request Reply"))
                {
                    //suggested.add(list.get(i));
                    mTitle.setText(title);
                }
                else if(title.contains("Your request is accepted by"))
                {
                    mTitle.setText(title);

                }
                else if(title.contains("Bidding Request Rejected By"))
                {
                    mTitle.setText(title);
                }
                else if(title.contains("Bidding Request Rejected"))
                {
                    mTitle.setText(title);
                }
                else if(title.contains("Agent Booking Request"))
                {
                    mTitle.setText(title);
                }
                /*if(title.equalsIgnoreCase("Room Rejected")||title.equalsIgnoreCase("Room Upgrade Suggession Rejected")||title.equalsIgnoreCase("Bidding Request Rejected")){
                    //mRejected.setVisibility(View.VISIBLE);

                    //mSelect.setVisibility(View.GONE);
                }else if(title.equalsIgnoreCase("Agent Booking Request")){
                    mTitle.setText("You have a new Booking request");
                }*/

            }


            if(message!=null){
                System.out.println("Agenst message = "+message);
                if(message.contains(",")){
                    String bookin[] = message.split(",");
                    System.out.println("Length=="+bookin.length);
                    if(bookin.length==9){
                        String agentId = bookin[0];
                        fds = bookin[1];
                        tds = bookin[2];
                        price = bookin[3];
                        room = bookin[4];
                        adult = bookin[5];
                        child = bookin[6];
                        cit = bookin[7];
                        cot = bookin[8];

                        duration(mAgentName);

                        profileId = Integer.parseInt(agentId);

                        mBiddingPrice.setText("Bidding Price is ₹ "+price);
                        mRoomCount.setText(room+" Room(s)");
                        mGuestCount.setText(adult+" Adult(s) "+child+" child");
                        mCIT.setText(""+cit);
                        mCOT.setText(""+cot);
                        //getAgentDetails(Integer.parseInt(agentId));
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            Date CIDate = null,CODate= null;
                            if(fds.contains("-"))
                            {
                                CIDate  =  simpleDateFormat2.parse(fds);
                                CODate  =  simpleDateFormat2.parse(tds);
                            }
                            else
                            {
                                CIDate  =  simpleDateFormat.parse(fds);
                                CODate  =  simpleDateFormat.parse(tds);
                            }
                            mCID.setText(simpleDateFormat1.format(CIDate));
                            mCOD.setText(simpleDateFormat1.format(CODate));


                        }
                        catch (ParseException ex)
                        {
                            ex.printStackTrace();
                        }


                    }
                    else
                    {
                        String agentId = bookin[0];
                        fds = bookin[1];
                        tds = bookin[2];
                        price = bookin[3];
                        room = bookin[4];
                        adult = bookin[5];
                        child = bookin[6];
                        cit = bookin[7];
                        cot = bookin[8];

                        duration(mAgentName);

                        profileId = Integer.parseInt(agentId);

                        mBiddingPrice.setText("Bidding Price is ₹ "+price);
                        mRoomCount.setText(room+" Room(s)");
                        mGuestCount.setText(adult+" Adult(s) "+child+" child");
                        mCIT.setText(""+cit);
                        mCOT.setText(""+cot);
                        //getAgentDetails(Integer.parseInt(agentId));
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            Date CIDate = null,CODate= null;
                            if(fds.contains("-"))
                            {
                                CIDate  =  simpleDateFormat2.parse(fds);
                                CODate  =  simpleDateFormat2.parse(tds);
                            }
                            else
                            {
                                CIDate  =  simpleDateFormat.parse(fds);
                                CODate  =  simpleDateFormat.parse(tds);
                            }
                            mCID.setText(simpleDateFormat1.format(CIDate));
                            mCOD.setText(simpleDateFormat1.format(CODate));


                        }
                        catch (ParseException ex)
                        {
                            ex.printStackTrace();
                        }
                    }



                }else{

                }
            }


            /*mAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AgentBookingRequestActivity.this,"Accept",Toast.LENGTH_SHORT).show();

                    FireBaseModel fm = new FireBaseModel();
                    fm.setSenderId(Constants.AGENT_SENDERID);
                    fm.setServerId(Constants.AGENT_SERVERID);
                    fm.setTravellerAgentProfileId(profileId);
                    fm.setTitle("Bidding Request Accepted");
                    String pass = room+","+adult+","+child;
                    fm.setMessage(hotelId+","+fds+","+tds+","+price+","+pass+","+cit+","+cot);
                    //registerTokenInDB(fm);
                    sendNotificationAgent(fm);

                }
            });*/

            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try {
                        //CloseButton.closeButton(AgentBookingRequestActivity.this,hotelId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            /*mReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(AgentBookingRequestActivity.this,"Reject",Toast.LENGTH_SHORT).show();
                    FireBaseModel fm = new FireBaseModel();
                    fm.setSenderId(Constants.AGENT_SENDERID);
                    fm.setServerId(Constants.AGENT_SERVERID);
                    fm.setTravellerAgentProfileId(profileId);
                    //fm.setDeviceId(deviceId);
                    fm.setTitle("Bidding Request Rejected");
                    String pass = room+","+adult+","+child;
                    fm.setMessage(hotelId+","+fds+","+tds+","+price+","+pass+","+cit+","+cot);

                    //registerTokenInDB(fm);
                    sendNotificationAgent(fm);
                    // sendNotificationByDevice(fm);
                    // sendNotification(fm);

                }
            });

            mSuggest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showAlertBox();
                }
            });*/
        }catch (Exception e){
            e.printStackTrace();
        }


    }

   /* public void showAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try{
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.bidding_layout_adapter,null);

            final EditText mReason = (EditText) view.findViewById(R.id.bid_reason);
            final EditText mAmount = (EditText) view.findViewById(R.id.bid_amount);

            Button mSave = (Button) view.findViewById(R.id.bid);


            builder.setView(view);
            builder.setTitle("Bidding");
            final AlertDialog dialog = builder.create();
            dialog.show();

            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try
                    {
                        String bid = mAmount.getText().toString();
                        String reason = mReason.getText().toString();

                        if(bid==null||bid.isEmpty()){
                            mAmount.setError("Should not be Empty");
                            mAmount.requestFocus();
                        }else if(reason==null||reason.isEmpty()){
                            mReason.setError("Should not be Empty");
                            mReason.requestFocus();

                        }else{

                            FireBaseModel fm = new FireBaseModel();
                            fm.setSenderId(Constants.AGENT_SENDERID);
                            fm.setServerId(Constants.AGENT_SERVERID);
                            fm.setTravellerAgentProfileId(profileId);
                            fm.setTitle("Bidding Request Reply");
                            String pass = room+","+adult+","+child;
                            fm.setMessage(hotelId+","+fds+","+tds+","+price+","+pass+","+cit+","+cot+","+bid+","+reason);
                            //registerTokenInDB(fm);
                            sendNotificationAgent(fm);
                            dialog.dismiss();
                        }

                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }


                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }



    }*/
   /* public void getDetails(final String bookingsnum)
    {



        //  System.out.println("TravelleR id="+ PreferenceHandler.getInstance(AgentBookingRequestActivity.this).getTravelerID());

        System.out.println("Hotel id = "+ PreferenceHandler.getInstance(AgentBookingRequestActivity.this).getHotelID());
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(AgentBookingRequestActivity.this);
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                Call<ArrayList<Bookings1>> getBookingsApiResponse = bookingApi.getBookingByNum(authenticationString,bookingsnum);

                getBookingsApiResponse.enqueue(new Callback<ArrayList<Bookings1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                        int code = response.code();

                        if(code == 200)
                        {

                            bookingId = response.body().get(0).getBookingId();
                            System.out.println("Booking Id"+bookingId);
                            System.out.println("Nou"+response.body().get(0).getBookingNumber());

                            mGuestCount.setText("No of Guest: "+response.body().get(0).getNoOfAdults());
                            String cit   = response.body().get(0).getCheckInDate();
                            String cot = response.body().get(0).getCheckOutDate();
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                            //mCID.setText(simpleDateFormat1.format(cit));
                            // mCOD.setText(simpleDateFormat1.format(cot));
                            if(cit != null && cot != null)
                            {
                                try {
                                    Date CIDate = null,CODate= null;
                                    if(cit.contains("-"))
                                    {
                                        CIDate  =  simpleDateFormat2.parse(cit);
                                        CODate  =  simpleDateFormat2.parse(cot);
                                    }
                                    else
                                    {
                                        CIDate  =  simpleDateFormat.parse(cit);
                                        CODate  =  simpleDateFormat.parse(cot);
                                    }
                                    mCID.setText(simpleDateFormat1.format(CIDate));
                                    mCOD.setText(simpleDateFormat1.format(CODate));


                                }
                                catch (ParseException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }



                        }
                        else
                        {
                            Toast.makeText(AgentBookingRequestActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {

                        System.out.println(t.getMessage());
                        Toast.makeText(AgentBookingRequestActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }*/

    /*private void getAgentDetails(final int profileId) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(AgentBookingRequestActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                IRegistrasionService travellerApi = Util.getClient().create(IRegistrasionService.class);
                Call<TravellerAgentProfiles> getTravellerDetails = travellerApi.getProfileByID(auth_string,profileId);

                getTravellerDetails.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {

                        try
                        {
                            if(response.code() == 200)
                            {
                                try{
                                    mAgentName.setText(response.body().getFirstName());
                                }catch (Exception e ){
                                    e.printStackTrace();
                                }

                            }
                            else
                            {
                                System.out.println("traveller = "+response.message());
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {

                    }
                });
            }
        });
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                AgentBookingRequestActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

   /* public void sendNotification(final FireBaseModel fireBaseModel) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(AgentBookingRequestActivity.this);
                FireBaseApi apiService =
                        Util.getClient().create(FireBaseApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.send(auth_string, fireBaseModel)*//*getString()*//*;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if (statusCode == 200) {

                            try {
                                ArrayList<String> list = response.body();

                                Toast.makeText(AgentBookingRequestActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                                //sendEmailattache();
                                Intent main = new Intent(AgentBookingRequestActivity.this, MainActivity.class);
                                startActivity(main);
                                AgentBookingRequestActivity.this.finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        } else {

                            Toast.makeText(AgentBookingRequestActivity.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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


    public void sendNotificationByDevice(final FireBaseModel fireBaseModel) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(AgentBookingRequestActivity.this);
                FireBaseApi apiService =
                        Util.getClient().create(FireBaseApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.addNotification(auth_string, fireBaseModel)*//*getString()*//*;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if (statusCode == 200) {

                            try{
                                ArrayList<String> list = response.body();

                                Toast.makeText(AgentBookingRequestActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                                //sendEmailattache();
                                Intent main = new Intent(AgentBookingRequestActivity.this, MainActivity.class);
                                startActivity(main);
                                AgentBookingRequestActivity.this.finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        } else {

                            Toast.makeText(AgentBookingRequestActivity.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
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

    public void sendNotificationAgent(final FireBaseModel fireBaseModel) {

        final ProgressDialog dialog = new ProgressDialog(AgentBookingRequestActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(AgentBookingRequestActivity.this);
                FireBaseApi apiService =
                        Util.getClient().create(FireBaseApi.class);


                System.out.println("Nodel" + fireBaseModel.toString());
                Call<ArrayList<String>> call = apiService.sendAgent(auth_string, fireBaseModel)*//*getString()*//*;

                call.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        try{
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        int statusCode = response.code();

                        if (statusCode == 200) {


                                ArrayList<String> list = response.body();



                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(fireBaseModel.getTitle());
                                nf.setNotificationFor(fireBaseModel.getMessage());
                                nf.setTravellerAgentId(fireBaseModel.getTravellerAgentProfileId());
                                savenotification(nf);




                        } else {

                            Toast.makeText(AgentBookingRequestActivity.this, " failed due to status code:" + statusCode, Toast.LENGTH_SHORT).show();
                        }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        // Log error here since request failed
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Log.e("TAG", t.toString());
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
                String auth_string = Util.getToken(AgentBookingRequestActivity.this);
                NotificationManagerApi travellerApi = Util.getClient().create(NotificationManagerApi.class);
                Call<NotificationManager> response = travellerApi.saveNotification(auth_string,notification);

                response.enqueue(new Callback<NotificationManager>() {
                    @Override
                    public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {


                        //System.out.println(response.code());
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {

                                try{
                                      *//*Toast.makeText(ReviewHotelDetailsActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*//*
                                    //SelectRoom.this.finish();
                                    Toast.makeText(AgentBookingRequestActivity.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                                    //sendEmailattache();
                                    Intent main = new Intent(AgentBookingRequestActivity.this, MainActivity.class);
                                    startActivity(main);
                                    AgentBookingRequestActivity.this.finish();

                                    //Toast.makeText(ReviewHotelDetailsActivity.this, "Save Notification", Toast.LENGTH_SHORT).show();

                                }catch (Exception e){
                                    e.printStackTrace();
                                }




                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationManager> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
            }
        });
    }*/
    public void duration(final TextView tv) throws Exception{

        long diffDays,diffHours,diffMinutes;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        String duration = null;
        String from= null;
        String to = null;
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        if (cit.contains("AM") || cit.contains("PM"))
        {
            Date date = null;
            try {
                date = parseFormat.parse(cit);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String st = displayFormat.format(date);
            from = fds + " " + st;
        }
        else
        {
            from = fds+" "+cit;
        }

        if (cot.contains("AM") || cot.contains("PM"))
        {
            Date date = null;
            try {
                date = parseFormat.parse(cot);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String st = displayFormat.format(date);
            to = tds + " " + st;
        }
        else
        {
            to = tds+" "+cot;
        }


       // SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {

            d1 = format.parse(from);
            d2 = format.parse(to);
            long diff = d2.getTime() - d1.getTime();
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);
            duration =  diffDays+" Days "+diffHours+" Hours "+diffMinutes+" Minutes";
        }catch(Exception e){
            e.printStackTrace();
        }
        tv.setText(duration);
        System.out.println("Duration=="+duration);
    }


}
