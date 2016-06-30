package com.almond.nativetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecruitActivity extends MainActivity {
    private ListView recruitList;
    private RecruitViewAdapter recruitAdapter;
    private int count = 1;
    private boolean loadingMore = false;
    private JSONArray jarr;
    private int findex = 0;
    private View footerView;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_recruit, frameLayout);

        findex = 0;

        recruitAdapter = new RecruitViewAdapter();
        recruitList = (ListView) findViewById(R.id.listRecruit);
        recruitList.setAdapter(recruitAdapter);

        footerView = ((LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
        recruitList.addFooterView(footerView);

        LinearLayout listfooter = (LinearLayout)findViewById(R.id.listfooter);
        empty = (TextView) listfooter.findViewById(R.id.empty);

        Thread thread =  new Thread(null, loadMoreListItems);
        thread.start();

        /* 아이템들 클릭했을때 */
        recruitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecruitItemView item = (RecruitItemView) parent.getItemAtPosition(position);

                String title = item.getTitle();
                String date = item.getDate();

                Toast.makeText(RecruitActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        /* 스크롤할때 */
        recruitList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.e("notice : ", "scroll ... firstVisibleItem + visible : " + (firstVisibleItem + visibleItemCount)  );
                Log.e("notice : ", "scroll ... total: " + totalItemCount );

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0 && !(loadingMore)) {
                    Thread thread =  new Thread(null, loadMoreListItems);
                    thread.start();
                }
            }
        });
    }

    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            loadingMore = true;

            try {
                String urlString = "http://192.168.0.186:8090/getRecruit.do?findex="+(findex*10);
                findex++;
                URL url = new URL(urlString);
                String result;

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int resCode = conn.getResponseCode();
                Log.e("connect : " , "resCode ===== " + resCode);

                InputStream instream = conn.getInputStream();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;

                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }

                JSONObject json = new JSONObject(responseStrBuilder.toString());
                jarr = new JSONArray(json.getString("items"));

                if(jarr.length() == 0) {
                    empty.setText("더이상 게시물이 없습니다.");
                    footerView.invalidate();
                }

            } catch (Exception e) {
                Log.e("err", e.toString());
            }

            try { Thread.sleep(1000);
            } catch (InterruptedException e) {}

            runOnUiThread(returnRes);
        }
    };

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < jarr.length(); i++) {
                try {

                    JSONObject obj = (JSONObject) jarr.get(i);
                    recruitAdapter.addItem(obj.getString("PR_PART"), obj.getString("SUBJECT"), obj.getString("REG_DATE"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recruitAdapter.notifyDataSetChanged();

            loadingMore = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        findex = 0;
    }
}
