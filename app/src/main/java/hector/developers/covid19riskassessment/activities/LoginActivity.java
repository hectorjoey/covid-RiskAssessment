package hector.developers.covid19riskassessment.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;

import hector.developers.covid19riskassessment.Api.RetrofitClient;
import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.model.Users;
import hector.developers.covid19riskassessment.utils.SessionManagement;
import hector.developers.covid19riskassessment.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText mEmail, mPassword;
    Button mLogin;
    TextView supLink;
    ProgressDialog progressDialog;
    private Util util;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mLogin = findViewById(R.id.btn_login);
        supLink = findViewById(R.id.tvSupervisorLink);
//        loadingBar = new ProgressDialog(this);
        HashMap<String, String> status = status();
        String statuss = status.get("status");
        util = new Util();

        sessionManagement = new SessionManagement(this);

        sessionManagement.getLoginEmail();
        sessionManagement.getLoginPassword();

        if (statuss != null) {

        } else {
            showCustomDialog();
        }

        supLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent superviseIntent = new Intent(LoginActivity.this, SupLoginActivity.class);
                startActivity(superviseIntent);
                finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //validating fields
                if (util.isNetworkAvailable(getApplicationContext())) {
                    if (email.isEmpty()) {
                        mEmail.setError("Email is required");
                        mEmail.requestFocus();
                        return;
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        mEmail.setError("Enter a valid email!");
                        mEmail.requestFocus();
                        return;
                    }

                    if (password.isEmpty()) {
                        mPassword.setError("Enter Password!");
                        mPassword.requestFocus();
                        return;
                    }

                    if (password.length() < 6) {
                        mPassword.setError("Password should be at least 6 character long");
                        mPassword.requestFocus();
                    }
                    loginUser(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginUser(String email, String password) {
        //do registration API call
        Call<Users> call = RetrofitClient
                .getInstance()
                .getApi().login(email, password);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Please Wait...");
        progressDialog.show();

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {

                    Users users = response.body();
                    System.out.println("users ..... " + users);
                    Log.d("user33", "response:..  " + new Gson().toJson(response.body()));
                    String phone = response.body().getPhone();
                    System.out.println("phone " + phone);
                    String email = response.body().getEmail();
                    System.out.println("email " + email);
                    String state = response.body().getState();
                    System.out.println("state " + state);

                    System.out.println("users XXXXXX " + response);
                    assert response.body() != null;
//                    userID = response.body().getId();
                    assert users != null;
                    //save userId to shared pref
                    if (users.getUserType().equalsIgnoreCase("admin")) {
                        Intent adminIntent = new Intent(LoginActivity.this, MainActivity.class);
                        assert response.body() != null;
                        sessionManagement.setLoginEmail(email);
                        sessionManagement.setLoginPassword(password);
                        adminIntent.putExtra("Admin", response.body().getUserType());
                        startActivity(adminIntent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Welcome Admin!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, UserHealthDataActivity.class);
                        intent.putExtra("userId ", users.getId());
                        saveUserId(users.getId());
                        System.out.println("users ,,,," + users.getId());
                        assert response.body() != null;
                        intent.putExtra("User", response.body().getUserType());
                        intent.putExtra("state", response.body().getState());
                        startActivity(intent);
                        sessionManagement.setLoginEmail(email);
                        sessionManagement.setLoginPassword(password);
                        Toast.makeText(LoginActivity.this, "Welcome User!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    System.out.println("RESPONSE ==>> " + response);
                    Toast.makeText(LoginActivity.this, "Cannot Login! Check Credentials..!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Throwing >>>>" + t);
            }
        });
    }

    public void saveUserId(Long id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("userId", id + "");
        edit.apply();
    }


    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewGroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        Button ok = dialogView.findViewById(R.id.buttonOk);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStatus();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public HashMap<String, String> status() {
        HashMap<String, String> user = new HashMap<>();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("status", Context.MODE_PRIVATE);
        user.put("status", sharedPreferences.getString("status", null));
        return user;
    }

    @SuppressLint("ApplySharedPref")
    public void saveStatus() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status", "1");
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}