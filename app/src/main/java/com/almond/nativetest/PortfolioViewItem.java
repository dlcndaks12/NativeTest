package com.almond.nativetest;

/**
 * Created by admin on 2016-06-29.
 */
public class PortfolioViewItem {
    private String thumb;
    private String title;
    private String client;
    private String date;
    private int idx;
    private int biCateSubIdx;
    private String detailImages;
    private String projectName;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getBiCateSubIdx() {
        return biCateSubIdx;
    }

    public void setBiCateSubIdx(int biCateSubIdx) {
        this.biCateSubIdx = biCateSubIdx;
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
