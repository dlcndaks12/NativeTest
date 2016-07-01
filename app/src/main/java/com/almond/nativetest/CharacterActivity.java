package com.almond.nativetest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CharacterActivity extends MainActivity {

    private Spinner select;
    private SpinnerAdapter spinnerAdapter;
    private int findex = 0;
    private boolean loadingMore = false;
    private ListView portfolitList;
    private JSONArray jarr;
    private View footerView;
    private TextView empty;
    private PortfolioAdapter portfolioAdapter;
    private boolean endList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_character, frameLayout);

        select = (Spinner) findViewById(R.id.spinnerSub);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.character, R.layout.spinner_item);
        select.setAdapter(spinnerAdapter);
        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(view, position, "business");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        select.setSelection(0);

        /*  =============================================  */
        findex = 0;

        portfolioAdapter = new PortfolioAdapter();
        portfolitList = (ListView) findViewById(R.id.listPortfolio);
        portfolitList.setAdapter(portfolioAdapter);

        footerView = ((LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
        portfolitList.addFooterView(footerView);

        LinearLayout listfooter = (LinearLayout)findViewById(R.id.listfooter);
        empty = (TextView) listfooter.findViewById(R.id.empty);

        Thread thread =  new Thread(null, loadMoreListItems);
        thread.start();

        /* 아이템들 클릭했을때 */
        portfolitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PortfolioViewItem item = (PortfolioViewItem) parent.getItemAtPosition(position);

                int idx = item.getIdx();

                String url = "http://m.coscoi.net:8200/m/pr/noticeDetail.do?prIndex="+idx;

                Log.e("url == : ", url);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        /* 스크롤할때 */
        portfolitList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                String urlString = "http://52.78.64.186:8090/getPortfolio.do?biCateSubIdx=1&findex="+(findex*10);
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
                    portfolioAdapter.addItem(obj.getString("PR_PART"), obj.getString("SUBJECT"), obj.getString("REG_DATE"), obj.getInt("CONTENTS"), obj.getInt("PR_INDEX"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            portfolioAdapter.notifyDataSetChanged();
            loadingMore = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        findex = 0;
    }
}
