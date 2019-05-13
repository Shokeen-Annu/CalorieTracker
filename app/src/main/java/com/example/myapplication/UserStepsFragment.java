package com.example.myapplication;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserStepsFragment extends Fragment{

    View view;
    Bundle bundle;
    UserStepsDatabase db = null;
    List<HashMap<String,String>> stepsArrayList;
    SimpleAdapter listViewAdapter;
    ListView list_view;

    HashMap<String,String> map = new HashMap<>();
    String[] colHead = new String[]{"ID","DATE AND TIME","STEPS"};
    int[] dataCell = new int[]{R.id.stepid,R.id.time,R.id.steps};
    HashMap<String,String> selectedRow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        view = inflater.inflate(R.layout.fragment_user_steps,container,false);
        bundle = getArguments();

        db = Room.databaseBuilder(view.getContext(),UserStepsDatabase.class,"UserStepsDatabase").fallbackToDestructiveMigration().build();
        // Adding user steps to SQL Lite table
        Button add = view.findViewById(R.id.addStepsButton);
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                try {
                    EditText steps = view.findViewById(R.id.addStepsEditor);
                    String stepsVal = steps.getText().toString();
                    Integer stepsInt;

                    // Validate user input
                    if (!stepsVal.trim().isEmpty()) {
                        stepsInt = Integer.parseInt(stepsVal);
                        Users user = bundle.getParcelable("userObject");
                        UserSteps userSteps = new UserSteps(user.getUserid(),stepsInt, new Date().toString());
                        new AddUserSteps().execute(userSteps);

                    } else {
                        Toast.makeText(view.getContext(), "Enter valid steps.", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(view.getContext(), "Enter valid value.", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button update = view.findViewById(R.id.updateStepsButton);
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                try {
                    EditText steps = view.findViewById(R.id.addStepsEditor);
                    String stepsVal = steps.getText().toString();

                    Button add = view.findViewById(R.id.addStepsButton);
                    add.setVisibility(View.VISIBLE);
                    Button update = view.findViewById(R.id.updateStepsButton);
                    update.setVisibility(View.GONE);
                    steps.setText("");
                    Integer stepsInt;

                    // Validate user input
                    if (!stepsVal.trim().isEmpty()) {
                        stepsInt = Integer.parseInt(stepsVal);
                        Users user = bundle.getParcelable("userObject");

                        UserSteps userSteps = new UserSteps();
                        userSteps.setDateTime(selectedRow.get("DATE AND TIME"));
                        userSteps.setUserId(user.getUserid());
                        userSteps.setSteps(stepsInt);
                        userSteps.setId(Integer.parseInt(selectedRow.get("ID")));

                        new UpdateUserSteps().execute(userSteps);

                    } else {
                        Toast.makeText(view.getContext(), "Enter valid steps.", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(view.getContext(), "Enter valid value.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // List view set up
        list_view = view.findViewById(R.id.list_view_steps);
        stepsArrayList = new ArrayList<>();
        new FetchStepsData().execute();
        return view;
    }

    private class AddUserSteps extends AsyncTask<UserSteps,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(UserSteps... params)
        {
          boolean result = false;
          if(db.userStepsDao().insert(params[0]) > 0)
              result = true;

          return result;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess)
        {
            // List view set up
            stepsArrayList = new ArrayList<>();
            new FetchStepsData().execute();
            Toast.makeText(view.getContext(), "User steps added successfully!", Toast.LENGTH_LONG).show();
        }
    }

    private class FetchStepsData extends AsyncTask<Void,Void,List<UserSteps>>
    {
        @Override
        protected List<UserSteps> doInBackground(Void... params)
        {
            return db.userStepsDao().getAll();
        }

        @Override
        protected void onPostExecute(List<UserSteps> userSteps)
        {
            try {
                for (UserSteps step : userSteps) {
                    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
                    //LocalDateTime date = LocalDateTime.parse(step.getDateTime(),formatter);
                    //String newDate = date.getDayOfMonth() + " " + date.getMonth() + " " + date.getYear() + " " + date.toLocalTime();
                    map = new HashMap<>();
                    map.put("ID",step.getId().toString());
                    map.put("DATE AND TIME", step.getDateTime());
                    map.put("STEPS", step.getSteps().toString());
                    stepsArrayList.add(map);
                }

                listViewAdapter = new SimpleAdapter(view.getContext(), stepsArrayList, R.layout.list_view_steps, colHead, dataCell);
                list_view.setAdapter(listViewAdapter);
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {

                        selectedRow = (HashMap<String,String>)parent.getItemAtPosition(position);
                        try {

                            Button add = view.findViewById(R.id.addStepsButton);
                            add.setVisibility(View.GONE);
                            Button update = view.findViewById(R.id.updateStepsButton);
                            update.setVisibility(View.VISIBLE);
                            EditText steps = view.findViewById(R.id.addStepsEditor);
                            steps.setText(selectedRow.get("STEPS"));

                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(view.getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private class UpdateUserSteps extends AsyncTask<UserSteps,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(UserSteps... params)
        {
             db.userStepsDao().updateUserSteps(params[0]);
             return true;
        }

        @Override
        protected  void onPostExecute(Boolean isSuccess)
        {
            stepsArrayList = new ArrayList<>();
            new FetchStepsData().execute();
        }
    }
}
