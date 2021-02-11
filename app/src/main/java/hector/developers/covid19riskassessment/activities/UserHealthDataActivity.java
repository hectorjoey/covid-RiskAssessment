package hector.developers.covid19riskassessment.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import hector.developers.covid19riskassessment.Api.RetrofitClient;
import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.model.UserHealthData;
import hector.developers.covid19riskassessment.model.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHealthDataActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar mToolbar;
//    Calendar myCalendar = Calendar.getInstance();

    EditText mDate;

    private ProgressDialog loadingBar;
    private Spinner mVisitSpinner;
    private String mGender, mVisit, mAge;
    private List<String> states;
    private String userId;
    private String firstname;
    private String phone;
    private String userType;

    private String risk;

    //global variables
    AtomicInteger sectionOneYes = new AtomicInteger();
    //declaring the radio group;
    AtomicInteger sectionTwoYes = new AtomicInteger();
    //local variables
    ArrayList<String> sectionOne = new ArrayList<>();
    ArrayList<String> sectionTwo = new ArrayList<>();
    RadioGroup mFever, mHeadache, mSneezing, mChestPain,
            mBodyPain, mNauseaOrVomitingSymptom, mDiarrhoea,
            mFlu, mSoreThroatSymptoms, mFatigueSymptom,
            mNewOrWorseningCough, mDifficultyInBreathing,
            mLossOfSmellSymptoms, mLossOfTasteSymptoms, mContactWithFamily;
    //declaring radio buttons
    RadioButton rbFever, rbHeadache, rbSneezing,
            rbChestPain, rbBodyPain, rbNauseaOrVomitingSymptom, rbDiarrhoea, rbFlu, rbSoreThroatSymptoms,
            rbFatigueSymptom,
            rbNewOrWorseningCough,
            rbDifficultyInBreathing, rbLossOfSmellSymptoms, rbLossOfTasteSymptoms,
            rbContactWithFamily;
    Button mBtn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_health_data);
        loadingBar = new ProgressDialog(this);
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Covid Screening");


        HashMap<String, String> id = getUserId();
        userId = id.get("userId");

        HashMap<String, String> phones = getPhone();
        phone = phones.get("phone");

        HashMap<String, String> firstnames = getFirstname();
        firstname = firstnames.get("firstname");

        HashMap<String, String> userTypes = getUserType();
        userType = userTypes.get("userType");

        if (userType.equalsIgnoreCase("user")) {
            mToolbar.setVisibility(View.INVISIBLE);
        } else {
            mToolbar.setVisibility(View.VISIBLE);
        }

        mDate = findViewById(R.id.edit_date);
        //section1
        mFever = findViewById(R.id.radio_fever);
        mHeadache = findViewById(R.id.radio_headache);
        mSneezing = findViewById(R.id.radio_sneezingSymptoms);
        mChestPain = findViewById(R.id.radio_chestPainSymptoms);
        mBodyPain = findViewById(R.id.radio_bodyPainSymptom);
        mNauseaOrVomitingSymptom = findViewById(R.id.radio_nauseaSymptom);
        mDiarrhoea = findViewById(R.id.radio_diarrhoeaSymptoms);
        mFlu = findViewById(R.id.radio_fluSymptoms);
        mSoreThroatSymptoms = findViewById(R.id.radio_soreSymptoms);
        mFatigueSymptom = findViewById(R.id.fatigueSymptoms);
        //section 2
        mNewOrWorseningCough = findViewById(R.id.cough);
        mDifficultyInBreathing = findViewById(R.id.radio_difficultBreathing);
        mLossOfSmellSymptoms = findViewById(R.id.lossOfSmellSymptoms);
        mLossOfTasteSymptoms = findViewById(R.id.lossTasteSymptoms);
        //section 3
        mContactWithFamily = findViewById(R.id.radio_contactWithFamily);


        mBtn_submit = findViewById(R.id.btn_submit);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   initialize date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserHealthDataActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //   store date in string

                        month=month+1;
                        String sDate = month + "/" + dayOfMonth + "/" + year;
                        // set date on view
                        mDate.setText(sDate);
                        String myFormat = "MM/dd/yy";   //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        mDate.setText(sdf.format(calendar.getTime()));

                    }
                }, year, month, day
                );
                //disable future dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
                //disable past dates
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                //show date
                datePickerDialog.show();
            }
        });

        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = mDate.getText().toString().trim();

                final String feverSymptom = ((RadioButton) findViewById(mFever.getCheckedRadioButtonId())).getText().toString();
                final String headacheSymptom = ((RadioButton) findViewById(mHeadache.getCheckedRadioButtonId())).getText().toString();
                final String sneezingSymptoms = ((RadioButton) findViewById(mSneezing.getCheckedRadioButtonId())).getText().toString();
                final String chestPainSymptoms = ((RadioButton) findViewById(mChestPain.getCheckedRadioButtonId())).getText().toString();
                final String bodyPainSymptoms = ((RadioButton) findViewById(mBodyPain.getCheckedRadioButtonId())).getText().toString();
                final String nauseaOrVomitingSymptom = ((RadioButton) findViewById(mNauseaOrVomitingSymptom.getCheckedRadioButtonId())).getText().toString();
                final String diarrhoeaSymptoms = ((RadioButton) findViewById(mDiarrhoea.getCheckedRadioButtonId())).getText().toString();
                final String fluSymptoms = ((RadioButton) findViewById(mFlu.getCheckedRadioButtonId())).getText().toString();
                final String soreThroatSymptoms = ((RadioButton) findViewById(mSoreThroatSymptoms.getCheckedRadioButtonId())).getText().toString();
                final String fatigueSymptoms = ((RadioButton) findViewById(mFatigueSymptom.getCheckedRadioButtonId())).getText().toString();

                final String newOrWorseningCough = ((RadioButton) findViewById(mNewOrWorseningCough.getCheckedRadioButtonId())).getText().toString();
                final String difficultyInBreathingSymptom = ((RadioButton) findViewById(mDifficultyInBreathing.getCheckedRadioButtonId())).getText().toString();
                final String lossOfSmellSymptoms = ((RadioButton) findViewById(mLossOfSmellSymptoms.getCheckedRadioButtonId())).getText().toString();
                final String lossOfTasteSymptoms = ((RadioButton) findViewById(mLossOfTasteSymptoms.getCheckedRadioButtonId())).getText().toString();

                final String contactWithFamily = ((RadioButton) findViewById(mContactWithFamily.getCheckedRadioButtonId())).getText().toString();

                if (TextUtils.isEmpty(date)) {
                    mDate.setError("Enter date!");
                    mDate.requestFocus();
                    return;
                }
                //add section 1 results to the arrayList
                sectionOne.add(feverSymptom);
                sectionOne.add(headacheSymptom);
                sectionOne.add(chestPainSymptoms);
                sectionOne.add(sneezingSymptoms);
                sectionOne.add(chestPainSymptoms);
                sectionOne.add(bodyPainSymptoms);
                sectionOne.add(nauseaOrVomitingSymptom);
                sectionOne.add(diarrhoeaSymptoms);
                sectionOne.add(fluSymptoms);
                sectionOne.add(soreThroatSymptoms);
                sectionOne.add(fatigueSymptoms);

                //add section 2 results to the arrayList
                sectionTwo.add(newOrWorseningCough);
                sectionTwo.add(difficultyInBreathingSymptom);
                sectionTwo.add(lossOfSmellSymptoms);
                sectionTwo.add(lossOfTasteSymptoms);
                sectionTwo.add(contactWithFamily);

                //call the method for each section
                checkSectionCount(sectionOne, sectionOneYes);
                checkSectionCount(sectionTwo, sectionTwoYes);


                System.out.println("SECTION ONE ==>>> " + sectionOne);
                System.out.println("SECTION ONE YESSES ==>>> " + sectionOneYes);

                System.out.println("SECTION TWO ==>>> " + sectionTwo);
                System.out.println("SECTION TWO YESSES ==>>> " + sectionTwoYes);


                if ((sectionOneYes.get() >= 2) || (sectionTwoYes.get() >= 1)) {
                    Log.d("section2244..", "high risk: ");
                    risk = "High Risk";
                } else {
                    Log.d("section2244..", "low risk: ");
                    risk = "Low Risk";
                }

                registerUserHealthData(date, feverSymptom,
                        headacheSymptom, sneezingSymptoms, chestPainSymptoms, bodyPainSymptoms,
                        nauseaOrVomitingSymptom, diarrhoeaSymptoms, fluSymptoms, soreThroatSymptoms,
                        fatigueSymptoms, newOrWorseningCough, difficultyInBreathingSymptom,
                        lossOfSmellSymptoms, lossOfTasteSymptoms, contactWithFamily,
                        Long.parseLong(userId), firstname, phone, risk, userType
                );
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        int radioButtonId2 = mFever.getCheckedRadioButtonId();
        rbFever = findViewById(radioButtonId2);

        int radioButtonId3 = mHeadache.getCheckedRadioButtonId();
        rbHeadache = findViewById(radioButtonId3);

        int radioButtonId12 = mNewOrWorseningCough.getCheckedRadioButtonId();
        rbNewOrWorseningCough = findViewById(radioButtonId12);

        int radioButtonId13 = mBodyPain.getCheckedRadioButtonId();
        rbBodyPain = findViewById(radioButtonId13);

        int radioButtonId4 = mDifficultyInBreathing.getCheckedRadioButtonId();
        rbDifficultyInBreathing = findViewById(radioButtonId4);

        int radioButtonId5 = mSneezing.getCheckedRadioButtonId();
        rbSneezing = findViewById(radioButtonId5);

        int radioButtonId6 = mChestPain.getCheckedRadioButtonId();
        rbChestPain = findViewById(radioButtonId6);

        int radioButtonId7 = mDiarrhoea.getCheckedRadioButtonId();
        rbDiarrhoea = findViewById(radioButtonId7);

        int radioButtonId8 = mFlu.getCheckedRadioButtonId();
        rbFlu = findViewById(radioButtonId8);

        int radioButtonId9 = mSoreThroatSymptoms.getCheckedRadioButtonId();
        rbSoreThroatSymptoms = findViewById(radioButtonId9);

        int radioButtonId10 = mLossOfSmellSymptoms.getCheckedRadioButtonId();
        rbLossOfSmellSymptoms = findViewById(radioButtonId10);

        int radioButtonId11 = mLossOfTasteSymptoms.getCheckedRadioButtonId();
        rbLossOfTasteSymptoms = findViewById(radioButtonId11);

        int radioButtonId14 = mNauseaOrVomitingSymptom.getCheckedRadioButtonId();
        rbNauseaOrVomitingSymptom = findViewById(radioButtonId14);
        int radioButtonId15 = mFatigueSymptom.getCheckedRadioButtonId();
        rbFatigueSymptom = findViewById(radioButtonId15);

        int radioButtonId = mContactWithFamily.getCheckedRadioButtonId();
        rbContactWithFamily = findViewById(radioButtonId);
    }

    public void registerUserHealthData(String date, String feverSymptom, String headacheSymptom, String sneezingSymptom,
                                       String chestPainSymptom, String bodyPainSymptoms, String nauseaOrVomitingSymptom,
                                       String diarrhoeaSymptom, String fluSymptom, String soreThroatSymptom,
                                       String fatigueSymptom, String newOrWorseningCough, String difficultyInBreathing,
                                       String lossOfOrDiminishedSenseOfSmell, String lossOfOrDiminishedSenseOfTaste,
                                       String contactWithFamily, Long userId, String firstname, String phone, String risk, String userType) {
        //making api calls
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Data...");
        progressDialog.show();

        //building retrofit object
        Call<UserHealthData> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUserHealthData(
                        date, feverSymptom, headacheSymptom, sneezingSymptom, chestPainSymptom,
                        bodyPainSymptoms,
                        nauseaOrVomitingSymptom,
                        diarrhoeaSymptom,
                        fluSymptom, soreThroatSymptom,
                        fatigueSymptom, newOrWorseningCough, difficultyInBreathing,
                        lossOfOrDiminishedSenseOfSmell, lossOfOrDiminishedSenseOfTaste, contactWithFamily,
                        userId, firstname, phone, risk, userType);
        call.enqueue(new Callback<UserHealthData>() {
            @Override
            public void onResponse(Call<UserHealthData> call, Response<UserHealthData> response) {
                if (response.isSuccessful()) {
                    if (risk.equals("High Risk")) {
                        loadingBar.dismiss();
                        Toast.makeText(UserHealthDataActivity.this, "Data Saved!", Toast.LENGTH_LONG).show();
                        Intent highRiskIntent = new Intent(UserHealthDataActivity.this, HighRiskActivity.class);
                        highRiskIntent.putExtra("risk", "High Risk");
                        highRiskIntent.getStringExtra(phone);
                        startActivity(highRiskIntent);
                        finish();
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(UserHealthDataActivity.this, "Data Saved!", Toast.LENGTH_LONG).show();
                        Intent lowRiskIntent = new Intent(UserHealthDataActivity.this, LowRiskActivity.class);
                        lowRiskIntent.getStringExtra(phone);
                        startActivity(lowRiskIntent);
                        finish();
                    }
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(UserHealthDataActivity.this, "Data sending failed!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserHealthData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserHealthDataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_refresh:
                // fetchData(userId);
            case R.id.main_userHealthData:
                moveToSupervisor();
                return true;
//            case R.id.main_logout:
//                logout();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void moveToSupervisor() {
        Intent supIntent = new Intent(getApplicationContext(), SupervisorActivity.class);
        startActivity(supIntent);
        finish();
    }


    public HashMap<String, String> getUserId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        id.put("userId", sharedPreferences.getString("userId", null));
        return id;
    }

    private HashMap<String, String> getPhone() {
        HashMap<String, String> phones = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("phone", Context.MODE_PRIVATE);
        phones.put("phone", sharedPreferences.getString("phone", null));
        return phones;
    }

    private HashMap<String, String> getFirstname() {
        HashMap<String, String> firstnames = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("firstname", Context.MODE_PRIVATE);
        firstnames.put("firstname", sharedPreferences.getString("firstname", null));
        return firstnames;
    }

    private HashMap<String, String> getUserType() {
        HashMap<String, String> userTypes = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userType", Context.MODE_PRIVATE);
        userTypes.put("userType", sharedPreferences.getString("userType", null));
        return userTypes;
    }

    //method that should be outside the onCreate(){}
    private void checkSectionCount(ArrayList<String> section, AtomicInteger count) {
        for (String sections : section) {
            if (sections.equalsIgnoreCase("Yes")) {
                count.incrementAndGet();
            }
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(UserHealthDataActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }

}