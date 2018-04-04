package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.zingo.com.billgenerate.Model.PreferenceHandler;

public class MainActivity extends AppCompatActivity {

    Button mProperty,mBill,mPlan,mRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProperty = (Button)findViewById(R.id.create_property);
        mBill = (Button)findViewById(R.id.create_bill);
        mPlan = (Button)findViewById(R.id.create_plan);
        mRoom = (Button)findViewById(R.id.create_room);

        mProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent property = new Intent(MainActivity.this,PropertyDetail.class);
                startActivity(property);
            }
        });

        mBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bill = new Intent(MainActivity.this,BillDetails.class);
                startActivity(bill);
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
                finish();
            }
        });

        mRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent room = new Intent(MainActivity.this,RoomDetail.class);
                startActivity(room);
            }
        });

    }
}
