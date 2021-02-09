package hector.developers.covid19riskassessment.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.model.UserHealthData;

public class HealthDetailsActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView detailFirstname;
    TextView detailDate;
    TextView detailFeverSymptom;
    TextView detailsHeadacheSymptom;
    TextView detailSneezingSymptoms;
    TextView detailsChestPainSymptoms;
    TextView detailsBodyPainSymptoms;
    TextView detailsNauseaOrVomitingSymptom;
    TextView detailsFluSymptoms;
    TextView detailsSoreThroatSymptoms;
    TextView detailsDiarrhoeaSymptoms;
    TextView detailsFatigueSymptoms;

    TextView detailNewOrWorseningCoughSymptom;
    TextView detailDifficultyInBreathingSymptom;
    TextView detailsLossOfSmellSymptoms;
    TextView detailsLossOfTasteSymptoms;

    TextView detailsContactWithFamily;

    TextView detailsPhone;
    TextView mDisplayOutcome;

    TextView detailRisk;

    Button mBtnCall, mBtnText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_details);
        detailFirstname = findViewById(R.id.detailsFirstname);
        detailDate = findViewById(R.id.detailsDate);
        detailFeverSymptom = findViewById(R.id.detailsFever);
        detailsHeadacheSymptom = findViewById(R.id.detailsHeadacheSymptom);
        detailSneezingSymptoms = findViewById(R.id.tvSneezingSymptoms);
        detailsChestPainSymptoms = findViewById(R.id.detailsChestPainSymptoms);
        detailsBodyPainSymptoms = findViewById(R.id.detailsBodyPainSymptoms);
        detailsNauseaOrVomitingSymptom = findViewById(R.id.detailsNauseaOrVomitingSymptom);
        detailsDiarrhoeaSymptoms = findViewById(R.id.detailsDiarrhoeaSymptoms);
        detailsFluSymptoms = findViewById(R.id.detailsFluSymptoms);
        detailsSoreThroatSymptoms = findViewById(R.id.detailsSoreThroatSymptoms);
        detailsFatigueSymptoms = findViewById(R.id.detailsFatigueSymptoms);

        detailNewOrWorseningCoughSymptom = findViewById(R.id.detailsCoughSymptom);
        detailDifficultyInBreathingSymptom = findViewById(R.id.detailsDifficultyInBreathingSymptom);
        detailsLossOfSmellSymptoms = findViewById(R.id.detailsLossOfSmellSymptoms);
        detailsLossOfTasteSymptoms = findViewById(R.id.detailsLossOfTasteSymptoms);

        detailsContactWithFamily = findViewById(R.id.detailsContactWithFamily);
        detailRisk = findViewById(R.id.detailsRisk);
        linearLayout = findViewById(R.id.detailLayout);

        detailsPhone = findViewById(R.id.detailsPhone);
        mBtnCall = findViewById(R.id.btnCall);
        mBtnText = findViewById(R.id.btnText);

        mDisplayOutcome = findViewById(R.id.displayResult);

        mBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });

        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
        Intent intent = getIntent();
        String phoneString = intent.getStringExtra("phone");
        String firstnameString = intent.getStringExtra("firstname");

        detailFirstname.setText(firstnameString);
        detailsPhone.setText(phoneString);

        System.out.println("phone String +++" + detailsPhone);
        System.out.println("first name String ++++" + detailFirstname);


        UserHealthData userHealthData;
        userHealthData = (UserHealthData) getIntent().getSerializableExtra("key");
        assert userHealthData != null;
        detailFirstname.setText(userHealthData.getFirstname());
        detailDate.setText(userHealthData.getDate().toString());
        detailsPhone.setText(userHealthData.getPhone());
        detailFeverSymptom.setText(userHealthData.getFeverSymptom());
        detailsHeadacheSymptom.setText(userHealthData.getHeadacheSymptom());
        detailSneezingSymptoms.setText(userHealthData.getSneezingSymptom());
        detailsChestPainSymptoms.setText(userHealthData.getChestPainSymptom());
        detailsBodyPainSymptoms.setText(userHealthData.getBodyPainSymptom());
        detailsDiarrhoeaSymptoms.setText(userHealthData.getDiarrhoeaSymptom());
        detailsFluSymptoms.setText(userHealthData.getFluSymptom());
        detailsSoreThroatSymptoms.setText(userHealthData.getSoreThroatSymptom());
        detailsNauseaOrVomitingSymptom.setText(userHealthData.getNauseaOrVomitingSymptom());
        detailNewOrWorseningCoughSymptom.setText(userHealthData.getNewOrWorseningCough());
        detailsFatigueSymptoms.setText(userHealthData.getFatigueSymptom());

        detailDifficultyInBreathingSymptom.setText(userHealthData.getDifficultyInBreathing());
        detailsLossOfSmellSymptoms.setText(userHealthData.getLossOfOrDiminishedSenseOfSmell());
        detailsLossOfTasteSymptoms.setText(userHealthData.getLossOfOrDiminishedSenseOfTaste());
        detailsContactWithFamily.setText(userHealthData.getContactWithFamily());

        if (detailRisk.getText().toString().equalsIgnoreCase("High Risk")) {
            mDisplayOutcome.setText("High risk");
            linearLayout.setBackgroundColor(Color.parseColor("#E82815"));
        } else {
            mDisplayOutcome.setText("Low risk");
            linearLayout.setBackgroundColor(Color.parseColor("#1BE815"));
        }
    }

    private void makeCall() {
        // TODO Auto-generated method stub
        try {
            //  Toast.makeText(mContext, "initiating call to " + phone.getText().toString(), Toast.LENGTH_LONG).show();
            Log.e("Health Details Activity", "Invoking call");
            if (ContextCompat.checkSelfPermission(HealthDetailsActivity.this, "android.permission.CALL_PHONE") == 0) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + detailsPhone.getText().toString()));
                startActivity(i);
            } else {
                ActivityCompat.requestPermissions((Activity) HealthDetailsActivity.this, new String[]{"android.permission.CALL_PHONE"}, 0);
            }

        } catch (Exception e) {
            Log.e("Health Details Activity", "Failed to invoke call", e);
        }
    }

    private void sendSms() {
        Uri uri = Uri.parse("smsto: " + detailsPhone.getText().toString());
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", " ");
        startActivity(intent);
    }
}