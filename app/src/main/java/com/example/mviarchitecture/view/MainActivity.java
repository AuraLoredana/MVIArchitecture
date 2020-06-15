package com.example.mviarchitecture.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mviarchitecture.R;
import com.example.mviarchitecture.model.MainView;
import com.example.mviarchitecture.utils.DataSource;
import com.hannesdorfmann.mosby3.mvi.MviActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;

public class MainActivity extends MviActivity<MainView, MainPresenter> implements MainView {

    ImageView imageView;
    Button button;
    ProgressBar progressBar;
    List<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn_get_data);
        progressBar = findViewById(R.id.progress_bar);
        imageView = findViewById(R.id.recycler_data);
        imageList = createListOfImages();
    }

    private List<String> createListOfImages() {
        return Arrays.asList
                ("https://images.unsplash.com/photo-1464982326199-86f32f81b211?ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80","https://images" +
                        ".unsplash.com/photo-1587272343929-bbac70b15c31?ixlib=rb-1.2.1&auto=format&fit=crop&w=2767&q=80","https://hips.hearstapps" +
                        ".com/hmg-prod.s3.amazonaws.com/images/close-up-of-tulips-blooming-in-field-royalty-free-image-1584131603.jpg","https://images.pexels.com/photos/462118/pexels-photo-462118.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
    }
    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(new DataSource(imageList));
    }


    @Override
    public Observable<Integer> getImageIntent() {
        return RxView.clicks(button)
                .map(click -> getRandomNumberInRange(0, imageList.size()-1));
    }

    private Integer getRandomNumberInRange(int min, int max) throws IllegalAccessError{
        if (min >= max) try {
            throw new IllegalAccessException("max must not be bigger then min");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



    @Override
    public void render(MainViewState viewState) {
        // process changed state to show view
        if (viewState.isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            button.setEnabled(false);
        } else if (viewState.error != null) {
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            button.setEnabled(true);
            Toast.makeText(this, viewState.error.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (viewState.isImageViewShow) {
            imageView.setVisibility(View.VISIBLE);
            button.setEnabled(true);

            // load image
            Picasso.get().load(viewState.imageLink).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setAlpha(0f);
                    Picasso.get().load(viewState.imageLink).into(imageView);
                    // fade animation
                    imageView.animate().setDuration(300).alpha(1f).start();
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onError(Exception e) {
                    Log.d("Error", "This is the error", e);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
