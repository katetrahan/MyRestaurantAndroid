package com.epicodus.myrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;

import static com.epicodus.myrestaurants.R.id.listView;


public class RestaurantsActivity extends AppCompatActivity {
    public static final String TAG = RestaurantsActivity.class.getSimpleName();


    @BindView(R.id.locationTextView) TextView mLocationTextView;
    @BindView(R.id.listView) ListView mListView;

    public ArrayList<Restaurant> restaurants = new ArrayList<>();


//    private String[] restaurants = new String[] {"Sweet Hereafter", "Cricket", "Hawthorne Fish House", "Viking Soul Food",
//            "Red Square", "Horse Brass", "Dick's Kitchen", "Taco Bell", "Me Kha Noodle Bar",
//            "La Bonita Taqueria", "Smokehouse Tavern", "Pembiche", "Kay's Bar", "Gnarly Grey", "Slappy Cakes", "Mi Mero Mole" };
//
//    private String[] cuisines = new String[] {"Vegan Food", "Breakfast", "Fishs Dishs", "Scandinavian", "Coffee", "English Food", "Burgers", "Fast Food", "Noodle Soups", "Mexican", "BBQ", "Cuban", "Bar Food", "Sports Bar", "Breakfast", "Mexican" };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, restaurants); //must match constructor!
//        mListView.setAdapter(adapter);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String restaurant = ((TextView)view).getText().toString();
//                Toast.makeText(RestaurantsActivity.this, restaurant, Toast.LENGTH_LONG).show();
////                Log.v(TAG, "In the onItemClickListener!");
//            }
//        });

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        mLocationTextView.setText("Here are all the restaurants near:"+location);
//        Log.d(TAG, "In the onCreate method!");
        getRestaurants(location);


    }

    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                restaurants = yelpService.processResults(response);

                RestaurantsActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        String[] restaurantNames = new String[restaurants.size()];
                        for (int i = 0; i < restaurantNames.length; i++) {
                            restaurantNames[i] = restaurants.get(i).getName();
                        }

                        ArrayAdapter adapter = new ArrayAdapter(RestaurantsActivity.this,
                                android.R.layout.simple_list_item_1, restaurantNames);
                        mListView.setAdapter(adapter);

                        for (Restaurant restaurant : restaurants) {
                            Log.d(TAG, "Name: " + restaurant.getName());
                            Log.d(TAG, "Phone: " + restaurant.getPhone());
                            Log.d(TAG, "Website: " + restaurant.getWebsite());
                            Log.d(TAG, "Image url: " + restaurant.getImageUrl());
                            Log.d(TAG, "Rating: " + Double.toString(restaurant.getRating()));
                            Log.d(TAG, "Address: " + android.text.TextUtils.join(", ", restaurant.getAddress()));
                            Log.d(TAG, "Categories: " + restaurant.getCategories().toString());
                        }

                    }

                });
            }

        });
    }
}