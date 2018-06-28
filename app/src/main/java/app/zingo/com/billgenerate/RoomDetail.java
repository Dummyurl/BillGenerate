package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;

import app.zingo.com.billgenerate.Utils.RoomDataBase;

public class RoomDetail extends AppCompatActivity {

    EditText mName;
    FlatButton mSave;

    //database
    private RoomDataBase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        mName = (EditText)findViewById(R.id.room_name);
        mSave = (FlatButton) findViewById(R.id.submit_room);

        dbHelper = new RoomDataBase(this);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();


                if(name == null || name.isEmpty()){
                    Toast.makeText(RoomDetail.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }else{

                    if(dbHelper.insertROOM(name)) {
                        Toast.makeText(getApplicationContext(), "Room Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Could not Insert Room", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
