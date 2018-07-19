package app.zingo.com.billgenerate.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.billgenerate.R;

/**
 * Created by ZingoHotels Tech on 12-07-2018.
 */

public class AdapterDevices extends RecyclerView.Adapter<AdapterDevices.ViewHolder> {

    private Context context;
    private ArrayList<BluetoothDevice> mList;


    public AdapterDevices(Context context, ArrayList<BluetoothDevice> mList)
    {
        this.context = context;
        this.mList = mList;
        setHasStableIds(true);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_device_list,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final BluetoothDevice bluetoothDevice = mList.get(position);
        holder.mDeviceName.setText(""+bluetoothDevice.getName());


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mDeviceName;

        public ViewHolder(View itemView) {
            super(itemView);

            mDeviceName = (TextView) itemView.findViewById(R.id.device_item);

        }
    }



}
