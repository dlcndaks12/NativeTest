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
public class NoticeViewAdapter extends BaseAdapter {

    private ArrayList<NoticeItemView> noticeItemList = new ArrayList<NoticeItemView>() ;

    public NoticeViewAdapter() {

    }

    @Override
    public int getCount() {
        return noticeItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeItemList.get(position);
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
            convertView = inflater.inflate(R.layout.notice_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView typeView = (TextView) convertView.findViewById(R.id.type) ;
        TextView titleView = (TextView) convertView.findViewById(R.id.title) ;
        TextView dateView = (TextView) convertView.findViewById(R.id.date) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        NoticeItemView noticeItem = noticeItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        typeView.setText(noticeItem.getType());
        titleView.setText(noticeItem.getTitle());
        dateView.setText(noticeItem.getDate());

        return convertView;
    }

    public void addItem(String type, String title, String date) {
        NoticeItemView item = new NoticeItemView();

        item.setType(type);
        item.setTitle(title);
        item.setDate(date);

        noticeItemList.add(item);
    }
}
