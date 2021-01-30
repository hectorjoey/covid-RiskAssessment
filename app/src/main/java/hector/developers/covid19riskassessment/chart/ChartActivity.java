package hector.developers.covid19riskassessment.chart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import hector.developers.covid19riskassessment.R;


public class ChartActivity extends AppCompatActivity {
    RelativeLayout mPieChartView, mBarChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mPieChartView = findViewById(R.id.vPieChart);
        mBarChartView = findViewById(R.id.vBarChart);

        mPieChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pieChartIntent = new Intent(getApplicationContext(), PieChartActivity.class);
                startActivity(pieChartIntent);
                finish();
            }
        });

        mBarChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barChatIntent = new Intent(getApplicationContext(), BarChartActivity.class);
                startActivity(barChatIntent);
                finish();
            }
        });
    }
}