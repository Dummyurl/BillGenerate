package app.zingo.com.billgenerate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.com.billgenerate.Model.Payment;
import app.zingo.com.billgenerate.Model.SearchBook;
import app.zingo.com.billgenerate.R;

/**
 * Created by ZingoHotels Tech on 23-07-2018.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Payment> paymentArrayList;

    public PaymentAdapter(Context context, ArrayList<Payment> paymentArrayList)
    {
        this.context = context;
        this.paymentArrayList = paymentArrayList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_payment_shown,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        try{

            Payment payment = paymentArrayList.get(position);

            holder.mPaymenmtStatus.setText(""+payment.getPaymentStatus());
            holder.mPaymentName.setText(""+payment.getPaymentName());
            holder.mAMount.setText("Rs "+payment.getAmount());

            SimpleDateFormat dfs = new SimpleDateFormat("MMM dd,yyyy");
            String paymentDate[] = payment.getPaymentDate().split("T");
            Date paymentDates = new SimpleDateFormat("yyyy-MM-dd").parse(paymentDate[0]);

            holder.mDate.setText(""+dfs.format(paymentDates));


        }catch (Exception e){
            e.printStackTrace();
          //  Toast.makeText(context, "Payment MEssage "+e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public int getItemCount() {
        return paymentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mPaymenmtStatus,mPaymentName,mAMount,mDate;

        public ViewHolder(View itemView) {
            super(itemView);

            mPaymenmtStatus = (TextView) itemView.findViewById(R.id.payment_status);
            mPaymentName = (TextView) itemView.findViewById(R.id.payment_name);
            mAMount = (TextView) itemView.findViewById(R.id.amount);
            mDate = (TextView) itemView.findViewById(R.id.date);


        }
    }





}
