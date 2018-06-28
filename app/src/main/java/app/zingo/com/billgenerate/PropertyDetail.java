package app.zingo.com.billgenerate;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;

import app.zingo.com.billgenerate.Utils.DataBaseHelper;
import app.zingo.com.billgenerate.Model.Documents;
import app.zingo.com.billgenerate.Utils.PreferenceHandler;
import app.zingo.com.billgenerate.Utils.ThreadExecuter;
import app.zingo.com.billgenerate.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

                    /*if(dbHelper.insertProperty(name,location,city,mail)) {
                        Toast.makeText(getApplicationContext(), "Property Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Could not Insert property", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }*/
                    addHotel(PreferenceHandler.getInstance(PropertyDetail.this).getUserId());
                }
            }
        });
    }

    private void addHotel(int id){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Documents rc = new Documents();

        rc.setDocumentName(mName.getText().toString());
        rc.setStatus("status");
        rc.setDocumentType(mLocation.getText().toString());
        rc.setDocumentNumber(mCity.getText().toString());
        rc.setReEnterDocumentNumber(mEmail.getText().toString());
        rc.setProfileId(id);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);

                String authenticationString = Util.getToken(PropertyDetail.this);
                Call<Documents> call = apiService.addHotels(authenticationString,rc);

                call.enqueue(new Callback<Documents>() {
                    @Override
                    public void onResponse(Call<Documents> call, Response<Documents> response) {
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201) {

                            Documents dto = response.body();
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(PropertyDetail.this, "Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(PropertyDetail.this, "failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Documents> call, Throwable t) {
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });

    }
}
