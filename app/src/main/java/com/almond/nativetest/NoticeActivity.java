package com.almond.nativetest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

public class NoticeActivity extends MainActivity {
    private ListView noticeList;
    private NoticeViewAdapter noticeAdapter;
    private boolean loadingMore = false;
    private JSONArray jarr;
    private int findex = 0;
    private View footerView;
    private TextView empty;
    private boolean endList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_notice, frameLayout);

        findex = 0;

        noticeAdapter = new NoticeViewAdapter();
        noticeList = (ListView) findViewById(R.id.listNotice);
        noticeList.setAdapter(noticeAdapter);

        footerView = ((LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
        noticeList.addFooterView(footerView);

        LinearLayout listfooter = (LinearLayout)findViewById(R.id.listfooter);
        empty = (TextView) listfooter.findViewById(R.id.empty);

        /* 아이템들 클릭했을때 */
        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoardViewItem item = (BoardViewItem) parent.getItemAtPosition(position);

                String type = item.getType();
                String title = item.getTitle();
                String date = item.getDate();
                String contents = item.getContents();

                Log.e("콘텐츠", "Contents : " + contents);

                Intent intent = new Intent(NoticeActivity.this, DetailViewActivity.class);
                intent.putExtra("heading", "NOTICE");
                intent.putExtra("type", type);
                intent.putExtra("title", title);
                intent.putExtra("date", date);
                intent.putExtra("contents", contents);
                startActivity(intent);
            }
        });

        /* 스크롤할때 */
        noticeList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!endList) {
                    if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0 && !(loadingMore)) {
                        Thread thread =  new Thread(null, loadMoreListItems);
                        thread.start();
                    }
                }
            }
        });
    }

    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            loadingMore = true;
            InputStream instream = null;
            BufferedReader streamReader = null;

            try {
                String urlString = "http://52.78.64.186:8090/getNotice.do?findex="+(findex*10);
                findex++;
                URL url = new URL(urlString);
                String result;

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int resCode = conn.getResponseCode();
                Log.e("connect : " , "resCode ===== " + resCode);

                instream = conn.getInputStream();

                streamReader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;

                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }

                JSONObject json = new JSONObject(responseStrBuilder.toString());
                jarr = new JSONArray(json.getString("items"));

                /* 게시글이 더이상 없을때 */
                if(jarr.length() == 0) {
                    Message msg = footerHandler.obtainMessage();
                    footerHandler.sendMessage(msg);
                    endList = true;
                }

            } catch (Exception e) {
                Log.e("err", e.toString());
            } finally {
                try {
                    if(instream != null) instream.close();
                    if(streamReader != null) streamReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(returnRes);
        }
    };

    final Handler footerHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.e("더이상 게시글이 없습니다", "더이상 게시글이 없습니다");
            empty.setText("더이상 게시물이 없습니다.");
        }
    };

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < jarr.length(); i++) {
                try {

                    JSONObject obj = (JSONObject) jarr.get(i);
                    noticeAdapter.addItem(obj.getString("PR_PART"), obj.getString("SUBJECT"), obj.getString("REG_DATE"), obj.getString("CONTENTS"), obj.getInt("PR_INDEX"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            noticeAdapter.notifyDataSetChanged();

            loadingMore = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        findex = 0;
    }
}
