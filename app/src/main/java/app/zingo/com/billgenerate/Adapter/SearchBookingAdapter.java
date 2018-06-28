package app.zingo.com.billgenerate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.SearchBook;
import app.zingo.com.billgenerate.R;

/**
 * Created by CSC on 1/20/2018.
 */

public class SearchBookingAdapter extends RecyclerView.Adapter<SearchBookingAdapter.ViewHolder> {


    private Context context;
    private ArrayList<SearchBook> bookingArrayList;
    //ArrayList<Traveller> travellerArrayList;

    public SearchBookingAdapter(Context context, ArrayList<SearchBook> bookingArrayList)
    {
        this.context = context;
        this.bookingArrayList = bookingArrayList;
       // this.travellerArrayList = travellerArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_booking_adapter,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SearchBook bookings1 = bookingArrayList.get(position);
        //Traveller traveller = travellerArrayList.get(position);

        if(bookings1 != null)
        {
            holder.mBookedPersonName.setText(bookings1.getFirstName()+" "+bookings1.getLastName());
            holder.mBookedNumber.setText(bookings1.getBookingNumber());
            holder.mBookedFrom.setText(bookings1.getCheckInDate());
            holder.mBookedTo.setText(bookings1.getCheckOutDate());
            holder.mBookedStatus.setText(bookings1.getBookingStatus());


        }
    }

    @Override
    public int getItemCount() {
        return bookingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mBookedPersonName,mBookedNumber,mBookedFrom,mBookedTo,mBookedStatus,mEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            mBookedPersonName = (TextView) itemView.findViewById(R.id.booked_person_name_s);
            mBookedNumber = (TextView) itemView.findViewById(R.id.booked_number);
            mBookedFrom = (TextView) itemView.findViewById(R.id.booked_from);
            mBookedTo = (TextView) itemView.findViewById(R.id.booked_to);
            mBookedStatus = (TextView) itemView.findViewById(R.id.booked_status);
            mEdit = (TextView) itemView.findViewById(R.id.edit);

        }
    }





}