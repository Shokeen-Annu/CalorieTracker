package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PieGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_graph);
        setTitle("Pie Chart");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String pieData = bundle.getString("pieData");
        try {
            PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> entries = new ArrayList();
            ArrayList<String> pieValues = new ArrayList<>();
        if(!pieData.isEmpty()) {
            JSONArray jsonArray = new JSONArray(pieData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int calConsumed = obj.getInt("TotalCaloriesConsumed");
                int calBurned = obj.getInt("TotalCaloriesBurned");
                int calRemaining = obj.getInt("RemainingCalorie");
                entries.add(new PieEntry((float) calConsumed, i));
                entries.add(new PieEntry((float) calBurned, i));
                entries.add(new PieEntry((float) calRemaining, i));
                // counter++;
            }

            pieValues.add("Consumed");
            pieValues.add("Burned");
            pieValues.add("Remaining");
            PieDataSet dataSet = new PieDataSet(entries, "Percentage of Calories");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            dataSet.setValueTextSize(14f);
            dataSet.setSliceSpace(2f);
            dataSet.setSelectionShift(5f);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            Description desc = new Description();
            desc.setText("Calories in %");
            desc.setTextSize(14f);
            pieChart.setDescription(desc);
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setTransparentCircleRadius(25f);
            pieChart.setHoleRadius(25f);
            pieChart.setUsePercentValues(true);

            Legend l = pieChart.getLegend();
            l.setTextSize(15f);
            l.setFormSize(15f);
            l.setForm(Legend.LegendForm.CIRCLE);
            List<LegendEntry> legendEntries = new ArrayList<>();
            for (int i = 0; i < pieValues.size(); i++) {
                LegendEntry entry = new LegendEntry();
                entry.formColor = ColorTemplate.COLORFUL_COLORS[i];
                entry.label = pieValues.get(i);
                legendEntries.add(entry);
            }
            l.setCustom(legendEntries);
        }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
