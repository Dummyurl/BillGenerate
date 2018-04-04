package app.zingo.com.billgenerate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;

import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import app.zingo.com.billgenerate.Model.PreferenceHandler;
import app.zingo.com.billgenerate.Model.Profile1;
import app.zingo.com.billgenerate.Model.ThreadExecuter;
import app.zingo.com.billgenerate.Model.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";
    private EditText input_num_signup,input_pass_signup;
    private FlatButton ok_signup;
    private ProgressDialog progressDialog;


    String enc;
    Profile1 dto;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        getSupportActionBar().setTitle("Login");

        input_num_signup = (EditText) findViewById(R.id.input_num_signup);
        input_pass_signup = (EditText) findViewById(R.id.input_pass_signup);

        ok_signup = (FlatButton) findViewById(R.id.ok_signup);



    }




    @Override
    protected void onResume() {
        super.onResume();
        ok_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.hideKeyboard(LoginActivity.this);
                if (!input_num_signup.getText().toString().trim().isEmpty() &&
                        !input_pass_signup.getText().toString().trim().isEmpty()) {

                    encrypt(input_pass_signup.getText().toString());

                }
                else
                    Toast.makeText(LoginActivity.this, "Fields should not be empty", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void login( final String username, final String password,final String type){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Profile1 p = new Profile1();
        p.setUserName(username);
        p.setPassword(password);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                LoginApi apiService =
                        Util.getClient().create(LoginApi.class);


                String authenticationString = Util.getToken(LoginActivity.this);
                Call<ArrayList<Profile1>> call = apiService.loginApiByUsernamePassword("Basic emluZ29ob3RlbHM6U3dvcmRmaXNoITI=",p);

                call.enqueue(new Callback<ArrayList<Profile1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Profile1>> call, Response<ArrayList<Profile1>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        if (statusCode == 200 || statusCode == 201) {

                            ArrayList<Profile1> dto1 = response.body();//-------------------should not be list------------
                            if (dto1!=null && dto1.size()!=0) {
                                dto = dto1.get(0);

                                if(dto.getStatus() != null && dto.getStatus().equalsIgnoreCase("enabled"))
                                {
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    SharedPreferences.Editor spe = sp.edit();
                                    PreferenceHandler.getInstance(LoginActivity.this).setPhoneNumber(dto.getPhoneNumber());
                                    PreferenceHandler.getInstance(LoginActivity.this).setUserName(dto.getUserName());
                                    PreferenceHandler.getInstance(LoginActivity.this).setUserId(dto.getProfileId());

                                    spe.apply();


                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Sorry your profile is disabled", Toast.LENGTH_SHORT).show();
                                }


                            }else{
//
                                if(type.equalsIgnoreCase("Encrypted")){
                                    login(input_num_signup.getText().toString(), input_pass_signup.getText().toString(),"Normal");
                                }else{
                                    Toast.makeText(LoginActivity.this, "Login credentials are wrong..", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Profile1>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    public void encrypt(String pwd){
        try
        {
            // String text = "Hello World";
            String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(pwd.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }
            // the encrypted String
            enc = sb.toString();
            System.out.println("encrypted:" + enc);

            login(input_num_signup.getText().toString(), enc,"Encrypted");



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
