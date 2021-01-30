package hector.developers.covid19riskassessment.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hector.developers.covid19riskassessment.R;


public class HivActivity extends AppCompatActivity {

    TextView mTollFree;
    Button mHivCloseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiv);

        mHivCloseBtn = findViewById(R.id.hivCloseBtn);
        mTollFree = findViewById(R.id.toll_free);

        mTollFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //method for invoking call
                String phone = mTollFree.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);


            }
        });

        mHivCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hivIntent = new Intent(HivActivity.this, LoginActivity.class);
                startActivity(hivIntent);
                finish();
            }
        });

    }
}
