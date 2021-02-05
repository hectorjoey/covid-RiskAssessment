package hector.developers.covid19riskassessment.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import hector.developers.covid19riskassessment.Api.RetrofitClient;
import hector.developers.covid19riskassessment.R;
import hector.developers.covid19riskassessment.adapter.UserHealthAdapter;
import hector.developers.covid19riskassessment.model.UserHealthData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupervisorActivity extends AppCompatActivity {
    String TAG = "SUPERVISOR ACTIVITY";
    private Toolbar mToolbar;

    ProgressDialog loadingBar;
    private RecyclerView rv;
    private UserHealthAdapter adapter;
    private SearchView searchView;
    private Paint p = new Paint();
    private List<UserHealthData> userHealthDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);
        HashMap<String, String> id = getSupperVisorId1();
        String userId = id.get("userId");
        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Supervisor Dashboard");
        userHealthDataList = populateList();
        loadingBar = new ProgressDialog(this);
        fetchHealthData(userId);
        enableSwipe();
    }

    private void enableSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    final UserHealthData deletedModel = userHealthDataList.get(position);
                    final int deletedPosition = position;
                    adapter.removeItem(position);
                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), " removed from Recyclerview!", Snackbar.LENGTH_LONG);
//                    snackbar.setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // undo is selected, restore the deleted item
//                            adapter.restoreItem(deletedModel, deletedPosition);
//                        }
//                    });
//                    snackbar.setActionTextColor(Color.YELLOW);
//                    snackbar.show();
                }
//                else {
//                    final UserHealthData deletedModel = userHealthDataList.get(position);
//                    final int deletedPosition = position;
//                    adapter.removeItem(position);
//                    // showing snack bar with Undo option
//                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), " removed from Recyclerview!", Snackbar.LENGTH_LONG);
//                    snackbar.setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            // undo is selected, restore the deleted item
//                            adapter.restoreItem(deletedModel, deletedPosition);
//                        }
//                    });
//                    snackbar.setActionTextColor(Color.YELLOW);
//                    snackbar.show();
//                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    private ArrayList<UserHealthData> populateList() {
        ArrayList<UserHealthData> userHealthDataArrayList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            UserHealthData imageModel = new UserHealthData();
//            imageModel.setDate(myImageNameList[i]);
            userHealthDataArrayList.add(imageModel);
        }

        return userHealthDataArrayList;
    }

    public void fetchHealthData(String userId) {
        System.out.println("userHealthDataListID " + userId);
        Call<List<UserHealthData>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUserHealthDataBySupervisorId(String.valueOf(userId));
        call.enqueue(new Callback<List<UserHealthData>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserHealthData>> call, @NonNull Response<List<UserHealthData>> response) {
                if (response.isSuccessful()) {
                    loadingBar.dismiss();
                    assert response.body() != null;
                    Log.d("checker5", "userhealth_data: " + response.toString());
                    Log.d("checker55", "userhealth_data: " + response.body());
                    List<UserHealthData> userHealthDataList = response.body();
                    adapter = new UserHealthAdapter(SupervisorActivity.this, userHealthDataList);
                    sortData(true);
                    rv.setAdapter(adapter);

                } else {
                    Toast.makeText(getApplicationContext(), "failed!", Toast.LENGTH_LONG).show();
                    System.out.println("failed!");
                    Log.d(TAG, "onResponse: ERROR BODY ==>> " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserHealthData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void sortData(boolean asc) {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (asc) {
            Collections.reverse(userHealthDataList);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater =
//                getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        // listening to search query text change
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                adapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                adapter.getFilter().filter(query);
//                return false;
//            }
//        });
//        return true;
//    }

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
                // fetchData(userId);
            case R.id.main_userHealthData:
                userHealthData();
                return true;
            case R.id.main_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void userHealthData() {
        Intent userHealthDataIntent = new Intent(SupervisorActivity.this, UserHealthDataActivity.class);
        userHealthDataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userHealthDataIntent);
        finish();
    }

    private void logout() {
        Intent intent = new Intent(SupervisorActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(SupervisorActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }

    public HashMap<String, String> getSupperVisorId1() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        id.put("userId", sharedPreferences.getString("userId", null));
        return id;
    }
}