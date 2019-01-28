package com.example.rateurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;
import java.util.Map;

public class RestaurantListActivity extends AppCompatActivity {

    private ListView listViewRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        wireWidgets();
        populateListView();
    }

    private void populateListView() {
        Backendless.Data.of(Restaurant.class).find( new AsyncCallback<List<Restaurant>>(){
            @Override
            public void handleResponse(List<Restaurant> restaurantList)
            {
                // first contact instance has been found
                RestaurantAdapter adapter = new RestaurantAdapter(
                        RestaurantListActivity.this,
                        android.R.layout.simple_list_item_1,
                        restaurantList
                );

                listViewRestaurant.setAdapter(adapter);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(RestaurantListActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void wireWidgets() {
        listViewRestaurant = findViewById(R.id.listview_restaurantlist);
    }


}
