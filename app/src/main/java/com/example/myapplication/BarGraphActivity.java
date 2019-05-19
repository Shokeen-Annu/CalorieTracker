package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BarGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String barData = bundle.getString("barData");
        try {
            BarChart bar = findViewById(R.id.barChart);
            List<BarEntry> entries = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(barData);
                if(jsonArray.length()>0)
                {
                int counter = 1;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int calConsumed = obj.getInt("TotalCaloriesConsumed");
                    int calBurned = obj.getInt("TotalCaloriesBurned");
                    entries.add(new BarEntry((float) counter, (float) calConsumed, "Calories Consumed"));

                    entries.add(new BarEntry((float) ++counter, (float) calBurned, "Calories Burned"));
                    counter++;
                }


                BarDataSet bSet = new BarDataSet(entries, "Calories Data");
                bSet.setColors(ColorTemplate.COLORFUL_COLORS[0], ColorTemplate.COLORFUL_COLORS[1]);

                ArrayList<String> barValues = new ArrayList<>();
                barValues.add("Calories Consumed");
                barValues.add("Calories Burned");

                bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barValues));

                BarData data = new BarData(bSet);
                data.setValueTextSize(14);
                bSet.setFormSize(14);
                Description description = new Description();
                description.setText("All values in kCal");
                description.setTextSize(14f);
                bar.setDescription(description);
                bar.setData(data);
                bar.setFitBars(true);

                // Creating legend
                Legend l = bar.getLegend();
                l.setFormSize(14f);
                l.setTextSize(14f);
                l.setForm(Legend.LegendForm.CIRCLE);
                List<LegendEntry> legendEntries = new ArrayList<>();
                for (int i = 0; i < barValues.size(); i++) {
                    LegendEntry entry = new LegendEntry();
                    entry.formColor = ColorTemplate.COLORFUL_COLORS[i];
                    entry.label = barValues.get(i);
                    legendEntries.add(entry);
                }
                l.setCustom(legendEntries);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}


