package com.almond.nativetest;

/**
 * Created by admin on 2016-06-29.
 */
public class BoardViewItem {
    private String type;
    private String title;
    private String date;
    private String contents;
    private int prIndex;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getPrIndex() {
        return prIndex;
    }

    public void setPrIndex(int prIndex) {
        this.prIndex = prIndex;
    }
}
