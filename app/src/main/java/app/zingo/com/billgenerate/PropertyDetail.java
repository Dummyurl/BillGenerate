package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;

import java.util.ArrayList;

import app.zingo.com.billgenerate.Model.DataBaseHelper;

public class PropertyDetail extends AppCompatActivity {

    EditText mName,mLocation,mCity,mEmail;
    FlatButton mSave;

    //database
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

        mName = (EditText)findViewById(R.id.property_name);
        mLocation = (EditText)findViewById(R.id.property_location);
        mCity = (EditText)findViewById(R.id.property_city);
        mEmail = (EditText)findViewById(R.id.property_email);
        mSave = (FlatButton) findViewById(R.id.submit_td);

        dbHelper = new DataBaseHelper(this);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String location = mLocation.getText().toString();
                String city = mCity.getText().toString();
                String mail = mEmail.getText().toString();

                if(name == null || name.isEmpty()){
                    Toast.makeText(PropertyDetail.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }else if(location == null || location.isEmpty()){
                    Toast.makeText(PropertyDetail.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }else if(city == null || city.isEmpty()){
                    Toast.makeText(PropertyDetail.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }else if(mail == null || mail.isEmpty()){
                    Toast.makeText(PropertyDetail.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }else{

                    if(dbHelper.insertProperty(name,location,city,mail)) {
                        Toast.makeText(getApplicationContext(), "Property Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Could not Insert property", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
