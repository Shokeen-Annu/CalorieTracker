package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DailyDietFragment extends Fragment implements View.OnClickListener {


    List<String> foodCategoriesList;
    String selectedFoodCategory;
    Spinner foodCategorySpinner;
    Food[] foodItemsList;
    Integer selectedFoodItem;
    Spinner foodItemsSpinner;
    Integer consumptionId;
    View view;
    Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {

        view = inflator.inflate(R.layout.fragment_daily_diet,container,false);

        Button addFoodItem = view.findViewById(R.id.addFood);
        addFoodItem.setOnClickListener(this);

        Button searchFoodItem = view.findViewById(R.id.search);
        searchFoodItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                EditText keyword = view.findViewById(R.id.keyword);
                String keywordVal = keyword.getText().toString();
                if(keywordVal.isEmpty())
                    Toast.makeText(view.getContext(),"Enter a name!",Toast.LENGTH_LONG).show();
                new GoogleSearch().execute(keywordVal);
            }
        });
        bundle = getArguments();
        FetchFoodCategories categories = new FetchFoodCategories();
        categories.execute();

        return view;
    }

    @Override
    public void onClick(View v)
    {
        new GetConsumptionId().execute();

    }
    private class FetchFoodCategories extends AsyncTask<Void,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {

            foodCategoriesList = RestClient.getFoodCategories();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess)
        {

            foodCategorySpinner = view.findViewById(R.id.foodCategoryList);
            ArrayAdapter<String> adapter=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item,foodCategoriesList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            foodCategorySpinner.setAdapter(adapter);
            foodCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedFoodCategory = (String) parent.getItemAtPosition(position);
                    if(!selectedFoodCategory.equals("Select Category")) {
                        FetchFoodItems foodItem = new FetchFoodItems();
                        foodItem.execute(selectedFoodCategory);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
    private class FetchFoodItems extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(String... params)
        {
           foodItemsList= RestClient.getFoodItems(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess)
        {
            final HashMap<String,Integer> itemsList = new HashMap<>();
            for(Food food : foodItemsList)
            {
                itemsList.put(food.getName(),food.getId());
            }
            foodItemsSpinner = view.findViewById(R.id.foodItemsList);
            Set<String> spinnerValues = itemsList.keySet();
            ArrayList<String> foodItems = new ArrayList<>(spinnerValues);
            ArrayAdapter<String> adapter=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item,foodItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            foodItemsSpinner.setAdapter(adapter);
            foodItemsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedFoodItem = itemsList.get(parent.getItemAtPosition(position));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    private class GetConsumptionId extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean isSuccess = true;
            try {
                consumptionId = RestClient.getMaxConsumptionId() + 1;
            } catch (Exception ex) {
                isSuccess = false;
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            try {
                EditText quantity = view.findViewById(R.id.foodQuantity);
                String quantityString = quantity.getText().toString();
                int quantityVal = 0;
                if (!quantityString.isEmpty())
                    quantityVal = Integer.parseInt(quantityString);
                if (quantityVal <= 0 || selectedFoodItem == -1 || selectedFoodCategory.equals("Select Category")) {
                    isSuccess = false;

                }
                if (isSuccess) {
                    Users user = bundle.getParcelable("userObject");
                    Food food = new Food();
                    for (Food item : foodItemsList) {
                        if (item.getId() == selectedFoodItem) {
                            food = item;
                            break;
                        }
                    }
                    Consumption consumption = new Consumption(consumptionId, new Date(), quantityVal, user, food);
                    new CreateConsumption().execute(consumption);
                } else {
                    Toast.makeText(view.getContext(), "Please enter values properly.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(view.getContext(), "Some error occurred. Please enter values properly.", Toast.LENGTH_LONG).show();
            }
        }
    }
        private class CreateConsumption extends AsyncTask<Consumption,Void,Boolean>
        {
            @Override
            protected Boolean doInBackground(Consumption... params)
            {
                RestClient.createConsumption(params[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess)
            {
                Toast.makeText(view.getContext(),"Daily diet added successfully!",Toast.LENGTH_LONG).show();
            }
        }
    private class GoogleSearch extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            return SearchGoogleAPI.search(params[0],new String[]{"num","searchType"},new String[]{"1","image"});

        }

        @Override
        protected void onPostExecute(String searchOutput)
        {
            String image = SearchGoogleAPI.getImage(searchOutput);
            new LoadImage().execute(image);
                //Bitmap bitmap = BitmapFactory.decodeStream(new java.net.URL(image).openStream());


        }
    }
    private class LoadImage extends AsyncTask<String,Void,Drawable>
    {
        @Override
        protected Drawable doInBackground(String... params)
        {
            InputStream in=null;
            try {
                in = (InputStream) new URL(params[0]).getContent();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            return Drawable.createFromStream(in, "src");

        }

        @Override
        protected void onPostExecute(Drawable drawable)
        {
            ImageView searchResult = view.findViewById(R.id.searchResult);
            searchResult.setImageDrawable(drawable);
        }
    }

    }

