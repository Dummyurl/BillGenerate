package app.zingo.com.billgenerate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;

import app.zingo.com.billgenerate.Model.DataBaseHelper;
import app.zingo.com.billgenerate.Model.PlanDataBase;

public class PlanDetails extends AppCompatActivity {

    EditText mName,mDesc;
    FlatButton mSave;

    //database
    private PlanDataBase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);

        mName = (EditText)findViewById(R.id.plan_name);
        mDesc = (EditText)findViewById(R.id.plan_desc);
        mSave = (FlatButton) findViewById(R.id.submit_plan);

        dbHelper = new PlanDataBase(this);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String desc = mDesc.getText().toString();
                

                if(name == null || name.isEmpty()){
                    Toast.makeText(PlanDetails.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }else if(desc == null || desc.isEmpty()){
                    Toast.makeText(PlanDetails.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }else{

                    if(dbHelper.insertPLAN(name,desc)) {
                        Toast.makeText(getApplicationContext(), "Plan Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Could not Insert plan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
