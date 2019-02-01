package com.example.rateurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RestaurantActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextCuisine;
    private EditText editTextAddress;
    private EditText editTextWebsite;
    private RatingBar ratingBar;
    private SeekBar seekBar;
    private Button saveRestaurant;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        wireWidgets();
        saveRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRestaurantInfo();
            }
        });
        prefillFields();
    }

    private void prefillFields() {
        Intent restaurantIntent = getIntent();
        restaurant = restaurantIntent.getParcelableExtra(RestaurantListActivity.EXTRA_RESTAURANT);
        if(restaurant != null){
            editTextName.setText(restaurant.getName());
            editTextCuisine.setText(restaurant.getCuisine());
            editTextAddress.setText(restaurant.getAddress());
            editTextWebsite.setText(restaurant.getWebsiteLink());
            ratingBar.setRating((float)restaurant.getRating());
            seekBar.setProgress(restaurant.getPrice());
        }
    };

    private boolean allFieldsValid(String name, String cuisine, String address, String website) {
        if (name.length() > 0 && cuisine.length() > 0 && address.length() > 0 && website.length() > 0) {
            return true;
        }
        return false;
    }

    private void saveRestaurantInfo() {
        String name = editTextName.getText().toString();
        String cuisine = editTextCuisine.getText().toString();
        String address = editTextAddress.getText().toString();
        String website = editTextWebsite.getText().toString();
        float rating = ratingBar.getRating();
        int price = seekBar.getProgress()+1;
        if(allFieldsValid(name,cuisine,address,website)){
            if(restaurant != null){
                restaurant.setName(name);
                restaurant.setCuisine(cuisine);
                restaurant.setPrice(price);
                restaurant.setWebsiteLink(website);
                restaurant.setRating(rating);
                restaurant.setAddress(address);
            }
            else{
                restaurant = new Restaurant(name,cuisine,rating,website,address,price);
            }

            Backendless.Persistence.save( restaurant, new AsyncCallback<Restaurant>() {
                public void handleResponse( Restaurant restaurant )
                {
                    finish();
                    // new Contact instance has been saved
                }

                public void handleFault( BackendlessFault fault )
                {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Toast.makeText(RestaurantActivity.this, "NOT SAVED", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void wireWidgets() {
        editTextName = findViewById(R.id.editText_restaurant_name);
        editTextCuisine = findViewById(R.id.editText_restaurant_cuisine);
        editTextAddress = findViewById(R.id.editText_restaurant_address);
        editTextWebsite = findViewById(R.id.editText_restaurant_website);
        ratingBar = findViewById(R.id.ratingBar_restaurant_rating);
        seekBar = findViewById(R.id.seekBar_restaurant_price);
        saveRestaurant = findViewById(R.id.button_restaurant_save);
    }
}
