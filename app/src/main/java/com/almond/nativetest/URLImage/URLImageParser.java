package com.almond.nativetest.URLImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.almond.nativetest.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by admin on 2016-07-04.
 */
public class URLImageParser implements Html.ImageGetter {
    Context c;
    TextView container;

    public URLImageParser(TextView t, Context c) {
        this.c = c;
        this.container = t;
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = c.getResources().getDrawable(R.drawable.ic_menu_camera);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;

        /*URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask =
                new ImageGetterAsyncTask( urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;*/
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            HttpURLConnection connection = null;
            String source = (String) params[0];

            String path = source.substring(0, source.lastIndexOf('/')+1);
            String fileName = source.substring( source.lastIndexOf('/')+1, source.length() );

            mDrawable = (LevelListDrawable) params[1];
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
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int imgWidth = 0;
                int imgHeight = 0;

                //가로제어(가로기준으로 세로를 맞춤)
                TextView tv = (TextView) container;
                imgWidth = c.getResources().getDisplayMetrics().widthPixels - (tv.getPaddingLeft() + tv.getPaddingRight()); //이미지의 가로를 플랫폼으로 맞추고
                imgHeight = height * (width/imgWidth); //가로에따른 비율계산

                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, imgWidth, imgHeight);
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = container.getText();
                container.setText(t);
            }
        }
    }
/*
    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            urlDrawable.setBounds(0, 0, 0 + c.getResources().getDisplayMetrics().widthPixels, 200);

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(c.getResources().getDisplayMetrics().widthPixels, View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            URLImageParser.this.container.measure(widthMeasureSpec, heightMeasureSpec);
            URLImageParser.this.container.invalidate();
        }

        *//***
         * Get the Drawable from URL
         * @param urlString
         * @return
         *//*
        public Drawable fetchDrawable(String urlString) {
            try {

                InputStream is = fetch(urlString);
                Drawable drawable = Drawable.createFromStream(is, "src");

                drawable.setBounds(0, 0, 0 + c.getResources().getDisplayMetrics().widthPixels, 200);
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }*/
}
