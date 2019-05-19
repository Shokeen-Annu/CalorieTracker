package com.example.myapplication;

import android.graphics.drawable.Drawable;
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
    String googleSnippet = "";
    String usdaFoodId = "";
    String keywordVal = "";
    Food food;
    Food selectedFoodObject;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {

        view = inflator.inflate(R.layout.fragment_daily_diet, container, false);

        // Add food item onClick method
        Button addFoodItem = view.findViewById(R.id.addFood);
        addFoodItem.setOnClickListener(this);

        getActivity().setTitle("Daily Diet");
        // Search button onClick method
        Button searchFoodItem = view.findViewById(R.id.search);
        searchFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validation for keyword input
                EditText keyword = view.findViewById(R.id.keyword);
                keywordVal = keyword.getText().toString();
                if (keywordVal.isEmpty())
                    Toast.makeText(view.getContext(), "Enter a name!", Toast.LENGTH_LONG).show();
                else {
                    // default settings
                    TextView desc = view.findViewById(R.id.description);
                    desc.setText("Loading...");
                    TextView nutrientText = view.findViewById(R.id.nutrients);
                    nutrientText.setVisibility(View.GONE);
                    ImageView searchResult = view.findViewById(R.id.searchResult);
                    searchResult.setVisibility(View.GONE);

                    // Call search APIs
                    new GoogleSearch().execute(keywordVal);
                }
            }
        });

        // Create food item onClick method.
        Button createFoodItem = view.findViewById(R.id.createFoodItem);
        createFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Testing for validation
                if (selectedFoodCategory.equals("Select Category")) {
                    Toast.makeText(view.getContext(), "Please select category.", Toast.LENGTH_LONG).show();
                }
                else if (food == null) {
                    Toast.makeText(view.getContext(), "Please search food item first to add.", Toast.LENGTH_LONG).show();

                }else {

                    //Get max food Id for creating valid food id for new object.
                    food.setCategory(selectedFoodCategory);
                    new GetFoodId().execute();
                }

            }
        });

        //Getting bundle from previous activity and loading food categories.
        bundle = getArguments();
        FetchFoodCategories categories = new FetchFoodCategories();
        categories.execute();

        return view;
    }

    @Override
    public void onClick(View v) {
        new GetConsumptionId().execute();

    }

    private class FetchFoodCategories extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            foodCategoriesList = RestClient.getFoodCategories();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {

            foodCategorySpinner = view.findViewById(R.id.foodCategoryList);

            // Setting adapter for spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, foodCategoriesList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            foodCategorySpinner.setAdapter(adapter);
            foodCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedFoodCategory = (String) parent.getItemAtPosition(position);
                    if (!selectedFoodCategory.equals("Select Category")) {
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

    private class FetchFoodItems extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            foodItemsList = RestClient.getFoodItems(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {

            final HashMap<String, Integer> itemsList = new HashMap<>();
            for (Food food : foodItemsList) {
                itemsList.put(food.getName(), food.getId());
            }
            foodItemsSpinner = view.findViewById(R.id.foodItemsList);
            Set<String> spinnerValues = itemsList.keySet();
            ArrayList<String> foodItems = new ArrayList<>(spinnerValues);

            // Setting adapter for spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, foodItems);
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

    private class GetConsumptionId extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean isSuccess = true;
            try {
                consumptionId = RestClient.getMaxId("getMaxConsumptionId","consumption") + 1;
            } catch (Exception ex) {
                isSuccess = false;
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            Users user = bundle.getParcelable("userObject");
            selectedFoodObject = new Food();

            // Get the selected food item object
            for (Food item : foodItemsList) {
                if (item.getId() == selectedFoodItem) {
                    selectedFoodObject = item;
                    break;
                }
            }
            new TestFoodItemPresence().execute(user.getUserid(),selectedFoodObject.getId());
        }
    }

    private class CreateConsumption extends AsyncTask<Consumption, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Consumption... params) {
            RestClient.createConsumption(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            Toast.makeText(view.getContext(), "Daily diet added successfully!", Toast.LENGTH_LONG).show();
        }
    }

    private class GoogleSearch extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String[] result = new String[3];

            // Call API to get image
            result[0] = SearchGoogleAPI.search(params[0], new String[]{"num", "searchType"}, new String[]{"1", "image"});

            // Call API to get description
            result[1] = SearchGoogleAPI.search(params[0], new String[]{"num"}, new String[]{"1"});

            // Call API to get ndbno to fetch food nutrients
            String keyword = params[0].replace(" ", "+");
            result[2] = UsdaApi.search("search", new String[]{"q", "max"}, new String[]{keyword, "1"});
            return result;
        }

        @Override
        protected void onPostExecute(String[] searchOutput) {
            String image = SearchGoogleAPI.getImage(searchOutput[0]);
            googleSnippet = SearchGoogleAPI.getSnippet(searchOutput[1]);
            usdaFoodId = UsdaApi.getFoodId(searchOutput[2]);
            new LoadImage().execute(image);


        }
    }

    private class LoadImage extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... params) {
            InputStream in = null;
            try {
                in = (InputStream) new URL(params[0]).getContent();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return Drawable.createFromStream(in, "src");

        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            // Set image
            ImageView searchResult = view.findViewById(R.id.searchResult);
            searchResult.setVisibility(View.VISIBLE);
            searchResult.setImageDrawable(drawable);

            // Filter snippet and set description
            TextView description = view.findViewById(R.id.description);
            String formattedSnippet = googleSnippet;
            if (googleSnippet.contains("..."))
                formattedSnippet = googleSnippet.substring(0, googleSnippet.indexOf("...") - 1);
            String finalSnippet = formattedSnippet;
            if (formattedSnippet.contains("."))
                finalSnippet = formattedSnippet.substring(0, formattedSnippet.lastIndexOf('.'));
            finalSnippet = finalSnippet.replace("\n", "");
            description.setText(finalSnippet);

            // Call API for fetching nutrients data
            new FoodNutrients().execute(usdaFoodId);
        }
    }

    private class FoodNutrients extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            return UsdaApi.search("reports", new String[]{"ndbno"}, new String[]{params[0]});

        }

        @Override
        protected void onPostExecute(String searchOutput) {
            // Create food object and set with all attributes like calories, fat,etc.
            food = UsdaApi.getFoodNutrients(searchOutput);
            food.setName(keywordVal);

            //Display nutrients to user
            String nutrients = "Name : " + keywordVal + "\nAmount : " + food.getServingamount() + " " + food.getServingunit() + ", Calories : " + food.getCalorieamount() + ", Fat : " + food.getFat();
            TextView nutrientText = view.findViewById(R.id.nutrients);
            nutrientText.setVisibility(View.VISIBLE);
            nutrientText.setText(nutrients);

        }
    }

    private class GetFoodId extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            return RestClient.getMaxId("getMaxFoodId","food") + 1;

        }

        @Override
        protected void onPostExecute(Integer foodId) {
            try {
                food.setId(foodId);
                new CreateFoodItem().execute(food);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class CreateFoodItem extends AsyncTask<Food, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Food... params) {
            RestClient.createFoodItem(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            Toast.makeText(view.getContext(), "Food item added successfully!", Toast.LENGTH_LONG).show();
            new FetchFoodItems().execute(selectedFoodCategory);
        }
    }

    private class TestFoodItemPresence extends AsyncTask<Integer,Void,Consumption>
    {
        @Override
        protected Consumption doInBackground(Integer... params)
        {
            String today = DateFormat.formatStringToLocalDate(LocalDate.now().toString()).toString();
            return RestClient.isFoodItemAlreadyAddedByUser(params[0],today,params[1]);

        }

        @Override
        protected void onPostExecute(Consumption consumption) {
            Boolean isSuccess = true;

            try {
                // Test for valid quantity
                EditText quantity = view.findViewById(R.id.foodQuantity);
                String quantityString = quantity.getText().toString();

                int quantityVal = 0;
                if (!quantityString.isEmpty())
                    quantityVal = Integer.parseInt(quantityString);

                if (consumption == null) {

                    // Test for valid user entries
                    if (quantityVal <= 0 || selectedFoodItem == -1 || selectedFoodCategory.equals("Select Category")) {
                        isSuccess = false;
                    }
                    if (isSuccess) {
                        Users user = bundle.getParcelable("userObject");

                        // Create consumption object and call post to save it.
                        Consumption consumptionNew = new Consumption(consumptionId, new Date(), quantityVal, user, selectedFoodObject);

                        new CreateConsumption().execute(consumptionNew);
                    } else {
                        Toast.makeText(view.getContext(), "Please enter values properly.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    int quantityOld = consumption.getQuantity();
                    quantityOld += quantityVal;
                    consumption.setQuantity(quantityOld);
                    new UpdateConsumption().execute(consumption);
                }
            }
                catch(Exception ex){

                    ex.printStackTrace();
                    Toast.makeText(view.getContext(), "Some error occurred. Please enter values properly.", Toast.LENGTH_LONG).show();
                }

            }

    }
    private class UpdateConsumption extends AsyncTask<Consumption,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Consumption... consumption)
        {
            return RestClient.updateConsumption(consumption[0]);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess)
        {
            if(isSuccess)
                Toast.makeText(view.getContext(), "Daily diet updated successfully!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(view.getContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
        }
    }
}

