package com.xhf.csdn.model;

public class ArticleRequest {
    private String url;

    private String articleId;

    private String token;
    /**
     * 0: 默认下载（保留 CSDN 默认内容）
     * 1: 图片链接下载（下载存储在 CSDN 的图片资源）
     * 2: 去除水印下载（自动裁剪图片）
     */
    private int downloadType;

    /**
     * 给我get and set
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(int downloadType) {
        this.downloadType = downloadType;
    }
}