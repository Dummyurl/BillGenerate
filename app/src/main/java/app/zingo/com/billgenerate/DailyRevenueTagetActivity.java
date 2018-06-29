package app.zingo.com.billgenerate;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.zingo.com.billgenerate.Utils.DataBaseHelper;
import app.zingo.com.billgenerate.Utils.PreferenceHandler;

public class DailyRevenueTagetActivity extends AppCompatActivity {

    LinearLayout mCompitionLinearLayout,mRatesLinearlayout,mAvailableLinearLayout;
    Button mSet;
    EditText mRoomCount,mTargetPrice;
    RecyclerView mRecyclerView;

    private RoomAdapter adapter;
    RoomAdapter.ViewHolder viewHolder;

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_daily_revenue_taget);
            setTitle("Target");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            dbHelper = new DataBaseHelper(this);

            mCompitionLinearLayout = (LinearLayout)findViewById(R.id.competition_linear_layout);
            mRatesLinearlayout = (LinearLayout)findViewById(R.id.rates_linear_layout);
            mAvailableLinearLayout = (LinearLayout)findViewById(R.id.competition_status_linear_layout);
            mRoomCount = (EditText) findViewById(R.id.total_rooms);
            mTargetPrice = (EditText) findViewById(R.id.target_price);
            mSet = (Button) findViewById(R.id.btnSet);
            mRecyclerView = (RecyclerView) findViewById(R.id.rate_list);

            mSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String roomCount = mRoomCount.getText().toString();
                    String price = mTargetPrice.getText().toString();
                    if(roomCount==null||roomCount.isEmpty()){
                        mRoomCount.setError("Enter Room Count");
                    }else if(price==null||price.isEmpty()){
                        mTargetPrice.setError("Enter Target Price");
                    }else{

                        int count = Integer.parseInt(roomCount);
                        int room = count;
                        adapter = new RoomAdapter(DailyRevenueTagetActivity.this,count,Double.parseDouble(price));
                        mRecyclerView.setAdapter(adapter);
                        mRoomCount.setText(""+room);



                    }


                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.submit_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void onAddField() {




        /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.competition_linear_layout_, null);
        // Add the new row before the add field button.


        mCompitionLinearLayout.addView(rowView);
        mCompitionLinearLayout.setEnabled(false);
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView1 = inflater1.inflate(R.layout.competition_linear_layout_, null);

        mRatesLinearlayout.addView(rowView1);
        mRatesLinearlayout.setEnabled(false);


        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView2 = inflater1.inflate(R.layout.competition_linear_layout_, null);

        mAvailableLinearLayout.addView(rowView2);*/
        double price = Double.parseDouble(mTargetPrice.getText().toString());
        if(mRoomCount.getText().toString()!=null&&!mRoomCount.getText().toString().isEmpty()){

            int roomCount = Integer.parseInt(mRoomCount.getText().toString());
            if(mRecyclerView.getChildCount()!=roomCount){

                adapter = new RoomAdapter(DailyRevenueTagetActivity.this,mRecyclerView.getChildCount(),price);
                mRecyclerView.setAdapter(adapter);
                mRoomCount.setText(""+mRecyclerView.getChildCount());
              //  calculation(mRecyclerView.getChildCount());

            }else{
                adapter = new RoomAdapter(DailyRevenueTagetActivity.this,(roomCount+1),price);
                mRecyclerView.setAdapter(adapter);
                mRoomCount.setText(""+(roomCount+1));
                //calculation(roomCount+1);

            }

        }else{
            adapter = new RoomAdapter(DailyRevenueTagetActivity.this,1,price);
            mRecyclerView.setAdapter(adapter);
            mRoomCount.setText("1");
          //  calculation(1);
        }


    }

    public void removeView() {
        double price = Double.parseDouble(mTargetPrice.getText().toString());

        int no = mRecyclerView.getChildCount();
        if(no >0)
        {
            int roomCount = Integer.parseInt(mRoomCount.getText().toString());
            mRoomCount.setText(""+(roomCount-1));
            /*System.out.println(mCompitionLinearLayout.getChildAt(no-1));*/
           /* mCompitionLinearLayout.removeView(mCompitionLinearLayout.getChildAt(no-1));
            mRatesLinearlayout.removeView(mRatesLinearlayout.getChildAt(no-1));
            mAvailableLinearLayout.removeView(mAvailableLinearLayout.getChildAt(no-1));*/
            adapter = new RoomAdapter(DailyRevenueTagetActivity.this,(roomCount-1),price);
            mRecyclerView.setAdapter(adapter);

           // calculation(roomCount-1);
        }
        else
        {
            Toast.makeText(DailyRevenueTagetActivity.this,"No views available",Toast.LENGTH_SHORT).show();
            mSet.setVisibility(View.VISIBLE);
            mRoomCount.setEnabled(true);
            mTargetPrice.setEnabled(true);
        }

    }

    public void dataBaseFunction(final int count){

        if(dbHelper.exists("property"+ PreferenceHandler.getInstance(DailyRevenueTagetActivity.this).getUserId())){

            if(dbHelper.getAllProperty()!=null){

                double targetPrice = Double.parseDouble(mTargetPrice.getText().toString());
                double avgPrice = targetPrice/count;

                for(int i=0;i<count;i++){
                    dbHelper.updateProperty(""+(i+1),avgPrice);
                }

            }else{

                double targetPrice = Double.parseDouble(mTargetPrice.getText().toString());
                double avgPrice = targetPrice/count;

                for(int i=0;i<count;i++){
                    dbHelper.insertProperty(""+(i+1),avgPrice,0);
                }


            }


        }else{
            Toast.makeText(DailyRevenueTagetActivity.this, "Please Refresh", Toast.LENGTH_SHORT).show();
        }

    }

    public void calculation(final int position){

        DecimalFormat df = new DecimalFormat("#.##");
        double sell=0;
        double balanceTarget=0,balnzavgTarget=0;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            viewHolder = (RoomAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
            TextView targetPrice = viewHolder.mTargetPrices;
            if(i==0){

            }
            System.out.println("Item = "+i+""+df.format(balnzavgTarget));

            EditText sell_amount =  viewHolder.mSellPrice;
            targetPrice.setText(""+df.format(balnzavgTarget));
            double sellRate =0;


         //   double total = Double.parseDouble(targetPrice.getText().toString());
            double target = Double.parseDouble(mTargetPrice.getText().toString());
            if(sell_amount.getText().toString()!=null&&!sell_amount.getText().toString().isEmpty()){
                 sellRate = Double.parseDouble(sell_amount.getText().toString());
            }else{
                 sellRate = 0;
            }

          //  double avgPrice = total/adapter.getItemCount();
            sell = sell + sellRate;
            balanceTarget = target -sell;
            balnzavgTarget = balanceTarget/(adapter.getItemCount()-position);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.action_add:
                //logout();

                if(mTargetPrice.getText().toString()==null||mTargetPrice.getText().toString().isEmpty()){
                    mTargetPrice.setError("Please enter value");
                }else {
                    onAddField();
                }

                return true;
            case R.id.action_delete:
                //logout();
                if(mTargetPrice.getText().toString()==null||mTargetPrice.getText().toString().isEmpty()){
                    mTargetPrice.setError("Please enter value");
                }else {
                    removeView();
                }

                return true;
            case android.R.id.home:
                // app icon action bar is clicked; go to parent activity

                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{

        private Context context;

        private ArrayList<String> list;
        private double targetPrice;
        public int size;


        public RoomAdapter(Context context,int size,double targetPrice) {

            this.context = context;
           // this.list = list;
            this.size = size;
            this.targetPrice = targetPrice;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            try
            {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_room_target, parent, false);
                ViewHolder viewHolder = new ViewHolder(v);

                return viewHolder;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            DecimalFormat df = new DecimalFormat("#.##");

            try {
               holder.mRoomNum.setText(""+(position+1));

               double avgPrice = targetPrice/size;

               holder.mTargetPrices.setText(""+df.format(avgPrice));
              // holder.mRoomNum.setText(""+(position+1));
                holder.mSellPrice.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        double sellPrice = Double.parseDouble(holder.mSellPrice.getText().toString());
                        calculation((position+1));
                    }

                    public void afterTextChanged(Editable s) {

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            dbHelper.insertProperty(holder.mRoomNum.getText().toString(),Double.parseDouble(holder.mTargetPrices.getText().toString()),Double.parseDouble(holder.mSellPrice.getText().toString()));

                                        } catch(Exception nfe) {
                                            nfe.printStackTrace();
                                        }

                                    }
                                });
                            }
                        }, 1000);


                    }
                });
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {

            return size;

        }


        class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

            public EditText mSellPrice;
            public TextView mRoomNum,mTargetPrices;


            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setClickable(true);
                mRoomNum = (TextView) itemView.findViewById(R.id.target_room_edit_text);

                mTargetPrices = (TextView) itemView.findViewById(R.id.target_rates_edit_text);

                mSellPrice = (EditText) itemView.findViewById(R.id.target_sell_edit_text);


            }
        }

    }

}
