package com.example.mviarchitecture.model;

// control state
public interface PartialMainState {
    final class Loading implements PartialMainState{

    }

    class GotImageLink implements PartialMainState{
        public String imageLink;

        public GotImageLink(String imageLink) {
            this.imageLink = imageLink;
        }
    }
    final class Error implements PartialMainState{
        public Throwable error;

        public Error(Throwable error){
            this.error = error;
        }
    }

}
