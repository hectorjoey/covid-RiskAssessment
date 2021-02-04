package hector.developers.covid19riskassessment.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hector.developers.covid19riskassessment.Api.RetrofitClient;
import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.utils.Util;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {

    private static final int SPINNER_HEIGHT = 500;
    EditText mFirstname, mLastname, mPhone, mEmail, mPassword, mDesignation;
    Button supRegister;

    Spinner mUserTypeSpinner, mStateSpinner;
    private ProgressDialog loadingBar;
    private String mState;
    private List<String> states;
    String id, stateName;

    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mStateSpinner = findViewById(R.id.stateSpinner);
        mFirstname = findViewById(R.id.Supfirstname);
        mLastname = findViewById(R.id.SupLastname);
        mPhone = findViewById(R.id.SupPhone);
        mEmail = findViewById(R.id.Supemail);
        mPassword = findViewById(R.id.Suppassword);
        mUserTypeSpinner = findViewById(R.id.SupUsertype_spinner);
        mStateSpinner = findViewById(R.id.SupstateSpinner);
        supRegister = findViewById(R.id.supRegister);
        mDesignation = findViewById(R.id.Supdesignation);
        loadingBar = new ProgressDialog(this);

        util = new Util();
//        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
//
//        states = Nigeria.getStates();

        statesFetch();

        //call to method that'll set up state and lga spinner
//        setupSpinners();

        supRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String firstname = mFirstname.getText().toString().trim();
                final String lastname = mLastname.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String userType = String.valueOf(mUserTypeSpinner.getSelectedItem());
                final String state = String.valueOf(mStateSpinner.getSelectedItem());
                final String designation = mDesignation.getText().toString().trim();

                //validating fields
                if (util.isNetworkAvailable(getApplicationContext())) {
                    if (TextUtils.isEmpty(firstname)) {
                        mFirstname.setError("first name is required!");
                        mFirstname.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(lastname)) {
                        mLastname.setError("last name is required!");
                        mLastname.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(phone)) {
                        mPhone.setError("Phone number is required!");
                        mPhone.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        mEmail.setError("Email is required!");
                        mEmail.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        mEmail.setError("Enter a valid email!");
                        mEmail.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        mPassword.setError("Password is required!");
                        mPassword.requestFocus();
                        return;
                    }
                    if (password.length() < 6) {
                        mPassword.setError("Password length is too short!");
                        mPassword.requestFocus();
                    }
                    if (TextUtils.isEmpty(designation)) {
                        mDesignation.setError("Designation is required!");
                    }

                    registerSup(firstname, lastname, phone, email, password,
                            userType, state, designation);

                } else {
                    Toast.makeText(RegisterUserActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.userType_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mUserTypeSpinner.setAdapter(userTypeAdapter);
    }

    private void statesFetch() {
        AndroidNetworking.get("https://covid-19-risk-assesment-server.herokuapp.com/api/v1/states")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.d("resp2", response.toString());

                        List<String> stateNames = new ArrayList<>();
                        List<Long> idss2 = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json_data = response.getJSONObject(i);
                                id = json_data.getString("id");
                                stateName = json_data.getString("stateName");
                                Long id = json_data.getLong("id");
                                Log.d("resp4", "id: " + id + " stateName: " + stateName);
                                //initializing spinner;
                                idss2.add(id);
                                stateNames.add(stateName);
//                                System.out.println("IDD22  +++++ " + idss2);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        spinner(idss2, stateNames);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("err2", error.toString());
                    }
                });
    }

    private void spinner(List<Long> idLits2, List<String> stateNames1) {
//        System.out.println("idsggsgs " + idLits2 + "nmammeme" + stateNames1);
        final ArrayAdapter states = new ArrayAdapter<>(RegisterUserActivity.this, R.layout.support_simple_spinner_dropdown_item, stateNames1);
        states.notifyDataSetChanged();
        mStateSpinner.setAdapter(states);
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Long ids2 = idLits2.get(position);
                System.out.println("stanley " + ids2);
                saveId(ids2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void saveId(Long id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("id", id + "");
        edit.apply();
    }


    /**
     * Method to set up the spinners
     */
//    public void setupSpinners() {
//        // Create adapter for spinner. The list options are from the String array it will use
//        // the spinner will use the default layout
//        //populates the quantity spinner ArrayList
//        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
//
//        // Specify dropdown layout style - simple list view with 1 item per line
//        statesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        // Apply the adapter to the spinner
//        statesAdapter.notifyDataSetChanged();
//        mStateSpinner.setAdapter(statesAdapter);
//
//        // Set the integer mSelected to the constant values
//        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mState = (String) parent.getItemAtPosition(position);
////                setUpStatesSpinner(position);
//            }
//
//            // Because AdapterView is an abstract class, onNothingSelected must be defined
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Unknown
//            }
//        });
//    }
//
//    private void resizeSpinner(Spinner spinner, int height) {
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//
//            //Get private mPopup member variable and try cast to ListPopupWindow
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
//
//            //set popupWindow height to height
//            popupWindow.setHeight(height);
//
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException ex) {
//            ex.printStackTrace();
//        }
//    }
    public void registerSup(String firstname, String lastname, String phone, String email, String password,
                            String userType, String state, String designation) {

        //do registration API call
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createSupervisor(firstname, lastname, phone, email, password,
                        userType, state, designation);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(RegisterUserActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                System.out.println("Responding ::: " + response);
                Intent intent = getIntent();
                clearFields();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterUserActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                System.out.println("throwing " + t);
            }
        });
    }

    private void clearFields() {
        mEmail.setText("");
        mPassword.setText("");
        mFirstname.setText("");
        mDesignation.setText("");
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}