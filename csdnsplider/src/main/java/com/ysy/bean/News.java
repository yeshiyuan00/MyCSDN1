package com.ysy.bean;

/**
 * Author: yeshiyuan
 * Date: 11/28/15.
 */
public class News {
    public static interface NewsType {
        public static final int TITLE = 1;
        public static final int SUMMARY = 2;
        public static final int CONTENT = 3;
        public static final int IMG = 4;
        public static final int BOLD_TITLE = 5;
    }

    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 内容
     */
    private String content;

    /**
     * 图片链接
     */
    private String imageLink;

    /**
     * 类型
     */
    private int type;

    public String getContent() {
        return content;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "News [title=" + title + ", summary=" + summary + ", " +
                "content=" + content + ", imageLink=" + imageLink
                + ", type=" + type + "]";
    }
}
