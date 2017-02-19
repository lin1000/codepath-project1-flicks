package com.codepath.week1assignment.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.week1assignment.R;
import com.codepath.week1assignment.models.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by lin1000 on 2017/2/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie>{

    static class ViewHolder{
        @BindView(R.id.ivMovieImage) ImageView ivImage;
        @Nullable @BindView(R.id.tvTitle) TextView tvTitle;
        @Nullable @BindView(R.id.tvOverview) TextView tvOverview;
        @Nullable @BindView(R.id.playbutton) ImageView ivPlayButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context,android.R.layout.simple_list_item_1, movies);
    }

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return 2;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        // Return an integer here representing the type of View.
        // Note: Integers must be in the range 0 to getViewTypeCount() - 1
        Movie movie = getItem(position);
        if(movie.getVotedAverage() < 5){
            return 0;
        }else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data item from the position
        Movie movie = getItem(position);

        final ViewHolder viewHolder;

        int orientation = getContext().getResources().getConfiguration().orientation;

        int type = 0;
        // Get the data item type for this position
        type = getItemViewType(position);

        //check the existing view being reused
        if(convertView  == null){

            //LayoutInflater inflater = LayoutInflater.from(getContext());
            //convertView =  inflater.inflate(R.layout.item_movie, parent, false);

            // Inflate XML layout based on the type
            convertView = getInflatedLayoutForType(type);
            System.out.println("convertView="+convertView);
            viewHolder = new ViewHolder(convertView);

                /** ButterKnifed
                //find my image view
                //ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                //cache in viewHolder
                //viewHolder.ivImage = ivImage;
                //clear out images from convertView
                //ivImage.setImageResource(0);

                //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

                //if (tvTitle != null) {
                    //cache in viewHolder
                    //viewHolder.tvTitle = tvTitle;
                //}

                //TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                //if(tvOverview != null){
                //    viewHolder.tvOverview = tvOverview;
                //}**/
            convertView.setTag(viewHolder);

        }else{
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //populate the data.
        if(type==0) {
            viewHolder.tvTitle.setText(movie.getOriginalTitle());
            viewHolder.tvOverview.setText(movie.getOverview());
        }

        //load the playbutton
        viewHolder.ivPlayButton.setVisibility(View.INVISIBLE);
        Picasso.with(getContext()).load(R.drawable.playbutton).into(viewHolder.ivPlayButton);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(type==0)
                Picasso.with(getContext()).load(movie.getPosterPath()).fit().placeholder(R.drawable.loading).error(R.drawable.error).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage,new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Picasso Callback on Success");
                        viewHolder.ivPlayButton.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        System.err.println("Picasso Callback on Error");
                        viewHolder.ivPlayButton.setVisibility(View.INVISIBLE);
                    }
                });
            else if(type==1)
                Picasso.with(getContext()).load(movie.getPosterPath()).resize(1060,500).centerCrop().placeholder(R.drawable.loading).error(R.drawable.error).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Picasso Callback on Success");
                        viewHolder.ivPlayButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        System.err.println("Picasso Callback on Error");
                        viewHolder.ivPlayButton.setVisibility(View.INVISIBLE);
                    }
                });
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(type==0)
                Picasso.with(getContext()).load(movie.getBackdropPath()).fit().centerCrop().placeholder(R.drawable.loading).error(R.drawable.error).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage,new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Picasso Callback on Success");
                        viewHolder.ivPlayButton.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        System.err.println("Picasso Callback on Error");
                        viewHolder.ivPlayButton.setVisibility(View.INVISIBLE);
                    }
                });
            else if (type==1)
                Picasso.with(getContext()).load(movie.getBackdropPath()).resize(1900,500).placeholder(R.drawable.loading).error(R.drawable.error).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage,new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Picasso Callback on Success");
                        viewHolder.ivPlayButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        System.err.println("Picasso Callback on Error");
                        viewHolder.ivPlayButton.setVisibility(View.INVISIBLE);
                    }
                });
        }

        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
        System.out.println("itemViewType="+type);
        if (type == 0) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else if (type == 1) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_type1, null);
        } else {
            return null;
        }
    }
}
