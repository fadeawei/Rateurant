package com.example.rateurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RestaurantActivity extends AppCompatActivity {

    private String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        wireWidgets();


    }

    private void wireWidgets() {
        EditText name = findViewById(R.id.editText_restaurant_name);
        EditText cuisine = findViewById(R.id.editText_restaurant_cuisine);
        EditText address = findViewById(R.id.editText_restaurant_address);
        EditText website = findViewById(R.id.editText_restaurant_website);
        RatingBar ratingBar = findViewById(R.id.ratingBar_restaurant_rating);
        SeekBar seekBar = findViewById(R.id.seekBar_restaurant_price);
    }
}
