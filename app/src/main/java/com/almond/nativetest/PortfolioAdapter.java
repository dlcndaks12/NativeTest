package com.almond.nativetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.almond.nativetest.callback.AsyncCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by admin on 2016-06-29.
 */
public class PortfolioAdapter extends BaseAdapter {

    private ArrayList<PortfolioViewItem> portfolioItemList = new ArrayList<PortfolioViewItem>() ;

    public PortfolioAdapter() { }

    @Override
    public int getCount() {
        return portfolioItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return portfolioItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageView imageView;
        String imageURL;
        Bitmap bitmap;
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
        private AsyncCallback setImageViewCallback;

        public DownloadAsyncTask() {

        }

        public DownloadAsyncTask(AsyncCallback setImageViewCallback) {
            this.setImageViewCallback = setImageViewCallback;
        }

        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            // TODO Auto-generated method stub
            //load image directly
            ViewHolder viewHolder = params[0];
            try {

                HttpURLConnection connection = null;
                String source = viewHolder.imageURL;

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
                    viewHolder.bitmap = myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(connection!=null)connection.disconnect();
                }


            } catch (Exception e) {
                // TODO: handle exception
                Log.e("error", "Downloading Image Failed");
                viewHolder.bitmap = null;
            }

            return viewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder result) {
            // TODO Auto-generated method stub
            if (result.bitmap == null) {
            } else {
                result.imageView.setImageBitmap(result.bitmap);
                setImageViewCallback.callback();
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        Context context = parent.getContext();
        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.portfolio_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.thumb);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView thumbView = (ImageView) convertView.findViewById(R.id.thumb);
        TextView titleView = (TextView) convertView.findViewById(R.id.title) ;
        TextView clientView = (TextView) convertView.findViewById(R.id.client) ;
        TextView dateView = (TextView) convertView.findViewById(R.id.date) ;

        PortfolioViewItem portfolioViewItem = portfolioItemList.get(position);

        String source = "http://coscoi.net/" + portfolioViewItem.getThumb();
        Log.e("source : ", source);

        viewHolder.imageURL = source;
        new DownloadAsyncTask(new AsyncCallback() {
            @Override
            public void callback() {
                Log.e("img callback !!!!!!!!!!!!!", "cal !!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }).execute(viewHolder);


        // 아이템 내 각 위젯에 데이터 반영
        titleView.setText(portfolioViewItem.getTitle());
        clientView.setText(portfolioViewItem.getClient());
        dateView.setText(portfolioViewItem.getDate());

        return convertView;
    }

    public void addItem(String mainImageUrl, int biCateSubIdx, String clientName, String projectName, String detailImagesUrl) {
        PortfolioViewItem item = new PortfolioViewItem();

        item.setThumb(mainImageUrl);
        item.setBiCateSubIdx(biCateSubIdx);
        item.setClient(clientName);
        item.setProjectName(projectName);
        item.setBiCateSubIdx(biCateSubIdx);
        item.setDetailImages(detailImagesUrl);

        portfolioItemList.add(item);
    }

    public void addItem(PortfolioViewItem item) {
        portfolioItemList.add(item);
    }
}
