package com.example.rateurant;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

public class RestaurantListActivity extends AppCompatActivity {

    private ListView listViewRestaurant;
    private FloatingActionButton addNew;

    public static final String EXTRA_RESTAURANT = "The Restaurant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        wireWidgets();
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantActivity.class);
                startActivity(intent);
            }
        });
        
        listViewRestaurant.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(RestaurantListActivity.this, "long pressed", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateListView();
    }

    private void populateListView() {
        //only get the items that belong to the user
        //get the current user's id  backendless.userservice
        //make data query
        //find all restaurants who ownerID matches the user's objectID
        String ownerId = Backendless.UserService.CurrentUser().getObjectId();
        String whereClause = "ownerId = '" +ownerId+"'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);


        Backendless.Data.of(Restaurant.class).find(queryBuilder, new AsyncCallback<List<Restaurant>>(){
            @Override
            public void handleResponse(final List<Restaurant> restaurantList)
            {
                // first contact instance has been found
                RestaurantAdapter adapter = new RestaurantAdapter(
                        RestaurantListActivity.this,
                        android.R.layout.simple_list_item_1,
                        restaurantList
                );

                listViewRestaurant.setAdapter(adapter);
                //set onItemClickListener to open the restaurant activity
                //
                listViewRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent restaurantDetailIntent = new Intent(RestaurantListActivity.this, RestaurantActivity.class);
                        restaurantDetailIntent.putExtra(EXTRA_RESTAURANT, restaurantList.get(position));
                        startActivity(restaurantDetailIntent);
                    }
                });

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
        addNew = findViewById(R.id.fab_restaurantlist_new);
        listViewRestaurant = findViewById(R.id.listview_restaurantlist);
    }
}
