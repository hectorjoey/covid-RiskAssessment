package hector.developers.covid19riskassessment.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.util.HashMap;

import hector.developers.covid19riskassessment.R;

public class MainActivity extends AppCompatActivity {
    ImageView mImageSup, mImgAddUser, mImageViewSup, mImageViewUser;
    private Toolbar mToolbar;
    CardView mViewSupLayout, mViewUserLayout, mAddSupLayout, mAddUserLayout;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Admin Dashboard");

        mImageSup = findViewById(R.id.imgAddSup);
        mImgAddUser = findViewById(R.id.imgAddUser);
        mImageViewSup = findViewById(R.id.imgViewSup);
        mImageViewUser = findViewById(R.id.imgViewUser);
        mAddSupLayout = findViewById(R.id.addUserLayout);
        mAddSupLayout = findViewById(R.id.addSupLayout);
        mViewSupLayout = findViewById(R.id.viewSupLayout);
        mViewUserLayout = findViewById(R.id.viewUsersLayout);

        HashMap<String, String> states = getState();
        state = states.get("state");

        if (!state.equalsIgnoreCase("Abuja")) {
            mAddSupLayout.setVisibility(View.INVISIBLE);
            mViewSupLayout.setVisibility(View.INVISIBLE);
            mViewUserLayout.setVisibility(View.INVISIBLE);

        } else {
            mAddSupLayout.setVisibility(View.VISIBLE);
            mViewSupLayout.setVisibility(View.VISIBLE);
            mViewUserLayout.setVisibility(View.VISIBLE);
        }

        mImageSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click on add user to add", Toast.LENGTH_LONG).show();
            }
        });

        mImgAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addUserIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(addUserIntent);
                finish();
            }
        });

        mImageViewSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_SHORT).show();
//              Intent intent = new Intent(getApplicationContext(), DashActivity.class);
//              startActivity(intent);
                finish();
            }
        });

        mImageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewUserIntent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(viewUserIntent);
                finish();
            }
        });
    }

    private HashMap<String, String> getState() {
        HashMap<String, String> states = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("state", Context.MODE_PRIVATE);
        states.put("state", sharedPreferences.getString("state", null));
        return states;
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}