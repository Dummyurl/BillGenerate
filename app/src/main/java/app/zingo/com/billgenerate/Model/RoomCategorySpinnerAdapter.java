package app.zingo.com.billgenerate.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.billgenerate.R;

/**
 * Created by ZingoHotels Tech on 13-06-2018.
 */

public class RoomCategorySpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RoomCategories> mList = new ArrayList<>();

    public RoomCategorySpinnerAdapter(Context context, ArrayList<RoomCategories> mList)
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
            view = mInflater.inflate(R.layout.category_name_spinner,viewGroup,false);
        }

        TextView mCityName = (TextView) view.findViewById(R.id.category_name);


        mCityName.setText(mList.get(pos).getCategoryName().toString());

        return view;
    }
}
