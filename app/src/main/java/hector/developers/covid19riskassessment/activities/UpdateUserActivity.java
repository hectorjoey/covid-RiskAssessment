package hector.developers.covid19riskassessment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.api.RetrofitClient;
import hector.developers.covid19riskassessment.model.Users;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {
    private static final int SPINNER_HEIGHT = 500;

    EditText mFirstname, mLastname, mEmail, mDesignation, mPhone, mAddress;
    Button mButtonUpdate;
    Spinner mUserTypeSpinner, mStateSpinner, mLgaSpinner, mAgeSpinner,
            mGender;
    private String mState, mLga, mAge, mSupervisor;
    private List<String> states;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        mStateSpinner = findViewById(R.id.stateSpinner);
        mFirstname = findViewById(R.id.firstname);
        mLastname = findViewById(R.id.lastname);
        mPhone = findViewById(R.id.phone);
        mAddress = findViewById(R.id.address);
        mEmail = findViewById(R.id.email);
        mUserTypeSpinner = findViewById(R.id.userType_spinner);
        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        mButtonUpdate = findViewById(R.id.btnUpdate);
        mGender = findViewById(R.id.genderSpinner);
        mDesignation = findViewById(R.id.designation);

        mAgeSpinner = findViewById(R.id.age_spinner);
        HashMap<String, String> id = getUserId();
        userId = id.get("userId");

        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        states = Nigeria.getStates();

        //call to method that'll set up state and lga spinner
        setupSpinners();

        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final String firstname = mFirstname.getText().toString().trim();
//                final String lastname = mLastname.getText().toString().trim();
                final String age = String.valueOf(mAgeSpinner.getSelectedItem());
//                final String designation = mDesignation.getText().toString().trim();
//                final String userType = String.valueOf(mUserTypeSpinner.getSelectedItem());
                final String gender = String.valueOf(mGender.getSelectedItem());
                final String address = mAddress.getText().toString().trim();
                final String state = String.valueOf(mStateSpinner.getSelectedItem());
                final String lga = String.valueOf(mLgaSpinner.getSelectedItem());
//                final String email = mEmail.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();

//                if (TextUtils.isEmpty(firstname)) {
//                    mFirstname.setError("first name is required!");
//                    mFirstname.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(lastname)) {
//                    mLastname.setError("last name is required!");
//                    mLastname.requestFocus();
//                    return;
//                }
                if (TextUtils.isEmpty(phone)) {
                    mPhone.setError("Phone number is required!");
                    mPhone.requestFocus();
                    return;
                }

//                if (TextUtils.isEmpty(email)) {
//                    mEmail.setError("Email is required!");
//                    mEmail.requestFocus();
//                    return;
//                }
                if (TextUtils.isEmpty(address)) {
                    mAddress.setError("Address is required!");
                    mAddress.requestFocus();
                    return;
                }
//
//                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    mEmail.setError("Enter a valid email!");
//                    mEmail.requestFocus();
//                    return;
//                }


//                if (TextUtils.isEmpty(designation)) {
//                    mDesignation.setError("Designation is required!");
//                    mDesignation.requestFocus();
//                    return;
//                }

//                Log.d("UserUpdate", "firstname" + firstname+ "lastname" +  lastname   );
//                System.out.println(" firstname ::" +  firstname + "lastname :: "+ lastname +"age :" + age + "designation:::"  +designation
//                        + "email ::" + email + "phone ::" +  phone + "gender:: " + gender + "address::" +  address +"state::: "+  state + "lga:::" + lga  );
                UpdateUser();
//                Users users = new Users(Long.valueOf(userId), firstname, lastname, age, designation, phone, email, gender, address, state, lga);

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
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mGender.setAdapter(genderAdapter);
    }


    public HashMap<String, String> getUserId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        id.put("userId", sharedPreferences.getString("userId", null));
        return id;
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

    private void UpdateUser() {

//        System.out.println(" firstname 1::" +  firstname + "lastname1 :: "+ lastname +" age : " + age + " designation::: "  +designation
//                + " email :: " + email + " phone :: " +  phone + " gender:: " + gender + " address:: " +  address +" state::: "+  state + " lga::: " + lga  );
//

        RequestBody address = RequestBody.create(MediaType.parse("text/plain"),
                mAddress.getText()
                        .toString());

        RequestBody state = RequestBody.create(MediaType.parse("text/plain"),
                mStateSpinner.getSelectedItem().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),
                mPhone.getText().toString());
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"),
                mAgeSpinner.getSelectedItem().toString());
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"),
                mGender.getSelectedItem().toString());
        RequestBody lga = RequestBody.create(MediaType.parse("text/plain"),
                mLgaSpinner.getSelectedItem().toString());
        Call<Users> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateUser(Long.valueOf(userId), age, phone,
                        gender, address, state, lga);
//                .updateUser(Long.valueOf(userId), MultipartBody.Part.createFormData( firstname , lastname , age ,designation ,email,
//                       phone, gender , address,  state , lga  ));

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Updated Successfully!", Toast.LENGTH_LONG).show();
//
                    System.out.println("Responding :::@@@" + response);
                    System.out.println("response body::" + response.body());
                    System.out.println("Phone::::: " + response.body().getPhone());

                } else {
                    Toast.makeText(getApplicationContext(), "Updating User failed!", Toast.LENGTH_LONG).show();
                    System.out.println("Failing >>>" + response);
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                System.out.println("Throwable :::" + t.getMessage());

            }
        });
    }
}
