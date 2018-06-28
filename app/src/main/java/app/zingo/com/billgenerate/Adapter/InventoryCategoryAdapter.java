package app.zingo.com.billgenerate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.RoomCategories;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Util;

/**
 * Created by ZingoHotels Tech on 19-06-2018.
 */

public class InventoryCategoryAdapter extends RecyclerView.Adapter<InventoryCategoryAdapter.ViewHolder>{

    private Context context;
    private ArrayList<RoomCategories> list;
    private ArrayList<String> countList;
    private long dateTime;
    public InventoryCategoryAdapter(Context context, ArrayList<RoomCategories> list,ArrayList<String> countList,long dateTime) {

        this.context = context;
        this.list = list;
        this.countList = countList;
        this.dateTime = dateTime;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_inventory_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomCategories dto = list.get(position);
        String count = countList.get(position);
        System.out.println("Count=="+count);
        holder.category_name.setText(dto.getCategoryName());
        holder.category_count.setText(count);

        long date = Util.PreferenceHandler.getInstance(context).getInventoryTime("CategoryTime"+dto.getRoomCategoryId());

        if(date==0){
            Util.PreferenceHandler.getInstance(context).setInventoryTime("CategoryTime"+dto.getRoomCategoryId(),dateTime);
            Util.PreferenceHandler.getInstance(context).setInventory("Category"+dto.getRoomCategoryId(),count);
        }else{
            if(date<=dateTime){
                Util.PreferenceHandler.getInstance(context).setInventory("Category"+dto.getRoomCategoryId(),count);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView category_name;
        EditText category_count;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            category_name = (TextView) itemView.findViewById(R.id.category_name);
            category_count = (EditText) itemView.findViewById(R.id.category_count);

        }
    }
}
