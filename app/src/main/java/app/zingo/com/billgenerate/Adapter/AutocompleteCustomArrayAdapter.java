package app.zingo.com.billgenerate.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.billgenerate.BillDetails;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.R;


/**
 * Created by ZingoHotels.com on 5/30/2018.
 */

public class AutocompleteCustomArrayAdapter extends ArrayAdapter<Traveller> {

    final String TAG = "AutocompleteCustomArrayAdapter.java";

    Context mContext;
    int layoutResourceId;
    ArrayList<Traveller> data = null;

    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId, ArrayList<Traveller> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{

            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((BillDetails) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position
            Traveller objectItem = data.get(position);

            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.added_hotel_name);
            textViewItem.setText(objectItem.getFirstName());

            // in case you want to add some style, you can do something like:
            textViewItem.setBackgroundColor(Color.CYAN);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }
}
