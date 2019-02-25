package com.codepath.claudia.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.claudia.flickster.adapters.MoviesAdapter;
import com.codepath.claudia.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    List<Movie> movies;

//    Add RecyclerView support library to the gradle build file -- DONE
//    Define a model class to use as the data source --  DONE (our movie class)
//    Add a RecyclerView to your activity to display the items -- DONE
//    Create a custom row layout XML file to visualize the item -- DONE
//    Create a RecyclerView.Adapter and ViewHolder to render the item -- DONE
//    Bind the adapter to the data source to populate the RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();
        final MoviesAdapter adapter = new MoviesAdapter(this, movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvMovies.setAdapter(adapter); //set recycler view adapter to our adapter

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(MOVIE_URL, new JsonHttpResponseHandler() {
            //To capture success response from get request
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray movieJsonArray = response.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(movieJsonArray));
                    //noify adapter that data that is suppose to render has changed
                    adapter.notifyDataSetChanged();
                    //can search for this log by the tag smile in logcat to make sure we get the value we expect (d is for debug)
                    Log.d("smile", movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace(); //can see if anything goes wrong in logcat
                }
            }
            //To capture a failure response from the get request
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        Log.d("exit",String.format("%s", movies.size()));
    }

    private String getVideoURL(String movieID) {
        return String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movieID);
    }
}
