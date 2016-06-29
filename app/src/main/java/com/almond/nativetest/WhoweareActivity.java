package com.almond.nativetest;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class WhoweareActivity extends MainActivity {

    private Spinner select;
    private SpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_whoweare, frameLayout);

        select = (Spinner) findViewById(R.id.spinnerSub);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.whoweare, R.layout.spinner_item);
        select.setAdapter(spinnerAdapter);
        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(view, position, "about");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        select.setSelection(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        select.setSelection(0);
    }
}
