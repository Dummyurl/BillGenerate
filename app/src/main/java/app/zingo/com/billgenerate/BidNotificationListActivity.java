package app.zingo.com.billgenerate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import app.zingo.com.billgenerate.Model.NotificationManager;

import app.zingo.com.billgenerate.Adapter.BidNotificationManagerAdapter;
/*import app.zingo.com.hotelmanagement.Adapter.NotificationManagerAdapter;
import app.zingo.com.hotelmanagement.Util.PreferenceHandler;*/
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidNotificationListActivity extends AppCompatActivity {

    private RecyclerView notification_list;
    private ImageView mNoData;

    private BidNotificationManagerAdapter adapter;
    private ArrayList<NotificationManager> list;

    private ProgressDialog progressDialog;
    String activity;

    ArrayList<NotificationManager> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            setContentView(R.layout.activity_notification_list);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Notifications");
            notification_list = (RecyclerView) findViewById(R.id.notification_list);
            mNoData = (ImageView) findViewById(R.id.notification_no_result_image);

            if(getIntent().getExtras()!=null){
                activity = getIntent().getStringExtra("Activity");
            }

            Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                arrayList = (ArrayList<NotificationManager>) bundle.getSerializable("NotificationList");
            }
            //getNotification();
           if(arrayList != null && arrayList.size() != 0)
           {
               for(int i=0;i<arrayList.size();i++)
               {
                   System.out.println("Text = "+arrayList.get(i).getNotificationText());
               }
               mNoData.setVisibility(View.GONE);
               notification_list.setVisibility(View.VISIBLE);
               adapter = new BidNotificationManagerAdapter(BidNotificationListActivity.this,arrayList);
               notification_list.setAdapter(adapter);
           }
        }catch(Exception e){
            e.printStackTrace();
        }


       // getHotels();

    }

    /*private void getNotification(){
        final ProgressDialog progressDialog = new ProgressDialog(NotificationListActivity.this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        PreferenceHandler ph = PreferenceHandler.getInstance(this);
        final int hotelId = ph.getHotelID();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                NotificationManagerApi apiService =
                        Util.getClient().create(NotificationManagerApi.class);
                String authenticationString = Util.getToken(NotificationListActivity.this);
                Call<ArrayList<NotificationManager>> call = apiService.getNotification(authenticationString)*//*getRooms()*//*;

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
                                list =  response.body();
                                ArrayList<NotificationManager> nfm = new ArrayList<>();

//                            docList = list.get(1).getProfileList();

                                if(list != null && list.size() != 0)
                                {

                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).getHotelId()==hotelId&&!list.get(i).getNotificationText().equalsIgnoreCase("Please Update Inventory")&&!list.get(i).getNotificationText().contains("competative analysis report for the day")&&!list.get(i).getNotificationText().contains("Dear !")){
                                            nfm.add(list.get(i));
                                        }
                                    }

                                    if(nfm!=null && nfm.size()!=0){

                                        Collections.reverse(nfm);



                                    }


                                }
                                else
                                {
                                *//*Intent intent = new Intent(NotificationListActivity.this,AddRoomsActivity.class);
                                startActivity(intent);
                                NotificationListActivity.this.finish();*//*
                                    mNoData.setVisibility(View.VISIBLE);
                                    notification_list.setVisibility(View.GONE);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                mNoData.setVisibility(View.VISIBLE);
                                notification_list.setVisibility(View.GONE);
                            }


//                            Object dto = response.body();
//                            listCities.add(dto);



                        }else {

                            mNoData.setVisibility(View.VISIBLE);
                            notification_list.setVisibility(View.GONE);
                            //Toast.makeText(NotificationListActivity.this, " failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<NotificationManager>> call, Throwable t) {
                        // Log error here since request failed
                        mNoData.setVisibility(View.VISIBLE);
                        notification_list.setVisibility(View.GONE);
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }*/
    //777774
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
               //back();
                BidNotificationListActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //back();
    }

    /*public void back(){

        if(activity.equalsIgnoreCase("DashBoard")){
            Intent back = new Intent(NotificationListActivity.this, MainActivity.class);
            startActivity(back);
            NotificationListActivity.this.finish();
        }else{
            Intent back = new Intent(NotificationListActivity.this, NotificationOptionsActivity.class);
            startActivity(back);
            NotificationListActivity.this.finish();
        }

    }*/

}
