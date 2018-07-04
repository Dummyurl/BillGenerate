package app.zingo.com.billgenerate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ZingoHotels.com on 2/26/2018.
 */


public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.ViewHolder> {

    Context context;
    ArrayList<HotelDetails> hotelDetailsArrayList;

    public HotelListAdapter(Context context, ArrayList<HotelDetails> hotelDetailsArrayList)
    {
        this.context = context;
        this.hotelDetailsArrayList = hotelDetailsArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_list_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mHotelName.setText(hotelDetailsArrayList.get(position).getHotelName());
        holder.mHotelLocality.setText(hotelDetailsArrayList.get(position).getLocalty());
    }

    @Override
    public int getItemCount() {
        return hotelDetailsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mHotelName,mHotelLocality;
        public ViewHolder(View itemView) {
            super(itemView);
            mHotelName = itemView.findViewById(R.id.hotel_name);
            mHotelLocality = itemView.findViewById(R.id.hotel_place);
        }
    }
}
