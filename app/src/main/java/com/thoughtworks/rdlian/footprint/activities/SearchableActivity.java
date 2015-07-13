package com.thoughtworks.rdlian.footprint.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.thoughtworks.rdlian.footprint.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rdlian on 7/7/15.
 */
public class SearchableActivity extends AppCompatActivity {

    @InjectView(R.id.searchList)
    ListView searchList;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.search_map)
    MapView mapView;
    private AMap aMap;
    private SearchView searchView;
    private UiSettings uiSettings;

    private View.OnClickListener navigationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        initView(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String locationInfo = intent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
            searchView.clearFocus();
            String [] locationStrings = locationInfo.split("\n");
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.BLUE);
            LatLng currentLatLng = null;
            LatLng newLatLng = null;
            for (String locationString : locationStrings) {
                String [] latAndLonStrings = locationString.split(" ");
                newLatLng = new LatLng(Double.parseDouble(latAndLonStrings[1]), Double.parseDouble(latAndLonStrings[3]));
                if (null != currentLatLng) {
                    polylineOptions.add(currentLatLng, newLatLng);
                }
                currentLatLng = newLatLng;
            }
            aMap.addPolyline(polylineOptions);
            mapView.setVisibility(View.VISIBLE);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_serachable, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchableActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchableActivity.this.getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView(Bundle savedInstanceState) {
        ButterKnife.inject(this);
        initAmapComponent(savedInstanceState);

        final List<String> cityNames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cityNames.add("123");
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cityNames);
        searchList.setAdapter(adapter);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                      int position, long id) {
                Log.v("nice", "itemSelected");
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cityNames.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.footprint);
        toolbar.setNavigationOnClickListener(navigationOnClickListener);
    }

    private void initAmapComponent(Bundle savedInstanceState) {
        if (aMap == null) {
            mapView.onCreate(savedInstanceState);
            aMap = mapView.getMap();
            uiSettings = aMap.getUiSettings();
            uiSettings.setCompassEnabled(true);//show compass
            uiSettings.setScaleControlsEnabled(true);//comparing rule
            uiSettings.setMyLocationButtonEnabled(true);// myLocation button
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));//zoom to size 15
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search_main:
                Toast.makeText(SearchableActivity.this, "nice", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doMySearch(String query) {
        Toast.makeText(SearchableActivity.this, query, Toast.LENGTH_LONG).show();
    }
}
