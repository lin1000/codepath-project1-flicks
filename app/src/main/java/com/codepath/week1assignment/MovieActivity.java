package com.codepath.week1assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.week1assignment.adapters.MovieArrayAdapter;
import com.codepath.week1assignment.http.SynchronousHttpCall;
import com.codepath.week1assignment.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.loopj.android.http.AsyncHttpClient.log;


public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    @BindView(R.id.lvMovies) ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        //butterknifed
        //lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this,movies);
        lvItems.setAdapter(movieArrayAdapter);
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        /** Replaced by using OkHttpClient with private Async class.
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray movieJsonResults = null;
                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieArrayAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", movieJsonResults.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });**/

        //AsyncTask : download movie from specified url
        new LoadMovieActivity().execute("https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed");

        setupListViewListener();
    }

    public void launchYoutubeView(String youtubeVideoCode){
        // first parameter is the context, second is the class of the activity to launch
        Intent intent = new Intent(MovieActivity.this, QuickPlayActivity.class);
        // put "extras" into the bundle for access in the second activity
        intent.putExtra("video_code", youtubeVideoCode);
        //startActivity(i); // brings up the second activity
        startActivityForResult(intent,20);
    }

    public void launchMovieDetailView(Movie movie){
        // first parameter is the context, second is the class of the activity to launch
        Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
        // put "extras" into the bundle for access in the second activity
        String originalTitle = movie.getOriginalTitle();
        double votedAverage = movie.getVotedAverage();
        String youtubeVideoCode = movie.getYoutubeVideoCode();
        String backdropPath = movie.getBackdropPath();
        String releaseDate = movie.getReleaseDate();
        double popularity = movie.getPopularity();
        String overview = movie.getOverview();
        String posterPath = movie.getPosterPath();

        intent.putExtra("originalTitle", originalTitle);
        intent.putExtra("votedAverage", votedAverage);
        intent.putExtra("youtubeVideoCode", youtubeVideoCode);
        intent.putExtra("backdropPath", backdropPath);
        intent.putExtra("releaseDate", releaseDate);
        intent.putExtra("popularity", popularity);
        intent.putExtra("overview",overview);
        intent.putExtra("posterPath",posterPath);

        startActivityForResult(intent,20);
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new android.widget.AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                        log.d(this.getClass().getName(),"onItemLongClicked");
                        return true;
                    }
                });

        lvItems.setOnItemClickListener(
                new android.widget.AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id){
                        log.d(this.getClass().getName(),"onItemClicked");
                        log.d(this.getClass().getName(),"pos="+ pos);
                        log.d(this.getClass().getName(),"id="+ id);
                        log.d(this.getClass().getName(),"movieTitle: " + movies.get(pos).getOriginalTitle());
                        log.d(this.getClass().getName(),"movieId: " + movies.get(pos).getId());

                        //AsyncTask : download movie from specified url
                        //new getMovieDetailActivity().execute(String.valueOf(movies.get(pos).getId()));
                        new getMovieDetailActivity().execute(movies.get(pos));
                    }
                });
    }

    private class getMovieDetailActivity extends AsyncTask<Movie, Void, Movie> {

        protected Movie doInBackground(Movie... m) {
            String movieId = String.valueOf(m[0].getId());
            //String movieId = test.getId();
            // Some long-running task like downloading an image.
            //OKHttpClient
            Log.d(this.getClass().getName(),"getYoutubeVideoCode");
            Log.d(this.getClass().getName(),"movieId="+movieId);
            OkHttpClient okClient = SynchronousHttpCall.getInstance().getClient();;
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/"+ movieId +"/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US")
                    .build();

            try {
                Response response = okClient.newCall(request).execute();
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                if(jsonArray.length()>0){
                    String youtubeVideoCode=jsonArray.getJSONObject(0).getString("key");
                    m[0].setYoutubeVideoCode(youtubeVideoCode);
                    Log.d(this.getClass().getName(), "youtubeVideoCode="+ youtubeVideoCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return m[0];
        }

        protected void onPostExecute(Movie movie) {
            // This method is executed in the UIThread
            log.d(this.getClass().getName(),"onPostExecute");
            if ( movie.getVotedAverage() > 5 ) {
                log.d(this.getClass().getName(),"launchYoutubeView");
                launchYoutubeView(movie.getYoutubeVideoCode());
            }else {
                log.d(this.getClass().getName(),"launchMovieDetailView");
                launchMovieDetailView(movie);
            }
        }

    }

    private class LoadMovieActivity extends AsyncTask<String, Void, ArrayList<Movie>> {

        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        protected ArrayList<Movie> doInBackground(String... strings) {
            ArrayList<Movie> movieArrayList;
            // Some long-running task like downloading an image.
            //OKHttpClient
            OkHttpClient okClient = SynchronousHttpCall.getInstance().getClient();;

            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed")
                    .build();

            try {
                Response response = okClient.newCall(request).execute();
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                movieArrayList = Movie.fromJSONArray(jsonArray);
                Log.d(this.getClass().getName(),response.body().string());
                Log.d(this.getClass().getName(),"There are "+ String.valueOf(movieArrayList.size())+ " movie data downloaded");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e){
                e.printStackTrace();
                return null;
            }
            return movieArrayList;
        }

        protected void onProgressUpdate(Void... values) {
            // Executes whenever publishProgress is called from doInBackground
            // Used to update the progress indicator
            //progressBar.setProgress(values[0]);
        }

        protected void onPostExecute(ArrayList<Movie> movie) {
            // This method is executed in the UIThread
            log.d(this.getClass().getName(),"onPostExecute");
            movies.addAll(movie);
            movieArrayAdapter.notifyDataSetChanged();
        }
    }
}
