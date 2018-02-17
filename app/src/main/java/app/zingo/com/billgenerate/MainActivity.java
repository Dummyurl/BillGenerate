package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent plan = new Intent(MainActivity.this,PlanDetails.class);
                startActivity(plan);
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
