package com.ntuhpi.demo1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class WebPage {

    @Id
    private String id;
    private String url;
    private String pageDump;

    public WebPage() {}

    public WebPage(String url, String pageDump) {
        this.url = url;
        this.pageDump = pageDump;
    }

    // Геттери і сеттери для полів id, url і pageDump

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPageDump() {
        return pageDump;
    }

    public void setPageDump(String pageDump) {
        this.pageDump = pageDump;
    }
}