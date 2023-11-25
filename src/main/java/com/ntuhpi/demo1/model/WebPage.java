package com.ntuhpi.demo1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class WebPage {

    @Id
    private String id;
    private String url;
    @Indexed
    private String pageDump;

    private String title;
    private String fullPageDump;

    public WebPage() {
    }

    public WebPage(String id, String url, String pageDump, String title, String fullPageDump) {
        this.id = id;
        this.url = url;
        this.pageDump = pageDump;
        this.title = title;
        this.fullPageDump = fullPageDump;
    }

    public WebPage(String url, String pageDump, String fullPageDump, String title) {
        this.url = url;
        this.pageDump = pageDump;
        this.fullPageDump = fullPageDump;
        this.title = title;
    }

    public String getFullPageDump() {
        return fullPageDump;
    }

    public void setFullPageDump(String fullPageDump) {
        this.fullPageDump = fullPageDump;
    }


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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getPageDump() {
        return pageDump;
    }

    public void setPageDump(String pageDump) {
        this.pageDump = pageDump;
    }
}
