package app.zingo.com.billgenerate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.zingo.com.billgenerate.CustomViews.RecyclerViewClickListener;
import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Rooms;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Activiies.SettleAudit;
import app.zingo.com.billgenerate.Utils.Util;
import app.zingo.com.billgenerate.WebApis.TravellerApi;
//import app.zingo.com.billgenerate.Activiies.SettleAudit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by new on 2/26/2018.
 */

public class SettlementAdapters extends RecyclerView.Adapter<SettlementAdapters.ViewHolder> {

    private Context context;
    private ArrayList<Bookings1> bookingsArrayList;
    private static RecyclerViewClickListener itemListener;


    // RecyclerView account_list_settle;

    public SettlementAdapters(Context context, ArrayList<Bookings1> bookingsArrayList)
    {
        this.context = context;
        this.bookingsArrayList = bookingsArrayList;

    }
    @Override
    public SettlementAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settle_detail_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SettlementAdapters.ViewHolder holder, final int position) {

        final Bookings1 bookings1 = bookingsArrayList.get(position);

        if(bookings1 != null && bookings1.getTravellerId() != 0)
        {
            holder.mSerialNo.setText((position+1)+"");
            holder.mSell.setText(""+bookings1.getSellRate());
            holder.mGst.setText(""+bookings1.getGstAmount());
            holder.mExtra.setText(""+bookings1.getExtraCharges());
            holder.mTotal.setText(""+bookings1.getTotalAmount());

            if(bookings1.getAuditSettlementList().size()!=0){
                holder.mAuditSell.setText(""+bookings1.getAuditSettlementList().get(0).getAuditingSellRate());
                 holder.mAuditGst.setText(""+bookings1.getAuditSettlementList().get(0).getAuditingGST());
                 holder.mAuditExtra.setText(""+bookings1.getAuditSettlementList().get(0).getAuditingExtra());
                 holder.mDiff.setText(""+bookings1.getAuditSettlementList().get(0).getDifferenceAmount());
                 holder.mMode.setText(""+bookings1.getAuditSettlementList().get(0).getPaymentMode());
                 holder.mStatus.setText(""+bookings1.getAuditSettlementList().get(0).getPaymentStatus());
                 String dates = bookings1.getAuditSettlementList().get(0).getSettlementDate();
                 String[] date = dates.split("T");
                  holder.mAuditDate.setText(""+date[0]);
                holder.mAuditTime.setText(""+date[1]);
                  holder.mAuditBy.setText(""+bookings1.getAuditSettlementList().get(0).getCreatedBy());
                holder.mRemark.setText(""+bookings1.getAuditSettlementList().get(0).getRemark());
            }else{
                holder.mRemark.setText("NA Pending");
            }
            // holder.mMode.setText(""+bookings1.get());
            // holder.mStatus.setText(""+bookings1.getSellRate());
            //holder.mAuditSell.setText(""+bookings1.getSellRate());
            // holder.mAuditGst.setText(""+bookings1.getSellRate());
            // holder.mAuditExtra.setText(""+bookings1.getSellRate());
            // holder.mDiff.setText(""+bookings1.getSellRate());
            //  holder.mAuditDate.setText(""+bookings1.getSellRate());
            // holder.mAuditTime.setText(""+bookings1.getSellRate());
            //  holder.mAuditBy.setText(""+bookings1.getSellRate());
            // holder.mRemark.setText(""+bookings1.getSellRate());
            holder.mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SettleAudit.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("BookingId",bookingsArrayList.get(position).getBookingId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });

            try {
                String checkdate = new SimpleDateFormat("dd MMM yy")
                        .format(new SimpleDateFormat("MM/dd/yyyy").parse(bookings1.getCheckInDate()));
                holder.mCheckInDate.setText(checkdate);

                String checkoutdate = new SimpleDateFormat("dd MMM yy")
                        .format(new SimpleDateFormat("MM/dd/yyyy").parse(bookings1.getCheckOutDate()));
                holder.mCheckOutDate.setText(checkoutdate);
                getRoomNo(bookings1.getRoomId(),holder.mRoomNo);
                getTravellerName(bookings1.getTravellerId(),holder.mGuest);




            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public int getItemCount() {
        return bookingsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mSerialNo,mRoomNo,mCheckInDate,mCheckOutDate,mGuest,mSell,mExtra,mGst,
        mTotal,mMode,mStatus,mAuditSell,mAuditGst,mAuditExtra,mDiff,mAuditDate,mAuditTime,mAuditBy,mRemark,mCheck;


        public ViewHolder(View itemView) {
            super(itemView);
            mSerialNo = (TextView) itemView.findViewById(R.id.settle_list_sno);
            mRoomNo = (TextView) itemView.findViewById(R.id.settle_list_room);
            mCheckInDate = (TextView) itemView.findViewById(R.id.settle_list_cit);
            mCheckOutDate = (TextView) itemView.findViewById(R.id.settle_list_cot);
            mGuest = (TextView) itemView.findViewById(R.id.settle_list_guest);
            mSell = (TextView) itemView.findViewById(R.id.settle_list_sell);
            mExtra = (TextView) itemView.findViewById(R.id.settle_list_extra);
            mGst = (TextView) itemView.findViewById(R.id.settle_list_gst);
            mTotal = (TextView) itemView.findViewById(R.id.settle_list_total);
            mMode = (TextView) itemView.findViewById(R.id.settle_list_mode);
            mStatus = (TextView) itemView.findViewById(R.id.settle_list_status);
            mAuditSell = (TextView) itemView.findViewById(R.id.settle_list_aSell);
            mAuditGst = (TextView) itemView.findViewById(R.id.settle_list_aGst);
            mAuditExtra = (TextView) itemView.findViewById(R.id.settle_list_aExtra);
            mDiff = (TextView) itemView.findViewById(R.id.settle_list_aDiff);
            mAuditDate = (TextView) itemView.findViewById(R.id.settle_list_aDate);
            mAuditTime = (TextView) itemView.findViewById(R.id.settle_list_aTime);
            mAuditBy = (TextView) itemView.findViewById(R.id.settle_list_aBy);
            mRemark = (TextView) itemView.findViewById(R.id.settle_list_aRemark);
            mCheck = (TextView) itemView.findViewById(R.id.settle_list_aCheck);


           /* itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    posAdapter = getAdapterPosition();

                    // check if item still exists
                    if(posAdapter != RecyclerView.NO_POSITION){
                        Bookings1 clickedDataItem = bookingsArrayList.get(posAdapter);
                        System.out.println("Adap=="+posAdapter);
                        Toast.makeText(v.getContext(), "You clicked " + posAdapter, Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getPosition());
        }
    }

    public  void getRoomNo(final int i, final TextView t)
    {
        String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        LoginApi api = Util.getClient().create(LoginApi.class);
        Call<Rooms> getRoom = api.getRoom(auth_string,i);

        getRoom.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                if(response.code() == 200)
                {
                    if(response.body() != null)
                    {
                        t.setText(response.body().getRoomNo());
                    }

                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public  void getTravellerName(final int i, final TextView t)
    {
        String auth_string = Util.getToken(context);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        TravellerApi api = Util.getClient().create(TravellerApi.class);
        Call<Traveller> getTrav = api.getTravellerDetails(auth_string,i);

        getTrav.enqueue(new Callback<Traveller>() {
            @Override
            public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                if(response.code() == 200)
                {
                    if(response.body() != null)
                    {
                        t.setText(response.body().getFirstName());
                    }

                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Traveller> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }



}