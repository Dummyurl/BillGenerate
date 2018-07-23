package app.zingo.com.billgenerate.Activiies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.zingo.com.billgenerate.Adapter.PaymentAdapter;
import app.zingo.com.billgenerate.Model.Bookings1;
import app.zingo.com.billgenerate.Model.Payment;
import app.zingo.com.billgenerate.R;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import app.zingo.com.billgenerate.WebApis.BookingApi;
import app.zingo.com.billgenerate.WebApis.PaymentApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingReconcilationDataUpdate extends AppCompatActivity {

    TextView mShortName,mGuestName,mBookingStatus,mBookingNum,
                mBookingDate,mZingoBookingStatus,mCheckInDate,mCheckOutDate,mRoomTYpe,
            mGuestCount;

    EditText mRoomCharge,mExtraCharge,mHotelTaxes,mTotalAmount,
                mOtaCommission,mOtaCommissionGst,mOtaTotalCommision,
            mOtaServiceFees,mCustomerPaymentAtOtA,mAdditionCharge,mOTAToHotel,
            mHOtelToOTA,mZingoToHotel,mHotelToZingo,mZingoCommsion;

    RecyclerView mPaymentList;

    ImageView mAddPayment,mShowPayment;

    Button mUpdate;

    //Intent
    Bookings1 bookings1;
    String guestName,status;
    int hotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_booking_reconcilation_data_update);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Booking Update");

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                bookings1 = (Bookings1)bundle.getSerializable("Bookings");
                guestName = bundle.getString("GuestName");
                status = bundle.getString("Status");
                hotelId = bundle.getInt("HotelId");
            }

            mShortName = (TextView)findViewById(R.id.bre_details_short_name);
            mGuestName = (TextView)findViewById(R.id.bre_details_person_name);
            mBookingStatus = (TextView)findViewById(R.id.bre_booking_details_brec_status);
            mBookingNum = (TextView)findViewById(R.id.bre_details_booking_id);
            mBookingDate = (TextView)findViewById(R.id.bre_details_booking_date);
            mZingoBookingStatus = (TextView)findViewById(R.id.bre_details_booking_status);
            mCheckInDate = (TextView)findViewById(R.id.bre_details_check_in);
            mCheckOutDate = (TextView)findViewById(R.id.bre_details_check_out);
            mGuestCount = (TextView)findViewById(R.id.bre_details_guest_count);
            mRoomCharge = (EditText) findViewById(R.id.bill_room_charge);
            mExtraCharge = (EditText)findViewById(R.id.bill_extra_charge);
            mHotelTaxes = (EditText)findViewById(R.id.bill_hotel_taxes);
            mTotalAmount = (EditText)findViewById(R.id.bill_property_amount);
            mOtaCommission = (EditText)findViewById(R.id.bill_booking_com_amount);
            mOtaCommissionGst = (EditText)findViewById(R.id.bill_booking_com_gst);
            mOtaTotalCommision = (EditText)findViewById(R.id.bill_booking_com);
            mOtaServiceFees = (EditText)findViewById(R.id.bill_booking_com_service);
            mCustomerPaymentAtOtA = (EditText)findViewById(R.id.bill_customer_pay);
            mAdditionCharge = (EditText)findViewById(R.id.bill_additional_charge);
            mOTAToHotel = (EditText)findViewById(R.id.ota_to_hotel);
            mHOtelToOTA = (EditText)findViewById(R.id.hotel_to_ota);
            mZingoToHotel = (EditText)findViewById(R.id.zingo_to_hotel);
            mHotelToZingo = (EditText)findViewById(R.id.hotel_to_zingo);
            mZingoCommsion = (EditText)findViewById(R.id.zingo_commission);
            mUpdate = (Button) findViewById(R.id.booking_update);
            mAddPayment = (ImageView) findViewById(R.id.add_payment);
            mShowPayment = (ImageView) findViewById(R.id.show_payment);
            mPaymentList = (RecyclerView) findViewById(R.id.payment_list);

            if(guestName!=null||!guestName.isEmpty()){
                mGuestName.setText(""+guestName);
                String[] ab = guestName.split(" ");
                if(ab.length > 1 && ab[0] != null && !ab[0].isEmpty())
                {
                    //if(ab[1].charAt(0) != "")
                    mShortName.setText(ab[0].charAt(0)+"");//+""+ab[1].charAt(0));
                }
                else
                {
                    mShortName.setText(guestName.charAt(0)+"");
                }
            }

            if(status!=null&&!status.isEmpty()){
                mBookingStatus.setText(""+status);
                if(status.equalsIgnoreCase("Confirmed")){
                    mBookingStatus.setBackgroundColor(Color.GREEN);
                }else if(status.equalsIgnoreCase("No Show")){
                    mBookingStatus.setBackgroundColor(Color.CYAN);
                }else if(status.equalsIgnoreCase("Cancelled")){
                    mBookingStatus.setBackgroundColor(Color.RED);
                }
            }

            if(bookings1!=null){
                setBookingsData(bookings1);
            }

            mUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    validate();
                }
            });

            mAddPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{
                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BookingReconcilationDataUpdate.this);
                        builder.setTitle("Payment For?");
                        builder.setPositiveButton("OTA Adjustment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymentPopUp("adjustment");

                            }
                        });

                        builder.setNegativeButton("Zingo Payment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                paymentPopUp("zingo");
                            }
                        });

                        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void setBookingsData(final  Bookings1 bookingsData){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfc = new SimpleDateFormat("MMM dd,yyyy");
            mBookingNum.setText(""+ bookingsData.getOTABookingID());
            mBookingDate.setText(""+sdfc.format(sdf.parse(bookingsData.getBookingDate())));
            mZingoBookingStatus.setText(""+bookingsData.getBookingStatus());
            mCheckInDate.setText(""+sdfc.format(sdf.parse(bookingsData.getCheckInDate())));
            mCheckOutDate.setText(""+sdfc.format(sdf.parse(bookingsData.getCheckOutDate())));
            mGuestCount.setText(""+bookingsData.getNoOfAdults());
            mRoomCharge.setText(""+bookingsData.getSellRate());
            mExtraCharge.setText(""+bookingsData.getExtraCharges());
            mHotelTaxes.setText(""+bookingsData.getGstAmount());
            mTotalAmount.setText(""+bookingsData.getTotalAmount());
            mOtaCommission.setText(""+bookingsData.getOTACommissionAmount());
            mOtaCommissionGst.setText(""+bookingsData.getOTACommissionGSTAmount());
            mOtaTotalCommision.setText(""+bookingsData.getOTATotalCommissionAmount());
            mOtaServiceFees.setText(""+bookingsData.getOTAServiceFees());
            mCustomerPaymentAtOtA.setText(""+bookingsData.getCustomerPaymentAtOTA());
            mAdditionCharge.setText(""+bookingsData.getAdditionalCharges());
            mOTAToHotel.setText(""+bookingsData.getOTAToPayHotel());
            mHOtelToOTA.setText(""+bookingsData.getHotelToPayOTA());
            mZingoToHotel.setText(""+bookingsData.getZingoToHotel());
            mHotelToZingo.setText(""+bookingsData.getHotelToZingo());
            mZingoCommsion.setText(""+bookingsData.getZingoCommision());

            if(bookingsData.getPaymentList()!=null&&bookingsData.getPaymentList().size()!=0){
                PaymentAdapter adapter = new PaymentAdapter(BookingReconcilationDataUpdate.this,bookingsData.getPaymentList());
                mPaymentList.setAdapter(adapter);
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        
    }

    public void validate(){

        try{

            String sellRate = mRoomCharge.getText().toString();
            String extra = mExtraCharge.getText().toString();
            String tax = mHotelTaxes.getText().toString();
            String total = mTotalAmount.getText().toString();
            String otaCommission = mOtaCommission.getText().toString();
            String otaGst = mOtaCommissionGst.getText().toString();
            String otaTotal = mOtaTotalCommision.getText().toString();
            String otaService = mOtaServiceFees.getText().toString();
            String customerOta = mCustomerPaymentAtOtA.getText().toString();
            String additional = mAdditionCharge.getText().toString();
            String otaToHotel = mOTAToHotel.getText().toString();
            String hotelToOta = mHOtelToOTA.getText().toString();
            String zingoToHotel = mZingoToHotel.getText().toString();
            String hotelToZingo = mHotelToZingo.getText().toString();
            String zingoCommision = mZingoCommsion.getText().toString();

            if(sellRate == null || sellRate.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(extra == null || extra.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(tax == null || tax.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(total == null || total.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(otaCommission == null || otaCommission.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(otaGst == null || otaGst.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(otaTotal == null || otaTotal.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(otaService == null || otaService.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(customerOta == null || customerOta.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(additional == null || additional.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(otaToHotel == null || otaToHotel.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(hotelToOta == null || hotelToOta.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(zingoToHotel == null || zingoToHotel.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(hotelToZingo == null || hotelToZingo.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else if(zingoCommision == null || zingoCommision.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else{

                Bookings1 updateBookings = bookings1;
                updateBookings.setSellRate(Integer.parseInt(sellRate));
                updateBookings.setExtraCharges(Integer.parseInt(extra));
                updateBookings.setGstAmount(Integer.parseInt(tax));
                updateBookings.setTotalAmount(Integer.parseInt(total));
                updateBookings.setOTACommissionAmount(Double.parseDouble(otaCommission));
                updateBookings.setOTACommissionGSTAmount(Double.parseDouble(otaGst));
                updateBookings.setOTATotalCommissionAmount(Double.parseDouble(otaTotal));
                updateBookings.setOTAServiceFees(Double.parseDouble(otaService));
                updateBookings.setCustomerPaymentAtOTA(Double.parseDouble(customerOta));
                updateBookings.setAdditionalCharges(Double.parseDouble(additional));
                updateBookings.setOTAToPayHotel(Double.parseDouble(otaToHotel));
                updateBookings.setHotelToPayOTA(Double.parseDouble(hotelToOta));
                updateBookings.setZingoToHotel(Double.parseDouble(zingoToHotel));
                updateBookings.setHotelToZingo(Double.parseDouble(hotelToZingo));
                updateBookings.setZingoCommision(Double.parseDouble(zingoCommision));

                updateBooking(updateBookings);

            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void updateBooking(final Bookings1 bookings1) {
        final ProgressDialog dialog = new ProgressDialog(BookingReconcilationDataUpdate.this);
        dialog.setTitle(BookingReconcilationDataUpdate.this.getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);


                String authenticationString = Util.getToken(BookingReconcilationDataUpdate.this);
                Call<String> checkout = bookingApi.updateBookingStatus(authenticationString,bookings1.getBookingId(),bookings1);
                checkout.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 204||response.code() == 201||response.code() == 200)
                        {

                            Toast.makeText(BookingReconcilationDataUpdate.this,"Booking is updated successfully",Toast.LENGTH_SHORT).show();
                            //BookingDetailsActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(BookingReconcilationDataUpdate.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void  paymentPopUp(final String status){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(BookingReconcilationDataUpdate.this);
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View views = inflater.inflate(R.layout.layout_payment_add, null);

            builder.setView(views);
            final Button mNext = (Button) views.findViewById(R.id.payment_save_btn);
            final EditText mAmount = (EditText) views.findViewById(R.id.payment_amount);
            final EditText mName = (EditText) views.findViewById(R.id.payment_name);
            final EditText mRemarks = (EditText) views.findViewById(R.id.payment_name);
            final Spinner mPaymentMode = (Spinner) views.findViewById(R.id.payment_mode);
            final AlertDialog dialogs = builder.create();
            dialogs.show();
            dialogs.setCanceledOnTouchOutside(false);



            if(status.equalsIgnoreCase("zingo")){
                mName.setText("Zingo Payment");
                mName.setEnabled(false);
            }


            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String enterAmount = mAmount.getText().toString();
                    String name = mName.getText().toString();
                    String paymentMode = mPaymentMode.getSelectedItem().toString();
                    if(enterAmount==null||enterAmount.isEmpty()){
                        mAmount.setError("Can't be Empty");
                    }else if(name==null||name.isEmpty()){
                        mName.setError("Can't be Empty");
                    }else{
                        dialogs.dismiss();
                        double amountEnter = Double.parseDouble(enterAmount);
                        final Payment payment = new Payment();
                        payment.setAmount((int) amountEnter);
                        payment.setPaymentType(paymentMode);
                        payment.setBookingId(bookings1.getBookingId());
                        payment.setPaymentName(name);
                        if(mRemarks.getText().toString()!=null&&!mRemarks.getText().toString().isEmpty()){
                            payment.setRemarks(mRemarks.getText().toString());
                        }
                        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                        payment.setPaymentDate(date.format(new Date()));

                        if(status.equalsIgnoreCase("zingo")){
                            payment.setPaymentStatus("Payment Done By Zingo");
                        }else{
                            payment.setPaymentStatus("Adjustment");
                        }



                        addPayment(payment);
                        dialogs.dismiss();


                    }

                }
            });







        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addPayment(final Payment payment){
        final ProgressDialog dialog = new ProgressDialog(BookingReconcilationDataUpdate.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        new app.zingo.com.billgenerate.ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                String auth_string = Util.getToken(BookingReconcilationDataUpdate.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                PaymentApi paymentApi = Util.getClient().create(PaymentApi.class);

                Call<Payment> response = paymentApi.addPayment(auth_string,payment);
                response.enqueue(new Callback<Payment>() {
                    @Override
                    public void onResponse(Call<Payment> call, Response<Payment> response) {

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Payment serviceResponse = response.body();

                        if(response.code() == 200 && serviceResponse != null)
                        {

                            try{
                                Toast.makeText(BookingReconcilationDataUpdate.this,"Payment added successfully",Toast.LENGTH_LONG).show();



                            }catch (Exception e){
                                e.printStackTrace();
                            }




                        }
                        else {

                            Toast.makeText(BookingReconcilationDataUpdate.this,"Please try after some time",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Payment> call, Throwable t) {
                        System.out.println("onFailure");
                        Toast.makeText(BookingReconcilationDataUpdate.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
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
                    Intent main = new Intent(BookingReconcilationDataUpdate.this,BookingLIstActivity.class);
                    main.putExtra("HotelId",hotelId);
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
        try{
            Intent main = new Intent(BookingReconcilationDataUpdate.this,BookingLIstActivity.class);
            main.putExtra("HotelId",hotelId);
            startActivity(main);
            this.finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
