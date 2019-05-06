package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class NewUser extends AppCompatActivity {

    DatePickerDialog datePicker;
    TextView message ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        //Date picker for Date of Birth field
        final EditText dobEditor = (EditText)findViewById(R.id.dobEditor);
        dobEditor.setInputType(InputType.TYPE_NULL);
        dobEditor.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                datePicker = new DatePickerDialog(NewUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        LocalDate dateOfBirth = Year.of(year).atMonth(month).atDay(dayOfMonth);
                        dobEditor.setText(dateOfBirth.toString());
                    }
                },year,month,day);
                datePicker.show();
            }
        });

        //Spinner on activity level
        final EditText actLevelEditor = (EditText)findViewById(R.id.activityLevelEditor);
        final Spinner actLevelSpinner = (Spinner)findViewById(R.id.activityLevelSpinner);
        final ArrayAdapter<ActivityLevel> actLevelAdapter = new ArrayAdapter<ActivityLevel>(this,android.R.layout.simple_spinner_item,ActivityLevel.values());
        actLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actLevelSpinner.setAdapter(actLevelAdapter);
        actLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ActivityLevel selectedActLevel = (ActivityLevel) parent.getItemAtPosition(position);
                if(selectedActLevel!=null)
                    actLevelEditor.setText(String.valueOf(selectedActLevel.ordinal()+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void createNewUser(View v)
    {
        PostUser postUser=new PostUser();
        PostCredential postCredential=new PostCredential();

        try {
            //Fetching input data
            EditText fName = (EditText) findViewById(R.id.fNameEditor);
            String firstName = fName.getText().toString();

            EditText lName = (EditText) findViewById(R.id.lNameEditor);
            String lastName = lName.getText().toString();

            EditText email = (EditText) findViewById(R.id.emailEditor);
            String emailId = email.getText().toString();

            EditText dobEditor = (EditText) findViewById(R.id.dobEditor);
            Date dob = DateFormat.formatDate(dobEditor.getText().toString()); //java.date.tosql ..check forum post

            EditText height = (EditText) findViewById(R.id.heightEditor);
            BigDecimal heightVal = new BigDecimal(height.getText().toString());

            EditText weight = (EditText) findViewById(R.id.weightEditor);
            BigDecimal weightVal = new BigDecimal(weight.getText().toString());

            RadioGroup gender = (RadioGroup) findViewById(R.id.genderEditor);
            int genderSelectedId = gender.getCheckedRadioButtonId();
            RadioButton genderId = findViewById(genderSelectedId);
            char genderChar = 'M';
            if (genderId.getText().toString().equals("Female"))
                genderChar = 'F';

            EditText address = (EditText) findViewById(R.id.addEditor);
            String add = address.getText().toString();

            EditText postcode = (EditText) findViewById(R.id.postcodeEditor);
            String pcode = postcode.getText().toString();

            EditText actLevelEditor = (EditText) findViewById(R.id.activityLevelEditor);
            Short actLevel = Short.parseShort(actLevelEditor.getText().toString());

            EditText spm = (EditText) findViewById(R.id.spmEditor);
            Integer spmVal = Integer.parseInt(spm.getText().toString());

            EditText username = (EditText) findViewById(R.id.usernameEditor);
            String usernameVal = username.getText().toString();

            EditText password = (EditText) findViewById(R.id.passwordEditor);
            String passwordVal = password.getText().toString();

            //Creating user object
            Users user = new Users(SequenceGenerator.getUserUniqueId(), firstName, lastName, emailId, dob, heightVal, weightVal, genderChar, pcode, add, actLevel, spmVal);

            //Creating credential object
            Credential credential = new Credential(usernameVal, passwordVal, new Date());

            postUser.execute(user);
            postCredential.execute(credential);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void goBack(View v)
    {
        Intent intent=new Intent(NewUser.this,MainActivity.class);
        startActivity(intent);
    }

    private class PostUser extends AsyncTask<Users,Void,String>
    {
        @Override
        protected String doInBackground(Users... params)
        {
            RestClient.createUser(params[0]);
            return "User is created";
        }

        @Override
        protected void onPostExecute(String response) {
             message= findViewById(R.id.message);
             message.setText(response);
        }
    }

    private class PostCredential extends AsyncTask<Credential,Void,String>
    {
        @Override
        protected String doInBackground(Credential... params)
        {
            RestClient.createCredential(params[0]);
            return "Credentials are added";
        }

        @Override
        protected void onPostExecute(String response) {
            message= findViewById(R.id.message);
            message.setText(response);
        }
    }
}
