package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class NewUser extends AppCompatActivity {

    DatePickerDialog datePicker;
    Boolean isUniqueEmail = false;
    Boolean isUniqueUsername = false;
    Users user = null;
    Credential credential = null;
    Context context;
    private static final String emailRegex = "^(.+)@(.+)$";

    private static final String passwordRegex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        context = getApplicationContext();
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
                        LocalDate dateOfBirth = Year.of(year).atMonth(month+1).atDay(dayOfMonth);
                        dobEditor.setText(dateOfBirth.toString());
                    }
                },year,month,day);
                datePicker.show();
            }
        });

        //Spinner on activity level
        final EditText actLevelEditor = findViewById(R.id.activityLevelEditor);
        final Spinner actLevelSpinner = findViewById(R.id.activityLevelSpinner);
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
        try {
            //Fetch and validate input data
            EditText fName = findViewById(R.id.fNameEditor);
            String firstName = fName.getText().toString();

            if(firstName.isEmpty()) {
                Toast.makeText(context,"First name is empty",Toast.LENGTH_LONG).show();
                return;
            }

            EditText lName =  findViewById(R.id.lNameEditor);
            String lastName = lName.getText().toString();

            if(lastName.isEmpty()) {
                Toast.makeText(context,"Last name is empty",Toast.LENGTH_LONG).show();
                return;
            }
            EditText email =  findViewById(R.id.emailEditor);
            String emailId = email.getText().toString();

            if(emailId.isEmpty()) {
                Toast.makeText(context,"Email is empty",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!validateEmailId(emailId))
            {
                Toast.makeText(context,"Email is invalid.",Toast.LENGTH_LONG).show();
                return;
            }
            emailId = emailId.toLowerCase();
            EditText dobEditor = findViewById(R.id.dobEditor);
            if(dobEditor.getText().toString().isEmpty()) {
                Toast.makeText(context,"Dob is empty",Toast.LENGTH_LONG).show();
                return;
            }
            Date dob = DateFormat.formatDateInShortFormat(dobEditor.getText().toString());


            EditText height =  findViewById(R.id.heightEditor);
            String hVal = height.getText().toString();
            if(hVal.isEmpty())
            {
                Toast.makeText(context,"Height is empty",Toast.LENGTH_LONG).show();
                return;
            }
            BigDecimal heightVal = new BigDecimal(hVal);
            EditText weight =  findViewById(R.id.weightEditor);
            BigDecimal weightVal = new BigDecimal(weight.getText().toString());

            RadioGroup gender =  findViewById(R.id.genderEditor);
            int genderSelectedId = gender.getCheckedRadioButtonId();

            if(genderSelectedId == -1) {
                Toast.makeText(context,"Gender is empty",Toast.LENGTH_LONG).show();
                return;
            }
            RadioButton genderId = findViewById(genderSelectedId);
            char genderChar = 'M';
            if (genderId.getText().toString().equals("Female"))
                genderChar = 'F';

            EditText address = findViewById(R.id.addEditor);
            String add = address.getText().toString();

            if(add.isEmpty()) {
                Toast.makeText(context,"Address is empty",Toast.LENGTH_LONG).show();
                return;
            }
            EditText postcode =  findViewById(R.id.postcodeEditor);
            String pcode = postcode.getText().toString();

            if(pcode.isEmpty()) {
                Toast.makeText(context,"Postcode is empty",Toast.LENGTH_LONG).show();
                return;
            }
            else if(pcode.length() != 4)
            {
                Toast.makeText(context,"Postcode should be 4 characters",Toast.LENGTH_LONG).show();
                return;
            }
            EditText actLevelEditor = findViewById(R.id.activityLevelEditor);
            Short actLevel = Short.parseShort(actLevelEditor.getText().toString());

            EditText spm =  findViewById(R.id.spmEditor);


            if(spm.getText().toString().isEmpty()) {
                Toast.makeText(context,"Steps per mile is empty",Toast.LENGTH_LONG).show();
                return;
            }
            Integer spmVal = Integer.parseInt(spm.getText().toString());
            EditText username =  findViewById(R.id.usernameEditor);
            String usernameVal = username.getText().toString();

            if(usernameVal.isEmpty()) {
                Toast.makeText(context,"UserName is empty",Toast.LENGTH_LONG).show();
                return;
            }
            EditText password =  findViewById(R.id.passwordEditor);
            String passwordVal = password.getText().toString();

            if(passwordVal.isEmpty()) {
                Toast.makeText(context,"Password is empty",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!validatePassword(passwordVal))
            {
                Toast.makeText(context,"Password should contain one special character, one digit,one lower and upper case letter and its length should be at least 8 characters",Toast.LENGTH_LONG).show();
                return;
            }
            EditText pswdConfirm = findViewById(R.id.passwordConfirm);
            String pswdConfirmVal = pswdConfirm.getText().toString();

            if(pswdConfirmVal.isEmpty()) {
                Toast.makeText(context,"Re-password is empty",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!validatePassword(pswdConfirmVal))
            {
                Toast.makeText(context,"Re password should contain one special character, one digit,one lower and upper case letter and its length should be at least 8 characters",Toast.LENGTH_LONG).show();
                return;
            }

            if(!pswdConfirmVal.equals(passwordVal))
                Toast.makeText(getApplicationContext(),"Passwords do not match!",Toast.LENGTH_LONG).show();
            else {
                String hashedPassword = HashGenerator.hashCodeGenerator(passwordVal);
                //Creating user object
                user = new Users(0, firstName, lastName, emailId, dob, heightVal, weightVal, genderChar, pcode, add, actLevel, spmVal);

                //Creating credential object
                credential = new Credential(usernameVal, hashedPassword, new Date());

                new GetMaxIdAndValidate().execute(emailId, usernameVal);
            }
        }
        catch (ParseException ex)
        {
            Toast.makeText(context,"Date is invalid.",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        catch (NumberFormatException ex)
        {
            Toast.makeText(context,"Height, steps per mile or weight is invalid.",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        catch(Exception ex)
        {
            Toast.makeText(context,"Values are invalid.",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private class PostUser extends AsyncTask<Users,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Users... params)
        {
            try {
                RestClient.createUser(params[0]);
            }catch (Exception ex) {
                return false;
            }
            return  true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if(isSuccess) {
                if(credential!=null) {
                    credential.setUserid(user);
                    new PostCredential().execute(credential);
                }
            }
            else
                Toast.makeText(getApplicationContext(),"User not created!",Toast.LENGTH_LONG).show();
        }
    }

    private class PostCredential extends AsyncTask<Credential,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Credential... params)
        {
            try {
                RestClient.createCredential(params[0]);
            }catch(Exception ex)
            {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {

            if(isSuccess) {
                Toast.makeText(getApplicationContext(),"User added successfully!",Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(),"Credential not created.",Toast.LENGTH_LONG).show();
        }
    }
    private class GetMaxIdAndValidate extends AsyncTask<String,Void,Integer>
    {
        @Override
        protected Integer doInBackground(String... params)
        {
            isUniqueEmail = RestClient.isEmailUnique(params[0]);
            Credential cred = RestClient.findCredential(params[1]);
            isUniqueUsername = cred == null ? true : false;
            return RestClient.getMaxId("getMaxUserId","users");
        }

        @Override
        protected void onPostExecute(Integer maxId) {
            if(isUniqueEmail && isUniqueUsername)
            {
                if(user != null)
                user.setUserid(maxId + 1);
                new PostUser().execute(user);
            }
            else if(!isUniqueUsername)
                Toast.makeText(getApplicationContext(),"Username is already used.",Toast.LENGTH_LONG).show();
            else if(!isUniqueEmail)
                Toast.makeText(getApplicationContext(),"Email is already used.",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Invalid data!",Toast.LENGTH_LONG).show();
        }
    }

    public Boolean validateEmailId(String emailId)
    {
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(emailId).matches();
    }

    public Boolean validatePassword(String pswd)
    {
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(pswd).matches();
    }
}
