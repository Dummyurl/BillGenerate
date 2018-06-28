package app.zingo.com.billgenerate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.R;


/**
 * Created by ZingoHotels.com on 23-11-2017.
 */

public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<BookingRecyclerViewAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Bookings1> bookingArrayList;
    ArrayList<Traveller> travellerArrayList;

    public BookingRecyclerViewAdapter(Context context, ArrayList<Bookings1> bookingArrayList, ArrayList<Traveller> travellerArrayList)
    {
        this.context = context;
        this.bookingArrayList = bookingArrayList;
        this.travellerArrayList = travellerArrayList;
        setHasStableIds(true);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookings,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final Bookings1 bookings1 = bookingArrayList.get(position);
        final Traveller traveller = travellerArrayList.get(position);

        if(bookings1 != null && traveller != null)
        {
            //holder.mBookedPersonName.setText(traveller.getFirstName()+" "+traveller.getLastName());
            holder.mBookedPersonName.setText(traveller.getFirstName());

            holder.mBookedDate.setText("Booked On: "+getBookedOnDateFormate(bookings1.getBookingDate()));
           if(bookings1.getCheckInDate()!=null && !bookings1.getCheckInDate().isEmpty()&&bookings1.getCheckOutDate()!=null && !bookings1.getCheckOutDate().isEmpty()){
               holder.mBookingDates.setText(getBookingDateFormate(bookings1.getCheckInDate())
                       +" To "+getBookingDateFormate(bookings1.getCheckOutDate()));
           }
            holder.mNetAmount.setText("Net Amount: ₹ "+bookings1.getTotalAmount());
            if(bookings1.getBookingSourceType() != null)
            {
                holder.mBookingSourceType.setText(bookings1.getBookingSourceType());
            }
            if(traveller.getFirstName() != null && !traveller.getFirstName().isEmpty())
            {
               /* if(traveller.getLastName() != null && !traveller.getLastName().isEmpty())
                {
                    holder.mShortName.setText(traveller.getFirstName().charAt(0)+""+traveller.getLastName().charAt(0));
                }
                else
                {
                    holder.mShortName.setText(traveller.getFirstName().charAt(0));
                }*/

               String[] ab = traveller.getFirstName().split(" ");
               if(ab.length > 1)
               {
                   //if(ab[1].charAt(0) != "")
                   holder.mShortName.setText(ab[0].charAt(0)+"");//+""+ab[1].charAt(0));
               }
               else
               {
                   holder.mShortName.setText(ab[0].charAt(0)+"");
               }
            }


            System.out.println("Booking status = "+bookings1.getBookingStatus());









            holder.mNoofRooms.setText((long)getDays(bookings1.getCheckInDate(),bookings1.getCheckOutDate())+" Night(s)");
            /*holder.mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String callingNumber = travellerArrayList.get(position).getPhoneNumber();

                    if(callingNumber != null && !callingNumber.isEmpty())
                    {
                        if (!callingNumber.equals("")) {
                            Uri number = Uri.parse("tel:" + callingNumber);
                            Intent dial = new Intent(Intent.ACTION_CALL, number);
                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                return;
                            }
                            context.startActivity(dial);
                        }
                    }
                }
            });*/
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
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return bookingArrayList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mBookedPersonName,mBookedDate,mBookingDates,mNoofRooms,mNetAmount,mShortName,mCall,mPayAtHotel,mBookedRoom,mBookingSourceType;
        LinearLayout mparent;

        public ViewHolder(View itemView) {
            super(itemView);

            mBookedPersonName = (TextView) itemView.findViewById(R.id.booked_person_name);
            mBookedDate = (TextView) itemView.findViewById(R.id.booked_date);
            mBookingDates = (TextView) itemView.findViewById(R.id.booked_from_to_date);
            mNoofRooms = (TextView) itemView.findViewById(R.id.booked_no_rooms_night);
            mNetAmount = (TextView) itemView.findViewById(R.id.net_amount);
            mShortName = (TextView) itemView.findViewById(R.id.person_short_name);
            mCall = (TextView) itemView.findViewById(R.id.call_booked_person);
            mPayAtHotel = (TextView) itemView.findViewById(R.id.pay_at_hotel);
            mBookedRoom = (TextView) itemView.findViewById(R.id.call_booked_room_no);
            mparent = (LinearLayout) itemView.findViewById(R.id.parent_layout_for_user_details);
            mBookingSourceType = (TextView) itemView.findViewById(R.id.booking_source_type);

        }
    }

    public String getBookedOnDateFormate(String sdate)
    {
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(sdate);
            String sDate = new SimpleDateFormat("dd MMM yyyy").format(date);
            System.out.println("sDate = "+sDate);
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
            System.out.println("sDate = "+sDate);
            return sDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



}
