package hector.developers.covid19riskassessment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hector.developers.covid19riskassessment.Api.RetrofitClient;
import hector.developers.covid19riskassessment.activities.DashboardActivity;
import hector.developers.covid19riskassessment.activities.LoginActivity;
import hector.developers.covid19riskassessment.activities.MainActivity;
import hector.developers.covid19riskassessment.adapter.UserAdapter;
import hector.developers.covid19riskassessment.adapter.UserHealthAdapter;
import hector.developers.covid19riskassessment.model.UserHealthData;
import hector.developers.covid19riskassessment.model.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashActivity extends AppCompatActivity {


ProgressDialog loadingBar;
    private RecyclerView rv;
    private List<UserHealthData> userHealthDataList;
    private UserHealthAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        rv = findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        loadingBar = new ProgressDialog(this);
        fetchData();
    }


    private void fetchData() {
        Call<List<UserHealthData>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUserHealthData();
        call.enqueue(new Callback<List<UserHealthData>>() {
            @Override
            public void onResponse(Call<List<UserHealthData>> call, Response<List<UserHealthData>> response) {
                loadingBar.dismiss();
                userHealthDataList = response.body();
                System.out.println("Respondingg >>>>" + userHealthDataList);
                System.out.println("Respondingg ++++++" + response);
                adapter = new UserHealthAdapter( DashActivity.this, userHealthDataList);
                System.out.println(adapter);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<UserHealthData>> call, Throwable t) {
                Toast.makeText(DashActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_refresh:
                fetchData();
                return true;
//            case R.id.main_logout:
//                logout();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        Intent logOut = new Intent(DashActivity.this, LoginActivity.class);
        startActivity(logOut);
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(DashActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }

}