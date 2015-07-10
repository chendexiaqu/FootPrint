package com.thoughtworks.rdlian.footprint;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.offlinemap.OfflineMapCity;

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

    private List<OfflineMapCity> cities = new ArrayList<>();

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
        ButterKnife.inject(this);
        init();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_serachable, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchableActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchableActivity.this.getComponentName()));
        }
//        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        final List<String> cityNames = new ArrayList<>();
//        for (OfflineMapCity city : cities) {
            //cityNames.add(city.getCity());
//        }
        for (int i = 0; i < 10; i++) {
            cityNames.add("");
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cityNames);
        searchList.setAdapter(adapter);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
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
