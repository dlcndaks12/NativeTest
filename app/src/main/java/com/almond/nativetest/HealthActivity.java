package com.almond.nativetest;

import android.os.Bundle;

public class HealthActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_health, frameLayout);
    }
}
