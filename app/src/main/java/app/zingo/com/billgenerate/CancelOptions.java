package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CancelOptions extends AppCompatActivity {

    Button mById,mByHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_cancel_options);


            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Edit Optins");

            mByHotel = (Button)findViewById(R.id.based_hotel);
            mById = (Button)findViewById(R.id.based_id);

            mByHotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            mById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent room = new Intent(CancelOptions.this,ShowBookingById.class);
                    startActivity(room);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent main = new Intent(CancelOptions.this,MainActivity.class);
                startActivity(main);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
