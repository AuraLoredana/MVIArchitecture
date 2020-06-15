package com.example.mviarchitecture.view;

public class MainViewState {
    boolean isLoading;
    boolean isImageViewShow;
    String imageLink;
    Throwable error;

    MainViewState(boolean isLoading, boolean isImageViewShow, String imageLink, Throwable error) {
        this.isLoading = isLoading;
        this.isImageViewShow = isImageViewShow;
        this.imageLink = imageLink;
        this.error = error;
    }
}
