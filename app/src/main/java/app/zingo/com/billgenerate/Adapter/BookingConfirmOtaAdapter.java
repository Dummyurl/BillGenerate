package app.zingo.com.billgenerate.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Rooms;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import app.zingo.com.billgenerate.WebApis.BookingApi;
import app.zingo.com.billgenerate.WebApis.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 21-07-2018.
 */

public class BookingConfirmOtaAdapter extends RecyclerView.Adapter<BookingConfirmOtaAdapter.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private Context context;
    private ArrayList<Bookings1> list;

    private boolean isLoadingAdded = false;
    public BookingConfirmOtaAdapter(Context context,ArrayList<Bookings1> list)
    {
        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ota_booking,
                parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        DecimalFormat df = new DecimalFormat("#.##");

        final Bookings1 bookingAndTraveller = list.get(position);

                    if(bookingAndTraveller != null)
                    {
                        //holder.mBookedPersonName.setText(traveller.getFirstName()+" "+traveller.getLastName());


                        getTraveller(bookingAndTraveller,holder.mBookedPersonName,holder.mShortName);
                        holder.mBookedDate.setText("Booked On: "+getBookedOnDateFormate(bookingAndTraveller.getBookingDate()));
                        if(bookingAndTraveller.getCheckInDate()!=null && !bookingAndTraveller.getCheckInDate().isEmpty()&&bookingAndTraveller.getCheckOutDate()!=null && !bookingAndTraveller.getCheckOutDate().isEmpty()){
                            holder.mBookingDates.setText(getBookingDateFormate(bookingAndTraveller.getCheckInDate())
                                    +" To "+getBookingDateFormate(bookingAndTraveller.getCheckOutDate()));
                        }
                        holder.mTotalAmount.setText("Gross Amount: ₹ "+df.format(bookingAndTraveller.getTotalAmount()));
                        holder.mNetAmount.setText("Net Amount: ₹ "+df.format(bookingAndTraveller.getTotalAmount()-bookingAndTraveller.getOTATotalCommissionAmount()));
                        holder.mOtaBookingId.setText("OTA Id: "+bookingAndTraveller.getOTABookingID());
                        if(bookingAndTraveller.getBookingSourceType() != null)
                        {
                            holder.mBookingSourceType.setText(bookingAndTraveller.getBookingSource());
                        }

                        System.out.println("Past - "+bookingAndTraveller.getBookingId());
                        /*if(traveller != null)
                        {


                            holder.mBookedPersonName.setText(traveller.getFirstName());
                            if(traveller.getFirstName() != null && !traveller.getFirstName().isEmpty())
                            {


                                String[] ab = traveller.getFirstName().split(" ");
                                if(ab.length > 1 && ab[0] != null && !ab[0].isEmpty())
                                {
                                    //if(ab[1].charAt(0) != "")
                                    holder.mShortName.setText(ab[0].charAt(0)+"");//+""+ab[1].charAt(0));
                                }
                                else
                                {
                                    holder.mShortName.setText(traveller.getFirstName().charAt(0)+"");
                                }
                            }
                        }*/



                        holder.mUpdateCheckIn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(bookingAndTraveller != null )
                                {
                                    // Toast.makeText(context,bookings1.getBookingStatus()+" == "+traveller.getFirstName(),Toast.LENGTH_SHORT).show();
                                    if(bookingAndTraveller != null)
                                    {
                                        if(bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Active"))
                                        {
                                            updateBooking(bookingAndTraveller,"Completed");
                                        }
                                        else
                                        {
                                            updateBooking(bookingAndTraveller,"Active");
                                        }
                                    }
                                }
                            }
                        });
                        holder.mUpdateNoShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(bookingAndTraveller != null )
                                {

                                    if(bookingAndTraveller != null)
                                    {
                                        if(bookingAndTraveller.getBookingStatus().equalsIgnoreCase("Quick") ||
                                                bookingAndTraveller.getBookingStatus().equalsIgnoreCase("delay"))
                                        {
                                            updateBooking(bookingAndTraveller,"Abandoned");
                                        }
                                        else
                                        {
                                            updateBooking(bookingAndTraveller,"Cancelled");
                                        }
                                    }
                                }
                            }
                        });



                        if(bookingAndTraveller.getCheckInDate() == null || bookingAndTraveller.getCheckOutDate() == null)
                        {
                            System.out.println("Check out = "+bookingAndTraveller.getBookingId());
                        }
                        holder.mNoofRooms.setText((long)getDays(bookingAndTraveller.getCheckInDate(),bookingAndTraveller.getCheckOutDate())+" Night(s)");


                    }


    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("inside getItemViewType = "+position);
        if(position == list.size() - 1 && isLoadingAdded)
        {
            //System.out.println("inside getItemViewType LOADING = "+position);
            return LOADING;
        }
        else
        {
            //System.out.println("inside getItemViewType ITEM = "+position);
            return ITEM;
        }
        //return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mBookedPersonName,mBookedDate,mBookingDates,mNoofRooms,mNetAmount,mTotalAmount,mOtaBookingId,
                mShortName,mCall,mPayAtHotel,mBookedRoom,mBookingSourceType,mUpdateCheckIn,mUpdateNoShow;
        LinearLayout mparent,mButtonsParent;

        public ViewHolder(View itemView) {
            super(itemView);

            mBookedPersonName = (TextView) itemView.findViewById(R.id.booked_person_name);
            mBookedDate = (TextView) itemView.findViewById(R.id.booked_date);
            mBookingDates = (TextView) itemView.findViewById(R.id.booked_from_to_date);
            mNoofRooms = (TextView) itemView.findViewById(R.id.booked_no_rooms_night);
            mNetAmount = (TextView) itemView.findViewById(R.id.net_amount);
            mTotalAmount = (TextView) itemView.findViewById(R.id.total_amount);
            mOtaBookingId = (TextView) itemView.findViewById(R.id.ota_booking_id);
            mShortName = (TextView) itemView.findViewById(R.id.person_short_name);
            mCall = (TextView) itemView.findViewById(R.id.call_booked_person);
            mPayAtHotel = (TextView) itemView.findViewById(R.id.pay_at_hotel);
            mUpdateCheckIn = (TextView) itemView.findViewById(R.id.recycler_update_checkin);
            mUpdateNoShow = (TextView) itemView.findViewById(R.id.recycler_update_noshow);
            mBookedRoom = (TextView) itemView.findViewById(R.id.call_booked_room_no);
            mparent = (LinearLayout) itemView.findViewById(R.id.parent_layout_for_user_details);
            mButtonsParent = (LinearLayout) itemView.findViewById(R.id.buttons_parent_view);
            mBookingSourceType = (TextView) itemView.findViewById(R.id.booking_source_type);

        }
    }


    public String getBookedOnDateFormate(String sdate)
    {
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(sdate);
            String sDate = new SimpleDateFormat("dd MMM yyyy").format(date);

            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    public String getBookingDateFormate(String bdate)
    {
        String sDate = null;
        try {
            if(bdate.contains("-"))
            {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(bdate);
                sDate = new SimpleDateFormat("dd MMM").format(date);
            }
            else
            {
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(bdate);
                sDate = new SimpleDateFormat("dd MMM").format(date);
            }

            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long getDays(String checkInDate, String checkOutDate) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat myFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        /*String inputString1 = "01/10/2018";
        String inputString2 = "01/19/2018";*/
        Date date1,date2;
        try {
            if(checkInDate.contains("-"))
            {
                date1 = myFormat1.parse(checkInDate);
                date2 = myFormat1.parse(checkOutDate);
            }
            else
            {
                date1 = myFormat.parse(checkInDate);
                date2 = myFormat.parse(checkOutDate);
            }
            long diff = date2.getTime() - date1.getTime();
            //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void getTraveller(final Bookings1 bookings1, final TextView tv, final TextView tv1) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                TravellerApi travellerApi = Util.getClient().create(TravellerApi.class);


                String authenticationString = Util.getToken(context);
                Call<Traveller> getTraveller = travellerApi.getTravellerDetails(authenticationString,bookings1.getTravellerId());
                getTraveller.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/

                        if(response.code() == 200 || response.code() == 201 || response.code() == 204)
                        {
                            Traveller traveller =  response.body();
                            //travellers = response.body();
                            if(traveller != null)
                            {


                                tv.setText(traveller.getFirstName());
                                if(traveller.getFirstName() != null && !traveller.getFirstName().isEmpty())
                                {

                                    String[] ab = traveller.getFirstName().split(" ");
                                    if(ab.length > 1 && ab[0] != null && !ab[0].isEmpty())
                                    {
                                        //if(ab[1].charAt(0) != "")
                                        tv1.setText(ab[0].charAt(0)+"");//+""+ab[1].charAt(0));
                                    }
                                    else
                                    {
                                        tv1.setText(traveller.getFirstName().charAt(0)+"");
                                    }
                                }
                            }


                        }
                        else
                        {
                            //Toast.makeText(context,"Please try after some time",Toast.LENGTH_SHORT).show();
                            //travellers = null;
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                    }
                });
            }
        });


    }

    public  void getRoomNo(final int i, final TextView t)
    {
        String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        LoginApi api = Util.getClient().create(LoginApi.class);
        Call<Rooms> getRoom = api.getRoom(auth_string,i);

        getRoom.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                if(response.code() == 200)
                {
                    if(response.body() != null)
                    {
                        t.setText("R No: "+response.body().getRoomNo());
                    }

                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    private void updateBooking(final Bookings1 bookings1, final String status) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                Bookings1 dBook = bookings1;
                dBook.setBookingStatus(status);

                String authenticationString = Util.getToken(context);
                Call<String> checkout = bookingApi.updateBookingStatus(authenticationString,bookings1.getBookingId(),dBook);
                checkout.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 204)
                        {

                            Toast.makeText(context,"Booking is updated successfully",Toast.LENGTH_SHORT).show();
                            //BookingDetailsActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(context,"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }



    public Bookings1 getItem(int position) {
        return list.get(position);
    }



}
