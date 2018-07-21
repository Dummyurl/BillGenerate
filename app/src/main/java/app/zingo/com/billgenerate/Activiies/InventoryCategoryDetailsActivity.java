package app.zingo.com.billgenerate.Activiies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.billgenerate.LoginApi;
import app.zingo.com.billgenerate.Model.HotelDetails;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.PreferenceHandler;
import app.zingo.com.billgenerate.Model.RoomCategories;
import app.zingo.com.billgenerate.Model.Rooms;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryCategoryDetailsActivity extends AppCompatActivity {
    
    TextView mHotelName,mDateTime;
    RecyclerView mCategory_list;
    Button mUpdate;
    InventoryCategoryAdapter adapter;

    int hotelId;
    String hotelName;
    String pass,notifyDate;
    ArrayList<String> countList;
    ArrayList<Rooms> roomsArrayList;
    ArrayList<Rooms> roomList;
    private ArrayList<RoomCategories> roomCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_inventory_category_details);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Inventory Details");
            
            mHotelName = (TextView)findViewById(R.id.brief_detail_hotel_name);
            mDateTime = (TextView)findViewById(R.id.date_time);
            mCategory_list = (RecyclerView) findViewById(R.id.notification_list);
            mUpdate = (Button) findViewById(R.id.update_inventory);



            hotelId = getIntent().getExtras().getInt("HotelId");
            hotelName = getIntent().getExtras().getString("HotelName");
            pass = getIntent().getExtras().getString("Pass");
            notifyDate = getIntent().getExtras().getString("DateTime");

            countList = new ArrayList<>();



            if(hotelName!=null||!hotelName.isEmpty()){
                mHotelName.setText(""+hotelName);
            }

            if(notifyDate!=null||!notifyDate.isEmpty()){
                mDateTime.setText(""+notifyDate);
            }

            if(hotelId!=0){
                System.out.println("ArrayList size main=="+countList.size());
                categoryList();
            }

            mUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                    if(roomCategories!=null&&roomCategories.size()!=0){
                        if(roomsArrayList!=null&&roomsArrayList.size()!=0){
                            for(int i=0;i<roomCategories.size();i++){
                                roomList = new ArrayList<>();
                                int inventoryCount = Integer.parseInt(countList.get(i));
                                System.out.println("Inventory Count Cate=="+inventoryCount);

                                for(int j=0;j<roomsArrayList.size();j++){


                                    if((roomCategories.get(i).getRoomCategoryId()==roomsArrayList.get(j).getRoomCategoryId())
                                            &&(roomsArrayList.get(j).getStatus().equalsIgnoreCase("Available")||roomsArrayList.get(j).getStatus().equalsIgnoreCase("Unclean")||roomsArrayList.get(j).getStatus().equalsIgnoreCase("Blocked"))){
                                        roomList.add(roomsArrayList.get(j));
                                    }
                                }

                                if(roomList!=null&&roomList.size()!=0&&inventoryCount<=roomList.size()){
                                    // roomsArrayList = new ArrayList<>();
                                    System.out.println("Before Room SIze=="+roomsArrayList.size());
                                    System.out.println("After Room SIze=="+roomList.size());
                                    for(int k=0;k<(roomList.size()+1);k++){

                                        if(k>=roomList.size()){
                                            Intent main = new Intent(InventoryCategoryDetailsActivity.this,NotificationListActivity.class);
                                            startActivity(main);
                                            InventoryCategoryDetailsActivity.this.finish();
                                        }else{
                                            Rooms rooms = roomList.get(k);
                                            if(k<inventoryCount){
                                                rooms.setStatus("Available");
                                            }else{
                                                rooms.setStatus("Blocked");
                                            }

                                            updateRoom(rooms);
                                        }

                                    }

                                }else{
                                    Toast.makeText(InventoryCategoryDetailsActivity.this, "Inventory count more than Room Available", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }else{

                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void categoryList()
    {
        final ProgressDialog dialog = new ProgressDialog(InventoryCategoryDetailsActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final LoginApi getRoomCategories = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(InventoryCategoryDetailsActivity.this);
                Call<ArrayList<RoomCategories>> getRoomCategoriesResponse = getRoomCategories.
                        fetchRoomCategoriesByHotelId(authenticationString, hotelId);


                getRoomCategoriesResponse.enqueue(new Callback<ArrayList<RoomCategories>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RoomCategories>> call, Response<ArrayList<RoomCategories>> response) {

                        try
                        {
                            if(dialog != null)
                            {
                                dialog.dismiss();
                            }
                            if(response.code() == 200)
                            {
                                if(response.body() != null && response.body().size() != 0)
                                {

                                    if(pass.contains(",")){
                                        String split[] = pass.split(",");
                                        if(split.length!=0){
                                            for(int i=0;i<split.length;i++){
                                                countList.add(split[i]);
                                            }
                                        }
                                    }else{
                                        countList.add(pass);
                                    }
                                    System.out.println("ArrayList size inside=="+countList.size());
                                    if(countList!=null&&countList.size()!=0&&response.body().size()==countList.size()){
                                        //notifyDate
                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
                                        Date date = sdf.parse(notifyDate);

                                        long dateTime = date.getTime();
                                        roomCategories = response.body();

                                        adapter = new InventoryCategoryAdapter(InventoryCategoryDetailsActivity.this,response.body(),countList,dateTime);
                                        mCategory_list.setAdapter(adapter);
                                        getHotels();
                                    }

                                }
                                else
                                {


                                    Toast.makeText(InventoryCategoryDetailsActivity.this, "No categories", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RoomCategories>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(InventoryCategoryDetailsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }


    private void getHotels() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(InventoryCategoryDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi hotelOperation = Util.getClient().create(LoginApi.class);
                Call<HotelDetails> response = hotelOperation.getHotelsById(auth_string,hotelId);

                response.enqueue(new Callback<HotelDetails>() {
                    @Override
                    public void onResponse(Call<HotelDetails> call, Response<HotelDetails> response) {
                        System.out.println("GetHotelByProfileId = " + response.code());


                        if (progressDialog != null)
                            progressDialog.dismiss();
                        try{
                            if (response.code() == 200||response.code() == 201||response.code() == 204) {

                                roomsArrayList = response.body().getRooms();




                            } else {
                                Toast.makeText(InventoryCategoryDetailsActivity.this, "Check your internet connection or please try after some time",
                                        Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(Call<HotelDetails> call, Throwable t) {
                        System.out.println("Failed");
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Toast.makeText(InventoryCategoryDetailsActivity.this, "Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void updateRoom(final Rooms rooms) {
        final ProgressDialog progressDialog = new ProgressDialog(InventoryCategoryDetailsActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(InventoryCategoryDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                LoginApi api = Util.getClient().create(LoginApi.class);
                Call<Rooms> response = api.updateRoom(auth_string,rooms.getRoomId(),rooms);

                response.enqueue(new Callback<Rooms>() {
                    @Override
                    public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                        if(progressDialog != null)
                        {
                            progressDialog.dismiss();
                        }
                        try{
                            int code = response.code();
                            if(code == 200||code == 201||code == 204)
                            {
                                Rooms roomresponse = response.body();
                                if(roomresponse != null)
                                {
                                    Toast.makeText(InventoryCategoryDetailsActivity.this, "Updating Inventory", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }




                    }

                    @Override
                    public void onFailure(Call<Rooms> call, Throwable t) {
                        if(progressDialog != null)
                        {
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent main = new Intent(InventoryCategoryDetailsActivity.this,NotificationListActivity.class);
                    startActivity(main);
                    this.finish();
                    return true;


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent main = new Intent(InventoryCategoryDetailsActivity.this,NotificationListActivity.class);
        startActivity(main);
        this.finish();
    }


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

            long date = PreferenceHandler.getInstance(context).getInventoryTime("CategoryTime"+dto.getRoomCategoryId());

            if(date==0){
                PreferenceHandler.getInstance(context).setInventoryTime("CategoryTime"+dto.getRoomCategoryId(),dateTime);
                PreferenceHandler.getInstance(context).setInventory("Category"+dto.getRoomCategoryId(),count);
                mUpdate.setVisibility(View.VISIBLE);
            }else{
                if(date<=dateTime){
                    PreferenceHandler.getInstance(context).setInventory("Category"+dto.getRoomCategoryId(),count);
                    mUpdate.setVisibility(View.VISIBLE);
                }else{
                    mUpdate.setVisibility(View.GONE);
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
}
