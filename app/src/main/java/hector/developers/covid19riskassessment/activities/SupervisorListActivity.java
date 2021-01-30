package hector.developers.covid19riskassessment.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hector.developers.covid19riskassessment.Api.RetrofitClient;
import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.adapter.SupervisorAdapter;
import hector.developers.covid19riskassessment.model.Supervisor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupervisorListActivity extends AppCompatActivity {
    ProgressDialog loadingBar;
    private RecyclerView rv;
    private List<Supervisor> supervisorList;
    private SupervisorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_list);

        rv = findViewById(R.id.supRecyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        loadingBar = new ProgressDialog(this);
        fetchSupList();
    }

    private void fetchSupList() {
        Call<List<Supervisor>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getSupervisors();

        call.enqueue(new Callback<List<Supervisor>>() {
            @Override
            public void onResponse(Call<List<Supervisor>> call, Response<List<Supervisor>> response) {
                loadingBar.dismiss();
                supervisorList = response.body();
                System.out.println("Respondingg >>>>" + supervisorList);
                System.out.println("Respondingg ++++++" + response);
                adapter = new SupervisorAdapter(supervisorList, SupervisorListActivity.this);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Supervisor>> call, Throwable t) {

            }
        });

    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(SupervisorListActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}