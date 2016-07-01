package com.almond.nativetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 2016-06-29.
 */
public class PortfolioAdapter extends BaseAdapter {

    private ArrayList<PortfolioViewItem> portfolioItemList = new ArrayList<PortfolioViewItem>() ;

    public PortfolioAdapter() {

    }

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.portfolio_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView thumbView = (ImageView) convertView.findViewById(R.id.thumb);
        TextView titleView = (TextView) convertView.findViewById(R.id.title) ;
        TextView clientView = (TextView) convertView.findViewById(R.id.client) ;
        TextView dateView = (TextView) convertView.findViewById(R.id.date) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        PortfolioViewItem portfolioViewItem = portfolioItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        /*thumbView.setImageResource();*/
        titleView.setText(portfolioViewItem.getTitle());
        clientView.setText(portfolioViewItem.getClient());
        dateView.setText(portfolioViewItem.getDate());

        return convertView;
    }

    public void addItem(String title, String client, String date, int idx, int biCateSubIdx) {
        PortfolioViewItem item = new PortfolioViewItem();

        item.setTitle(title);
        item.setClient(client);
        item.setDate(date);
        item.setIdx(idx);
        item.setBiCateSubIdx(biCateSubIdx);

        portfolioItemList.add(item);
    }
}
