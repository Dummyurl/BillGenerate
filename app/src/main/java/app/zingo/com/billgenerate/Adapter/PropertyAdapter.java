package app.zingo.com.billgenerate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.R;

/**
 * Created by ZingoHotels Tech on 04-04-2018.
 */

public class PropertyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HotelDetails> mList = new ArrayList<>();

    public PropertyAdapter(Context context, ArrayList<HotelDetails> mList)
    {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.adapter_property,viewGroup,false);
        }

        TextView mHotelName = (TextView) view.findViewById(R.id.hotel_name);


        mHotelName.setText(mList.get(pos).getHotelDisplayName().toString());


        return view;
    }
}
