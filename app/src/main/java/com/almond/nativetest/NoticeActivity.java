package com.almond.nativetest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class NoticeActivity extends MainActivity {
    private ListView noticeList;
    private NoticeViewAdapter noticeAdapter;
    private int count = 1;
    private boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_notice, frameLayout);

        noticeAdapter = new NoticeViewAdapter();
        noticeList = (ListView) findViewById(R.id.listNotice);
        noticeList.setAdapter(noticeAdapter);

        View footerView = ((LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
        noticeList.addFooterView(footerView);

        for (int i = 0; i < 10; i++) {
            noticeAdapter.addItem("공지", "제목입니다. " + count++, "2016. 05. 29");
        }

        /* 아이템들 클릭했을때 */
        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeItemView item = (NoticeItemView) parent.getItemAtPosition(position);

                String type = item.getType();
                String title = item.getTitle();
                String date = item.getDate();

                Toast.makeText(NoticeActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        /* 스크롤할때 */
        noticeList.setOnScrollListener(new AbsListView.OnScrollListener() {
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

            try { Thread.sleep(1000);
            } catch (InterruptedException e) {}

            runOnUiThread(returnRes);
        }
    };

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                noticeAdapter.addItem("공지", "제목입니다. " + count++, "2016. 05. 29");
            }
            noticeAdapter.notifyDataSetChanged();

            loadingMore = false;
        }
    };
}
