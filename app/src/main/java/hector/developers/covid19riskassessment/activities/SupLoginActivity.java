package hector.developers.covid19riskassessment.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import hector.developers.covid19riskassessment.Api.RetrofitClient;
import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.model.Supervisor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupLoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mSupLogin;
    ProgressDialog loadingBar;
    TextView userLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_login);
        mSupLogin = findViewById(R.id.supervisorLogin);
        mEmail  = findViewById(R.id.et_email);
        mPassword  = findViewById(R.id.et_password);
        loadingBar = new ProgressDialog(this);
        userLink = findViewById(R.id.tvUser);

        userLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupLoginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mSupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    mEmail.setError("Email is required");
                    mEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Enter a valid email");
                    mEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    mPassword.setError("Password required");
                    mPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password should be at least 6 character long");
                    mPassword.requestFocus();
                }
                loginSup(email, password);
            }

        });
    }

    private void loginSup(String email, String password) {
            Call<Supervisor> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .supLogin(email, password);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Authenticating please wait...");
            progressDialog.show();
            call.enqueue(new Callback<Supervisor>() {
                @Override
                public void onResponse(Call<Supervisor> call, Response<Supervisor> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Long supervisorId = response.body().getId();
                        System.out.println("SUPPERVISORID " + supervisorId);
                        suppervisorId(supervisorId);
                        Intent supIntent = new Intent(SupLoginActivity.this, SupervisorActivity.class);
                        startActivity(supIntent);
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(SupLoginActivity.this, "Welcome Sup!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(SupLoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Supervisor> call, Throwable t) {
                    Toast.makeText(SupLoginActivity.this, "failed!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    public void suppervisorId(Long id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("suppervisorId", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("suppervisorId", id + "");
        edit.apply();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to go back?");
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


}




