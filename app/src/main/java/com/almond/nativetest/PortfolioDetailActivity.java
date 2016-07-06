package com.almond.nativetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almond.nativetest.URLImage.URLImageParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PortfolioDetailActivity extends MainActivity {
    private String heading;
    private String title;
    private String client;
    private String date;
    private String detailImages;
    private TextView viewHeading;
    private TextView viewClient;
    private TextView viewTitle;
    private TextView viewDate;
    private LinearLayout viewDetail;
    private Button btnList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_portfolio_detail, frameLayout);

        mContext = this;

        Intent intent = getIntent();
        heading = intent.getExtras().getString("heading");
        title = intent.getExtras().getString("title");
        client = intent.getExtras().getString("client");
        date = intent.getExtras().getString("date");
        detailImages = intent.getExtras().getString("detailImages");

        viewHeading = (TextView) findViewById(R.id.viewHeading);
        viewHeading.setText(heading);

        viewTitle = (TextView) findViewById(R.id.viewTitle);
        viewTitle.setText(title);

        viewClient = (TextView) findViewById(R.id.viewClient);
        viewClient.setText(client);

        viewDate = (TextView) findViewById(R.id.viewDate);
        viewDate.setText(date);

        viewDetail = (LinearLayout) findViewById(R.id.viewDetail);

        String[] obj = detailImages.split("\\_\\_\\]SEPARATOR\\[\\_\\_");

        for (int i=0; i<obj.length; i++) {
            new GetImages().execute(obj[i]);
        }


        btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class GetImages extends AsyncTask<Object, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Object... params) {

            try {
                HttpURLConnection connection = null;
                Log.e("쓰레드 ============== ", "src =================== " + params[0]);
                String source = "http://coscoi.net" + (String) params[0];
                String path = source.substring(0, source.lastIndexOf('/')+1);
                String fileName = source.substring( source.lastIndexOf('/')+1, source.length() );

                try {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                    fileName = fileName.replaceAll("\\+", "%20");

                    URL url = new URL(path + fileName);

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(connection!=null)connection.disconnect();
                }

            } catch (Exception e) {
                Log.e("error", "Downloading Image Failed");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // TODO Auto-generated method stub
            if (bitmap == null) {
                Log.e("onPostExcute == ", " bitmap is null");
            } else {
                ImageView detailView = new ImageView(mContext);
                FrameLayout.LayoutParams imageParam = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                detailView.setLayoutParams(imageParam);
                detailView.setImageBitmap(bitmap);

                viewDetail.addView(detailView);
            }
        }
    }
}
