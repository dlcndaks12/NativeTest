package com.almond.nativetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SmartToyActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_smart_toy, frameLayout);
    }
}
