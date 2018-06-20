package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class InventoryOptionsActivity extends AppCompatActivity {

    Button mAll,mOne,mNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_inventory_options);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Inventory");

            /*mAll = (Button)findViewById(R.id.send_all_hotels);
            mAll.setVisibility(View.GONE);*/
            mOne = (Button)findViewById(R.id.send_one_hotel);
            mNotifications = (Button)findViewById(R.id.notifications);

            mOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent one = new Intent(InventoryOptionsActivity.this,OneHotelInventoryActivity.class);
                    startActivity(one);
                }
            });

            mNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent one = new Intent(InventoryOptionsActivity.this,NotificationListActivity.class);
                    startActivity(one);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent main = new Intent(InventoryOptionsActivity.this,MainActivity.class);
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
        Intent main = new Intent(InventoryOptionsActivity.this,MainActivity.class);
        startActivity(main);
        this.finish();
    }
}
