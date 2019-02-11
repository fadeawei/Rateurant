package com.example.rateurant;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import java.util.List;

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

        //allowing context menu_delete to work
        registerForContextMenu(listViewRestaurant);
    }

    //populating the view in the onStart lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        populateListView();
    }

    //context menu_delete stuff
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
    }
    //context menu_delete stuff
    public boolean onContextItemSelected(MenuItem item) {
        //find out which menu_delete item was pressed
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.option1:
                Restaurant restaurant = (Restaurant) listViewRestaurant.getItemAtPosition(info.position);
                deleteRestaurant(restaurant);
                return true;
            default:
                return false;
        }
    }

    //options menu logout stuff
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    //options menu logout stuff
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //logout from backendless
    private void logout() {
        Backendless.UserService.logout( new AsyncCallback<Void>()
        {
            public void handleResponse( Void response )
            {
                // user has been logged out.
                Intent intent = new Intent(RestaurantListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            public void handleFault( BackendlessFault fault )
            {
                // something went wrong and logout failed, to get the error code call fault.getCode()
                Toast.makeText(RestaurantListActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //delete restaurant from backendless
    private void deleteRestaurant(Restaurant restaurant) {
        Backendless.Persistence.of(Restaurant.class ).remove(restaurant, new AsyncCallback<Long>()
                {
                    public void handleResponse( Long response )
                    {
                        // Contact has been deleted. The response is the
                        // time in milliseconds when the object was deleted
                        populateListView();
                    }
                    public void handleFault( BackendlessFault fault )
                    {
                        // an error has occurred, the error code can be
                        // retrieved with fault.getCode()
                        Toast.makeText(RestaurantListActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } );
    }


    //populating the listview
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


    //wirewidgets
    private void wireWidgets() {
        addNew = findViewById(R.id.fab_restaurantlist_new);
        listViewRestaurant = findViewById(R.id.listview_restaurantlist);
    }
}
