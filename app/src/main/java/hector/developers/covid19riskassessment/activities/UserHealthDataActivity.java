package hector.developers.covid19riskassessment.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
    final Calendar myCalendar = Calendar.getInstance();
    EditText mFullname, mDate, mPhone;
    Spinner mAgeSpinner;
    Users users;
    //global variables
    AtomicInteger sectionOneYes = new AtomicInteger();
    //declaring the radio group;
    AtomicInteger sectionTwoYes = new AtomicInteger();
    //local variables
    ArrayList<String> sectionOne = new ArrayList<>();
    ArrayList<String> sectionTwo = new ArrayList<>();
    String phone;
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
    Locale myLocale;
    String currentLanguage = "en", currentLang;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;
    private Spinner mVisitSpinner, mLanguageSpinner;
    private String mGender, mVisit, mAge;
    private List<String> states;
    private String userId;

    private String risk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_health_data);
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        setTitle("User Dashboard");
        loadingBar = new ProgressDialog(this);

        currentLanguage = getIntent().getStringExtra(currentLang);
        mLanguageSpinner = findViewById(R.id.language_spinner);
        List<String> list = new ArrayList<>();
        list.add("Select language");
        list.add("English");
        list.add("Hausa");
        list.add("Igbo");
        HashMap<String, String> id = getUserId();
        userId = id.get("userId");
        mAgeSpinner = findViewById(R.id.age_spinner);
//
        mFullname = findViewById(R.id.edit_text_fullname);
        mDate = findViewById(R.id.edit_date);
        mPhone = findViewById(R.id.phone);
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

        mAgeSpinner = findViewById(R.id.age_spinner);

        mBtn_submit = findViewById(R.id.btn_submit);
//        mUserId = findViewById(R.id.userId);

        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = mFullname.getText().toString().trim();
                String date = mDate.getText().toString().trim();
                final String age = String.valueOf(mAgeSpinner.getSelectedItem());
                final String phone = mPhone.getText().toString().trim();

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

                //validations of fields
                if (TextUtils.isEmpty(fullname)) {
                    mFullname.setError("Please enter initials!");
                    mFullname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    mPhone.setError("Phone number is required!");
                    mPhone.requestFocus();
                    return;
                }
                if (fullname.contains(".") || fullname.contains(",") || fullname.contains("&")
                        || fullname.contains("*") || fullname.contains("-")) {
                    mFullname.setError("special characters not allowed here!");
                    mFullname.requestFocus();
                    return;
                }
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


                /*** The below if condition alone can check for
                 *  High and Low risk (you don't really require
                 *  the section two the check since if the symptoms
                 *  in section 1 gives more than 2 yess then it is high risk
                 */
//                if (sectionOneYes.get() < 2) {
//                    risk = "Low Risk";
//                    System.out.println("RISK IS LOW");
//                } else {
//                    risk = "High Risk";
//                    System.out.println("RISK IS High");
//            }


                /***
                 * BUt you can still add condition from section 2  just to fulfill all righteousness :)
                 * NOTE: you can replace the if condition below with the above one ...enjoy...
                 */
//                if (sectionOneYes.get() < 2 || sectionTwoYes.get() == 0 ) {
//                    System.out.println("RISK IS LOW");
//                    risk = "Low Risk";
                if (sectionOneYes.get() >= 2 || sectionTwoYes.get() > 0) {
                    System.out.println("RISK IS HIGH");
                    risk = "High Risk";
                } else if (sectionOneYes.get() > 1 && sectionTwoYes.get() > 0) {
                    System.out.println("RISK IS HIGH");
                    risk = "High Risk";
                } else {
                    System.out.println("RISK IS LOW");
                    risk = "Low Risk";
                }

                registerUserHealthData(fullname, date, age, phone, feverSymptom,
                        headacheSymptom, sneezingSymptoms, chestPainSymptoms, bodyPainSymptoms,
                        nauseaOrVomitingSymptom, diarrhoeaSymptoms, fluSymptoms, soreThroatSymptoms,
                        fatigueSymptoms, newOrWorseningCough, difficultyInBreathingSymptom,
                        lossOfSmellSymptoms, lossOfTasteSymptoms, contactWithFamily,
                        Long.parseLong(userId), risk
                );
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mAgeSpinner.setAdapter(ageAdapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mLanguageSpinner.setAdapter(languageAdapter);

        mLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("ha");
                        break;
                    case 3:
                        setLocale("ig");
                        break;
                    case 4:
                        setLocale("yo");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //            // TODO Auto-generated method stub
                new DatePickerDialog(UserHealthDataActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy";   //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDate.setText(sdf.format(myCalendar.getTime()));

    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(UserHealthDataActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
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

    public void registerUserHealthData(String fullname, String date, String age, String phone,
                                       String feverSymptom, String headacheSymptom, String sneezingSymptom,
                                       String chestPainSymptom,
                                       String bodyPainSymptoms,
                                       String nauseaOrVomitingSymptom,
                                       String diarrhoeaSymptom,
                                       String fluSymptom, String soreThroatSymptom,
                                       String fatigueSymptom, String newOrWorseningCough, String difficultyInBreathing,
                                       String lossOfOrDiminishedSenseOfSmell, String lossOfOrDiminishedSenseOfTaste,
                                       String contactWithFamily, Long userId, String risk) {
        //making api calls
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Data...");
        progressDialog.show();

        //building retrofit object
        Call<UserHealthData> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUserHealthData(
                        fullname, date, age, phone, feverSymptom, headacheSymptom, sneezingSymptom, chestPainSymptom,
                        bodyPainSymptoms,
                        nauseaOrVomitingSymptom,
                        diarrhoeaSymptom,
                        fluSymptom, soreThroatSymptom,
                        fatigueSymptom, newOrWorseningCough, difficultyInBreathing,
                        lossOfOrDiminishedSenseOfSmell, lossOfOrDiminishedSenseOfTaste, contactWithFamily,
                        userId, risk);
        call.enqueue(new Callback<UserHealthData>() {
            @Override
            public void onResponse(Call<UserHealthData> call, Response<UserHealthData> response) {
                if (response.isSuccessful()) {
                    if (risk.equals("High Risk")) {
                        loadingBar.dismiss();
                        Toast.makeText(UserHealthDataActivity.this, "Data Saved!", Toast.LENGTH_LONG).show();
                        Intent highRiskIntent = new Intent(UserHealthDataActivity.this, HighRiskActivity.class);
                        highRiskIntent.putExtra("risk", "High risk");
                        startActivity(highRiskIntent);
                        finish();
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(UserHealthDataActivity.this, "Data Saved!", Toast.LENGTH_LONG).show();
                        Intent lowRiskIntent = new Intent(UserHealthDataActivity.this, LowRiskActivity.class);
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
        inflater.inflate(R.menu.user_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_settings:
//                settings();
                return true;
            case R.id.main_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        Intent intent = new Intent(UserHealthDataActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(UserHealthDataActivity.this, LoginActivity.class);
                       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }

    public HashMap<String, String> getUserId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        id.put("userId", sharedPreferences.getString("userId", null));
        return id;
    }

    //method that should be outside the onCreate(){}
    private void checkSectionCount(ArrayList<String> section, AtomicInteger count) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            section.forEach(symptom -> {
                if (symptom.equalsIgnoreCase("Yes")) {
                    count.getAndIncrement();
                }
            });
        }
    }
}