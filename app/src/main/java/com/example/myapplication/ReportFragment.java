package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReportFragment extends Fragment {

    View view;
    DatePickerDialog datePicker;
    Bundle bundle;
    Integer userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_report, container, false);
        bundle = getArguments();
        userId = bundle.getInt("userId");

        // Show appropriate fields

        Button pieBtn = view.findViewById(R.id.pieButton);
        Button barBtn = view.findViewById(R.id.barButton);
        final EditText dateEditor = view.findViewById(R.id.date);
        final EditText fromDate = view.findViewById(R.id.fromDate);
        final EditText toDate = view.findViewById(R.id.toDate);
        final TextView dateTv = view.findViewById(R.id.dateTv);
        final TextView fromDateTv = view.findViewById(R.id.fromDateTv);
        final TextView toDateTv = view.findViewById(R.id.toDateTv);
        pieBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            //Date picker for Date

            dateEditor.setVisibility(View.VISIBLE);
            dateTv.setVisibility(View.VISIBLE);
            dateEditor.setInputType(InputType.TYPE_NULL);
            dateEditor.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v)
                {
                    final Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    datePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            LocalDate date = Year.of(year).atMonth(month+1).atDay(dayOfMonth);
                            dateEditor.setText(date.toString());
                            new GetPieData().execute(date.toString());
                        }
                    },year,month,day);
                    datePicker.show();
                }
            });

            toDate.setVisibility(View.GONE);
            fromDate.setVisibility(View.GONE);
            toDateTv.setVisibility(View.GONE);
            fromDateTv.setVisibility(View.GONE);
        }
        });

        barBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //Date picker for from Date

                fromDate.setVisibility(View.VISIBLE);
                fromDateTv.setVisibility(View.VISIBLE);
                fromDate.setInputType(InputType.TYPE_NULL);
                fromDate.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v)
                    {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        datePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                LocalDate date = Year.of(year).atMonth(month+1).atDay(dayOfMonth);
                                fromDate.setText(date.toString());
                            }
                        },year,month,day);
                        datePicker.show();
                    }
                });

                //Date picker for to Date

                toDate.setVisibility(View.VISIBLE);
                toDateTv.setVisibility(View.VISIBLE);
                toDate.setInputType(InputType.TYPE_NULL);
                toDate.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v)
                    {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        datePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                LocalDate date = Year.of(year).atMonth(month + 1).atDay(dayOfMonth);
                                toDate.setText(date.toString());
                                // Bar chart
                                String fDate = fromDate.getText().toString();
                                String tDate = toDate.getText().toString();
                                if(!fDate.isEmpty() && !tDate.isEmpty())
                                    new GetBarData().execute(fDate,tDate);
                                else
                                    Toast.makeText(view.getContext(),"Either from date or to date or both are missing!",Toast.LENGTH_LONG).show();

                            }
                        },year,month,day);
                        datePicker.show();
                    }
                });

                dateEditor.setVisibility(View.GONE);
                dateTv.setVisibility(View.GONE);
            }
        });


        return view;
    }

    private class GetBarData extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            return RestClient.getReportOnDuration(userId,params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String s) {

                Intent intent = new Intent(view.getContext(),BarGraphActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("barData",s);
                intent.putExtras(bundle);
                startActivity(intent);

        }
    }

    private class GetPieData extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            return RestClient.getReportOnGivenDate(userId,params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
                Intent intent = new Intent(view.getContext(),PieGraphActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pieData",s);
                intent.putExtras(bundle);
                startActivity(intent);

        }
    }

}
