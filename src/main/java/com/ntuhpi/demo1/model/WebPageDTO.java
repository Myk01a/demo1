package com.ntuhpi.demo1.model;

public class WebPageDTO {
    public WebPageDTO(String id, String url, String page, String fullPageDumpUrl) {
        this.id = id;
        this.url = url;
        this.page = page;
        this.fullPageDumpUrl = fullPageDumpUrl;
    }

    public WebPageDTO() {
    }

    private String id;
    private String url;
    private String page; // Частина pageDump, яка містить пошуковий запит
    private String fullPageDumpUrl; // Посилання на повний pageDump

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

    public String getFullPageDumpUrl() {
        return fullPageDumpUrl;
    }

    public void setFullPageDumpUrl(String fullPageDumpUrl) {
        this.fullPageDumpUrl = fullPageDumpUrl;
    }
// Конструктори, геттери та сеттери
}

