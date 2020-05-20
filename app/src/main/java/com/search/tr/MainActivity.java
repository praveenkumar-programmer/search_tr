package com.search.tr;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Movie> movieList;
    private MoviesAdapter mAdapter;
    private SearchView searchView;
    private ProgressDialog loadingDialog;
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private MovieDetailsParser movieDetailsParser;

    // url to fetch movies json
    private static final String URL = "https://harishwarrior.github.io/JsonHosting/movies.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = getSharedPreferences("currentItem",0);
        spe = sp.edit();

        // toolbar fancy stuff
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        mAdapter = new MoviesAdapter(movieList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        if(savedInstanceState == null){

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
            recyclerView.setAdapter(mAdapter);

            loadingDialog = new ProgressDialog(MainActivity.this);
            loadingDialog.setMessage("This may take few seconds..");
            loadingDialog.setTitle("Loading...");
            loadingDialog.setIndeterminate(false);
            loadingDialog.setCancelable(true);
            loadingDialog.show();

            fetchMovies();

        }

    }

    private void fetchMovies() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch movies! Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Movie> items = new Gson().fromJson(response.toString(), new TypeToken<List<Movie>>() {
                        }.getType());

                        // adding movies to movies list
                        movieList.clear();
                        movieList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {

        movieDetailsParser = new MovieDetailsParser(movie.getName());

        spe.putString("title", movie.getNormalized_name());
        spe.putString("name", movie.getName());
        spe.putString("thumbnailurl", movie.getThumbNailUrl());
        spe.putInt("noofmagnets", movie.getMagnets().size());

        spe.putString("year", movieDetailsParser.getYear());
        spe.putString("size", movieDetailsParser.getSize());
        spe.putString("quality", movieDetailsParser.getQuality());
        spe.putString("languages", movieDetailsParser.getLanguages());

        for(int i = 0; i < movie.getMagnets().size(); i++)
            spe.putString("magnet"+i, movie.getMagnets().get(i));
        spe.commit();

        startActivity(new Intent(this, ViewMovieDetails.class));
    }

}
