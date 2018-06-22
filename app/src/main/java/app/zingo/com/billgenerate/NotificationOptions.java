package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class NotificationOptions extends AppCompatActivity {
    Button mAll,mOne;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_notification_options);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Other Notifications");

             /*mAll = (Button)findViewById(R.id.send_all_hotels);
            mAll.setVisibility(View.GONE);*/
            mOne = (Button)findViewById(R.id.send_one_hotel);
            mAll = (Button)findViewById(R.id.notifications);

            mOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent one = new Intent(NotificationOptions.this,OneHotelOtheNotification.class);
                    startActivity(one);
                }
            });

            mAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent one = new Intent(NotificationOptions.this,AllHotelNotification.class);
                    startActivity(one);
                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent main = new Intent(NotificationOptions.this,MainActivity.class);
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
        Intent main = new Intent(NotificationOptions.this,MainActivity.class);
        startActivity(main);
        this.finish();
    }
}
