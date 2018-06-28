package app.zingo.com.billgenerate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by ZingoHotels.com on 04-01-2018.
 */

public class GeneralAdapter extends BaseAdapter {


    private Context context;
    private String[] list;

    public GeneralAdapter(Context context, String[] list)
    {
        this.context = context;
        this.list = list;
    }



    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.general_adapter_layout,viewGroup,false);
        }

        TextView tabName = (TextView) view.findViewById(R.id.adapter_spinner_item);
        tabName.setText(list[i].toString());

        return view;
    }
}
