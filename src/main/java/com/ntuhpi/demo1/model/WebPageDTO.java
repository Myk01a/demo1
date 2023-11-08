package com.ntuhpi.demo1.model;

public class WebPageDTO {
    public WebPageDTO(String id, String url, String page, String fullPageDump) {
        this.id = id;
        this.url = url;
        this.page = page;
        this.fullPageDump = fullPageDump;
    }

    public WebPageDTO() {
    }

    private String id;
    private String url;
    private String page;
    private String fullPageDump;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getFullPageDump() {
        return fullPageDump;
    }

    public void setFullPageDump(String fullPageDump) {
        this.fullPageDump = fullPageDump;
    }
}

