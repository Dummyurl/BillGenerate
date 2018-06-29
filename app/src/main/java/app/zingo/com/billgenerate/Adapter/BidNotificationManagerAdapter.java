package app.zingo.com.billgenerate.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*import app.zingo.com.hotelmanagement.Model.Bookings1;
import app.zingo.com.hotelmanagement.Model.NotificationManager;
import app.zingo.com.hotelmanagement.Model.Traveller;
import app.zingo.com.hotelmanagement.R;
import app.zingo.com.hotelmanagement.Util.PreferenceHandler;
import app.zingo.com.hotelmanagement.Util.ThreadExecuter;
import app.zingo.com.hotelmanagement.Util.Util;
import app.zingo.com.hotelmanagement.WebApi.BookingApi;
import app.zingo.com.hotelmanagement.WebApi.TravellerApi;
import app.zingo.com.hotelmanagement.activities.AgentBookingRequestActivity;
import app.zingo.com.hotelmanagement.activities.RoomBookingNotifyActivity;*/
import app.zingo.com.billgenerate.AgentBookingRequestActivity;

import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.Model.TravellerAgentProfiles;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import app.zingo.com.billgenerate.WebApis.BookingApi;
import app.zingo.com.billgenerate.WebApis.LoginApi;
import app.zingo.com.billgenerate.WebApis.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 10-05-2018.
 */

public class BidNotificationManagerAdapter extends RecyclerView.Adapter<BidNotificationManagerAdapter.ViewHolder>{

    private Context context;
    private ArrayList<NotificationManager> list;
    String activity;
    public BidNotificationManagerAdapter(Context context, ArrayList<NotificationManager> list) {

        this.context = context;
        this.list = list;
        //this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        try{
             v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bid_adapter_notification_manager, parent, false);

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try{
            final NotificationManager dto = list.get(position);
            System.out.println("getNotificationText = "+dto.getNotificationText());
            System.out.println("getNotificationText = "+dto.getNotificationFor());
            if(dto.getNotificationText().equalsIgnoreCase("New Booking from Zingo Hotels")){
                holder.mTitle.setText(""+dto.getNotificationText());
                holder.mRoom.setVisibility(View.GONE);
                holder.mTime.setVisibility(View.GONE);
                holder.mOther.setVisibility(View.VISIBLE);

                String message = dto.getNotificationFor();
                String bookingNumber;

                if(message.contains(":")){
                    String bookin[] = message.split(":");
                    if(bookin[1].contains(",")){
                        String bookinn[] = bookin[1].split(",");
                        bookingNumber = bookinn[0];

                    }else{
                        bookingNumber = bookin[1];
                    }
                    if(bookingNumber!=null){

                        System.out.println("Bookinlll"+bookingNumber);
                        holder.mBookingNumber.setText(""+bookingNumber);
                        getDetails(bookingNumber,holder.mCID,holder.mCOD,holder.mGuestName,holder.mTotal,holder.mNights,holder.mBookingStatus,holder.mBookDate,holder.mDuration);

                    }else{

                    }
                }
            }else {

                System.out.println("ProfileId = "+dto.getProfileId());
                if(dto.getNotificationText().contains("Bidding Request Accepted"))
                {

                    //getAgentProfile(dto.getProfileId(),holder.mTitle,"Accepted");
                    holder.mTitle.setText(dto.getNotificationText());
                }
                else if(dto.getNotificationText().contains("Bidding Request Reply"))
                {
                    //suggested.add(list.get(i));
                    holder.mTitle.setText(dto.getNotificationText());
                }
                else if(dto.getNotificationText().contains("Your request is accepted by"))
                {
                    System.out.println("Message = "+dto.getNotificationFor());
                    holder.mTitle.setText("Accepted By ");
                    getAgentProfile(dto.getProfileId(),holder.mTitle,"Accepted By");
                }
                else if(dto.getNotificationText().contains("Bidding Request Rejected By"))
                {
                    holder.mTitle.setText("Rejected By ");
                    getAgentProfile(dto.getProfileId(),holder.mTitle,"Rejected By");
                }
                else if(dto.getNotificationText().contains("Bidding Request Rejected"))
                {
                    holder.mTitle.setText(dto.getNotificationText());
                }
                else if(dto.getNotificationText().contains("Agent Booking Request"))
                {
                    holder.mTitle.setText("Requested By ");
                    System.out.println("Requested By = "+dto.getProfileId());
                    getAgentProfile(dto.getProfileId(),holder.mTitle,"Requested By");
                }

                holder.mRoom.setVisibility(View.VISIBLE);
                holder.mTime.setVisibility(View.VISIBLE);
                holder.mOther.setVisibility(View.GONE);
                holder.mBookingStatus.setVisibility(View.GONE);
                holder.mDateBooking.setVisibility(View.GONE);

                String message = dto.getNotificationFor();

                if(message.contains(",")){
                    String bookin[] = message.split(",");
                    System.out.println("Length=="+bookin.length);
                    if(bookin.length<=11){
                        String agentId = bookin[0];
                        String fds = bookin[1];
                        String tds = bookin[2];
                        String price = bookin[3];
                        String room = bookin[4];
                        String adult = bookin[5];
                        String child = bookin[6];
                        String cit = bookin[7];
                        String cot = bookin[8];

                        //profileId = Integer.parseInt(agentId);
                        if(dto.getNotificationText().contains("Bidding Request Reply"))
                        {
                            holder.mGuestName.setText("Suggested Price is ₹ "+price);
                        }
                        else
                        {
                            holder.mGuestName.setText("Bidding Price is ₹ "+price);
                        }

                        holder.mRooms.setText(room+" Room(s)");
                        //mGuestCount.setText(adult+" Adult(s) "+child+" child");
                        holder.mCIT.setText(""+cit);
                        holder.mCOT.setText(""+cot);
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
                            holder.mCID.setText(simpleDateFormat1.format(CIDate));
                            holder.mCOD.setText(simpleDateFormat1.format(CODate));


                        }
                        catch (ParseException ex)
                        {
                            ex.printStackTrace();
                        }


                    }



                }else{

                }


            }

            System.out.println("Adaptermessage = "+dto.getNotificationFor());
            holder.mNotificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(dto.getNotificationText().equalsIgnoreCase("New Booking from Zingo Hotels")) {
                        /*Intent intent = new Intent(context, RoomBookingNotifyActivity.class);
                        intent.putExtra("Title",dto.getNotificationText());
                        intent.putExtra("Message",dto.getNotificationFor());
                        intent.putExtra("Activity",activity);

                        context.startActivity(intent);*/

                    }else {
                        Intent intent = new Intent(context, AgentBookingRequestActivity.class);
                        intent.putExtra("Title",dto.getNotificationText());
                        intent.putExtra("Message",dto.getNotificationFor());
                        //intent.putExtra("Activity",activity);
                        context.startActivity(intent);
                    }

                }
            });
        }catch (Exception e){

        }



    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public TextView mTitle,mGuestName,mBookingStatus,mCID,mCOD,mNights,mTotal,mBookingNumber,
                        mCIT,mCOT,mRooms,mBookDate,mDuration;
        CardView mNotificationLayout;
        LinearLayout mTime,mRoom,mOther,mDateBooking;
//

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            try{
                mNotificationLayout = (CardView) itemView.findViewById(R.id.notification_layout);
                mTitle = (TextView) itemView.findViewById(R.id.title);
                mGuestName = (TextView) itemView.findViewById(R.id.brief_detail_traveller_name);
                mBookingStatus = (TextView) itemView.findViewById(R.id.status);
                mBookDate = (TextView) itemView.findViewById(R.id.booking_date);
                mCID = (TextView) itemView.findViewById(R.id.brief_detail_check_in_date);
                mCOD = (TextView) itemView.findViewById(R.id.brief_detail_check_out_date);
                mNights = (TextView) itemView.findViewById(R.id.nights);
                mTotal = (TextView) itemView.findViewById(R.id.total_amount_info);
                mBookingNumber = (TextView) itemView.findViewById(R.id.booking_number);
                mDuration = (TextView) itemView.findViewById(R.id.duration);
                mCIT= (TextView) itemView.findViewById(R.id.brief_detail_check_in_time);
                mCOT = (TextView) itemView.findViewById(R.id.brief_detail_check_out_time);
                mRooms = (TextView) itemView.findViewById(R.id.num_rooms);
                mTime = (LinearLayout) itemView.findViewById(R.id.time);
                mRoom = (LinearLayout) itemView.findViewById(R.id.layout_room);
                mOther = (LinearLayout) itemView.findViewById(R.id.layout_amount);
                mDateBooking = (LinearLayout) itemView.findViewById(R.id.date_booking_layout);

            }catch(Exception e){
                e.printStackTrace();
            }



        }
    }
    public void getDetails(final String bookingsnum,final TextView tv1,final TextView tv2,final TextView tv3,final TextView tv4,final TextView tv5,final TextView tv6,final TextView tv7,final TextView tv8)
    {



        //  System.out.println("TravelleR id="+ PreferenceHandler.getInstance(RoomBookingNotifyActivity.this).getTravelerID());

        /*System.out.println("Hotels id = "+ PreferenceHandler.getInstance(context).getHotelID());
        System.out.println("bookkkkk"+bookingsnum);*/
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(context);
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                Call<ArrayList<Bookings1>> getBookingsApiResponse = bookingApi.getBookingByNum(authenticationString,bookingsnum);

                getBookingsApiResponse.enqueue(new Callback<ArrayList<Bookings1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Bookings1>> call, Response<ArrayList<Bookings1>> response) {
                        int code = response.code();

                        if(code == 200)
                        {

                            // System.out.println("Nou"+response.body().get(0).getBookingNumber());


                            try{
                                String cit   = response.body().get(0).getCheckInDate();
                                String cot = response.body().get(0).getCheckOutDate();
                                String bookdate = response.body().get(0).getBookingDate();
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                                //mCID.setText(simpleDateFormat1.format(cit));
                                // mCOD.setText(simpleDateFormat1.format(cot));
                                if(cit != null && cot != null)
                                {
                                    try {
                                        Date CIDate = null,CODate= null,bookDate=null;
                                        if(cit.contains("-"))
                                        {
                                            CIDate  =  simpleDateFormat2.parse(cit);
                                            CODate  =  simpleDateFormat2.parse(cot);
                                            bookDate  =  simpleDateFormat2.parse(bookdate);
                                        }
                                        else
                                        {
                                            CIDate  =  simpleDateFormat.parse(cit);
                                            CODate  =  simpleDateFormat.parse(cot);
                                            bookDate  =  simpleDateFormat.parse(bookdate);
                                        }
                                        tv1.setText(""+simpleDateFormat1.format(CIDate));
                                        tv2.setText(""+simpleDateFormat1.format(CODate));
                                        tv7.setText(""+simpleDateFormat1.format(bookDate)+", "+response.body().get(0).getBookingTime());
                                        tv8.setText(duration(""+simpleDateFormat1.format(bookDate)+", "+response.body().get(0).getBookingTime()));
                                    /*mCIT.setText(booked.getCheckInTime());
                                    mCOT.setText(booked.getCheckOutTime());*/

                                    }
                                    catch (ParseException ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }

                                tv4.setText("₹ "+response.body().get(0).getTotalAmount());
                                tv5.setText(""+response.body().get(0).getDurationOfStay());
                                tv6.setText(""+response.body().get(0).getBookingStatus());

                                String bookStatus = response.body().get(0).getBookingStatus();
                                if(bookStatus.equalsIgnoreCase("Active")){
                                    tv6.setBackgroundColor(Color.parseColor("#4CAF50"));
                                }else if(bookStatus.equalsIgnoreCase("Quick")){
                                    tv6.setBackgroundColor(Color.parseColor("#FBC02D"));
                                }else if(bookStatus.equalsIgnoreCase("Completed")){
                                    tv6.setBackgroundColor(Color.parseColor("#FF69B4"));
                                }else if(bookStatus.equalsIgnoreCase("Cancelled")){
                                    tv6.setBackgroundColor(Color.parseColor("#F44336"));
                                }


                                getTravellerDetails(response.body().get(0).getTravellerId(),tv3);
                            }catch(Exception e){
                                e.printStackTrace();
                            }



                        }
                        else
                        {
                            //Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Bookings1>> call, Throwable t) {

                        System.out.println(t.getMessage());
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getAgentProfile(final int id, final TextView tv, final String title) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(context);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString,id);
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {

                        if(response.code() == 200 || response.code() == 201 || response.code() == 204)
                        {

                            tv.setText(title+" "+response.body().getFirstName());
                        }
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void getTravellerDetails(final int travellerId,final TextView tv) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                TravellerApi travellerApi = Util.getClient().create(TravellerApi.class);
                Call<Traveller> getTravellerDetails = travellerApi.getTravellerDetails(auth_string,travellerId);

                getTravellerDetails.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {

                        if(response.code() == 200)
                        {
                            try{
                                tv.setText(""+response.body().getFirstName());
                            }catch (Exception e){

                            }

                        }
                        else
                        {
                           // System.out.println("traveller = "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {

                    }
                });
            }
        });
    }

    public String duration(String notifyDate){

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm a");
        Date fd = null,td = null;
        long diffDays=0,diffMinutes=0,diffHours=0 ;
        String duration = "" ;
        try {
            fd = sdf.parse(notifyDate);
            System.out.println("Text=="+notifyDate+" Date"+fd);
            //td = sdf.parse(book_to_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            long diff = new Date().getTime() - fd.getTime();
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.println("Diff===" + diffDays);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(diffDays!=0){
            duration =  diffDays+" day ago";
        }else if(diffHours!=0){
            duration =  diffHours+" hours ago";
        }else if(diffMinutes!=0){
            duration =  diffMinutes+" mins ago";
        }

        return duration;
    }




}
