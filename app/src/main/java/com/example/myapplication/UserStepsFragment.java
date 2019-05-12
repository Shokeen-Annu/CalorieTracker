package com.example.myapplication;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;

public class UserStepsFragment extends Fragment{

    View view;
    Bundle bundle;
    UserStepsDatabase db = null;
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
            Toast.makeText(view.getContext(), "User steps added successfully!", Toast.LENGTH_LONG).show();
        }
    }
}
