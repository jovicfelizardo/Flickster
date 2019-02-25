package com.codepath.claudia.flickster.models;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Movie {
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    String voteAverage;
    String review;
    String id;


    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getString("vote_average");
        id = jsonObject.getString("id");

        requestReview();
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public void requestReview() {
        String MOVIE_REVIEW_URL = "https://api.themoviedb.org/3/movie/%s/reviews?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        String url = String.format(MOVIE_REVIEW_URL, this.getMovieId());
        Log.d("hi", this.getMovieId());

        /*MAKE REVIEW REQUEST*/
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            //To capture success response from get request
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("hi", "making request");
                try {
                    JSONArray reviewJsonArray = response.getJSONArray("results");
                    String newReview = "No Reviews Yet";
                    if(reviewJsonArray.length() != 0) {
                        newReview = reviewJsonArray.getJSONObject(0).getString("content");
                    }

                    review = newReview;
                } catch (JSONException e) {
                    e.printStackTrace(); //can see if anything goes wrong in logcat
                }
            }
            //To capture a failure response from the get request
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("hi", "failedd");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() { return voteAverage; }

    public String getMovieId() { return id; }

    public String getReview() { return review; };
}
