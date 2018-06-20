package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.zingo.com.billgenerate.Model.PreferenceHandler;

public class MainActivity extends AppCompatActivity {

    Button mProperty,mBill,mPlan,mRoom,mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProperty = (Button)findViewById(R.id.create_property);
        mBill = (Button)findViewById(R.id.create_bill);
        mPlan = (Button)findViewById(R.id.create_plan);
        mRoom = (Button)findViewById(R.id.create_room);
        mCancel = (Button)findViewById(R.id.cancel_booking);

        mProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent property = new Intent(MainActivity.this,PropertyDetail.class);
                startActivity(property);
                MainActivity.this.finish();
            }
        });

        mBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bill = new Intent(MainActivity.this,BillDetails.class);
                startActivity(bill);
                MainActivity.this.finish();
            }
        });

        mPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceHandler.getInstance(MainActivity.this).clear();
                Intent log = new Intent(MainActivity.this, LoginActivity.class);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(log);
                MainActivity.this.finish();
            }
        });

        mRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent room = new Intent(MainActivity.this,InventoryOptionsActivity.class);
                startActivity(room);
                MainActivity.this.finish();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent room = new Intent(MainActivity.this,CancelOptions.class);
                startActivity(room);
                MainActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
