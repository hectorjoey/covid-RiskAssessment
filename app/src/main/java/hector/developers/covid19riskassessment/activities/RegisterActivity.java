package hector.developers.covid19riskassessment.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hector.developers.covid19riskassessment.api.RetrofitClient;
import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.model.Users;
import hector.developers.covid19riskassessment.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final int SPINNER_HEIGHT = 500;
    EditText mFirstname, mLastname, mEmail, mPassword, mDesignation, mPhone, mAddress;
    Button mButtonRegister;
    Spinner mUserTypeSpinner, mStateSpinner, mLgaSpinner,
            mGender, mSupervisorSpinner;
    private ProgressDialog loadingBar;
    private String mState, mLga, mAge, mSupervisor;
    private List<String> states;

    private Spinner mAgeSpinner;
    private SearchableSpinner spSupervisor;
    private SmartMaterialSpinner spEmptyItem;
    private List<String> supervisorsList;
    ArrayAdapter<String> adapter;
    String id, firstname, stateName, userType;

    private Util util;

    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mStateSpinner = findViewById(R.id.stateSpinner);
        mFirstname = findViewById(R.id.firstname);
        mLastname = findViewById(R.id.lastname);
        mPhone = findViewById(R.id.phone);
        mAddress = findViewById(R.id.address);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mUserTypeSpinner = findViewById(R.id.userType_spinner);
        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        mButtonRegister = findViewById(R.id.btnRegister);
        mGender = findViewById(R.id.genderSpinner);
        mDesignation = findViewById(R.id.designation);
        loadingBar = new ProgressDialog(this);
        spSupervisor = findViewById(R.id.spinner1);
        mAgeSpinner = findViewById(R.id.age_spinner);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        Intent intent = new Intent();

//        String state_n = getIntent().getExtras().getString("state");

        Log.d("state33", "state: " + getIntent().getStringExtra("state"));

        util = new Util();

        network();
//        statesFetch();

        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        states = Nigeria.getStates();
//        call to method that'll set up state and lga spinner
        setupSpinners();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                network();
                Toast.makeText(getApplicationContext(), "Refreshing Supervisors!", Toast.LENGTH_LONG).show();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeRefresh.setRefreshing(false);
                    }
                }, 4000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeRefresh.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = mFirstname.getText().toString().trim();
                final String lastname = mLastname.getText().toString().trim();
                final String age = String.valueOf(mAgeSpinner.getSelectedItem());
                final String designation = mDesignation.getText().toString().trim();
                final String userType = String.valueOf(mUserTypeSpinner.getSelectedItem());
                final String gender = String.valueOf(mGender.getSelectedItem());
                final String address = mAddress.getText().toString().trim();
                final String state = String.valueOf(mStateSpinner.getSelectedItem());
                final String lga = String.valueOf(mLgaSpinner.getSelectedItem());
                final String email = mEmail.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
//                  final String supervisedBy = spSupervisor.getSelectedItem().toString();

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
                    if (TextUtils.isEmpty(address)) {
                        mAddress.setError("Address is required!");
                        mAddress.requestFocus();
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
                    HashMap<String, String> id = getId();
                    String id1 = id.get("id");

                    registerUser(firstname, lastname, age, designation, email, phone, userType,
                            gender, address, password, state, lga, id1);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
                }
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
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.userType_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mUserTypeSpinner.setAdapter(userTypeAdapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mGender.setAdapter(genderAdapter);

    }

    private void network() {
        AndroidNetworking.get("https://covid-19-risk-assesment-server.herokuapp.com/api/v1/users")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        Log.d("resp2", response.toString());

                        List<String> firstNames = new ArrayList<>();
                        List<Long> idss = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json_data = response.getJSONObject(i);
                                id = json_data.getString("id");
                                userType = json_data.getString("userType");
                                if (userType.equalsIgnoreCase("supervisor") || userType.equals("ADMIN")) {
                                    firstname = json_data.getString("firstname");
                                    Long id = json_data.getLong("id");
                                    Log.d("resp4", "id: " + id + " firstname: " + firstname);
                                    //initializing spinner;
                                    idss.add(id);
                                    firstNames.add(firstname);
                                    System.out.println("IDD  +++++ " + idss);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        spinner(idss, firstNames);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("err2", error.toString());
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

    public HashMap<String, String> getId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        id.put("id", sharedPreferences.getString("id", null));
        return id;
    }

    //spSupervisor
    public void spinner(List<Long> idLits, List<String> fullNames1) {
        System.out.println("idsggsgs " + idLits + "nmammeme" + fullNames1);
        final ArrayAdapter superVisor = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, fullNames1);
        superVisor.notifyDataSetChanged();
        spSupervisor.setAdapter(superVisor);
        spSupervisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Long ids1 = idLits.get(position);
                System.out.println("stanley " + ids1);
                saveId(ids1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Method to set up the spinners
     */
    public void setupSpinners() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        //populates the quantity spinner ArrayList
        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);

        // Specify dropdown layout style - simple list view with 1 item per line
        statesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        statesAdapter.notifyDataSetChanged();
        mStateSpinner.setAdapter(statesAdapter);

        // Set the integer mSelected to the constant values
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = (String) parent.getItemAtPosition(position);
                setUpStatesSpinner(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Unknown
            }
        });
    }

    /**
     * method to set up the state spinner
     *
     * @param position current position of the spinner
     */
    private void setUpStatesSpinner(int position) {
        List<String> list = new ArrayList<>(Nigeria.getLgasByState(states.get(position)));
        setUpLgaSpinner(list);
    }

    /**
     * Method to set up the local government areas corresponding to selected states
     *
     * @param lgas represents the local government areas of the selected state
     */
    private void setUpLgaSpinner(List<String> lgas) {
        ArrayAdapter lgaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lgas);
        lgaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        lgaAdapter.notifyDataSetChanged();
        mLgaSpinner.setAdapter(lgaAdapter);

        mLgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mLga = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void resizeSpinner(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            //Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            //set popupWindow height to height
            popupWindow.setHeight(height);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void registerUser(String firstname, String lastname, String age, String designation, String email,
                             String phone, String userType, String gender, String address, String password,
                             String state, String lga, String supervisedBy) {

        //do registration API call
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();
        Call<Users> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(firstname, lastname, age, designation, phone, email, password, userType, gender, address, state, lga, supervisedBy);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                System.out.println("Responding ::: " + response);
                clearFields();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                System.out.println("throwing " + t);
            }
        });
    }

    public void clearFields() {
        mEmail.setText("");
        mPassword.setText("");
        mFirstname.setText("");
        mLastname.setText("");
        mDesignation.setText("");
        mPhone.setText("");
        mAddress.setText("");
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}