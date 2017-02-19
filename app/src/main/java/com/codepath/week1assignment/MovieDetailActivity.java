package com.codepath.week1assignment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.loopj.android.http.AsyncHttpClient.log;


/**
 * Created by lin1000 on 2017/2/19.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.ivMovieImage) ImageView ivImge;
    @Nullable @BindView(R.id.tvTitle) TextView tvTitle;
    @Nullable @BindView(R.id.releaseDate) TextView tvReleaseDate;
    @Nullable @BindView(R.id.tvOverview) TextView tvOverview;
    @Nullable @BindView(R.id.playbutton) ImageView ivPlayButton;
    @Nullable @BindView(R.id.ratingBar) RatingBar rbRatingBar;
    @Nullable @BindView(R.id.popularity) TextView tvPopularity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        final String originalTitle = getIntent().getStringExtra("originalTitle");
        final double votedAverage = getIntent().getDoubleExtra("votedAverage",0.0);
        final String youtubeVideoCode = getIntent().getStringExtra("youtubeVideoCode");
        final String backdropPath = getIntent().getStringExtra("backdropPath");
        final String releaseDate = getIntent().getStringExtra("releaseDate");
        final double popularity = getIntent().getDoubleExtra("popularity",0.0);
        final String overview = getIntent().getStringExtra("overview");
        final String posterPath = getIntent().getStringExtra("posterPath");

        log.d(this.getClass().getName(), "originalTitle="+originalTitle);
        log.d(this.getClass().getName(), "votedAverage="+votedAverage);
        log.d(this.getClass().getName(), "youtubeVideoCode="+youtubeVideoCode);
        log.d(this.getClass().getName(), "backdropPath="+backdropPath);
        log.d(this.getClass().getName(), "releaseDate="+releaseDate);
        log.d(this.getClass().getName(), "popularity="+popularity);
        log.d(this.getClass().getName(), "overview="+overview);
        log.d(this.getClass().getName(), "posterPath="+posterPath);
        log.d(this.getClass().getName(), "rbRatingBar.getNumStars()="+rbRatingBar.getNumStars());
        log.d(this.getClass().getName(), "normalizeRatingBar(votedAverage)="+normalizeRatingBar(votedAverage));

        //load the playbutton
        ivPlayButton.setVisibility(View.INVISIBLE);
        Picasso.with(this).load(R.drawable.playbutton).resize(80,80).into(ivPlayButton);

        //load the movie details
        Picasso.with(this).load(posterPath).fit().centerCrop().placeholder(R.drawable.loading).error(R.drawable.error).transform(new RoundedCornersTransformation(10, 10)).into(ivImge,new Callback() {
            @Override
            public void onSuccess() {
                Log.d(this.getClass().getName(),"Picasso Callback on Success");
                ivPlayButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                Log.d(this.getClass().getName(),"Picasso Callback on Error");
                ivPlayButton.setVisibility(View.INVISIBLE);
            }
        });



        rbRatingBar.setRating(normalizeRatingBar(votedAverage));
        tvTitle.setText(originalTitle);
        tvReleaseDate.setText("release date : " + releaseDate);
        tvOverview.setText(overview);
        tvPopularity.setText("Popularity: "+ popularity);
    }

    private float normalizeRatingBar(double votedAverage){
        return (float)(((float)votedAverage)/10.0)*5.0f;

    }
}
