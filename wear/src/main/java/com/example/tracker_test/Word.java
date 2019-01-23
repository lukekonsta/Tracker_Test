package com.example.tracker_test;

import android.support.v7.app.AppCompatActivity;

public class Word extends AppCompatActivity {

    /** Default definition for the word */
    private String buttonName;


    public Word(String defaultTranslation) {
        buttonName = defaultTranslation;
    }


    public String getDefaultTranslation() {
        return buttonName;
    }

}