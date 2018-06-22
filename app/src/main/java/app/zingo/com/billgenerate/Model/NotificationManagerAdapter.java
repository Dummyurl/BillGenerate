package app.zingo.com.billgenerate.Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.com.billgenerate.BillDetails;
import app.zingo.com.billgenerate.InventoryCategoryDetailsActivity;
import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by ZingoHotels Tech on 10-05-2018.
 */

public class NotificationManagerAdapter extends RecyclerView.Adapter<NotificationManagerAdapter.ViewHolder>{

    private Context context;
    private ArrayList<NotificationManager> list;
    public NotificationManagerAdapter(Context context, ArrayList<NotificationManager> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        try{
             v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_notification_manager, parent, false);

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final int hotelID;

        try{
            final NotificationManager dto = list.get(position);
            final String message = dto.getNotificationFor();

            if(message.contains("-")) {
                String category[] = message.split("-");
                
                if (category.length == 4) {

                    String hotel = category[0];
                    String notifyDate = category[2];
                    String hotelName = category[3];

                    hotelID = Integer.parseInt(hotel);
                    
                    holder.mDate.setText(""+notifyDate);
                    holder.mHotelName.setText(""+hotelName);
                    //getHotelName(hotelID,holder.mHotelName);
                    
                    
                }
            }


            holder.mNotificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(message.contains("-")) {
                        String category[] = message.split("-");

                        if (category.length == 4) {

                            String hotel = category[0];
                            String pass = category[1];
                            String notifyDate = category[2];

                            Intent intent = new Intent(context, InventoryCategoryDetailsActivity.class);
                            intent.putExtra("HotelId",Integer.parseInt(hotel));
                            intent.putExtra("Pass",pass);
                            intent.putExtra("DateTime",notifyDate);
                            intent.putExtra("HotelName",holder.mHotelName.getText().toString());
                            context.startActivity(intent);
                            ((Activity)context).finish();

                        }
                    }




                }
            });





        }catch (Exception e){

        }



    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public TextView mTitle,mHotelName,mDate;
        CardView mNotificationLayout;
        


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            try{
                
                mTitle = (TextView)itemView.findViewById(R.id.title);
                mHotelName = (TextView)itemView.findViewById(R.id.brief_detail_hotel_name);
                mDate = (TextView)itemView.findViewById(R.id.booking_date);
                mNotificationLayout = (CardView) itemView.findViewById(R.id.notification_layout);


            }catch(Exception e){
                e.printStackTrace();
            }



        }
    }

    private void getHotelName(final int id,final TextView textView) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(context);
                Call<HotelDetails> call = apiService.getHotelsById(authenticationString, id);

                call.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog != null)
                                progressDialog.dismiss();


                            if (response.body() != null) {

                                textView.setText(""+response.body().getHotelName());

                            }

//


                        } else {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(context, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }


}
