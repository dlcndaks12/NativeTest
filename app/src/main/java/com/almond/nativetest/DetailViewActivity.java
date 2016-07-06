package com.almond.nativetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.almond.nativetest.URLImage.URLImageParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailViewActivity extends MainActivity {
    private String heading;
    private String type;
    private String title;
    private String date;
    private String contents;
    private TextView viewHeading;
    private TextView viewType;
    private TextView viewTitle;
    private TextView viewDate;
    private TextView viewArticle;
    private Button btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_detail_view, frameLayout);

        Intent intent = getIntent();
        heading = intent.getExtras().getString("heading");
        type = intent.getExtras().getString("type");
        title = intent.getExtras().getString("title");
        date = intent.getExtras().getString("date");
        contents = intent.getExtras().getString("contents");

        viewHeading = (TextView) findViewById(R.id.viewHeading);
        viewHeading.setText(heading);

        viewType = (TextView) findViewById(R.id.viewType);
        viewType.setText(type);

        viewTitle = (TextView) findViewById(R.id.viewTitle);
        viewTitle.setText(title);

        viewDate = (TextView) findViewById(R.id.viewDate);
        viewDate.setText(date);

        viewArticle = (TextView) findViewById(R.id.viewArticle);

        URLImageParser p = new URLImageParser(viewArticle, this);
        Spanned htmlSpan = Html.fromHtml(contents, p, null);

        if (getImgSrc(contents).size() > 0) {
            Log.e("src =========  : ", getImgSrc(contents).get(0));
        }
        viewArticle.setText(htmlSpan);

        btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static List<String> getImgSrc(String str) {
        Pattern nonValidPattern = Pattern
                .compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");

        List<String> result = new ArrayList<String>();
        Matcher matcher = nonValidPattern.matcher(str);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }
}
