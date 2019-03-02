package com.codepath.claudia.flickster.models;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

@Parcel
public class Movie {
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    double voteAverage;
    String review;
    int id;

    // empty constructor needed by the Parceler library
    public Movie() {
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");

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
//        Log.d("hi", (String) this.getMovieId());

        /*MAKE REVIEW REQUEST*/
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            //To capture success response from get request
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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

    public double getVoteAverage() { return voteAverage; }

    public int getMovieId() { return id; }

    public String getReview() { return review; };
}
