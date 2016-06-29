package com.almond.nativetest;

import android.graphics.drawable.DrawableContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class PrintingActivity extends MainActivity {
    private Spinner select;
    private SpinnerAdapter spinnerAdapter;
    private ImageView printingContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_printing, frameLayout);

        printingContent = (ImageView) findViewById(R.id.printingContent);
        select = (Spinner) findViewById(R.id.spinnerSub);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.printing, R.layout.spinner_item);
        select.setAdapter(spinnerAdapter);
        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: printingContent.setImageResource(R.drawable.printing_content1);
                        break;
                    case 1: printingContent.setImageResource(R.drawable.printing_content2);
                        break;
                    case 2: printingContent.setImageResource(R.drawable.printing_content3);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
