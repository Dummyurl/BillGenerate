package app.zingo.com.billgenerate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.com.billgenerate.Model.NotificationManager;
import app.zingo.com.billgenerate.Utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDetailsActivity extends AppCompatActivity {

    TextView mAcceptedCount,mAcceptedCountByAgent,mRejectedCount,mRejectedCountByAgent,mSuggestedCount,mHotelName,mRequestCount;
    LinearLayout mAcceptedCountParent,mAcceptedCountByAgentParent,mRejectedCountParent
            ,mRejectedCountByAgentParent,mSuggestedCountParent,mRequestCountParent;

    int hotelId;
    String hotelName;

    ArrayList<NotificationManager> accepted;
    ArrayList<NotificationManager> suggested;
    ArrayList<NotificationManager> rejected;
    ArrayList<NotificationManager> acceptedByAgent;
    ArrayList<NotificationManager> rejectedByAgent;
    ArrayList<NotificationManager> requestByAgent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Notification Details");

        mAcceptedCount = findViewById(R.id.accepted_notify_count);
        mRejectedCount = findViewById(R.id.rejected_notify_count);
        mSuggestedCount = findViewById(R.id.suggested_notify_count);
        mHotelName = findViewById(R.id.selected_hotel_name);
        mAcceptedCountByAgent = findViewById(R.id.accepted_notify_count_by_agent);
        mRejectedCountByAgent = findViewById(R.id.rejected_notify_count_by_agent);
        mRequestCount = findViewById(R.id.request_notify_count);

        mAcceptedCountParent = findViewById(R.id.accepted_by_hotel_parent);
        mAcceptedCountByAgentParent = findViewById(R.id.accepted_notify_count_by_agent_parent);
        mRejectedCountParent = findViewById(R.id.rejected_notify_count_parent);
        mRejectedCountByAgentParent = findViewById(R.id.rejected_notify_count_by_agent_parent);
        mSuggestedCountParent = findViewById(R.id.suggested_notify_count_parent);
        mRequestCountParent = findViewById(R.id.request_by_agent_parent);

        hotelId = getIntent().getIntExtra("HotelId",0);
        hotelName = getIntent().getStringExtra("HotelName");

        mHotelName.setText(hotelName);

        mAcceptedCountByAgentParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationDetailsActivity.this,BidNotificationListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NotificationList",acceptedByAgent);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mAcceptedCountParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationDetailsActivity.this,BidNotificationListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NotificationList",accepted);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRejectedCountParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationDetailsActivity.this,BidNotificationListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NotificationList",rejected);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRejectedCountByAgentParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationDetailsActivity.this,BidNotificationListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NotificationList",rejectedByAgent);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mSuggestedCountParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationDetailsActivity.this,BidNotificationListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NotificationList",suggested);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRequestCountParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationDetailsActivity.this,BidNotificationListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NotificationList",requestByAgent);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        getNotifications();
    }


    private void getNotifications(){
        final ProgressDialog progressDialog = new ProgressDialog(NotificationDetailsActivity.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                NotificationManagerApi apiService =
                        Util.getClient().create(NotificationManagerApi.class);
                String authenticationString = Util.getToken(NotificationDetailsActivity.this);
                Call<ArrayList<NotificationManager>> call = apiService.getNotification(authenticationString)/*getRooms()*/;

                call.enqueue(new Callback<ArrayList<NotificationManager>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NotificationManager>> call, Response<ArrayList<NotificationManager>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                        if (statusCode == 200) {

                            try{
                                ArrayList<NotificationManager> list =  response.body();
                                //ArrayList<NotificationManager> nfm = new ArrayList<>();

//                            docList = list.get(1).getProfileList();
                                accepted = new ArrayList<>();
                                suggested = new ArrayList<>();
                                rejected = new ArrayList<>();
                                acceptedByAgent = new ArrayList<>();
                                rejectedByAgent = new ArrayList<>();
                                requestByAgent = new ArrayList<>();

                                if(list != null && list.size() != 0)
                                {

                                    for(int i=0;i<list.size();i++){
                                        NotificationManager notificationManager = list.get(i);
                                        if(list.get(i).getHotelId()==hotelId){
                                            System.out.println("Title = "+notificationManager.getNotificationText());
                                            if(notificationManager.getNotificationText().contains("Bidding Request Accepted"))
                                            {
                                                accepted.add(list.get(i));
                                            }
                                            else if(notificationManager.getNotificationText().contains("Bidding Request Reply"))
                                            {
                                                suggested.add(list.get(i));
                                            }
                                            else if(notificationManager.getNotificationText().contains("Your request is accepted by"))
                                            {
                                                acceptedByAgent.add(list.get(i));
                                            }
                                            else if(notificationManager.getNotificationText().contains("Bidding Request Rejected By"))
                                            {
                                                rejectedByAgent.add(list.get(i));
                                            }
                                            else if(notificationManager.getNotificationText().contains("Bidding Request Rejected"))
                                            {
                                                rejected.add(list.get(i));
                                            }
                                            else if(notificationManager.getNotificationText().contains("Agent Booking Request"))
                                            {
                                                requestByAgent.add(list.get(i));
                                            }
                                        }
                                    }

                                    mAcceptedCount.setText(accepted.size()+"");
                                    mSuggestedCount.setText(suggested.size()+"");
                                    mAcceptedCountByAgent.setText(acceptedByAgent.size()+"");
                                    mRejectedCountByAgent.setText(rejectedByAgent.size()+"");
                                    mRejectedCount.setText(rejected.size()+"");
                                    mRequestCount.setText(requestByAgent.size()+"");




                                }
                                else
                                {
                                    Toast.makeText(NotificationDetailsActivity.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                                }
                            }catch(Exception e){
                                e.printStackTrace();

                            }


                        }else {


                            Toast.makeText(NotificationDetailsActivity.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<NotificationManager>> call, Throwable t) {

                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                NotificationDetailsActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
