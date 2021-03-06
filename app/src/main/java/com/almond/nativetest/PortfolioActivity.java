package com.almond.nativetest;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

public class PortfolioActivity extends MainActivity {

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
    private Context mContext;
    private int biCateSubIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_character, frameLayout);

        mContext = this;

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            biCateSubIdx = intent.getExtras().getInt("biCateSubIdx");
        } else {
            biCateSubIdx = 1;
        }

        select = (Spinner) findViewById(R.id.spinnerSub);

        if (biCateSubIdx < 4) {
            spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.character, R.layout.spinner_item);
            select.setAdapter(spinnerAdapter);
            select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("==================================", "============================ call E = : " + position);

                    switch (position) {
                        case 0:
                            biCateSubIdx = 1;
                            break;
                        case 1:
                            biCateSubIdx = 2;
                            break;
                        case 2:
                            biCateSubIdx = 3;
                            break;
                        default:
                            break;
                    }

                    findex = 0;
                    portfolioAdapter.clearAdapter();

                    Thread thread =  new Thread(null, loadMoreListItems);
                    thread.start();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.web, R.layout.spinner_item);
            select.setAdapter(spinnerAdapter);
            select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("==================================", "============================ call E = : " + position);

                    switch (position) {
                        case 0:
                            biCateSubIdx = 4;
                            break;
                        case 1:
                            biCateSubIdx = 5;
                            break;
                        case 2:
                            biCateSubIdx = 6;
                            break;
                        default:
                            break;
                    }

                    findex = 0;
                    portfolioAdapter.clearAdapter();

                    Thread thread =  new Thread(null, loadMoreListItems);
                    thread.start();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        //select.setSelection(biCateSubIdx-1);

        findex = 0;

        portfolioAdapter = new PortfolioAdapter();
        portfolitList = (ListView) findViewById(R.id.listPortfolio);
        portfolitList.setDivider(null);
        portfolitList.setAdapter(portfolioAdapter);

        footerView = ((LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
        portfolitList.addFooterView(footerView);

        LinearLayout listfooter = (LinearLayout)findViewById(R.id.listfooter);
        empty = (TextView) listfooter.findViewById(R.id.empty);

        /* 아이템들 클릭했을때 */
        portfolitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PortfolioViewItem item = (PortfolioViewItem) parent.getItemAtPosition(position);

                String title = item.getTitle();
                String client = item.getClient();
                String date = item.getDate();
                String detailImages = item.getDetailImages();

                Log.e("콘텐츠", "Contents : " + detailImages);

                Intent intent = new Intent(PortfolioActivity.this, PortfolioDetailActivity.class);
                intent.putExtra("heading", "BUSINESS CHARACTER & MD");
                intent.putExtra("title", title);
                intent.putExtra("client", client);
                intent.putExtra("date", date);
                intent.putExtra("detailImages", detailImages);
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
                    Log.e("notice : ", "scroll ... firstVisibleItem + visible : " + (firstVisibleItem + visibleItemCount)  );
                    Log.e("notice : ", "scroll ... total: " + totalItemCount );

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
                String urlString = "http://52.78.64.186:8090/getPortfolio.do?biCateSubIdx="+biCateSubIdx+"&findex="+(findex*5);
                Log.e("url : ", urlString);
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

                    PortfolioViewItem item = new PortfolioViewItem();
                    item.setThumb(obj.getString("MAIN_IMAGE_URL"));
                    item.setTitle(obj.getString("SUMMARY"));
                    item.setClient(obj.getString("CLIENT_NAME"));
                    item.setProjectName(obj.getString("PROJECT_NAME"));
                    item.setDetailImages(obj.getString("DETAIL_IMAGES_URL"));
                    item.setBiCateSubIdx(obj.getInt("BI_CATE_SUB_IDX"));
                    item.setDate(obj.getString("PROJECT_DATE"));
                    item.setIdx(obj.getInt("IDX"));

                    /*portfolioAdapter.addItem(obj.getString("MAIN_IMAGE_URL"), obj.getInt("BI_CATE_SUB_IDX"), obj.getString("CLIENT_NAME"), obj.getString("PROJECT_NAME"), obj.getString("DETAIL_IMAGES_URL"));*/

                    portfolioAdapter.addItem(item);

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
