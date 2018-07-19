package app.zingo.com.billgenerate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.zingo.com.billgenerate.Model.AuditSettlement;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Rooms;
import app.zingo.com.billgenerate.Model.Traveller;
import app.zingo.com.billgenerate.Utils.Util;
import app.zingo.com.billgenerate.WebApis.AuditApi;
import app.zingo.com.billgenerate.WebApis.BookingApi;
import app.zingo.com.billgenerate.WebApis.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettleAudit extends AppCompatActivity {

    //View
    Button mAdd,mCalc;
    EditText mBNumber,mRoomNo,mGuest,mCIT,mCOT,mSell,mGST,mExtra,
            mTotal,mMode,mASell,mAGST,mAExtra,mDiff,mRemark;//mStatus
    Spinner mStatus;
    TextView mTransaction;

    int bookingId;
    private Timer timer;
    private long diffDays;

    //Api
    Bookings1 bookings;
    AuditSettlement auditSettlement;
    ArrayList<Bookings1> booked;
    private int auditId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_settle_audit);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Audit Settlement");



            mBNumber = (EditText)findViewById(R.id.audit_settle_bNum);
            mTransaction = (TextView)findViewById(R.id.transaction);
            mRoomNo = (EditText)findViewById(R.id.audit_settle_room);
            mGuest = (EditText)findViewById(R.id.audit_settle_guest);
            mCIT = (EditText)findViewById(R.id.audit_settle_cit);
            mCOT = (EditText)findViewById(R.id.audit_settle_cot);
            mSell = (EditText)findViewById(R.id.audit_settle_sell);
            mGST = (EditText)findViewById(R.id.audit_settle_gst);
            mExtra = (EditText)findViewById(R.id.audit_settle_extra);
            mTotal = (EditText)findViewById(R.id.audit_settle_total);
            mMode = (EditText)findViewById(R.id.audit_settle_mode);
            //mStatus = (EditText)findViewById(R.id.audit_settle_status);
            mStatus = (Spinner) findViewById(R.id.audit_settle_status);
            mASell = (EditText)findViewById(R.id.audit_settle_aSell);
            mAGST = (EditText)findViewById(R.id.audit_settle_aGst);
            mAExtra = (EditText)findViewById(R.id.audit_settle_aExtra);
            mDiff = (EditText)findViewById(R.id.audit_settle_diff);
            mRemark = (EditText)findViewById(R.id.audit_settle_remark);
            mAdd = (Button) findViewById(R.id.audit_settle_update);
            mCalc = (Button) findViewById(R.id.audit_settle_calc);


            mTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bookings.getPaymentList().size()!=0){



                        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                        for(int j=0;j<bookings.getPaymentList().size();j++){

                        /*    Intent intent = new Intent(getApplicationContext(), NightAuditPaymentList.class);
                            intent.putExtra("BookingNumber",bookings.getBookingNumber());
                            intent.putExtra("BookingID",bookings.getBookingId());
                            intent.putExtra("ACTIVITY","Settle");
                            startActivity(intent);
*/

                        }




                    }else{

                        Toast.makeText(SettleAudit.this, "There is no transaction happened for this booking", Toast.LENGTH_SHORT).show();

                    }

                }
            });


            mASell.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {



                            runOnUiThread(new Runnable() {
                                public int sellRate;

                                @Override
                                public void run() {
                                    if(mASell.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(SettleAudit.this,"Please Enter Sell Rate",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        try {
                                            sellRate = Integer.parseInt(mASell.getText().toString());
                                        }catch(NumberFormatException nfe) {
                                            System.out.println("Could not parse " + nfe);
                                        }
                                        NumberFormat df = new DecimalFormat("##.###");


                                        // dateCal();


                                        if (sellRate <= 999.99) {

                                            // double gstamount = (sellRate * 0) / 100;
                                            mAGST.setText("0");
                                        } else if (sellRate >= 1000 && sellRate <= 2499.99) {
                                            double gstamount = (sellRate * 12) / 100;
                                            mAGST.setText("" + df.format(gstamount));
                                        } else if (sellRate >= 2500 && sellRate <= 7499.99) {
                                            double gstamount = (sellRate * 18) / 100;
                                            mAGST.setText("" + df.format(gstamount));
                                        } else if (sellRate >= 7500) {
                                            double gstamount = (sellRate * 28) / 100;
                                            mAGST.setText("" + df.format(gstamount));
                                        }



                                    }

                                }

                            });
                        }
                    }, 1000);

                }
            });



            mCalc.setOnClickListener(new View.OnClickListener() {

                NumberFormat df = new DecimalFormat("##.###");

                @Override
                public void onClick(View view) {
                    String total = mTotal.getText().toString();
                    String aSell = mASell.getText().toString();
                    String aGst = mAGST.getText().toString();
                    String aExtra = mAExtra.getText().toString();
                    if(aSell == null || aSell.isEmpty()){
                        mASell.setError("Sell Rate should not be empty");
                        mASell.requestFocus();
                    }else  if(aGst == null || aGst.isEmpty()){
                        mAGST.setError("GST Amount should not be empty");
                        mAGST.requestFocus();
                    }else  if(aExtra == null || aExtra.isEmpty()){
                        mAExtra.setError("Extra Charges should not be empty");
                        mAExtra.requestFocus();
                    }else if(total==null || total.isEmpty()){
                        Toast.makeText(SettleAudit.this, "Invalid Data was received", Toast.LENGTH_SHORT).show();
                    }else{
                        double amount = Double.parseDouble(total);
                        double sellRate = diffDays * Double.parseDouble(aSell);
                        double tax = diffDays * Double.parseDouble(aGst);
                        double charges = Double.parseDouble(aExtra);
                        double totals = sellRate + tax + charges;
                        double diff = amount - totals;
                        mDiff.setText(""+df.format(diff));
                    }
                }
            });

            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String aSell = mASell.getText().toString();
                    String aGst = mAGST.getText().toString();
                    String aExtra = mAExtra.getText().toString();
                    String diff = mDiff.getText().toString();
                    String remark = mRemark.getText().toString();
                    String mode = mMode.getText().toString();
                    //String status = mStatus.getText().toString();
                    String status = mStatus.getSelectedItem().toString();

                    if(aSell == null || aSell.isEmpty()){
                        mASell.setError("Sell Rate should not be empty");
                        mASell.requestFocus();
                    }else  if(aGst == null || aGst.isEmpty()){
                        mAGST.setError("GST amount should not be empty");
                        mAGST.requestFocus();
                    }else  if(aExtra == null || aExtra.isEmpty()){
                        mAExtra.setError("Extra charges should not be empty");
                        mAExtra.requestFocus();
                    }else if(diff == null || diff.isEmpty()){
                        mDiff.setError("Difference should not be empty");
                        mDiff.requestFocus();
                    }else  if(remark == null || remark.isEmpty()){
                        mRemark.setError("Write some remarks");
                        mRemark.requestFocus();
                    }else  if(mode == null || mode.isEmpty()){
                        mMode.setError("Payment mode should not be empty");
                        mMode.requestFocus();
                    }else{

                        if(auditId!=0){
                            AuditSettlement audits = bookings.getAuditSettlementList().get(0);
                            audits.setAuditingExtra(Integer.parseInt(aExtra));
                            audits.setAuditingSellRate(Integer.parseInt(aSell));
                            audits.setAuditingGST(Integer.parseInt(aGst));
                            audits.setDifferenceAmount(Integer.parseInt(diff));
                            audits.setPaymentMode(mode);
                            audits.setPaymentStatus(status);
                            audits.setRemark(remark);
                            audits.setBookingId(bookingId);
                            audits.setCreatedBy("Zingo");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                            audits.setSettlementDate(dateFormat.format(new Date()));

                            updateAudit(audits,auditId);
                        }else{
                            AuditSettlement audits = new AuditSettlement();
                            audits.setAuditingExtra(Integer.parseInt(aExtra));
                            audits.setAuditingSellRate(Integer.parseInt(aSell));
                            audits.setAuditingGST(Integer.parseInt(aGst));
                            audits.setDifferenceAmount(Integer.parseInt(diff));
                            audits.setPaymentMode(mode);
                            audits.setPaymentStatus(status);
                            audits.setRemark(remark);
                            audits.setBookingId(bookingId);
                            audits.setCreatedBy("Zingo");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                            audits.setSettlementDate(dateFormat.format(new Date()));

                            audit(audits);
                        }


                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }






    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){
                bookingId = bundle.getInt("BookingId");
            }
            if (bookingId!=0){
                getBookings();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //getBookings By Id
    public  void getBookings()
    {
        BookingApi api = Util.getClient().create(BookingApi.class);
        String authenticationString = Util.getToken(SettleAudit.this);
        Call<Bookings1> getBooking = api.getBookingById(authenticationString,bookingId);

        getBooking.enqueue(new Callback<Bookings1>() {
            @Override
            public void onResponse(Call<Bookings1> call, Response<Bookings1> response) {
                if(response.code() == 200)
                {
                    try{
                        bookings = response.body();
                        if(bookings != null)
                        {

                            mBNumber.setText(bookings.getBookingNumber());
                            mCIT.setText(bookings.getCheckInDate());
                            mCOT.setText(bookings.getCheckOutDate());
                            mSell.setText(""+bookings.getSellRate());
                            mGST.setText(""+bookings.getGstAmount());
                            mExtra.setText(""+bookings.getExtraCharges());
                            mTotal.setText(""+bookings.getTotalAmount());
                            duration(bookings.getCheckInDate(),bookings.getCheckOutDate());
                            if(bookings.getAuditSettlementList().size()!=0){
                                mASell.setText(""+bookings.getAuditSettlementList().get(0).getAuditingSellRate());
                                mAGST.setText(""+bookings.getAuditSettlementList().get(0).getAuditingGST());
                                mAExtra.setText(""+bookings.getAuditSettlementList().get(0).getAuditingExtra());
                                mDiff.setText(""+bookings.getAuditSettlementList().get(0).getDifferenceAmount());
                                mRemark.setText(""+bookings.getAuditSettlementList().get(0).getRemark());
                                mMode.setText(""+bookings.getAuditSettlementList().get(0).getPaymentMode());
                                if(bookings.getAuditSettlementList().get(0).getPaymentStatus().equalsIgnoreCase("Pending")){
                                    mStatus.setSelection(0);
                                }else{
                                    mStatus.setSelection(1);
                                }

                                auditId = bookings.getAuditSettlementList().get(0).getAuditSettlementId();

                            }

                            getRoomNo(bookings.getRoomId(),mRoomNo);
                            getTravellerName(bookings.getTravellerId(),mGuest);




                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }
                else
                {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Bookings1> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public  void getRoomNo(final int i, final TextView t)
    {
        String auth_string = Util.getToken(SettleAudit.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        LoginApi api = Util.getClient().create(LoginApi.class);
        Call<Rooms> getRoom = api.getRoom(auth_string,i);

        getRoom.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                if(response.code() == 200)
                {
                    try{
                        if(response.body() != null)
                        {
                            t.setText(response.body().getRoomNo());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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
        String auth_string = Util.getToken(SettleAudit.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
        TravellerApi api = Util.getClient().create(TravellerApi.class);
        Call<Traveller> getTrav = api.getTravellerDetails(auth_string,i);

        getTrav.enqueue(new Callback<Traveller>() {
            @Override
            public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                if(response.code() == 200)
                {
                    try{
                        if(response.body() != null)
                        {
                            t.setText(response.body().getFirstName()+" "+response.body().getLastName());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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

    public void duration(String fromm,String too){

        String from = fromm+" 00:00:00";
        String to = too+" 00:00:00";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {

            d1 = format.parse(from);
            d2 = format.parse(to);
            long diff = d2.getTime() - d1.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
            //duration = String.valueOf(diffDays);
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    //Audit
    private void audit(final AuditSettlement mAudit) {

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(SettleAudit.this);
                AuditApi api = Util.getClient().create(AuditApi.class);
                Call<AuditSettlement> response = api.postAudit(authenticationString,mAudit);

                response.enqueue(new Callback<AuditSettlement>() {
                    @Override
                    public void onResponse(Call<AuditSettlement> call, Response<AuditSettlement> response) {

                        int code = response.code();
                        if (code == 200||code == 201||code == 204||code == 202) {
                            try{
                                AuditSettlement bro = response.body();
                                if (bro != null) {

                                    Toast.makeText(SettleAudit.this, "Audit Success", Toast.LENGTH_SHORT).show();
                                /*Intent main = new Intent(SettleAudit.this, LastDaySettlement.class);
                                startActivity(main);*/
                                    SettleAudit.this.finish();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<AuditSettlement> call, Throwable t) {

                        Toast.makeText(SettleAudit.this, "Audit Fail, Due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }



    public void updateAudit(final AuditSettlement up,final int id)
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Updating...");
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String authenticationString = Util.getToken(SettleAudit.this);
                AuditApi auditApi = Util.getClient().create(AuditApi.class);
                Call<AuditSettlement> res = auditApi.updateAudit(authenticationString,id,up);
                res.enqueue(new Callback<AuditSettlement>() {
                    @Override
                    public void onResponse(Call<AuditSettlement> call, Response<AuditSettlement> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(SettleAudit.this,""+response.code(),Toast.LENGTH_SHORT).show();
                        if(response.code() == 204)
                        {
                            try{
                                Toast.makeText(SettleAudit.this,"Update Success",Toast.LENGTH_SHORT).show();
                            /*Intent main = new Intent(SettleAudit.this, LastDaySettlement.class);
                            startActivity(main);*/
                                SettleAudit.this.finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                        else
                        {
                            Toast.makeText(SettleAudit.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuditSettlement> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(SettleAudit.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
                SettleAudit.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

