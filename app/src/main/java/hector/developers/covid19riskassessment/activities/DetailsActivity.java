package hector.developers.covid19riskassessment.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.model.UserHealthData;
import hector.developers.covid19riskassessment.model.Users;


public class DetailsActivity extends AppCompatActivity {
    TextView detailUserFirstname;
    TextView detailUserLastname;
    TextView detailUserState;
    TextView detailsUserType;
    TextView detailUserDesignation;
    TextView detailsUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailUserFirstname = findViewById(R.id.detailFirstname);
        detailUserLastname = findViewById(R.id.detailsLastname);
        detailUserState = findViewById(R.id.detailsState);
        detailUserDesignation = findViewById(R.id.detailsDesignation);
        detailsUserType = findViewById(R.id.detailsUserType);
        detailsUserPhone = findViewById(R.id.detailPhone);

        displayDetails();
    }

    private void displayDetails() {
        Users users;
        users = (Users) getIntent().getSerializableExtra("key");
        assert users != null;
        detailUserFirstname.setText(users.getFirstname());
        detailUserLastname.setText(users.getLastname());
        detailUserState.setText(users.getState());
        detailsUserPhone.setText(users.getPhone());
        detailsUserType.setText(users.getUserType());
        detailUserDesignation.setText(users.getDesignation());
    }
}